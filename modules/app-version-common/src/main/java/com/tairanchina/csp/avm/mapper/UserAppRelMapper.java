package com.tairanchina.csp.avm.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tairanchina.csp.avm.entity.UserAppRel;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hzlizx on 2018/5/17 0017
 */
@Mapper
public interface UserAppRelMapper extends BaseMapper<UserAppRel> {

    /**
     * 查询出某个用户所有绑定的APP
     * @param userId
     * @return
     */
    ArrayList<HashMap> listBindApp(String userId);

}
