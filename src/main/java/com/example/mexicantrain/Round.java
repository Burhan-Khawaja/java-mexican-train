package com.example.mexicantrain;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

public class Round {
    //mexican train
    //boneyard

    //CLASS VARIABLES
    private Hand boneyard = new Hand();
    private Train mexicanTrain = new Train();
    private Human humanPlayer = new Human(this.activity);
    //private Computer computerPlayer = new Computer(this.activity);
    private Activity activity;


    private int engineInt;

    private Queue<Integer> engineQueue = new ArrayDeque<Integer>();
    private boolean humanTurn;
    private boolean computerTurn;
    private boolean computerWon;
    private boolean humanWon;

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
     * Displays the current state of the game, including human and player hands and trains
     * and top of boneyard
     */
    public void displayRoundState() {
        //BURBUR TESTING PURPOSES ADD TILES TO TRAIN.
        humanPlayer.addTileFront(new Tile(1,1));
        humanPlayer.addTileFront(new Tile(2,2));


        //get train section of UI

        //NEED TO GET THE LINEAR LAYOUT OF THE HORIZONTAL SCROLLVIEW, NOT THE ACTUAL SCROLLVIEW
        LinearLayout allTrains = (LinearLayout) this.activity.findViewById(R.id.allTrainsLayout);
        //create a new TextView that will store the train values.
        TextView humanTrainValues = new TextView(this.activity);
        humanTrainValues.setText(humanPlayer.trainAsString());

        allTrains.addView(humanTrainValues);

        humanPlayer.displayHand();
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
            dealTiles();
            displayRoundState();
        }
        int pipsValue = startTurns(humanScore, computerScore, roundNumber);
        return 0;
    }

    private int startTurns(int humanScore, int computerScore, int roundNumber) {
        //integers that will store the sum of pips of the tiles in a players hand when the other player empties their hand
        int humanPipsLeft, computerPipsLeft = 0;
        displayRoundState();
        humanTurn = true;
        do {
            if(humanTurn) {
                //BURBUR NEED TO CHANGE SECOND ARGUMENT IN THIS FUNCTION CALL
                //computerPipsLeft = humanPlayer.play(this.humanPlayer,this.humanPlayer, this.mexicanTrain, this.boneyard);
                if(humanPlayer.handEmpty()){
                    //do stuff if human players hand is empty.
                }
                this.humanTurn = false;
                this.computerTurn = true;
            }

            if(computerTurn) {
                this.computerTurn = false;
                this.humanTurn = true;
                break;
            }
        } while(true);
        return 0;
    }



    //CODE TESTING


}
