package com.taskschedular.processor;

import com.taskschedular.entity.Task;
import com.taskschedular.init.TaskRunner;
import com.taskschedular.manager.TaskManager;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TaskProcessor implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskProcessor.class);

    private TaskManager taskManager;
    private Integer THREAD_INTERVAL;

    public TaskProcessor(TaskManager taskManager, Integer THREAD_INTERVAL) {
        this.taskManager = taskManager;
        this.THREAD_INTERVAL = THREAD_INTERVAL;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            LOGGER.info("Thread " + getThreadName() + "started/resumed ...");
            taskManager.process();
            LOGGER.info("Thread " + getThreadName() + "waiting for " + THREAD_INTERVAL + "ms");
            Thread.sleep(THREAD_INTERVAL);
        }
    }

    public String getThreadName() {
        return Thread.currentThread().getName();
    }

}
