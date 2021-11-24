package com.example.mexicantrain;

import android.app.Activity;

public abstract class Player {

    protected Hand playerHand = new Hand();
    protected Activity activity;
    protected Train playerTrain = new Train();
    private boolean humanTrainPlayable;
    private boolean computerTrainPlayable;
    private boolean mexicanTrainPlayable;
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

    abstract int play(Player humanPlayer, Player computerPlayer, Train mexicanTrain, Hand Boneyard);


}
