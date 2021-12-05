package com.example.mexicantrain;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

public class Human extends Player {

    Human(Activity activity) {
        this.activity = activity;
    }

    public Train getTrain() {
        return this.playerTrain;
    }


    //check if tile fits on train and see if its a double
    private void userPlayedTile(TextView tilePlayed, char trainPlayedOn) {
        String tileAsString = tilePlayed.getText().toString();
        Tile userTile = new Tile(tileAsString.charAt(0) - 48, tileAsString.charAt(4) - 48);

        //user played on human train
        if(trainPlayedOn == 'h'){
            //this.humanPlayer.playerTrain.addTileBack(userTile);
            //displayRoundState();
            return;
        }
        else if (trainPlayedOn == 'c') {

        }

    }

    int play(Player humanPlayer, Player computerPlayer, Train mexicanTrain, Hand boneyard, Tile tileToPlay, char trainToPlay) {
        boolean validMoveSelected = false;

        //set playable trains
        if(!checkOrphanDoubles(humanPlayer, computerPlayer, mexicanTrain)) {
            this.computerTrainPlayable = computerPlayer.getTrainMarker();
            this.humanTrainPlayable = true;
            this.mexicanTrainPlayable = true;
        }

        //boolean playerHasValidMove = existsValidMove(humanPlayer,computerPlayer,mexicanTrain);
        //BURBURBURBUR PLAYERHASVALIDMOVE NOTHINGIS DONE WITH IT SO WE GO IN ENDLESS LOOP.
        //check that user can play on selected train
        boolean validTrainSelected = checkUserTrainPlayable(trainToPlay);
        if(validTrainSelected == false ) {
            setStringMoveExplanation("ERROR: Invalid train choice selected. Select a different tile and train ");
            return -22;
        }

        if(trainToPlay == 'h' && tileFitsOnTrain(tileToPlay, humanPlayer.getTrainEndNumber())) {
            //user played on human train, and the tile actually fits!
            Log.d("myTag", "User playing on human train.");
            humanPlayer.playerTrain.addTileBack(tileToPlay);
            setStringMoveExplanation("You played " + tileToPlay.tileAsString() + " on the human train" );
            if(this.getTrainMarker()) {
                this.clearTrainMarker();
            }
            validMoveSelected = true;
        }
        else if(trainToPlay == 'c' && tileFitsOnTrain(tileToPlay, computerPlayer.getTrainEndNumber())) {
            Log.d("myTag", "User playing on COMPUTER train.");
            computerPlayer.playerTrain.addTileFront(tileToPlay);
            setStringMoveExplanation("You played " + tileToPlay.tileAsString() + " on the computer train" );

            validMoveSelected = true;
        }
        else if(trainToPlay == 'm' && tileFitsOnTrain(tileToPlay,mexicanTrain.getTrainEndNumber())) {
            mexicanTrain.addTileBack(tileToPlay);
            Log.d("myTag", "User playing on MEXICAN train.");
            setStringMoveExplanation("You played " + tileToPlay.tileAsString() + " on the mexican train" );

            validMoveSelected = true;
        }

        //remove selected tile from players hand.
        if(validMoveSelected) {
            Log.d("myTag", "User Played " + tileToPlay.toString() + " on the " + trainToPlay + " train.");
            humanPlayer.playerHand.removeTile(tileToPlay.getFirstNum(), tileToPlay.getSecondNum());
            if(humanPlayer.getHand().getSize() == 0) {
                Log.d("myTag", "Human Player Turn Done");
                return computerPlayer.sumOfPips();

            }
        }
        else {
            Log.d("myTag", "User Entered an Invalid Tile");
            setStringMoveExplanation("You played an invalid tile! Select a different one" );
            return -1;

        }
        //check if humans hand is empty

        //return certain values based on what the user train previously played on.
        if(tileToPlay.isDouble()) {
            //-123 is code for double tile played
            setStringMoveExplanation("\nYou played a double tile! select another tile to play!");
            return -123;
        }
        else {
            Log.d("myTag", "Human Turn Over");
            setStringMoveExplanation("\nYour turn is over. Click the make a move button for the computer to play its turn!");
            //returning 0 means successful turn.
            return 0;
        }
        //return 0;
    }


}
