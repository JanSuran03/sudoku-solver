import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

public class Game extends Canvas {
    GameField game_field;
    static public final int
            OFFSET_X = 150,
            OFFSET_Y = 100,
            CELL_SIZE = 50,
            BORDER_WIDTH = 2,
            BW = BORDER_WIDTH,
            BW2 = BORDER_WIDTH * 2;

    static public final HashMap<Integer, int[]> ARROW_KEYS
            = new HashMap<>();

    static {
        ARROW_KEYS.put(37, new int[]{0, -1}); //LEFT
        ARROW_KEYS.put(38, new int[]{-1, 0}); //UP
        ARROW_KEYS.put(39, new int[]{0, 1}); //RIGHT
        ARROW_KEYS.put(40, new int[]{1, 0}); //BOTTOM
    }

    public void keyListener(KeyEvent ke) {
        Integer cell = game_field.selected_cell;
        char ch = ke.getKeyChar();
        if (cell != null) {
            int y = cell % GameField.SIZE;
            int x = cell / GameField.SIZE;
            int[] maybe_direction_change = ARROW_KEYS.get(ke.getKeyCode());
            // writes number
            if (Character.isDigit(ch) && ch != '0') {
                game_field.game_field[x][y].value = ((int) ch - (int) '0');
            } // moves the currently highlighted cell
            else if (maybe_direction_change != null) {
                game_field.game_field[x][y].is_selected = false;
                x = Math.min(GameField.SIZE - 1, Math.max(0, x + maybe_direction_change[0]));
                y = Math.min(GameField.SIZE - 1, Math.max(0, y + maybe_direction_change[1]));
                game_field.game_field[x][y].is_selected = true;
                game_field.selected_cell = x * GameField.SIZE + y;
            } else {
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
                        g.fillRect(OFFSET_X + j * CELL_SIZE + BW2,
                                OFFSET_Y + i * CELL_SIZE + BW2,
                                CELL_SIZE - BW2, CELL_SIZE - BW2);
                    }
                    if (c.value != null) {
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Calibri", Font.PLAIN, 40));
                        g.drawString(String.valueOf(c.value),
                                OFFSET_X + j * CELL_SIZE + BW + CELL_SIZE * 31 / 100,
                                OFFSET_Y + i * CELL_SIZE + BW + CELL_SIZE * 74 / 100);
                    }
                }
                g.setColor(Color.BLACK);
                g.drawRect(OFFSET_X + j * CELL_SIZE + BW2,
                        OFFSET_Y + i * CELL_SIZE + BW2,
                        CELL_SIZE - BW2, CELL_SIZE - BW2);
            }
    }

    static public void paintSquareBorders(Graphics g) {
        g.setColor(Color.BLACK);
        for (int i : new int[]{1, 2}) { // depends on GameField.SIZE as well
            g.fillRect(OFFSET_X + 3 * i * CELL_SIZE + 1, OFFSET_Y + BW2 - 2, BW2 - 1, GameField.SIZE * CELL_SIZE - BW2 + 5);
            g.fillRect(OFFSET_X + BW2 - 2, OFFSET_Y + 3 * i * CELL_SIZE + 1, GameField.SIZE * CELL_SIZE - BW2 + 5, BW2 - 1);
        }
    }

    @Override
    public void paint(Graphics g) {
        paintGameField(g);
        paintSquareBorders(g);
    }
}
