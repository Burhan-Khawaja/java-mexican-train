package com.example.mexicantrain;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class CoinFlip extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coin_flip);

        Random rng = new Random();
        int randomNum = rng.nextInt(2);

        ImageView coinPicture = (ImageView) findViewById(R.id.coinPicture);
        Button headsBtn = (Button) findViewById(R.id.headsBtn);
        headsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("myTag", "Test");
                coinPicture.setImageResource(R.drawable.coin_heads);
            }
        });
    }
}
