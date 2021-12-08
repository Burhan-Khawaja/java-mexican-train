package com.example.mexicantrain;

import android.app.Activity;

import java.util.ArrayList;

public class Human extends Player {

    //used to track what trains the humans plays on so we can check for orphan doubles later.
    private ArrayList<String> currentTrainsPlayed;


    /**
     * constructor for human object
     * @param activity unused parameter after we moved to MVC design
     *                 delete later
     */
    Human(Activity activity) {
        this.activity = activity;
        currentTrainsPlayed = new ArrayList<String>();
    }

    /**
     * get the humans train.
     * @return Train that represents all tiles on humans train
     */
    public Train getTrain() {
        return this.playerTrain;
    }

    /**
     * get the array list that stores strings of what trains the human played on
     * @return ArrayList<String> which stores what trains user selected to play tiles on
     */
    public ArrayList<String> getTrainsPlayed() {
        return this.currentTrainsPlayed;
    }

    /**
     * clear the arraylist that stores what trains the user played on
     */
    public void clearTrainsPlayed() {
        this.currentTrainsPlayed.clear();
    }

    /**
     * handle the steps and rules for when a human player plays a tile
     * @param humanPlayer stores data for human player
     * @param computerPlayer stores data for computer player
     * @param mexicanTrain stores data for mexican train
     * @param boneyard stores tiles in the boneyard
     * @param tileToPlay tile the user wants to play
     * @param trainToPlay train the user is attempting to play
     * @return integer value that is either a negative error code, 0 meaning the turn is complete, or a positive number
     *         that represents the pips of the computer players hand if the human wins
     */
    int play(Player humanPlayer, Player computerPlayer, Train mexicanTrain, Hand boneyard, Tile tileToPlay, char trainToPlay) {
        //check if the tile and train selector are actually valid
        boolean validMoveSelected = false;

        //set playable trains
        if(!checkOrphanDoubles(humanPlayer, computerPlayer, mexicanTrain)) {
            this.computerTrainPlayable = computerPlayer.getTrainMarker();
            this.humanTrainPlayable = true;
            this.mexicanTrainPlayable = true;
        }

        //check that user can play on selected train
        boolean validTrainSelected = checkUserTrainPlayable(trainToPlay);
        if(validTrainSelected == false ) {
            setStringMoveExplanation("ERROR: Invalid train choice selected. Select a different tile and train ");
            return -22;
        }

        //user plays on human train, check if the tile selected fits on the train
        if(trainToPlay == 'h' && tileFitsOnTrain(tileToPlay, humanPlayer.getTrainEndNumber())) {
            //user played on human train, and the tile actually fits!

            //add the tile, and add a string to the humans explanation saying they played the tile on the human train

            humanPlayer.playerTrain.addTileBack(tileToPlay);
            setStringMoveExplanation("You played " + tileToPlay.tileAsString() + " on the human train" );
            //if theres a marker,clear it. if theres an orphan double, clear it
            if(this.getTrainMarker()) {
                this.clearTrainMarker();
            }

            if(this.getOrphanDouble()) {
                this.setOrphanDouble(false);
            }
            validMoveSelected = true;
        }
        //human plays on computer train, and check if the tile they played fits on the train
        else if(trainToPlay == 'c' && tileFitsOnTrain(tileToPlay, computerPlayer.getTrainEndNumber())) {
            //add the tile to the computer train, clear the orphan double if it exists
            computerPlayer.playerTrain.addTileFront(tileToPlay);
            if(computerPlayer.getOrphanDouble()) {
                computerPlayer.setOrphanDouble(false);
            }
            //add a message that the human played a tile on this train to the humans string explanation for thier move
            setStringMoveExplanation("You played " + tileToPlay.tileAsString() + " on the computer train" );

            validMoveSelected = true;
        }
        //human plays on mexican train, and check if the tile they played fits on the train
        else if(trainToPlay == 'm' && tileFitsOnTrain(tileToPlay,mexicanTrain.getTrainEndNumber())) {
            //add the tile to the mexican train, clear the orphan double if it exists
            mexicanTrain.addTileBack(tileToPlay);
            if(mexicanTrain.getOrphanDouble()) {
                mexicanTrain.setOrphanDouble(false);
            }
            //add a message that the human played a tile on this train to the humans string explanation for thier move
            setStringMoveExplanation("You played " + tileToPlay.tileAsString() + " on the mexican train" );

            validMoveSelected = true;
        }

        //remove selected tile from players hand.
        if(validMoveSelected) {
            humanPlayer.playerHand.removeTile(tileToPlay.getFirstNum(), tileToPlay.getSecondNum());
            //add train user played on to arraylist of trains
            currentTrainsPlayed.add(Character.toString( trainToPlay));
            //check if human player's hand is empty,meaning round is over
            if(humanPlayer.getHand().getSize() == 0) {
                return computerPlayer.sumOfPips();
            }
        }
        else {
            //human player selected an invalid tile. return -1 after telling them they messed up
            setStringMoveExplanation("You played an invalid tile! Select a different one" );
            return -1;

        }

        //return certain values based on what the user train previously played on.
        //-123 = human played double.
        if(tileToPlay.isDouble()) {
            //-123 is code for double tile played
            setStringMoveExplanation("\nYou played a double tile! select another tile to play!");
            return -123;
        }
        else {
            setStringMoveExplanation("\nYour turn is over. Click the make a move button for the computer to play its turn!");
            //returning 0 means successful turn.
            return 0;
        }
    }
}
