package com.gracjan_d.taskmanager.entity;

public enum Priority {
    LOW, MEDIUM, HIGH;

    @Override
    public String toString(){
        return switch(this){
            case LOW -> "NISKI";
            case MEDIUM -> "ŚREDNI";
            case HIGH -> "WYSOKI";
        };
    }
}
