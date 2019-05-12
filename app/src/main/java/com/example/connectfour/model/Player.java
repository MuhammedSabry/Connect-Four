package com.example.connectfour.model;

public enum Player {
    COMPUTER, PLAYER, NONE;

    @Override
    public String toString() {
        return this == COMPUTER ? "Computer" : "Player";
    }
}
