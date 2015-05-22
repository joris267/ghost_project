package com.joris_schefold.ghost;


import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by joris on 4/15/2015.
 * Dictionary class for the ghost game project.
 * Contains a Hashset with words that can be filtered.
 */

public class Dictionary{
    private HashSet<String> activeDictionary = new HashSet<>();
    private HashSet<String> filteredList = new HashSet<>();


    public Dictionary(HashSet<String> dict){
        activeDictionary = dict;
        filteredList = deepCloneHashSet(activeDictionary);
    }


    public void filter(String input){
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


    public boolean formedWord(String word){
        /**Checks if a word was formed, returns True if formed, false otherwise*/
        return filteredList.contains(word);
    }


    public int countRemainingWords(){
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


    public String result(){
        /**Returns the last remaining word if there is only one word left, null otherwise.*/
        if (this.countRemainingWords() == 1) {
            return (String) (filteredList.toArray())[0];
        }
        return null;
    }


    public void reset(){
        /**Resets the filtered list to its non filtered value (complete dictionary)*/
        filteredList = deepCloneHashSet(activeDictionary);
    }
}