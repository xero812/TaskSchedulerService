package com.taskschedular.listener;

import com.taskschedular.entity.Task;
import com.taskschedular.manager.TaskManager;
import com.taskschedular.manager.impl.TaskManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

@Component
public class RabbitMQListener implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQListener.class);

    private TaskManager taskManager;

    public RabbitMQListener() {
        taskManager = new TaskManagerImpl();
    }

    @Override
    public void onMessage(Message message) {
        try {
            Task task = (Task)(new ObjectInputStream(new ByteArrayInputStream(message.getBody())).readObject());
            LOGGER.info("Task Received from Queue and Ready for execution :"+ task);
            Task taskResult = taskManager.finish(task);
            LOGGER.info("Task has been finished : "+ taskResult);
        } catch (Exception e) {
            LOGGER.error("I/O Error caught:" + e.getMessage());
        }
    }

}
