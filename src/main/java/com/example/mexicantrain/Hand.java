package com.example.mexicantrain;


import java.util.ArrayList;

public class Hand {
    private ArrayList<Tile> hand = new ArrayList<>();

    /**
     Add a tile to the players hand
     @param tile tileToAdd - Tile object that gets added to the hand
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
}
