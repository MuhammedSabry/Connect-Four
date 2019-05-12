package com.example.connectfour.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.connectfour.R;
import com.example.connectfour.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String IS_CUSTOM_GAME_INTENT_EXTRA = "custom_game_extra";
    public static final String DIFFICULTY_INTENT_EXTRA = "difficulty_extra";
    public static final String IS_NEW_GAME_INTENT_EXTRA = "new_game_extra";
    public static int DIFFICULTY_EASY = 0;
    public static int DIFFICULTY_MED = 1;
    public static int DIFFICULTY_HARD = 2;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setHandler(this);
    }

    public void onPlayClicked() {
        startPlaying(true, false);
    }

    public void onLoadGameClicked() {
        startPlaying(false, false);
    }

    public void onCustomBoardClicked() {
        startPlaying(false, true);
    }

    private void startPlaying(boolean isNew, boolean isCustom) {
        Intent intent = new Intent(this, PlayActivity.class);

        int checkedRadioButtonId = binding.radioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.easy)
            intent.putExtra(DIFFICULTY_INTENT_EXTRA, DIFFICULTY_EASY);
        else if (checkedRadioButtonId == R.id.medium)
            intent.putExtra(DIFFICULTY_INTENT_EXTRA, DIFFICULTY_MED);
        else
            intent.putExtra(DIFFICULTY_INTENT_EXTRA, DIFFICULTY_HARD);

        intent.putExtra(IS_NEW_GAME_INTENT_EXTRA, isNew);
        intent.putExtra(IS_CUSTOM_GAME_INTENT_EXTRA, isCustom);

        startActivity(intent);
    }
}
