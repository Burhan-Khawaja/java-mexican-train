package com.example.mexicantrain;

import android.app.Activity;
import android.util.Log;

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

    //BURBUR TESTING STORING HUMAN/COMPUTER/ROUND NUMBER IN ROUND CLASS
    private int humanScore;
    private int computerScore;
    private int roundNumber;

    //CONSTRUCTOR

    //BURBUR MIGHT NOT NEED ACTIVITY CLASS
    /**
     * Constructor for Round class
     * @param activity Activity object used to change display. this variable is unused since we have shifted to an MVC model
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
        String trains = computerPlayer.trainAsString()  + " " + engineInt + " - " + engineInt + " " + humanPlayer.trainAsString();
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
     * return the human players hand
     * @return Hand object that is the human players hand
     */
    public Hand getHumanHand() {
        return this.humanPlayer.getHand();
    }

    /**
     * Return the computer players hand
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
     * @return String that represents the tiles on the mexican trian
     */
    public String getMexicanTrain() {
        return mexicanTrain.trainAsString();
    }

    /**
     * get the winner of the round
     * @return boolean value, true if human won false otherwise
     */
    public boolean getWinner() {
        if(humanWon) {
            return true;
        }
        else {
            return false;
        }
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
     * set the current playable trains
     */
    public void setPlayableTrains(){
        //orphan double check
        //if false, then get marker on cpu train and set human to playable
        if(humanPlayer.checkOrphanDoubles(humanPlayer,computerPlayer,mexicanTrain) == false) {
            humanPlayer.humanTrainPlayable = true;
            humanPlayer.computerTrainPlayable= humanPlayer.getTrainMarker();
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
    public void dealTiles(){

        //track if we should add tiles to human players hand, computer players hand, or boneyard.
        //BURBUR refactor line below?
        int totalTileCreated = 0;
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
        //generate a random number from 0-size of array, and then swap the tile at the current index with the tile at the random position
        Random rng = new Random();
        for (int i = 0; i < tiles.size(); i++) {
            int randomNum = rng.nextInt(tiles.size() - 1);
            Tile tmpTile = tiles.get(i);
            tiles.set(i, tiles.get(randomNum));
            tiles.set(randomNum, tmpTile);
        }

        //deal 16 tiles to human, 16 to computer, and rest add to boneyard.
        for(int i = 0; i < tiles.size(); i++) {
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
    *Start the round class and initialize all round data.
    *@param serializedStart boolean value that tracks whether we load a game or start a new one
    *@param humanScore integer value that stores the humans score
    *@param computerScore integer value that stores the computers score
    *@param roundNumber integer value that stores the current round number
    *@return integer value that //BURBUR should be a void function
     */
    public int startRound(boolean serializedStart, int humanScore, int computerScore, int roundNumber) {


        if(!serializedStart) {
            this.roundNumber = 0;
            dealTiles();
            //BURBUR REMOVE ENGINE TILE
            this.engineInt = getNextEngineValue();
            setTrainEndNumbers();
            //humanPlayer.addTileToHand(new Tile(9,9));
            //BURBUR who goes first
            humanTurn = true;
        }
        this.roundNumber = roundNumber;
        this.humanScore = humanScore;
        this.computerScore = computerScore;
        //int pipsValue = startTurns(humanPlayer,humanPlayer,mexicanTrain,boneyard, new Tile(-1,-1), ' ');
        return 0;
    }


    /**
    *@param userTile Tile object that is what the user chose to play.
    *@param trainToPlayOn character that represents what train the user wants to play on 
    *@return integer value that repreents a code based on what happened
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
            Log.d("myTag", "human Turn TURN");

            //bottomText.setText("Humans turn. Select a tile to play.");
            //gameFeed.addView(bottomText);
            //displayRoundState();
            humanPipsValue = humanPlayer.play(humanPlayer, computerPlayer, mexicanTrain, boneyard, userTile, trainToPlayOn);
            if(humanPipsValue > 0) {
                humanWon = true;
            }
            return humanPipsValue;
        }

        if(computerTurn == true && userTile.getFirstNum() == -1 && userTile.getSecondNum() == -1) {
            Log.d("myTag", "COMPUTER TURN");
            //bottomText.setText("COMPUTER TURN!");
            //gameFeed.addView(bottomText);

            computerPipsValue = computerPlayer.play(humanPlayer, computerPlayer, mexicanTrain, boneyard, userTile, trainToPlayOn);
            computerTurn = false;
            humanTurn = true;
            //after computer is done with its turn, display round state.
            //displayRoundState();
            return computerPipsValue;
        }
        return 0;
    }

    /**
    *checks if a player has a valid move.
    @return boolean value, true if user has a move, false if user has to skip their turn after drawing from boneyard
     */
    public boolean playerHasValidMove() {
        boolean existsValidMove = humanPlayer.existsValidMove(humanPlayer, computerPlayer, mexicanTrain);

        //-1: Player does not have a valid move.
        //-666: boneyard is empty and player has to skip tern.
        //-2: player picks a tile and it is playable.
        //player does not have a valid move.
        if (existsValidMove == false) {
            //go through procedure for when a user has no playable tiles.
            boolean skipTurn = humanPlayer.noPlayableTiles(humanPlayer, computerPlayer, mexicanTrain, boneyard);
            //the tile the user picked is not playable,so place a marker on their train and skip their turn
            if (skipTurn == false) {
                computerTurn = true;
                humanTurn = false;
                //tile drawn is not playable, place marker and skip turn
                //BURBUR NEED TO ADD MARKER TO TRAIN.
                //bottomText.setText("The tile you picked from the boneyard is not playable.");
                return false;
            }
            else { //tile picked is playable. play it.
                //bottomText.setText("The tile you picked from the boneyard is playable. Restarting turn.");
                return true;
            }
        }
        return true;
    }

    /**
    * Set a player hand equal to an ArrayList passed in, used for serialization
    *@param tiles ArrayList of Tiles that stores a players hand
    *@param whoseHand integer value that represents whose hand we are setting: 0 if its computers, 1 is human, 2 for boneyard
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
    * Set a player train equal to an ArrayList passed in, used for serialization
    *@param tiles ArrayList of Tiles that stores a players train
    *@param whoseHand integer value that represents whose hand we are setting: 0 if its computers, 1 is human, 2 for boneyard
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
                computerPlayer.setOrphanDouble();
            }
        }
        else if (whoseTrain == 1) {
            //set human train end number
            humanPlayer.setTrainEndNumber(tiles.get(tiles.size() - 1).getSecondNum());
            if (tiles.get(tiles.size() - 1).isDouble()) {
                //set orphan double
                humanPlayer.setOrphanDouble();
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
    *Return the tile thats at the top of boneyard
    *@return Tile object that is the top tile of the boneyard
    */
    public Tile getTopOfBoneyard() {
        return this.boneyard.getTile(0);
    }

    /**
    * set the computer turn to true and human turn to false
    */
    public void setComputerTurn() {
        this.computerTurn = true;
        this.humanTurn = false;

    }

    /**
    * set the human turn to true and computer turn to false
    */
    public void setHumanTurn() {
        this.humanTurn = true;
        this.computerTurn = false;

    }

    /**
    * set the players train marker to be toggled on 
    *@param whoseTrain - integer value that represents whose train we are marking, 0 for computer train and 1 for human train
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
    *Get the human players move explination, and the string that represents what they did 
    *@return String that represents what the human played
    */
    public String getHumanMoveExplanation(){
        return this.humanPlayer.getMoveExplanation();
    }
    /**
    *Get the computers players move explination, and the string that represents what they did
    *@return String that repreents what the computer played.
    */
    public String getComputerMoveExplanation(){
        return this.computerPlayer.getMoveExplanation();
    }
}


