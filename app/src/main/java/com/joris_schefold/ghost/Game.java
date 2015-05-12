package com.joris_schefold.ghost;

import android.app.Activity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by joris on 4/16/2015.
 */
public class Game {
    private String word;
    private String p1_score;
    private String p2_score;
    private String score_array[];
    private Activity game_activity;
    private int starting_player = (int) Math.round(Math.random());
    private int active_player = starting_player;
    private Dictionary game_dictonary;
    private ArrayList<String> player_names;
    TextView player_turn_view;

    public Game(Activity game,Dictionary dict, String p1, String p2) {
        /*Initializes constants. dict is dictionary to use and p1 and p2 are the names of the players*/
        game_activity = game;
        player_names = new ArrayList<>(Arrays.asList(p1, p2));
        p1_score = "";
        p2_score = "";
        word = "";
        game_dictonary = dict;
//        score_array = new String[]{"G", "H", "O", "S", "T"};
        score_array = new String[]{"G", "H"};
        player_turn_view = (TextView) game_activity.findViewById(R.id.player_turn_view);
        player_turn_view.setText(player_names.get(active_player));
    }


    public boolean checkFinished() {
        /*Return true if the game has finished*/
        return p1_score.length() == score_array.length || p2_score.length() == score_array.length;
    }

    private void addLoss(int i){
        /*Ads a loss to the players score.
        * i is the players number, 0 for player 1 and 1 for player 2
        * Returns True if the game is finished (one of the players has GHOST)*/
        if (i == 0) {
            p1_score = p1_score + score_array[p1_score.length()];
            TextView p1_score_view = (TextView) game_activity.findViewById(R.id.p1_score_view);
            p1_score_view.setText(p1_score);
        } else {
            p2_score = p2_score + score_array[p2_score.length()];
            TextView p2_score_view = (TextView) game_activity.findViewById(R.id.p2_score_view);
            p2_score_view.setText(p2_score);
        }
    }


    String formed_word(){
        /*Not used atm*/
        return game_dictonary.result();
    }


    int valid_guess(String player_input){
        /*Checks if the input (a letter) is valid. adds loss of it isn't and changes active player
        * if it is.
        * returns 0 if valid guess
        * returns 1 if no words can be formed
        * returns 2 if a word of more then 3 char is formed*/
        word = word + player_input;

//        First check if a word is formed and filter after only if none has been formed.
//        This is because HashSet can see if a word is formed really fast.
        if (game_dictonary.formed_word(word) && (word.length() > 3)){
            addLoss(active_player);
            return 2;
        }else{game_dictonary.filter(word);}

//        If the dictionary is empty.
        if (game_dictonary.count_remaining_words() == 0){
            addLoss(active_player);
            return 1;
        }else {
//            If the diconary is not empty an no word has been formed the next player is.
            active_player++;
            active_player = active_player % 2;
            player_turn_view.setText(player_names.get(active_player));
            return 0;
        }
    }


    String winner(){
        /*Returns the name of the player who won.*/
//        Winning player is not the active player
        int winning_player = (active_player + 1)%2;
        return player_names.get(winning_player);
    }

    String loser(){
        /*Returns the name of the player who lost.*/
        return player_names.get(active_player);
    }


    void next_round() throws Error {
        /*Throws error if there are more then one words remaining in the dictionary and no word has
        * been formed.
        * if not resets values*/
//        Check if the game really has ended.
         if (!(game_dictonary.count_remaining_words() == 0 || (game_dictonary.formed_word(word) &&
                (word.length() > 3)))){throw new Error();}

//        Reset word
        word = "";
        TextView word_view = (TextView) game_activity.findViewById(R.id.ghost_word_textview);
        word_view.setText("");

//        Change starting player and active player
        starting_player ++;
        starting_player = starting_player%2;
        active_player = starting_player;
        TextView active_player_view = (TextView) game_activity.findViewById(R.id.player_turn_view);
        active_player_view.setText(player_names.get(active_player));

        game_dictonary.reset();
    }

}
