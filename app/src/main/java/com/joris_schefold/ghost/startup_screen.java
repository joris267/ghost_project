package com.joris_schefold.ghost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class startup_screen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_screen);
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
