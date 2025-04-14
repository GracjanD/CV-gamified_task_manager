package com.gracjan_d.taskmanager.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="shop_items")
public class ShopItem {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String title;
    private int price;
    private int available;
    private int quantity;
    @Enumerated(EnumType.STRING)
    private ShopItemType type;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "refresh_days")
    private int refreshDays;
    @Column(name = "refresh_date")
    private LocalDate refreshDate;

    public ShopItem(){}

    public ShopItem(int id, String title, int price, int available, int quantity, ShopItemType type, Status status, LocalDate refreshDate) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.available = available;
        this.quantity = quantity;
        this.type = type;
        this.status = status;
        this.refreshDate = refreshDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ShopItemType getType() {
        return type;
    }

    public void setType(ShopItemType type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getRefreshDays() {
        return refreshDays;
    }

    public void setRefreshDays(int refreshDays) {
        this.refreshDays = refreshDays;
    }

    public LocalDate getRefreshDate() {
        return refreshDate;
    }

    public void setRefreshDate(LocalDate refreshDate) {
        this.refreshDate = refreshDate;
    }

    public void calculateRefreshDate(){
        refreshDate = LocalDate.now().plusDays(refreshDays);
    }

    public RemainingTime countRemainingTime(){
        return RemainingTime.countRemainingTime(LocalDateTime.now(), refreshDate);
    }

    public RemainingTime countRemainingTime(LocalDateTime startDate){
        return RemainingTime.countRemainingTime(startDate, refreshDate);
    }
}
