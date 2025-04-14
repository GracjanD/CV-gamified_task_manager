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
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;
    private UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping
    public String getTasks(@RequestParam(required = false, defaultValue = "GENERAL") String type,
                           @RequestParam(required = false, defaultValue = "all") String showOnly,
                           @RequestParam(required = false, defaultValue = "deadline") String sort, Model model){

        taskService.refreshTasks(TaskType.valueOf(type));
        model.addAttribute("points", userService.findById(1).getPoints());
        model.addAttribute("tasks", taskService.sortTasks(taskService.getTasksBySpecificParams(type, showOnly), sort));
        model.addAttribute("type", type);
        model.addAttribute("showOnly", showOnly);
        model.addAttribute("sort", sort);
        model.addAttribute("completionProgress", taskService.getPercentOfTasksByStatus(Status.COMPLETED));
        return "tasks";
    }

    @PostMapping("/{id}/delete")
    public String deleteTask(@RequestParam(required = false) String type,
                             @RequestParam(required = false) String showOnly,
                             @PathVariable("id") int taskId, Model model){

        taskService.deleteTaskById(taskId);
        return redirectByTypeAndShowOnly(type, showOnly);
    }

    @PostMapping("/{id}/complete")
    public String completeTask(@RequestParam(required = false, defaultValue = "GENERAL") String type,
                               @RequestParam(required = false) String showOnly,
                               @PathVariable("id") int taskId){

        Task task = taskService.getTaskById(taskId);
        int points = task.getPrizeIncludingDeadline();
        taskService.completeTask(task);
        userService.addPoints(points);
        return redirectByTypeAndShowOnly(type, showOnly);
    }

    private String redirectByTypeAndShowOnly(String type, String showOnly){

        if(type == null && showOnly == null) return "redirect:/tasks";
        if(type == null) return "redirect:/tasks?showOnly=" + showOnly;
        if(showOnly == null) return "redirect:/tasks?type=" + type;
        return "redirect:/tasks?type=" + type + "&showOnly=" + showOnly;
    }
}
