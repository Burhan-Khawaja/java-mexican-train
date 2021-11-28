package com.example.mexicantrain;

import android.app.Activity;
import android.util.Log;

public abstract class Player {

    protected Hand playerHand = new Hand();
    protected Activity activity;
    protected Train playerTrain = new Train();
    protected boolean humanTrainPlayable;
    protected boolean computerTrainPlayable;
    protected boolean mexicanTrainPlayable;

    public Hand getHand() {
        return this.playerHand;
    }


    /**
     * Add a tile to a players hand
     * @param tileToAdd Tile object that gets added to a players hand
     */
    public void addTileToHand(Tile tileToAdd){
        playerHand.addTile(tileToAdd);
    }

    /**
     * set the member variable activity so we can access gui
     * @param _activity Activity object
     */
    public void setActivity(Activity _activity) {
        this.activity = _activity;
    }

    /**
     * Checks if a players hand is empty
     * @return boolean value, true if hand is empty false otherwise
     */
    public boolean handEmpty(){
        if(playerHand.getSize() == 0) {
            return true;
        }
        return false;
    }

    public String trainAsString(){
        return playerTrain.trainAsString();
    }

    public void addTileFront(Tile tileToAdd) {
        playerTrain.addTileFront(tileToAdd);
    }

    public void addTileBack(Tile tileToAdd) {
        playerTrain.addTileBack(tileToAdd);
    }

    abstract int play(Player humanPlayer, Player computerPlayer, Train mexicanTrain, Hand Boneyard, Tile tileToPlay, char trainToPlay);

    public boolean tileFitsOnTrain(Tile tileToCheck, int trainEndNumber) {
        if(tileToCheck.getFirstNum() == trainEndNumber) {
            return true;
        }
        else if (tileToCheck.getSecondNum()== trainEndNumber) {
            return true;
        }
        return false;
    }
    public int getTrainEndNumber() {
        return this.playerTrain.getTrainEndNumber();
    }

    public boolean existsValidMove(Player humanPlayer, Player computerPlayer, Train mexicanTrain) {
        boolean validMove = false;

        if(this.humanTrainPlayable && playerHasMove(humanPlayer.getTrainEndNumber())) {
            validMove = true;
        }

        if(this.computerTrainPlayable && playerHasMove(computerPlayer.getTrainEndNumber())) {
            validMove = true;
        }

        if(mexicanTrainPlayable && playerHasMove(mexicanTrain.getTrainEndNumber())) {
            validMove = true;
        }
        return validMove;
    }

    public boolean playerHasMove(int trainEndNumber) {
        for(int i = 0; i < playerHand.getSize(); i++) {
            if(playerHand.getTile(i).getFirstNum() == trainEndNumber) {
                return true;
            }
            else if (playerHand.getTile(i).getSecondNum() == trainEndNumber) {
                return true;
            }
        }
        return false;
    }

    boolean noPlayableTiles(Player humanPlayer, Player computerPlayer, Train mexicanTrain, Hand boneyard) {
        if(boneyard.getSize() == 0) {
            //BURBUR add error message that boneyard is empty and a marker is placed on the train
            //this.settrain
            Log.d("myTag", "ERROR: BONEYARD IS EMPTY AND YOU HAVE NO MOVES. A MARKER IS PLACED ON YOUR TRAIN");
        }
        else {
            //get top tile of boneyard.
            Tile boneyardTile = boneyard.getTile(0);
            //remove that tile from the boneyard
            boneyard.removeTile(boneyardTile.getFirstNum(),boneyardTile.getSecondNum());
            //add the tile to players hand
            this.addTileToHand(boneyardTile);
            //check now if user has a valid move after taking the tile from the boneyard

            boolean validMove  = this.existsValidMove(humanPlayer,computerPlayer,mexicanTrain);
            if(!validMove) {
                //user does not have a valid move. place marker, return false.
                Log.d("myTag", "USER HAS NO VALID MOVE AFTER TAKING TILE FROM BONEYARD");
                //BURBUR set train marker here
                return false;

            }
            else {
                //the tile selected is playable.
                return true;
            }

        }
        return false;
    }

    boolean checkOrphanDoubles(Player humanPlayer, Player computerPlayer, Train mexicanTrain)
    {
        boolean orphanDouble = false;
        //1- check if there exists any orphan double on a train
        if (humanPlayer.playerTrain.getOrphanDouble() == true || computerPlayer.playerTrain.getOrphanDouble() == true || mexicanTrain.getOrphanDouble() == true) {
            //make all trains unplayable. later we will check which trains have orphan doubles and set them as the only trains playable
            this.humanTrainPlayable = false;
            this.computerTrainPlayable = false;
            this.mexicanTrainPlayable = false;
        }
        //check if each train has an orphan double. if it does, make that train playable, and set orphanDouble to true.
        if (humanPlayer.playerTrain.getOrphanDouble() == true) {
            //BURBUR output that theres an orphandouble on humantrain
            this.humanTrainPlayable = true;
            orphanDouble = true;
        }
        if (computerPlayer.playerTrain.getOrphanDouble() == true) {
            //BURBUR output that theres an orphandouble on
            this.computerTrainPlayable = true;
            orphanDouble = true;
        }
        if (mexicanTrain.getOrphanDouble() == true) {
            //BURBUR output that theres an orphandouble on
            this.mexicanTrainPlayable = true;
            orphanDouble = true;
        }
        return orphanDouble;
    }

    public boolean checkUserTrainPlayable(char userTrainChoice) {
        if (userTrainChoice == 'h' && !this.humanTrainPlayable) {
        //    std::cout << "Error: You are not allowed to play on the human train.";
            return false;
        }
        else if (userTrainChoice == 'c' && !this.computerTrainPlayable) {
        //    std::cout << "Error: You are not allowed to play on the computer train";
            return false;
        }
        else if (userTrainChoice == 'm' && !this.mexicanTrainPlayable) {
        //    std::cout << "Error: You are not allowed to play on the Mexican Train";
            return false;
        }
        return true;
    }

    public boolean getTrainMarker() {
        return this.playerTrain.getMarker();
    }

    public int sumOfPips() {
        //total will hold the sum of the pips in the hand
        int total = 0;
        //for every tile in the players hand, add the first and second number to total.
        for (int i = 0; i < playerHand.getSize(); i++) {
            total += playerHand.getTile(i).getFirstNum();
            total += playerHand.getTile(i).getSecondNum();
        }
        Log.d("myTag", "Sum Of Pips = " + Integer.toString(total));
        return total;
    }
}
