package com.example.mexicantrain;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

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
}
