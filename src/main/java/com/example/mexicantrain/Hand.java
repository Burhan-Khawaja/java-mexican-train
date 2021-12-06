package com.example.mexicantrain;


import java.util.ArrayList;

public class Hand {
    //ArrayList that stores an array of tiles that is the users hand
    private ArrayList<Tile> hand = new ArrayList<>();

    /**
     Add a tile to the players hand
     @param Tile tileToAdd - Tile object that gets added to the hand
     */
    public void addTile(Tile tileToAdd) {
        hand.add(tileToAdd);
    }
    
    /**
     * Get the size of the hand
     * @return int, which represents number of tiles in the ArrayList
     */
    public final int getSize() {
        return this.hand.size();
    }

    /**
     * Get a tile from the players hand at a specified index
     * @param index
     * @return Tile returns tile that is at position in hand
     */
    public Tile getTile(int index) {
        //BURBUR error checking
        if(hand.size() == 0) {
            return new Tile(-1,-1);
        }
        return hand.get(index);
    }

    /**BURBUR not used
     * Access an index of our hand and change the tile present to a tile that is passed in
     * @param newTile Tile object that will replace the index
     * @param index Integer value that is the index we access.
     */
    public void changeTileAtIndex(Tile newTile,int index) {

    }

    /**
     * remove a tile from a  users hand given 2 numbers that are its value
     * @param value1 - integer value that is the first value of the tile
     * @param value2 - integer value that is the second value of the tile
     */
    public void removeTile(int value1, int value2) {
        Tile tmpTile = new Tile(value1, value2);

        //find index of Tile
        for(int i = 0; i < hand.size(); i++) {
            //since we occasionaly swap tiles to fit on the train we have to check
            //if the tile at index's first number is equal to value 1 and value 2 since it might be swapped.
            //if hand[i].firstnumber = value1, and hand[i].secondnumber = value 2, remove it
            if(hand.get(i).getFirstNum() == value1 && hand.get(i).getSecondNum() == value2  ) {
                hand.remove(i);
            }
            //else if hand[i]'s first number = value 2, and hand[i]'s second number = value 1
            else if(hand.get(i).getFirstNum() == value2 && hand.get(i).getSecondNum() == value1 ) {
                hand.remove(i);
            }
        }
    }

    /**
     * return the arrayList of the players hand
     * @return ArrayList that is the users hand.
     */
    public ArrayList<Tile> getHandArrayList() {
        return this.hand;
    }

    public String handAsString() {
        String handString = "";
        for(int i = 0; i < hand.size(); i++) {
            //cant use getTileAsString in tile class b/c It is formated as x - y, when serialization
            //is x-y format.
            handString += hand.get(i).getFirstNum();
            handString += "-";
            handString += hand.get(i).getSecondNum();
            handString += " ";
        }
        return handString;
    }
}
