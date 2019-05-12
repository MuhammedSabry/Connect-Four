package com.example.connectfour.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<Node> children;
    private int alpha;
    private int beta;
    private Hole[][] board;
    private boolean isMax;

    public Node(int alpha, int beta, Hole[][] board) {
        this.alpha = alpha;
        this.beta = beta;
        this.board = board;
        this.children = new ArrayList<>();
    }

    public void setMax(boolean max) {
        isMax = max;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public List<Node> getChildren() {
        return children;
    }

    @NonNull
    @Override
    public String toString() {
        return (isMax ? "Maximizer" : "Minimizer") + ", Alpha= " + alpha + ", Beta= " + beta;
    }

    public Hole[][] getBoard() {
        return board;
    }

    public void setBoard(Hole[][] board) {
        this.board = board;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getBeta() {
        return beta;
    }

    public void setBeta(int beta) {
        this.beta = beta;
    }
}
