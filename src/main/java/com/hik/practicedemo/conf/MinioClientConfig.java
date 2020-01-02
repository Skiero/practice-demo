package com.hik.practicedemo.conf;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by wangJinChang on 2019/12/13 20:43
 * minion服务器配置类
 */
@Configuration
@PropertySource(value = "classpath:application.yml")
@ConfigurationProperties(prefix = "spring.minio")
public class MinioClientConfig {

    @Value("${endpoint}")
    private String endpoint;

    @Value("${access_key}")
    private String accessKey;

    @Value("${secret_key}")
    private String secretKey;

    @Bean
    public MinioClient loadMinioClient() throws InvalidPortException, InvalidEndpointException {

        return new MinioClient(endpoint, accessKey, secretKey);
    }
}
