package com.example.mexicantrain;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Game {
    private int roundNumber;
    private int humanScore;
    private int computerScore;
    private Round round ;
    private Activity activity;

    Game(Activity activity) {
        this.roundNumber = 0;
        this.humanScore = 0;
        this.computerScore = 0;
        this.activity = activity;
        this.round = new Round(activity);
    }


    public void playGame(boolean serializedStart) {
        //BURBUR serialized start -- need to include that
        round.startRound(serializedStart,humanScore,computerScore,roundNumber);
    }

    public String getHumanAndComputerTrain() {
        return round.getHumanAndComputerTrain();
    }
    public String getMexicanTrain() {
        return round.getMexicanTrain();
    }
    public Hand getHumanHand(){
        return round.getHumanHand();
    }

    public int playTile(Tile userTile, char userTrain) {
        int value = round.playTile(userTile, userTrain);
        return value;
    }

    public boolean humanHasValidMove() {
        return this.round.playerHasValidMove();
    }

    public void setPlayableTrainsHuman(){
        this.round.setPlayableTrains();
    }

    public boolean getHumanTurn(){
        return this.round.getHumanTurn();
    }

    public Hand getComputerHand() {
        return this.round.getComputerHand();
    }

    public int getComputerScore() {
        return this.computerScore;
    }

    public int getHumanScore() {
        return this.humanScore;
    }

    public int getRoundNumber() {
        return this.roundNumber;
    }

    public void addComputerScore(int pipVal){
        this.computerScore += pipVal;
    }
    public void addHumanScore(int pipVal){
        this.humanScore += pipVal;
    }
    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void playAgain() {
        //BURBUR TESTING CODE
        Intent playAgain = new Intent(this.activity, playAgainScreen.class);
        playAgain.putExtra("gameRoundNum", getRoundNumber() );
        playAgain.putExtra("gameHumanScore", getHumanScore());
        playAgain.putExtra("gameComputerScore", getComputerScore());
        playAgain.putExtra("roundEngineInt", round.getEngineValue());
        Log.d("myIntent", Integer.toString(playAgain.getIntExtra("gameComputerScore",-121312323)));
        Log.d("myIntent", Integer.toString(playAgain.getIntExtra("roundEngineInt",-121312323)));
        this.activity.startActivity(playAgain);
    }

    public void setEngineInt(int engineVal) {
        round.setEngineInt(engineVal);
    }

    public void loadGame(Scanner loadGameFile) {
        boolean computerData = false;
        boolean humanData = false;
        boolean setMarker = false;

        while(loadGameFile.hasNextLine()) {
            //create words to track what we are tracking
            String firstWord;
            //get the entire line of the file
            //firstWord = loadGameFile.next();
            String line = loadGameFile.nextLine();
            String[] arrOfWords = line.split(" ");
            Log.d("loadGame", line);
            firstWord = arrOfWords[0];
            if (firstWord.equals("Round:")) {
                //set round number to next value.
                String roundNum = arrOfWords[1];
                setRoundNumber(Integer.parseInt(roundNum) - 1);
                continue;
            }

            if (firstWord.equals("Computer:")) {
                computerData = true;
                humanData = false;
                continue;
            }

            if (firstWord.equals("Human:")) {
                computerData = false;
                humanData = true;
                continue;
            }

            //if our array of words has empty spaces for the first 3 things, then we're working on player data
            //and the 4th thing in teh array is the thing we are working no.
            if(arrOfWords.length> 4 ) {
                if (arrOfWords[3].equals("Score:")) {
                    String score = arrOfWords[4];
                    int scoreAsInt = Integer.parseInt(score);

                    if (computerData) {
                        setComputerScore(scoreAsInt);
                    }
                    else {
                        setHumanScore(scoreAsInt);
                    }
                    continue;
                }
                else if (arrOfWords[3].equals("Hand:")) {
                    ArrayList<Tile> tiles = parseLineOfTiles(line, humanData);
                    if(computerData) {
                        round.setPlayerHand(tiles, 0);
                    }
                    else {
                        round.setPlayerHand(tiles,1);
                    }
                }
                else if(arrOfWords[3].equals("Train:")) {
                    ArrayList<Tile> tiles =  parseLineOfTiles(line, humanData);
                    if(computerData){
                        //back of train contains engine that we dont want to print, so pop it off
                        tiles.remove(tiles.size()-1);
                        //reverse tiles so it prints to screen normally.
                        Collections.reverse(tiles);
                        round.setPlayerTrain(tiles, 0);

                    }
                    else {//human train
                        round.setEngineInt(tiles.get(0).getFirstNum());
                        tiles.remove(0);
                        round.setPlayerTrain(tiles, 1);

                    }
                }
            }
            if(firstWord.equals("Mexican")) {
                computerData = false;
                humanData = false;
                //do stuff.
                ArrayList<Tile> tiles = parseLineOfTiles(line, humanData);
                round.setPlayerTrain(tiles,2);
                continue;
            }
            if(firstWord.equals("Boneyard:")) {
                ArrayList<Tile> tiles = parseLineOfTiles(line, humanData);
                round.setPlayerHand(tiles,2);
                continue;
            }
            if(firstWord.equals("Next")) {
                //next word is player, word after that is human or computer
                String whoseTurn = arrOfWords[2];
                if(whoseTurn.equals("Computer")) {
                    round.setComputerTurn();
                }
                else {
                    round.setHumanTurn();
                }
            }
        }
    }

    ArrayList<Tile> parseLineOfTiles(String line, boolean humanData) {
        ArrayList<Tile> tileArray = new ArrayList<Tile>();
        String[] arrOfTiles = line.split(" ");
        if(arrOfTiles[0].equals("Mexican")) {
            //mexican train.
            for(int i = 2; i < arrOfTiles.length; i++) {
                int fnum = Integer.parseInt(String.valueOf(arrOfTiles[i].charAt(0)));
                int snum = Integer.parseInt(String.valueOf(arrOfTiles[i].charAt(2)));
                tileArray.add(new Tile(fnum,snum));

            }
            return tileArray;
        }
        if(arrOfTiles[0].equals("Boneyard:")) {
            for(int i = 1; i < arrOfTiles.length; i++) {
                int fnum = Integer.parseInt(String.valueOf(arrOfTiles[i].charAt(0)));
                int snum = Integer.parseInt(String.valueOf(arrOfTiles[i].charAt(2)));
                tileArray.add(new Tile(fnum,snum));
            }
            return tileArray;

        }
        if(arrOfTiles[4].equals("M")) {
            //SET MARKER
            if(humanData) {
                //set human train marker
                round.setTrainMarker(1);
            }
            else {
                //else set computer train marker
                round.setTrainMarker(0);
            }
            Log.d("loadGame", "Set marker!" );
        }
        if(arrOfTiles[4].equals("Mexican")) {
            //do nothing here.
        }
        //if first character is a digit it is a tile.
        if(Character.isDigit( arrOfTiles[4].charAt(0))) {
            int fnum = Integer.parseInt( String.valueOf(arrOfTiles[4].charAt(0)));
            int snum = Integer.parseInt( String.valueOf(arrOfTiles[4].charAt(2)));
            tileArray.add(new Tile(fnum,snum));
        }

        for(int i = 5; i < arrOfTiles.length; i++) {
            if(arrOfTiles[i].equals("M")){
                //do stuff
                if(humanData) {
                    round.setTrainMarker(1);
                }
                else {
                    round.setTrainMarker(0);
                }
                return tileArray;
            }
            int fnum = Integer.parseInt(String.valueOf(arrOfTiles[i].charAt(0)));
            int snum = Integer.parseInt(String.valueOf(arrOfTiles[i].charAt(2)));
            tileArray.add(new Tile(fnum,snum));
        }
        return tileArray;
    }


    private void setHumanScore(int scoreAsInt) {
        this.humanScore = scoreAsInt;
    }

    private void setComputerScore(int scoreAsInt) {
        this.computerScore = scoreAsInt;
    }

    public Tile getTopOfBoneyard() {
        return this.round.getTopOfBoneyard();
    }

    public void setHumanTurn() {
        this.round.setHumanTurn();
    }
    public void setComputerTurn() {
        this.round.setComputerTurn();
    }

    public String getHumanMoveExplanation(){
        return this.round.getHumanMoveExplanation();
    }

    public String getComputerMoveExplanation(){
        return this.round.getComputerMoveExplanation();
    }

    public void clearHumanMoveExplanation() {
        this.round.clearHumanMoveExplanation();
    }

    public void clearComputerMoveExplanation() {
        round.clearComputerMoveExplanation();
    }

    public void getBestHumanMove() {
        round.getBestHumanMove();
    }
}

