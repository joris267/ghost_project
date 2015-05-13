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
    private Game gameClass;
    private Button confirmRestartButton;
    private SharedPreferences gameHighScores;
    private SharedPreferences gameDefaults;
    public static final String GAMESCORES = "HighScoreFile";
    String nameP1;
    String nameP2;
    DictionaryConstructor dictConst;


/* Below two different on click listeners are created, one to confirm the input and one to move to
* the next round*/

     View.OnClickListener confirmInput = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        /**This onClickListener checks the input and acts on it.
         * If it is invalid it wil raise a error and refuse to move to the next round.
         * If it is valid it wil update the dictionary and check if the round is lost.*/

//      Getting the user input and the word thus far
        EditText inputField = (EditText) findViewById(R.id.playerLetterInputField);
        String input = String.valueOf(inputField.getText()).toLowerCase();
        TextView wordView = (TextView) findViewById(R.id.ghostWordTextview);

//      If the input is not a letter the user is given a warning
        if (!input.matches("[a-z]") || input.length() != 1) {
            popUpUnvalidInput();
        } else {
            int guessResult = gameClass.validGuess(input);

//      If the input is valid update the word.
            if (guessResult == 0) {
                String word = String.valueOf(wordView.getText());
                word = word + input;
                wordView.setText(word);

//      The input is not valid, the round/game was lost.
            } else {
                resolvePlayerLost(guessResult, wordView, input);
            }
        }
        }
    };


    View.OnClickListener nextRound = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            /**Moves to the next round.
            * Sets new onclicklistner*/
            gameClass.nextRound();
            confirmRestartButton.setText("confirm input");
            confirmRestartButton.setOnClickListener(confirmInput);
        }
    };

    View.OnClickListener highScores = new View.OnClickListener() {
        /*Sets button to move to highscorescreen*/
        @Override
        public void onClick(View view) {
            /**Moves to the highscore screen.*/
            Intent goHighscoreScreen = new Intent(getApplicationContext(),
                    com.joris_schefold.ghost.HighscoreScreen.class);
            startActivity(goHighscoreScreen);
        }
    };


    @Override
    public void onBackPressed() {
//        Disabled
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**Creates a lot of global variables and loads shared preferences/*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);
        gameHighScores = getSharedPreferences(GAMESCORES, 0);
        gameDefaults = getSharedPreferences(StartupScreen.GAMEDEFAULTS, 0);

//        Gets the player names from the previous screen and sets it in the activity.
        Intent startupScreenData = getIntent();
        nameP1 = startupScreenData.getExtras().getString("nameP1");
        TextView textviewP1 = (TextView) findViewById(R.id.p1NameView);
        textviewP1.setText(nameP1);

        nameP2 = startupScreenData.getExtras().getString("nameP2");
        TextView textviewP2 = (TextView) findViewById(R.id.p2NameView);
        textviewP2.setText(nameP2);

//        Set first on click listener
        confirmRestartButton = (Button) this.findViewById(R.id.confirmRestart);
        confirmRestartButton.setOnClickListener(confirmInput);

//        Creates a dictionary and start the game.
        dictConst = new DictionaryConstructor(this);
        Dictionary dictonary = dictConst.createDict(gameDefaults.getInt("Language", 0));
        gameClass = new Game(this, dictonary, nameP1, nameP2);
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

//        If settings is pressed go to settings
        if (id == R.id.actionSettingsGameScreen) {
            Intent settings = new Intent(this, SettingsScreen.class);
            settings.putExtra("p1", nameP1);
            settings.putExtra("p2", nameP2);
            startActivityForResult(settings, 0);

//            If game restart is pressed restart the game
        }else if(id == R.id.actionRestartGame){
            restartGame();
        }

        return super.onOptionsItemSelected(item);
    }


    private void popUpUnvalidInput(){
        /**Creates a popup if the user input was not a letter.*/
        Intent popUp = new Intent(getApplicationContext(), PopUpScreen.class);
        popUp.putExtra("errorMsg", "FOOL, \n you can only enter letters!!!");
        popUp.putExtra("button", "I solemnly swear never to do this again");
        startActivity(popUp);
    }


    public void resolvePlayerLost(int guessResult, TextView wordView, String input){
        /**Sets personalized lose reason.
        * Checks if game if finished and sets button to either go to next round or to the highscores*/
        ((TextView) findViewById(R.id.currentWordAnouncer)).setText("");

//        Different loss reasons.
        if (guessResult == 1) {
            String wordFragment = ((TextView) findViewById(R.id.ghostWordTextview)).getText().toString() + input;
            wordView.setText("You can't form a word that starts with " + wordFragment + " you lost!!!");
        } else if (guessResult == 2) {
            String word = ((TextView) findViewById(R.id.ghostWordTextview)).getText().toString() + input;
            wordView.setText(word + " is a word, better luck next time");
        }

//        If finished go to highscores, Else to the next round.
        if (gameClass.checkFinished()) {
            safeScore();
            confirmRestartButton.setText("Go to Highscores");
            confirmRestartButton.setOnClickListener(highScores);
        } else {
            confirmRestartButton.setText("Next round");
            confirmRestartButton.setOnClickListener(nextRound);
        }
    }


    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        /**Only used when coming back from settings menu
        * Updates names and if needed restarts the game.*/

