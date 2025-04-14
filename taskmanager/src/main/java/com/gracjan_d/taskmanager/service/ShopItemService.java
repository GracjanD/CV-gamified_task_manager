package com.gracjan_d.taskmanager.service;

import com.gracjan_d.taskmanager.entity.ShopItem;
import com.gracjan_d.taskmanager.entity.Status;
import com.gracjan_d.taskmanager.exception.ShopItemNotFoundException;
import com.gracjan_d.taskmanager.repository.ShopItemJpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ShopItemService {

    private ShopItemJpaRepository shopItemJpaRepository;

    public ShopItemService(ShopItemJpaRepository shopItemJpaRepository){
        this.shopItemJpaRepository = shopItemJpaRepository;
    }

    public List<ShopItem> getAllShopItems(){
        return shopItemJpaRepository.findAll();
    }

    public ShopItem getShopItemById(int id){
        return shopItemJpaRepository.findById(id).orElseThrow(() -> new ShopItemNotFoundException(id));
    }

    public ShopItem createShopItem(ShopItem shopItem){
        shopItem.setStatus(Status.ACTIVE);
        shopItem.setAvailable(shopItem.getQuantity());
        shopItem.setRefreshDate(LocalDate.now());

        return shopItemJpaRepository.save(shopItem);
    }

    public void deleteShopItemById(int id){
        if(!shopItemJpaRepository.existsById(id)) throw new ShopItemNotFoundException(id);
        shopItemJpaRepository.deleteById(id);
    }

    public void buyShopItem(ShopItem shopItem){
        if(shopItem.getAvailable() > 1){
            shopItem.setAvailable(shopItem.getAvailable()-1);
        } else {
            shopItem.setAvailable(0);
            shopItem.calculateRefreshDate();
            shopItem.setStatus(Status.COMPLETED);
        }
        shopItemJpaRepository.save(shopItem);
    }

    public void refreshShopItems(){
        List<ShopItem> shopItems = shopItemJpaRepository.findByStatus(Status.COMPLETED);
        for(var shopItem : shopItems){
            if(!shopItem.getRefreshDate().isAfter(LocalDate.now())){
                shopItem.setStatus(Status.ACTIVE);
                shopItem.setAvailable(shopItem.getQuantity());
                shopItemJpaRepository.save(shopItem);
            }
        }
    }
}
