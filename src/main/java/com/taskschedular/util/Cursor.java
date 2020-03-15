package com.taskschedular.util;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class Cursor {

    private AtomicInteger cursor;

    @Value("${batch.size}")
    private Integer BATCH_SIZE;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${spring.redis.key}")
    private String key;

    @Value("${pickCursorFromRedis}")
    private Boolean pickCursorFromRedis;

    @Bean
    public void getCursor() {
        if(cursor == null) {
            try {
                if(pickCursorFromRedis) {
                    Integer CURSOR_VALUE = Integer.parseInt(redisTemplate.opsForValue().get(key));
                    cursor = new AtomicInteger(CURSOR_VALUE - BATCH_SIZE);
                } else {
                    cursor = new AtomicInteger(- BATCH_SIZE);
                }
            } catch (Exception e) {
                cursor = new AtomicInteger(- BATCH_SIZE);
            }
        }
    }

    public int update(int delta) {
        return cursor.addAndGet(delta);
    }

}
