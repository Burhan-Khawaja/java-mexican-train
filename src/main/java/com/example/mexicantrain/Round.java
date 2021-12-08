package com.example.mexicantrain;

import android.app.Activity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

public class Round {
    //CLASS VARIABLES
    private Hand boneyard = new Hand();
    private Train mexicanTrain = new Train();
    private Human humanPlayer = new Human(this.activity);
    private Computer computerPlayer = new Computer(this.activity);
    private Activity activity;

    private int engineInt;

    private Queue<Integer> engineQueue = new ArrayDeque<Integer>();
    private boolean humanTurn;
    private boolean computerTurn;
    private boolean computerWon;
    private boolean humanWon;


    //test value.
    private boolean humanHasMove;

    //these might have been set but never used. double check that.
    private int humanScore;
    private int computerScore;
    private int roundNumber;

    //CONSTRUCTOR

    //BURBUR MIGHT NOT NEED ACTIVITY CLASS
    /**
     * Constructor for Round class. Takes an activity, however it is not used after we switched to MVC
     * and it is legacy code
     * @param activity
     */
    Round(Activity activity) {
        this.activity = activity;
        //set activity through a setter b/c if we use constructor, then this.activity is not initialized,
        // and we set it to a null value in other classes.
        humanPlayer.setActivity(this.activity);
    }

    //SELECTORS

    /**
     * return the human and computer train as strings formatted properly
     * @return String that is both trains concatenated with the engine tile
     */
    public String getHumanAndComputerTrain(){
        String trains = "";
        if(computerPlayer.getTrainMarker()) {
            trains += " M ";
        }
        trains += computerPlayer.trainAsString()  + " " + engineInt + " - " + engineInt + " " + humanPlayer.trainAsString();
        if(humanPlayer.getTrainMarker()) {
            trains += " M ";
        }
        return trains;
    }

    /**
     * get the next value in the queue of engine values
     * @return integer value that is the next engine value
     */
    public int getNextEngineValue() {
        if(engineQueue.isEmpty()) {
            resetEngineQueue();
        }
        this.engineInt = engineQueue.remove();
        return engineInt;
    }

    /**
     * return the human players hand, used for serialization
     * @return Hand object that is the human players hand
     */
    public Hand getHumanHand() {
        return this.humanPlayer.getHand();
    }

    /**
     * Return the computer players hand, used for serialization
     * @return Hand object that is the computer players hand
     */
    public Hand getComputerHand() {
        return this.computerPlayer.getHand();
    }

    /**
     * return the class boolean value human turn that is true if its the humans turn
     * @return boolean value that is humanTurn
     */
    public boolean getHumanTurn() {
        return this.humanTurn;
    }

    /**
     * return mexican train as a string representation
     * @return String that represents the tiles on the mexican train
     */
    public String getMexicanTrain() {
        return mexicanTrain.trainAsString();
    }

    /**
     * return the current round engine value
     * @return integer value that is the engine
     */
    public int getEngineValue() {
        return this.engineInt;
    }

    //MUTATORS

    /**
     * set the current playable trains for the human
     */
    public void setPlayableTrains() {
        //check for orphan doubles, if it exists then set orphaned trains as playable
        //if false, then get marker on cpu train and set human to playable
        if(humanPlayer.checkOrphanDoubles(humanPlayer,computerPlayer,mexicanTrain) == false) {
            humanPlayer.humanTrainPlayable = true;
            humanPlayer.computerTrainPlayable= computerPlayer.getTrainMarker();
            humanPlayer.mexicanTrainPlayable = true;
        }
    }

    /**
     * set the train end numbers to the correct value, which is the engineInt
     */
    private void setTrainEndNumbers() {
        mexicanTrain.setTrainEndNumber(this.engineInt);
        humanPlayer.playerTrain.setTrainEndNumber(this.engineInt);
        computerPlayer.playerTrain.setTrainEndNumber(this.engineInt);
    }

