package utils;

import static utils.Constants.*;

public class Utils {

    /**
     * Converts position into rows and columns of the chessboard
     * @param position - Position we want to convert
     * @return - Array with 1st element as row and 2nd element as column
     */
    public static int[] convertToCoordinates(int position) {
        int[] coordinates = new int[2];
        // Row
        coordinates[0] = position / Constants.NUM_ROWS;
        coordinates[1] = position % Constants.NUM_COLS;
        return coordinates;
    }

    /**
     * Converts row and column of the chessboard into a position
     * @param row - Row of the chessboard
     * @param col - Column of the chessboard
     * @return - Position
     */
    public static int convertToPosition(int row, int col) {
        if((row < 0 || row >= NUM_ROWS) || (col < 0 || col >= NUM_COLS))
            return -1;

        return row * Constants.NUM_ROWS + col;
    }

    /**
     * Checks whether position lies on chessboard or not
     * @param position - Position we want to check the validity of
     * @return - True if position lies on the chessboard else false
     */
    public static boolean assertPosition(int position) {
        return 0 <= position && position < NUM_ROWS * NUM_COLS;
    }

    /**
     * Given two positions, returns the manhattan distance between their rows
     * @param oldPosition - Position 1
     * @param newPosition - Position 2
     * @return - Manhattan distance between both position rows
     */
    public static int getRowDiff(int oldPosition, int newPosition) {
        int[] newCoordinates = Utils.convertToCoordinates(newPosition);
        int[] oldCoordinates = Utils.convertToCoordinates(oldPosition);
        return Math.abs(newCoordinates[0] - oldCoordinates[0]);
    }

    /**
     * Given two positions, returns the manhattan distance between their columns
     * @param oldPosition - Position 1
     * @param newPosition - Position 2
     * @return - Manhattan distance between both position columns
     */
    public static int getColDiff(int oldPosition, int newPosition) {
        int[] newCoordinates = Utils.convertToCoordinates(newPosition);
        int[] oldCoordinates = Utils.convertToCoordinates(oldPosition);
        return Math.abs(newCoordinates[1] - oldCoordinates[1]);
    }

    /**
     * Return the opposite color of the input color
     * @param currentColor - input color
     * @return - Returns BLACK if input is WHITE else WHITE if input is BLACK
     */
    public static Constants.Color getOpposingColor(Constants.Color currentColor) {
        if(currentColor == Constants.Color.WHITE) {
            return Constants.Color.BLACK;
        }
        return Constants.Color.WHITE;
    }

    /**
     * Checks which of the two inputs are greater
     * @param num1 - Input number 1
     * @param num2 - Input number 2
     * @return - Returns POSITIVE if input 1 is greater else returns NEGATIVE
     */
    public static int isGreater(int num1, int num2) {
        if(num1 >= num2) {
            return POSITIVE;
        }
        return NEGATIVE;
    }

}
