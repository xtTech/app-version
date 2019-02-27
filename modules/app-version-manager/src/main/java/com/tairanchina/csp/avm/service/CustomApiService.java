package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.CustomApi;

public interface CustomApiService {


    ServiceResult createCustomApi(CustomApi customApi);

    ServiceResult updateCustomApi(CustomApi customApi);

    /**
     * 软删
     * @param id
     * @return
     */
    ServiceResult deleteCustomApi(int id);

    /**
     * 硬删
     * @param id
     * @return
     */
    ServiceResult deleteCustomApiForver(int id);

    ServiceResult getCustomApiByOne(CustomApi customApi);

    ServiceResult getCustomApiByKeyAndAppId(String customKey);

    ServiceResult list(int page, int pageSize, EntityWrapper<CustomApi> wrapper);

}
