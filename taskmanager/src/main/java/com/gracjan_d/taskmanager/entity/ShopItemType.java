package com.gracjan_d.taskmanager.entity;

public enum ShopItemType {
    SHOPPING, ENTERTAINMENT,OTHER;

    @Override
    public String toString() {
        return switch (this){
            case SHOPPING -> "ZAKUPY";
            case ENTERTAINMENT -> "ROZRYWKA";
            default -> "INNE";
        };
    }
}
