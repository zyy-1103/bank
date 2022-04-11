package com.bank.configuration;

import com.bank.bean.OverdueRuleBean;
import com.bank.bean.UserRuleBean;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@ConfigurationProperties(prefix = "redis")
public class BeanConfiguration {

    public Thread thread(){
        return null;
    }

    /**
     * 用户登录状态：u+$id
     * 商品配置：_+$field
     * 是否已配置商品信息：~isConfigured
     * 秒杀URL：~url
     * 逾期规则sql: ~odsql
     * 逾期规则次数: ~odcnt
     * 逾期规则操作符: ~odope
     * 是否配置了用户属性规则: ~userRule
     */
    @Bean
    public RedisAsyncCommands<String, String> redisAsyncCommands() {
        RedisURI redisUri = RedisURI.builder()
                .withHost("localhost").withPort(6379)
                .withTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
        RedisClient client = RedisClient.create(redisUri);
        StatefulRedisConnection<String, String> connection = client.connect();
        return connection.async();
    }

    @Bean
    public UserRuleBean userRuleBean(){
        return new UserRuleBean();
    }

    @Bean
    public OverdueRuleBean overdueRuleBean(){
        return new OverdueRuleBean();
    }
}
