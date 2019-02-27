package com.tairanchina.csp.avc.mapper;


import com.tairanchina.csp.avm.mapper.UserAppRelMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;

public class UserAppRelMapperTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(UserAppRelMapperTest.class);

    @Autowired
    UserAppRelMapper userAppRelMapper;

    @Test
    public void listBindApp() throws Exception {
        ArrayList<HashMap> list = userAppRelMapper.listBindApp("b9e980c1495e4d0582c257901d86b4ff");
        logger.info("list..." + list.size());
    }

}