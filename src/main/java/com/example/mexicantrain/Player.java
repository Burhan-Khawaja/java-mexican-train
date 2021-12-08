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
    protected String moveExplanation = " ";

    /**
     * Return a players hand as a Hand object
     * @return Hand that has all the tiles in a players possession.
     */
    public Hand getHand() {
        return this.playerHand;
    }

    /**
     * Get a players hand as an arrayList, used for serialization
     * @return ArrayList<Tile> stores all the tiles in a players hand.
     */
    public ArrayList<Tile> getHandArrayList() {
        return this.playerHand.getHandArrayList();
    }


    /**
     * Add a tile to a players hand
     * @param tileToAdd Tile object that gets added to a players hand
     */
    public void addTileToHand(Tile tileToAdd){
        playerHand.addTile(tileToAdd);
    }

    /**
     * set a trains marker to true
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
    }

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
     * returns a train as a string so we an display it
     * @return String object that represents a train
     */
    public String trainAsString(){
        return playerTrain.trainAsString();
    }

    /**
     * Add a tile to the front of a players train
     * @param tileToAdd tile that the user wants to add
     */
    public void addTileFront(Tile tileToAdd) {
        playerTrain.addTileFront(tileToAdd);
    }

    /**
     * Add a tile to the back of the players train
     * @param tileToAdd tile the user wants to add
     */
    public void addTileBack(Tile tileToAdd) {
        playerTrain.addTileBack(tileToAdd);
    }

    /**
     * procedure that plays a turn. reimplemented in human/computer class
     * @param humanPlayer stores data for human player
     * @param computerPlayer stores data for computer player
     * @param mexicanTrain stores data for mexican train
     * @param Boneyard stores hand that is the boneyard
     * @param tileToPlay tile the user wants to play
     * @param trainToPlay train the user is attempting to play
     * @return integer value that represents either the sum of pips of the loser, or an error code or 0 if next players turn
     */
    abstract int play(Player humanPlayer, Player computerPlayer, Train mexicanTrain, Hand Boneyard, Tile tileToPlay, char trainToPlay);

    /**
     * check if a tile fits on a train
     * @param tileToCheck The tile that is trying to be added to a train
     * @param trainEndNumber The number on the train a tile must match to fit on the train
     * @return boolean true if tile fits on train, false if it does not
     */
    public boolean tileFitsOnTrain(Tile tileToCheck, int trainEndNumber) {
        if(tileToCheck.getFirstNum() == trainEndNumber) {
            return true;
        }
        else if (tileToCheck.getSecondNum()== trainEndNumber) {
            return true;
        }
        return false;
    }

    /**
     * Get the end number of a train, which is the number tiles must match to be added to the train
     * @return integer that is the hanging number on a train
     */
    public int getTrainEndNumber() {
        return this.playerTrain.getTrainEndNumber();
    }

    /**
     * Check if a player has a valid move
     * @param humanPlayer stores human player data and its train
     * @param computerPlayer stores computer player data and its train
     * @param mexicanTrain stores mexican train data
     * @return boolean, true if player has a move false otherwise
     */
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

    /**
     * Check if a player has a move on a certain train
     * @param trainEndNumber the number that represents the end number on a train a tile has to match to fit on it.
     * @return boolean true if the player has a move false otherwise.
     */
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

    /**
     * Function that deals with procedure of having no playable tiles.
     * @param humanPlayer stores human player data
     * @param computerPlayer stores computer player data
     * @param mexicanTrain stores mexican train data
     * @param boneyard stores boneyard data
     * @return boolean value, true if drawn tile is playable, false otherwise.
     */
    boolean noPlayableTiles(Player humanPlayer, Player computerPlayer, Train mexicanTrain, Hand boneyard) {
        if(boneyard.getSize() == 0) {
            //BURBUR add error message that boneyard is empty and a marker is placed on the train
            //this.settrain
            setStringMoveExplanation("Boneyard is empty, so a marker has been placed on the train.");
            this.setTrainMarker();
            return false;
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
                setStringMoveExplanation(" There is no valid move after taking the tile " + boneyardTile.tileAsString() + " ");
                setStringMoveExplanation("from the boneyard.\n A marker has been placed on your train");
                Log.d("myTag", "USER HAS NO VALID MOVE AFTER TAKING TILE FROM BONEYARD");
                return false;

            }
            else {
                //the tile selected is playable.
                setStringMoveExplanation("The tile " +boneyardTile.tileAsString() + " drawn from the boneyard is playable. ");
                return true;
            }
        }
    }

    /**
     * check for any existing orphan doubles, and if they exist set those trains only as playable.
     * @param humanPlayer stores human players information
     * @param computerPlayer stores computer players information
     * @param mexicanTrain stores tiles on mexican train
     * @return Boolean value that is true if there exists orphan doubles, false otherwise.
     * @algorithm 1- Check if there exists an orphan double on a train
     *            2- If it does, set all trains to unplayable. Then set each train with an orphan double as playable
     */
    boolean checkOrphanDoubles(Player humanPlayer, Player computerPlayer, Train mexicanTrain)
    {
        boolean orphanDouble = false;
        //1- check if there exists any orphan double on a train
        if (humanPlayer.playerTrain.getOrphanDouble() || computerPlayer.playerTrain.getOrphanDouble() || mexicanTrain.getOrphanDouble()) {
            //make all trains unplayable. later we will check which trains have orphan doubles and set them as the only trains playable
            this.humanTrainPlayable = false;
            this.computerTrainPlayable = false;
            this.mexicanTrainPlayable = false;
        }
        //check if each train has an orphan double. if it does, make that train playable, and set orphanDouble to true.
        if (humanPlayer.playerTrain.getOrphanDouble()) {
            setStringMoveExplanation("There is an orphan double on the human train");
            //BURBUR output that theres an orphandouble on humantrain
            this.humanTrainPlayable = true;
            orphanDouble = true;
        }
        if (computerPlayer.playerTrain.getOrphanDouble()) {
            setStringMoveExplanation("There is an orphan double on the computer train");
            this.computerTrainPlayable = true;
            orphanDouble = true;
        }
        if (mexicanTrain.getOrphanDouble()) {
            setStringMoveExplanation("There is an orphan double on the mexican train");
            this.mexicanTrainPlayable = true;
            orphanDouble = true;
        }
        return orphanDouble;
    }

    /**
     * check if a players choice of train is playable
     * @param userTrainChoice character that represents the train to play on
     * @return
     */
    public boolean checkUserTrainPlayable(char userTrainChoice) {
        if (userTrainChoice == 'h' && !this.humanTrainPlayable) {
            return false;
        }
        else if (userTrainChoice == 'c' && !this.computerTrainPlayable) {
            return false;
        }
        else if (userTrainChoice == 'm' && !this.mexicanTrainPlayable) {
            return false;
        }
        return true;
    }

    /**
     * Get a player trains marker
     * @return boolean value, true if train is marked false otherwise.
     */
    public boolean getTrainMarker() {
        return this.playerTrain.getMarker();
    }

    /**
     * Sum all the pips left in a players hand
     * @return integer value that is the sum of pips in hand
     */
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

    /**
     * Find the best move a player can make
     * @param humanPlayer - Human object that stores everything about the human
     * @param computerPlayer - Computer object that stores everything about the computer
     * @param mexicanTrain - Mexican train that stores all tiles on mexican train
     * @param boneyard - Hand object that stores the boneyard
     * @param bestTiles - ArrayList of tiles that stores what tiles should be played, empty when passed in
     * @param trains - ArrayList of Strings that represent the
     *                 corresponding trains to play the tiles on, also empty when passed in
     * @algorithm 0-create 3 vector of Tiles that store what tiles we can play on mexican, human, and computer train,
     *                 and 3 more vectors of Tiles that store the doubles we can play
     *              1-Create new hand, tempPlayerHand that we store players hand into so we can manipulate it
     *              2-Store player hand in tempPlayerHand
     *              3-while true
     *                 3a- if mexican train is playable, get all the playable tiles on that train and place them into mexicanPlayableTiles vector
     *                 3b- if computer train is playable, get all the playable tiles on that train and place them into computerPlayableTiles vector
     *                 3c- if human train is playable, get all the playable tiles on that train and place them into humanPlayableTiles vector
     *                 3d- store all the doubles into their respective doubles vector
     *                 3e- If we can play on the mexican train, and the mexicanDoubles vector isnt empty, and we have played less then 2 doubles,
     *                     add the first double to bestTiles vector. remove the tile from out temporary hand, and restart from step 3.
     *                 3f- If we can play on the human train, and the mexicanDoubles vector isnt empty, and we have played less then 2 doubles,
     *                     add the first double to bestTiles vector. remove the tile from out temporary hand, and restart from step 3.
     *                 3g- If we can play on the computer train, and the mexicanDoubles vector isnt empty, and we have played less then 2 doubles,
     *                     add the first double to bestTiles vector. remove the tile from out temporary hand, and restart from step 3.
     *                 3h- Repeat steps 3e -3g, except this time for the vector that stores the non double tiles.
     *                 3i- If we play a tile that is not a double, return from the function
     *                 3j- when we have played a non double tile, or have played 3 tiles return from the function.
     */
    public void findBestMove(Player humanPlayer, Player computerPlayer, Train mexicanTrain, Hand boneyard, ArrayList<Tile> bestTiles, ArrayList<String> trains){
        boolean turnFinished = false;
        //stores what tiles the computer will play. either 1 single tile, a double and a non doubl, or 2 doubles and a non double.

        ArrayList<Tile> tilesPlayed = new ArrayList<Tile>();
        //stores what tiles can be played on the mexican, human, or computer trains
        ArrayList<Tile> mexicanPlayableTiles= new ArrayList<Tile>();
        ArrayList<Tile> humanPlayableTiles= new ArrayList<Tile>();
        ArrayList<Tile> computerPlayableTiles= new ArrayList<Tile>();

        //stores any double tiles the player can play
        ArrayList<Tile> mexicanDoubles= new ArrayList<Tile>();
        ArrayList<Tile> humanDoubles= new ArrayList<Tile>();
        ArrayList<Tile> computerDoubles= new ArrayList<Tile>();
        ArrayList<Tile> tmpVector = this.getHandArrayList();
        //Create temporary hand that is a copy of player hand so we can freely manipulate it
        Hand tempPlayerHand = new Hand();
        for (int i = 0; i < tmpVector.size(); i++) {
            tempPlayerHand.addTile(tmpVector.get(i));
        }
        //track doubles played, b/c we can only play 2 doubles
        int doublesPlayed = 0;
        do {
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

    /**
     * Get the playable tiles a user can play on a train, based on the end number of that train
     * @param playerHand A players hand that contains all the tile
     * @param trainEndNumber The last number on a train a tile must match to fit
     * @return ArrayList<Tile> that is all playable tiles on a train
     */
    public ArrayList<Tile> getPlayableTiles(ArrayList<Tile> playerHand, int trainEndNumber) {
        /*
         Algorithm: 1)Loop through playerHand
            2)if the tile's first number is equal to the trains end number, add the tile to vector we return
            3)else if the tile's second number is equal to the trains end number, add the tile to vector we return
            4)once we have looped through the entire hand, return the vector of playable tiles.
         */
        ArrayList<Tile> playableTiles = new ArrayList<Tile>();
        //1)Loop through playerHand
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

    /**
     * Interpret the best move found by the computer
     * @param bestTiles arrayList of tiles that the computer thought would be best to play
     * @param trains ArrayList of the corresponding trains to play those tiles on
     * @return String that is the reasoning for why to play those tiles on those trains
     */
    public String interpretBestMove(ArrayList<Tile> bestTiles, ArrayList<String> trains) {
        String explanation = "";

        //if bestTiles is empty,meaning we have no move then just return
        if(bestTiles.isEmpty()) {
            return explanation;
        }
        //print out the doubles we played, and the reasoning we played it was because it is a double tile.
        for (int i = 0; i < bestTiles.size(); i++) {
            if (bestTiles.get(i).isDouble()) {
                explanation += bestTiles.get(i).tileAsString();
                explanation += " was played on the ";
                if (trains.get(i).equals("m")) {
                    explanation += "mexican";
                }
                else if (trains.get(i).equals("c")) {
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

    /**
     * Clear the marker on a players train
     */
    protected void clearTrainMarker() {
        this.playerTrain.clearTrainMarker();
    }

    /**
     * Get the orphan double of a train
     * @return Boolean value, true if there is an orphan double false otherwise
     */
    public boolean getOrphanDouble() {
        return this.playerTrain.getOrphanDouble();
    }

    /**
     * Set a player trains end number that a tile must match to fit on the train
     * @param newEndNumber integer value that is the new end number for a train
     */
    public void setTrainEndNumber(int newEndNumber){
        this.playerTrain.setTrainEndNumber(newEndNumber);
    }

    /**
     * set a players orphan double to a value
     * @param value Boolean value that is what we are setting the orphan double to
     */
    public void setOrphanDouble(boolean value) {
        playerTrain.setOrphanDouble(value);
    }

    /**
     * append to a players stringMoveExplanation a string passed in
     * @param s String that is passed in that we append to what the user did for their turn
     */
    public void setStringMoveExplanation(String s) {
        this.moveExplanation += s;
    }

    /**
     * Get a players move explanation, which is what they did in a turn
     * @return String that is a representation of the tiles played on the trains / what errors occurred
     */
    public String getMoveExplanation() {
        return this.moveExplanation;
    }

    /**
     * Clear a players move explanation, aka what they did for a turn
     */
    public void clearMoveExplanation(){
        this.moveExplanation = "";
    }

    /**
     * Return a players train as a string, used for serialization
     * @return String that represents a players train the way it should be for serialization
     */
    public String getTrainAsString(){
        return playerTrain.trainAsStringSerialization();
    }
}
