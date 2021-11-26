package com.example.mexicantrain;

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
        if(tileToAdd.getFirstNum() == this.getTrainEndNumber()) {
            this.setTrainEndNumber(tileToAdd.getSecondNum());
        }
        else if (tileToAdd.getSecondNum() == this.getTrainEndNumber()) {
            this.setTrainEndNumber(tileToAdd.getFirstNum());
            tileToAdd.swapNumbers();
        }
        //BURBUR double check this?
        this.trainDeque.addLast(tileToAdd);
    }

    public void setTrainEndNumber(int newEndNumber) {
        this.trainEndNumber = newEndNumber;
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
        return train;
    }

    public int getTrainEndNumber(){
        return this.trainEndNumber;
    }
}
