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
public class GameActivity extends Activity {
    private Game gameClass;
    private Button confirmRestartButton;
    private SharedPreferences gameHighScores;
    private SharedPreferences gameDefaults;
    private SharedPreferences gameStateStorage;
    public static final String GAMESCORES = "HighScoreFile";
    String nameP1;
    String nameP2;
    DictionaryConstructor dictConst;


/** Below two different on click listeners are created, one to confirm the input and one to move to
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
            inputField.setText("");
            TextView wordView = (TextView) findViewById(R.id.ghostWordTextview);

//            If the input is not a letter the user is given a warning
            if (!input.matches("[a-z]") || input.length() != 1) {
                popUpUnvalidInput();
            } else if (gameClass.checkRoundEnded(input)) {

//                The input is not valid, the round/game was lost.
                resolvePlayerLostRound(wordView);
            } else {

//                If the input is valid update the word.
                String word = String.valueOf(wordView.getText());
                word = word + input;
                wordView.setText(word);

//                Set player turn
                TextView activePlayerView = (TextView) findViewById(R.id.playerTurnView);
                activePlayerView.setText(gameClass.getActivePlayer() + " it's your turn!");
            }
        }
    };


    View.OnClickListener nextRound = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            /**Moves to the next round.
             * Sets new onclicklistner*/
            try {
                gameClass.nextRound();
            } catch (gameTerminationException e) {
                finish();
            } //demanded by androidStudio
            confirmRestartButton.setText("confirm input");
            confirmRestartButton.setOnClickListener(confirmInput);

//            Reset word, inputfield and set word announcer
            TextView wordView = (TextView) findViewById(R.id.ghostWordTextview);
            wordView.setText("");
            ((TextView) findViewById(R.id.currentWordAnouncer)).setText("The word is:");
            ((EditText) findViewById(R.id.playerLetterInputField)).setText("");

//            Set player turn
            TextView activePlayerView = (TextView) findViewById(R.id.playerTurnView);
            activePlayerView.setText(gameClass.getActivePlayer() + " it's your turn!");
        }
    };


    View.OnClickListener highScores = new View.OnClickListener() {
        /*Sets button to move to highscorescreen*/
        @Override
        public void onClick(View view) {
            /**Moves to the highscore screen.*/
//          If going to highscore the game should be recreated next time.
            SharedPreferences.Editor stateEdit = gameStateStorage.edit();
            stateEdit.putBoolean("destroyed", false);
            stateEdit.apply();

            Intent goHighscoreScreen = new Intent(getApplicationContext(),
                    HighscoreActivity.class);
            finish();
            startActivity(goHighscoreScreen);
        }
    };


    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**Creates a lot of global variables and loads shared preferences/*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        gameHighScores = getSharedPreferences(GAMESCORES, 0);
        gameDefaults = getSharedPreferences(StartupActivity.GAMEDEFAULTS, 0);
        gameStateStorage = getSharedPreferences("safeOnDestroy",0);

//        Check if game was destroyed or a new game must be started
        if (gameStateStorage.getBoolean("destroyed", false)) {
            recreateGame();
        } else {
            startNewGame();
        }
    }


    private void startNewGame() {
        /**Starts a new game*/
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
        gameClass = new Game(dictonary, nameP1, nameP2);

