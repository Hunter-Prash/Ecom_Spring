package com.example.Ecom.Repositories;

import com.example.Ecom.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    // Fetch order history for a specific customer
    List<Order> findByCustomerId(Long customerId);

    // Useful for an SQS worker looking for orders stuck in "PLACED" status
    List<Order> findByOrderStatus(String orderStatus);

    // Tailored for your EventBridge Daily Sales Report trigger
    List<Order> findByCreatedAtBetween(ZonedDateTime start, ZonedDateTime end);
}
