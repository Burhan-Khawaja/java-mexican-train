package com.example.mexicantrain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    static final String INTENT_COMPUTER_SCORE = "gameComputerScore";
    static final String INTENT_HUMAN_SCORE = "gameHumanScore";
    static final String INTENT_ROUND_NUMBER = "gameRoundNum";
    static final String INTENT_ROUND_ENGINEINT = "roundEngineInt";
    static final String INTENT_LOAD_GAME_NAME = "loadGame";

    public Game game = new Game(this);

    /**
     * get data ready for game
     * @param savedInstanceState Bundle object called by default with android studio
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get make A Move button on game screen and set its on click listener to call makeAMove function
        Button makeAMoveButton = (Button) findViewById(R.id.makeMoveButton);
        makeAMoveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("mytag","makeAMove onclick created.");
                makeAMove();
            }
        });

        //get help button on game screen and set its on click listener to provide human player with help
        Button helpButton = (Button) findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("mytag","helpButton onclick created.");
                humanHelp();
            }
        });

        //get save game button and set the on click listener to open the save game popup,
        Button saveGame = (Button) findViewById(R.id.saveGameButton);
        saveGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("mytag","save button onclick created.");
                saveGamePopup();
            }
        });

        //get save game button and set the on click listener to go to start screen.
        Button quitGame = (Button) findViewById(R.id.quitGameButton);
        quitGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent quitGame = new Intent(getBaseContext(), welcomeScreen.class);
                startActivity(quitGame);
            }
        });
        //create variable that will track whether we are loading a game
        boolean serializedStart = false;

        //Load intent data
        if(getIntent() != null) {
            Log.d("myTag", "Get Intent is not null");
            //starting a new round, with human/computer/round number/engine passed in so we have and can
            //keep track of the values

            //get computer score
            if(getIntent().hasExtra(INTENT_COMPUTER_SCORE) ) {
                game.addComputerScore( getIntent().getIntExtra(INTENT_COMPUTER_SCORE, -1));
            }
            //get human score
            if(getIntent().hasExtra(INTENT_HUMAN_SCORE) ) {
                game.addHumanScore( getIntent().getIntExtra(INTENT_HUMAN_SCORE, -1));
            }
            //get round number
            if(getIntent().hasExtra(INTENT_ROUND_NUMBER) ) {
                game.setRoundNumber( getIntent().getIntExtra(INTENT_ROUND_NUMBER, -1));
            }
            //get current engine
            if(getIntent().hasExtra(INTENT_ROUND_ENGINEINT)) {
                game.setEngineInt(getIntent().getIntExtra(INTENT_ROUND_ENGINEINT, -1));
            }

            //clear all markers and orphan doubles possibly lingering from last round
            game.resetValues();
            //load game. open the file in mainActivity and pass it to game class
            // so it can manipulate the data/file as needed.
            if(getIntent().hasExtra(INTENT_LOAD_GAME_NAME)) {
                //set serialized start to true since we are loading a game
                serializedStart = true;
                //get file name and store it as a string
                String fileName = getIntent().getStringExtra(INTENT_LOAD_GAME_NAME);

                //access the file and read context of it by using a fileInputStream
                try {
                    //create file input stream, then create input stream reader, and
                    //finally create a buffered reader.
                    FileInputStream fis = getBaseContext().openFileInput(fileName);
                    InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
                    BufferedReader fileReader = new BufferedReader(inputStreamReader);
                    //load the game
                    game.loadGame(fileReader);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //done with intents here, begin the game

        //increment round number.
        game.setRoundNumber(game.getRoundNumber() + 1);

        //set labels for round, computer, and human score to the appropriate value
        //get labels
        TextView roundLabel = (TextView) findViewById(R.id.roundLabel);
        TextView computerScoreLabel = (TextView) findViewById(R.id.computerScoreLabel);
        TextView humanScoreLabel = (TextView) findViewById(R.id.humanScoreLabel);
        //set labels
        roundLabel.setText("Round: " + Integer.toString(game.getRoundNumber()));
        computerScoreLabel.setText("Computer Score: " + Integer.toString(game.getComputerScore()));
        humanScoreLabel.setText("Human Score: " + Integer.toString(game.getHumanScore()));


        game.playGame(serializedStart);

        //check who goes first. we only do this when we are NOT loading in a game
        if(game.getHumanScore() == game.getComputerScore() && !serializedStart) {
            Intent coinFlip = new Intent(this, CoinFlip.class);
            //0 is request code and represents MainActivity
            startActivityForResult(coinFlip, 0);
        }
        else if(game.getHumanScore() > game.getComputerScore() && !serializedStart ){
            game.setComputerTurn();
        }
        else if (game.getHumanScore() < game.getComputerScore() && !serializedStart) {
            game.setHumanTurn();
        }
        //display trains and hands.
        displayTrains();
        displayComputerHand();
        displayHand();
    }

    /**
     * function that deals with data sent back by coin flip, and determines who goes first.
     * @param requestCode integer value represents what intent was finished
     * @param resultCode integer value that represents how the intent finished (ie a confirmation code)
     * @param data intent that was recieved
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            //get the value from the coin flip intent.
            int humanTurnResult = data.getIntExtra("humanTurn", -1);
            //if the coin flip result is 1, that means the human guessed correctly and it is their turn
            if(humanTurnResult == 1) {
                game.setHumanTurn();
            }
            else {
                game.setComputerTurn();
            }
        }
        displayHand();
        displayTrains();
        displayComputerHand();
    }


    /**
     * handle the popup for when the user wants to save a game, and create fileOutputStream object
     */
    private void saveGamePopup() {
        //create layout Inflator to create a popup menu to ask the user what they want the save game name to be.
        LayoutInflater inflater = (LayoutInflater) this.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.save_game, null);

        //create the popup window dimensions
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        //show popup window
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(this.findViewById(R.id.testCL), Gravity.CENTER, 0, 0);

        //get button that user clicks to save in the popup.
        Button saveAndQuitBtn = (Button) popupView.findViewById(R.id.Save);
        //when the user clicks the button, get the value they entered and call the saveGame function and begin saving everything to that file.
        if(saveAndQuitBtn != null ) {
            //set on click listener
            saveAndQuitBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //access textView that user entered the save name in, and get the data
                    EditText rawSaveName = (EditText) popupView.findViewById(R.id.saveGameName);
                    //convert it to a string
                    String saveName = rawSaveName.getText().toString();
                    FileOutputStream fileOutput = null;
                    try {
                        //create the fileOutputStream object with the string we received from above,
                        //and call the game saveGame function.
                        fileOutput = openFileOutput(saveName, MODE_PRIVATE);
                        game.saveGame(fileOutput);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * output help for the human.
     */
    private void humanHelp() {
        //get human move, add it to the bottom of the screen, clear human move explanation so the human
        //can continue to play tiles
        game.getBestHumanMove();
        addGameFeedMessage(game.getHumanMoveExplanation());
        game.clearHumanMoveExplanation();
    }

    /**
     * Display trains on the screens
     */
    public void displayTrains() {
        //get layout where human and computer train will reside.
        LinearLayout allTrains = (LinearLayout) findViewById(R.id.allTrainsLayout);
        //clear any lingering data so we can add new data
        allTrains.removeAllViews();

        //create a new TextView that will store the train values.
        TextView humanTrainValues = new TextView(this);
        //set the text to be the human and computer train with the engine
        humanTrainValues.setText(game.getHumanAndComputerTrain());
        allTrains.addView(humanTrainValues);

        //display mexican train
        //get view that holds mexican train output
        LinearLayout mexicanTrain = (LinearLayout) findViewById(R.id.mexicanTrainLL);
        //clear lingering data
        mexicanTrain.removeAllViews();
        //create new TextView, add mexican train as a string and append it to textview.
        TextView mexicanTrainValue = new TextView(this);
        mexicanTrainValue.setText(game.getMexicanTrain());
        mexicanTrain.addView(mexicanTrainValue);
    }

    /**
     * display the human and computer hand,and check if human has valid move, and update whose turn it is
     */
    public void displayHand() {
        //set humans playable trains so we can check if they have a valid move
        game.setPlayableTrainsHuman();
        //IF human is playing we have to check if they have a valid move before they play
        if(game.getHumanTurn()) {
            boolean doesHumanHaveMove = game.humanHasValidMove();
            if (doesHumanHaveMove == false) {
                TextView text = new TextView(this);
                if(game.getBoneyardHand().getSize() ==0 ) {
                    game.setSkippedHumanTurn(true);
                }
            }
        }
        //UPDATE WHOSE TURN IT IS
        TextView whoseTurn = (TextView) findViewById(R.id.whoseTurnLabel);
        if (game.getHumanTurn()) {
            whoseTurn.setText("Human Turn");
        }
        else {
            whoseTurn.setText("Computer Turn");
        }

        //begin printint out player hand
        // get player hand as Hand object, and get the rows will place the tiles on
        Hand playerHand = game.getHumanHand();
        TableRow row1 = (TableRow) this.findViewById(R.id.playerHandRow1);
        TableRow row2 = (TableRow) this.findViewById(R.id.playerHandRow2);
        TableRow row3 = (TableRow) this.findViewById(R.id.playerHandRow3);
        TableRow row4 = (TableRow) this.findViewById(R.id.playerHandRow4);

        //clear all the rows of any lingering data
        row1.removeAllViews();
        row2.removeAllViews();
        row3.removeAllViews();
        row4.removeAllViews();

        //loop through the hand, create a textview and set its text to be a tile, then add to row.
        for(int i = 0; i < playerHand.getSize(); i++) {
            //textView element that will store the current tile we are displaying in the table.
            TextView element = new TextView(this);
            element.setText(playerHand.getTile(i).tileAsString());
            element.setPadding(5,5,5,5);
            //element.setBackgroundResource(R.drawable.testd);
            element.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    trainSelection(element);
                }
            });

            if (i < 9) {
                row1.addView(element);
            }
            else if(i < 18) {
                row2.addView(element);
            }
            else if(i < 27) {
                row3.addView(element);
            }
            else {
                row4.addView(element);
            }
        }

        //display top of boneyard
        TextView topOfBoneyard = (TextView) findViewById(R.id.topOfBoneyard);
        Tile boneyard = game.getTopOfBoneyard();
        //getTopOfBoneyard return -1,-1 if its empty, and we can check for that and output "empty" instead of having
        //it be blank on the gui.
        if(boneyard.getFirstNum() == -1 && boneyard.getSecondNum() == -1) {
            topOfBoneyard.setText("Empty");
        }
        else {
            topOfBoneyard.setText(boneyard.tileAsString());
        }
        displayComputerHand();

    }

    /**
     * display the computer hand
     */
    public void displayComputerHand() {
        //get computer hand as a Hand object
        Hand computerHand = game.getComputerHand();
        //get the rows where we will be printing out computer tiles
        TableRow cRow1 = (TableRow) this.findViewById(R.id.computerHandRow1);
        TableRow cRow2 = (TableRow) this.findViewById(R.id.computerHandRow2);
        TableRow cRow3 = (TableRow) this.findViewById(R.id.computerHandRow3);
        TableRow cRow4 = (TableRow) this.findViewById(R.id.computerHandRow4);
        //clear the rows so the things in them previously are gone
        cRow1.removeAllViews();
        cRow2.removeAllViews();
        cRow3.removeAllViews();
        cRow4.removeAllViews();

        //loop through the hand, create a textview and set its text to be a tile, then add to row.
        for(int i = 0; i < computerHand.getSize(); i++) {
            //textView element that will store the current tile we are displaying in the table.
            TextView element = new TextView(this);
            element.setText(computerHand.getTile(i).tileAsString());
            element.setPadding(5,5,5,5);
            if (i < 9) {
                cRow1.addView(element);
            }
            else if(i < 18) {
                cRow2.addView(element);
            }
            else if(i < 27) {
                cRow3.addView(element);
            }
            else {
                cRow4.addView(element);
            }
        }

    }

    /**
     * function called whenever a user selects a tile. asks user what train to play on, and passes data off to different function.
     * @param tilePlayed TextView object that is passed in that holds the tile representation that the user clicked on
     */
    public void trainSelection(TextView tilePlayed) {
        Log.d("gameplay", tilePlayed.getText().toString());

        //turn tilePlayed from textView to Tile
        String tileAsString = tilePlayed.getText().toString();
        Tile userTile = new Tile(tileAsString.charAt(0) - 48, tileAsString.charAt(4) - 48);


        //Create popup view that asks user to select what train to play on.

        //layout inflater - "creates" a view from an xml file.
        LayoutInflater inflater = (LayoutInflater) this.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.train_choice, null);

        //create the popup window dimensions
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        //last param is 'focusable'-- setting to true makes taps outside popup dismiss it.
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(this.findViewById(R.id.testCL), Gravity.CENTER, 0, 0);

        //set on touch listener to dismiss popup
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;

            }
        });
        //create onclick listeners to all buttons so we can handle what user selects

        //get buttons to make on click listeners for them
        //use inflater to access things from different views
        //we have the popupView from before that has the train_choice.xml view "stored" in it and we can
        //access all its values that way.

        Button humanTrainSelection  = (Button) popupView.findViewById( R.id.selectHumanTrain);
        Button computerTrainSelection = (Button) popupView.findViewById( R.id.selectComputerTrain);
        Button mexicanTrainSelection = (Button) popupView.findViewById( R.id.selectMexicanTrain);


        //have to make sure that the button is not null
        // ie we have initialized the view its in before we set the listener for it.
        //human train button
        if(humanTrainSelection != null ) {
            //set onclicklistener for the human train button when a user chooses which train to play on
            humanTrainSelection.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    popupWindow.dismiss();
                    humanPlaysTile(userTile, 'h');

                }
            });
        }

        //computer train button
        if(computerTrainSelection != null ) {
            //set onclicklistener for the human train button when a user chooses which train to play on
            computerTrainSelection.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    popupWindow.dismiss();
                    humanPlaysTile(userTile, 'c');
                }
            });
        }

        //mexican train button
        if(mexicanTrainSelection != null ) {
            //set onclicklistener for the human train button when a user chooses which train to play on
            mexicanTrainSelection.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    popupWindow.dismiss();
                    humanPlaysTile(userTile, 'm');
                }
            });
        }
    }

    /**
     * funciton called when user plays a tile, deals with response and checks if game is over
     * @param userTile tile user selected to play
     * @param userTrain train user selected to play on
     */
    void humanPlaysTile(Tile userTile, char userTrain) {
        //value stores the response to the user playing a tile
        //-666 means boneyard is empty and no move,
        //0 means valid move and to set computer turn
        //any positive integer means the human won and that it is the value of the sum of computer pips
        int value = game.playTile( userTile, userTrain);

        //If value returned is -666, then boneyard is empty and human turn is skipped.
        game.setSkippedHumanTurn(value == -666);
        game.checkHungGame();
        addGameFeedMessage(game.getHumanMoveExplanation());
        game.clearHumanMoveExplanation();

        //if value is positive, human won game
        if(value > 0) {
            game.addComputerScore(value);
            //reset all data, display everything again.
            game.playAgain();
        }
        if (value == 0) {
            //check if user created any orphan doubles
            game.setHumanPlayerOrphanDoubles();
            game.setComputerTurn();
            //human turn over, computer turn now.
        }
        displayTrains();
        displayHand();
    }

    /**
     * Make a move and play a tile. This function is used for the computer player to play a tile
     */
    public void makeAMove() {

        if(game.getHumanTurn()) {
            //add to box click a tile and select a train to play
        }
        else if(!game.getHumanTurn()) {
            //pass in a tile of -1,-1 and empty train so we know its computer turn
            //the comptuer will figure out the best tile to play
            int value = this.game.playTile(new Tile(-1,-1), ' ');
            //set computerSkippedTurn boolean true if value is -666,meaning that
            //the boneyard wsa empty and computer had no playable tile.
            game.setSkippedComputerTurn(value == -666);
            if(value > 0 ){
                //computer won game!
                game.addHumanScore(value);
                game.playAgain();
            }
            game.checkHungGame();

            //set human turn to true, add what computer did, and refresh screen
            game.setHumanTurn();
            addGameFeedMessage(game.getComputerMoveExplanation());
            game.clearComputerMoveExplanation();
            game.clearHumanMoveExplanation();//BURBUR double check clearing here is where it should be cleared
            displayTrains();
            displayHand();
        }
    }

    /**
     * Add a message to the game feed
     * @param s String that is the message we want to add to the screen for the user to see
     */
    public void addGameFeedMessage(String s) {
        LinearLayout gameFeed = (LinearLayout) findViewById(R.id.gameInstructionLL);
        //gameFeed.removeAllViews();if removing feed doesnt work then just copy what is in the feed.
        TextView tmpText = new TextView(this);
        tmpText.setText(s);
        gameFeed.addView(tmpText);
    }

}