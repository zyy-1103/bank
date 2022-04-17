package com.bank.configuration;

import com.bank.bean.OverdueRuleBean;
import com.bank.bean.UserRuleBean;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import jdk.nashorn.internal.objects.annotations.Property;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class BeanConfiguration {

    private final static int TOTAL=10000;

    @Value("${redis.port}")
    Integer port;
    @Value("${redis.addr}")
    String addr;
    @Value("${redis.pass}")
    String pass;

    /**
     * 用户登录状态：u+$id
     * 商品配置：_+$field
     * 是否已配置商品信息：~isConfigured
     * 逾期规则sql: ~odsql
     * 逾期规则次数: ~odcnt
     * 逾期规则操作符: ~odope
     * 是否配置了用户属性规则: ~userRule
     */
    @Bean
    public RedisAsyncCommands<String, String> redisAsyncCommands() {
        RedisURI redisUri = RedisURI.builder()
                .withHost(addr).withPort(port).withPassword(pass.toCharArray())
                .withTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
        RedisClient client = RedisClient.create(redisUri);
        StatefulRedisConnection<String, String> connection = client.connect();
        return connection.async();
    }

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){
        return new ThreadPoolExecutor(6, 12, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<>(TOTAL));
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
