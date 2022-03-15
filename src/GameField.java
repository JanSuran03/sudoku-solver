public class GameField {

    public static final int SIZE = 9;
    public Cell[][] game_field;
    public Integer selected_cell;

    public GameField() {
        Cell[][] temp = new Cell[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                temp[i][j] = new Cell();
        game_field = temp;
    }

    public boolean setCell(int row, int col, int number) {
        if (game_field[row][col].value == null) {
            game_field[row][col].value = number;
            return true;
        }
        return false;
    }
}
