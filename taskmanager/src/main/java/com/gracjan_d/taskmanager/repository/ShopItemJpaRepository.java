package com.gracjan_d.taskmanager.repository;

import com.gracjan_d.taskmanager.entity.ShopItem;
import com.gracjan_d.taskmanager.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShopItemJpaRepository extends JpaRepository<ShopItem, Integer> {
    List<ShopItem> findByStatus(Status status);
}