    /**
     * Set the engineVal to be the correct number. get the value from the enginequeue and reset it if needed
     * @param engineVal- The new value that will be our engine int.
     */
    public void setEngineInt(int engineVal) {
        //if the engineQueue is empty, then reset it
        if (engineQueue.isEmpty()) {
            resetEngineQueue();
        }
        //pop values off the queue until we reach the value we need.
        while (this.engineInt != engineVal) {
            engineInt = getNextEngineValue();
        }
    }

    //HELPER FUNCTIONS

    /**
     * create a double nine set of tiles, as well as shuffle the tiles and deal the tiles to the human and computer player.
     */
    public void dealTiles() {
        //arraylist to store all tiles
        ArrayList<Tile> tiles = new ArrayList<>();

        //create double nine set of tiles.
        for (int i = 0; i < 10; i++) {
            for (int j = i; j < 10; j++) {
                tiles.add(new Tile(i,j));
            }
        }


        //shuffle all tiles
        //loop through all the tiles
        //generate a random number from 0-size of array,
        // and then swap the tile at the current index with the tile at the random position
        Random rng = new Random();
        for (int i = 0; i < tiles.size(); i++) {
            int randomNum = rng.nextInt(tiles.size() - 1);
            Tile tmpTile = tiles.get(i);
            tiles.set(i, tiles.get(randomNum));
            tiles.set(randomNum, tmpTile);
        }

        //deal 16 tiles to human, 16 to computer, and rest add to boneyard.
        for(int i = 0; i < tiles.size(); i++) {
            //check if the tile we are dealing is the engine tile.
            // if it is, continue because we do not want to add it anywhere.
            if(tiles.get(i).getFirstNum() == this.engineInt && tiles.get(i).getSecondNum() == this.engineInt) {
                continue;
            }
            if(i < 16) {
                humanPlayer.addTileToHand(tiles.get(i));
            }
            else if (i < 32) {
                //Add tiles to computer players hand.
                 computerPlayer.addTileToHand(tiles.get(i));
            }
            else {
                boneyard.addTile(tiles.get(i));
            }
        }

    }



    /**
     * reset the queue of engine tiles to be 9-0 descending.
     */
    public void resetEngineQueue() {
        this.engineQueue.clear();
        //is there a better way of doing this? probably. Just refactor it later lol.
        //at the very least its a simple and understandable solution. just add 9-0 to the queue one at a time.
        this.engineQueue.add(9);
        this.engineQueue.add(8);
        this.engineQueue.add(7);
        this.engineQueue.add(6);
        this.engineQueue.add(5);
        this.engineQueue.add(4);
        this.engineQueue.add(3);
        this.engineQueue.add(2);
        this.engineQueue.add(1);
        this.engineQueue.add(0);

    }


    /**
     * Start the round, meaning we set up the round variable for the game to begin
     * @param serializedStart - boolean to check if we are loading in a game, or are starting a fresh game
     * @param humanScore - Current human score.
     * @param computerScore - Current comptuer score
     * @param roundNumber - Current round number
     */
    public void startRound(boolean serializedStart, int humanScore, int computerScore, int roundNumber) {
        if(!serializedStart) {
            this.roundNumber = 0;
            this.engineInt = getNextEngineValue();
            dealTiles();
            setTrainEndNumbers();
            //BURBUR who goes first
            humanTurn = true;
        }
        this.roundNumber = roundNumber;
        this.humanScore = humanScore;
        this.computerScore = computerScore;
    }

