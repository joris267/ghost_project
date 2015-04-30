package com.joris_schefold.ghost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import android.content.SharedPreferences;




/**
 * Created by joris on 4/15/2015.
 */
public class gameScreen extends Activity {
    private Game game_class;
    private Button confirm_restart_button;
    private SharedPreferences gamePrefs;
    public static final String GAME_PREFS = "ArithmeticFile";


/* Below two different on click listeners are created, one to confirm the input and one to move to
* the next round*/
    View.OnClickListener confirm_input = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            Getting the user input and the word thus far
            EditText input_field = (EditText) findViewById(R.id.player_letter_input_field);
            String input = String.valueOf(input_field.getText()).toLowerCase();
            TextView word_view = (TextView) findViewById(R.id.ghost_word_textview);

//            If the input is not a letter the user is given a warning
            if(!input.matches("[a-z]") || input.length() != 1){
                Intent popUp = new Intent(getApplicationContext(), com.joris_schefold.ghost.popUp.class);
                popUp.putExtra("errorMsg", "FOOL, \n you can only enter letters!!!");
                popUp.putExtra("button", "I solemnly swear never to do this again");
                startActivity(popUp);
            }

//            If the input is a letter and it is valid the next player is.
            else if (game_class.valid_guess(input) == 0){
                String word = String.valueOf(word_view.getText());
                word = word + input;
                word_view.setText(word);

//                If the input is not valid the player gets a message explaining why he lost
//                and the confirm input button can be used to start a new round.
            }else if (game_class.valid_guess(input) == 1){
                String wordFragment = ((TextView) findViewById(R.id.ghost_word_textview)).getText().toString();
                word_view.setText("You can't form a word that starts with " + wordFragment + " you lost!!!");
                ((TextView)findViewById(R.id.current_word_anouncer)).setText("");
                confirm_restart_button.setText("Next round");
                confirm_restart_button.setOnClickListener(next_round);

            }else if (game_class.valid_guess(input) == 2){
                String word = ((TextView) findViewById(R.id.ghost_word_textview)).getText().toString() + input;
                word_view.setText(word + " is a word, better luck next time");
                ((TextView)findViewById(R.id.current_word_anouncer)).setText("");
                confirm_restart_button.setText("Next round");
                confirm_restart_button.setOnClickListener(next_round);
            }
        }
    };

//    Moves to the next round.
    View.OnClickListener next_round = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            game_class.next_round();
            confirm_restart_button.setText("confirm input");
            confirm_restart_button.setOnClickListener(confirm_input);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);
        gamePrefs = getSharedPreferences(GAME_PREFS, 0);

//        Gets the player names from the previous screen and sets it in the activity.
        Intent startup_screen_data = getIntent();
        String p1_name = startup_screen_data.getExtras().getString("name_p1");
        TextView p1_textview = (TextView) findViewById(R.id.p1_name_view);
        p1_textview.setText(p1_name);

        String p2_name = startup_screen_data.getExtras().getString("name_p2");
        TextView p2_textview = (TextView) findViewById(R.id.p2_name_view);
        p2_textview.setText(p2_name);

//        Set first on click listener
        confirm_restart_button = (Button) this.findViewById(R.id.confirm_restart);
        confirm_restart_button.setOnClickListener(confirm_input);

//        Creates a dictionary and start the game.
        Dictionary dictonary = new Dictionary(this, R.raw.test);
        game_class = new Game(this, dictonary, p1_name, p2_name);
    }

    private void safeScore(){
//        SCORE IS SAFED AS #PLAYERD|#WON

//        check to make sure that the game has ended.
        if (!(game_class.valid_guess("") == 0)){
            String winner = game_class.winner();
            String loser = game_class.loser();
            SharedPreferences.Editor scoreEdit = gamePrefs.edit();
            String scoresWinner = gamePrefs.getString("stats" + winner, "");
            String scoresLoser = gamePrefs.getString("stats" + loser, "");

            if(scoresWinner.length()>0){
                String[] newResultsWinner = new String[2];
                String[] prevResultsWinner = scoresWinner.split("|");
                newResultsWinner[0] = String.valueOf(Integer.parseInt(prevResultsWinner[0]) + 1);
                newResultsWinner[1] = String.valueOf(Integer.parseInt(prevResultsWinner[1]) + 1);
                scoreEdit.putString("stats" + winner, newResultsWinner[0] + "|" + newResultsWinner[1] );
            }
            else{
                scoreEdit.putString("stats" + winner, "0|0");
            }

            if(scoresLoser.length()>0){
                String[] newResultsLoser = new String[2];
                String[] prevResultsLoser = scoresLoser.split("|");
                newResultsLoser[0] = String.valueOf(Integer.parseInt(prevResultsLoser[0]) + 1);
                newResultsLoser[1] = String.valueOf(Integer.parseInt(prevResultsLoser[1]) + 0);
                scoreEdit.putString("stats" + loser, newResultsLoser[0] + "|" + newResultsLoser[1] );
            }
            else{
                scoreEdit.putString("stats" + loser, "0|0");
            }
            scoreEdit.commit();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_startup_screen, menu);
        return true;
    }

    public void BackToMain(View view) {
//        Function for going back to the main menu
        Intent goingBack = new Intent();
        setResult(RESULT_OK, goingBack);
        finish();
    }
}





