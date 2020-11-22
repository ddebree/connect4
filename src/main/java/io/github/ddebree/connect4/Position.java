package io.github.ddebree.connect4;

public class Position {

    public static final int WIDTH = 7;  // Height of the board
    public static final int HEIGHT = 6; // Width of the board

    private final int[][] board = new int[WIDTH][HEIGHT]; // 0 if cell is empty, 1 for first player and 2 for second player.
    private final int[] height = new int[WIDTH];        // number of stones per column
    private int moves;       // number of moves played since the beginning of the game.

    /*
     * Default constructor, build an empty position.
     */
    private Position() {
    }

    /**
     * Plays a sequence of successive played columns, mainly used to initialize a board.
     * @param sequence: a sequence of digits corresponding to the 1-based index of the column played.
     *
     * @return number of played moves. Processing will stop at first invalid move that can be:
     *           - invalid character (non digit, or digit >= WIDTH)
     *           - playing a colum the is already full
     *           - playing a column that makes an aligment (we only solve non).
     *         Caller can check if the move sequence was valid by comparing the number of
     *         processed moves to the length of the sequence.
     */
    public static Position fromInputSeq(final String sequence) throws InvalidMoveException {
        final Position position = new Position();
        for (char c : sequence.trim().toCharArray()) {
            int col = c - '1';
            if (col < 0 || col >= Position.WIDTH
                    || !position.canPlay(col)
                    || position.isWinningMove(col)) {
                throw new InvalidMoveException();
            }
            position.play(col);
        }
        return position;
    }

    public Position nextPostion(int col) {
        final Position newPosition = new Position();
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                newPosition.board[x][y] = this.board[x][y];
            }
        }
        for (int i = 0; i < WIDTH; i++) {
            newPosition.height[i] = this.height[i];
        }
        newPosition.moves = this.moves;
        newPosition.play(col);
        return newPosition;
    }

    /**
     * Indicates whether a column is playable.
     * @param col: 0-based index of column to play
     * @return true if the column is playable, false if the column is already full.
     */
    public boolean canPlay(int col) {
        return height[col] < HEIGHT;
    }

    /**
     * Plays a playable column.
     * This function should not be called on a non-playable column or a column making an alignment.
     *
     * @param col: 0-based index of a playable column.
     */
    private void play(int col) {
        board[col][height[col]] = 1 + moves%2;
        height[col]++;
        moves++;
    }

    /**
     * Indicates whether the current player wins by playing a given column.
     * This function should never be called on a non-playable column.
     * @param col: 0-based index of a playable column.
     * @return true if current player makes an alignment by playing the corresponding column col.
     */
    public boolean isWinningMove(int col) {
        int current_player = 1 + moves%2;
        // check for vertical alignments
        if(height[col] >= 3
                && board[col][height[col]-1] == current_player
                && board[col][height[col]-2] == current_player
                && board[col][height[col]-3] == current_player)
            return true;

        for (int dy = -1; dy <=1; dy++) {    // Iterate on horizontal (dy = 0) or two diagonal directions (dy = -1 or dy = 1).
            int nb = 0;                       // counter of the number of stones of current player surronding the played stone in tested direction.
            for(int dx = -1; dx <=1; dx += 2) // count continuous stones of current player on the left, then right of the played column.
                for(int x = col+dx, y = height[col]+dx*dy; x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT && board[x][y] == current_player; nb++) {
                    x += dx;
                    y += dx*dy;
                }
            if (nb >= 3) return true; // there is an aligment if at least 3 other stones of the current user
            // are surronding the played stone in the tested direction.
        }
        return false;
    }

    /**
     * @return number of moves played from the beginning of the game.
     */
    public int nbMoves() {
        return moves;
    }

}