    /**
     * Play a tile on a train.
     * @param userTile Tile object that is what the user wants to play. (-1,-1) if its the comptuers turn
     * @param trainToPlayOn Character that represents what train to place tile on
     * @return integer value that represents either the sum of pips of the loser, or 0 if its the next players move.
     */
    public int playTile(Tile userTile, char trainToPlayOn) {
        int humanPipsValue = 0;
        int computerPipsValue = 0;


        //if we want to have the computer make a move, pass in a tile with values of -1, -1
        if(userTile.getFirstNum() == -1 && userTile.getSecondNum() == -1) {
            computerTurn = true;
            humanTurn = false;
        }

        if(humanTurn) {
            //play tile on train
            humanPipsValue = humanPlayer.play(humanPlayer, computerPlayer, mexicanTrain, boneyard, userTile, trainToPlayOn);
            //if player.play returns positive number, then human won
            if(humanPipsValue > 0) {
                humanWon = true;
            }
            return humanPipsValue;
        }

        if(computerTurn && userTile.getFirstNum() == -1 && userTile.getSecondNum() == -1) {
            //if player.play returns positive number, then human won
            computerPipsValue = computerPlayer.play(humanPlayer, computerPlayer, mexicanTrain, boneyard, userTile, trainToPlayOn);
            computerTurn = false;
            humanTurn = true;
            return computerPipsValue;
        }
        //if we return 0, then game is still going on.
        return 0;
    }

    /**
     * Check if a player has a move and deal with the rule set of what occures when user doesnt have a turn
     * @return boolean value, true if the tile drawn by the user is playable, false otherwise
     */
    public boolean playerHasValidMove() {
        //check if they user has a move
        boolean existsValidMove = humanPlayer.existsValidMove(humanPlayer, computerPlayer, mexicanTrain);


        //player does not have a valid move.
        if (!existsValidMove) {
            //go through procedure for when a user has no playable tiles.
            boolean skipTurn = humanPlayer.noPlayableTiles(humanPlayer, computerPlayer, mexicanTrain, boneyard);
            //the tile the user picked is not playable,so place a marker on their train and skip their turn
            if (!skipTurn) {
                computerTurn = true;
                humanTurn = false;
                //tile drawn is not playable, place marker and skip turn
                this.humanPlayer.setTrainMarker();
                this.humanPlayer.setStringMoveExplanation(" The tile drawn is not playable, a marker has been placed on your train");
                return false;
            }
            else {
                //tile picked is playable. play it.
                this.humanPlayer.setStringMoveExplanation(" The tile you picked from the boneyard is playable. Restarting turn.");
                return true;
            }
        }
        return true;
    }


    /**
     * Set a players hand to an ArrayList of tiles, used for serialization
     * @param tiles ArrayList that is a list of tiles that should be a users hand
     * @param whoseHand integer that represents whose hand we are setting, 0 = computer, 1 = human 2 = boneyard
     */
    public void setPlayerHand(ArrayList<Tile> tiles, int whoseHand) {
        for (int i = 0; i < tiles.size(); i++) {
            if (whoseHand == 0) {
                //add to computer hand
                computerPlayer.addTileToHand(tiles.get(i));
            }
            else if (whoseHand == 1) {
                //add to human hand
                humanPlayer.addTileToHand(tiles.get(i));
            }
            else if (whoseHand == 2) {
                //add to boneyard.
                boneyard.addTile(tiles.get(i));
            }
        }

    }

