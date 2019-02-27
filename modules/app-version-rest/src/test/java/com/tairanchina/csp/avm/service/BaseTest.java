package com.tairanchina.csp.avm.service;

import com.tairanchina.csp.avm.AppVersionRestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AppVersionRestApplication.class})
@Transactional
@ComponentScan("com.tairanchina.csp.avm.service")
public class BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @Test
    public void test() {
        logger.info("BaseTest........");
    }

}
