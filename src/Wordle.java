import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Wordle {

    private String targetWord;
    private ArrayList<String> dictionary;
    private ArrayList<String> guesses;




    public Wordle(String answer, ArrayList<String> dictionary){
        this.targetWord = answer;
        this.dictionary = dictionary;

    }

    public static ArrayList<String> loadDictionary() {
        ArrayList<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("5_letter_words.txt"))) {
            String line;
            while((line = reader.readLine()) != null) {
                words.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StdOut.println(words.toArray().length);
        return words;


    }


    public static boolean isValidGuess(String guess, ArrayList<String> dictionary){
        if(guess.length() != 5 || !dictionary.contains(guess)) {
            return false;
        }

        return true;
    }


    public static ArrayList<String> checkGuess(String guess){
        ArrayList<String> array = new ArrayList<String>(5);
        return array;
    }

    public static boolean isGameOver(){
        return false;
    }

    public static void reset(){

    }

    public static void main(String[] args) {
        ArrayList<String> dict = loadDictionary();
        System.out.print(dict);
    }

}
