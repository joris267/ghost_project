package com.joris_schefold.ghost;


import android.app.Activity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 * Created by joris on 4/15/2015.
 */
public class Dictionary{
//    private HashSet<String> active_dictionary = new HashSet<String>(Arrays.asList("aap","appel","annanas","aarde","aardsteen","aardachtig","aanbeeld","anker","Anders","achtig"));
    private HashSet<String> active_dictionary = new HashSet<String>();
    private HashSet<String> filtered_list = new HashSet<String>();
    private Activity dictonary_activity;


    public Dictionary(Activity activ, int fileId){
        dictonary_activity = activ;
        active_dictionary = loadDictonary(fileId);
        filtered_list = (HashSet<String>)active_dictionary.clone();
        filtered_list = deepCloneHashSet(active_dictionary);
//        filtered_list = new HashSet<String>(active_dictionary);
    }

    HashSet<String> loadDictonary(int fileId){
        HashSet<String> dictionary = new HashSet<String>();
        BufferedReader buffreader;
        String line;
        try {
            InputStream is = dictonary_activity.getResources().openRawResource(fileId);
            buffreader = new BufferedReader(new InputStreamReader(is));
            while ((line = buffreader.readLine()) != null) {
                if (isAlpha(line)) {
                    dictionary.add(line.toLowerCase());
                }
            }
            buffreader.close();
        }catch (IOException e){
            System.out.println(e + "<============");
        }

        return dictionary;
    }

    public boolean isAlpha(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }


    void filter(String input){
        /**
         * Filters the dictionary. Must be called every time a new letter is guessed.
         * Param, letter guessed.
         */
        for (Iterator<String> iter = filtered_list.iterator(); iter.hasNext();) {
            String word = iter.next();
            if (!word.startsWith(input)) {
                iter.remove();
            }
        }
    }

    boolean formed_word(String word){
        /*
        * Checks if a word was formed, returns True if formed, false otherwise
        * */
        return filtered_list.contains(word);
    }

    int count_remaining_words(){
        /*
        * Returns how many words are remaining in the dictionary as int*/
        int list_length;
        list_length = filtered_list.size();
        return list_length;
    }


    public HashSet<String> deepCloneHashSet(HashSet set){
        HashSet<String> copy = new HashSet<String>(set.size());
        Iterator<String> iterator = set.iterator();
        while(iterator.hasNext()){
            copy.add(iterator.next());
        }
        return copy;
    }

    String result(){
        /*Returns the last remaining word if there is only one word left, null otherwise.
        * Function is never used but was required for some reason.*/
        if (this.count_remaining_words() == 1) {
            Iterator iter = filtered_list.iterator();
            String last_word = (String)iter.next();
            return last_word;
        }
        return null;
    }


    void reset(){
        /*resets the filtered list to its non filtered value (complete dictionary)*/
//        HashSet<String> filtered_list = new HashSet<String>(active_dictionary);
        filtered_list = deepCloneHashSet(active_dictionary);
    }

}