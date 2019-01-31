package pieces;

import GameFiles.Board;
import utils.Constants;

import java.util.ArrayList;

import static utils.Utils.getColDiff;
import static utils.Utils.getRowDiff;

public class Queen extends Piece {

    enum MovementType {
        ROOK, BISHOP
    }

    /**
     * Creates a new Queen for the chess board
     * @param color - Color of the newly created piece
     * @param position - Position the piece should be placed at
     */
    public Queen(Constants.Color color, int position) {
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
            // Check if the new position lies straight in its path or in its diagonal path
            int rowDiff = getRowDiff(position, newPosition);
            int colDiff = getColDiff(position, newPosition);

            // Lies straight in its path
            // Mimics Rook movement
            if((rowDiff > 0 && colDiff == 0) || (rowDiff == 0 && colDiff > 0)) {
                return !hasObstacle(board, newPosition, MovementType.ROOK);
            }
            // Position on its diagonal
            // Mimics Bishop movement
            else if(rowDiff == colDiff && rowDiff > 0) {
                return !hasObstacle(board, newPosition, MovementType.BISHOP);
            }
        }
        return false;
    }

    @Override
    protected boolean hasObstacle(Board board, int newPosition) {
        return false;
    }

    /**
     * Checks if there is an obstacle in the path of the piece and the new position
     * Obstacles come in the form of other positions
     * Assumes the position is valid and that its possible for the piece to be placed on it
     * @param board - The board on which the current game is being played
     * @param newPosition - The position the player is trying to move the piece to
     * @return - True if there is an obstacle in the way else False
     */
    private boolean hasObstacle(Board board, int newPosition, MovementType movementType) {
        if(movementType == MovementType.ROOK) {
            return Rook.hasObstacle(board, position, newPosition);
        }
        else if(movementType == MovementType.BISHOP) {
            return Bishop.hasObstacle(board, position, newPosition);
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

        // Queen can mimic the movement of a rook
        for(Integer[] direction : Rook.getPossibleDirections(position)) {
            addIfValid(possibleMoves, board, direction[Constants.ROW], direction[Constants.COLUMN]);
        }

        // Queen can also mimic the movement of a bishop
        for(Integer[] direction : Bishop.getPossibleDirections(position)) {
            addIfValid(possibleMoves, board, direction[Constants.ROW], direction[Constants.COLUMN]);
        }

        return possibleMoves;
    }

    @Override
    public String toString() {
        return "QUEEN";
    }
}