    /**
     * Set a players train to an arraylist passed in, set markers and orphan doubles as well
     * @param tiles ArrayList that represents the tiles that should be on a players train
     * @param whoseTrain integer that represents whose train we are setting, 0 = computer, 1 = human 2 = boneyard
     */
    public void setPlayerTrain(ArrayList<Tile> tiles, int whoseTrain) {
        if (tiles.isEmpty()) {
            return;
        }
        for (int i = 0; i < tiles.size(); i++) {
            //set train values
            if (whoseTrain == 0) {
                //add to computer hand
                computerPlayer.addTileFront(tiles.get(i));
            }
            else if (whoseTrain == 1) {
                //add to human hand
                humanPlayer.addTileBack(tiles.get(i));
            }
            else if (whoseTrain == 2) {
                //add to boneyard.
                mexicanTrain.addTileBack(tiles.get(i));
            }
        }

        //set train end numbers.
        //also check front of computer train/back of human train and mexican train for orphan doubles.
        if (whoseTrain == 0) {
            //set computer train end number
            computerPlayer.setTrainEndNumber(tiles.get(tiles.size() - 1).getFirstNum());
            if (tiles.get(tiles.size() - 1).isDouble()) {
                //set orphan double
                computerPlayer.setOrphanDouble(true);
            }
        }
        else if (whoseTrain == 1) {
            //set human train end number
            humanPlayer.setTrainEndNumber(tiles.get(tiles.size() - 1).getSecondNum());
            if (tiles.get(tiles.size() - 1).isDouble()) {
                //set orphan double
                humanPlayer.setOrphanDouble(true);
            }
        }
        else if (whoseTrain == 2) {
            //set mexican train end number
            mexicanTrain.setTrainEndNumber(tiles.get(tiles.size() - 1).getSecondNum());
            if (tiles.get(tiles.size() - 1).isDouble()) {
                //set orphan double
                mexicanTrain.setOrphanDouble(true);
            }
        }

    }

    /**
     * Get top of boneyard
     * @return Tile that represents the top of the boneyard
     */
    public Tile getTopOfBoneyard() {
        return this.boneyard.getTile(0);
    }

    /**
     * Set the computer turn to true and human turn to false
     */
    public void setComputerTurn() {
        this.computerTurn = true;
        this.humanTurn = false;

    }

    /**
     * Set the human turn to true and the computer turn to false
     */
    public void setHumanTurn() {
        this.humanTurn = true;
        this.computerTurn = false;

    }

    /**
     * set a trains marker to true
     * @param whoseTrain whose marker are we setting, 0 for computer 1 for human
     */
    public void setTrainMarker(int whoseTrain) {
        if(whoseTrain == 0) {
            //set computer marker
            computerPlayer.setTrainMarker();
        }
        else if (whoseTrain == 1){
            humanPlayer.setTrainMarker();
        }
    }

    /**
     * get the string that is the explanation of what the human player played
     * @return String object that represents the human players move
     */
    public String getHumanMoveExplanation(){
        return this.humanPlayer.getMoveExplanation();
    }

    /**
     * get the string that is the explanation of what the computer player played
     * @return String object that represents the computer players move
     */
    public String getComputerMoveExplanation(){
        return this.computerPlayer.getMoveExplanation();
    }

    /**
     * clear the string that represents the humans move explanation
     */
    public void clearHumanMoveExplanation() {
        this.humanPlayer.clearMoveExplanation();
    }
    /**
     * clear the string that represents the computer move explanation
     */
    public void clearComputerMoveExplanation() {
        this.computerPlayer.clearMoveExplanation();
    }

    /**
     * get the humans best move, called when user asks for help.
     */
    public void getBestHumanMove() {
        //before we figure out what moves are playable, we have to set the train we can play on
        if(!humanPlayer.checkOrphanDoubles(humanPlayer, computerPlayer, mexicanTrain)) {
            humanPlayer.computerTrainPlayable = computerPlayer.getTrainMarker();
            humanPlayer.humanTrainPlayable = true;
            humanPlayer.mexicanTrainPlayable = true;
        }
        ArrayList<Tile> bestTiles = new ArrayList<Tile>();
        ArrayList<String> bestTrains = new ArrayList<String>();
        humanPlayer.findBestMove(humanPlayer,computerPlayer,mexicanTrain, boneyard, bestTiles, bestTrains);
        String bestMove = humanPlayer.interpretBestMove(bestTiles,bestTrains);
        humanPlayer.setStringMoveExplanation(" The computer suggests: " + bestMove);
    }

