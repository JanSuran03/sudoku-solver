import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        App app = new App();
        app.start();
        Cell c = new Cell();
        app.root.game_field.game_field[4][2] = c;
        c.value = 5;
        app.root.repaint();
    }
}
