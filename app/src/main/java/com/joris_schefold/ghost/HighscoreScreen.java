package com.joris_schefold.ghost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HighscoreScreen extends Activity {
    Map<String, int[]> allScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore_screen);

//        Get the scores from sharedPreferences and find the tableView.
        SharedPreferences scorePrefs = getSharedPreferences(gameScreen.GAMESCORES, 0);
        TableLayout highScoreTable = (TableLayout)findViewById(R.id.highScoresTable);
        highScoreTable.setStretchAllColumns(true);
        highScoreTable.bringToFront();

//        Create new DecimalFormat for pretty printing the scores.
        DecimalFormat decimalFormater = new DecimalFormat("0.0");

//        Get all the scores.
        Map<String, ?> extractedScores  = scorePrefs.getAll();
        allScores = new HashMap<>();

        addRow("Name", "#played", "#won", "ratio", highScoreTable);

//        Loop over all the scores and split them.
        for (Object scoreString : extractedScores.entrySet()) {
            Map.Entry pair = (Map.Entry) scoreString;
            String[] scoreArray = ((String) pair.getValue()).split(";");
            System.out.println(Arrays.toString(scoreArray) + "  scorestring");
            int[] score = {Integer.parseInt(scoreArray[0]), Integer.parseInt(scoreArray[1])};
            allScores.put((String) pair.getKey(), score);

            double percentage = (double) 100 * score[1] / score[0];

            addRow((String) pair.getKey(), scoreArray[0], scoreArray[1],
                    decimalFormater.format(percentage) + "%", highScoreTable);
        }
    }


    private void addRow(String a, String b, String c, String d, TableLayout highScoreTable){
        //            Create the new row and collums for the scores.
        TableRow row = new TableRow(this);
        TextView collumName = new TextView(this);
        TextView collumPlayed = new TextView(this);
        TextView collumWin = new TextView(this);
        TextView collumPercentage = new TextView(this);

//            Set the values for the views.
        collumName.setText(a);
        collumPlayed.setText(b);
        collumWin.setText(c);
        collumPercentage.setText(d);

//            Add views to the main table.
        row.addView(collumName);
        row.addView(collumPlayed);
        row.addView(collumWin);
        row.addView(collumPercentage);
        highScoreTable.addView(row);
    }


    @Override
    public void onBackPressed() {
//        Disabled
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_highscore_screen, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void BackToMain(View view) {
        Intent back = new Intent(this, StartupScreen.class);
        startActivity(back);
    }
}
