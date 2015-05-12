package com.joris_schefold.ghost;

import android.app.Activity;

import com.joris_schefold.ghost.Dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 * Created by joris on 5/12/2015.
 */
public class DictionaryConstructor {
    Activity activity;
    HashSet dict;


    public DictionaryConstructor(Activity activ) {
        activity = activ;
    }

    public Dictionary createDict(int fileId){
        dict = loadDictonary(fileId);
        return new Dictionary(dict);
    }

    HashSet<String> loadDictonary(int fileId){
        /**Loads the dictinary from file with fileID.
         * Filters strange symbols and converts everything to lowercase.
         * Returns a new Hashset with all the words.*/
        HashSet<String> dictionary = new HashSet<>();
        BufferedReader buffreader;
        String line;
        try {
//            Create reader
            InputStream is = activity.getResources().openRawResource(fileId);
            buffreader = new BufferedReader(new InputStreamReader(is));

//            As long as there are new lines and they dont contain strange symbols add them to dict.
            while ((line = buffreader.readLine()) != null) {
                if (isAlpha(line)) {dictionary.add(line.toLowerCase());}
            }
            buffreader.close();
        }catch (IOException e){System.out.println(e + "<============");}

        return dictionary;
    }



    public boolean isAlpha(String word) {
        /**Checks if all the characters in the string are letters.*/
        char[] chars = word.toCharArray();
        for (char letter : chars) {
            if(!Character.isLetter(letter)) {
                return false;
            }
        }
        return true;
    }


}
