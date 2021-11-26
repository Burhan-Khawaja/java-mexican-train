package com.example.mexicantrain;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

public class Round {
    //CLASS VARIABLES
    private Hand boneyard = new Hand();
    private Train mexicanTrain = new Train();
    private Human humanPlayer = new Human(this.activity);
    private Human computerPlayer = new Human(this.activity);
    private Activity activity;

    private int engineInt;

    private Queue<Integer> engineQueue = new ArrayDeque<Integer>();
    private boolean humanTurn;
    private boolean computerTurn;
    private boolean computerWon;
    private boolean humanWon;

    //test value.
    private boolean humanHasMove;
//burbur depreciated roundNumber stores this now    private int currentRound;

    //BURBUR TESTING STORING HUMAN/COMPUTER/ROUND NUMBER IN ROUND CLASS
    private int humanScore;
    private int computerScore;
    private int roundNumber;

    //CONSTRUCTOR
    Round(Activity activity) {
        this.activity = activity;
        //set activity through a setter b/c if we use constructor, then this.activity is not initialized,
        // and we set it to a null value in other classes.
        humanPlayer.setActivity(this.activity);

    }

//================================//================================//================================//================================
    //SELECTORS

    //MUTATORS

    //HELPER FUNCTIONS
    /**
     * create a double nine set of tiles, as well as shuffle the tiles and deal the tiles to the human and computer player.
     */
    public void dealTiles(){

        //track if we should add tiles to human players hand, computer players hand, or boneyard.
        int totalTileCreated = 0;
        //arraylist to store all tiles
        ArrayList<Tile> tiles = new ArrayList<>();

        //create double nine set of tiles.
        for (int i = 0; i < 10; i++) {
            for (int j = i; j < 10; j++) {
                tiles.add(new Tile(i,j));
            }
        }

        //shuffle all tiles
        Random rng = new Random();

        for (int i = 0; i < tiles.size(); i++) {
            int randomNum = rng.nextInt(tiles.size() - 1);
            Tile tmpTile = tiles.get(i);

            //boneyard.getTile(i) = boneyard.getTile(randomNum);
            tiles.set(i, tiles.get(randomNum));
            //boneyard[randomNum] = tmpTile;
            tiles.set(randomNum, tmpTile);
        }

        //deal tiles to human/computer/boneyard
        for(int i = 0; i < tiles.size(); i++) {
            if(i < 16) {
                humanPlayer.addTileToHand(tiles.get(i));
            }
            else if (i < 32) {
                //Add tiles to computer players hand.
                // computer.addTileToHand(new Tile(i, j));
            }
            else {
                boneyard.addTile(tiles.get(i));
            }
        }

    }



    /**
     * reset the queue of engine tiles to be 9-0 descending.
     */
    public void resetEngineQueue() {
        this.engineQueue.clear();
        //is there a better way of doing this? probably. Just refactor it later lol.
        //at the very least its a simple and understandable solution. just add 9-0 to the queue one at a time.
        this.engineQueue.add(9);
        this.engineQueue.add(8);
        this.engineQueue.add(7);
        this.engineQueue.add(6);
        this.engineQueue.add(5);
        this.engineQueue.add(4);
        this.engineQueue.add(3);
        this.engineQueue.add(2);
        this.engineQueue.add(1);
        this.engineQueue.add(0);

    }

    /**
     * get the next value in the queue of engine values
     * @return integer value that is the next engine value
     */
    public int getNextEngineValue() {
        if(engineQueue.isEmpty()) {
            resetEngineQueue();
        }
        this.engineInt = engineQueue.remove();
        return engineInt;
    }

    public int startRound(boolean serializedStart, int humanScore, int computerScore, int roundNumber) {


        if(!serializedStart) {
            this.roundNumber = 0;
            dealTiles();
            //BURBUR REMOVE ENGINE TILE
            this.engineInt = getNextEngineValue();
            setTrainEndNumbers();
/*
            //BURBUR for testing purposes delete all tiles in users hand
            while( humanPlayer.playerHand.getSize() > 0) {
                int i = 0;
                humanPlayer.playerHand.removeTile(humanPlayer.playerHand.getTile(i).getFirstNum(),humanPlayer.playerHand.getTile(i).getSecondNum());
            }

            //BURBUR TESTING CODE
            humanPlayer.addTileToHand(new Tile(1,1));
            humanPlayer.addTileToHand(new Tile(1,8));
            humanPlayer.addTileToHand(new Tile(8,8));

*/
            humanPlayer.addTileToHand(new Tile(9,9));
            //BURBUR who goes first
            humanTurn = true;
        }
        this.roundNumber = roundNumber;
        this.humanScore = humanScore;
        this.computerScore = computerScore;
        //int pipsValue = startTurns(humanPlayer,humanPlayer,mexicanTrain,boneyard, new Tile(-1,-1), ' ');
        return 0;
    }

