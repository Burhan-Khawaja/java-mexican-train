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

                /**
                 * 1- get the text from the view the user filled out
                 * 2- convert that text to a String called fileName
                 * 3- get the ID of the file using getResources.getIdentifier
                 * 4- create intent, add fileId to intent
                 * 5- Start the intent
                 */
                //1- get the text from the view the user filled out
                EditText loadGameName = (EditText) findViewById(R.id.loadGameName);
                Log.d("myTag", "Loading an existing game. game name: " + loadGameName.getText());
                //2- convert that text to a String called fileName
                String fileName = loadGameName.getText().toString();
                Log.d("myTag", "Filename as string: " + fileName);
                //get the integer id that represents the file we want to open.
                //3- get the ID of the file using getResources.getIdentifier
                //int fileId = getResources().getIdentifier(fileName,"raw", "com.example.mexicantrain");
                //Log.d("myTag", "fileID: " + Integer.toString(fileId));
                //create intent, add fileId to intent
                Intent loadGameIntent = new Intent(getBaseContext(), MainActivity.class);
                loadGameIntent.putExtra("loadGame", fileName);
                //start intent
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