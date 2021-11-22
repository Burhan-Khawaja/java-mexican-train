package com.example.mexicantrain;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

public class Human extends Player {

    Human(Activity activity) {
        this.activity = activity;
    }

    public void testClickListener(){

    }
    /**
     * Displays the players hand. BURBUR this should be in game class b/c game handles view.
     * or just have view ahndle it and have a getter to get acopy of the data in the hand/train/
     * have game class call intent tto d isplay board.
     */
    public void displayHand() {

        TableRow row1 = (TableRow) this.activity.findViewById(R.id.playerHandRow1);
        TableRow row2 = (TableRow) this.activity.findViewById(R.id.playerHandRow2);
        TableRow row3 = (TableRow) this.activity.findViewById(R.id.playerHandRow3);
        TableRow row4 = (TableRow) this.activity.findViewById(R.id.playerHandRow4);

        for(int i = 0; i < playerHand.getSize(); i++) {
            //textView element that will store the current tile we are displaying in the table.
            TextView element = new TextView(this.activity);
            element.setText(playerHand.getTile(i).tileAsString());
            element.setPadding(5,5,5,5);
            Log.d("mytag","attempting to create event handler");
            element.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("mytag","onClick for element created.");

                }
            });
            if (i < 8) {
                row1.addView(element);
            }
            else if(i < 16) {
                row2.addView(element);
            }
            else if(i < 24) {
                row3.addView(element);
            }
            else {
                row4.addView(element);
            }
        }

    }
}
