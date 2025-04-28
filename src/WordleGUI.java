import java.awt.*;

public class WordleGUI {


    public static void main(String[] args){
        draw();
    }



    public static void draw(){
        // Initialize necessary items
        StdDraw.clear();
        StdDraw.setXscale(-100, 100);
        StdDraw.setYscale(-100, 100);
        StdDraw.setCanvasSize(1024, 1024);
        StdDraw.setPenColor(Color.BLACK);

        // Build the grid
        double x_cord = -40;
        double y_cord = 85;
        double boxSize = 9;

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                double x = x_cord + col * 20;
                double y = y_cord - row * 20;
                StdDraw.square(x, y, boxSize);
            }
        }

        String[] keyboardRows = {"qwertyuiop", "asdfghjkl", "zxcvbnm"
        };

        double x_keys = -70;
        double y_keys = -40;
        double keySize = 7;















    }








}
