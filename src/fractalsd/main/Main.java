package fractalsd.main;

import javax.swing.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * @author Tiago Murteira   21087
 * @author João Ramos       21397
 */
public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Fractal SD");
        frame.setContentPane(new GUIMain().getMainPanel());
        frame.setBounds(200, 40, 1200, 680);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
