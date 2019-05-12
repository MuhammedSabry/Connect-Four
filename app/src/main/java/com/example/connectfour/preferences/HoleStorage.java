package com.example.connectfour.preferences;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.connectfour.ConnectApp;
import com.example.connectfour.model.SavedBoard;
import com.example.connectfour.preferences.base.ObjectPreference;

public class HoleStorage {

    private final ObjectPreference<SavedBoard> savedBoardPreference;

    public HoleStorage() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ConnectApp.getContext());
        savedBoardPreference = new ObjectPreference<>(sharedPreferences,
                "pref_board",
                null,
                SavedBoard.class);
    }

    public void saveBoard(SavedBoard savedBoard) {
        savedBoardPreference.set(savedBoard);
    }

    public SavedBoard getSavedBoard() {
        return savedBoardPreference.get();
    }
}
