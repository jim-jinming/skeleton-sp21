package game2048;

import java.util.Arrays;
import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author JinMing YOU
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;
        board.setViewingPerspective(side);
        for (int col = 0; col < size(); col++){
            for (int row = size()-1; row >= 0; row--) {
                if (emptyTileExistsIn(col, row)) {
                    Tile nextTile = findNextNonEmptyTile(col, row);
                    if (nextTile != null) {
                        board.move(col, row, nextTile);
                        changed = true;
                    } else {
                        // there is no other non-empty tile
                        // break this loop of column
                        break;
                    }
                    // after the move to fill empty tile is done
                }
                Tile nextSameTile = findNextSameValueTile(col, row);
                if(nextSameTile != null) {
                    board.move(col, row, nextSameTile);
                    changed = true;
                    score += nextSameTile.value()*2;
                }
            }
        }

        board.setViewingPerspective(Side.NORTH);
        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }
    /**
     * Checks if the tile is an empty tile, return the Tile coordinates,
     * or return null.
     */
    public boolean emptyTileExistsIn(int Col, int Row) {
        Tile targetTile = board.tile(Col, Row);
        // if there is an empty tile, return its position
        if(targetTile == null) {
            return true;
        }
        return false;
    }
    /**
     * Find the next non-empty tile, return the Tile coordinates,
     * or return null.
     * */
    public Tile findNextNonEmptyTile(int colPrev, int rowPrev) {
        // iterate through every Row, starting from the previous row, stop at the first row
        for (int i = rowPrev-1; i >= 0; i--) {
            Tile targetTile = board.tile(colPrev, i);
            // if there is a non-empty tile, return its position
            if(targetTile != null) {
                return targetTile;
            }
        }
        return null;
    }
    /**
     * Find the next non-empty tile, has the value v, return its coordinates,
     * or return null.
     * */
    public Tile findNextSameValueTile(int prevCol, int prevRow) {
        int v = board.tile(prevCol, prevRow).value();
        for (int i = prevRow-1; i >= 0; i--) {
            Tile targetTile = board.tile(prevCol, i);
            if(targetTile != null) {
                if(targetTile.value() == v) {
                    return targetTile;
                } else {
                    // there is a non-empty tile down the road, but not of the same value
                    return null;
                }
            }
        }
        return null;
    }


    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /* Helper method: tell me what you want,
     * Iterate through given Board,
     * to find given char */

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // b.size RETURNS the length of edge of the Matrix b
        // iterate through y0, y1, y2, y3
        // by iterate through x0, x1, x2, x3
        int edgeLength = b.size();
        for (int y = 0; y < edgeLength; y++) {
            for (int x = 0; x < edgeLength; x++) {
                if (b.tile(y, x) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        int edgeLength = b.size();
        for (int y = 0; y < edgeLength; y++) {
            for (int x = 0; x < edgeLength; x++) {
                if (b.tile(y, x) != null) {
                    if (b.tile(y, x).value() == MAX_PIECE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        if (emptySpaceExists(b)) {
            return true;
        }
        // for each tile check if adjacent tiles has the same value
        // avoid repeated look-up
        // start from bottom-left, only look top-right
        int edgeLength = b.size();
        for (int y = 0; y < edgeLength; y++) {
            for (int x = 0; x < edgeLength; x++) {
                int dx = x + 1;
                if (dx < edgeLength) {
                    if (b.tile(y, x).value()==b.tile(y, dx).value()) {
                        return true;
                    }
                }
                int dy = y + 1;
                if (dy < edgeLength) {
                    if (b.tile(y, x).value()==b.tile(dy, x).value()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
