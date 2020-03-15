package com.taskschedular.manager;

import com.taskschedular.entity.Task;
import com.taskschedular.request.TaskRequest;
import com.taskschedular.response.TaskResponse;

import java.util.List;
import java.util.UUID;

public interface TaskManager {

    Task submit(TaskRequest taskRequest);

    List<Task> getAll();

    Task get(String id);

    void process();

}
