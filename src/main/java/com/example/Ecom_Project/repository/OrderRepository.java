package com.example.Ecom_Project.repository;

import com.example.Ecom_Project.model.Order;
import com.example.Ecom_Project.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserUserId(int userId);
    List<Order> findTop5ByUserUserIdOrderByOrderDateDesc(int userId);
    List<Order> findByUser(Users user);

}
