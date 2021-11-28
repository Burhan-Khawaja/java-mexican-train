package com.example.mexicantrain;

import android.content.Context;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    static final String INTENT_COMPUTER_SCORE = "gameComputerScore";
    static final String INTENT_HUMAN_SCORE = "gameHumanScore";
    static final String INTENT_ROUND_NUMBER = "gameRoundNum";
    static final String INTENT_ROUND_ENGINEINT = "roundEngineInt";

    public LinearLayout gameFeed;// = new LinearLayout(this);
    public Game game = new Game(this);

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
        gameFeed = (LinearLayout) findViewById(R.id.gameInstructionLL);
        //Game game = new Game(this);
        if(getIntent() != null) {
            Log.d("myTag", "Get Intent is not null");
            if(getIntent().hasExtra(INTENT_COMPUTER_SCORE) ) {
                game.addComputerScore( getIntent().getIntExtra(INTENT_COMPUTER_SCORE, -1));
            }
            if(getIntent().hasExtra(INTENT_HUMAN_SCORE) ) {
                game.addHumanScore( getIntent().getIntExtra(INTENT_HUMAN_SCORE, -1));
            }
            if(getIntent().hasExtra(INTENT_ROUND_NUMBER) ) {
                game.setRoundNumber( getIntent().getIntExtra(INTENT_ROUND_NUMBER, -1));
            }
            if(getIntent().hasExtra(INTENT_ROUND_ENGINEINT)) {
                game.setEngineInt(getIntent().getIntExtra(INTENT_ROUND_ENGINEINT, -1));
            }
        }
        //increment round number.
        game.setRoundNumber(game.getRoundNumber() + 1);

        //set labels for round, computer, and human score
        TextView roundLabel = (TextView) findViewById(R.id.roundLabel);
        TextView computerScoreLabel = (TextView) findViewById(R.id.computerScoreLabel);
        TextView humanScoreLabel = (TextView) findViewById(R.id.humanScoreLabel);

        roundLabel.setText("Round: " + Integer.toString(game.getRoundNumber()));
        computerScoreLabel.setText("Computer Score: " + Integer.toString(game.getComputerScore()));
        humanScoreLabel.setText("Human Score: " + Integer.toString(game.getHumanScore()));


        game.playGame();
        //remove all tiles from players hand
 //       /*
        while( game.getHumanHand().getSize() > 0) {
            int i = 0;
            game.getHumanHand().removeTile(game.getHumanHand().getTile(i).getFirstNum(),game.getHumanHand().getTile(i).getSecondNum());
        }

        //BURBUR TESTING CODE
       // game.getHumanHand().addTile(new Tile(1,1));
        //game.getHumanHand().addTile(new Tile(1,8));
        game.getHumanHand().addTile(new Tile(9,8));
        game.getHumanHand().addTile(new Tile(9,9));
        game.getHumanHand().addTile(new Tile(9,9));
        game.getHumanHand().addTile(new Tile(9,9));
        game.getHumanHand().addTile(new Tile(9,9));
