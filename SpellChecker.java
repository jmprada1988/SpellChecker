package unit5Set;

import java.io.*;
import java.util.Scanner;
import java.util.HashSet;
import javax.swing.*;
import java.util.TreeSet;
import java.util.Iterator;
 /**
 * This class works as a basic spell-checker. It uses the file words.txt to
 * check whether a given word is correctly spelled.
 * The user needs to provide a file with the list of words to check if it is properly spelled 
 * or not. This program implements the subroutines provided to complete the assignment.
 */
public class SpellChecker {
     public static void main(String[] args) {
    
        Scanner words;
        HashSet<String> dict = new HashSet<String>();
        Scanner userFile;
        
        try {
        
            words = new Scanner(new File("/home/lein/uopeople/Programming2/words.txt"));
        
            while (words.hasNext()) {
                String word = words.next();
                dict.add(word.toLowerCase());
            }
            
            userFile = new Scanner(getInputFileNameFromUser());
            
            // Skip over any non-letter characters in the file.
            userFile.useDelimiter("[^a-zA-Z]+");
            
            HashSet<String> badWords = new HashSet<String>();
            while (userFile.hasNext()) {
                String userWord = userFile.next();
                userWord = userWord.toLowerCase();
                if (!dict.contains(userWord) && 
                    !badWords.contains(userWord)) {
                    
                    badWords.add(userWord);
                    TreeSet<String> goodWords = new TreeSet<String>();
                    goodWords = corrections(userWord, dict);
                    System.out.print(userWord + ": ");
                    if (goodWords.isEmpty())
                        System.out.println("(no suggestions)");
                    else {
                        int count = 0;
                        for (String goodWord: goodWords) {
                            System.out.print(goodWord);
                            if (count < goodWords.size() - 1)
                                System.out.print(", ");
                            else
                                System.out.print("\n");
                            count++;
                        }
                    }
                    
                }
            
            }
            
        }
        catch (FileNotFoundException e) {
            System.exit(0);
        }
    
    } // end main()
    
     /**
      * Lets the user select an input file using a standard file
      * selection dialog box.  If the user cancels the dialog
      * without selecting a file, the return value is null.
      */
     static File getInputFileNameFromUser() {
        JFileChooser fileDialog = new JFileChooser();
        fileDialog.setDialogTitle("Select File for Input");
        int option = fileDialog.showOpenDialog(null);
        if (option != JFileChooser.APPROVE_OPTION)
           return null;
        else
           return fileDialog.getSelectedFile();
     }
    
    /*
     * Gives a list of possible correct spellings for misspelled words which
     * are variations of a a given word that are present in the dictionary.
     *
     * @return A tree set containing a list of possible corrections to the
     *         misspelled word.
     */
    static TreeSet corrections(String badWord, HashSet dictionary) {
    
        TreeSet<String> possibleWords =  new TreeSet<String>();
        String subStr1, subStr2, possibility;
        
        for (int i = 0; i < badWord.length(); i++) {
        
            // Remove character i from the word.
            subStr1 = badWord.substring(0, i);
            subStr2 = badWord.substring(i + 1);
            
            // Delete any one of the letters from the misspelled word.
            possibility = subStr1 + subStr2;
            if (dictionary.contains(possibility))
                possibleWords.add(possibility);
            
            // Change any letter in the misspelled word into any other
            // letter.    
            for (char ch = 'a'; ch <= 'z'; ch++) {
                possibility = subStr1 + ch + subStr2;
                if (dictionary.contains(possibility))
                    possibleWords.add(possibility);
            }
             // Divide the word into two substrings.
            subStr1 = badWord.substring(0, i);
            subStr2 = badWord.substring(i);
            
            // Insert any letter at any point in the misspelled word.
            for (char ch = 'a'; ch <= 'z'; ch++) {
                possibility = subStr1 + ch + subStr2;
                if (dictionary.contains(possibility))
                    possibleWords.add(possibility);
            }
            
            // Insert a space at any point in the misspelled word and check
            // that both of the words that are produced are in the dictionary.
            char ch = ' ';
            possibility = subStr1 + ch + subStr2;
            if (dictionary.contains(subStr1) && dictionary.contains(subStr2))
                      possibleWords.add(possibility);
                      
        }
        
        // Swap any two neighbouring characters in the misspelled word.
        for (int i = 1; i < badWord.length(); i++) {
            subStr1 = badWord.substring(0, i - 1);
            char ch1 = badWord.charAt(i - 1);
            char ch2 = badWord.charAt(i);
            subStr2 = badWord.substring(i + 1);
            possibility = subStr1 + ch2 + ch1 + subStr2;
            if (dictionary.contains(possibility))
                possibleWords.add(possibility);
        }
        
        return possibleWords;
    
    } // end corrections()
 } // end class SpellChecker
