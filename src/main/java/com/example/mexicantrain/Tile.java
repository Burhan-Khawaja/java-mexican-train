package com.example.mexicantrain;

public class Tile {

    //class variables
    private int firstNum;
    private int secondNum;

    //constructor
    Tile(int firstNum, int secondNum) {
        this.firstNum = firstNum;
        this.secondNum = secondNum;
    }

    public int getFirstNum() {
        return this.firstNum;
    }

    public int getSecondNum() {
        return this.secondNum;
    }

    public boolean isDouble() {
        return firstNum == secondNum;
    }

    public String tileAsString() {
        return firstNum + " - " + secondNum + " ";
    }
}
