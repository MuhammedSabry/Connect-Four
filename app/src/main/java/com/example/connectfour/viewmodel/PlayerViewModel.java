package com.example.connectfour.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.connectfour.model.Hole;
import com.example.connectfour.model.Player;

import java.util.Random;

public class PlayerViewModel extends ViewModel {
    private final int sizeX = 7, sizeY = 6;
    private Hole[][] grid = new Hole[sizeX][sizeY];
    private Player currentPlayer = Player.PLAYER;
    private MutableLiveData<Hole[][]> gridLiveData;
    private MutableLiveData<Player> winnerLiveData;
    private boolean hasWinner = false;
    private MutableLiveData<Boolean> drawLiveData;
    private boolean isDraw = false;

    public PlayerViewModel() {
        initGrid();
        this.gridLiveData = new MutableLiveData<>(this.grid);
        this.winnerLiveData = new MutableLiveData<>();
        this.drawLiveData = new MutableLiveData<>();
    }

    private void initGrid() {
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[i].length; j++)
                grid[i][j] = Hole.NONE;
    }

    public void onCoinClicked(int xIndex) {
        if (this.currentPlayer == Player.PLAYER && validTurn(xIndex) && !hasWinner)
            makeMove(xIndex);
    }


    private void makeMove(int xIndex) {
        if (hasWinner || isDraw)
            return;
        addCoin(xIndex);
        onMoveMade();
        checkForWinner();
        if (!hasWinner)
            checkForDraw();
        if (!hasWinner && !isDraw)
            switchTurns();
    }

    private void checkForDraw() {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (grid[i][j] == Hole.NONE)
                    return;
            }
        }
        drawLiveData.postValue(true);
        isDraw = true;
    }

    private void checkForWinner() {
        int cumulativePlayerHorizontal, cumulativeComputerHorizontal, cumulativePlayerVertical, cumulativeComputerVertical;

        //Checking for horizontal winning possibilities
        for (int i = 0; i < sizeY; i++) {
            cumulativePlayerHorizontal = 0;
            cumulativeComputerHorizontal = 0;
            for (int j = 0; j < sizeX; j++) {
                if (grid[j][i] == Hole.COMPUTER) {
                    cumulativePlayerHorizontal = 0;
                    cumulativeComputerHorizontal += 1;
                } else if (grid[j][i] == Hole.PLAYER) {
                    cumulativePlayerHorizontal += 1;
                    cumulativeComputerHorizontal = 0;
                } else {
                    cumulativePlayerHorizontal = 0;
                    cumulativeComputerHorizontal = 0;
                }

                if (cumulativeComputerHorizontal == 4) {
                    computerHasWon();
                    return;
                }
                if (cumulativePlayerHorizontal == 4) {
                    playerHasWon();
                    return;
                }
            }
        }

        //Checking for vertical winning possibilities
        for (int i = 0; i < sizeX; i++) {
            cumulativePlayerVertical = 0;
            cumulativeComputerVertical = 0;
            for (int j = 0; j < sizeY; j++) {
                if (grid[i][j] == Hole.COMPUTER) {
                    cumulativeComputerVertical += 1;
                    cumulativePlayerVertical = 0;
                } else if (grid[i][j] == Hole.PLAYER) {
                    cumulativeComputerVertical = 0;
                    cumulativePlayerVertical += 1;
                } else {
                    cumulativeComputerVertical = 0;
                    cumulativePlayerVertical = 0;
                }

                if (cumulativeComputerVertical == 4) {
                    computerHasWon();
                    return;
                }
                if (cumulativePlayerVertical == 4) {
                    playerHasWon();
                    return;
                }

            }
        }

    }

    private void playerHasWon() {
        this.hasWinner = true;
        this.winnerLiveData.postValue(Player.PLAYER);
    }

    private void computerHasWon() {
        this.hasWinner = true;
        this.winnerLiveData.postValue(Player.COMPUTER);
    }

    private void switchTurns() {
        this.currentPlayer = this.currentPlayer == Player.PLAYER ? Player.COMPUTER : Player.PLAYER;
        this.onTurnSwitched();
    }

    private void onTurnSwitched() {
        if (currentPlayer == Player.COMPUTER)
            makeComputerMove();
    }

    private void onMoveMade() {
        this.gridLiveData.postValue(this.grid);
    }

    private void makeComputerMove() {

        int moveIndex = new Random().nextInt(sizeX);

        while (!validTurn(moveIndex))
            moveIndex = new Random().nextInt(sizeX);

        makeMove(moveIndex);
    }

    private void addCoin(int xIndex) {
        for (int i = 0; i < grid[xIndex].length; i++) {
            Hole hole = grid[xIndex][i];
            if (hole == Hole.NONE) {
                grid[xIndex][i] = (currentPlayer == Player.PLAYER ? Hole.PLAYER : Hole.COMPUTER);
                return;
            }
        }
    }

    private boolean validTurn(int xIndex) {
        return grid[xIndex][sizeY - 1] == Hole.NONE;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public LiveData<Hole[][]> getGrid() {
        return this.gridLiveData;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public MutableLiveData<Boolean> isDraw() {
        return drawLiveData;
    }

    public LiveData<Player> getWinner() {
        return winnerLiveData;
    }
}
