package com.example.mexicantrain;

import android.app.Activity;
import android.util.Log;

public class Computer extends Player{

    Computer(Activity activity) {
        this.activity = activity;
    }

    @Override
    int play(Player humanPlayer, Player computerPlayer, Train mexicanTrain, Hand Boneyard, Tile tileToPlay, char trainToPlay) {

        Log.d("myTag", "computer turn finished.");
        return 0;
    }
}
