package com.joris_schefold.ghost;

/**
 * Created by joris on 5/21/2015.
 * Custom function to throw when nextRound is called in game.java before the round has ended.
 */

class gameTerminationException extends Exception
{
    //Constructor that accepts a message
    public gameTerminationException(String message)
    {
        super(message);
    }
}