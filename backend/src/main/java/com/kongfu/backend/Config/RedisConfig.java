package com.kongfu.backend.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author 付聪
 * Redis配置
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        // 设置 key 的序列化的方式
        redisTemplate.setKeySerializer(RedisSerializer.string());
        // 设置 value 的序列化的方式
        redisTemplate.setValueSerializer(RedisSerializer.json());
        // 设置 hash 的 key 的序列化的方式
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        // 设置 hash 的 value 的序列化的方式
        redisTemplate.setHashValueSerializer(RedisSerializer.json());

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
