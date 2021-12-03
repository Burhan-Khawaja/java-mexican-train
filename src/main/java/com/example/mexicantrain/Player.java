package com.example.mexicantrain;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;

public abstract class Player {

    protected Hand playerHand = new Hand();
    protected Activity activity;
    protected Train playerTrain = new Train();
    protected boolean humanTrainPlayable;
    protected boolean computerTrainPlayable;
    protected boolean mexicanTrainPlayable;
    //string will store the last move played by the user
    //this will be used so we can output to the screen what the player did.
    protected String moveExplanation;

    /**
    * get a players hand as a Hand object. Used to print to screen
    *@return Hand object that represents a players hand
    */
    public Hand getHand() {
        return this.playerHand;
    }

    /**
    * get the hand as an arrayList
    * @return ArrayList<Tile> that represents all the players hand
    */
    public ArrayList<Tile> getHandArrayList() {
        return this.playerHand.getHandArrayList();
    }
    //BURBUR why do we have the 2 things above they are the same thing

    /**
     * Add a tile to a players hand
     * @param tileToAdd Tile object that gets added to a players hand
     */
    public void addTileToHand(Tile tileToAdd){
        playerHand.addTile(tileToAdd);
    }

    /**
    *Set a players train marker to true
    */
    public void setTrainMarker() {
        this.playerTrain.setMarker(true);
    }

    /**
     * set the member variable activity so we can access gui
     * @param _activity Activity object
     */
    public void setActivity(Activity _activity) {
        this.activity = _activity;
    }//BURBUR does this need to be here?

    /**
     * Checks if a players hand is empty
     * @return boolean value, true if hand is empty false otherwise
     */
    public boolean handEmpty(){
        if(playerHand.getSize() == 0) {
            return true;
        }
        return false;
    }

    /**
    * returns the entirety of a players train as a string
    *@return String that represents the players train
    */
    public String trainAsString(){
        return playerTrain.trainAsString();
    }

    /**
    * Add a tile to the front of a players train, at index 0
    *@param tileToAdd tile object that gets added to train
    */
    public void addTileFront(Tile tileToAdd) {
        playerTrain.addTileFront(tileToAdd);
    }

    /**
    * add a tile to the back of a players train at index size()
    *@param tileToAdd tile object that gets added to train
    */
    public void addTileBack(Tile tileToAdd) {
        playerTrain.addTileBack(tileToAdd);
    }

    /**
    *Start a players turn
    *@param humanPlayer Player object that holds human player data
    *@param computerPlayer Player object that holds computer player data
    *@param mexicanTrain Train object that holds all mexicanTrain data
    *@param Boneyard Hand object that stores all of the tiles in boneyard
    *@param tileToPlay Tile object that represents the tile that the user is playing
    *@param trainToPlay char that represents what train user is playing on.
    */
    abstract int play(Player humanPlayer, Player computerPlayer, Train mexicanTrain, Hand Boneyard, Tile tileToPlay, char trainToPlay);

    public boolean tileFitsOnTrain(Tile tileToCheck, int trainEndNumber) {
        if(tileToCheck.getFirstNum() == trainEndNumber) {
            return true;
        }
        else if (tileToCheck.getSecondNum()== trainEndNumber) {
            return true;
        }
        return false;
    }
    public int getTrainEndNumber() {
        return this.playerTrain.getTrainEndNumber();
    }

    public boolean existsValidMove(Player humanPlayer, Player computerPlayer, Train mexicanTrain) {
        boolean validMove = false;

        if(this.humanTrainPlayable && playerHasMove(humanPlayer.getTrainEndNumber())) {
            validMove = true;
        }

        if(this.computerTrainPlayable && playerHasMove(computerPlayer.getTrainEndNumber())) {
            validMove = true;
        }

        if(mexicanTrainPlayable && playerHasMove(mexicanTrain.getTrainEndNumber())) {
            validMove = true;
        }
        return validMove;
    }

    public boolean playerHasMove(int trainEndNumber) {
        for(int i = 0; i < playerHand.getSize(); i++) {
            if(playerHand.getTile(i).getFirstNum() == trainEndNumber) {
                return true;
            }
            else if (playerHand.getTile(i).getSecondNum() == trainEndNumber) {
                return true;
            }
        }
        return false;
    }

