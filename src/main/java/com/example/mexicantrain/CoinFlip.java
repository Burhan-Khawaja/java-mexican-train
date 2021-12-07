package com.example.mexicantrain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import androidx.constraintlayout.widget.ConstraintLayout;

public class CoinFlip extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coin_flip);

        Random rng = new Random();
        int randomNum = rng.nextInt(2);

        //imageView where we will change picture
        ImageView coinPicture = (ImageView) findViewById(R.id.coinPicture);
        //textview that will tell the user if they guessed right
        TextView coinTossOutput = (TextView) findViewById(R.id.coinTossOutput);
        //if randomNum == 0 then heads wins, else randomnum ==1 and tails wins
        //create Intent to return
        Intent returnIntent = new Intent();

        Button headsBtn = (Button) findViewById(R.id.headsBtn);
        headsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("myTag", "Test");
                if(randomNum == 0) {
                    coinPicture.setImageResource(R.drawable.coin_heads);
                    //set human turn to 1 in the intent, meaning true
                    returnIntent.putExtra("humanTurn", 1);
                    setResult(RESULT_OK, returnIntent);
                    coinTossOutput.setText("Congratulation! you guessed heads correctly, and it is your turn\nTap anywhere to continue");
                }
                else {
                    coinPicture.setImageResource(R.drawable.coin_tails);
                    //set human turn to 0 in the intent, meaning false
                    returnIntent.putExtra("humanTurn", 0);
                    setResult(RESULT_OK, returnIntent);
                    coinTossOutput.setText("you guessed heads and it was wrong. It is the computer's turn\nTap anywhere to continue");

                }
            }
        });

        Button tailsBtn = (Button) findViewById(R.id.tailsBtn);
        tailsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("myTag", "Test");
                if(randomNum == 0) {
                    coinPicture.setImageResource(R.drawable.coin_heads);
                    //set human turn to 1 in the intent, meaning true
                    returnIntent.putExtra("humanTurn", 0);
                    setResult(RESULT_OK, returnIntent);
                    coinTossOutput.setText("you guessed tails and it was wrong. It is the computer's turn\nTap anywhere to continue");

                }
                else {
                    coinPicture.setImageResource(R.drawable.coin_tails);
                    //set human turn to 0 in the intent, meaning false
                    returnIntent.putExtra("humanTurn", 1);
                    setResult(RESULT_OK, returnIntent);
                    coinTossOutput.setText("Congratulation! you guessed tails correctly, and it is your turn\nTap anywhere to continue");

                }
            }
        });
        tapAnywhereToContinue();

    }

    private void tapAnywhereToContinue() {
        ConstraintLayout coinFlipLayout = (ConstraintLayout) findViewById(R.id.coinFlipLayout);
        coinFlipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }
}
