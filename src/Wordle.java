import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Wordle game implementation.
 */
public class Wordle {

    private String answer;
    private final ArrayList<String> dictionary;
    private final ArrayList<String> guesses = new ArrayList<>();
    private final Random rand = new Random();

    /**
     * Constructs a Wordle game with a given answer and dictionary.
     *
     * @param answer     the initial answer word
     * @param dictionary the list of valid guess words
     */
    public Wordle(String answer, ArrayList<String> dictionary) {
        this.answer = answer.toLowerCase();
        this.dictionary = dictionary;
    }

    /**
     * Loads all 5-letter words into a list.
     *
     * @return list of words read from the file
     */
    public static ArrayList<String> loadDictionary() {
        ArrayList<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("5_letter_words.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StdOut.println(words.size());
        return words;
    }

    /**
     * Checks if a guess is valid.
     *
     * @param guess      the word to validate
     * @param dictionary the list of valid words
     * @return true if guess is valid, false otherwise
     */
    public static boolean isValidGuess(String guess, ArrayList<String> dictionary) {
        return guess.length() == 5 && dictionary.contains(guess);
    }

    /**
     * Compares a guess to the answer and returns color feedback.
     *
     * @param guess  the guessed word
     * @param answer the correct answer
     * @return list of "green", "yellow", or "red" indicators
     */
    public static ArrayList<String> checkGuess(String guess, String answer) {
        guess = guess.toLowerCase();
        ArrayList<String> feedback = new ArrayList<>(5);
        Map<Character, Integer> frequency = new HashMap<>();

        for (char c : answer.toCharArray()) {
            frequency.put(c, frequency.getOrDefault(c, 0) + 1);
            feedback.add("red");
        }

        // First pass: mark greens
        for (int i = 0; i < 5; i++) {
            char g = guess.charAt(i), a = answer.charAt(i);
            if (g == a) {
                feedback.set(i, "green");
                frequency.put(g, frequency.get(g) - 1);
            }
        }

        // Second pass: mark yellows
        for (int i = 0; i < 5; i++) {
            if (feedback.get(i).equals("green")) continue;
            char g = guess.charAt(i);
            if (frequency.getOrDefault(g, 0) > 0) {
                feedback.set(i, "yellow");
                frequency.put(g, frequency.get(g) - 1);
            }
        }

        return feedback;
    }

    /**
     * Determines whether the game is over.
     *
     * @return true if game over, false otherwise
     */
    public boolean isGameOver() {
        return !guesses.isEmpty() && guesses.get(guesses.size() - 1).equals(answer);
    }

    /**
     * Clears previous guesses and selects a new random answer.
     */
    public void reset() {
        guesses.clear();
        pickRandomAnswer();
    }

    /**
     * Chooses a new random answer from the dictionary.
     */
    private void pickRandomAnswer() {
        answer = dictionary.get(rand.nextInt(dictionary.size()));
    }

    /**
     * Returns the current answer word.
     *
     * @return the answer
     */
    public String getAnswer() {
        return answer;
    }
}
