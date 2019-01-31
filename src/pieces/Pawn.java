package pieces;

import GameFiles.Board;
import utils.Constants;
import java.util.ArrayList;

import static utils.Utils.*;

public class Pawn extends Piece {

    /**
     * Creates a new Pawn for the chess board
     * @param color - Color of the newly created piece
     * @param position - Position the piece should be placed at
     */
    public Pawn(Constants.Color color, int position) {
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

        // Pawn cannot move backward. So check if new position makes pawn move backward or not
        if((color == Constants.Color.WHITE && newPosition > position) || (color == Constants.Color.BLACK && newPosition < position)) {
            return false;
        }

        // This will return whether it is possible for the piece to be placed at the new position
        // it will not give info of whether its possible for the piece to reach that position or not
        if(board.isValidPosition(this, newPosition)) {
            // Check if it's possible for a pawn to reach that position given its current position

            int rowDiff = getRowDiff(position, newPosition);
            int colDiff = getColDiff(position, newPosition);

            // If first move, pawn is allowed to move upto 2 rows
            if(firstMove) {
                if(rowDiff == 2 && colDiff == 0) {
                    // Possibly Valid move
                    // Check for obstacles in the way
                    // If it has an obstacle in the path, invalid move
                    return !hasObstacle(board, newPosition);
                }
                else if(rowDiff == 2) {
                    // Trying to move two spaces forward and diagonal at the same time
                    // Invalid move
                    return false;
                }
            }
            // Trying to move one row forward. May or may not be diagonal movement
            if(rowDiff == 1 && colDiff <= 1) {
                // Check if movement is diagonal or not
                if(colDiff == 1) {
                    // Movement is diagonal
                    // Check if new position has a piece
                    // If it has a piece, we already know its of the opposite color (From isValidPosition()) and the move is valid
                    // If it doesn't, trying to move diagonally without attacking piece. Move is invalid.
                    return board.hasPiece(newPosition);
                }
                else {
                    // Trying to move straight. If piece exists at that position, invalid move
                    return !hasObstacle(board, newPosition);
                }
            }
            else {
                // Trying to move either more or less than 1 row or more than 1 column
                return false;
            }

        }
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
    @Override
    protected boolean hasObstacle(Board board, int newPosition) {
        // Check if there is a piece a single step in front of the pawn
        int rowDiff = getRowDiff(position, newPosition);
        int forwardRow;
        int currentRow = getRow();
        int currentColumn = getCol();
        int direction;
        if(color == Constants.Color.WHITE) {
            // Row is subtracted to move pawn forward
            direction = Constants.NEGATIVE;
        }
        else {
            // Row is added to move pawn forward
            direction = Constants.POSITIVE;
        }
        if(rowDiff == 2) {
            //Check if piece exists two steps forward
            forwardRow = currentRow + direction*2;
            if(board.hasPiece(forwardRow, currentColumn)) {
                return true;
            }
        }

        // Check one step forward
        forwardRow = currentRow + direction;
        return board.hasPiece(forwardRow, currentColumn);
    }

    /**
     * Return a list of all possible positions the piece can move to given the current state of the game
     * @param board - The board on which the current game is being played
     * @return - List of possible moves
     */
    @Override
    public ArrayList<Integer> getPossibleMoves(Board board) {
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        // Pawn can move one or two steps in the front, and diagonally on the side
        int direction;
        if(color == Constants.Color.WHITE) {
            direction = Constants.NEGATIVE;
        }
        else {
            direction = Constants.POSITIVE;
        }
        int[] currentCoordinates = convertToCoordinates(position);
        int row = currentCoordinates[Constants.ROW];
        int col = currentCoordinates[Constants.COLUMN];
        // Pawn can possibly move two rows forward
        addIfValid(possibleMoves, board, row + direction*2, col);
        addIfValid(possibleMoves, board, row + direction, col);
        addIfValid(possibleMoves, board, row + direction, col + 1);
        addIfValid(possibleMoves, board, row + direction, col - 1);

        return possibleMoves;
    }

    @Override
    public String toString() {
        return "PAWN";
    }
}
