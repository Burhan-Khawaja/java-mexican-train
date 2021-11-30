package com.example.mexicantrain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class welcomeScreen extends Activity {
    //public Game game = new Game(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        Button startNewGame = (Button) findViewById(R.id.startNewGame);
        Button loadGame = (Button) findViewById(R.id.loadGame);

        //intent and click listener for starting a new game
        Intent intentStartGame = new Intent(this, MainActivity.class);

        startNewGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("mytag","Starting a FRESH game.");
                startActivity(intentStartGame);
            }
        });

        loadGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText loadGameName = (EditText) findViewById(R.id.loadGameName);
                Log.d("myTag", "Loading an existing game. game name: " + loadGameName.getText());
                String fileName = loadGameName.getText().toString();
                Intent loadGameIntent = new Intent(getBaseContext(), MainActivity.class);

                loadGameIntent.putExtra("loadGame", fileName);
                startActivity(loadGameIntent);

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