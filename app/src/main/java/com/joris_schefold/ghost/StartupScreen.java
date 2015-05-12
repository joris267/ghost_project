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

public class StartupScreen extends Activity implements AdapterView.OnItemSelectedListener {
    public static final String GAMEDEFAULTS = "DefaultsFile";
    SharedPreferences gameDefaults;
    private boolean userIsInteracting;


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

//        If the spinner for P1 was used update his name, otherwise update the name of P2.
        long spinnerId = (parent).getId();
        if (!userIsInteracting) { //pass
        }else if (spinnerId == R.id.spinnerP2){
            EditText textBox = (EditText)findViewById(R.id.usernameInputP2Startupscreen);
            textBox.setText(parent.getItemAtPosition(pos).toString());
            System.out.println("changeing name p1 to " + parent.getItemAtPosition(pos).toString()  + "<====================" + userIsInteracting);
        }else{
            EditText textBox = (EditText)findViewById(R.id.usernameInputP1Startupscreen);
            textBox.setText(parent.getItemAtPosition(pos).toString());
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }


    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_screen);
        gameDefaults = getSharedPreferences(StartupScreen.GAMEDEFAULTS, 0);
//        clearSharedPreferences();

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.settingsStartupScreen) {


            //        Get information player 1
            EditText inputfield_p1 = (EditText) findViewById(R.id.usernameInputP1Startupscreen);
            String username_p1 = String.valueOf(inputfield_p1.getText());

//        Get information player 2
            EditText inputfield_p2 = (EditText) findViewById(R.id.usernameInputP2Startupscreen);
            String username_p2 = String.valueOf(inputfield_p2.getText());

            Intent settings = new Intent(this, SettingsScreen.class);
            settings.putExtra("p1", username_p1);
            settings.putExtra("p2", username_p2);
            startActivityForResult(settings,0);
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean initNameSpinner(){
        String Names = gameDefaults.getString("playerNames", "");
        String[] prevoriouslyChosenNames = Names.split(";");

        Spinner spin1 = (Spinner) findViewById(R.id.spinnerP1);
        Spinner spin2 = (Spinner) findViewById(R.id.spinnerP2);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, prevoriouslyChosenNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adapter);
        spin1.setOnItemSelectedListener(this);
        spin2.setAdapter(adapter);
        spin2.setOnItemSelectedListener(this);
        return true;
    }

    public void startGame(View view) {
//        Get information player 1
        EditText inputfield_p1 = (EditText) findViewById(R.id.usernameInputP1Startupscreen);
        String username_p1 = String.valueOf(inputfield_p1.getText());

//        Get information player 2
        EditText inputfield_p2 = (EditText) findViewById(R.id.usernameInputP2Startupscreen);
        String username_p2 = String.valueOf(inputfield_p2.getText());

        if (username_p1.length() == 0 || username_p2.length() == 0 || username_p1.equals(username_p2)) {
            Intent popUp = new Intent(getApplicationContext(), PopUpScreeh.class);
            popUp.putExtra("errorMsg", "A player has a unique name....");
            popUp.putExtra("button", "Oops, I forgot");
            startActivity(popUp);
        }else{
            Intent getNameScreenIntent = new Intent(this,
                    gameScreen.class);
            final int result = 1;

            //Put the information in the intent.
            getNameScreenIntent.putExtra("name_p1", username_p1);
            getNameScreenIntent.putExtra("name_p2", username_p2);
            startActivityForResult(getNameScreenIntent, result);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //      reste names
        String nameP1 = data.getExtras().getString("nameP1");
        String nameP2= data.getExtras().getString("nameP2");
        if (initNameSpinner()){
            try {
                Thread.sleep(100);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            TextView textNameP1 = (TextView) findViewById(R.id.usernameInputP1Startupscreen);
            textNameP1.setText(nameP1);
            System.out.println("chagingname p1 to ===============>" + nameP1);
            TextView textNameP2 = (TextView) findViewById(R.id.usernameInputP2Startupscreen);
            textNameP2.setText(nameP2);
        }
    }

}
