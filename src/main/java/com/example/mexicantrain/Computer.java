package com.example.mexicantrain;

import android.app.Activity;

import java.util.ArrayList;

public class Computer extends Player {

    /**
     * constructor for computer player
     * @param activity unused parameter because we switched to MVC so models dont chagne the view
     *                 remove this later
     */
    Computer(Activity activity) {
        this.activity = activity;
    }

    /**
     * This function will handle the steps and rules for when a computer player plays a turn
     * @param humanPlayer stores data for human player
     * @param computerPlayer stores data for computer player
     * @param mexicanTrain stores data for mexican train
     * @param boneyard stores tiles in the boneyard
     * @param tileToPlay tile the user wants to play
     * @param trainToPlay train the user is attempting to play
     * @return integer value that is either a negative error code, 0 meaning the turn is complete, or a positive number
     *          that represents the pips of the human players hand if the computer wins
     */
    @Override
    int play(Player humanPlayer, Player computerPlayer, Train mexicanTrain, Hand boneyard, Tile tileToPlay, char trainToPlay) {

        //create 2 arraylists, 1 which holds the best tiles to play
        //the second one holds the corresponding trains to play on
        ArrayList<Tile> bestTilesToPlay = new ArrayList<Tile>();
        ArrayList<String> trainsToPlayOn = new ArrayList<String>();

        //set the computers playable trains
        // check for any orphan doubles, and if there are set only those playable..
        if(checkOrphanDoubles(humanPlayer,computerPlayer,mexicanTrain) == false) {
            this.computerTrainPlayable = true;
            this.humanTrainPlayable = humanPlayer.getTrainMarker();
            this.mexicanTrainPlayable = true;
        }

        //check for valid move
        boolean computerHasValidMove = existsValidMove(humanPlayer, computerPlayer, mexicanTrain);
        //if the computer does not have a valid move, undergo the procedure for a player not
        //having a valid move.
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
        //store the reasoning for why the move was chosen in cpuMoveExplanation.
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
                //remove played tiles from computers hand
                computerPlayer.playerHand.removeTile(bestTilesToPlay.get(i).getFirstNum(), bestTilesToPlay.get(i).getSecondNum());
                if(computerPlayer.playerHand.getSize() == 0) {
                    //if computer hand is empty then the game is over
                    return humanPlayer.sumOfPips();
                }
            }
        }


        //Check for orphan doubles below.

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
        return 0;
    }
}
