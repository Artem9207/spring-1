package com.javarush.dao;

import com.javarush.domain.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TaskDAO {

    private final SessionFactory sessionFactory;

    public TaskDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSessionFactory() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Task> getAll(int offset, int limit) {
        Query<Task> query = getSessionFactory().createQuery("SELECT T FROM Task t", Task.class);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public int getAllCount() {
        Query<Long> query = getSessionFactory().createQuery("SELECT COUNT(T) FROM Task t", Long.class);
        return Math.toIntExact(query.uniqueResult());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Task getById(int id) {
        Query<Task> query = getSessionFactory().createQuery("SELECT t FROM Task t WHERE t.id = :ID", Task.class);
        query.setParameter("ID", id);
        return query.uniqueResult();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void edit(Task task) {
        getSessionFactory().persist(task);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Task task) {
        getSessionFactory().remove(task);
    }
}
