package com.hmdp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient(){
        // 配置
        Config config = new Config();

        // Redis 需要密码
        //config.useSingleServer().setAddress("redis://localhost:6379").setPassword("19980528");
        // Redis 无需密码
        config.useSingleServer().setAddress("redis://localhost:6379");

        // 创建RedissonClient对象
        return Redisson.create(config);
    }
}
