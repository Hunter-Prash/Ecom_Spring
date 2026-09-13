package com.example.Ecom.Entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username" ,nullable = false ,unique = true ,length = 50)
    private String username;

    @Column(name="email",nullable = false,unique = true,length = 50)
    private String email;

    @Column(name="password",nullable = false,length = 50)
    private String password;

    @Column(name = "role",nullable = false,length = 50)
    private String role;

    @Column(name = "created_at",insertable = false,updatable = false)
    private ZonedDateTime createdAt;

}
