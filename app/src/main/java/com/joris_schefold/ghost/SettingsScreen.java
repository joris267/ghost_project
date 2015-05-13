package com.joris_schefold.ghost;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;


public class SettingsScreen extends Activity implements AdapterView.OnItemSelectedListener {
    HashMap<String, Integer> languages = new HashMap<String, Integer>();
    private SharedPreferences gameDefaults;
    private SharedPreferences.Editor defaultEditor;
    private int originalLanguage;
    String orignialP1Name;
    String orignialP2Name;

//    Setting an setOnItemSelectedListener will call onItemSelected, to stop this a bool is used
//    that is true if the actual user is calling onItemSelected.
    private boolean userIsInteracting;

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        long spinnerId = (parent).getId();
        if (!userIsInteracting) { //pass

//        If a new language was selected update the shared preferences
        }else if (spinnerId == R.id.languageSpinner) {
            defaultEditor = gameDefaults.edit();
            String selectedLanguage = parent.getItemAtPosition(pos).toString();
            defaultEditor.putInt("Language", languages.get(selectedLanguage));
            defaultEditor.apply();

//        Else a name was selected, update the editText containing the name.
        }else if (spinnerId == R.id.spinnerP1Settings){
            EditText textBox = (EditText)findViewById(R.id.usernameInputP1Settings);
            textBox.setText(parent.getItemAtPosition(pos).toString());
        }else if (spinnerId == R.id.spinnerP2Settings){
            EditText textBox = (EditText)findViewById(R.id.usernameInputP2Settings);
            textBox.setText(parent.getItemAtPosition(pos).toString());
        }
    }


    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }


    @Override
    public void onUserInteraction() {
        /**Checks if a user is interacting a or an automated process*/
        super.onUserInteraction();
        userIsInteracting = true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**Initialize some constants*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        gameDefaults = getSharedPreferences(StartupScreen.GAMEDEFAULTS, 0);
        originalLanguage = gameDefaults.getInt("Language",0);
        Intent origingIntent = getIntent();

//        Create languages dict.
        languages.put("Dutch", R.raw.dutch_dict);
        languages.put("English", R.raw.english_dict);
        initLanguageSpinner();
        initNameSpinners();

//        Set languages as defaults
        EditText p1TextView = (EditText)findViewById(R.id.usernameInputP1Settings);
        EditText p2TextView = (EditText)findViewById(R.id.usernameInputP2Settings);
        orignialP1Name = origingIntent.getExtras().getString("p1");
        orignialP2Name = origingIntent.getExtras().getString("p2");
        p1TextView.setText(orignialP1Name);
        p2TextView.setText(orignialP2Name);
    }



    private void initNameSpinners(){
        /**Initialize the name spinners with all the playernames in gameDefaults*/
        String Names = gameDefaults.getString("playerNames", "");
        String[] prevoriouslyChosenNames = Names.split(";");

        Spinner spin1 = (Spinner) findViewById(R.id.spinnerP1Settings);
        Spinner spin2 = (Spinner) findViewById(R.id.spinnerP2Settings);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, prevoriouslyChosenNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adapter);
        spin1.setOnItemSelectedListener(this);
        spin2.setAdapter(adapter);
        spin2.setOnItemSelectedListener(this);
    }

    public void initLanguageSpinner(){
        /**Initialize with all the possible languages. Set the current language as the dispayed value*/
        String[] laugageList = languages.keySet().toArray(new String[languages.size()]);

//        Initialize spinner
        Spinner languageSpinner = (Spinner) findViewById(R.id.languageSpinner);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, laugageList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);
        languageSpinner.setOnItemSelectedListener(this);

//        set current language as default language in spinner by looping over all the languages
//        until the language id is the same as the current default one
        for (Object o : languages.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            if (gameDefaults.getInt("Language", 0) == (int)pair.getValue()) {

//                If we know the Id of the language we nee to loop over all the languages in the
//                languagelist to get its id.
                for (int i = 0; i < languages.size(); i++) {
                    if (pair.getKey() == laugageList[i]) {
                        languageSpinner.setSelection(i);
                        System.out.println(pair.getKey() + laugageList[i] + gameDefaults.getInt("Language", 0)  + "   " + pair.getValue() );
                        break;
                    }
                }
//              Breaks out for loop over all the languages.
                break;
            }
        }
    }


    public void clearSharedPreferences(View view) {

        SharedPreferences gameScores = getSharedPreferences(gameScreen.GAMESCORES, 0);
        SharedPreferences gameDefaults = getSharedPreferences(StartupScreen.GAMEDEFAULTS, 0);
        EditText editTextP1 = (EditText) findViewById(R.id.usernameInputP1Settings);
        EditText editTextP2 = (EditText) findViewById(R.id.usernameInputP2Settings);
        String nameP1 = String.valueOf(editTextP1.getText());
        String nameP2 = String.valueOf(editTextP2.getText());
        int language = gameDefaults.getInt("Language", 0);
        SharedPreferences.Editor editor = gameDefaults.edit();
        editor.clear();

//        Keep language
        editor.putInt("Language", language);
        editor.commit();
//      Need to update the name Spinners
//        initNameSpinners();
//        editTextP1.setText(nameP1);
//        editTextP2.setText(nameP2);

        SharedPreferences.Editor editor2 = gameScores.edit();
        editor2.clear();
        editor2.commit();
    }


    public void goBack(View view) {
        /**Function for going back, checks if something vital has changed*/

//        Create new intent and get player names.
        Intent goingBack = new Intent();
        EditText p1TextView = (EditText)findViewById(R.id.usernameInputP1Settings);
        EditText p2TextView = (EditText)findViewById(R.id.usernameInputP2Settings);
        String newNameP1 = String.valueOf(p1TextView.getText());
        String newNameP2 = String.valueOf(p2TextView.getText());

//        restart is True if the language or one of the player names has changed
        boolean restart = ((originalLanguage != gameDefaults.getInt("Language", -1)) ||
                !(newNameP1.equalsIgnoreCase(orignialP1Name)) ||
                !(newNameP2.equalsIgnoreCase(orignialP2Name)));
        goingBack.putExtra("restart",restart);
        goingBack.putExtra("nameP1",newNameP1);
        goingBack.putExtra("nameP2",newNameP2);
        if (getParent() == null) {
            setResult(Activity.RESULT_OK, goingBack);
        } else {
            getParent().setResult(Activity.RESULT_OK, goingBack);
        }
        finish();
    }
}
