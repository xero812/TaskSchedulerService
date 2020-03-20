package com.taskschedular.manager;

import com.taskschedular.entity.Task;
import com.taskschedular.exception.NotFoundException;
import com.taskschedular.exception.ValidationException;
import com.taskschedular.request.TaskRequest;

import java.util.List;

public interface TaskManager {

    Task submit(TaskRequest taskRequest) throws ValidationException;

    List<Task> getAll();

    Task get(String id) throws NotFoundException;

    void process();

    Task finish(Task task);

}
