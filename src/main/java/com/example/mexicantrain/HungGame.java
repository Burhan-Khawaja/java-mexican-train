package com.example.mexicantrain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * class called when the game is a draw.
 */
public class HungGame extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hung_game);
        //output that the game is a draw, and output updated human/computer score
        //okButton will take user to the play again prompt screen
        Button okButton = (Button) findViewById(R.id.okButton);

        //retrieve textviews that store human/computer score
        TextView humanScore = (TextView) findViewById(R.id.humanScore);
        TextView computerScore = (TextView) findViewById(R.id.computerScore);
        // set textview values by getting the data from the intent that called this page
        humanScore.setText("Human Score: " + getIntent().getIntExtra("gameHumanScore", -1));
        computerScore.setText("Computer Score: " + getIntent().getIntExtra("gameComputerScore", -1));

        //when user clicks ok, send them to play again screen
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //grab the intent with all the data we need, but
                // change the location so it points to main MainActivity
                Intent thisIntent = getIntent();
                thisIntent.setClass(getBaseContext(), playAgainScreen.class);
                startActivity(thisIntent);
            }
        });
    }
}
