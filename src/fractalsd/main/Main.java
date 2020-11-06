package fractalsd.main;

import javax.swing.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Fractal SD");
        frame.setContentPane(new GUIMain().getMainPanel());
        frame.setBounds(300, 40, 950, 780);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //frame.pack();
        frame.setVisible(true);
    }
}