//        Set player turn and word announcer.
        ((TextView) findViewById(R.id.playerTurnView)).setText(gameClass.getActivePlayer() + " it's your turn");
        ((TextView) findViewById(R.id.currentWordAnouncer)).setText("The word is:");
    }


    public void recreateGame(){
        /**Recreates the game from savedInstances*/

//        Set player names
        nameP1 = gameStateStorage.getString("nameP1", "P1");
        ((TextView) findViewById(R.id.p1NameView)).setText(nameP1);

        nameP2 = gameStateStorage.getString("nameP2", "P2");
        ((TextView) findViewById(R.id.p2NameView)).setText(nameP2);

//        Get info old state
        String oldScoreP1 = gameStateStorage.getString("scoreP1", "");
        String oldScoreP2 = gameStateStorage.getString("scoreP2", "");
        String oldWord = gameStateStorage.getString("word", "");
        String oldActivePlayer = gameStateStorage.getString("activePlayer", "");


//        Set on click listener
        confirmRestartButton = (Button) this.findViewById(R.id.confirmRestart);
        if (gameStateStorage.getBoolean("nextRoundListener", true)){
            confirmRestartButton.setOnClickListener(nextRound);
            confirmRestartButton.setText("Next round");

        }else{
            confirmRestartButton.setOnClickListener(confirmInput);
            ((TextView) findViewById(R.id.currentWordAnouncer)).setText("The word is:");
        }

//        Create a dictonary and use it to recreate the game
        dictConst = new DictionaryConstructor(this);
        Dictionary dictonary = dictConst.createDict(gameDefaults.getInt("Language", 0));
        gameClass = new Game(dictonary, nameP1, nameP2, oldScoreP1, oldScoreP2, oldWord, oldActivePlayer);

//        Set word, score and player turn
        ((TextView) findViewById(R.id.playerTurnView)).setText(gameClass.getActivePlayer() + " it's your turn");
        ((TextView) findViewById(R.id.ghostWordTextview)).setText(gameClass.getWord());
        ((TextView) findViewById(R.id.p1ScoreView)).setText(gameClass.getScore(nameP1));
        ((TextView) findViewById(R.id.p2ScoreView)).setText(gameClass.getScore(nameP2));
        
//        Only restore same game state once!
        SharedPreferences.Editor stateEdit = gameStateStorage.edit();
        stateEdit.putBoolean("destroyed", false);
        stateEdit.apply();
    }


    @Override
    protected void onSaveInstanceState (Bundle savedInstanceState){
        /**Is called on destruction of the game. Saves everything so the game can be recreated.*/
        SharedPreferences.Editor stateEdit = gameStateStorage.edit();
        
//        So that is is known oncreate that the game was destroyed
        stateEdit.putBoolean("destroyed", true);

//        put information for recreation of the game state.
        stateEdit.putString("nameP1", nameP1);
        stateEdit.putString("nameP2", nameP2);
        stateEdit.putString("scoreP1", gameClass.getScore(nameP1));
        stateEdit.putString("scoreP2", gameClass.getScore(nameP2));
        stateEdit.putString("activePlayer", gameClass.getActivePlayer());
        stateEdit.putBoolean("nextRoundListener", gameClass.nextRoundListener());

//        Word can be word or a lost reason, for thet reason its easier to get it directly from
//        the TextView.
        stateEdit.putString("word", String.valueOf(((TextView) findViewById(R.id.ghostWordTextview)).getText()));

        stateEdit.apply();
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
            Intent settings = new Intent(this, SettingsActivity.class);
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
        Intent popUp = new Intent(getApplicationContext(), PopUpActivity.class);
        popUp.putExtra("errorMsg", "FOOL, \n you can only enter letters!!!");
        popUp.putExtra("button", "I solemnly swear never to do this again");
        startActivity(popUp);
    }


    public void resolvePlayerLostRound(TextView wordView){
        /**Sets personalized lose reason.
        * Checks if game if finished and sets button to either go to next round or to the highscores*/
        ((TextView) findViewById(R.id.currentWordAnouncer)).setText("");

        String lossText = gameClass.getLossText();
        wordView.setText(lossText);

//        Update scores for both players
        TextView p1ScoreView = (TextView) findViewById(R.id.p1ScoreView);
        p1ScoreView.setText(gameClass.getScore(nameP1));
        TextView p2ScoreView = (TextView) findViewById(R.id.p2ScoreView);
        p2ScoreView.setText(gameClass.getScore(nameP2));

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
        gameClass = new Game(dictonary, nameP1, nameP2);

//            Reset wordview en scoreviews and active player
        TextView wordView = (TextView) findViewById(R.id.ghostWordTextview);
        wordView.setText("");
        TextView scoreViewP1 = (TextView) findViewById(R.id.p1ScoreView);
        scoreViewP1.setText("");
        TextView scoreViewP2 = (TextView) findViewById(R.id.p2ScoreView);
        scoreViewP2.setText("");
        ((TextView) findViewById(R.id.playerTurnView)).setText(gameClass.getActivePlayer());

//            Reset the button
        confirmRestartButton = (Button) this.findViewById(R.id.confirmRestart);
        confirmRestartButton.setText("confirm input");
        confirmRestartButton.setOnClickListener(confirmInput);
    }


    private void safeScore(){
       /** Safes the scores and player names.
        * SCORE IS SAVED AS #PLAYERD;#WON
        * NAMES ARE SAVED AS NAME;NAME;NAME.....*/

//        Get who won and who lost the game.
        String winner = gameClass.winner();
        String loser = gameClass.loser();
        SharedPreferences.Editor scoreEdit = gameHighScores.edit();

//        Get the names of all the previous players.
        SharedPreferences.Editor namesEdit = gameDefaults.edit();
        String names = gameDefaults.getString("playerNames", "");


//        Try to update the score, if it fails create a new record.
        if(!updatePlayerScore(winner, 1)){
            scoreEdit.putString(winner, "1;1");
            names = names + ";" + winner;
        }

        if(!updatePlayerScore(loser, 0)){
            scoreEdit.putString(loser, "1;0");
            names = names + ";" + loser;
        }

//        Commit the name and score changes
        namesEdit.putString("playerNames", names);
        namesEdit.apply();
        scoreEdit.apply();
    }


    public boolean updatePlayerScore(String playerName, int result) {
        /**Tries to update the score for player with playerName.
         * Put 1 for winner (games played += 1 and games won +=1)
         * put 0 for loser (games played += 1 and games won +=0)
         * Returns true if update was possible false otherwise (no record yet)*/
        SharedPreferences.Editor scoreEdit = gameHighScores.edit();
        String scoreHistory = gameHighScores.getString(playerName, "");

//        If played before update scores.
        if(scoreHistory.length()>0){
            String[] newResult = new String[2];
            String[] prevResult = scoreHistory.split(";");
            newResult[0] = String.valueOf(Integer.parseInt(prevResult[0]) + 1);
            newResult[1] = String.valueOf(Integer.parseInt(prevResult[1]) + result);
            scoreEdit.putString(playerName, newResult[0] + ";" + newResult[1] );
            scoreEdit.apply();
            return true;

//            If never played before it is not possible to update the score so return false.
        }else{
            return false;
        }
    }


    public void backToMain(View view) {
//        Function for going back to the main menu
        Intent goingBack = new Intent();
        setResult(RESULT_OK, goingBack);
        finish();
    }
}





