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

    /**
     * constuctor for Train class.
     */
    public Train() {
        this.trainDeque = new ArrayDeque<Tile>();
        this.marker = false;
        this.orphanDouble = false;
        this.trainEndNumber = -1;
    }

    //selectors

    /**
     * get the end number of a train that is left "hanging"
     * @return integer value that is the last number on the train
     */
    public int getTrainEndNumber(){
        return this.trainEndNumber;
    }

    /**
     * check if a train has an orphan double on it
     * @return boolean value, true if there exists an orphan double, false if it doesnt
     */
    public boolean getOrphanDouble() {
        return orphanDouble;
    }

    /**
     * check if a train has a marker on it.
     * @return true if it does false if it doesnt
     */
    public boolean getMarker() {
        return marker;
    }

    /**
     * set a marker on a train.
     * @param marker- boolean value that is either true or false to set the value
     */
    public void setMarker(boolean marker) {
        this.marker = marker;
    }


    //mutators
    /**
     * Add a tile to the back of the train
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

    /**
     * set a trains orphan double to the boolean value passed in
     * @param orphanDouble - boolean value that will be set to orphanDouble
     */
    public void setOrphanDouble(boolean orphanDouble) {
        this.orphanDouble = orphanDouble;
    }

    /**
     * set the trains end number
     * @param newEndNumber integer value that will be the new end value for the train
     */
    public void setTrainEndNumber(int newEndNumber) {
        this.trainEndNumber = newEndNumber;
    }

    /**
     * Add a tile to the back of the train
     * @param tileToAdd Object of Tile type that will be added to train
     */
    public void addTileFront(Tile tileToAdd) {
        if (tileToAdd.getFirstNum() == getTrainEndNumber()) {
            setTrainEndNumber(tileToAdd.getSecondNum());
            tileToAdd.swapNumbers();
        }
        else if (tileToAdd.getSecondNum() == getTrainEndNumber()) {
            setTrainEndNumber(tileToAdd.getFirstNum());
        }
        this.trainDeque.addFirst(tileToAdd);
    }
    //utility methods

    /**
     * represent a train as a readable string
     * @return String that represents all the tiles in the train
     */
    public String trainAsString(){
        String train = new String();
        Iterator<Tile> iterator = trainDeque.iterator();

        while(iterator.hasNext()){
            train += iterator.next().tileAsString();
        }
        return train;
    }

    /**
     * set a trains marker to false
     */
    public void clearTrainMarker() {
        this.marker = false;
    }

    public int getSize() {
        return this.trainDeque.size();
    }

    public String trainAsStringSerialization() {
        String train = new String();
        Iterator<Tile> iterator = trainDeque.iterator();

        while(iterator.hasNext()){
            Tile tmpTile = iterator.next();

            train += tmpTile.getFirstNum();
            train += "-";
            train += tmpTile.getSecondNum();
            train += " ";
        }
        return train;

    }
}
