import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class WordleTest {




    @Test
    public void isValidGuess_ValidWord() {
        ArrayList<String> dict = new ArrayList<>(Arrays.asList("apple", "brick", "zebra"));
        Wordle w = new Wordle(dict);
        assertTrue(w.isValidGuess("apple"));
        assertTrue(w.isValidGuess("brick"));
        assertTrue(w.isValidGuess("zebra"));
    }

    @Test
    public void isValidGuess_InvalidLength() {
        ArrayList<String> dict = new ArrayList<>(Arrays.asList("apple", "brick"));
        Wordle w = new Wordle(dict);
        assertFalse(w.isValidGuess("app"));
        assertFalse(w.isValidGuess("bricks"));
        assertFalse(w.isValidGuess(""));
    }

    @Test
    public void isValidGuess_NotInDictionary() {
        ArrayList<String> dict = new ArrayList<>(Arrays.asList("apple", "brick"));
        Wordle w = new Wordle(dict);
        assertFalse(w.isValidGuess("reach"));
        assertFalse(w.isValidGuess("crazy"));
        assertFalse(w.isValidGuess("lucky"));
    }
}
