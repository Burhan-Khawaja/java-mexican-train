package com.example.mexicantrain;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getIntent().hasExtra("hand")) {
            //TextView txt = (TextView) findViewById(R.id.editTextTextPersonName2);
            //txt.setText("ASDSADASDSADSAD");
        }

/*
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.userName);
       // textView.setText("TESTTESTETEST");
*/

        Log.d("mytag","testTag");
        this.setContentView(R.layout.activity_main);
        Round gameRound = new Round(this);
        gameRound.dealTiles();



        gameRound.displayHumanHand();
        gameRound.displayRoundState();
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