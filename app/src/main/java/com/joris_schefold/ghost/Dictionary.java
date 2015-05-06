package com.joris_schefold.ghost;


import android.app.Activity;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 * Created by joris on 4/15/2015.
 * Dictionary class for the ghost game project.
 */
public class Dictionary{
    private HashSet<String> active_dictionary = new HashSet<>();
    private HashSet<String> filtered_list = new HashSet<>();
    private Activity dictonary_activity;


    public Dictionary(Activity activ, int fileId){
        dictonary_activity = activ;
        active_dictionary = loadDictonary(fileId);
//        filtered_list = (HashSet<String>)active_dictionary.clone();
        filtered_list = deepCloneHashSet(active_dictionary);
    }

    HashSet<String> loadDictonary(int fileId){
        HashSet<String> dictionary = new HashSet<>();
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
        /**
        * Checks if a word was formed, returns True if formed, false otherwise
        * */
        return filtered_list.contains(word);
    }

    int count_remaining_words(){
        /**
        * Returns how many words are remaining in the dictionary as int*/
        return filtered_list.size();
    }


    private HashSet<String> deepCloneHashSet(HashSet<String> set){
        /**Returns a deep clone of HashSet*/
        HashSet<String> copy = new HashSet<>(set.size());
        for (String aSet : set) {
            copy.add(aSet);
        }
        return copy;
    }

    String result(){
        /*Returns the last remaining word if there is only one word left, null otherwise.*/
        if (this.count_remaining_words() == 1) {
            Iterator<String> iter = filtered_list.iterator();
            return iter.next();
        }
        return null;
    }


    void reset(){
        /*resets the filtered list to its non filtered value (complete dictionary)*/
//        HashSet<String> filtered_list = new HashSet<String>(active_dictionary);
        filtered_list = deepCloneHashSet(active_dictionary);
    }

}