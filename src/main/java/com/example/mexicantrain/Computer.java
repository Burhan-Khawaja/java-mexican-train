package com.example.mexicantrain;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;

public class Computer extends Player {

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
        //check for valid move
        boolean computerHasValidMove = existsValidMove(humanPlayer, computerPlayer, mexicanTrain);
        if(!computerHasValidMove) {
            setStringMoveExplanation("Computer has no valid move. ");
            boolean skipTurn = noPlayableTiles(humanPlayer,computerPlayer,mexicanTrain,boneyard);
            if(skipTurn == false) {
                //drawn tile is not playable, skip turn after a marker is placed on train.
                this.setTrainMarker();
                if(boneyard.getSize() == 0) {
                    //if boneyard is empty, and have to skip turn return -666 since thats our error code for this scenario
                    return -666;
                }
                return 0;
            }
        }

        //find best move for computer
        findBestMove(humanPlayer,computerPlayer,mexicanTrain, boneyard,bestTilesToPlay,trainsToPlayOn);
        String cpuMoveExplanation = interpretBestMove(bestTilesToPlay, trainsToPlayOn);
        this.setStringMoveExplanation(cpuMoveExplanation);
        if(!bestTilesToPlay.isEmpty()) {
            for(int i = 0; i < bestTilesToPlay.size(); i++) {
                if(trainsToPlayOn.get(i).equals("c")) {
                    computerPlayer.addTileFront(bestTilesToPlay.get(i));
                    //check the computer train for a marker, and clear it if there is one
                    if (computerPlayer.getTrainMarker()) {
                        computerPlayer.clearTrainMarker();
                    }
                    //also check for an orphan double and clear it if it exists.
                    if (computerPlayer.getOrphanDouble()) {
                        computerPlayer.setOrphanDouble(false);
                    }
                }
                else if(trainsToPlayOn.get(i).equals("h")) {
                    humanPlayer.addTileBack(bestTilesToPlay.get(i));
                    //add the tile to the human train, and check for an orphan double.
                    if (humanPlayer.getOrphanDouble()) {
                        humanPlayer.setOrphanDouble(false);
                    }
                }
                else {//mexican train is being played on
                    mexicanTrain.addTileBack(bestTilesToPlay.get(i));
                    if(mexicanTrain.getOrphanDouble()) {
                        mexicanTrain.setOrphanDouble(false);
                    }
                }
                computerPlayer.playerHand.removeTile(bestTilesToPlay.get(i).getFirstNum(), bestTilesToPlay.get(i).getSecondNum());
                if(computerPlayer.playerHand.getSize() == 0) {
                    //BURBUR GAME IS WON HERE.
                    return humanPlayer.sumOfPips();
                }
            }
        }



        //user played 2 tiles, so theres a chance that there is 1 orphan double.
        if (trainsToPlayOn.size() >= 2) {
            if (!trainsToPlayOn.get(0).equals(trainsToPlayOn.get(1))) {
                //train user played on arent equal to each other, so make the first train an orphan double
                if (trainsToPlayOn.get(0).equals("m")) {
                    mexicanTrain.setOrphanDouble(true);
                }
                else if (trainsToPlayOn.get(0).equals("c")) {
                    computerPlayer.setOrphanDouble(true);
                }
                else {
                    humanPlayer.setOrphanDouble(true);
                }
            }
        }
        //user played 3 tiles, so we check if the second tile they played is also an orphan double.
        if (trainsToPlayOn.size() == 3) {
            if (!trainsToPlayOn.get(1).equals(trainsToPlayOn.get(2))) {
                //train user played on arent equal to each other, so make the first train an orphan double
                if (trainsToPlayOn.get(1).equals("m")) {
                    mexicanTrain.setOrphanDouble(true);
                }
                else if (trainsToPlayOn.get(1).equals("c")) {
                    computerPlayer.setOrphanDouble(true);
                }
                else {
                    humanPlayer.setOrphanDouble(true);
                }
            }

        }

        Log.d("myTag", "computer turn finished.");
        return 0;
    }
}
