package com.cloud.ordertrackingapp.dao;

import com.cloud.ordertrackingapp.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
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
    public void merge(Order order) {
        liveEntityManager.merge(order);
    }

    @Transactional(readOnly = true, transactionManager = "secondTransactionManager")
    public Order findById(Long orderId) {
        return gaurdEntityManager.find(Order.class, orderId);
    }

}
