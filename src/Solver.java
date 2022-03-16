import java.util.HashSet;

public class Solver {

    static boolean isSafeDefault(Cell[][] game_field) {
        System.out.println("SAFE DEFAULT?");
        for (int row = 0; row < GameField.SIZE; row++) {
            HashSet<Integer> distinct = new HashSet<>();
            for (int col = 0; col < GameField.SIZE; col++) {
                if (game_field[row][col].value != null
                        && distinct.contains(game_field[row][col].value)) {
                    return false;
                }
                distinct.add(game_field[row][col].value);
            }
        }
        System.out.println("ROWS SAFE!");
        for (int row = 0; row < GameField.SIZE; row++) {
            HashSet<Integer> distinct = new HashSet<>();
            for (int col = 0; col < GameField.SIZE; col++) {
                if (game_field[col][row].value != null
                        && distinct.contains(game_field[col][row].value)) {
                    return false;
                }
                distinct.add(game_field[col][row].value);
            }
        }
        System.out.println("COLS SAFE!");
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                HashSet<Integer> distinct = new HashSet<>();
                for (int k = 0; k < 3; k++)
                    for (int l = 0; l < 3; l++) {
                        if (game_field[3 * i + k][3 * j + l].value != null
                                && distinct.contains(game_field[3 * i + k][3 * j + l].value)) {
                            return false;
                        }
                        distinct.add(game_field[3 * i + k][3 * j + l].value);
                    }
            }
        System.out.println("ALL SAFE!");
        return true;
    }

    static boolean isSafeRow(Cell[][] game_field, int row, int value) {
        for (int i = 0; i < GameField.SIZE; i++)
            if (game_field[row][i].value != null
                    && game_field[row][i].value == value)
                return false;
        return true;
    }

    static boolean isSafeCol(Cell[][] game_field, int col, int value) {
        for (int i = 0; i < GameField.SIZE; i++)
            if (game_field[i][col].value != null
                    && game_field[i][col].value == value)
                return false;
        return true;
    }

    static boolean isSafeSquare(Cell[][] game_field, int row, int col, int value) {
        int first_row = (row / 3) * 3;
        int first_col = (col / 3) * 3;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                if (game_field[first_row + i][first_col + j].value != null
                        && game_field[first_row + i][first_col + j].value == value)
                    return false;
            }
        return true;
    }

    static boolean isSafe(Cell[][] game_field, int row, int col, int value) {
        return isSafeRow(game_field, row, value)
                && isSafeCol(game_field, col, value)
                && isSafeSquare(game_field, row, col, value);
    }

    static boolean solveSudoku(Cell[][] game_field, int row, int col) {
        if (col == GameField.SIZE) {
            row++;
            col = 0;
        }
        if (row == GameField.SIZE) {
            return true;
        }

        Integer v = game_field[row][col].value;
        if (v != null && v >= 1 && v <= GameField.SIZE)
            return solveSudoku(game_field, row, col + 1);

        for (int try_this = 1; try_this < GameField.SIZE + 1; try_this++) {
            if (isSafe(game_field, row, col, try_this)) {
                game_field[row][col].value = try_this;
                game_field[row][col].filled_by_player = false;
                if (solveSudoku(game_field, row, col + 1))
                    return true;
                game_field[row][col].value = null;
                game_field[row][col].filled_by_player = true;
            }
        }
        return false;
    }

    public static boolean solve(GameField game_field) {
        return isSafeDefault(game_field.game_field)
                && solveSudoku(game_field.game_field, 0, 0);
    }

    public Solver() {
    }
}
