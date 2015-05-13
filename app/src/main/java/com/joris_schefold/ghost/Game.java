package com.joris_schefold.ghost;

import android.app.Activity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by joris on 4/16/2015.
 * Game class, keeps track of score, word, dictionary and active players
 */
public class Game {
    private String word;
    private String scoreP1;
    private String scoreP2;
    private String scoreArray[];
    private Activity gameActivity;
    private int startingPlayer = (int) Math.round(Math.random());
    private int activePlayer = startingPlayer;
    private Dictionary gameDictonary;
    private ArrayList<String> playerNames;
    TextView playerTurnTiew;

    public Game(Activity game,Dictionary dict, String p1, String p2) {
        /**Initializes constants. dict is dictionary to use and p1 and p2 are the names of the players*/
        gameActivity = game;
        playerNames = new ArrayList<>(Arrays.asList(p1, p2));
        scoreP1 = "";
        scoreP2 = "";
        word = "";
        gameDictonary = dict;
//        scoreArray = new String[]{"G", "H", "O", "S", "T"};
        scoreArray = new String[]{"G", "H"};
        playerTurnTiew = (TextView) gameActivity.findViewById(R.id.playerTurnView);
        playerTurnTiew.setText(playerNames.get(activePlayer));
    }


    public boolean checkFinished() {
        /**Return true if the game has finished*/
        return scoreP1.length() == scoreArray.length || scoreP2.length() == scoreArray.length;
    }


    private void addLoss(int i){
        /**Ads a loss to the players score.
        * i is the players number, 0 for player 1 and 1 for player 2
        * Returns True if the game is finished (one of the players has GHOST)*/
        if (i == 0) {
            scoreP1 = scoreP1 + scoreArray[scoreP1.length()];
            TextView p1ScoreView = (TextView) gameActivity.findViewById(R.id.p1ScoreView);
            p1ScoreView.setText(scoreP1);
        } else {
            scoreP2 = scoreP2 + scoreArray[scoreP2.length()];
            TextView p2ScoreView = (TextView) gameActivity.findViewById(R.id.p2ScoreView);
            p2ScoreView.setText(scoreP2);
        }
    }


    String formedWord(){
        /*Not used atm*/
        return gameDictonary.result();
    }


    int validGuess(String playerInput){
        /**Checks if the input (a letter) is valid. adds loss of it isn't and changes active player
        * if it is.
        * returns 0 if valid guess
        * returns 1 if no words can be formed
        * returns 2 if a word of more then 3 char is formed*/
        word = word + playerInput;

//        First check if a word is formed and filter after only if none has been formed.
//        This is because HashSet can see if a word is formed really fast.
        if (gameDictonary.formedWord(word) && (word.length() > 3)){
            addLoss(activePlayer);
            return 2;
        }else{
            gameDictonary.filter(word);}

//        If the dictionary is empty it is impossible to form words.
        if (gameDictonary.countRemainingWords() == 0){
            addLoss(activePlayer);
            return 1;

//        If the dictionary is not empty an no word has been formed the next player is.
        }else {
            activePlayer++;
            activePlayer = activePlayer % 2;
            playerTurnTiew.setText(playerNames.get(activePlayer));
            return 0;
        }
    }


    public String winner(){
        /**Returns the name of the player who won.*/
//        Winning player is not the active player
        int winningPlayer = (activePlayer + 1)%2;
        return playerNames.get(winningPlayer);
    }


    public String loser(){
        /**Returns the name of the player who lost.*/
        return playerNames.get(activePlayer);
    }


    public void nextRound() throws Error {
        /**Throws error if the round hasn't ended.
        * If the round has ended it resets all the values and sets the onClickListener for the next
         * round.*/
//        Check if the game really has ended.
         if (!(gameDictonary.countRemainingWords() == 0 || (gameDictonary.formedWord(word) &&
                (word.length() > 3)))){throw new Error();}

//        Reset the word and dictionary.
        word = "";
        TextView wordView = (TextView) gameActivity.findViewById(R.id.ghostWordTextview);
        wordView.setText("");
        gameDictonary.reset();

//        Change starting player and active player.
        startingPlayer++;
        startingPlayer = startingPlayer %2;
        activePlayer = startingPlayer;
        TextView activePlayerView = (TextView) gameActivity.findViewById(R.id.playerTurnView);
        activePlayerView.setText(playerNames.get(activePlayer));
    }
}