    private void setTrainEndNumbers() {
        mexicanTrain.setTrainEndNumber(this.engineInt);
        humanPlayer.playerTrain.setTrainEndNumber(this.engineInt);

    }

    public int playTile(Tile userTile, char trainToPlayOn) {
        int humanPipsValue = 0;
        int computerPipsValue = 0;
        //LinearLayout gameFeed = (LinearLayout) this.activity.findViewById(R.id.gameInstructionLL);
        //TextView bottomText = (TextView) new TextView(this.activity);
        if(humanTurn) {
            //bottomText.setText("Humans turn. Select a tile to play.");
            //gameFeed.addView(bottomText);
            //displayRoundState();
            humanPipsValue = humanPlayer.play(humanPlayer, humanPlayer, mexicanTrain, boneyard, userTile, trainToPlayOn);

            //THIS LOGIC SHOULD BE HANDELED IN CONTROLLER CLASS
            //user played a double tile.
            if (humanPipsValue == -123) {
                //user played 1 double tile. redisplay hand and tell them to select another tile.
                //bottomText.setText("You played a double tile! Select another tile to play!");
                Log.d("doubleTesting", "User played a double tile, displaying hand again.");
            }


            //users turn is over
            if (humanPipsValue == 0) {
                humanTurn = false;
                computerTurn = true;
            }

            if (humanPipsValue > 0) {
                //human hand is empty, and the human won the round

            }
        }

        if(computerTurn == true) {
            Log.d("myTag", "COMPUTER TURN");
            //bottomText.setText("COMPUTER TURN!");
            //gameFeed.addView(bottomText);

            //computerPlayer.play()
            computerTurn = false;
            humanTurn = true;
            //after computer is done with its turn, display round state.
            //displayRoundState();

            TextView bottomText2 = (TextView) new TextView(this.activity);
            bottomText2.setText("Computers Turn finished, Humans turn now. Select a tile.");
            return 0;

        }
        //gameFeed.addView(bottomText2);
        return 0;
    }


    public void playerHasValidMove() {
        boolean existsValidMove = humanPlayer.existsValidMove(humanPlayer, humanPlayer, mexicanTrain);

        //player does not have a valid move.
        if (existsValidMove == false) {
            //go through procedure for when a user has no playable tiles.
            boolean skipTurn = humanPlayer.noPlayableTiles(humanPlayer, humanPlayer, mexicanTrain, boneyard);
            //the tile the user picked is not playable,so place a marker on their train and skip their turn
            if (skipTurn == false) {
                //tile drawn is not playable, place marker and skip turn
                //BURBUR NEED TO ADD MARKER TO TRAIN.
                //bottomText.setText("The tile you picked from the boneyard is not playable.");
            }
            else { //tile picked is playable. play it.
                //bottomText.setText("The tile you picked from the boneyard is playable. Restarting turn.");
            }

        }
    }

    public String getHumanAndComputerTrain(){
        String trains = humanPlayer.trainAsString() + " " + engineInt + "-" + engineInt + " " + computerPlayer.trainAsString();
        return trains;
    }

    public Hand getHumanHand() {
        return this.humanPlayer.getHand();
    }
}
/*
    boolean playerHasValidMove = humanPlayer.existsValidMove(humanPlayer, humanPlayer, mexicanTrain);

        if (playerHasValidMove == false) {
                //get bottomText so we can update it based on what happens
                LinearLayout gameFeed = (LinearLayout) this.activity.findViewById(R.id.gameInstructionLL);
                TextView bottomText = (TextView) new TextView(this.activity);
                //go through procedure for when a user has no playable tiles.
                boolean skipTurn = humanPlayer.noPlayableTiles(humanPlayer, humanPlayer, mexicanTrain, boneyard);
                if (skipTurn == false) { //the tile the user picked is not playable,so place a marker on their train and skip their turn
                computerTurn = true;
                humanTurn = false;
                //BURBUR maybe have a startComputerTurn function that we kickoff.
                //tile drawn is not playable, place marker and skip turn
                //BURBUR NEED TO ADD MARKER TO TRAIN.

                bottomText.setText("You had no valid move and The tile you picked from the boneyard is not playable. Computers Turn.");
                gameFeed.addView(bottomText);
                //startTurns(new Tile (-1,-1), ' ');
                }
                else { //tile picked is playable. play it.
                bottomText.setText("The tile you picked from the boneyard is playable. Restarting turn.");
                gameFeed.addView(bottomText);
                }

                }
                */