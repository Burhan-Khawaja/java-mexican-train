package com.example.mexicantrain;

import android.app.Activity;

public class Game {
    private int roundNumber;
    private int humanScore;
    private int computerScore;
    private Round round ;
    private Activity activity;

    Game(Activity activity) {
        this.roundNumber = 0;
        //REMOVE THESE 2 VARS AND ISNTEAD PUT THEM IN PLAYER CLASS.
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
    public Hand getHumanHand(){
        return round.getHumanHand();
    }

    public int playTile(Tile userTile, char h) {
        return round.playTile(userTile, h);
    }
}
