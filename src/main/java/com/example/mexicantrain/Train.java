package com.example.mexicantrain;

import android.util.Log;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class Train {
    //Class Variables
    private Deque<Tile> trainDeque = new ArrayDeque<Tile>();
    private boolean marker;
    private boolean orphanDouble;
    private int trainEndNumber;

    //Constructor
    public Train() {
        this.trainDeque = new ArrayDeque<Tile>();
        this.marker = false;
        this.orphanDouble = false;
        this.trainEndNumber = -1;
    }

    //selectors

    //mutators
    /**
     * Add a tile to the front of the train
     * @param tileToAdd Object of Tile type that will be added to train
     */
    public void addTileBack(Tile tileToAdd) {
        this.trainDeque.addFirst(tileToAdd);
    }

    /**
     * Add a tile to the back of the train
     * @param tileToAdd Object of Tile type that will be added to train
     */
    public void addTileFront(Tile tileToAdd) {
        this.trainDeque.addFirst(tileToAdd);
    }
    //utility methods

    /**
     *
     */
    public String trainAsString(){
        String train = new String();
        Iterator<Tile> iterator = trainDeque.iterator();

        while(iterator.hasNext()){
            train += iterator.next().tileAsString();
        }
        Log.d("mytag",train);
        //for(int i = 0; i < trainDeque.size(); i++) {
        //    trainDeque.toString() .tileAsString();
        //}*/

        return train;


    }
}
