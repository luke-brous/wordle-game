import java.util.ArrayList;



public class Wordle {

    private String targetWord;
    ArrayList<String> guesses;




    public Wordle(String answer){
        this.targetWord = answer;
        targetWord =

    }


    public static boolean isValidGuess(String guess){
        if(guess.length() != 5) {
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

}