//      Reset names
        nameP1 = data.getExtras().getString("nameP1");
        nameP2 = data.getExtras().getString("nameP2");
        TextView textNameP1 = (TextView) findViewById(R.id.p1NameView);
        textNameP1.setText(nameP1);
        TextView textNameP2 = (TextView) findViewById(R.id.p2NameView);
        textNameP2.setText(nameP2);

//        If something vital was changed the game will be restart.
        if(data.getExtras().getBoolean("restart")) {
            restartGame();
        }
    }


    private void restartGame(){
        /**Completely restarts the game*/

//        Create new dictionary and game class.
        Dictionary dictonary = dictConst.createDict(gameDefaults.getInt("Language", 0));
        gameClass = new Game(this, dictonary, nameP1, nameP2);

//            Reset wordview en scoreviews
        TextView wordView = (TextView) findViewById(R.id.ghostWordTextview);
        wordView.setText("");
        TextView scoreViewP1 = (TextView) findViewById(R.id.p1ScoreView);
        scoreViewP1.setText("");
        TextView scoreViewP2 = (TextView) findViewById(R.id.p2ScoreView);
        scoreViewP2.setText("");

//            Reset the button
        confirmRestartButton = (Button) this.findViewById(R.id.confirmRestart);
        confirmRestartButton.setText("confirm input");
        confirmRestartButton.setOnClickListener(confirmInput);
    }


    private void safeScore(){
       /** Safes the scores and player names.
        * SCORE IS SAVED AS #PLAYERD;#WON
        * NAMES ARE SAVED AS NAME;NAME;NAME.....*/

//        Get previous scores.
        String winner = gameClass.winner();
        String loser = gameClass.loser();
        SharedPreferences.Editor scoreEdit = gameHighScores.edit();
        String scoresWinner = gameHighScores.getString(winner, "");
        String scoresLoser = gameHighScores.getString(loser, "");

//        Get previous names.
        SharedPreferences.Editor namesEdit = gameDefaults.edit();
        String names = gameDefaults.getString("playerNames", "");

//        If played before update scores.
        if(scoresWinner.length()>0){
            String[] newResultsWinner = new String[2];
            String[] prevResultsWinner = scoresWinner.split(";");
            newResultsWinner[0] = String.valueOf(Integer.parseInt(prevResultsWinner[0]) + 1);
            newResultsWinner[1] = String.valueOf(Integer.parseInt(prevResultsWinner[1]) + 1);
            scoreEdit.putString(winner, newResultsWinner[0] + ";" + newResultsWinner[1] );

//        If not played before create new player and add score.
        }else{
            names = names + ";" + winner;
            scoreEdit.putString(winner, "1;1");
        }

//        If played before update scores.
        if(scoresLoser.length()>0){
            String[] newResultsLoser = new String[2];
            String[] prevResultsLoser = scoresLoser.split(";");
            newResultsLoser[0] = String.valueOf(Integer.parseInt(prevResultsLoser[0]) + 1);
            newResultsLoser[1] = String.valueOf(Integer.parseInt(prevResultsLoser[1]));
            scoreEdit.putString(loser, newResultsLoser[0] + ";" + newResultsLoser[1] );

//        If not played before create new player and add score.
        }else{
            scoreEdit.putString(loser, "1;0");
            names = names + ";" + loser;
        }

//        Commit the name and score changes
        namesEdit.putString("playerNames", names);
        namesEdit.apply();
        scoreEdit.apply();
    }


    public void BackToMain(View view) {
//        Function for going back to the main menu
        Intent goingBack = new Intent();
        setResult(RESULT_OK, goingBack);
        finish();
    }
}





