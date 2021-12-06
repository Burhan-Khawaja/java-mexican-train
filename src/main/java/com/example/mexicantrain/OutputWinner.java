package com.example.mexicantrain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class OutputWinner extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.output_winner);

        Button quitGame = (Button) findViewById(R.id.playAnotherRoundBTN);
        TextView whoWon = (TextView) findViewById(R.id.whoWon);
        TextView outputComputerScore = (TextView) findViewById(R.id.outputComputerScore);
        TextView outputHumanScore = (TextView) findViewById(R.id.outputHumanScore);

        Intent intentData = getIntent();

        int computerScore = intentData.getIntExtra("gameComputerScore", -1);
        int humanScore = intentData.getIntExtra("gameHumanScore", -1);
        int roundNum = intentData.getIntExtra("gameRoundNum", -1);


        outputComputerScore.setText("Computer Score: " + computerScore);
        outputHumanScore.setText("Human Score: " + humanScore);

        if(computerScore == humanScore) {
            whoWon.setText("The game is a tie!");
        }
        else if (humanScore > computerScore) {
            whoWon.setText("You Lost!");
        }
        else {
            whoWon.setText("You Won!");
        }

    }
}
