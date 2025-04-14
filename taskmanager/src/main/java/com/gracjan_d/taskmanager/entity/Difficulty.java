package com.gracjan_d.taskmanager.entity;

public enum Difficulty {
    EASY, MEDIUM, HARD;

    @Override
    public String toString(){
        return switch(this){
            case EASY -> "ŁATWY";
            case MEDIUM -> "ŚREDNI";
            case HARD -> "TRUDNY";
        };
    }
}
