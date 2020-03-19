package com.taskschedular.dao.impl;

import com.taskschedular.config.CursorConfig;
import com.taskschedular.dao.TaskDao;
import com.taskschedular.entity.Status;
import com.taskschedular.entity.Task;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public class TaskDaoImpl implements TaskDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskDaoImpl.class);

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CursorConfig cursor;

    @Value("${batch.size}")
    private Integer BATCH_SIZE;

    @Override
    public Task save(Task task) {
        Session session = entityManager.unwrap(Session.class);
        UUID uuid = (UUID)session.save(task);
        return get(uuid);
    }

    @Override
    public List<Task> getAll() {
        Session session = entityManager.unwrap(Session.class);
        Query query=session.createQuery("from Task");
        List<Task> tasks = query.list();
        return tasks;
    }

    @Override
    public Task get(UUID id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("From Task where id= :id ");
        query.setParameter("id", id);
        List<Task> tasks = query.list();
        return tasks.get(0);
    }

    @Override
    public List<Task> get() {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("From Task t where timestamp < :currenDateTime and status = :status ");
        Date currentDate = new Date();
        query.setParameter("currenDateTime", currentDate);
        query.setParameter("status", Status.SUBMITTED);
        int start = cursor.getCursor();
        LOGGER.info("Querying Tasks from Limit : (" + start + "," + BATCH_SIZE + ") Before " + currentDate);
        query.setFirstResult(start);
        query.setMaxResults(BATCH_SIZE);
        List<Task> tasks = query.list();
        if(tasks.size() == 0) {
            LOGGER.info("No Tasks found");
            cursor.setToDefault();
        }
        LOGGER.info("Query Result Size: " + tasks.size());
        return tasks;
    }

}
