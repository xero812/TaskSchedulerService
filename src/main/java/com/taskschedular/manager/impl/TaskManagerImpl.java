package com.taskschedular.manager.impl;

import com.taskschedular.dao.TaskDao;
import com.taskschedular.entity.Status;
import com.taskschedular.entity.Task;
import com.taskschedular.manager.TaskManager;
import com.taskschedular.request.TaskRequest;
import com.taskschedular.service.impl.TaskServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TaskManagerImpl implements TaskManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskManagerImpl.class);

    @Autowired
    private TaskDao taskDao;

    @Value("${batch.size}")
    private Integer BATCH_SIZE;

    @Value("${rabbitmq.exchange}")
    public String EXCHANGE;

    @Value("${rabbitmq.topic}")
    public String ROUTING_KEY;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public Task submit(TaskRequest taskRequest) {
        Task task = Task.builder()
                .message(taskRequest.getMessage())
                .timestamp(taskRequest.getTimestamp())
                .status(Status.SUBMITTED)
                .build();
        Task taskResult = taskDao.save(task);
        LOGGER.info("Task saved successfully : ", taskResult);
        return taskResult;
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = taskDao.getAll();
        LOGGER.info("Number of Tasks fetched successfully : ", tasks.size());
        return tasks;
    }

    @Override
    public Task get(String id) {
        Task task = taskDao.get(UUID.fromString(id));
        LOGGER.info("Task fetched successfully : ", task);
        return task;
    }

    @Override
    @Transactional
    public void process() {
        List<Task> result = new ArrayList();
        List<Task> tasks = taskDao.get(BATCH_SIZE);
        LOGGER.info("Fetched tasks from Storage : " + tasks.stream().map(task -> task.getId()).collect(Collectors.toList()));
        for (Task task : tasks) {
            if(task.getStatus().equals(Status.SUBMITTED)) {
                task.setStatus(Status.IN_PROGRESS);
                Task updatedTask = taskDao.save(task);
                result.add(updatedTask);
            }
        }
        LOGGER.info("Queueing tasks for processing : " + result.stream().map(task -> task.getId()).collect(Collectors.toList()));
        tasks.forEach(task -> rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, task));
    }

}
