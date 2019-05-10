package com.example.connectfour.model;

public enum Player {
    COMPUTER, PLAYER;

    @Override
    public String toString() {
        return this == COMPUTER ? "Computer" : "Player";
    }
}
