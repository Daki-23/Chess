package pieces;

import GameFiles.Board;
import utils.Constants;
import utils.Utils;

import java.util.ArrayList;

public class Knight extends Piece {

    /**
     * Creates a new Knight for the chess board
     * @param color - Color of the newly created piece
     * @param position - Position the piece should be placed at
     */
    public Knight(Constants.Color color, int position) {
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
            // Check if its possible for knight to reach that position given current position
            int rowDiff = Utils.getRowDiff(position, newPosition);
            int colDiff = Utils.getColDiff(position, newPosition);

            if((rowDiff == 1 && colDiff == 2) || (rowDiff == 2 && colDiff == 1)) {
                // Valid move. Since a knight jumps over other pieces, we don't need to check for obstacles
                return true;
            }
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

        int row = getRow();
        int col = getCol();

        addIfValid(possibleMoves, board, row + 2, col + 1);
        addIfValid(possibleMoves, board, row + 2, col - 1);
        addIfValid(possibleMoves, board, row - 2 , col + 1);
        addIfValid(possibleMoves, board, row - 2, col - 1);
        addIfValid(possibleMoves, board, row + 1, col + 2);
        addIfValid(possibleMoves, board, row + 1, col - 2);
        addIfValid(possibleMoves, board, row - 1, col + 2);
        addIfValid(possibleMoves, board, row - 1, col - 2);

        return possibleMoves;

    }

    /**
     * Checks if there is an obstacle in the path of the piece and the new position
     * Obstacles come in the form of other positions
     * Assumes the position is valid and that its possible for the piece to be placed on it
     * @param board - The board on which the current game is being played
     * @param newPosition - The position the player is trying to move the piece to
     * @return - True if there is an obstacle in the way else False
     */
    @Override
    protected boolean hasObstacle(Board board, int newPosition) {
        return false;
    }

    @Override
    public String toString() {
        return "KNIGHT";
    }

}
