package com.example.connectfour.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connectfour.R;
import com.example.connectfour.model.Hole;

class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {

    private final Context context;
    private final OnCoinClickedListener onCoinClickedListener;
    private Hole[][] grid;
    private int sizeX, sizeY;

    GridAdapter(Context context, Hole[][] grid, int sizeX, int sizeY, OnCoinClickedListener onCoinClickedListener) {
        this.context = context;
        this.grid = grid;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.onCoinClickedListener = onCoinClickedListener;
    }

    void setGrid(Hole[][] grid) {
        this.grid = grid;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GridViewHolder(LayoutInflater.from(context).inflate(R.layout.coin_itemview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        int xIndex = getXIndex(position);
        int yIndex = position / (sizeY + 1);
        holder.bind(grid[xIndex][yIndex]);
    }

    @Override
    public int getItemCount() {
        return this.sizeX * this.sizeY;
    }

    private int getXIndex(int adapterPosition) {
        return adapterPosition % sizeX;
    }

    public interface OnCoinClickedListener {
        void onCoinClicked(int xIndex);
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        private ImageView coin;

        GridViewHolder(@NonNull View itemView) {
            super(itemView);
            this.coin = itemView.findViewById(R.id.coin);
            itemView.setOnClickListener(v -> onCoinClickedListener.onCoinClicked(getXIndex(getAdapterPosition())));
        }

        void bind(Hole hole) {
            if (hole != null)
                switch (hole) {
                    case COMPUTER:
                        coin.setImageResource(R.drawable.coin_computer);
                        return;
                    case PLAYER:
                        coin.setImageResource(R.drawable.coin_player);
                        return;
                    default:
                        coin.setImageDrawable(null);
                }
            else
                coin.setImageDrawable(null);
        }
    }

}
