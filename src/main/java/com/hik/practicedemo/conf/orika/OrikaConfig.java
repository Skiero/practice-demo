package com.hik.practicedemo.conf.orika;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wangJinChang on 2019/12/27 11:54
 * Orika配置类
 */
@Configuration
public class OrikaConfig {

    @Bean
    public MapperFactory loadMapperFactory() {
        return new DefaultMapperFactory.Builder().build();
    }

    @Bean
    public MapperFacade loadMapperFacade(MapperFactory mapperFactory) {
        return mapperFactory.getMapperFacade();
    }
}
