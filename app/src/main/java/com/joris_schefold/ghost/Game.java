package com.joris_schefold.ghost;


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
    private int startingPlayer = (int) Math.round(Math.random());
    private int activePlayer;
    private Dictionary gameDictonary;
    private ArrayList<String> playerNames;


    public Game(Dictionary dict, String p1, String p2) {
        /**Initializes constants. dict is dictionary to use and p1 and p2 are the names of the players*/
        playerNames = new ArrayList<>(Arrays.asList(p1, p2));
        scoreP1 = "";
        scoreP2 = "";
        word = "";
        gameDictonary = dict;
        scoreArray = new String[]{"G", "H", "O", "S", "T"};
        activePlayer = startingPlayer;
    }


    public Game(Dictionary dict, String p1, String p2, String oldScoreP1, String oldScoreP2,
                String oldWord, String oldActivePlayer) {
        /**Initializes constants. dict is dictionary to use and p1 and p2 are the names of the players*/
        playerNames = new ArrayList<>(Arrays.asList(p1, p2));
        scoreP1 = oldScoreP1;
        scoreP2 = oldScoreP2;
        word = oldWord;
        gameDictonary = dict;

//        Filter the dictonary on the previous word
        dict.filter(word);
        scoreArray = new String[]{"G", "H", "O", "S", "T"};
        activePlayer = playerNames.indexOf(oldActivePlayer);
    }


    public String getActivePlayer(){
        return playerNames.get(activePlayer);
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
        } else {
            scoreP2 = scoreP2 + scoreArray[scoreP2.length()];
        }
    }


    public String getWord(){
        return word;
    }


    public String getScore(String player){
        /**Returns the score of player*/

//        Convert player name to index to find out if it is player 1 or player 2
        int i = playerNames.indexOf(player);
        if (i == 0){return scoreP1;}
        else {return scoreP2;}
    }


    boolean checkRoundEnded(String playerInput){
        /**Checks if the input (a letter) is valid. adds loss of it isn't and changes active player
        * if it is.
         * Returns true if round has ended (input NOT valid)
         * Returns false if input is valid.*/
        word = word + playerInput;

//        First check if a word is formed and filter after only if none has been formed.
//        This is because HashSet can see if a word is formed really fast.
        if (gameDictonary.formedWord(word) && (word.length() > 3)){
            addLoss(activePlayer);
            return true;
        }else{
            gameDictonary.filter(word);}

//        If the dictionary is empty it is impossible to form words.
        if (gameDictonary.countRemainingWords() == 0){
            addLoss(activePlayer);
            return true;

//        If the dictionary is not empty an no word has been formed the next player is.
        }else {
            activePlayer++;
            activePlayer = activePlayer % 2;
            return false;
        }
    }


    boolean nextRoundListener(){
        /**Returns true if round has ended
         * Returns false otherwise.*/

//        If a word has formed or no words can be formed the round has ended.
//        If the dictionary is not empty an no word has been formed the round goes on
        return (gameDictonary.formedWord(word) && (word.length() > 3)) ||
                gameDictonary.countRemainingWords() == 0;
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


    public void nextRound() throws gameTerminationException {
        /**Throws error if the round hasn't ended.
        * If the round has ended it resets all the values*/
//        Check if the game really has ended.
         if (!(gameDictonary.countRemainingWords() == 0 || (gameDictonary.formedWord(word) &&
                (word.length() > 3)))){throw new gameTerminationException(
                 "Program tried to terminate game round before it was finished.");}

//        Reset the word and dictionary.
        word = "";
        gameDictonary.reset();

//        Change starting player and active player.
        startingPlayer++;
        startingPlayer = startingPlayer %2;
        activePlayer = startingPlayer;
    }


    public String getLossText() {
        if (gameDictonary.formedWord(word) && (word.length() > 3)){
            return word + " is a word, better luck next time";

//        If the dictionary is empty it is impossible to form words.
        }else if(gameDictonary.countRemainingWords() == 0){
            return "You can't form a word that starts with " + word + " you lost!!!";

//        If  lost for no reason make funny remark.
        }else {
            return "You seem to have lost, maybe treat your programmers beter next time...";
        }
    }
}
