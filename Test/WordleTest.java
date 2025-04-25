import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class WordleTest {

    @Test
    public void checkGuess_AllCorrect() {
        ArrayList<String> dict = new ArrayList<>(Arrays.asList("apple", "stone", "brave"));
        Wordle w1 = new Wordle("apple", dict);
        Wordle w2 = new Wordle("stone", dict);
        Wordle w3 = new Wordle("brave", dict);

        assertEquals(Arrays.asList("green", "green", "green", "green", "green"),
                w1.checkGuess("apple", w1.getAnswer()));
        assertEquals(Arrays.asList("green", "green", "green", "green", "green"),
                w2.checkGuess("stone", w2.getAnswer()));
        assertEquals(Arrays.asList("green", "green", "green", "green", "green"),
                w3.checkGuess("brave", w3.getAnswer()));
    }


    @Test
    public void checkGuess_Mixed() {
        ArrayList<String> dict = new ArrayList<>(Arrays.asList("apple", "plate", "pleat"));
        Wordle w = new Wordle("apple", dict);

        // 1) 'ample' → m is not in 'apple'
        assertEquals(Arrays.asList("green", "red",   "green", "green", "green"),
                w.checkGuess("ample", w.getAnswer()));

        // 2) 'paple' → p and a are present but mis-placed
        assertEquals(Arrays.asList("yellow","yellow","green", "green", "green"),
                w.checkGuess("paple", w.getAnswer()));

        // 3) 'pleap' → every letter appears, but none in correct spot
        assertEquals(Arrays.asList("yellow","yellow","yellow","yellow","yellow"),
                w.checkGuess("pleap", w.getAnswer()));
    }

    @Test
    public void isValidGuess_ValidWord() {
        ArrayList<String> dict = new ArrayList<>(Arrays.asList("apple", "brick", "zebra"));
        assertTrue(Wordle.isValidGuess("apple", dict));
        assertTrue(Wordle.isValidGuess("brick", dict));
        assertTrue(Wordle.isValidGuess("zebra", dict));
    }

    @Test
    public void isValidGuess_InvalidLength() {
        ArrayList<String> dict = new ArrayList<>(Arrays.asList("apple", "brick"));
        assertFalse(Wordle.isValidGuess("app", dict));
        assertFalse(Wordle.isValidGuess("bricks", dict));
        assertFalse(Wordle.isValidGuess("", dict));
    }

    @Test
    public void isValidGuess_NotInDictionary() {
        ArrayList<String> dict = new ArrayList<>(Arrays.asList("apple", "brick"));
        assertFalse(Wordle.isValidGuess("reach", dict));
        assertFalse(Wordle.isValidGuess("crazy", dict));
        assertFalse(Wordle.isValidGuess("lucky", dict));
    }
}
