package com.tairanchina.csp.avc.mapper;

import com.tairanchina.csp.avm.entity.LoginInfo;
import com.tairanchina.csp.avm.AppVersionManagerApplication;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AppVersionManagerApplication.class})
@Transactional
@ComponentScan("com.tairanchina.csp.avc.mapper")
public class BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);


    /**
     * 设置登录状态
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        LoginInfo loginInfo = new LoginInfo(24, "b9e980c1495e4d0582c257901d86b4ff");
        ThreadLocalUtils.USER_THREAD_LOCAL.set(loginInfo);
    }

    @Test
    public void test() {
        logger.info("BaseTest........");
    }

}
