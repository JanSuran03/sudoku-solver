import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class App extends JFrame {

    Game root;

    public App() {
        setTitle("SUDOKU");
        setBounds(100, 100, 1000, 700);
        root = new Game();
        add(root);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void start() {
        setVisible(true);
    }

}
