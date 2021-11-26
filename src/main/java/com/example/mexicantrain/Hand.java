package com.example.mexicantrain;


import java.util.ArrayList;

public class Hand {
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
    public int getSize() {
        return this.hand.size();
    }

    /**
     * Get a tile from the players hand at a specified index
     * @param index
     * @return Tile returns tile that is at position in hand
     */
    public Tile getTile(int index) {
        //BURBUR error checking
        return hand.get(index);
    }

    /**
     * Access an index of our hand and change the tile present to a tile that is passed in
     * @param newTile Tile object that will replace the index
     * @param index Integer value that is the index we access.
     */
    public void changeTileAtIndex(Tile newTile,int index) {

    }

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
}
