package com.joris_schefold.ghost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HighscoreScreen extends Activity {
    ArrayList<Score>  allScores;
    DecimalFormat decimalFormater;
    TableLayout highScoreTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**Loads scores from sharedPreferences and initilizes the high score table*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore_screen);

//        Create new DecimalFormat for pretty printing the scores.
        decimalFormater = new DecimalFormat("0.0");

//        Get the scores from sharedPreferences and find the tableView.
        SharedPreferences scorePrefs = getSharedPreferences(gameScreen.GAMESCORES, 0);
        Map<String, ?> extractedScores  = scorePrefs.getAll();
        highScoreTable = (TableLayout)findViewById(R.id.highScoresTable);
        highScoreTable.setStretchAllColumns(true);
        highScoreTable.bringToFront();

        initTable(extractedScores);
    }


    @Override
    public void onBackPressed() {
//        Disabled
    }


    private void initTable(Map scores){
        /**Add all values of scores to the table, also adds header*/
        allScores = new ArrayList<>();
        addRow("NAME", "#PLAYED", "#WON", "RATIO");

//        Loop over all the scores, split them and add them to allScores.
        for (Object scoreString : scores.entrySet()) {
            Map.Entry pair = (Map.Entry) scoreString;
            String[] scoreArray = ((String) pair.getValue()).split(";");
            Score score = new Score((String) pair.getKey(), Integer.parseInt(scoreArray[0]), Integer.parseInt(scoreArray[1]));
            allScores.add(score);
        }


        Collections.sort(allScores, Score.totalComperator());

//        Add everything to the table in reverse order, this is because first added element is
//        pushed to the bottom of the table by subsequent elements.
        for (int i = allScores.size() -1; i >= 0; i--){
            Score score = allScores.get(i);
            addRow(score.getName(), Integer.toString(score.getNumberPlayed()),
                    Integer.toString(score.getNumberWon()),
                    decimalFormater.format(score.getPercetage()) + "%");
        }
    }


    private void addRow(String a, String b, String c, String d){
        /**Add a new row to the table with four columns.
         * Item a goes in the first column, b in the second etc.*/

 //        Create the new row and columns for the scores.
        TableRow row = new TableRow(this);
        TextView collumName = new TextView(this);
        TextView collumPlayed = new TextView(this);
        TextView collumWin = new TextView(this);
        TextView collumPercentage = new TextView(this);

//       Set the values for the views.
        collumName.setText(a);
        collumPlayed.setText(b);
        collumWin.setText(c);
        collumPercentage.setText(d);

//        Add views to the main table.
        row.addView(collumName);
        row.addView(collumPlayed);
        row.addView(collumWin);
        row.addView(collumPercentage);
        highScoreTable.addView(row);
    }


    public void BackToMain(View view) {
        /**Go back to StartupScreen*/
        Intent back = new Intent(this, StartupScreen.class);
        startActivity(back);
    }
}
