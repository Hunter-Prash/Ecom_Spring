package com.example.Ecom.Repositories;

import com.example.Ecom.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
}
