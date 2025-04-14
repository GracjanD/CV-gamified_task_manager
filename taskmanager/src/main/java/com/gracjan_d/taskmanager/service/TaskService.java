package com.gracjan_d.taskmanager.service;

import com.gracjan_d.taskmanager.entity.*;
import com.gracjan_d.taskmanager.exception.TaskNotFoundException;
import com.gracjan_d.taskmanager.repository.TaskJpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private TaskJpaRepository taskJpaRepository;

    public TaskService(TaskJpaRepository taskJpaRepository) {
        this.taskJpaRepository = taskJpaRepository;
    }

    public Task getTaskById(int id){
        return taskJpaRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public List<Task> getAllTasks(){
        return taskJpaRepository.findAll();
    }

    public void deleteTaskById(int taskId){
        if(!taskJpaRepository.existsById(taskId)) throw new TaskNotFoundException(taskId);
        taskJpaRepository.deleteById(taskId);
    }

    public List<Task> getTasksByTaskTypeAndTaskStatus(TaskType taskType, Status status){
        return taskJpaRepository.findByTaskTypeAndStatus(taskType, status);
    }

    public int getPercentOfTasksByStatus(Status status){
        int totalTasks = taskJpaRepository.countTasks();
        if(totalTasks == 0) return 0;

        double resultOfDivide = (double) taskJpaRepository.countByStatus(status) / totalTasks;
        return (int) (resultOfDivide * 100);
    }

    public void completeTask(Task task){
        task.setStatus(Status.COMPLETED);
        if(!task.getDeadline().isAfter(LocalDate.now())){
            LocalDate newDeadline = getDeadlineByTaskType(task);
            task.setDeadline(newDeadline);
        }
        taskJpaRepository.save(task);
    }

    public List<Task> getTasksByPriority(Priority priority){
        return taskJpaRepository.findByPriority(priority);
    }

    public List<Task> getTasksByTaskType(TaskType taskType){
        return taskJpaRepository.findByTaskType(taskType);
    }

    public List<Task> getTasksByTaskTypeAndPriority(TaskType taskType, Priority priority){
        return taskJpaRepository.findByTaskTypeAndPriority(taskType, priority);
    }

    public List<Task> getTasksByTaskTypeAndDifficulty(TaskType taskType, Difficulty difficulty){
        return taskJpaRepository.findByTaskTypeAndDifficulty(taskType, difficulty);
    }

    public List<Task> getTasksBySpecificParams(String type, String showOnly){

        TaskType taskType = TaskType.valueOf(type);
        return switch(showOnly){
            case "active" -> getTasksByTaskTypeAndTaskStatus(taskType, Status.ACTIVE);
            case "completed" -> getTasksByTaskTypeAndTaskStatus(taskType, Status.COMPLETED);
            case "priority-low" -> getTasksByTaskTypeAndPriority(taskType, Priority.LOW);
            case "priority-mid" -> getTasksByTaskTypeAndPriority(taskType, Priority.MEDIUM);
            case "priority-high" -> getTasksByTaskTypeAndPriority(taskType, Priority.HIGH);
            case "difficulty-easy" -> getTasksByTaskTypeAndDifficulty(taskType, Difficulty.EASY);
            case "difficulty-mid" -> getTasksByTaskTypeAndDifficulty(taskType, Difficulty.MEDIUM);
            case "difficulty-hard" -> getTasksByTaskTypeAndDifficulty(taskType, Difficulty.HARD);
            default -> getTasksByTaskType(taskType);
        };
    }

    public Task createTask(Task task){
        task.setDeadlineByTaskType();
        return taskJpaRepository.save(task);
    }

    public List<Task> sortTasks(List<Task> tasks, String sortType){
        return tasks.stream()
                .sorted(getComparator(sortType))
                .collect(Collectors.toList());
    }

    public Comparator<Task> getComparator(String sortType){

        return switch(sortType){
            case "prize-min" -> Comparator.comparing(Task::getPrize);
            case "prize-max" -> Comparator.comparing(Task::getPrize).reversed();
            case "priority-min" -> Comparator.comparing(Task::getPriority);
            case "priority-max" -> Comparator.comparing(Task::getPriority).reversed();
            default -> Comparator.comparing(Task::getDeadline);
        };
    }

    public void refreshTasks(TaskType taskType){
        List<Task> tasks = taskJpaRepository.findByTaskTypeAndStatus(taskType, Status.COMPLETED);
        for(var task : tasks){
            if(!task.getDeadline().isAfter(LocalDate.now())){
                LocalDate newDeadline = getDeadlineByTaskType(task);
                task.setDeadline(newDeadline);
                task.setStatus(Status.ACTIVE);
                taskJpaRepository.save(task);
            }
        }
    }

    public LocalDate getDeadlineByTaskType(Task task){
        return switch(task.getTaskType()){
            case DAILY -> LocalDate.now().plusDays(1);
            case WEEKLY -> LocalDate.now().plusDays(7);
            default -> LocalDate.now().plusDays(30);
        };
    }
}
