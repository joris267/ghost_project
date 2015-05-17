package com.joris_schefold.ghost;

import java.util.Comparator;

/**
* Created by joris on 4/30/2015.
* Class that holds all the attributes of the score.
* Name, #gamesplayed, #gameswon and the percentage.
*/

public class Score{
    private String scoreName;
    private int scorePlayed;
    private int scoreWon;
    private float scorePercentage;

    public Score(String name, int gamesPlayed, int gamesWon){
        scoreName = name;
        scorePlayed = gamesPlayed;
        scoreWon = gamesWon;
        scorePercentage = (gamesWon / (float) gamesPlayed) * 100;
    }

    public int getNumberPlayed(){
        return scorePlayed;
    }


    public int getNumberWon(){
        return scoreWon;
    }


    public float getPercetage(){
        return scorePercentage;
    }


    public String getName(){
        return scoreName;
    }




    static Comparator<Score> totalComperator() {
        return new Comparator<Score>() {
            public int compare(Score one, Score two) {
                /** return 0 if equal
                * 1 if one greater then two
                * -1 if one smaller then two*/
                float scoreOne = one.getNumberPlayed() * one.getPercetage();
                float scoreTwo = two.getNumberPlayed() * two.getPercetage();
                if (scoreOne == scoreTwo){
                    return 0;
                }else if(scoreOne > scoreTwo){
                    return 1;
                }else{
                    return -1;
                }
            }
        };
    }

//    static Comparator<Score> getAttribute2Comparator() {
//        return new Comparator<Score>() {
//            // compare using attribute 2
//        };
//    }


//    public int compareTo(Score sc){
//        //return 0 if equal
//        //1 if one greater then two
//        //-1 if one smaller then two
//        return sc.scoreNum>scoreNum? 1 : sc.scoreNum<scoreNum? -1 : 0;
//    }
}





