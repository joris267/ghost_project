package com.joris_schefold.ghost;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**Activity that is loaded on startup. Here players enter their name and start the game.*/

public class StartupScreen extends Activity implements AdapterView.OnItemSelectedListener {
    public static final String GAMEDEFAULTS = "DefaultsFile";
    SharedPreferences gameDefaults;
    private boolean userIsInteracting;


    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**Load shared preferences and init spinners*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_screen);
        gameDefaults = getSharedPreferences(StartupScreen.GAMEDEFAULTS, 0);
        initNameSpinner();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_startup_screen, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.settingsStartupScreen) {

//            Get information player 1
            EditText inputfieldP1 = (EditText) findViewById(R.id.usernameInputP1Startupscreen);
            String usernameP1 = String.valueOf(inputfieldP1.getText());

//            Get information player 2
            EditText inputfieldP2 = (EditText) findViewById(R.id.usernameInputP2Startupscreen);
            String usernameP2 = String.valueOf(inputfieldP2.getText());

//            Go to the settings screen and pass on the player names.
            Intent settings = new Intent(this, SettingsScreen.class);
            settings.putExtra("p1", usernameP1);
            settings.putExtra("p2", usernameP2);
            startActivityForResult(settings,0);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        /**Only used when coming back from settings menu
         * Updates names and if needed restarts the spinners.*/

//        If needed reload Spinners
        if(data.getExtras().getBoolean("restart")) {initNameSpinner();}

//        reset name player 1
        String nameP1 = data.getExtras().getString("nameP1");
        TextView textNameP1 = (TextView) findViewById(R.id.usernameInputP1Startupscreen);
        textNameP1.setText(nameP1);

//        reset name player 2
        String nameP2 = data.getExtras().getString("nameP2");
        TextView textNameP2 = (TextView) findViewById(R.id.usernameInputP2Startupscreen);
        textNameP2.setText(nameP2);
    }


    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        /**Update the editText containing the player name if a player name was selected using the
         * Spinner drop down menu.*/

        long spinnerId = (parent).getId();

        if (!userIsInteracting) { // If it was an automated call from innitSipinner pass

//            Udate name player1 or player2
        }else if (spinnerId == R.id.spinnerP2){
            EditText textBox = (EditText)findViewById(R.id.usernameInputP2Startupscreen);
            textBox.setText(parent.getItemAtPosition(pos).toString());
        }else{
            EditText textBox = (EditText)findViewById(R.id.usernameInputP1Startupscreen);
            textBox.setText(parent.getItemAtPosition(pos).toString());
        }
    }


    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }


    private void initNameSpinner(){
        /**Initialize the name spinners with all the playernames in gameDefaults*/

//        Get the names, split then and set them as dropdownview
        String Names = gameDefaults.getString("playerNames", "");
        String[] prevoriouslyChosenNames = Names.split(";");
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, prevoriouslyChosenNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        Set the adapter on the spinner for player 1
        Spinner spin1 = (Spinner) findViewById(R.id.spinnerP1);
        spin1.setAdapter(adapter);
        spin1.setOnItemSelectedListener(this);

//        Set the adapter on the spinner for player 2
        Spinner spin2 = (Spinner) findViewById(R.id.spinnerP2);
        spin2.setAdapter(adapter);
        spin2.setOnItemSelectedListener(this);
    }


    public void startGame(View view) {
        /**Checks if the players both have chosen a different name.
         * Start the game if they have.
         * Creates a popUp if they haven't*/

//        Get information player 1
        EditText inputfieldP1 = (EditText) findViewById(R.id.usernameInputP1Startupscreen);
        String usernameP1 = String.valueOf(inputfieldP1.getText());

//        Get information player 2
        EditText inputfieldP2 = (EditText) findViewById(R.id.usernameInputP2Startupscreen);
        String usernameP2 = String.valueOf(inputfieldP2.getText());

//        If players haven't chosen a unique name warn them.
        if (usernameP1.length() == 0 || usernameP2.length() == 0 || usernameP1.equals(usernameP2)) {
            Intent popUp = new Intent(getApplicationContext(), PopUpScreen.class);
            popUp.putExtra("errorMsg", "A player has a unique name....");
            popUp.putExtra("button", "Oops, I forgot");
            startActivity(popUp);

//        Create new Intent Intent, add the player name, and start it.
        }else{
            Intent getNameScreenIntent = new Intent(this, gameScreen.class);
            getNameScreenIntent.putExtra("nameP1", usernameP1);
            getNameScreenIntent.putExtra("nameP2", usernameP2);
            startActivity(getNameScreenIntent);
        }
    }

}