    boolean noPlayableTiles(Player humanPlayer, Player computerPlayer, Train mexicanTrain, Hand boneyard) {
        if(boneyard.getSize() == 0) {
            //BURBUR add error message that boneyard is empty and a marker is placed on the train
            //this.settrain
            Log.d("myTag", "ERROR: BONEYARD IS EMPTY AND YOU HAVE NO MOVES. A MARKER IS PLACED ON YOUR TRAIN");
        }
        else {
            //get top tile of boneyard.
            Tile boneyardTile = boneyard.getTile(0);
            //remove that tile from the boneyard
            boneyard.removeTile(boneyardTile.getFirstNum(),boneyardTile.getSecondNum());
            //add the tile to players hand
            this.addTileToHand(boneyardTile);
            //check now if user has a valid move after taking the tile from the boneyard

            boolean validMove  = this.existsValidMove(humanPlayer,computerPlayer,mexicanTrain);
            if(!validMove) {
                //user does not have a valid move. place marker, return false.
                Log.d("myTag", "USER HAS NO VALID MOVE AFTER TAKING TILE FROM BONEYARD");
                //BURBUR set train marker here
                return false;

            }
            else {
                //the tile selected is playable.
                return true;
            }

        }
        return false;
    }

    boolean checkOrphanDoubles(Player humanPlayer, Player computerPlayer, Train mexicanTrain)
    {
        boolean orphanDouble = false;
        //1- check if there exists any orphan double on a train
        if (humanPlayer.playerTrain.getOrphanDouble() == true || computerPlayer.playerTrain.getOrphanDouble() == true || mexicanTrain.getOrphanDouble() == true) {
            //make all trains unplayable. later we will check which trains have orphan doubles and set them as the only trains playable
            this.humanTrainPlayable = false;
            this.computerTrainPlayable = false;
            this.mexicanTrainPlayable = false;
        }
        //check if each train has an orphan double. if it does, make that train playable, and set orphanDouble to true.
        if (humanPlayer.playerTrain.getOrphanDouble() == true) {
            //BURBUR output that theres an orphandouble on humantrain
            this.humanTrainPlayable = true;
            orphanDouble = true;
        }
        if (computerPlayer.playerTrain.getOrphanDouble() == true) {
            //BURBUR output that theres an orphandouble on
            this.computerTrainPlayable = true;
            orphanDouble = true;
        }
        if (mexicanTrain.getOrphanDouble() == true) {
            //BURBUR output that theres an orphandouble on
            this.mexicanTrainPlayable = true;
            orphanDouble = true;
        }
        return orphanDouble;
    }

    public boolean checkUserTrainPlayable(char userTrainChoice) {
        if (userTrainChoice == 'h' && !this.humanTrainPlayable) {
        //    std::cout << "Error: You are not allowed to play on the human train.";
            return false;
        }
        else if (userTrainChoice == 'c' && !this.computerTrainPlayable) {
        //    std::cout << "Error: You are not allowed to play on the computer train";
            return false;
        }
        else if (userTrainChoice == 'm' && !this.mexicanTrainPlayable) {
        //    std::cout << "Error: You are not allowed to play on the Mexican Train";
            return false;
        }
        return true;
    }

    public boolean getTrainMarker() {
        return this.playerTrain.getMarker();
    }

    public int sumOfPips() {
        //total will hold the sum of the pips in the hand
        int total = 0;
        //for every tile in the players hand, add the first and second number to total.
        for (int i = 0; i < playerHand.getSize(); i++) {
            total += playerHand.getTile(i).getFirstNum();
            total += playerHand.getTile(i).getSecondNum();
        }
        Log.d("myTag", "Sum Of Pips = " + Integer.toString(total));
        return total;
    }

