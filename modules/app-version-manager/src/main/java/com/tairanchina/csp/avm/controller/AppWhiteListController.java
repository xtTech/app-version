package com.tairanchina.csp.avm.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.AppWhiteList;
import com.tairanchina.csp.avm.service.AppWhiteListService;
import com.tairanchina.csp.avm.utils.ExcelUtil;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/white")
public class AppWhiteListController {

    private static final Logger logger = LoggerFactory.getLogger(AppWhiteListController.class);

    @Autowired
    AppWhiteListService appWhiteListService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/templet/{type}")
    public Object generateTemplet(@NotBlank @PathVariable String type) {
        String typeRegex = "^ip|phone$";
        if (!type.matches(typeRegex)) {
            return ServiceResultConstants.PARAMS_TYPE_ERROR;
        }
        try {
            if ("phone".equals(type)) {
                type = "手机号";
            }
            String[] title = {type + "白名单"};
            HSSFWorkbook workbook = ExcelUtil.getHSSFWorkbook("白名单批量添加模板", title, null, null);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();

            byte[] buffer = outputStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(type + "白名单批量添加模板.xls", "UTF-8"));
            headers.add("Content-type", "application/octet-stream;charset=UTF-8");
            headers.add("Cache-Control", "no-cache");
            Resource resource = new InputStreamResource(new ByteArrayInputStream(buffer));

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/x-msdownload"))
                    .body(resource);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ServiceResult.failed(40001, "白名单模板下载失败");
        }
    }


    /**
     * 批量新增白名单
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PostMapping("/add_batch")
//    @OperationRecord(type = OperationRecordLog.OperationType.CREATE, resource = OperationRecordLog.OperationResource.APP_WHITE_LIST_BATCH, description = OperationRecordLog.OperationDescription.CREATE_APP_WHITE_LIST_BATCH)
    public ServiceResult batchUpdate(@RequestParam("batchName") String batchName, @RequestParam("type") String type, @RequestParam("file") MultipartFile file) {
        String typeRegex = "^ip|phone$";
        if (!type.matches(typeRegex)) {
            return ServiceResultConstants.PARAMS_TYPE_ERROR;
        }
        //白名单名称支持英文数字和汉字，50个字符，不能为空
        String regex = "^[a-zA-Z0-9\u4E00-\u9FA5]{1,50}";
        if (!batchName.matches(regex)) {
            return ServiceResultConstants.PARAMS_TYPE_ERROR;
        }
        //判断白名单名称是否已存在
        ServiceResult check = checkBatchNameExisit(batchName);
        if (null == check.getData()) {
            return check;
        }
        if (null != check.getData() && (Boolean) check.getData()) {
            return ServiceResultConstants.WHITE_LIST_BATCH_EXISTS;
        }
        //创建白名单组
        AppWhiteList batch = new AppWhiteList();
        batch.setBatchName(batchName);
        batch.setWhiteType(type);
        ServiceResult createBatchResult = appWhiteListService.createBatch(batch);
        AppWhiteList appWhiteListResult = (AppWhiteList) createBatchResult.getData();
        //构造单个白名单
        AppWhiteList one = new AppWhiteList();
        one.setBatchId(appWhiteListResult.getId());
        one.setWhiteType(batch.getWhiteType());
        one.setBatchName(batch.getBatchName());

        //xls中白名单值类型不匹配的列表
        List<String> failureList = new ArrayList<>();
        String valueRegexp = "";
        if ("phone".equals(type)) {
            valueRegexp = "^1[3|4|5|6|7|8|9]\\d{9}$";
        }
        if ("ip".equals(type)) {
            valueRegexp = "^((?:,\\s*)?\\d{1,3}(?:\\.\\d{1,3}){3})+$";
        }
        // IO流读取文件
        InputStream input = null;
        HSSFWorkbook wb = null;
        try {
            //遍历xls
            input = file.getInputStream();
            // 创建文档
            wb = new HSSFWorkbook(input);
            input.close();
            //读取第一页sheet
            HSSFSheet hssfSheet = wb.getSheetAt(0);
            int totalRows = hssfSheet.getLastRowNum();
            //读取Row,从第二行开始
            for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    //读取第一列
                    HSSFCell cell = hssfRow.getCell(0);
                    if (cell == null) {
                        continue;
                    }
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    //校验白名单的值是否符合当前组的类型,如不符合，则将不符合内容返回
                    String whiteValue = cell.toString();
                    if (StringUtils.isNotBlank(whiteValue) && whiteValue.matches(valueRegexp)) {
                        one.setWhiteValue(whiteValue);
                        appWhiteListService.createOneByBatch(one);
                    } else {
                        failureList.add(whiteValue);
                    }
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return ServiceResultConstants.ERROR_500;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("batchName", batchName);
        result.put("failureList", failureList);
        return ServiceResult.ok(result);
    }

    /**
     * 校验【白名单组】的【名称】是否已存在
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/check_name/{name}")
    public ServiceResult checkBatchNameExisit(@PathVariable("name") String batchName) {
        ServiceResult result = appWhiteListService.checkBatchName(batchName);
        List list = (List) result.getData();
        Boolean isExisit = false;
        if (list.isEmpty()) {
            isExisit = false;
        } else if (list.size() == 1) {
            isExisit = true;
        }
        if (list.size() > 1) {
            return ServiceResultConstants.WHITE_LIST_BATCH_ERROR;
        }
        return ServiceResult.ok(isExisit);
    }

    /**
     * 分页显示【白名单组】列表
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/batch")
    public ServiceResult getBatch(@RequestParam(required = false, defaultValue = "1") int page,
                                  @RequestParam(required = false, defaultValue = "10") int pageSize,
                                  @RequestParam(required = false) String batchName,
                                  @RequestParam(required = false) String operatorId) {
        EntityWrapper<AppWhiteList> wrapper = new EntityWrapper<>();
        wrapper.and().eq("app_id", ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        if (StringUtils.isNotBlank(batchName)) {
            wrapper.andNew().like("batch_name", "%" + batchName + "%");
        }
        if (StringUtils.isNotBlank(operatorId)) {
            wrapper.andNew().eq("created_by", operatorId);
        }
        wrapper.andNew().eq("del_flag", 0);
        wrapper.orderBy("created_time", false);
        return appWhiteListService.getAppWhiteBatchPage(page, pageSize, wrapper);
    }

    /**
     * 显示batch内【白名单】内容
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/{batchId}/list")
    public ServiceResult getWhiteListByBatch(@PathVariable Integer batchId) {
        return appWhiteListService.getAppWhiteListByBatch(batchId);
    }

    /**
     * 分页显示batch内【白名单】内容
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/{batchId}/page")
    public ServiceResult getWhiteListPageByBatch(@RequestParam(required = false, defaultValue = "1") int page,
                                                 @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                 @PathVariable Integer batchId,
                                                 @RequestParam(required = false) String query) {
        EntityWrapper<AppWhiteList> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotBlank(query)) {
            wrapper.andNew().eq("white_value", query);
        }
        return appWhiteListService.getAppWhiteListPageByBatch(page, pageSize, batchId, wrapper);
    }

    /**
     * 查询白名单所在的哪些组
     * （当前APP下所有白名单列表分页）
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/page")
    public ServiceResult getWhiteListBatchByQuery(@RequestParam(required = false, defaultValue = "1") int page,
                                                  @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                  @RequestParam(required = false) String type,
                                                  @RequestParam(required = false) String query) {
        EntityWrapper<AppWhiteList> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotBlank(type)) {
            wrapper.andNew().eq("white_type", type);
        }
        if (StringUtils.isNotBlank(query)) {
            wrapper.andNew().like("white_value", "%" + query + "%");
        }
        wrapper.andNew().eq("del_flag", 0);
        wrapper.orderBy("created_time", false);

        return appWhiteListService.getAppWhiteListPageByIpOrPhone(page, pageSize, wrapper);
    }

    /**
     * 单个删除白名单
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @DeleteMapping("/{id}")
//    @OperationRecord(type = OperationRecordLog.OperationType.DELETE, resource = OperationRecordLog.OperationResource.APP_WHITE_LIST_ONE, description = OperationRecordLog.OperationDescription.DELETE_APP_WHITE_LIST_ONE)
    public ServiceResult deleteOne(@PathVariable Integer id) {
        return appWhiteListService.deleteOne(id);
    }


    /**
     * 删除白名单组
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @DeleteMapping("/{batchId}/delete")
//    @OperationRecord(type = OperationRecordLog.OperationType.DELETE, resource = OperationRecordLog.OperationResource.APP_WHITE_LIST_BATCH, description = OperationRecordLog.OperationDescription.DELETE_APP_WHITE_LIST_BATCH)
    public ServiceResult deleteBatch(@PathVariable Integer batchId) {
        return appWhiteListService.deleteBatch(batchId);
    }


    /**
     * 手动创建白名单组
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PostMapping("/add/batch")
//    @OperationRecord(type = OperationRecordLog.OperationType.CREATE, resource = OperationRecordLog.OperationResource.APP_WHITE_LIST_BATCH, description = OperationRecordLog.OperationDescription.CREATE_APP_WHITE_LIST_BATCH)
    public ServiceResult createBatch(@RequestBody AppWhiteList appWhiteBatch) {
        if (StringUtils.isBlank(appWhiteBatch.getBatchName()) || StringUtils.isBlank(appWhiteBatch.getWhiteType())) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        ServiceResult check = checkBatchNameExisit(appWhiteBatch.getBatchName());
        if (null == check.getData()) {
            return check;
        }
        if (null != check.getData() && (Boolean) check.getData()) {
            return ServiceResultConstants.WHITE_LIST_BATCH_EXISTS;
        }
        return appWhiteListService.createBatch(appWhiteBatch);
    }

    /**
     * 单个添加白名单，需要batchID
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PostMapping("/add/one")
//    @OperationRecord(type = OperationRecordLog.OperationType.CREATE, resource = OperationRecordLog.OperationResource.APP_WHITE_LIST_ONE, description = OperationRecordLog.OperationDescription.CREATE_APP_WHITE_LIST_ONE)
    public ServiceResult createOne(@Valid @RequestBody AppWhiteList appWhiteList) {
        if (null == appWhiteList.getBatchId() || StringUtils.isNotBlank(appWhiteList.getBatchName())) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return appWhiteListService.createOne(appWhiteList);
    }


    /**
     * 导出一组白名单
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/export/{batchId}")
    public Object exportBatch(@NotBlank @PathVariable Integer batchId) {
        try {
            ServiceResult result = appWhiteListService.getAppWhiteListByBatch(batchId);
            if (null == result.getData()) {
                return ServiceResult.failed(10001, "导出内容为空");
            }
            List<AppWhiteList> batch = (List<AppWhiteList>) result.getData();
            String batchName = null;
            String[][] values = new String[batch.size()][1];
            if (!batch.isEmpty()) {
                for (int row = 0; row < batch.size(); row++) {
                    AppWhiteList one = batch.get(row);
                    values[row][0] = one.getWhiteValue();
                }
                batchName = batch.get(0).getBatchName();
            }
            String[] title = {"白名单-" + batchName};
            HSSFWorkbook workbook = ExcelUtil.getHSSFWorkbook("白名单", title, values, null);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();

            byte[] buffer = outputStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode("APP白名单-" + batchName + ".xls", "UTF-8"));
            headers.add("Content-type", "application/octet-stream;charset=UTF-8");
            headers.add("Cache-Control", "no-cache");
            Resource resource = new InputStreamResource(new ByteArrayInputStream(buffer));

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/x-msdownload"))
                    .body(resource);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ServiceResult.failed(40001, "白名单组导出失败");
        }
    }

}
