package com.example.connectfour.viewmodel;

import androidx.annotation.CheckResult;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.connectfour.model.Hole;
import com.example.connectfour.model.Node;
import com.example.connectfour.model.Player;
import com.example.connectfour.model.SavedBoard;
import com.example.connectfour.preferences.HoleStorage;

public class PlayerViewModel extends ViewModel {

    private final int sizeX = 7, sizeY = 6;
    private final HoleStorage holeStorage;
    private boolean isFreePlay = false;
    private Hole[][] grid = new Hole[sizeX][sizeY];
    private Player currentPlayer = Player.PLAYER;
    private MutableLiveData<Hole[][]> gridLiveData;
    private MutableLiveData<Player> winnerLiveData;
    private boolean hasWinner = false;
    private MutableLiveData<Boolean> drawLiveData;
    private boolean isDraw = false;
    private MutableLiveData<Player> currentPlayerLiveData;
    private int difficulty;

    public PlayerViewModel(int difficulty, boolean isNewGame, boolean isCustomGame) {
        initGrid();
        this.gridLiveData = new MutableLiveData<>(this.grid);
        this.winnerLiveData = new MutableLiveData<>();
        this.drawLiveData = new MutableLiveData<>();
        this.currentPlayerLiveData = new MutableLiveData<>();
        this.difficulty = difficulty;
        holeStorage = new HoleStorage();

        if (isCustomGame)
            this.isFreePlay = true;

        else if (!isNewGame && holeStorage.getSavedBoard() != null) {
            SavedBoard savedBoard = holeStorage.getSavedBoard();
            this.grid = savedBoard.getGrid();
            this.isDraw = savedBoard.isDraw();
            this.hasWinner = savedBoard.isWinner();
            this.currentPlayer = savedBoard.getCurrentPlayer();
            afterCoinAdded(false);
            notifyCurrentPlayer();
        }

    }

