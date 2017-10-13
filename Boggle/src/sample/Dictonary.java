package sample;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Dictonary extends Main
{

    /**
     * fillDictonary will read in our dictonary and fill an arraylist called words for me to use.
     * @return words: Which is our arraylist of words we are going to use.
     */


    public static ArrayList<String> fillDictonary()
    {
        ArrayList<String> words;
        words = new ArrayList<String>();
        String filename = "Dictionary.txt";
        String line;
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                words.add(line);
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file " + filename + " ");
        } catch (IOException ex) {
            System.out.println("Error reading file " + filename + " ");
        }
        return words;
    }
}
