package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.AppWhiteList;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class AppWhiteListServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(AppWhiteListServiceTest.class);

    @Autowired
    private AppWhiteListService appWhiteListService;

//    @Before
//    public void setUp() throws Exception {
//        LoginInfo loginInfo = new LoginInfo(24, "test");
//        ThreadLocalUtils.USER_THREAD_LOCAL.set(loginInfo);
//    }

    @Test
    public void checkBatchName() throws Exception {
        ServiceResult result = appWhiteListService.checkBatchName("test");
        if (result.getData() != null) {
            if (result.getData() != null) {
                logger.info(result.getData().toString());
            }
        }
    }

    @Test
    public void getAppWhiteListPageByIpOrPhone() throws Exception {
        String type = "phone";
        String query = "15670950484";

        EntityWrapper<AppWhiteList> wrapper = new EntityWrapper<>();
        wrapper.andNew().eq("del_flag", 0);
        wrapper.orderBy("created_time", false);
        if (StringUtils.isNotBlank(type)) {
            wrapper.andNew().eq("white_type", type);
        }
        if (StringUtils.isNotBlank(query)) {
            wrapper.andNew().eq("white_value", query);
        }

        ServiceResult result = appWhiteListService.getAppWhiteListPageByIpOrPhone(1, 10, wrapper);
        if (result.getData() != null) {
            if (result.getData() != null) {
                logger.info(result.getData().toString());
            }
        }
    }

    @Test
    public void createOne() throws Exception {
        AppWhiteList appWhiteList = new AppWhiteList();
        appWhiteList.setWhiteValue("10.2.3.2");
        appWhiteList.setBatchId(1);

        ServiceResult result = appWhiteListService.createOne(appWhiteList);
        if (result.getData() != null) {
            if (result.getData() != null) {
                logger.info(result.getData().toString());
            }
        }
    }

    @Test
    public void createOneByBatch() throws Exception {
        AppWhiteList appWhiteList = new AppWhiteList();
        appWhiteList.setWhiteValue("10.2.3.2");
        appWhiteList.setBatchId(1);
        appWhiteList.setBatchName("test");
        appWhiteList.setWhiteType(AppWhiteList.WhiteType.ip.name());

        ServiceResult result = appWhiteListService.createOneByBatch(appWhiteList);
        if (result.getData() != null) {
            if (result.getData() != null) {
                logger.info(result.getData().toString());
            }
        }
    }

    @Test
    public void createBatch() throws Exception {
        AppWhiteList appWhiteList = new AppWhiteList();
        appWhiteList.setBatchName("test");
        appWhiteList.setWhiteType(AppWhiteList.WhiteType.ip.name());

        ServiceResult result = appWhiteListService.createBatch(appWhiteList);
        if (result.getData() != null) {
            if (result.getData() != null) {
                logger.info(result.getData().toString());
            }
        }
    }

    @Test
    public void getAppWhiteBatchPage() throws Exception {
        String batchName = "test";
        String operatorId = "b9e980c1495e4d0582c257901d86b4ff";

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

        ServiceResult result = appWhiteListService.getAppWhiteBatchPage(1, 10, wrapper);
        if (result.getData() != null) {
            if (result.getData() != null) {
                logger.info(result.getData().toString());
            }
        }
    }

    @Test
    public void getAppWhiteListByBatch() throws Exception {
        Integer batchId = 87;
        ServiceResult result = appWhiteListService.getAppWhiteListByBatch(batchId);
        if (result.getData() != null) {
            if (result.getData() != null) {
                logger.info(result.getData().toString());
            }
        }
    }

    @Test
    public void getAppWhiteListPageByBatch() throws Exception {
        Integer batchId = 77;
        String query = "15670950484";
        EntityWrapper<AppWhiteList> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotBlank(query)) {
            wrapper.andNew().eq("white_value", query);
        }

        ServiceResult result = appWhiteListService.getAppWhiteListPageByBatch(1, 10, batchId, wrapper);
        if (result.getData() != null) {
            if (result.getData() != null) {
                logger.info(result.getData().toString());
            }
        }
    }

    @Test
    public void deleteOne() throws Exception {
        Integer id = 92;
        ServiceResult result = appWhiteListService.deleteOne(id);
        if (result.getData() != null) {
            if (result.getData() != null) {
                logger.info(result.getData().toString());
            }
        }
    }

    @Test
    public void deleteOneForever() throws Exception {
        Integer id = 92;
        ServiceResult result = appWhiteListService.deleteOneForever(id);
        if (result.getData() != null) {
            if (result.getData() != null) {
                logger.info(result.getData().toString());
            }
        }
    }

    @Test
    public void deleteBatch() throws Exception {
        Integer batchId = 91;
        ServiceResult result = appWhiteListService.deleteBatch(batchId);
        if (result.getData() != null) {
            if (result.getData() != null) {
                logger.info(result.getData().toString());
            }
        }
    }

    @Test
    public void deleteBatchForever() throws Exception {
        Integer batchId = 91;
        ServiceResult result = appWhiteListService.deleteOneForever(batchId);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

}