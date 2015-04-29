package com.joris_schefold.ghost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class startup_screen extends Activity implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        long spinnerId = ((Spinner)parent).getId();
        if (spinnerId == R.id.spinnerP2){
            EditText textBox = (EditText)findViewById(R.id.username_input_p2_startupscreen);
            textBox.setText(parent.getItemAtPosition(pos).toString());
        }else{
            EditText textBox = (EditText)findViewById(R.id.username_input_p1_startupscreen);
            textBox.setText(parent.getItemAtPosition(pos).toString());
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_screen);


        String[] prevoriouslyChosenNames = {"", "Jan", "Peter", "John", "Darth Vader", "no one"};
        Spinner spin1 = (Spinner) findViewById(R.id.spinnerP1);
        Spinner spin2 = (Spinner) findViewById(R.id.spinnerP2);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, prevoriouslyChosenNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adapter);
        spin1.setOnItemSelectedListener(this);
        spin2.setAdapter(adapter);
        spin2.setOnItemSelectedListener(this);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startGame(View view) {
        Intent getNameScreenIntent = new Intent(this,
                gameScreen.class);
        final int result = 1;


//        Get information player 1
        EditText inputfield_p1 = (EditText)
                findViewById(R.id.username_input_p1_startupscreen);
        String username_p1 = String.valueOf(inputfield_p1.getText());

//        Get information player 2
        EditText inputfield_p2 = (EditText)
                findViewById(R.id.username_input_p2_startupscreen);
        String username_p2 = String.valueOf(inputfield_p2.getText());

//        Put the information in the intent.
        getNameScreenIntent.putExtra("name_p1", username_p1);
        getNameScreenIntent.putExtra("name_p2", username_p2);
        startActivityForResult(getNameScreenIntent, result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
    }

}
