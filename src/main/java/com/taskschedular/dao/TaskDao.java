package com.taskschedular.dao;

import com.taskschedular.entity.Task;

import java.util.List;
import java.util.UUID;

public interface TaskDao {

    Task save(Task task);

    List<Task> getAll();

    Task get(UUID id);

    List<Task> get();

}
