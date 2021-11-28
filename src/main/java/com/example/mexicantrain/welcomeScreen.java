package com.example.mexicantrain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class welcomeScreen extends Activity {
    //public Game game = new Game(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        Button startNewGame = (Button) findViewById(R.id.startNewGame);
        Button loadGame = (Button) findViewById(R.id.loadGame);

        Intent intentStartGame = new Intent(this, MainActivity.class);

        startNewGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("mytag","Starting a FRESH game.");
                //intentStartGame.putExtra("gameRoundNum", game.getRoundNumber() );
                //intentStartGame.putExtra("gameHumanScore", game.getHumanScore());
                //intentStartGame.putExtra("gameComputerScore", game.getComputerScore());
            }
        });
    }
}
/*
    private int roundNumber;
    private int humanScore;
    private int computerScore;
    private Round round ;
    private Activity activity;
 */