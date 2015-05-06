package com.joris_schefold.ghost;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*Activity that functions as a pop up screen for the game activity.*/
public class PopUpScreeh extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);
        TextView messageView = (TextView) findViewById(R.id.errorMsg);
        String error = getIntent().getExtras().getString("errorMsg");
        messageView.setText(error);
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams)messageView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        messageView.setLayoutParams(layoutParams);



        TextView buttonView = (TextView) findViewById(R.id.backButton);
        String buttonText = getIntent().getExtras().getString("button");
        buttonView .setText(buttonText);
        RelativeLayout.LayoutParams layoutParams2 =
                (RelativeLayout.LayoutParams)buttonView.getLayoutParams();
        layoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        buttonView.setLayoutParams(layoutParams2);
    }

    public void popUp(String errors){

    }

    public void lesson_learned(View view) {
        finish();
    }
}
