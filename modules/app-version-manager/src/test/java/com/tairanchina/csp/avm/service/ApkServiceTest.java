package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.dto.UploadFileEntity;
import com.tairanchina.csp.avm.entity.Apk;
import com.tairanchina.csp.avm.entity.Channel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;


public class ApkServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(ApkService.class);

    @Autowired
    ApkService apkService;

    @Autowired
    ChannelService channelService;

    @Test
    public void create() throws Exception {
        UploadFileEntity uploadFileEntity = new UploadFileEntity();
        uploadFileEntity.setChannel("official");
        uploadFileEntity.setFileName("filename");
        uploadFileEntity.setOssUrl("ossurl");
        uploadFileEntity.setMd5("md5");
        uploadFileEntity.setVersionId(14);
        Apk apk = new Apk();
        ServiceResult serviceResult = channelService.findByChannelCode(uploadFileEntity.getChannel());
        if (serviceResult.getCode() == 200) {
            Channel channel = (Channel) serviceResult.getData();
            apk.setChannelId(channel.getId());
            apk.setVersionId(uploadFileEntity.getVersionId());
            apk.setOssUrl(uploadFileEntity.getOssUrl());
            apk.setMd5(uploadFileEntity.getMd5());
            ServiceResult result = apkService.create(apk);
            if (result.getData() != null) {
                logger.info(result.getData().toString());
            }
        } else {
            logger.info("找不到Channel");
            ServiceResult.failed(
                    ServiceResultConstants.APK_SAVE_ERROR.getCode(),
                    "文件[ " + uploadFileEntity.getFileName() + " ]录入失败，原因：" + serviceResult.getMessage()
            );
        }
    }

    @Test
    public void list() throws Exception {
        int versionId = 14;
        String channelCode = null;
        String md5 = "";
        Integer deliveryStatus = null;

        EntityWrapper<Apk> wrapper = new EntityWrapper<>();
        wrapper.and().eq("version_id", versionId);
        if (StringUtils.hasLength(channelCode)) {
            //wrapper.and().like("")
        }
        if (StringUtils.hasLength(md5)) {
            wrapper.and().like("md5", "%" + md5 + "%");
        }
        if (deliveryStatus != null) {
            wrapper.and().eq("delivery_status", deliveryStatus);
        }
        wrapper.and().eq("del_flag", 0);
        ServiceResult result = apkService.list(1, 2, wrapper, versionId);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        md5 = "dsgfggggggggggg";
        wrapper.and().like("md5", "%" + md5 + "%");
        result = apkService.list(1, 2, wrapper, versionId);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        versionId = 9999999;
        result = apkService.list(1, 2, wrapper, versionId);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void delivery() throws Exception {
        Integer id = 32;
        ServiceResult result = apkService.delivery(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        id = 45465454;
        result = apkService.delivery(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        id = 27;
        result = apkService.delivery(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void undelivery() throws Exception {
        Integer id = 32;
        ServiceResult result = apkService.undelivery(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        id = 45465454;
        result = apkService.undelivery(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        id = 27;
        result = apkService.undelivery(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void delete() throws Exception {
        Integer id = 32;
        ServiceResult result = apkService.delete(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        id = 45465454;
        result = apkService.delete(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        id = 27;
        result = apkService.delete(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void exists() throws Exception {
        String channelCode = "qudao";
        Integer versionId = 14;
        ServiceResult result = apkService.exists(channelCode, versionId);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        channelCode = "dsfdfdffds";
        result = apkService.exists(channelCode, versionId);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        channelCode = "aaaa2";
        result = apkService.exists(channelCode, versionId);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void selectApkWithChannelCode() throws Exception {
        Integer versionId = 14;
        String channelCode = "qudaocode";
        String md5 = "";
        Integer deliveryStatus = null;
        ServiceResult result = apkService.getApkPageWithChannelCode(1, 10, versionId, channelCode, md5, deliveryStatus);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        versionId = 999999;
        result = apkService.getApkPageWithChannelCode(1, 10, versionId, channelCode, md5, deliveryStatus);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }


}