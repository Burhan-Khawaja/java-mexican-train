package com.example.mexicantrain;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
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


    public void playGame() {
        //BURBUR serialized start -- need to include that
        round.startRound(false,humanScore,computerScore,roundNumber);
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
            String firstWord, secondWord, thirdWord;
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
                    parseLineOfTiles(line, humanData);
                }
                else if(arrOfWords[3].equals("Train:")) {
                    parseLineOfTiles(line, humanData);
                }
            }
            if(firstWord.equals("Mexican")) {
                //do stuff.
            }

        }
    }

    ArrayList<Tile> parseLineOfTiles(String line, boolean humanData) {
        ArrayList<Tile> tileArray = new ArrayList<Tile>();
        String[] arrOfTiles = line.split(" ");
        if(arrOfTiles[4].equals("M")) {
            //SET MARKER
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

}

