package com.joris_schefold.ghost;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by joris on 4/15/2015.
 */
public class gameScreen extends Activity {
    private Game game_class;
    private Button confirm_restart_button;


/* Below two different on click listeners are created, one to confirm the input and one to move to
* the next round*/
    View.OnClickListener confirm_input = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            Getting the user input and the word thus far
            EditText input_field = (EditText) findViewById(R.id.player_letter_input_field);
            String input = String.valueOf(input_field.getText()).toLowerCase();
            TextView word_view = (TextView) findViewById(R.id.ghost_word_textview);

//            If the input is not a letter the user is given a warning
            if(!input.matches("[a-z]") || input.length() != 1){
                Intent popUp = new Intent(getApplicationContext(), com.joris_schefold.ghost.popUp.class);
                startActivity(popUp);
            }
//            If the input is a letter and it is valid the next player is.
            else if (game_class.valid_guess(input)){
                String word = String.valueOf(word_view.getText());
                word = word + input;
                word_view.setText(word);
//                If the input is not valid the player gets a message explaining why he lost (NOT YET)
//                and the confirm input button can be used to start a new round.
            }else{
                word_view.setText("You lost for some reason");
                confirm_restart_button.setText("Next round");
                confirm_restart_button.setOnClickListener(next_round);
            }
        }
    };

//    Moves to the next round.
    View.OnClickListener next_round = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            game_class.next_round();
            confirm_restart_button.setText("confirm input");
            confirm_restart_button.setOnClickListener(confirm_input);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);

//        Gets the player names from the previous screen and sets it in the activity.
        Intent startup_screen_data = getIntent();
        String p1_name = startup_screen_data.getExtras().getString("name_p1");
        TextView p1_textview = (TextView) findViewById(R.id.p1_name_view);
        p1_textview.setText(p1_name);

        String p2_name = startup_screen_data.getExtras().getString("name_p2");
        TextView p2_textview = (TextView) findViewById(R.id.p2_name_view);
        p2_textview.setText(p2_name);

//        Set first on click listner
        confirm_restart_button = (Button) this.findViewById(R.id.confirm_restart);
        confirm_restart_button.setOnClickListener(confirm_input);

//        Creates a dictionary and start the game.
        Dictionary dictonary = new Dictionary(this, R.raw.dutch_dict);
        game_class = new Game(this, dictonary, p1_name, p2_name);
    }



    public void BackToMain(View view) {
//        Function for going back to the main menu
        Intent goingBack = new Intent();
        setResult(RESULT_OK, goingBack);
        finish();
    }
}





