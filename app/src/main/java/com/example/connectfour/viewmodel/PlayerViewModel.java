package com.example.connectfour.viewmodel;

import android.os.Handler;

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
        if (checkHorizontalWin()) return;
        if (checkForVerticalWin()) return;
        if (checkForRightDiagonalWin()) return;
        checkForLeftDiagonalWin();

    }

    private void checkForLeftDiagonalWin() {
        //Left diagonal check
        int x = 0;
        int y = sizeY - 3;
        while (x < sizeX - 3 && y < sizeY) {

            int playerDiagonal = 0;
            int computerDiagonal = 0;

            for (int i = 0; x + i < sizeX && y - i >= 0; i++) {

                //Left diagonal check
                if (grid[x + i][y - i] == Hole.COMPUTER) {
                    computerDiagonal += 1;
                    playerDiagonal = 0;
                } else if (grid[x + i][y - i] == Hole.PLAYER) {
                    computerDiagonal = 0;
                    playerDiagonal += 1;
                } else {
                    computerDiagonal = 0;
                    playerDiagonal = 0;
                }

                if (computerDiagonal == 4) {
                    computerHasWon();
                    return;
                }
                if (playerDiagonal == 4) {
                    playerHasWon();
                    return;
                }
            }

            if (y == sizeY - 1)
                x++;
            if (x == 0)
                y++;
        }
    }

    private boolean checkForRightDiagonalWin() {
        //Checking for right diagonal win
        int x = 0;
        int y = sizeY - 4;
        while (x < sizeX - 3 && y < sizeY - 3) {

            int playerDiagonal = 0;
            int computerDiagonal = 0;

            for (int i = 0; x + i < sizeX && y + i < sizeY; i++) {

                //Right diagonal check
                if (grid[x + i][y + i] == Hole.COMPUTER) {
                    computerDiagonal += 1;
                    playerDiagonal = 0;
                } else if (grid[x + i][y + i] == Hole.PLAYER) {
                    computerDiagonal = 0;
                    playerDiagonal += 1;
                } else {
                    computerDiagonal = 0;
                    playerDiagonal = 0;
                }


                if (hasSomeoneWon(playerDiagonal, computerDiagonal))
                    return true;
            }

            if (y == 0)
                x++;
            if (x == 0)
                y--;
        }
        return false;
    }

    private boolean checkForVerticalWin() {
        //Checking for vertical winning possibilities
        for (int i = 0; i < sizeX; i++) {
            int cumulativePlayerVertical = 0;
            int cumulativeComputerVertical = 0;
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
                    return true;
                }
                if (cumulativePlayerVertical == 4) {
                    playerHasWon();
                    return true;
                }

            }
        }
        return false;
    }

    private boolean checkHorizontalWin() {
        //Checking for horizontal winning possibilities
        for (int i = 0; i < sizeY; i++) {
            int cumulativePlayerHorizontal = 0;
            int cumulativeComputerHorizontal = 0;
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
                    return true;
                }
                if (cumulativePlayerHorizontal == 4) {
                    playerHasWon();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasSomeoneWon(int cumulativePlayerRightDiagonal, int cumulativeComputerRightDiagonal) {
        if (cumulativeComputerRightDiagonal == 4) {
            computerHasWon();
            return true;
        }
        if (cumulativePlayerRightDiagonal == 4) {
            playerHasWon();
            return true;
        }
        return false;
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
        new Handler().postDelayed(() -> {
            int moveIndex = new Random().nextInt(sizeX);

            while (!validTurn(moveIndex))
                moveIndex = new Random().nextInt(sizeX);

            makeMove(moveIndex);
        }, 100);

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
