package com.taskschedular.service.impl;

import com.taskschedular.entity.ResponseStatus;
import com.taskschedular.entity.Status;
import com.taskschedular.entity.Task;
import com.taskschedular.manager.TaskManager;
import com.taskschedular.request.TaskRequest;
import com.taskschedular.response.TaskResponse;
import com.taskschedular.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class TaskServiceImpl implements TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private TaskManager taskManager;

    @Override
    public TaskResponse submit(TaskRequest taskRequest) {
        TaskResponse taskResponse;
        try {
            LOGGER.info("Submitted TaskRequest : "+ taskRequest);
            Task task = taskManager.submit(taskRequest);
            List<Task> tasks = new ArrayList<>(Arrays.asList(task));
            taskResponse = TaskResponse.builder()
                    .status(ResponseStatus.builder()
                            .status(Status.SUCCEDED)
                            .count(tasks.size())
                            .build())
                    .data(tasks)
                    .build();
            LOGGER.info("Task submitted successfully : "+ taskResponse);
        } catch (Exception e) {
            taskResponse = TaskResponse.builder()
                    .status(ResponseStatus.builder()
                        .status(Status.FAILED)
                        .build())
                    .build();
            LOGGER.error("Task could not be submitted : "+ e.getMessage());
        }
        return taskResponse;
    }

    @Override
    public TaskResponse getAll() {
        TaskResponse taskResponse;
        try {
            List<Task> tasks = taskManager.getAll();
            taskResponse = TaskResponse.builder()
                    .status(ResponseStatus.builder()
                            .status(Status.SUCCEDED)
                            .count(tasks.size())
                            .build())
                    .data(tasks)
                    .build();
            LOGGER.info("Number of tasks fetched successfully : " + taskResponse.getStatus().getCount());
        } catch (Exception e) {
            taskResponse = TaskResponse.builder()
                    .status(ResponseStatus.builder()
                            .status(Status.FAILED)
                            .build())
                    .build();
            LOGGER.error("Tasks could not be fetched : "+ e.getMessage());
        }
        return taskResponse;
    }

    @Override
    public TaskResponse get(String id) {
        TaskResponse taskResponse = null;
        try {
            Task task = taskManager.get(id);
            List<Task> tasks = new ArrayList<>(Arrays.asList(task));
            taskResponse = TaskResponse.builder()
                    .status(ResponseStatus.builder()
                            .status(Status.SUCCEDED)
                            .count(tasks.size())
                            .build())
                    .data(tasks)
                    .build();
            LOGGER.info("Task fetched successfully : " + taskResponse);
        } catch (Exception e) {
            taskResponse = TaskResponse.builder()
                    .status(ResponseStatus.builder()
                            .status(Status.FAILED)
                            .build())
                    .build();
            LOGGER.error("Task could not be fetched : "+ e.getMessage());
        }
        return taskResponse;
    }

}
