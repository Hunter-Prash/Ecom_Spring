package com.example.Ecom.Repositories;

import com.example.Ecom.Entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,Long> {
    // Crucial for validating if a user already exists during checkout
    Optional<Customer> findByEmail(String email);

}
