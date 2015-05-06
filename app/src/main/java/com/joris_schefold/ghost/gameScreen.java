package com.joris_schefold.ghost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.SharedPreferences;


/**
 * Created by joris on 4/15/2015.
 * Main game activity.
 */
public class gameScreen extends Activity {
    private Game game_class;
    private Button confirm_restart_button;
    private SharedPreferences gameHighScores;
    private SharedPreferences gameDefaults;
    public static final String GAMESCORES = "HighScoreFile";
    String p1_name;
    String p2_name;


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
            if (!input.matches("[a-z]") || input.length() != 1) {
                Intent popUp = new Intent(getApplicationContext(), PopUpScreeh.class);
                popUp.putExtra("errorMsg", "FOOL, \n you can only enter letters!!!");
                popUp.putExtra("button", "I solemnly swear never to do this again");
                startActivity(popUp);
            } else {
                int guessResult = game_class.valid_guess(input);

                //                If the input is valid update the word.
                if (guessResult == 0) {
                    String word = String.valueOf(word_view.getText());
                    word = word + input;
                    word_view.setText(word);

                    //                    The input is not valid, the round/game was lost.
                } else {
                    ((TextView) findViewById(R.id.current_word_anouncer)).setText("");

                    //                    Different loss reasons.
                    if (guessResult == 1) {
                        String wordFragment = ((TextView) findViewById(R.id.ghost_word_textview)).getText().toString() + input;
                        word_view.setText("You can't form a word that starts with " + wordFragment + " you lost!!!");
                    } else if (guessResult == 2) {
                        String word = ((TextView) findViewById(R.id.ghost_word_textview)).getText().toString() + input;
                        word_view.setText(word + " is a word, better luck next time");
                    }

                    if (game_class.checkFinished()) {
                        safeScore();
                        Intent goHighscoreScreen = new Intent(getApplicationContext(), com.joris_schefold.ghost.HighscoreScreen.class);
                        startActivity(goHighscoreScreen);
                    } else {
                        confirm_restart_button.setText("Next round");
                        confirm_restart_button.setOnClickListener(next_round);
                    }
                }
            }
        }
    };


    //    Moves to the next round.
    View.OnClickListener next_round = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            game_class.next_round();
            confirm_restart_button.setText("confirm input");
            confirm_restart_button.setOnClickListener(confirm_input);
        }
    };


    @Override
    public void onBackPressed() {
//        Disabled
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameScreen);
        gameHighScores = getSharedPreferences(GAMESCORES, 0);
        gameDefaults = getSharedPreferences(StartupScreen.GAMEDEFAULTS, 0);

//        Gets the player names from the previous screen and sets it in the activity.
        Intent startup_screen_data = getIntent();
        p1_name = startup_screen_data.getExtras().getString("name_p1");
        TextView p1_textview = (TextView) findViewById(R.id.p1_name_view);
        p1_textview.setText(p1_name);

        p2_name = startup_screen_data.getExtras().getString("name_p2");
        TextView p2_textview = (TextView) findViewById(R.id.p2_name_view);
        p2_textview.setText(p2_name);

//        Set first on click listener
        confirm_restart_button = (Button) this.findViewById(R.id.confirm_restart);
        confirm_restart_button.setOnClickListener(confirm_input);

//        Creates a dictionary and start the game.
        Dictionary dictonary = new Dictionary(this, gameDefaults.getInt("Language", 0));
        game_class = new Game(this, dictonary, p1_name, p2_name);
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data){

//      reste names
        p1_name = data.getExtras().getString("nameP1");
        p2_name = data.getExtras().getString("nameP2");


        TextView textNameP1 = (TextView) findViewById(R.id.p1_name_view);
        textNameP1.setText(p1_name);
        TextView textNameP2 = (TextView) findViewById(R.id.p2_name_view);
        textNameP2.setText(p2_name);

        if(data.getExtras().getBoolean("restart")) {
            Dictionary dictonary = new Dictionary(this, gameDefaults.getInt("Language", 0));
            game_class = new Game(this, dictonary, p1_name, p2_name);

//            Reset wordview en scoreviews
            TextView wordView = (TextView) findViewById(R.id.ghost_word_textview);
            wordView.setText("");
            TextView scoreViewP1 = (TextView) findViewById(R.id.p1_score_view);
            scoreViewP1.setText("");
            TextView scoreViewP2 = (TextView) findViewById(R.id.p2_score_view);
            scoreViewP2.setText("");

//            Reset the button
            confirm_restart_button = (Button) this.findViewById(R.id.confirm_restart);
            confirm_restart_button.setText("confirm input");
            confirm_restart_button.setOnClickListener(confirm_input);
        }
    }


    private void safeScore() throws AssertionError {
//        SCORE IS SAVED AS #PLAYERD;#WON
//        NAMES ARE SAFED AS NAME;NAME;NAME.....

//        Get previous scores.
        String winner = game_class.winner();
        String loser = game_class.loser();
        SharedPreferences.Editor scoreEdit = gameHighScores.edit();
        String scoresWinner = gameHighScores.getString(winner, "");
        String scoresLoser = gameHighScores.getString(loser, "");

//        Get previous names.
        SharedPreferences.Editor namesEdit = gameDefaults.edit();
        String names = gameDefaults.getString("playerNames", "");

        if(scoresWinner.length()>0){
//            If played before update scores.
            String[] newResultsWinner = new String[2];
            String[] prevResultsWinner = scoresWinner.split(";");
            newResultsWinner[0] = String.valueOf(Integer.parseInt(prevResultsWinner[0]) + 1);
            newResultsWinner[1] = String.valueOf(Integer.parseInt(prevResultsWinner[1]) + 1);
            scoreEdit.putString(winner, newResultsWinner[0] + ";" + newResultsWinner[1] );
        }else{
//            If not played before create new player and add score.
            names = names + ";" + winner;
            scoreEdit.putString(winner, "1;1");
        }

        if(scoresLoser.length()>0){
            String[] newResultsLoser = new String[2];
            String[] prevResultsLoser = scoresLoser.split(";");
            newResultsLoser[0] = String.valueOf(Integer.parseInt(prevResultsLoser[0]) + 1);
            newResultsLoser[1] = String.valueOf(Integer.parseInt(prevResultsLoser[1]));
            scoreEdit.putString(loser, newResultsLoser[0] + ";" + newResultsLoser[1] );
        }else{
            scoreEdit.putString(loser, "1;0");
            names = names + ";" + loser;
        }
        namesEdit.putString("playerNames", names);
        if (!namesEdit.commit()) throw new AssertionError();
        scoreEdit.apply();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.actionSettingsGameScreen) {
            Intent settings = new Intent(this, SettingsScreen.class);
            settings.putExtra("p1", p1_name);
            settings.putExtra("p2", p2_name);
            startActivityForResult(settings, 0);
        }

        return super.onOptionsItemSelected(item);
    }


    public void BackToMain(View view) {
//        Function for going back to the main menu
        Intent goingBack = new Intent();
        setResult(RESULT_OK, goingBack);
        finish();
    }
}





