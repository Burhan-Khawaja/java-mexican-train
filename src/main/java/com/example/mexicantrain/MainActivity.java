package com.example.mexicantrain;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        if(getIntent().hasExtra("hand")) {
            //TextView txt = (TextView) findViewById(R.id.editTextTextPersonName2);
            //txt.setText("ASDSADASDSADSAD");
        }



        //random code idk what this testing it
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.userName);
       // textView.setText("TESTTESTETEST");

/*
        Log.d("mytag","testTag");
        //Round testing

        Round gameRound = new Round(this);
        gameRound.dealTiles();




        test enginequeue and engine values.

        for(int i = 0; i < 100; i++) {
            Log.d("engineTest", String.valueOf(gameRound.getNextEngineValue()));
        }

         */

        Game game = new Game(this);
        game.playGame();
    }

    /**
     * Called when the user taps the Send button
     */
    public void sendMessage(View view) {

/*
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

 */

    }

}