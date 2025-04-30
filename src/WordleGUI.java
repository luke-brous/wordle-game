import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class WordleGUI {

    private final Map<Character, String> keyboardColors = new HashMap<>();

    public static void main(String[] args) {
        ArrayList<String> dictionary = Wordle.loadDictionary();
        Wordle game = new Wordle(dictionary);
        new WordleGUI().playGame(game);
    }


    public void playGame(Wordle game) {
        setupCanvas();

        String currentGuess = "";
        boolean needsRedrawn = true;

        while (!game.isGameOver()) {
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

    public void showInvalidWordMessage() {
        StdDraw.setPenColor(Color.RED);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 16));
        StdDraw.text(0, -90, "Not in word list!");
        StdDraw.show();
        StdDraw.pause(800);
    }


    private void setupCanvas() {
        StdDraw.setCanvasSize(560, 560);
        StdDraw.setXscale(-100, 100);
        StdDraw.setYscale(-100, 100);
        StdDraw.enableDoubleBuffering();
    }

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

    public void drawBoard(ArrayList<String> guesses, String currentGuess, Wordle game) {
        StdDraw.clear(Color.WHITE);

        double boxSize = 9;
        double startX = -40, startY = 85;

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

                    StdDraw.text(x, y, String.valueOf(Character.toUpperCase(ch)));
                }
            }
        }

        drawKeyboard();
        StdDraw.show();
    }


    public void drawKeyboard() {
        String[] rows = {"qwertyuiop", "asdfghjkl", "zxcvbnm"};
        double keyWidth = 16, keyHeight = 14;
        double baseX = -80, baseY = -45;

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
                StdDraw.text(x, y, String.valueOf(letter));
            }
        }
    }


    public void drawGameOver(String answer, boolean win) {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 28));

        String message = win ? "You won!" : "You lost. Answer: " + answer.toUpperCase();
        StdDraw.text(0, 0, message);

        StdDraw.show();
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 14));
    }


    public boolean restartButtonClick{
        double buttonX = 80;
        double buttonY = -80;
        double buttonWidth = 10;
        double buttonHeight = 10;

        StdDraw.setPenColor(Color.CYAN);
        StdDraw.filledRectangle(buttonX, buttonY, buttonWidth / 2, buttonHeight / 2);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(buttonX, buttonY, "Restart");
        StdDraw.show();

        while(true) {
            if (StdDraw.isMousePressed()){
                double mouseX = StdDraw.mouseX();
                double mouseY = StdDraw.mouseY();
                if (mouseX >= buttonX - buttonWidth && mouseX <= buttonX - buttonWidth &&
                    mouseY >= buttonY - buttonHeight && mouseY <= buttonY - buttonHeight){

                    StdDraw.pause(200);
                    return true;

                }
            }
            StdDraw.pause(10);
        }




    }


}






