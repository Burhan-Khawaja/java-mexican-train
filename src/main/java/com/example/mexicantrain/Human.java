package com.example.mexicantrain;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;

public class Human extends Player {

    Human(Activity activity) {
        this.activity = activity;
    }

    public Hand getHand() {
        return this.playerHand;
    }

    public Train getTrain() {
        return this.playerTrain;
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
                    Log.d("mytag","onClick for tile created.");
                    userSelectedTile(element);
                }
            });
            if (i < 6) {
                row1.addView(element);
            }
            else if(i < 12) {
                row2.addView(element);
            }
            else if(i < 18) {
                row3.addView(element);
            }
            else {
                row4.addView(element);
            }
        }

    }

    /**
     * function called whenever a user selects a tile. The onclick will call this function that handles the game logic.
     * @param tilePlayed TextView object that is passed in that holds the tile representation that the user clicked on
     */
    public void userSelectedTile(TextView tilePlayed) {
        Log.d("gameplay", tilePlayed.getText().toString());

        /*
        * Create popup view that asks user to select what train to play on.
         */
        //layout inflater - "creates" a view from an xml file.
        LayoutInflater inflater = (LayoutInflater) this.activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.train_choice, null);

        //create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        //instead of using a final char, idk what that is,
        // make 3 booleans and set the one chosen to true.

        final char[] test = {new Character(' ')};

        //last param is 'focusable'-- setting to true makes taps outside popup dismiss it.
        //PopupWindow popupWindowFinal = new PopupWindow(popupView, width,height, true);
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(this.activity.findViewById(R.id.testCL), Gravity.CENTER, 0, 0);

        //create onclick listeners to all buttons so we can handle what user selects
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;

            }
        });


        //get buttons to make on click listeners for them
        //use inflator to access things from different views
        //we have the popupView from before that has the train_choice.xml view "stored" in it and we can
        //access all its values that way.

        Button humanTrainSelection  = (Button) popupView.findViewById( R.id.selectHumanTrain);
        Button computerTrainSelection = (Button) popupView.findViewById( R.id.selectComputerTrain);
        Button mexicanTrainSelection = (Button) popupView.findViewById( R.id.selectMexicanTrain);


        //have to make sure that the button isnt null -ie we have initialized the view its in before we set the listener for it.
        //human train button

        if(humanTrainSelection != null ) {
            //set onclicklistener for the human train button when a user chooses which train to play on
            humanTrainSelection.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    test[0] = 'h';
                    Log.d("mytag", "Human Train Selected.");
                    Log.d("mytag", String.valueOf(test[0]));
                    popupWindow.dismiss();
                    userPlayedTile(tilePlayed, 'h');
                }
            });
        }

        //computer train button
        if(computerTrainSelection != null ) {
            //set onclicklistener for the human train button when a user chooses which train to play on
            computerTrainSelection.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    test[0] = 'c';
                    Log.d("mytag", "Computer Train Selected.");
                    Log.d("mytag", String.valueOf(test[0]));
                    popupWindow.dismiss();
                    userPlayedTile(tilePlayed, 'c');

                }
            });
        }

        //mexican train button
        if(mexicanTrainSelection != null ) {
            //set onclicklistener for the human train button when a user chooses which train to play on
            mexicanTrainSelection.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    test[0] = 'm';
                    Log.d("mytag", "Mexican Train Selected.");
                    Log.d("mytag", String.valueOf(test[0]));
                    popupWindow.dismiss();
                    userPlayedTile(tilePlayed, 'm');

                }
            });
        }
    }

    //check if tile fits on train and see if its a double
    private void userPlayedTile(TextView tilePlayed, char trainPlayedOn) {
        String tileAsString = tilePlayed.getText().toString();
        Tile userTile = new Tile(tileAsString.charAt(0) - 48, tileAsString.charAt(4) - 48);

        //user played on human train
        if(trainPlayedOn == 'h'){
            this.playerTrain.addTileBack(userTile);
            return;
        }
        else if (trainPlayedOn == 'c') {

        }

    }

    int play(Player humanPlayer, Player computerPlayer, Train mexicanTrain, Hand Boneyard) {


        return 0;
    }
}
