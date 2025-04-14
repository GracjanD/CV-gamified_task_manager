package com.gracjan_d.taskmanager.service;

import com.gracjan_d.taskmanager.entity.User;
import com.gracjan_d.taskmanager.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserJpaRepository userJpaRepository;

    public UserService(UserJpaRepository userJpaRepository){
        this.userJpaRepository = userJpaRepository;
    }

    public User findById(int id){
        return userJpaRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found user with id: " + id));
    }

    public void addPoints(int points){
        User user = findById(1);
        user.setPoints(user.getPoints() + points);
        userJpaRepository.save(user);
    }

    public void minusPoints(int points){
        User user = findById(1);
        if(hasEnoughPoints(points)){
            user.setPoints(user.getPoints() - points);
            userJpaRepository.save(user);
        }
    }

    public boolean hasEnoughPoints(int points){
        User user = findById(1);
        return user.getPoints() >= points;
    }
}
