package com.example.connectfour.model;

public enum Hole {
    COMPUTER,
    PLAYER,
    NONE;

    @Override
    public String toString() {
        switch (this) {
            case PLAYER:
                return "Player";
            case COMPUTER:
                return "Computer";
            default:
                return "NONE";
        }
    }
}
