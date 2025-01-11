package com.cloud.ordertrackingapp.dao;

import com.cloud.ordertrackingapp.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
public class OrderDAO {

    @PersistenceContext(unitName = "master")
    private EntityManager liveEntityManager;

    @PersistenceContext(unitName = "slave")
    private EntityManager gaurdEntityManager;


    @Transactional(transactionManager = "transactionManager")
    public Order save(Order order) {
        liveEntityManager.persist(order);
        return order;
    }

    @Transactional(transactionManager = "transactionManager")
    public void executeQuery(String query) {
        log.info("Executing query: {}", query);
        Query q = liveEntityManager.createNativeQuery(query);
        int i = q.executeUpdate();
        log.info("Executed query: {}", i);
    }

    @Transactional(transactionManager = "transactionManager")
    public void merge(Order order) {
        liveEntityManager.merge(order);
    }

    @Transactional(readOnly = true, transactionManager = "secondTransactionManager")
    public Order findById(Long orderId) {
        return gaurdEntityManager.find(Order.class, orderId);
    }

}
