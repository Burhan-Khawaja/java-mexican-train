package com.example.mexicantrain;

import android.app.Activity;

public class Player {

    protected Hand playerHand = new Hand();
    protected Activity activity;
    private Train playerTrain = new Train();
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

    public String trainAsString(){
        return playerTrain.trainAsString();
    }

    public void addTileFront(Tile tileToAdd) {
        playerTrain.addTileFront(tileToAdd);
    }

    public void addTileBack(Tile tileToAdd) {
        playerTrain.addTileBack(tileToAdd);
    }
}
