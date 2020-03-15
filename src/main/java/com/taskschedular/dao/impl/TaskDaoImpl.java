package com.taskschedular.dao.impl;

import com.taskschedular.dao.TaskDao;
import com.taskschedular.entity.Status;
import com.taskschedular.entity.Task;
import com.taskschedular.util.Cursor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public class TaskDaoImpl implements TaskDao {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private Cursor cursor;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${redis.interval}")
    private Integer REDIS_INTERVAL;

    @Value("${thread.interval}")
    private Integer THREAD_INTERVAL;

    @Value("${spring.redis.key}")
    private String key;

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
    public List<Task> get(Integer batchSize) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("From Task t where timestamp between :fromDateTime and :toDateTime and status = :status ");
        Date toDate = new Date();
        Date fromDate = new Date();
        fromDate.setTime(toDate.getTime() - THREAD_INTERVAL);
        query.setParameter("fromDateTime", fromDate);
        query.setParameter("toDateTime", toDate);
        query.setParameter("status", Status.SUBMITTED);
        query.setMaxResults(batchSize);
        int start = cursor.update(batchSize);
        if(start % REDIS_INTERVAL == 0) {
            updateCursor(start);
        }
        query.setFirstResult(start);
        List<Task> tasks = query.list();
        return tasks;
    }

    private void updateCursor(Integer start) {
        try {
            redisTemplate.opsForValue().set(key, String.valueOf(start));
        } catch (Exception e) {
            redisTemplate.opsForValue().set(key, String.valueOf(0));
        }
    }

}