    /**
     * get the computer train as a string, and any markers.
     * @return String that represents all the tiles on the computer train
     */
    public String getComputerTrainAsString() {
        String trainAsString = "";
        //check if train has marker. computer marker is added to start of computer train
        if(computerPlayer.getTrainMarker()) {
            trainAsString += "M ";
        }
        //add all the tiles to the train
        trainAsString += computerPlayer.getTrainAsString();
        //add engine tile to end of train
        trainAsString += engineInt + "-" + engineInt;
        return trainAsString;
    }

    /**
     * get the human train as a string, and any markers.
     * @return String that represents all the tiles on the human train
     */
    public String getHumanTrainAsString() {
        //add engine int first to human train
        String trainAsString = engineInt + "-" + engineInt + " ";
        //add all tiles to train for human
        trainAsString += humanPlayer.getTrainAsString();
        //if there is a marker it gets added last to train
        if(humanPlayer.getTrainMarker()) {
            trainAsString += "M";
        }
        return trainAsString;
    }

    /**
     * Get all tiles on mexican train as a string, used for serialization
     * @return String that represents all the tiles on mexican trian
     */
    public String getMexicanTrainAsString() {
        return mexicanTrain.trainAsStringSerialization();
    }

    /**
     * Get boneyard Hand object for serialization
     * @return Hand object that holds the boneyards tiles
     */
    public Hand getBoneyardHand() {
        return this.boneyard;
    }

    /**
     * Get the sum of pips of the tiles in the human players hand
     * @return integer value that represents the sum of pips
     */
    public int getHumanPips() {
        return humanPlayer.sumOfPips();
    }

    /**
     * Get the sum of pips of the tiles in the computer players hand
     * @return integer value that represents the sum of pips
     */
    public int getComputerPips() {
        return computerPlayer.sumOfPips();
    }

    /**
     * set the human players orphan doubles based on what trains they play
     */
    public void setHumanPlayerOrphanDoubles() {
        //humanTrainsPlayed stores 0,1 or 2 strings that represent what trains the human played tiles on
        ArrayList<String> humanTrainsPlayed = this.humanPlayer.getTrainsPlayed();

        //user played 2 tiles, so theres a chance that there is 1 orphan double.
        if (humanTrainsPlayed.size() >= 2) {
            if (!humanTrainsPlayed.get(0).equals(humanTrainsPlayed.get(1))) {
                //train user played on arent equal to each other, so make the first train an orphan double
                if (humanTrainsPlayed.get(0).equals("m")) {
                    mexicanTrain.setOrphanDouble(true);
                }
                else if (humanTrainsPlayed.get(0).equals( "c")) {
                    computerPlayer.setOrphanDouble(true);
                }
                else {
                    humanPlayer.setOrphanDouble(true);
                }
            }
        }

        //user played 3 tiles, so we check if the second tile they played is also an orphan double.
        if (humanTrainsPlayed.size() == 3) {
            if (!humanTrainsPlayed.get(1).equals(humanTrainsPlayed.get(2))) {
                //train user played on arent equal to each other, so make the first train an orphan double
                if (humanTrainsPlayed.get(1).equals( "m")) {
                    mexicanTrain.setOrphanDouble(true);
                }
                else if (humanTrainsPlayed.get(1).equals( "c")) {
                    computerPlayer.setOrphanDouble(true);
                }
                else {
                    humanPlayer.setOrphanDouble(true);
                }
            }

        }
        humanPlayer.clearTrainsPlayed();
    }

    /**
     * Reset all human/computer/mexican values so we can start a new round.
     */
    public void resetValues() {
        humanPlayer.clearTrainsPlayed();
        humanPlayer.clearMoveExplanation();
        humanPlayer.clearTrainMarker();
        humanPlayer.setOrphanDouble(false);

        computerPlayer.clearMoveExplanation();
        computerPlayer.clearTrainMarker();
        computerPlayer.setOrphanDouble(false);

        mexicanTrain.clearTrainMarker();
        mexicanTrain.setOrphanDouble(false);
    }
}


