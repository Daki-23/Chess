package pieces;

import GameFiles.Board;
import utils.Constants;
import utils.Utils;

import java.util.ArrayList;

import static utils.Utils.*;

public class Rook extends Piece {

    /**
     * Creates a new Rook for the chess board
     * @param color - Color of the newly created piece
     * @param position - Position the piece should be placed at
     */
    public Rook(Constants.Color color, int position) {
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
            // Check if the newPosition lies either vertically or horizontally in front of the rook
            // i.e. Either rowDiff should be 0 or colDiff should be 0
            int rowDiff = getRowDiff(position, newPosition);
            int colDiff = getColDiff(position, newPosition);

            if((rowDiff > 0 && colDiff == 0) || (rowDiff == 0 && colDiff > 0)) {
                // Only case where its a valid move
                // Check if there's an obstacle in the way to its new position
                return !hasObstacle(board, newPosition);
            }
            else {
                return false;
            }
        }
        return false;
    }

    @Override
    protected boolean hasObstacle(Board board, int newPosition) {
        return Rook.hasObstacle(board, position, newPosition);
    }

    @Override
    public ArrayList<Integer> getPossibleMoves(Board board) {
        ArrayList<Integer> possibleMoves = new ArrayList<>();

        for(Integer[] direction : Rook.getPossibleDirections(position)) {
            addIfValid(possibleMoves, board, direction[Constants.ROW], direction[Constants.COLUMN]);
        }

        return possibleMoves;
    }

    public static ArrayList<Integer[]> getPossibleDirections(int currentPosition) {
        ArrayList<Integer[]> possibleDirections = new ArrayList<>();

        int row = convertToCoordinates(currentPosition)[Constants.ROW];
        int col = convertToCoordinates(currentPosition)[Constants.COLUMN];

        int maxMovement = Math.max((col - 0), Math.max((Constants.NUM_COLS - col), Math.max((row - 0), (Constants.NUM_ROWS - row))));

        for(int i = 1; i < maxMovement + 1; i++) {
            possibleDirections.add(new Integer[]{row + i, col});
            possibleDirections.add(new Integer[]{row - i, col});
            possibleDirections.add(new Integer[]{row, col + i});
            possibleDirections.add(new Integer[]{row, col - i});
        }
        return possibleDirections;
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
        int rowDiff = getRowDiff(oldPosition, newPosition);
        int colDiff = getColDiff(oldPosition, newPosition);
        int[] oldCoordinates = convertToCoordinates(oldPosition);
        int[] newCoordinates = convertToCoordinates(newPosition);

        // Direction = +1 depicts rook going towards the right or the bottom
        int direction;
        // If Rook is moving horizontally
        if(rowDiff == 0) {

            direction = Utils.isGreater(newCoordinates[1], oldCoordinates[1]);

            // For each square lying on the path between the old position and new position
            for(int squareCol = oldCoordinates[1] + direction; squareCol != newCoordinates[1]; squareCol += direction) {
                if(board.hasPiece(oldCoordinates[0], squareCol)) {
                    return true;
                }
            }
        }
        else if(colDiff == 0) {
            direction = Utils.isGreater(newCoordinates[0], oldCoordinates[0]);

            for(int sqaureRow = oldCoordinates[0] + direction; sqaureRow != newCoordinates[0]; sqaureRow += direction) {
                if(board.hasPiece(sqaureRow, oldCoordinates[1])) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "ROOK";
    }
}
