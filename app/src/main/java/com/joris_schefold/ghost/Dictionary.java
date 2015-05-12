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
 * Contains a Hashset with words that can be filtered.
 */

public class Dictionary{
    private HashSet<String> activeDictionary = new HashSet<>();
    private HashSet<String> filteredList = new HashSet<>();
    private Activity dictonaryActivity;


    public Dictionary(HashSet dict){
        activeDictionary = dict;
        filteredList = deepCloneHashSet(activeDictionary);
    }
//    public Dictionary(Activity activ, int fileId){
//        dictonaryActivity = activ;
//        activeDictionary = loadDictonary(fileId);
////        Need a copy of the list to be able to reset it for the next round.
//        filteredList = deepCloneHashSet(activeDictionary);
//    }
//
//    HashSet<String> loadDictonary(int fileId){
//        /**Loads the dictinary from file with fileID.
//         * Filters strange symbols and converts everything to lowercase.
//         * Returns a new Hashset with all the words.*/
//        HashSet<String> dictionary = new HashSet<>();
//        BufferedReader buffreader;
//        String line;
//        try {
////            Create reader
//            InputStream is = dictonaryActivity.getResources().openRawResource(fileId);
//            buffreader = new BufferedReader(new InputStreamReader(is));
//
////            As long as there are new lines and they dont contain strange symbols add them to dict.
//            while ((line = buffreader.readLine()) != null) {
//                if (isAlpha(line)) {dictionary.add(line.toLowerCase());}
//            }
//            buffreader.close();
//        }catch (IOException e){System.out.println(e + "<============");}
//
//        return dictionary;
//    }
//
//    public boolean isAlpha(String word) {
//        /**Checks if all the characters in the string are letters.*/
//        char[] chars = word.toCharArray();
//        for (char letter : chars) {
//            if(!Character.isLetter(letter)) {
//                return false;
//            }
//        }
//        return true;
//    }


    void filter(String input){
        /**
         * Filters the dictionary. Must be called every time a new letter is guessed.
         * Param, letter guessed.
         */
        for (Iterator<String> iter = filteredList.iterator(); iter.hasNext();) {
            String word = iter.next();
            if (!word.startsWith(input)) {
                iter.remove();
            }
        }
    }

    boolean formed_word(String word){
        /**Checks if a word was formed, returns True if formed, false otherwise*/
        return filteredList.contains(word);
    }

    int count_remaining_words(){
        /**Returns how many words are remaining in the dictionary as int*/
        return filteredList.size();
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
        /**Returns the last remaining word if there is only one word left, null otherwise.*/
        if (this.count_remaining_words() == 1) {
            Iterator<String> iter = filteredList.iterator();
            return iter.next();
        }
        return null;
    }


    void reset(){
        /**Resets the filtered list to its non filtered value (complete dictionary)*/
        filteredList = deepCloneHashSet(activeDictionary);
    }
}