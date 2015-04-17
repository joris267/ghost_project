package com.joris_schefold.ghost;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/*Activity that functions as a pop up screen for the game activity.*/
public class popUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);
    }

    public void lesson_learned(View view) {
        finish();
    }
}
