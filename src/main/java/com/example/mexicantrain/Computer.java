package com.example.mexicantrain;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;

public class Computer extends Player{

    Computer(Activity activity) {
        this.activity = activity;
    }

    @Override
    int play(Player humanPlayer, Player computerPlayer, Train mexicanTrain, Hand boneyard, Tile tileToPlay, char trainToPlay) {

        ArrayList<Tile> bestTilesToPlay = new ArrayList<Tile>();
        ArrayList<String> trainsToPlayOn = new ArrayList<String>();

        //BURBUR need to set playable trains.
        if(checkOrphanDoubles(humanPlayer,computerPlayer,mexicanTrain) == false) {
            this.computerTrainPlayable = true;
            this.humanTrainPlayable = humanPlayer.getTrainMarker();
            this.mexicanTrainPlayable = true;
        }

        boolean computerHasValidMove = existsValidMove(humanPlayer,computerPlayer,mexicanTrain);
        if(!computerHasValidMove) {
            Log.d("myTag", "Computer has no valid move. Adding marker to train and skipping turn");
            computerPlayer.setTrainMarker();
            return 0;
        }
        findBestMove(humanPlayer,computerPlayer,mexicanTrain, boneyard,bestTilesToPlay,trainsToPlayOn);
        String cpuMoveExplanation = interpretBestMove(bestTilesToPlay, trainsToPlayOn);
        this.setStringMoveExplanation(cpuMoveExplanation);
        if(!bestTilesToPlay.isEmpty()) {
            for(int i = 0; i < bestTilesToPlay.size(); i++) {
                if( trainsToPlayOn.get(i) == "c") {
                    computerPlayer.addTileFront(bestTilesToPlay.get(i));
                    //check the computer train for a marker, and clear it if there is one
                    if (computerPlayer.getTrainMarker()) {
                        computerPlayer.clearTrainMarker();
                    }
                    //also check for an orphan double and clear it if it exists.
                    if (computerPlayer.getOrphanDouble()) {
                        //computerPlayer->resetOrphanDouble();
                    }
                }
                else if( trainsToPlayOn.get(i) == "h") {
                    humanPlayer.addTileBack(bestTilesToPlay.get(i));
                    //add the tile to the human train, and check for an orphan double.
                    if (humanPlayer.getOrphanDouble()) {
                        //BURBUR RESET ORPHAN DOUBLE FUNCTION HERE. humanPlayer.resetOrphanDouble();
                    }
                }
                else {//mexican train is being played on
                    mexicanTrain.addTileBack(bestTilesToPlay.get(i));
                    if(mexicanTrain.getOrphanDouble()) {
                        //BURBUR RESET ORPHAN DOUBLE HERE.
                    }
                }
                computerPlayer.playerHand.removeTile(bestTilesToPlay.get(i).getFirstNum(), bestTilesToPlay.get(i).getSecondNum());
                if(computerPlayer.playerHand.getSize() == 0) {
                    //BURBUR GAME IS WON HERE.
                }
            }
        }
        Log.d("myTag", "computer turn finished.");
        return 0;
    }
}
