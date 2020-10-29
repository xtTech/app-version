package com.tairanchina.csp.avm.controller;

import cn.hutool.core.io.FileUtil;
import com.dingtalk.chatbot.FileResult;
import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.dto.UploadFileEntity;
import com.tairanchina.csp.avm.entity.Apk;
import com.tairanchina.csp.avm.entity.Channel;
import com.tairanchina.csp.avm.entity.OperationRecordLog;
import com.tairanchina.csp.avm.annotation.OperationRecord;
import com.tairanchina.csp.avm.mapper.ApkMapper;
import com.tairanchina.csp.avm.mapper.ChannelMapper;
import com.tairanchina.csp.avm.service.ApkService;
import com.tairanchina.csp.avm.service.ChannelService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;


/****
 * Created by hzlizx on 2018/6/13 0013
 ****/
@RestController
@RequestMapping("/apk")
public class ApkController {
    private static final Logger logger = LoggerFactory.getLogger(ApkController.class);

    @Autowired
    private ApkService apkService;

    @Autowired
    private ApkMapper apkMapper;

    @Autowired
    private ChannelService channelService;

    @Value("${is_use_project_path}")
    private boolean is_use_project_path;

    @Value("${uploadFilePath}")
    private String uploadFilePath;

    @Value("${downloadFileUrl}")
    private String downloadFileUrl;

    @Value("${defaultUploadFilePath}")
    private String defaultUploadFilePath; //默认的上传文件夹地址

