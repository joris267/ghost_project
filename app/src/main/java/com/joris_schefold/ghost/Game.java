package com.joris_schefold.ghost;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by joris on 4/16/2015.
 */
public class Game {
    private String name_p1;
    private String name_p2;
    private String word;
    private String p1_score;
    private String p2_score;
    private String score_array[];
    private Activity game_activity;
    private int starting_player = Math.round(Math.round(Math.random()));
    private int active_player = starting_player;
    private Dictionary game_dictonary;
    private ArrayList<String> player_names;
    TextView player_turn_view;

    public Game(Activity game,Dictionary dict, String p1, String p2) {
        /*Initializes constants. dict is dictionary to use and p1 and p2 are the names of the players*/
        game_activity = game;
        name_p1 = p1;
        name_p2 = p2;
        player_names = new ArrayList<>(Arrays.asList(p1, p2));
        p1_score = "";
        p2_score = "";
        word = "";
        game_dictonary = dict;
        score_array = new String[]{"G", "H", "O", "S", "T"};
        player_turn_view = (TextView) game_activity.findViewById(R.id.player_turn_view);
        player_turn_view.setText(player_names.get(active_player));
    }


    private void add_loss(int i){
        /*Ads a loss to the players score.
        * i is the players number, 0 for player 1 and 1 for player 2*/
        if (i == 0){
            p1_score = p1_score + score_array[p1_score.length()];
            TextView p1_score_view = (TextView) game_activity.findViewById(R.id.p1_score_view);
            p1_score_view.setText(p1_score);
        }
        else {
            p2_score = p2_score + score_array[p2_score.length()];
            TextView p2_score_view = (TextView) game_activity.findViewById(R.id.p2_score_view);
            p2_score_view.setText(p2_score);
        }
    }


    boolean valid_guess(String player_input){
        /*Checks if the input (a letter) is valid. adds loss of it isn't and changes active player
        * if it is.*/
        word = word + player_input;
        System.out.println(word +" " + player_input);
        game_dictonary.filter(word);
        if ((game_dictonary.count_remaining_words() == 0) || (game_dictonary.formed_word(word) && (word.length() > 3))) { // the player either formed a word or no word can be formed
            add_loss(active_player);
            return false;
        }else {
            active_player++;
            active_player = active_player % 2;
            player_turn_view.setText(player_names.get(active_player));
            return true;
        }
    }


    String winner(){
        /*Returns the name of the player who won.*/
        return player_names.get(active_player);
    }


    void next_round() throws Error {
        /*Throws error if there are more then one words remaining in the dictionary!
        * if not resets values*/
        if (game_dictonary.count_remaining_words()>1){throw new Error();}
        word = "";
        TextView word_view = (TextView) game_activity.findViewById(R.id.ghost_word_textview);
        word_view.setText("");

        starting_player ++;
        starting_player = starting_player%2;
        active_player = starting_player;
        TextView active_player_view = (TextView) game_activity.findViewById(R.id.player_turn_view);
        active_player_view.setText(player_names.get(active_player));

        game_dictonary.reset();
    }

}
