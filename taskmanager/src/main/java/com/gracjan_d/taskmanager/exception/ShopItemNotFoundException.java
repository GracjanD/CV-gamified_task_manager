package com.gracjan_d.taskmanager.exception;

public class ShopItemNotFoundException extends RuntimeException{
    public ShopItemNotFoundException(int id){
        super("Not found shop item with id: " + id);
    }
}
