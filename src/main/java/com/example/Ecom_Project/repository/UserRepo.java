package com.example.Ecom_Project.repository;

import com.example.Ecom_Project.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo  extends JpaRepository<Users , Integer> {


}