    public void findBestMove(Player humanPlayer, Player computerPlayer, Train mexicanTrain, Hand boneyard, ArrayList<Tile> bestTiles, ArrayList<String> trains){
        boolean turnFinished = false;
        //stores what tiles the computer will play. either 1 single tile, a double and a non doubl, or 2 doubles and a non double.

        ArrayList<Tile> tilesPlayed = new ArrayList<Tile>();
        ArrayList<Tile> mexicanPlayableTiles= new ArrayList<Tile>();
        ArrayList<Tile> humanPlayableTiles= new ArrayList<Tile>();
        ArrayList<Tile> computerPlayableTiles= new ArrayList<Tile>();

        //stores any double tiles the player can play
        ArrayList<Tile> mexicanDoubles= new ArrayList<Tile>();
        ArrayList<Tile> humanDoubles= new ArrayList<Tile>();
        ArrayList<Tile> computerDoubles= new ArrayList<Tile>();
        ArrayList<Tile> tmpVector = this.getHandArrayList();
        Hand tempPlayerHand = new Hand();
        for (int i = 0; i < tmpVector.size(); i++) {
            tempPlayerHand.addTile(tmpVector.get(i));
        }
        int doublesPlayed = 0;
        do{
            //clear all vectors of playable tiles.
            mexicanPlayableTiles.clear();
            humanPlayableTiles.clear();
            computerPlayableTiles.clear();
            mexicanDoubles.clear();
            humanDoubles.clear();
            computerDoubles.clear();
            //3a- if mexican train is playable, get all the playable tiles on that train and place them into mexicanPlayableTiles vector
            if (this.mexicanTrainPlayable) {
                mexicanPlayableTiles = getPlayableTiles(tempPlayerHand.getHandArrayList(), mexicanTrain.getTrainEndNumber());
            }
            //3b- if computer train is playable, get all the playable tiles on that train and place them into computerPlayableTiles vector

            if (this.computerTrainPlayable) {
                computerPlayableTiles = getPlayableTiles(tempPlayerHand.getHandArrayList(), computerPlayer.getTrainEndNumber());
            }
            //3c- if human train is playable, get all the playable tiles on that train and place them into humanPlayableTiles vector

            if (this.humanTrainPlayable) {
                humanPlayableTiles = getPlayableTiles(tempPlayerHand.getHandArrayList(), humanPlayer.getTrainEndNumber());
            }

            //3d- store all the doubles into their respective doubles vector
            for (int i = 0; i < mexicanPlayableTiles.size(); i++) {
                if (mexicanPlayableTiles.get(i).isDouble()) {
                    mexicanDoubles.add(mexicanPlayableTiles.get(i));
                }
            }

            for (int i = 0; i < humanPlayableTiles.size(); i++) {
                if (humanPlayableTiles.get(i).isDouble()) {
                    humanDoubles.add(humanPlayableTiles.get(i));
                }
            }

            for (int i = 0; i < computerPlayableTiles.size(); i++) {
                if (computerPlayableTiles.get(i).isDouble()) {
                    computerDoubles.add(computerPlayableTiles.get(i));
                }
            }

            //3e - If we can play on the mexican train, and the mexicanDoubles vector isnt empty, and we have played less then 2 doubles,
            //add the first double to bestTiles vector.remove the tile from out temporary hand, and restart from step 3.
            if (this.mexicanTrainPlayable & !mexicanDoubles.isEmpty() && doublesPlayed < 2) {
                tilesPlayed.add(mexicanDoubles.get(0));
                if (mexicanDoubles.get(0).getFirstNum() == mexicanTrain.getTrainEndNumber()) {
                    mexicanTrain.setTrainEndNumber(mexicanDoubles.get(0).getSecondNum());
                }
                else if (mexicanDoubles.get(0).getSecondNum() == mexicanTrain.getTrainEndNumber()) {
                    mexicanTrain.setTrainEndNumber(mexicanDoubles.get(0).getFirstNum());
                    mexicanDoubles.get(0).swapNumbers();
                }
                //remove the tile from the temp hand, and add the tile and train to the respective vectors
                tempPlayerHand.removeTile(mexicanDoubles.get(0).getFirstNum(), mexicanDoubles.get(0).getSecondNum());
                bestTiles.add(mexicanDoubles.get(0));
                trains.add("m");
                //increment doubles played.
                doublesPlayed++;
                continue;
            }
            //3f- If we can play on the human train, and the mexicanDoubles vector isnt empty, and we have played less then 2 doubles,
            // add the first double to bestTiles vector.remove the tile from out temporary hand, and restart from step 3.
            else if (humanTrainPlayable && !humanDoubles.isEmpty() && doublesPlayed < 2) {
                tempPlayerHand.removeTile(humanDoubles.get(0).getFirstNum(), humanDoubles.get(0).getSecondNum());
                bestTiles.add(humanDoubles.get(0));
                trains.add("h");
                doublesPlayed++;
                continue;
            }
            //3g - If we can play on the computer train, and the mexicanDoubles vector isnt empty, and we have played less then 2 doubles,
            //add the first double to bestTiles vector.remove the tile from out temporary hand, and restart from step 3.
            else if (computerTrainPlayable && !computerDoubles.isEmpty() && doublesPlayed < 2) {
                //remove the tile from the temp hand, and add the tile and train to the respective vectors
                tempPlayerHand.removeTile(computerDoubles.get(0).getFirstNum(), computerDoubles.get(0).getSecondNum());
                bestTiles.add(computerDoubles.get(0));
                trains.add("c");
                doublesPlayed++;
                continue;

            }

            //NOW PLAY NONDOUBLE TILES!!!!!
            //3h- Repeat steps 3e -3g, except this time for the vector that stores the non double tiles.
            //if we play a single tile, our turn is over so return.
            //push back the correct tile to the bestTiles vector
            //and push back the correct character to the trains vector.
            if (mexicanTrainPlayable && !mexicanPlayableTiles.isEmpty()) {
                bestTiles.add(mexicanPlayableTiles.get(0));
                tempPlayerHand.removeTile(mexicanPlayableTiles.get(0).getFirstNum(), mexicanPlayableTiles.get(0).getSecondNum());
                trains.add("m");
                return;
            }
            if (humanTrainPlayable && !humanPlayableTiles.isEmpty()) {
                bestTiles.add(humanPlayableTiles.get(0));
                trains.add("h");
                return;
            }
            if (computerTrainPlayable && !computerPlayableTiles.isEmpty()) {
                bestTiles.add(computerPlayableTiles.get(0));
                trains.add("c");
                return;
            }
            return;
        } while(turnFinished == false);
    }

