package com.gracjan_d.taskmanager.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RemainingTime {

    private long days;
    private long hours;
    private long minutes;

    public RemainingTime(long days, long hours, long minutes) {
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    public static RemainingTime countRemainingTime(LocalDateTime startDate, LocalDate endDate){
        long remainingDays = ChronoUnit.DAYS.between(startDate, endDate.atStartOfDay());
        long remainingHours = ChronoUnit.HOURS.between(startDate, endDate.atStartOfDay()) % 24;
        long remainingMinutes = ChronoUnit.MINUTES.between(startDate, endDate.atStartOfDay()) % 60;

        if(remainingDays < 0) remainingDays = 0;
        if(remainingHours < 0) remainingHours = 0;
        if(remainingMinutes < 0) remainingMinutes = 0;

        return new RemainingTime(remainingDays, remainingHours, remainingMinutes);
    }
}