    private void initGrid() {
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[i].length; j++)
                grid[i][j] = Hole.NONE;
    }

    public void onCoinClicked(int xIndex) {
        if ((this.currentPlayer == Player.PLAYER || isFreePlay) && validMove(xIndex, grid) && !hasWinner)
            makeMove(xIndex);
    }

    private void makeMove(int xIndex) {
        if (hasWinner || isDraw)
            return;
        addCoin(xIndex, this.currentPlayer, this.grid);
        afterCoinAdded(true);
    }

    private void afterCoinAdded(boolean shouldSwitchRoles) {
        onMoveMade();

        Player winner = getWinner(grid);
        if (winner == Player.COMPUTER)
            computerHasWon();
        else if (winner == Player.PLAYER)
            playerHasWon();

        if (!hasWinner) {

            boolean isDraw = isDraw(this.grid);

            if (isDraw) {
                drawLiveData.postValue(true);
                this.isDraw = true;
            }

        }
        if (!hasWinner && !isDraw && shouldSwitchRoles)
            switchTurns();
    }

    private boolean isDraw(Hole[][] grid) {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (grid[i][j] == Hole.NONE)
                    return false;
            }
        }
        return true;
    }

    @CheckResult
    private Player getWinner(Hole[][] grid) {

        Player horizontalWinner = checkHorizontalWin(grid);
        if (horizontalWinner != Player.NONE)
            return horizontalWinner;

        Player verticalWinner = checkForVerticalWin(grid);
        if (verticalWinner != Player.NONE)
            return verticalWinner;

        Player rightDiagonalWinner = checkForRightDiagonalWin(grid);
        if (rightDiagonalWinner != Player.NONE)
            return rightDiagonalWinner;

        return checkForLeftDiagonalWin(grid);

    }

    private Player checkForLeftDiagonalWin(Hole[][] grid) {
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

                if (computerDiagonal == 4)
                    return Player.COMPUTER;
                if (playerDiagonal == 4)
                    return Player.PLAYER;
            }

            if (y == sizeY - 1)
                x++;
            if (x == 0)
                y++;
        }
        return Player.NONE;
    }

    private Player checkForRightDiagonalWin(Hole[][] grid) {
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

                if (computerDiagonal == 4)
                    return Player.COMPUTER;
                if (playerDiagonal == 4)
                    return Player.PLAYER;
            }

            if (y == 0)
                x++;
            if (x == 0)
                y--;
        }
        return Player.NONE;
    }

    private Player checkForVerticalWin(Hole[][] grid) {
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

                if (cumulativeComputerVertical == 4)
                    return Player.COMPUTER;
                if (cumulativePlayerVertical == 4)
                    return Player.PLAYER;

            }
        }
        return Player.NONE;
    }

    private Player checkHorizontalWin(Hole[][] grid) {
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

                if (cumulativeComputerHorizontal == 4)
                    return Player.COMPUTER;
                if (cumulativePlayerHorizontal == 4)
                    return Player.PLAYER;
            }
        }
        return Player.NONE;
    }

    private int getScore(Hole[][] grid) {

        Player winner = getWinner(grid);
        if (winner == Player.PLAYER)
            return Integer.MIN_VALUE;
        if (winner == Player.COMPUTER)
            return Integer.MAX_VALUE;

        int winLength = 4;

        int[] player1Lines = new int[winLength];
        int[] player2Lines = new int[winLength];

        int winSize = winLength - 1;
        for (int i = 0; i < winLength; i++) {
            player1Lines[i] = 0;
            player2Lines[i] = 0;
        }

        // Check each tile
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {

                int playerHorizontal = 0;
                int computerHorizontal = 0;
                boolean isValidHorizontal = true;

                int playerVertical = 0;
                int computerVertical = 0;
                boolean isValidVertical = true;

                int playerRightDiagonal = 0;
                int computerRightDiagonal = 0;
                boolean isValidRightDiagonal = true;

                int playerLeftDiagonal = 0;
                int computerLeftDiagonal = 0;
                boolean isValidLeftDiagonal = true;

                // Get line segments for each player
                for (int w = 0; w < winLength; w++) {

                    // Check right
                    if (x + winSize < sizeX) {

                        if (grid[x + w][y] == Hole.COMPUTER)
                            computerHorizontal++;

                        if (grid[x + w][y] == Hole.PLAYER)
                            playerHorizontal++;

                    } else
                        isValidHorizontal = false;


                    // Check down and diagonals
                    if (y + winSize < sizeY) {

                        //Vertical check
                        if (grid[x][y + w] == Hole.COMPUTER)
                            computerVertical++;

                        if (grid[x][y + w] == Hole.PLAYER)
                            playerVertical++;

                        // Left Diagonal
                        if (x - winSize >= 0) {
                            if (grid[x - w][y + w] == Hole.COMPUTER)
                                computerLeftDiagonal++;

                            if (grid[x - w][y + w] == Hole.PLAYER)
                                playerLeftDiagonal++;

                        } else {
                            isValidLeftDiagonal = false;
                        }


                        // Right diagonal
                        if (x + winSize < sizeX) {
                            if (grid[x + w][y + w] == Hole.COMPUTER)
                                computerRightDiagonal++;

                            if (grid[x + w][y + w] == Hole.PLAYER)
                                playerRightDiagonal++;
                        } else
                            isValidRightDiagonal = false;
                    } else {
                        isValidVertical = false;
                        isValidLeftDiagonal = false;
                        isValidRightDiagonal = false;
                    }
                }

                updateCount(computerHorizontal,
                        playerHorizontal,
                        isValidHorizontal,
                        player1Lines,
                        player2Lines);
                updateCount(computerVertical,
                        playerVertical,
                        isValidVertical,
                        player1Lines,
                        player2Lines);
                updateCount(computerLeftDiagonal,
                        playerLeftDiagonal,
                        isValidLeftDiagonal,
                        player1Lines,
                        player2Lines);
                updateCount(computerRightDiagonal,
                        playerRightDiagonal,
                        isValidRightDiagonal,
                        player1Lines,
                        player2Lines);
            }
        }


        // Set score to infinity if any winning lines are found
        if (player1Lines[player1Lines.length - 1] > 0)
            return Integer.MAX_VALUE;

        if (player2Lines[player2Lines.length - 1] > 0)
            return Integer.MIN_VALUE;


        // Sum scores for each player
        int score = 0;

        for (int i = 0; i < player1Lines.length - 1; i++) {
            score += Math.pow(10 * i, i) * player1Lines[i];
            score -= Math.pow(10 * i, i) * player2Lines[i];
        }

        return score;
    }


    private void updateCount(int computer, int player, boolean isValid, int[] player1Lines, int[] player2Lines) {

        //Make sure line is valid
        if (computer > 0 && player > 0)
            isValid = false;

        //Update line counts
        if (isValid) {
            if (computer > 0)
                player1Lines[computer - 1]++;

            if (player > 0)
                player2Lines[player - 1]++;
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
        notifyCurrentPlayer();
        if (currentPlayer == Player.COMPUTER)
            makeComputerMove();
    }

    private void notifyCurrentPlayer() {
        this.currentPlayerLiveData.postValue(this.currentPlayer);
    }

    private void onMoveMade() {
        this.gridLiveData.postValue(this.grid);
    }

    private int miniMax(Hole[][] grid,
                        int depth,
                        int alpha,
                        int beta,
                        boolean isMaximize,
                        Node node) {


        //If we have reached the max depth or there is already a winner, no need to check any further
        if (depth == 0 || getWinner(grid) != Player.NONE || isDraw(grid))
            return getScore(grid);

        if (isMaximize) {
            int maxValue = Integer.MIN_VALUE;

            for (int positionToPlay = 0; positionToPlay < sizeX; positionToPlay++) {

                Hole[][] newGrid = getGridCopy(grid);

                //If not valid no need to check any further
                if (!validMove(positionToPlay, newGrid))
                    continue;

                addCoin(positionToPlay, Player.COMPUTER, newGrid);

                Node childNode = new Node(alpha, beta, newGrid);

                int currentValue = miniMax(newGrid, depth - 1, alpha, beta, false, childNode);

                if (currentValue > maxValue)
                    maxValue = currentValue;

                alpha = Math.max(alpha, maxValue);

                childNode.setAlpha(alpha);
                childNode.setBeta(beta);
                childNode.setMax(true);
                childNode.setPosition(positionToPlay);
                node.addChild(childNode);
                if (beta <= alpha)
                    break;
            }
            return maxValue;
        } else {

            int minValue = Integer.MAX_VALUE;

            for (int positionToPlay = 0; positionToPlay < sizeX; positionToPlay++) {

                Hole[][] newGrid = getGridCopy(grid);

                //If not valid no need to check any further
                if (!validMove(positionToPlay, newGrid))
                    continue;
                addCoin(positionToPlay, Player.PLAYER, newGrid);

                Node childNode = new Node(alpha, beta, newGrid);
                int currentValue = miniMax(newGrid, depth - 1, alpha, beta, true, childNode);

                if (currentValue < minValue)
                    minValue = currentValue;
                beta = Math.min(beta, minValue);

                childNode.setAlpha(alpha);
                childNode.setBeta(beta);
                childNode.setMax(false);
                node.addChild(childNode);
                if (beta <= alpha)
                    break;
            }
            return minValue;
        }
    }

    private void makeComputerMove() {

        if (isFreePlay)
            return;
        Runnable runnable = () -> {
            Node root = new Node(Integer.MIN_VALUE, Integer.MAX_VALUE, this.grid);
            root.setMax(true);

            int depth;
            if (difficulty == 1)
                depth = 5;
            else if (difficulty == 2)
                depth = 7;
            else
                depth = 3;
            miniMax(this.grid, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true, root);

            makeMove(getBestOption(root));

        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private int getBestOption(Node root) {
        int max = Integer.MIN_VALUE;
        int option = -1;
        for (int i = 0; i < root.getChildren().size(); i++) {
            Node node = root.getChildren().get(i);
            if (option == -1 || node.getAlpha() > max) {
                max = node.getAlpha();
                option = node.getPosition();
            }
        }
        return option;
    }

    private void addCoin(int xIndex, Player player, Hole[][] grid) {
        for (int i = 0; i < grid[xIndex].length; i++) {
            Hole hole = grid[xIndex][i];
            if (hole == Hole.NONE) {
                grid[xIndex][i] = (player == Player.PLAYER ? Hole.PLAYER : Hole.COMPUTER);
                return;
            }
        }
    }

    private boolean validMove(int xIndex, Hole[][] grid) {
        return grid[xIndex][sizeY - 1] == Hole.NONE;
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

    public MutableLiveData<Boolean> isDrawLiveData() {
        return drawLiveData;
    }

    public LiveData<Player> getWinnerLiveData() {
        return winnerLiveData;
    }

    private Hole[][] getGridCopy(Hole[][] grid) {
        Hole[][] newGrid = new Hole[sizeX][sizeY];

        for (int x = 0; x < sizeX; x++)
            System.arraycopy(grid[x], 0, newGrid[x], 0, sizeY);

        return newGrid;
    }

    public LiveData<Player> getCurrentPlayer() {
        return currentPlayerLiveData;
    }

    public void onSaveGameClicked() {
        this.holeStorage.saveBoard(new SavedBoard(this.grid, hasWinner, isDraw, this.currentPlayer));
    }

    public void onStartGameClicked() {
        this.isFreePlay = false;
    }
}
