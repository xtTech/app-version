package com.tairanchina.csp.avm.wapper;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

/**
 * 描述:版本比较拓展
 *
 * @author hzds
 * @Create 2018-09 : 26 19:44
 */
public class ExtWrapper<T> extends EntityWrapper<T> {

    public ExtWrapper<T> setVersionSort(String columns,Boolean isAsc) {
        this.orderBy("CONVERT(substring_index(substring_index(substring_index("+columns+",'-',1),'.',1),'.',-1),SIGNED)", isAsc);
        this.orderBy("CONVERT(substring_index(substring_index(substring_index("+columns+",'-',1),'.',2),'.',-1),SIGNED)", isAsc);
        this.orderBy("CONVERT(substring_index(substring_index(substring_index("+columns+",'-',1),'.',3),'.',-1),SIGNED)", isAsc);
        this.orderBy("CONVERT(substring_index(substring_index(substring_index("+columns+",'-',1),'.',4),'.',-1),SIGNED)", isAsc);
        return this;
    }
}
