package com.taskschedular.config;

import com.taskschedular.service.impl.TaskServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class CursorConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(CursorConfig.class);

    private AtomicInteger cursor;

    @Value("${batch.size}")
    private Integer BATCH_SIZE;

    @Value("${redis.interval}")
    private Integer REDIS_INTERVAL;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${spring.redis.key}")
    private String key;

    @Value("${pickCursorFromRedis}")
    private Boolean pickCursorFromRedis;

    @Bean
    public void init() {
        if(cursor == null) {
            try {
                if(pickCursorFromRedis) {
                    LOGGER.info("Picking value from Redis ...");
                    Integer redisCursor = Integer.parseInt(redisTemplate.opsForValue().get(key));
                    Assert.isTrue(redisCursor >= 0, "Unexpected value from Redis");
                    cursor = new AtomicInteger(redisCursor);
                    LOGGER.info("Default Cursor from redis set succesfully : " +cursor.get());
                } else {
                    setToDefault();
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                setToDefault();
            }
        }
    }

    private int update() {
        return cursor.addAndGet(BATCH_SIZE);
    }

    public int setToDefault() {
        LOGGER.info("Updating the cursor to default value");
        cursor = new AtomicInteger(0);
        int value = cursor.get();
        updateRedis(value);
        return value;
    }

    public int getCursor() {
        int start = cursor.get();
        int update = update();
        if(start % REDIS_INTERVAL == 0) {
            updateRedis(update);
        }
        return start;
    }

    public void updateRedis(Integer start) {
        try {
            redisTemplate.opsForValue().set(key, String.valueOf(start));
        } catch (Exception e) {
            redisTemplate.opsForValue().set(key, String.valueOf(0));
        }
    }

}
