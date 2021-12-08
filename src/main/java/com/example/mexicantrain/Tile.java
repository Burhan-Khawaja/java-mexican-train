package com.example.mexicantrain;

public class Tile {

    //store number that represent a tile
    private int firstNum;
    private int secondNum;

    /**
     * Create a tile when given 2 integer values
     * @param firstNum integer value that we assign to be the first value
     * @param secondNum integer value that we assign to be the second value
     */
    Tile(int firstNum, int secondNum) {
        this.firstNum = firstNum;
        this.secondNum = secondNum;
    }

    /**
     * return first number of a tile
     * @return integer that is the first number
     */
    public int getFirstNum() {
        return this.firstNum;
    }

    /**
     * get the second number of a tile
     * @return integer that is the second number
     */
    public int getSecondNum() {
        return this.secondNum;
    }

    /**
     * check if a tile is a double
     * @return boolean true if a tiles first number is the same as its second, false otherwise
     */
    public boolean isDouble() {
        return firstNum == secondNum;
    }

    /**
     * return a string representation of the tile.
     * @return String that is the tile
     */
    public String tileAsString() {
        return firstNum + " - " + secondNum + " ";
    }

    /**
     * swap the 2 numbers of a tile. Used to make it fit properly on the train aesthetic wise.
     */
    public void swapNumbers() {
        int tmp = this.getFirstNum();
        this.firstNum = this.secondNum;
        this.secondNum = tmp;
    }
}
