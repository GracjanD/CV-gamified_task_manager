package com.gracjan_d.taskmanager.repository;

import com.gracjan_d.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Integer> {

}
