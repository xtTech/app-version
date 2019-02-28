package com.tairanchina.csp.avc.mapper;


import com.baomidou.mybatisplus.plugins.Page;
import com.tairanchina.csp.avm.dto.OperationRecordLogExt;
import com.tairanchina.csp.avm.mapper.OperationRecordLogMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class OperationRecordLogMapperTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(OperationRecordLogMapperTest.class);

    @Autowired
    OperationRecordLogMapper operationRecordLogMapper;

    @Test
    public void selectLogExtByQuery() throws Exception {
        Page<OperationRecordLogExt> page = new Page<>();
        page.setCurrent(1);
        page.setSize(10);
        String endDate = "2018-06-27 19:10:27";

        List list = operationRecordLogMapper.selectLogExtByQuery(page, null, null, null, null, null, null,null,endDate);
        logger.info("size: " + list.size());

        list = operationRecordLogMapper.selectLogExtByQuery(page, "", "", null, "", null, "",null,null);
        logger.info("size: " + list.size());
    }

}