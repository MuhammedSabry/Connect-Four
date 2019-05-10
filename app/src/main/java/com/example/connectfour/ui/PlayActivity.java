package com.example.connectfour.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
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
        viewModel.getWinner().observe(this, this::onWinning);
        viewModel.isDraw().observe(this, isDraw -> this.onDraw());
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
        this.setHeader();
    }

    private void initViewModel() {
        this.viewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);
    }

    private void initViews() {
        initGridView();
        setHeader();
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

    private void setHeader() {
        binding.turnText.setText(getResources().getString(R.string.current_turn, viewModel.getCurrentPlayer() == Player.PLAYER ? "Your" : "Computer"));
        binding.turnText.setTextColor(getResources().getColor(getHeaderColor()));
    }

    private int getHeaderColor() {
        return viewModel.getCurrentPlayer() == Player.PLAYER ? R.color.player_color : R.color.computer_color;
    }

    @Override
    public void onCoinClicked(int xIndex) {
        viewModel.onCoinClicked(xIndex);
    }
}
