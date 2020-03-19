package com.taskschedular.manager;

import com.taskschedular.entity.Task;
import com.taskschedular.request.TaskRequest;

import java.util.List;

public interface TaskManager {

    Task submit(TaskRequest taskRequest);

    List<Task> getAll();

    Task get(String id);

    void process();

    Task finish(Task task);

}
