package com.taskschedular;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class TaskSchedularServiceApplication {

	public static void main(String[] args) {
		addInitHooks();
		SpringApplication.run(TaskSchedularServiceApplication.class, args);
	}

	public static void addInitHooks() {

	}

}
