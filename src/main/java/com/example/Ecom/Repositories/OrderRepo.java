package com.example.Ecom.Repositories;

import com.example.Ecom.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {
    @Query("select count(o) from Order o")
    Long totalCount();

    @Query("Select count(distinct o.customerName) from Order o")
    Long countUniqueCustomers();

    @Query("Select o from Order o where o.orderId=:id")
    Optional<Order> findByOrderId(@Param("id") Long id);
}
