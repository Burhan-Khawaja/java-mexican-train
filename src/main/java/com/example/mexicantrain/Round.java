package com.example.mexicantrain;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class Round {
    //mexican train
    //boneyard

    private Hand boneyard = new Hand();
    private Human humanPlayer = new Human(this.activity);
    //private Computer computerPlayer = new Computer(this.activity);
    private Activity activity;

    Round(Activity activity) {
        this.activity = activity;
        //set activity through a setter b/c if we use constructor, then this.activity is not initialized,
        // and we set it to a null value in other classes.
        humanPlayer.setActivity(this.activity);
    }


    /**
     * create a double nine set of tiles, as well as shuffle the tiles and deal the tiles to the human and computer player.
     */
    public void dealTiles(){

        //track if we should add tiles to human players hand, computer players hand, or boneyard.
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
        Random rng = new Random();

        for (int i = 0; i < tiles.size(); i++) {
            int randomNum = rng.nextInt(tiles.size() - 1);
            Tile tmpTile = tiles.get(i);

            //boneyard.getTile(i) = boneyard.getTile(randomNum);
            tiles.set(i, tiles.get(randomNum));
            //boneyard[randomNum] = tmpTile;
            tiles.set(randomNum, tmpTile);
        }

        //deal tiles to human/computer/boneyard
        for(int i = 0; i < tiles.size(); i++) {
            if(i < 16) {
                humanPlayer.addTileToHand(tiles.get(i));
            }
            else if (i < 32) {
                //Add tiles to computer players hand.
                // computer.addTileToHand(new Tile(i, j));
            }
            else {
                boneyard.addTile(tiles.get(i));
            }
        }

    }

    /**
     * Display the human players hand to the screen.
     */
    public void displayHumanHand(){
        humanPlayer.displayHand();
    }

    /**
     * Displays the current state of the game, including human and player hands and trains
     * and top of boneyard
     */
    public void displayRoundState() {
        //BURBUR TESTING PURPOSES ADD TILES TO TRAIN.
        humanPlayer.addTileFront(new Tile(1,1));
        humanPlayer.addTileFront(new Tile(2,2));


        //get train section of UI

        //NEED TO GET THE LINEAR LAYOUT OF THE HORIZONTAL SCROLLVIEW, NOT THE ACTUAL SCROLLVIEW
        LinearLayout allTrains = (LinearLayout) this.activity.findViewById(R.id.allTrainsLayout);
        //create a new TextView that will store the train values.
        TextView humanTrainValues = new TextView(this.activity);
        humanTrainValues.setText(humanPlayer.trainAsString());

        allTrains.addView(humanTrainValues);

    }
}
