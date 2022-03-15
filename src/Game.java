import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

public class Game extends Canvas {
    GameField game_field;
    boolean recent_solve_tries = false;
    public static final int
            OFFSET_X = 150,
            OFFSET_Y = 100,
            CELL_SIZE = 50,
            BORDER_WIDTH = 2,
            BW = BORDER_WIDTH,
            BW2 = BORDER_WIDTH * 2,
            SOLVE_X = 40, SOLVE_Y = 20,
            SOLVE_WIDTH = 100, SOLVE_HEIGHT = 40,
            RESET_X = 250, RESET_Y = SOLVE_Y,
            RESET_WIDTH = SOLVE_WIDTH, RESET_HEIGHT = SOLVE_HEIGHT;

    public static final HashMap<Integer, int[]> ARROW_KEYS
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
        int kc = ke.getKeyCode();

        int y = cell % GameField.SIZE;
        int x = cell / GameField.SIZE;
        int[] maybe_direction_change = ARROW_KEYS.get(kc);
        // writes number
        if (Character.isDigit(ch) && ch != '0') {
            game_field.game_field[x][y].value = ((int) ch - (int) '0');
            game_field.game_field[x][y].filled_by_player = true;
            recent_solve_tries = false;
        } // moves the currently highlighted cell
        else if (maybe_direction_change != null) {
            game_field.game_field[x][y].is_selected = false;
            x = Math.min(GameField.SIZE - 1, Math.max(0, x + maybe_direction_change[0]));
            y = Math.min(GameField.SIZE - 1, Math.max(0, y + maybe_direction_change[1]));
            game_field.game_field[x][y].is_selected = true;
            game_field.selected_cell = x * GameField.SIZE + y;
        } else if (kc == 10) { // enter -> solve
            if (!Solver.solve(game_field))
                recent_solve_tries = true;
        } else if (kc == 27) {
            for (int i = 0; i < GameField.SIZE; i++)
                for (int j = 0; j < GameField.SIZE; j++)
                    if (!game_field.game_field[i][j].filled_by_player) {
                        game_field.game_field[i][j].filled_by_player = true;
                        game_field.game_field[i][j].value = null;
                    }
            recent_solve_tries = false;
        } else {
            game_field.game_field[x][y].value = null;
            recent_solve_tries = false;
        }
        repaint();
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
        if (Util.between(as_cell_x, 0, GameField.SIZE)
                && Util.between(as_cell_y, 0, GameField.SIZE)) {
            if (game_field.selected_cell != null) {
                int sc_x = game_field.selected_cell % GameField.SIZE;
                int sc_y = game_field.selected_cell / GameField.SIZE;
                game_field.game_field[sc_y][sc_x].is_selected = false;
            }
            game_field.selected_cell = as_cell_x + as_cell_y * 9;
            game_field.game_field[as_cell_y][as_cell_x].is_selected = true;
            repaint();
        } else if (Util.between(raw_x, SOLVE_X, SOLVE_X + SOLVE_WIDTH)
                && Util.between(raw_y, SOLVE_Y, SOLVE_Y + SOLVE_HEIGHT)) {
            if (!Solver.solve(game_field))
                recent_solve_tries = true;
            repaint();
        } else if (Util.between(raw_x, RESET_X, RESET_X + RESET_WIDTH)
                && Util.between(raw_y, RESET_Y, RESET_WIDTH + RESET_HEIGHT)) {
            for (int i = 0; i < GameField.SIZE; i++)
                for (int j = 0; j < GameField.SIZE; j++) {
                    game_field.game_field[i][j] = new Cell();
                }
            recent_solve_tries = false;
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
                        g.setColor(new Color(0, 255, 0, 70));
                        g.fillRect(OFFSET_X + j * CELL_SIZE + BW2,
                                OFFSET_Y + i * CELL_SIZE + BW2,
                                CELL_SIZE - BW2, CELL_SIZE - BW2);
                    }
                    if (c.value != null) {
                        if (c.filled_by_player)
                            g.setColor(Color.BLACK);
                        else
                            g.setColor(new Color(0, 0, 0, 85));
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

    public void maybePaintNotSolvable(Graphics g) {
        if (recent_solve_tries) {
            g.setColor(Color.RED);
            g.setFont(new Font("Calibri", Font.PLAIN, 40));
            g.drawString("Can't solve.", 400, 70);
        }
    }

    public static void paintSquareBorders(Graphics g) {
        g.setColor(Color.BLACK);
        for (int i : new int[]{1, 2}) { // depends on GameField.SIZE as well
            g.fillRect(OFFSET_X + 3 * i * CELL_SIZE + 1, OFFSET_Y + BW2 - 2, BW2 - 1, GameField.SIZE * CELL_SIZE - BW2 + 5);
            g.fillRect(OFFSET_X + BW2 - 2, OFFSET_Y + 3 * i * CELL_SIZE + 1, GameField.SIZE * CELL_SIZE - BW2 + 5, BW2 - 1);
        }
    }

    static class CanvasButton {
        CanvasButton() {
        }

        public static void paint(Graphics g, String s, int x, int y, int width,
                                 int height, int border_radius) {
            g.setColor(Color.BLACK);
            g.drawRoundRect(x, y, width, height, border_radius, border_radius);
            g.setFont(new Font("Calibri", Font.PLAIN, 25));
            g.drawString(s, x + 5, y + 30);
        }
    }

    @Override
    public void paint(Graphics g) {
        paintGameField(g);
        paintSquareBorders(g);
        CanvasButton.paint(g, "SOLVE", SOLVE_X, SOLVE_Y, SOLVE_WIDTH, SOLVE_HEIGHT, 10);
        CanvasButton.paint(g, "RESET", RESET_X, RESET_Y, RESET_WIDTH, RESET_HEIGHT, 10);
        maybePaintNotSolvable(g);
    }
}
