package com.example.Ecom_Project.repository;

import com.example.Ecom_Project.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RoleRepo extends JpaRepository<Roles,Integer> {
    List<Roles> findByName(String name);
}
