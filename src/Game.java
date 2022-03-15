import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Game extends Canvas {
    GameField game_field;
    static public final int
            OFFSET_X = 150,
            OFFSET_Y = 100,
            CELL_SIZE = 50;

    public void keyListener(KeyEvent ke) {
        Integer cell = game_field.selected_cell;
        System.out.println(cell + " ...");
        char ch = ke.getKeyChar();
        if (cell != null) {
            int y = cell % GameField.SIZE;
            int x = cell / GameField.SIZE;
            System.out.println(x + " " + y + " " + ch);
            if (Character.isDigit(ch) && ch != '0') {
                game_field.game_field[x][y].value = ((int) ch - (int)'0');
            }else{
                game_field.game_field[x][y].value = null;
            }

            repaint();
        }
    }

    public void init() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                keyListener(ke);
            }
        });
    }

    public void mouseReleasedListener(MouseEvent me) {
        int raw_x = me.getX(),
                raw_y = me.getY(),
                x = raw_x - OFFSET_X,
                y = raw_y - OFFSET_Y,
                as_cell_x = x <= 0 ? -1 : (x) / CELL_SIZE,
                as_cell_y = y <= 0 ? -1 : (y) / CELL_SIZE;
        if (as_cell_x >= 0 && as_cell_x < GameField.SIZE
                && as_cell_y >= 0 && as_cell_y < GameField.SIZE) {
            if (game_field.selected_cell != null) {
                int sc_x = game_field.selected_cell % GameField.SIZE;
                int sc_y = game_field.selected_cell / GameField.SIZE;
                game_field.game_field[sc_y][sc_x].is_selected = false;
            }
            game_field.selected_cell = as_cell_x + as_cell_y * 9;
            game_field.game_field[as_cell_y][as_cell_x].is_selected = true;
            repaint();
        }
    }

    public Game() {
        game_field = new GameField();
        init();
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                mouseReleasedListener(me);
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });
    }

    public void paintGameField(Graphics g) {
        for (int i = 0; i < GameField.SIZE; i++)
            for (int j = 0; j < GameField.SIZE; j++) {
                Cell c = game_field.game_field[i][j];

                if (c != null) {
                    if (c.is_selected) {
                        g.setColor(new Color(255, 0, 0, 50));
                        g.fillRect(OFFSET_X + j * CELL_SIZE + 2,
                                OFFSET_Y + i * CELL_SIZE + 2,
                                CELL_SIZE - 2, CELL_SIZE - 2);
                    }
                    if (c.value != null) {
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Calibri", Font.PLAIN, 40));
                        g.drawString(String.valueOf(c.value),
                                OFFSET_X + j * CELL_SIZE + 2 + CELL_SIZE * 31 / 100,
                                OFFSET_Y + i * CELL_SIZE + 2 + CELL_SIZE * 74 / 100);
                    }
                }
                g.setColor(Color.BLACK);
                g.drawRect(OFFSET_X + j * CELL_SIZE + 2,
                        OFFSET_Y + i * CELL_SIZE + 2,
                        CELL_SIZE - 2, CELL_SIZE - 2);
            }
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        paintGameField(g);
    }
}
