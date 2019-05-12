package com.example.connectfour.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connectfour.R;
import com.example.connectfour.databinding.ActivityPlayBinding;
import com.example.connectfour.model.Hole;
import com.example.connectfour.model.Player;
import com.example.connectfour.viewmodel.PlayerViewModel;

public class PlayActivity extends AppCompatActivity implements GridAdapter.OnCoinClickedListener {

    private ActivityPlayBinding binding;
    private GridAdapter gridAdapter;
    private PlayerViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity();
    }

    private void initActivity() {
        initDataBinding();
        initViewModel();
        initViews();
        viewModel.getGrid().observe(this, this::setGrid);
        viewModel.getWinnerLiveData().observe(this, this::onWinning);
        viewModel.isDrawLiveData().observe(this, isDraw -> this.onDraw());
        viewModel.getCurrentPlayer().observe(this, this::setHeader);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.play_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_action:
                viewModel.onSaveGameClicked();
                return true;
            case R.id.start_action:
                viewModel.onStartGameClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onDraw() {
        binding.turnText.setText(getResources().getString(R.string.draw_title));
    }

    private void onWinning(Player player) {
        if (player == Player.PLAYER)
            binding.turnText.setText(getResources().getString(R.string.player_win_title));
        else
            binding.turnText.setText(getResources().getString(R.string.computer_win_title));
        binding.turnText.setTextColor(getResources().getColor(android.R.color.holo_green_light));
    }

    private void setGrid(Hole[][] grid) {
        this.gridAdapter.setGrid(grid);
    }

    private void initViewModel() {
        Intent intent = getIntent();
        boolean isNewGame = intent.getBooleanExtra(MainActivity.IS_NEW_GAME_INTENT_EXTRA, true);
        boolean isCustomGame = intent.getBooleanExtra(MainActivity.IS_CUSTOM_GAME_INTENT_EXTRA, false);
        int difficulty = intent.getIntExtra(MainActivity.DIFFICULTY_INTENT_EXTRA, MainActivity.DIFFICULTY_EASY);
        this.viewModel = new PlayerViewModel(difficulty, isNewGame, isCustomGame);
    }

    private void initViews() {
        initGridView();
        setHeader(Player.PLAYER);
    }

    private void initGridView() {
        Hole[][] initialGrid = new Hole[viewModel.getSizeX()][viewModel.getSizeY()];

        gridAdapter = new GridAdapter(this,
                initialGrid,
                viewModel.getSizeX(),
                viewModel.getSizeY(),
                this);

        binding.grid.setLayoutManager(new GridLayoutManager(this,
                viewModel.getSizeX(),
                RecyclerView.VERTICAL,
                true));

        binding.grid.setAdapter(gridAdapter);
    }

    private void initDataBinding() {
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_play);
    }

    private void setHeader(Player player) {
        binding.turnText.setText(getResources().getString(R.string.current_turn, player == Player.PLAYER ? "Your" : "Computer"));
        binding.turnText.setTextColor(getResources().getColor(getHeaderColor(player)));
    }

    private int getHeaderColor(Player player) {
        return player == Player.PLAYER ? R.color.player_color : R.color.computer_color;
    }

    @Override
    public void onCoinClicked(int xIndex) {
        viewModel.onCoinClicked(xIndex);
    }
}
