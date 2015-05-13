package com.joris_schefold.ghost;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**Activity that functions as a pop up screen, has message and one button.*/

public class PopUpScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        /**Sets the text for the message and for the back button*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

//        Set text of the message.
        TextView messageView = (TextView) findViewById(R.id.errorMsg);
        String error = getIntent().getExtras().getString("errorMsg");
        messageView.setText(error);
//        RelativeLayout.LayoutParams layoutParams =
//                (RelativeLayout.LayoutParams)messageView.getLayoutParams();
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//        messageView.setLayoutParams(layoutParams);

//        Set text of the back button.
        TextView buttonView = (TextView) findViewById(R.id.backButton);
        String buttonText = getIntent().getExtras().getString("button");
        buttonView .setText(buttonText);
//        RelativeLayout.LayoutParams layoutParams2 =
//                (RelativeLayout.LayoutParams)buttonView.getLayoutParams();
//        layoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
//        buttonView.setLayoutParams(layoutParams2);
    }


    public void lessonLearned(View view) {
        /**Used to close the popUp*/
        finish();
    }
}
