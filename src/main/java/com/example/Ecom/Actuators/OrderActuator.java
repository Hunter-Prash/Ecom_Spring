package com.example.Ecom.Actuators;

import com.example.Ecom.Repositories.OrderRepo;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id="orders")
public class OrderActuator {
    private final OrderRepo orderRepo;

    public OrderActuator(OrderRepo orderRepo){
        this.orderRepo=orderRepo;
    }

    @ReadOperation
    public Map<String,Object> orderStats(){
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders",orderRepo.totalCount());
        stats.put("uniqueSales",orderRepo.countUniqueCustomers());
        return stats;
    }

}
