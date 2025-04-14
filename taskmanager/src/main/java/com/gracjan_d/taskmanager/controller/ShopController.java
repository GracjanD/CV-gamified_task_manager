package com.gracjan_d.taskmanager.controller;

import com.gracjan_d.taskmanager.entity.ShopItem;
import com.gracjan_d.taskmanager.service.ShopItemService;
import com.gracjan_d.taskmanager.service.TaskService;
import com.gracjan_d.taskmanager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shop")
public class ShopController {

    private UserService userService;
    private ShopItemService shopItemService;

    public ShopController(UserService userService, ShopItemService shopItemService) {
        this.userService = userService;
        this.shopItemService = shopItemService;
    }

    @GetMapping
    public String showShopPanel(Model model){
        shopItemService.refreshShopItems();
        // TODO: Replace static user ID in future
        model.addAttribute("points", userService.findById(1).getPoints());
        model.addAttribute("shopItems", shopItemService.getAllShopItems());
        return "shop";
    }

    @PostMapping("/{id}/delete")
    public String deleteShopItem(@PathVariable("id") int shopItemId){
        shopItemService.deleteShopItemById(shopItemId);
        return "redirect:/shop";
    }

    @PostMapping("/{id}/buy")
    public String buyShopItem(@PathVariable("id") int id){
        ShopItem shopItem = shopItemService.getShopItemById(id);
        if(userService.hasEnoughPoints(shopItem.getPrice())){
            userService.minusPoints(shopItem.getPrice());
            shopItemService.buyShopItem(shopItem);
            return "redirect:/shop?success";
        }
        return "redirect:/shop?error";
    }
}
