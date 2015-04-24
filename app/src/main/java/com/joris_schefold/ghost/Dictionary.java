package com.joris_schefold.ghost;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by joris on 4/15/2015.
 */
public class Dictionary{
//  This must be changed for a function to load the dictionary, ALL LOWERCASE
    private Set<String> active_dictionary = new HashSet<String>(Arrays.asList("aap",
        "appel","annanas","aarde","aardsteen","aardachtig",
        "aanbeeld","anker","Anders","achtig"));

    private Set<String> filtered_list = active_dictionary;


    void filter(String input){
        /**
         * Filters the dictionary. Must be called every time a new letter is guessed.
         * Param, letter guessed.
         */
//
//        for (String word : filtered_list){
//            if (!word.startsWith(input)){
//                filtered_list.remove(word);
//            }
//        }


        for (Iterator<String> iter = filtered_list.iterator(); iter.hasNext();) {
            String word = iter.next();
            if (!word.startsWith(input)) {
                iter.remove();
            }
        }

//
//        List<String> new_filtered_list = new ArrayList<String>();
//
//        for(int i =0; i < filtered_list.size(); i++){
//            String word = filtered_list.get(i);
//            if (word.startsWith(input)){
//                new_filtered_list.add(word);
//            }
//        }
//        filtered_list = new_filtered_list;

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
        filtered_list = active_dictionary;
    }
}