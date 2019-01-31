package pieces;

import GameFiles.Board;
import utils.Constants;
import utils.Utils;

import java.util.ArrayList;

import static utils.Utils.convertToCoordinates;

public class Bishop extends Piece {

    /**
     * Creates a new Bishop for the chess board
     * @param color - Color of the newly created piece
     * @param position - Position the piece should be placed at
     */
    public Bishop(Constants.Color color, int position) {
        super(color, position);
    }

    /**
     * Checks if it's possible for piece to reach new position
     * Performs the following sub tasks
     * - Checks if position is valid or not
     * - If position is valid, checks if it's possible for current piece to reach that position in one turn
     * - If above is possible, checks if there is any obstacle in its path or not
     * @param board - The board on which the current game is being played
     * @param newPosition - The position the player is trying to move the piece to
     * @return - True if piece can be moved to the new position else false
     */
    @Override
    protected boolean isValidMove(Board board, int newPosition) {
        if(board.isValidPosition(this, newPosition)) {
            // Check if its possible for bishop to reach that position given current position
            // Bishop can only reach it if rowDiff == colDiff and both are greater than 0
            int rowDiff = Utils.getRowDiff(position, newPosition);
            int colDiff = Utils.getColDiff(position, newPosition);
            if(rowDiff == colDiff && rowDiff > 0) {
                // Bishop is moving diagonally. Check if there's an obstacle in its path or not
                return !hasObstacle(board, newPosition);
            }
            return false;
        }
        return false;
    }

    @Override
    protected boolean hasObstacle(Board board, int newPosition) {
        return Bishop.hasObstacle(board, position, newPosition);
    }

    /**
     * Checks if there is an obstacle in the path of the piece and the new position
     * Obstacles come in the form of other positions
     * Assumes the position is valid and that its possible for the piece to be placed on it
     * @param board - The board on which the current game is being played
     * @param oldPosition - The position the piece is currently at
     * @param newPosition - The position the player is trying to move the piece to
     * @return - True if there is an obstacle in the way else False
     */
    static boolean hasObstacle(Board board, int oldPosition, int newPosition) {
        int[] oldCoordinates = Utils.convertToCoordinates(oldPosition);
        int[] newCoordinates = Utils.convertToCoordinates(newPosition);
        int rowMultiplier = Utils.isGreater(newCoordinates[Constants.ROW], oldCoordinates[Constants.ROW]);
        int colMultiplier = Utils.isGreater(newCoordinates[Constants.COLUMN], oldCoordinates[Constants.COLUMN]);

        int row = oldCoordinates[Constants.ROW] + rowMultiplier;
        int col = oldCoordinates[Constants.COLUMN] + colMultiplier;

        while(row != newCoordinates[Constants.ROW]) {
            if(board.hasPiece(row, col)) {
                return true;
            }
            row += rowMultiplier;
            col += colMultiplier;
        }

        return false;
    }

    /**
     * Return a list of all possible positions the piece can move to given the current state of the game
     * @param board - The board on which the current game is being played
     * @return - List of possible moves
     */
    @Override
    public ArrayList<Integer> getPossibleMoves(Board board) {
        ArrayList<Integer> possibleMoves = new ArrayList<>();

        for(Integer[] direction : Bishop.getPossibleDirections(position)) {
            addIfValid(possibleMoves, board, direction[Constants.ROW], direction[Constants.COLUMN]);
        }

        return possibleMoves;
    }

    /**
     * Returns a list of all directions that can be accessed by the Bishop
     * Doesn't care about obstacles in the way
     * @param currentPosition - Current position of the Bishop
     * @return - List of possible directions
     */
    public static ArrayList<Integer[]> getPossibleDirections(int currentPosition) {
        ArrayList<Integer[]> possibleDirections = new ArrayList<>();

        int row = convertToCoordinates(currentPosition)[Constants.ROW];
        int col = convertToCoordinates(currentPosition)[Constants.COLUMN];

        int maxMovement = Math.max(Math.min(Constants.NUM_ROWS - row, Constants.NUM_COLS - col), Math.min(row - 0, col - 0));

        for(int i = 1; i < maxMovement + 1; i++) {
            possibleDirections.add(new Integer[]{row + i, col + i});
            possibleDirections.add(new Integer[]{row + i, col - i});
            possibleDirections.add(new Integer[]{row - i, col + i});
            possibleDirections.add(new Integer[]{row - i, col - i});
        }
        return possibleDirections;
    }

    @Override
    public String toString() {
        return "BISHOP";
    }
}
