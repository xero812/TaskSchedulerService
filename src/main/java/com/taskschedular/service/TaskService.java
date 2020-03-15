package com.taskschedular.service;

import com.taskschedular.manager.TaskManager;
import com.taskschedular.request.TaskRequest;
import com.taskschedular.response.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RequestMapping("/tasks")
public interface TaskService {

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    TaskResponse submit(@RequestBody TaskRequest taskRequest);

    @RequestMapping(value = "", method = RequestMethod.GET)
    TaskResponse getAll();

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    TaskResponse get(@PathVariable(value = "id") String id);

}