//*/
        displayTrains();
        displayComputerHand();
        displayHand();

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

    public void displayTrains() {

        LinearLayout allTrains = (LinearLayout) findViewById(R.id.allTrainsLayout);
        allTrains.removeAllViews();

        //create a new TextView that will store the train values.
        TextView humanTrainValues = new TextView(this);
        humanTrainValues.setText(game.getHumanAndComputerTrain());
        allTrains.addView(humanTrainValues);

        //display mexican train

        LinearLayout mexicanTrain = (LinearLayout) findViewById(R.id.mexicanTrainLL);
        mexicanTrain.removeAllViews();
        TextView mexicanTrainValue = new TextView(this);
        mexicanTrainValue.setText(game.getMexicanTrain());
        mexicanTrain.addView(mexicanTrainValue);
    }

    public void displayHand() {
        //BURBUR check for valid move BEFORE user makes a move
        //BURBUR need to get humanTrainPlayable and other trains too
        game.setPlayableTrainsHuman();

        //IF human is playing we have to check if they have a valid move before they play
        if(game.getHumanTurn()) {
            boolean doesHumanHaveMove = game.humanHasValidMove();
            if (doesHumanHaveMove == false) {
                TextView text = new TextView(this);
                Log.d("myTag", "The tile you picked from the boneyard is not playable, skipping turn.");
                //kickoff computer turn here.
                this.game.playTile(new Tile(-1,-1), ' ');
                //text.setText("Error: You have no move, and picked a tile from the boneyard");
                //gameFeed.addView(text, gameFeed.getChildCount() - 1);
                //set computer turn to true?
            }
        }

        Hand playerHand = game.getHumanHand();
        TableRow row1 = (TableRow) this.findViewById(R.id.playerHandRow1);
        TableRow row2 = (TableRow) this.findViewById(R.id.playerHandRow2);
        TableRow row3 = (TableRow) this.findViewById(R.id.playerHandRow3);
        TableRow row4 = (TableRow) this.findViewById(R.id.playerHandRow4);

        row1.removeAllViews();
        row2.removeAllViews();
        row3.removeAllViews();
        row4.removeAllViews();

        for(int i = 0; i < playerHand.getSize(); i++) {
            //textView element that will store the current tile we are displaying in the table.
            TextView element = new TextView(this);
            element.setText(playerHand.getTile(i).tileAsString());
            element.setPadding(5,5,5,5);
            Log.d("mytag","attempting to create event handler");
            element.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("mytag","onClick for tile created.");
                    trainSelection(element);
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
        displayComputerHand();

    }

    public void displayComputerHand() {
        Hand computerHand = game.getComputerHand();
        TableRow cRow1 = (TableRow) this.findViewById(R.id.computerHandRow1);
        TableRow cRow2 = (TableRow) this.findViewById(R.id.computerHandRow2);
        TableRow cRow3 = (TableRow) this.findViewById(R.id.computerHandRow3);
        TableRow cRow4 = (TableRow) this.findViewById(R.id.computerHandRow4);

        cRow1.removeAllViews();
        cRow2.removeAllViews();
        cRow3.removeAllViews();
        cRow4.removeAllViews();

        for(int i = 0; i < computerHand.getSize(); i++) {
            //textView element that will store the current tile we are displaying in the table.
            TextView element = new TextView(this);
            element.setText(computerHand.getTile(i).tileAsString());
            element.setPadding(5,5,5,5);
            Log.d("mytag","displaying tile from computer hand");

            if (i < 6) {
                cRow1.addView(element);
            }
            else if(i < 12) {
                cRow2.addView(element);
            }
            else if(i < 18) {
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

        /*
         * Create popup view that asks user to select what train to play on.
         */
        //layout inflater - "creates" a view from an xml file.
        LayoutInflater inflater = (LayoutInflater) this.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.train_choice, null);

        //create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        //last param is 'focusable'-- setting to true makes taps outside popup dismiss it.
        //PopupWindow popupWindowFinal = new PopupWindow(popupView, width,height, true);
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(this.findViewById(R.id.testCL), Gravity.CENTER, 0, 0);

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
                    Log.d("mytag", "Human Train Selected.");
                    popupWindow.dismiss();

                    humanPlaysTile(userTile, 'h');
                    //human finished its turn, so it has to be computers turn after this.

                    //displayTrains();
                    //displayHand();
                    //int value = game.playTile( userTile, 'h');
                    //if (value == 0) {
                    //}
                }
            });
        }

        //computer train button
        if(computerTrainSelection != null ) {
            //set onclicklistener for the human train button when a user chooses which train to play on
            computerTrainSelection.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("mytag", "Computer Train Selected.");
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
                    Log.d("mytag", "Mexican Train Selected.");
                    popupWindow.dismiss();
                    humanPlaysTile(userTile, 'm');
                }
            });
        }
        Log.d("mytag", "Redisplaying Hand");
    }

    void humanPlaysTile(Tile userTile, char userTrain) {
        int value = game.playTile( userTile, userTrain);

        //BURBUR THIS SHOULD BE IN GAME CLASS
        if(value > 0) {
            game.addComputerScore(value);
            Log.d("myTag",  "Computer Score: " + Integer.toString(game.getComputerScore()));
            //human won game, and hand is empty.
            //end round here,
            //game.playAgainPrompt
            //reset all data, display everything again.
            game.playAgain();
        }
        if (value == 0) {
            //human turn over, computer turn now.
            game.playTile(new Tile(-1, -1), ' ');
        }
        if(value == -22) {
            Log.d("myTag", "Error: User played on an invalid train. Choose again.");
            //user selected an invalid train.
            //gameFeed.addView( (TextView) TextView(this).setText("Error: You played on an invalid train"));
        }
        if(value == -123) {
            //human played a double, and is playing another tile.
        }
        displayTrains();
        displayHand();
    }
}