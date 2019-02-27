package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.RnRoute;

/**
 * Created by hzlizx on 2018/6/20 0020
 */
public interface RnRouteService {

    /**
     * 创建路由
     * @param rnRoute
     * @return
     */
    ServiceResult create(RnRoute rnRoute);

    /**
     * 删除路由
     * @param id
     * @return
     */
    ServiceResult delete(int id);

    /**
     * 更新路由
     * @param rnRoute
     * @return
     */
    ServiceResult update(RnRoute rnRoute);

    /**
     * 列表
     * @param page
     * @param pageSize
     * @param wrapper
     * @return
     */
    ServiceResult list(int page, int pageSize, EntityWrapper<RnRoute> wrapper);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    ServiceResult find(int id);

}
