package com.taskschedular.init;

import com.taskschedular.manager.TaskManager;
import com.taskschedular.manager.impl.TaskManagerImpl;
import com.taskschedular.processor.TaskProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Component
public class TaskRunner implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRunner.class);

    @Autowired
    private TaskManager taskManager;

    private ExecutorService threadPoolExecutor;

    @Value("${thread.count}")
    private Integer THREAD_COUNT;

    @Value("${thread.interval}")
    private Integer THREAD_INTERVAL;

    @Override
    public void run(ApplicationArguments args) {
        LOGGER.info("Starting Executor Service for polling ...");
        threadPoolExecutor = Executors.newFixedThreadPool(THREAD_COUNT);
        IntStream.range(0, THREAD_COUNT).forEach(value -> {
            threadPoolExecutor.submit((new TaskProcessor(taskManager, THREAD_INTERVAL)));
        });
        LOGGER.info("Executor Service has been started with " + THREAD_COUNT + "threads");
    }

}
