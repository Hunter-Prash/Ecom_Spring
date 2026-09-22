package com.example.Ecom.Repositories;

import com.example.Ecom.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {
    @Query("select count(o) from Order o")
    Long totalCount();

    @Query("Select count(distinct o.customerName) from Order o")
    Long countUniqueCustomers();
}
