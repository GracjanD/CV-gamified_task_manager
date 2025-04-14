package com.gracjan_d.taskmanager.entity;

public enum TaskType {
    GENERAL, DAILY, WEEKLY;

    @Override
    public String toString(){
        return switch(this){
            case DAILY -> "DZIENNE";
            case WEEKLY -> "TYGODNIOWE";
            case GENERAL -> "OGÓLNE";
        };
    }
}
