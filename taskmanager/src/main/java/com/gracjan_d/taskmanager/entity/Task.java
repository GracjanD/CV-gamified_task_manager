package com.gracjan_d.taskmanager.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Entity
@Table(name="tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    @Enumerated(EnumType.STRING)
    @Column(name="type")
    private TaskType taskType;
    @Enumerated(EnumType.STRING)
    private Status status;
    private int prize;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate deadline;

    public Task(){}

    public Task(int id, String title, String description, Priority priority, Difficulty difficulty, TaskType taskType, Status status, int prize, LocalDate deadline){
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.difficulty = difficulty;
        this.taskType = taskType;
        this.status = status;
        this.prize = prize;
        this.deadline = deadline;
        setDeadlineByTaskType();
    }

    public Task(int id, String title, Status status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public RemainingTime countRemainingTime(){
        return RemainingTime.countRemainingTime(LocalDateTime.now(), deadline);
    }

    public RemainingTime countRemainingTime(LocalDateTime startDate){
        return RemainingTime.countRemainingTime(startDate, deadline);
    }

    public void setDeadlineByTaskType(){

        if(taskType == TaskType.DAILY){
            this.deadline = (LocalDate.now().plusDays(1));
        } else if(taskType == TaskType.WEEKLY){
            this.deadline = (LocalDate.now().plusDays(7));
        } else if(deadline == null) {
            this.deadline = LocalDate.of(2999,1,1);
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", difficulty=" + difficulty +
                ", taskType=" + taskType +
                ", taskStatus=" + status +
                ", prize=" + prize +
                ", deadline=" + deadline +
                '}';
    }

    public boolean isAfterDeadline(){
        return !LocalDate.now().isBefore(deadline);
    }

    public String getTimeAfterDeadline(){
        long days = ChronoUnit.DAYS.between(deadline.atStartOfDay(), LocalDateTime.now());
        long hours = ChronoUnit.HOURS.between(deadline.atStartOfDay(), LocalDateTime.now()) % 24;

        return days == 0 ? "%dgodz".formatted(hours) : "%ddni %dgodz".formatted(days,hours);
    }

    public int getPrizeIncludingDeadline(){
        return isAfterDeadline() ? (prize / 2) : prize;
    }
}
