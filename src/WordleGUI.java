import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class WordleGUI {

    private final Map<Character, String> keyboardColors = new HashMap<>();


    public static void main(String[] args) {
        ArrayList<String> dictionary = Wordle.loadDictionary();
        WordleGUI gui = new WordleGUI();

        do {
            Wordle game = new Wordle(dictionary);
            gui.playGame(game);
        } while (gui.restartButtonClick());
    }

    /**
     * Runs one instance of the Wordle game loop, handling input and rendering.
     * @param game the Wordle game instance
     */
    public void playGame(Wordle game) {
        keyboardColors.clear();
        setupCanvas();

        String currentGuess = "";
        boolean needsRedrawn = true;

        while (game.isGameOver()) {
            if (needsRedrawn) {
                drawBoard(game.getGuesses(), currentGuess, game);
                needsRedrawn = false;
            }

            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                key = Character.toLowerCase(key);

                if (Character.isLetter(key) && currentGuess.length() < 5) {
                    currentGuess += Character.toLowerCase(key);
                    needsRedrawn = true;
                } else if (key == '\b' && !currentGuess.isEmpty()) {
                    currentGuess = currentGuess.substring(0, currentGuess.length() - 1);
                    needsRedrawn = true;
                } else if (key == '\n' || key == '\r' && currentGuess.length() == 5) {
                    if (game.isValidGuess(currentGuess)) {
                        game.addGuess(currentGuess);
                        updateKeyboardColors(currentGuess, game.getAnswer());
                        currentGuess = "";
                    } else {
                        showInvalidWordMessage();
                    }
                    needsRedrawn = true;
                }
            }

            StdDraw.pause(10);
        }

        drawBoard(game.getGuesses(), "", game);
        drawGameOver(game.getAnswer(), game.getGuesses().getLast().equals(game.getAnswer()));
    }

    /**
     * Displays a temporary error message if the user enters a word not in the dictionary.
     */
    public void showInvalidWordMessage() {
        StdDraw.setPenColor(Color.RED);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 24));
        StdDraw.text(0, -90, "Not in word list!");
        StdDraw.show();
        StdDraw.pause(800);
    }

    /**
     * Sets up the StdDraw canvas size, scale, and enables double buffering.
     */
    private void setupCanvas() {
        StdDraw.setCanvasSize(560, 560);
        StdDraw.setXscale(-100, 100);
        StdDraw.setYscale(-100, 100);
        StdDraw.enableDoubleBuffering();
    }

    /**
     * Updates keyboard colors based on guess feedback.
     * @param guess the user's guessed word
     * @param answer the correct answer word
     */
    private void updateKeyboardColors(String guess, String answer) {
        ArrayList<String> feedback = Wordle.checkGuess(guess, answer);

        for (int i = 0; i < guess.length(); i++) {
            char letter = guess.charAt(i);
            String newColor = feedback.get(i);
            String currentColor = keyboardColors.getOrDefault(letter, "none");

            if (newColor.equals("green") ||
                    (newColor.equals("yellow") && !currentColor.equals("green")) ||
                    (newColor.equals("gray") && currentColor.equals("none"))) {
                keyboardColors.put(letter, newColor);
            }
        }
    }

    /**
     * Draws the Wordle board with guesses, feedback, and the current guess.
     * @param guesses list of completed guesses
     * @param currentGuess the current guess in progress
     * @param game the Wordle game instance
     */
    public void drawBoard(ArrayList<String> guesses, String currentGuess, Wordle game) {
        StdDraw.clear(Color.WHITE);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 24));
        StdDraw.text(0, 93, "Wordle");

        double boxSize = 9;
        double startX = -40, startY = 77;

        for (int row = 0; row < 6; row++) {
            String guess;
            if (row < guesses.size()) {
                guess = guesses.get(row);
            } else if (row == guesses.size()) {
                guess = currentGuess;
            } else {
                guess = "";
            }

            ArrayList<String> feedback;
            if (row < guesses.size()) {
                feedback = Wordle.checkGuess(guess, game.getAnswer());
            } else {
                feedback = null;
            }

            for (int col = 0; col < 5; col++) {
                double x = startX + col * 20;
                double y = startY - row * 20;

                StdDraw.setPenColor(Color.BLACK);
                StdDraw.square(x, y, boxSize);

                if (col < guess.length()) {
                    char ch = guess.charAt(col);

                    if (feedback != null) {
                        switch (feedback.get(col)) {
                            case "green" -> StdDraw.setPenColor(new Color(106, 170, 100));
                            case "yellow" -> StdDraw.setPenColor(new Color(201, 180, 88));
                            case "gray" -> StdDraw.setPenColor(new Color(120, 124, 126));
                        }
                        StdDraw.filledSquare(x, y, boxSize);
                        StdDraw.setPenColor(Color.WHITE);
                    }
                    StdDraw.setFont(new Font("Arial", Font.BOLD, 18));
                    StdDraw.text(x, y, String.valueOf(Character.toUpperCase(ch)));
                }
            }
        }

        drawKeyboard();
        StdDraw.show();
    }

    /**
     * Draws the on-screen keyboard with color feedback for each letter.
     */
    public void drawKeyboard() {
        String[] rows = {"qwertyuiop", "asdfghjkl", "zxcvbnm"};
        double keyWidth = 16, keyHeight = 14;
        double baseX = -80, baseY = -49;

        for (int r = 0; r < rows.length; r++) {
            String row = rows[r];
            double offsetX;
            if (r == 1) {
                offsetX = 11;
            } else if (r == 2) {
                offsetX = 26;
            } else {
                offsetX = 0;
            }

            double y = baseY - r * 18;

            for (int i = 0; i < row.length(); i++) {
                char letter = row.charAt(i);
                double x = baseX + offsetX + i * 18;
                String color = keyboardColors.getOrDefault(letter, "none");

                switch (color) {
                    case "green" -> StdDraw.setPenColor(new Color(106, 170, 100));
                    case "yellow" -> StdDraw.setPenColor(new Color(201, 180, 88));
                    case "gray" -> StdDraw.setPenColor(new Color(120, 124, 126));
                    default -> StdDraw.setPenColor(Color.WHITE);
                }

                StdDraw.filledRectangle(x, y, keyWidth / 2, keyHeight / 2);
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.rectangle(x, y, keyWidth / 2, keyHeight / 2);
                StdDraw.setFont(new Font("Arial", Font.BOLD, 16));
                StdDraw.text(x, y, String.valueOf(letter));
            }
        }
    }

    /**
     * Displays the end-of-game message indicating win or loss.
     * @param answer the correct answer word
     * @param win true if the player guessed the word correctly
     */
    public void drawGameOver(String answer, boolean win) {
        StdDraw.setFont(new Font("Arial", Font.BOLD, 36));

        String message;
        if (win) {
            StdDraw.setPenColor(Color.GREEN);
            message = "You won!";
        } else {
            StdDraw.setPenColor(Color.RED);
            message = "You lost. Answer: " + answer.toUpperCase();
        }

        StdDraw.text(0, 0, message);
        StdDraw.show();
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 18));
    }

    /**
     * Draws a restart button and waits for the user to click it.
     * @return true if the button is clicked
     */
    public boolean restartButtonClick() {
        double buttonX = 0;
        double buttonY = -65;
        double buttonWidth = 50;
        double buttonHeight = 30;

        StdDraw.setPenColor(Color.darkGray);
        StdDraw.filledRectangle(buttonX, buttonY, buttonWidth / 2, buttonHeight / 2);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 24));
        StdDraw.text(buttonX, buttonY, "Restart");
        StdDraw.show();

        while (true) {
            if (StdDraw.isMousePressed()) {
                double mx = StdDraw.mouseX();
                double my = StdDraw.mouseY();
                if (mx >= buttonX - buttonWidth / 2 && mx <= buttonX + buttonWidth / 2 &&
                        my >= buttonY - buttonHeight / 2 && my <= buttonY + buttonHeight / 2) {
                    StdDraw.pause(200);
                    return true;
                }
            }
            StdDraw.pause(10);
        }
    }
}