    public ArrayList<Tile> getPlayableTiles(ArrayList<Tile> playerHand, int trainEndNumber) {
        ArrayList<Tile> playableTiles = new ArrayList<Tile>();
        for (int i = 0; i < playerHand.size(); i++) {
            //2)if the tile's first number is equal to the trains end number, add the tile to vector we return

            if (playerHand.get(i).getFirstNum() == trainEndNumber) {
                playableTiles.add(new Tile (playerHand.get(i).getFirstNum(), playerHand.get(i).getSecondNum() ) );
            }
            //3)else if the tile's second number is equal to the trains end number, add the tile to vector we return
            else if (playerHand.get(i).getSecondNum() == trainEndNumber) {
                playableTiles.add(new Tile (playerHand.get(i).getFirstNum(), playerHand.get(i).getSecondNum() ) );
            }
        }
        return playableTiles;
    }

    public String interpretBestMove(ArrayList<Tile> bestTiles, ArrayList<String> trains) {
        String explanation = new String();

        //if bestTiles is empty,meaning we have no move then just return
        if(bestTiles.isEmpty()) {
            return explanation;
        }
        //print out the doubles we played, and the reasoning we played it was because it is a double tile.
        for (int i = 0; i < bestTiles.size(); i++) {
            if (bestTiles.get(i).isDouble()) {
                explanation += bestTiles.get(i).tileAsString();
                explanation += " was played on the ";
                if (trains.get(i) == "m") {
                    explanation += "mexican";
                }
                else if (trains.get(i) == "c") {
                    explanation += "computer";
                }
                else {
                    explanation += "human";
                }
                explanation += " train because it was a valid double tile.";
            }
        }


        //need to make sure we print out the last tile that is not a double.
        if (!bestTiles.get(bestTiles.size()-1).isDouble()) {
            explanation += bestTiles.get(bestTiles.size()-1).tileAsString();
            explanation += " was played on the ";
            if (trains.get(trains.size()-1) == "m") {
                explanation += "mexican";
            }
            else if (trains.get(trains.size()-1) == "c") {
                explanation += "computer";
            }
            else {
                explanation += "human";
            }

            explanation += " train because it was the largest tile.\n\n";
        }
        return explanation;
    }

    protected void clearTrainMarker() {
        this.playerTrain.clearTrainMarker();
    }

    public boolean getOrphanDouble() {
        return this.playerTrain.getOrphanDouble();
    }

    public void setTrainEndNumber(int newEndNumber){
        this.playerTrain.setTrainEndNumber(newEndNumber);
    }

    public void setOrphanDouble() {
        playerTrain.setOrphanDouble(true);
    }

    public void setStringMoveExplanation(String s) {
        this.moveExplanation += s;
    }

    public void resetStringMoveExplanation() {
        this.moveExplanation = "";
    }

    public String getMoveExplanation() {
        return this.moveExplanation;
    }
}
