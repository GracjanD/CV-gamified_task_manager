package com.gracjan_d.taskmanager.controller;

import com.gracjan_d.taskmanager.entity.*;
import com.gracjan_d.taskmanager.service.ShopItemService;
import com.gracjan_d.taskmanager.service.TaskService;
import com.gracjan_d.taskmanager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/editor")
public class EditorController {

    private UserService userService;
    private TaskService taskService;
    private ShopItemService shopItemService;

    public EditorController(UserService userService, TaskService taskService, ShopItemService shopItemService) {
        this.userService = userService;
        this.taskService = taskService;
        this.shopItemService = shopItemService;
    }

    @GetMapping
    public String showEditPanel(@RequestParam(defaultValue = "shopItem", required = false) String objectType, Model model){

        if(objectType.equals("task")){
            model.addAttribute("priorities", Priority.values());
            model.addAttribute("difficulties", Difficulty.values());
            model.addAttribute("taskTypes", TaskType.values());
            Task task = new Task();
            task.setDeadline(LocalDate.now().plusDays(1));
            model.addAttribute("task", task);
        }
        if(objectType.equals("shopItem")){
            model.addAttribute("types", ShopItemType.values());
            ShopItem shopItem = new ShopItem();
            shopItem.setQuantity(1);
            model.addAttribute("shopItem", shopItem);

        }
        model.addAttribute("objectType", objectType);
        model.addAttribute("points", userService.findById(1).getPoints());
        return "editor";
    }

    @PostMapping("/create/task")
    public String createTask(@ModelAttribute Task task){
        task.setStatus(Status.ACTIVE);
        taskService.createTask(task);
        return "redirect:/editor";
    }

    @PostMapping("/create/shopItem")
    public String createShopItem(@ModelAttribute ShopItem shopItem){
        shopItemService.createShopItem(shopItem);
        return "redirect:/editor";
    }
}
