package com.example.Ecom;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DbConn implements CommandLineRunner {
    private final DataSource dataSource;

    public DbConn(DataSource dataSource){
        this.dataSource=dataSource;
    }

    @Override
    public void run(String... args)throws Exception{
        System.out.println("===============");
        System.out.println(dataSource.getConnection());
        System.out.println("DATABASE CONNECTED!");
        System.out.println("=================");
    }
}
