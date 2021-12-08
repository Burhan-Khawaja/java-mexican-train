package com.example.mexicantrain;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class Game {
    private int roundNumber;
    private int humanScore;
    private int computerScore;
    private Round round ;
    private Activity activity;
    private boolean skippedHumanTurn;
    private boolean skippedComputerTurn;

    /**
     * constructor for game
     * @param activity not used after we changed to MVC design, remember to remove this variable
     */
    Game(Activity activity) {
        this.roundNumber = 0;
        this.humanScore = 0;
        this.computerScore = 0;
        this.activity = activity;
        this.round = new Round(activity);
    }


    /**
     * begin the game and start setting all the appropriate data for the round
     * @param serializedStart boolean value that is true if we are loading a game, false otherwise.
     */
    public void playGame(boolean serializedStart) {
        round.startRound(serializedStart,humanScore,computerScore,roundNumber);
    }

    /**
     * get computer and human train as strings so we can output them to screen
     * @return String that is the human and computer trains together
     */
    public String getHumanAndComputerTrain() {
        return round.getHumanAndComputerTrain();
    }

    /**
     * get the mexican train as a String
     * @return String that is the all the tiles on the mexican train
     */
    public String getMexicanTrain() {
        return round.getMexicanTrain();
    }

    /**
     * get the human as a hand, used to save the game
     * @return Hand that is the all the tiles for human
     */
    public Hand getHumanHand(){
        return round.getHumanHand();
    }

    /**
     * get the boneyard as a hand, used to save the game
     * @return Hand that is the all the tiles in boneyard
     */
    public Hand getBoneyardHand() {
        return round.getBoneyardHand();
    }

    /**
     * play a tile on a train
     * @param userTile tile object that the user wants to play
     * @param userTrain char that is the train user wants to play on
     * @return value that is returned by round.playTile, which represents the sum of pips of a loser, an error code, or 0
     */
    public int playTile(Tile userTile, char userTrain) {
        int value = round.playTile(userTile, userTrain);
        return value;
    }

    /**
     * check if the human has a valid move
     * @return boolean, true if human has a move false otherwise
     */
    public boolean humanHasValidMove() {
        return this.round.playerHasValidMove();
    }

    /**
     * set which trains the humans can play on (check for orphan doubles/markers)
     */
    public void setPlayableTrainsHuman(){
        this.round.setPlayableTrains();
    }

    /**
     * get if its the humans turn
     * @return boolean value, true if its human turn, false otherwise
     */
    public boolean getHumanTurn(){
        return this.round.getHumanTurn();
    }

    /**
     * get the computers hand
     * @return Hand that is the comptuers hand
     */
    public Hand getComputerHand() {
        return this.round.getComputerHand();
    }

    /**
     * get current computer score
     * @return integer that is the current computer score.
     */
    public int getComputerScore() {
        return this.computerScore;
    }

    /**
     * get current Human Score
     * @return integer that is the current human score.
     */
    public int getHumanScore() {
        return this.humanScore;
    }

    /**
     * get current round number
     * @return integer that is the current round number.
     */
    public int getRoundNumber() {
        return this.roundNumber;
    }

    /**
     * add a value to a computer current score
     * @param pipVal integer that we want added to current computer score
     */
    public void addComputerScore(int pipVal){
        this.computerScore += pipVal;
    }

    /**
     * add a value to a humans current score
     * @param pipVal integer that we want added to current human score
     */
    public void addHumanScore(int pipVal){
        this.humanScore += pipVal;
    }

    /**
     * set the round number variable
     * @param roundNumber integer that we want to set the round to
     */
    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    /**
     * create the intent after a round is over asking the user if they want to play another round
     */
    public void playAgain() {
        Intent playAgain = new Intent(this.activity, playAgainScreen.class);
        //in the intent, store the round number, human score, computer score, and engine integer.
        playAgain.putExtra("gameRoundNum", getRoundNumber() );
        playAgain.putExtra("gameHumanScore", getHumanScore());
        playAgain.putExtra("gameComputerScore", getComputerScore());
        playAgain.putExtra("roundEngineInt", round.getEngineValue());
        Log.d("myIntent", Integer.toString(playAgain.getIntExtra("gameComputerScore",-121312323)));
        Log.d("myIntent", Integer.toString(playAgain.getIntExtra("roundEngineInt",-121312323)));
        this.activity.startActivity(playAgain);
    }

    /**
     * Set the current engine int to a value passed in
     * @param engineVal integer that is the engine int
     */
    public void setEngineInt(int engineVal) {
        round.setEngineInt(engineVal);
    }

    /**
     * Load a game from a file
     * @param loadGameFile BufferedReader that was created when user clicked load game button
     * @throws IOException Throws an exception with file input and output
     */
    public void loadGame(BufferedReader loadGameFile) throws IOException {
        //check whether we will be loading in computer data or human data
        boolean computerData = false;
        boolean humanData = false;
        //read file line by line, and store a line of the file in string line
        String line;
        //loop through entire file while the next line isnt null
        while( (line = loadGameFile.readLine() ) != null) {
            //create words to track what we are tracking
            String firstWord;
            //get the entire line of the file, split the line on spaces and store it in arrOfWords
            String[] arrOfWords = line.split(" ");
            //store the first word since in a string since we will be checking it a lot
            firstWord = arrOfWords[0];
            //set round number to next value on the line.
            if (firstWord.equals("Round:")) {
                String roundNum = arrOfWords[1];
                //set the round number to 1 less then what it actually is, because we increment it by 1 when
                //we create game object.
                setRoundNumber(Integer.parseInt(roundNum) - 1);
                continue;
            }

            //set computer data to true, human data to false
            if (firstWord.equals("Computer:")) {
                computerData = true;
                humanData = false;
                continue;
            }

            //set human data to true, computer data to false
            if (firstWord.equals("Human:")) {
                computerData = false;
                humanData = true;
                continue;
            }

            //if our array of words has empty spaces for the first 3 things, then we're working on player data line
            //and the 4th thing in the array is the thing we are working on.
            if(arrOfWords.length> 4 ) {
                //if arrOfWords[3] is score, we are setting score for player
                if (arrOfWords[3].equals("Score:")) {
                    //next value is the score as a string, convert it to an int
                    String score = arrOfWords[4];
                    int scoreAsInt = Integer.parseInt(score);
                    //check if we are setting computer or human score
                    if (computerData) {
                        setComputerScore(scoreAsInt);
                    }
                    else {
                        setHumanScore(scoreAsInt);
                    }
                    continue;
                }
                //we are setting a players hand
                else if (arrOfWords[3].equals("Hand:")) {
                    //parse the line of tiles.
                    ArrayList<Tile> tiles = parseLineOfTiles(line, humanData);
                    //check if we are setting computer or human Hand
                    if(computerData) {
                        round.setPlayerHand(tiles, 0);
                    }
                    else {
                        round.setPlayerHand(tiles,1);
                    }
                }
                //we are setting a players train
                else if(arrOfWords[3].equals("Train:")) {
                    //parse line of tiles
                    ArrayList<Tile> tiles =  parseLineOfTiles(line, humanData);
                    //check if we are setting computer or human train
                    if(computerData){
                        //back of train contains engine that we dont want to print, so pop it off
                        tiles.remove(tiles.size()-1);
                        //reverse tiles so it prints to screen normally.
                        Collections.reverse(tiles);
                        round.setPlayerTrain(tiles, 0);

                    }
                    else {//human train
                        round.setEngineInt(tiles.get(0).getFirstNum());
                        //remove first tile which is engine, which we dont want to print more then once
                        tiles.remove(0);
                        round.setPlayerTrain(tiles, 1);

                    }
                }
            }
            //setting mexican train
            if(firstWord.equals("Mexican")) {
                //we are no longer dealing with computer/human data so set it to false
                computerData = false;
                humanData = false;
                //grab all the tiles on the line and set mexican train
                ArrayList<Tile> tiles = parseLineOfTiles(line, humanData);
                round.setPlayerTrain(tiles,2);
                continue;
            }
            //set the boneyard
            if(firstWord.equals("Boneyard:")) {
                //grab tiles on the line
                ArrayList<Tile> tiles = parseLineOfTiles(line, humanData);
                round.setPlayerHand(tiles,2);
                continue;
            }
            //we are setting whose turn it is next.
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

    /**
     * parse a string line of tiles and store it in an ArrayList, used in serialization
     * @param line String that stores an entire line of tiles
     * @param humanData boolean value that represents whether we are reading the human data or computers data
     * @return ArrayList<Tile> which is a list of all the tiles that were present on the string that was passed in.
     */
    ArrayList<Tile> parseLineOfTiles(String line, boolean humanData) {
        //tileArray will store all tiles that are present on line of the file, and we will return it
        ArrayList<Tile> tileArray = new ArrayList<Tile>();
        //split the line based on spaces, and store it in an array
        String[] arrOfTiles = line.split(" ");

        //check what the first value in arrOfTiles is, if its mexican then we are setting the mexican train
        if(arrOfTiles[0].equals("Mexican")) {
            //mexican train loop through from the second index in the array to the end and they will all be tiles
            for(int i = 2; i < arrOfTiles.length; i++) {
                int fnum = Integer.parseInt(String.valueOf(arrOfTiles[i].charAt(0)));
                int snum = Integer.parseInt(String.valueOf(arrOfTiles[i].charAt(2)));
                tileArray.add(new Tile(fnum,snum));

            }
            return tileArray;
        }
        //first word is boneyard,
        if(arrOfTiles[0].equals("Boneyard:")) {
            //loop through the first index of the array and everything after
            //will be tiles
            for(int i = 1; i < arrOfTiles.length; i++) {
                int fnum = Integer.parseInt(String.valueOf(arrOfTiles[i].charAt(0)));
                int snum = Integer.parseInt(String.valueOf(arrOfTiles[i].charAt(2)));
                tileArray.add(new Tile(fnum,snum));
            }
            return tileArray;
        }
        //check if the 4th index is an "M", meaning the train has a marker
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
        }
        //If the 4th index in the arrOfTiles is "Mexican",
        //the next word is train and we dont do anythign for it yet.
        if(arrOfTiles[4].equals("Mexican")) {
            //do nothing here.
        }
        //if first character is a digit, it is a tile.
        if(Character.isDigit( arrOfTiles[4].charAt(0))) {
            int fnum = Integer.parseInt( String.valueOf(arrOfTiles[4].charAt(0)));
            int snum = Integer.parseInt( String.valueOf(arrOfTiles[4].charAt(2)));
            tileArray.add(new Tile(fnum,snum));
        }

        //loop through the rest of the line, and since we have checked for boneyard or mexican train
        //all that's left is computer/human hand/train
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

    /**
     * set the humans score
     * @param scoreAsInt integer value that is the value the humans score should be set too
     */
    private void setHumanScore(int scoreAsInt) {
        this.humanScore = scoreAsInt;
    }

    /**
     * set the computer's score
     * @param scoreAsInt integer value that is the value the computers score should be set too
     */
    private void setComputerScore(int scoreAsInt) {
        this.computerScore = scoreAsInt;
    }

    /**
     * Get the tile that is the top of the boneyard (ie- next one to be drawn)
     * @return Tile that is the next tile to be drawn from boneyard
     */
    public Tile getTopOfBoneyard() {
        return this.round.getTopOfBoneyard();
    }

    /**
     * set humans turn to true, computers turn to false
     */
    public void setHumanTurn() {
        this.round.setHumanTurn();
    }

    /**
     * set computers turn to true, humans turn to false
     */
    public void setComputerTurn() {
        this.round.setComputerTurn();
    }

    /**
     * retrieve string explanation for why human played the moves it did
     * @return String that is the explanation for the human move
     */
    public String getHumanMoveExplanation(){
        return this.round.getHumanMoveExplanation();
    }

    /**
     * retrieve string explanation for why computer played the moves it did
     * @return String that is the explanation for the computers move
     */
    public String getComputerMoveExplanation(){
        return this.round.getComputerMoveExplanation();
    }

    /**
     *Clear the explanation for why the human played what they did in a turn
     */
    public void clearHumanMoveExplanation() {
        this.round.clearHumanMoveExplanation();
    }

    /**
     *Clear the explanation for why the computer played what they did in a turn
     */
    public void clearComputerMoveExplanation() {
        round.clearComputerMoveExplanation();
    }

    /**
     * Get the best move available for a human, this method called when they use help button
     */
    public void getBestHumanMove() {
        round.getBestHumanMove();
    }

    /**
     * Retrieve a particular train as a string that is formatted in serialization format
     * @param whoseTrain integer that represents whose train we are accessing, 0 for computer, 1 for human, 2 for mexican
     * @return String that represents the entirety of a train.
     */
    public String getTrainAsString(int whoseTrain) {
        String trainAsString = "";
        if(whoseTrain == 0) {
            //computer train
            trainAsString = round.getComputerTrainAsString();
        }
        else if (whoseTrain == 1) {
            trainAsString = round.getHumanTrainAsString();
        }
        else if (whoseTrain == 2){
            trainAsString = round.getMexicanTrainAsString();
        }
        return trainAsString;
    }


    /**
     * Save a game to a file
     * @param saveFileName FileOutputStream object created when user clicked "save game button" and game will be saved here.
     */
    public void saveGame(FileOutputStream saveFileName) {

        try {
            //FileOutputStream takes a number of bytes to write
            //and a string has the member function .getBytes that we use to properly send data
            //to the FileOutputStream object,

            //store round number since that's the first thing we store
            saveFileName.write("Round: ".getBytes(StandardCharsets.UTF_8));
            int tmpRoundNumber = getRoundNumber();
            saveFileName.write(Integer.toString(tmpRoundNumber).getBytes(StandardCharsets.UTF_8));

            //========================Computer Data To File===================
            saveFileName.write("\n\nComputer:\n   Score: ".getBytes(StandardCharsets.UTF_8));
            int tmpComputerScore = getComputerScore();
            saveFileName.write(Integer.toString(tmpComputerScore).getBytes(StandardCharsets.UTF_8));

            //store computer hand
            saveFileName.write("\n   Hand: ".getBytes(StandardCharsets.UTF_8));
            saveFileName.write(getComputerHand().handAsString().getBytes(StandardCharsets.UTF_8));

            //store computer train
            saveFileName.write("\n   Train: ".getBytes(StandardCharsets.UTF_8));
            saveFileName.write(getTrainAsString(0).getBytes(StandardCharsets.UTF_8));

            //========================Human Data To File===================

            saveFileName.write("\n\nHuman:\n   Score: ".getBytes(StandardCharsets.UTF_8));
            //store human score
            saveFileName.write(Integer.toString(tmpComputerScore).getBytes(StandardCharsets.UTF_8));
            //store human hand
            saveFileName.write("\n   Hand: ".getBytes(StandardCharsets.UTF_8));
            saveFileName.write(getHumanHand().handAsString().getBytes(StandardCharsets.UTF_8));
            //store human train
            saveFileName.write("\n   Train: ".getBytes(StandardCharsets.UTF_8));
            saveFileName.write(getTrainAsString(1).getBytes(StandardCharsets.UTF_8));

            //========================Mexican Train To File===================
            saveFileName.write("\n\nMexican Train: ".getBytes(StandardCharsets.UTF_8));
            saveFileName.write(getTrainAsString(2).getBytes(StandardCharsets.UTF_8));

            //========================Boneyard to file.================
            saveFileName.write("\n\nBoneyard: ".getBytes(StandardCharsets.UTF_8));
            saveFileName.write(getBoneyardHand().handAsString().getBytes(StandardCharsets.UTF_8));

            //next player to file
            saveFileName.write("\n\nNext Player: ".getBytes(StandardCharsets.UTF_8));
            if(getHumanTurn()) {
                saveFileName.write("Human".getBytes(StandardCharsets.UTF_8));
            }
            else {
                saveFileName.write("Computer".getBytes(StandardCharsets.UTF_8));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Check if a human skipped its turn with the boneyard being empty
     * @return boolean value, true if human skipped turn while boneyard was empty, false otherwise
     */
    public boolean isSkippedHumanTurn() {
        return skippedHumanTurn;
    }

    /**
     * Set skippedhumanTurn to a value passed in
     * @param skippedHumanTurn boolean value that gets passed in.
     */
    public void setSkippedHumanTurn(boolean skippedHumanTurn) {
        this.skippedHumanTurn = skippedHumanTurn;
    }

    /**
     * Check if a computer skipped its turn with the boneyard being empty
     * @return boolean value, true if computer skipped turn while boneyard was empty, false otherwise
     */
    public boolean isSkippedComputerTurn() {
        return skippedComputerTurn;
    }

    /**
     * Set skippedComputerturn to a boolen value passed in
     * @param skippedComputerTurn boolean value
     */
    public void setSkippedComputerTurn(boolean skippedComputerTurn) {
        this.skippedComputerTurn = skippedComputerTurn;
    }

    /**
     * Check if a game is "Hung" ie - boneyard is empty and neither player can make a move
     */
    public void checkHungGame() {
        //hung game occurs when human skipped turn and computer skipped turn are both true
        //these are only set to true when boneyard is empty
        if(isSkippedHumanTurn() && isSkippedComputerTurn()) {
            Log.d("myTag", "Game over skipped.");
            computerScore += round.getComputerPips();
            humanScore += round.getHumanPips();

            //create intent and load it with relevant data.
            //have to store roundnum, computer score, humanscore, and engineint in intent
            //so we can access it if the user plays a new round.
            Intent endRoundIntent = new Intent(this.activity, HungGame.class);
            endRoundIntent.putExtra("gameRoundNum", getRoundNumber() );
            endRoundIntent.putExtra("gameHumanScore", getHumanScore());
            endRoundIntent.putExtra("gameComputerScore", getComputerScore());
            endRoundIntent.putExtra("roundEngineInt", round.getEngineValue());
            this.activity.startActivity(endRoundIntent);
        }
    }

    /**
     * Set any orphan doubles created by the human player during their move.
     */
    public void setHumanPlayerOrphanDoubles() {
        round.setHumanPlayerOrphanDoubles();
    }

    /**
     * Reset values of human/computer/mexican train, clear markers, orphan doubles etc.
     */
    public void resetValues() {
        round.resetValues();
    }
}