    @Value("${defaultDownloadFileContextPath}")
    private String defaultDownloadFileContextPath; //默认的下载文件地址

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    public ServiceResult upload(@RequestParam(value = "file", required = true) MultipartFile file,@RequestParam(required = true, value = "channelCode")String channelCode,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        System.out.print("file="+file);
        try {
             Channel channel=this.channelService.findChannelByChannelCode(channelCode);
             String disk_uploadFilePath=channel.getUploadFolder();
             String destFilePath="";
             //String serverUrl=httpServletRequest.getScheme()+"://"+httpServletRequest.getServerName()+":"+httpServletRequest.getServerPort()+"/";
             String downloadApkUrl="";
             if(is_use_project_path){ //使用项目中编译目录文件夹
                destFilePath= ResourceUtils.getURL("classpath:").getPath()+uploadFilePath + "\\" + channel.getChannelCode()+  "\\"+file.getOriginalFilename();
                downloadApkUrl=httpServletRequest.getContextPath()+"/"+disk_uploadFilePath+"/"+file.getOriginalFilename();
             }else{ //使用外置项目文件夹中的目录
                destFilePath= defaultUploadFilePath+"\\"+ disk_uploadFilePath+"\\"+file.getOriginalFilename();
                downloadApkUrl=httpServletRequest.getContextPath()+"/"+defaultDownloadFileContextPath+"/"+disk_uploadFilePath+"/"+file.getOriginalFilename();
             }
             File dest=new File(destFilePath);
             File uploadFile = FileUtil.writeFromStream(file.getInputStream(),dest);
             FileResult fileResult = new FileResult();
             fileResult.setFileName(uploadFile.getName());
             fileResult.setFilePath(uploadFile.getAbsolutePath());
             fileResult.setFileSize(uploadFile.getTotalSpace());
             fileResult.setRequestUrl(downloadFileUrl + "/" + channel.getAppId() + "/" + channel.getChannelCode() +"/" + file.getOriginalFilename());
             fileResult.setRequestApkUrl(downloadApkUrl);
             return ServiceResult.ok(fileResult);
        } catch (FileNotFoundException e){
             e.printStackTrace();
        } catch (IOException e) {
             e.printStackTrace();
        }
        return ServiceResult.failed();
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
        @ApiImplicitParam(name="appId", value = "appId", paramType = "path", dataType = "string", required = true),
        @ApiImplicitParam(name="channelCode",value = "渠道码", paramType = "path", dataType = "string", required = true),
        @ApiImplicitParam(name="fileName",value = "文件名", paramType = "path", dataType = "string", required = true)
    })
    @GetMapping(value = "/downloadFile/{appId}/{channelCode}/{fileName}")
    public ServiceResult downloadFile(@PathVariable(required = true)  Integer appId,@PathVariable(required = true)  String channelCode,@PathVariable(required = true)  String fileName,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
        //如果文件名不为空，则进行下载
        if(!StringUtils.isEmpty(fileName)){
           Channel channel=this.channelService.findChannelByChannelCode(appId,channelCode);
           String disk_uploadFilePath=channel.getUploadFolder();

           System.out.print("fileName="+fileName);
           String downloadFileName=fileName+".apk";
           String downloadFilePath= null;
           try {
               if(is_use_project_path){ //使用项目中编译目录文件夹
                  downloadFilePath = ResourceUtils.getURL("classpath:").getPath() + uploadFilePath + "\\" + channel.getChannelCode()+ "\\" + downloadFileName;
               }else{
                  downloadFilePath= defaultUploadFilePath+"\\"+ disk_uploadFilePath+"\\"+downloadFileName;
               }
           } catch (FileNotFoundException e) {
              e.printStackTrace();
           }
           File downloadFile=new File(downloadFilePath);
           //如果文件存在，则进行下载
           if(downloadFile.exists()){
               // 实现文件下载
               FileInputStream fis = null;
               BufferedInputStream bis = null;
               try {
                   fis = new FileInputStream(downloadFile);
                   bis = new BufferedInputStream(fis);
                   OutputStream os = httpServletResponse.getOutputStream();
                   try{
                       // 设置文件大小
                       // 清空response
                       httpServletResponse.reset();
                       // 配置文件下载
                       httpServletResponse.setContentType("application/octet-stream");
                       //写明要下载的文件的大小
                       httpServletResponse.setContentLength((int)downloadFile.length());
                       // 下载文件能正常显示中文
                       httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadFileName,"UTF-8"));
                   } catch (UnsupportedEncodingException e){
                       System.out.println("Download  failed!");
                       return ServiceResult.failed();
                   }

                   byte [] b=new byte[1024];//相当于我们的缓存
                   long k=0;//该值用于计算当前实际下载了多少字节
                   //开始循环下载
                   while(k<downloadFile.length()){
                       int j=bis.read(b,0,1024);
                       k+=j;
                       //将b中的数据写到客户端的内存
                       os.write(b,0,j);
                   }

                   os.flush();
                   os.close();
                   if (bis != null) {
                       bis.close();
                   }
                   if (fis != null) {
                       fis.close();
                   }
                   System.out.println("Download  successfully!");
                   return ServiceResult.ok();
               } catch (IOException e) {
                   System.out.println("Download  failed!");
                   return ServiceResult.failed();
               }
           }
        }
        return ServiceResult.failed();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数据条数", defaultValue = "10"),
            @ApiImplicitParam(name = "versionId", value = "版本id"),
            @ApiImplicitParam(name = "channelCode", value = "渠道码"),
            @ApiImplicitParam(name = "md5", value = "md5"),
            @ApiImplicitParam(name = "deliveryStatus", value = "上架状态，0-未上架；1-已上架"),
    })
    @GetMapping
    public ServiceResult list(@RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "10") int pageSize,
                              @RequestParam(defaultValue = "0") int versionId,
                              @RequestParam(required = false, defaultValue = "") String channelCode,
                              @RequestParam(required = false, defaultValue = "") String md5,
                              @RequestParam(required = false, defaultValue = "") Integer deliveryStatus) {
        if (versionId < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
            return apkService.getApkPageWithChannelCode(page, pageSize, versionId, channelCode, md5, deliveryStatus);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PostMapping
    @RequestMapping(produces = "application/json;charset=UTF-8", consumes = "application/json;charset=UTF-8")
    @OperationRecord(type = OperationRecordLog.OperationType.CREATE, resource = OperationRecordLog.OperationResource.APK, description = OperationRecordLog.OperationDescription.CREATE_APK,content="添加APK包")
    public ServiceResult create(@RequestBody UploadFileEntity uploadFileEntity) {
        Apk apk = new Apk();
        ServiceResult serviceResult = channelService.findByChannelCode(uploadFileEntity.getChannel());
        if (serviceResult.getCode() == 200) {
            Channel channel = (Channel) serviceResult.getRecord();
            apk.setChannelId(channel.getId());
            apk.setVersionId(uploadFileEntity.getVersionId());
            apk.setApkName(uploadFileEntity.getApkName());
            apk.setMd5(uploadFileEntity.getMd5());
            apk.setOssUrl(uploadFileEntity.getOssUrl());
            apk.setDownloadUrl(uploadFileEntity.getDownloadUrl());
            apk.setDownloadApkUrl(uploadFileEntity.getDownloadApkUrl());
            return apkService.create(apk);
        } else {
            logger.info("找不到Channel");
            return ServiceResult.failed(
                    ServiceResultConstants.APK_SAVE_ERROR.getCode(),
                    "文件[ " + uploadFileEntity.getFileName() + " ]录入失败，原因：" + serviceResult.getInfo()
            );
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PutMapping("/{id}/delivery")
    @OperationRecord(type = OperationRecordLog.OperationType.DELIVERY, resource = OperationRecordLog.OperationResource.APK, description = OperationRecordLog.OperationDescription.DELIVERY_APK,content="APK上架")
    public ServiceResult delivery(@PathVariable int id) {
        if (id < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return apkService.delivery(id);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PutMapping("/{id}/undelivery")
    @OperationRecord(type = OperationRecordLog.OperationType.UNDELIVERY, resource = OperationRecordLog.OperationResource.APK, description = OperationRecordLog.OperationDescription.UNDELIVERY_APK,content="APK下架")
    public ServiceResult undelivery(@PathVariable int id) {
        if (id < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return apkService.undelivery(id);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @DeleteMapping("/{id}")
    @OperationRecord(type = OperationRecordLog.OperationType.DELETE, resource = OperationRecordLog.OperationResource.APK, description = OperationRecordLog.OperationDescription.DELETE_APK,content="删除APK包")
    public ServiceResult delete(@PathVariable int id) {
        if (id < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        Apk apk=apkMapper.selectById(id); //选中apk
        if(apk!=null){
            Integer channelId=apk.getChannelId(); //获取渠道ID
            Channel channel=this.channelService.findChannelByChannelId(channelId);
            String disk_uploadFilePath=channel.getUploadFolder();

            String fullUploadFilePath= null;
            try {
              if(is_use_project_path){ //使用项目中编译目录文件夹
                 fullUploadFilePath = ResourceUtils.getURL("classpath:").getPath()+uploadFilePath + "\\" + channel.getChannelCode()+ "\\" + apk.getApkName();
              }else{
                 fullUploadFilePath= defaultUploadFilePath+"\\"+ disk_uploadFilePath+"\\"+apk.getApkName();
              }
              File delFile=new File(fullUploadFilePath);
              FileUtil.del(delFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        ServiceResult serviceResult=apkService.delete(id); //先删除文件
        return serviceResult;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/channel/check")
    public ServiceResult checkChannel(@RequestParam String channelCode) {
        if (StringUtils.isEmpty(channelCode)) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        ServiceResult channel = channelService.findByChannelCode(channelCode);
        if (channel.getCode() == 200) {
            return ServiceResult.ok(true);
        } else {
            return ServiceResult.ok(false);
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/exists")
    public ServiceResult exists(@RequestParam String channelCode,
                                @RequestParam Integer versionId) {
        if (StringUtils.isEmpty(channelCode) || versionId == null || versionId < 0) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return apkService.exists(channelCode, versionId);
    }
}
