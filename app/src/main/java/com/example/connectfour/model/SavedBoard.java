package com.example.connectfour.model;

public class SavedBoard {
    private Hole[][] grid;
    private boolean isWinner;
    private boolean isDraw;
    private Player currentPlayer;

    public SavedBoard(Hole[][] grid, boolean isWinner, boolean isDraw, Player currentPlayer) {
        this.grid = grid;
        this.isWinner = isWinner;
        this.isDraw = isDraw;
        this.currentPlayer = currentPlayer;
    }

    public Hole[][] getGrid() {
        return grid;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isDraw() {
        return isDraw;
    }

    public boolean isWinner() {
        return isWinner;
    }
}
