package pieces;


import GameFiles.Board;
import GameFiles.Game;
import GameFiles.Player;
import utils.Constants;
import utils.Utils;

import java.util.ArrayList;

import static utils.ErrorMessages.NO_PLAYER;
import static utils.Utils.convertToCoordinates;
import static utils.Utils.convertToPosition;
import static utils.Utils.getOpposingColor;

public abstract class Piece {
    Constants.Color color;
    int position;
    boolean firstMove;

    /**
     * Creates a new Piece for the chess board
     * @param color - Color of the newly created piece
     * @param position - Position the piece should be placed at
     */
    public Piece(Constants.Color color, int position) {
        this.color = color;
        this.position = position;
        firstMove = true;
        Player player = Game.getPlayer(color);
        if(player != null)
            player.addPiece(this);
    }

    /**
     * Try's to move the piece to the new position on the board if possible
     * - Checks for validity of move first
     * @param board - The board on which the current game is being played
     * @param newPosition - The position to which the player wants to move the piece
     * @return - True if the piece is moved else False
     */
    public boolean movePiece(Board board, int newPosition) {
        if(isValidMove(board, newPosition)) {
            // Move piece and update position on board
            firstMove = false;
            Piece capturedPiece = board.movePiece(this, newPosition);
            try {
                Game.getPlayer(this.color).addCapturedPiece(capturedPiece);
            }
            catch (NullPointerException npe) {
                System.err.println(NO_PLAYER);
            }
            // If moving piece is causing the current player to get checked
            // or player is already checked and current move doesn't block it
            // undo the move
            if(isGettingChecked(board)) {
                board.undoLastMove();
                return false;
            }
            // Player isn't getting checked
            // If the player was previously checked, moving this piece has blocked the check
            // Set the checked property of the player to false
            else {
                Game.getPlayer(color).setChecked(false);
            }
            if(isChecking(board)) {
                Constants.Color opposingColor = getOpposingColor(color);
                Game.getPlayer(opposingColor).setChecked(true);
            }
            return true;
        }
        return false;
    }

    public boolean movePiece(Board board, int newRow, int newCol) {
        return movePiece(board, Utils.convertToPosition(newRow, newCol));
    }

    /**
     * Checks if moving the piece causes the opposing player to get checked
     * @param board - The board on which the current game is being played
     * @return - True if the opposing player is getting checked else False
     */
    private boolean isChecking(Board board) {

        Constants.Color opposingColor = getOpposingColor(color);

        // Check if king position can be attacked by current piece in next turn
        // If yes, set checked value of opposing player
        int oppKingPosition = board.getKingPosition(opposingColor);
        boolean causingCheck = isValidMove(board, oppKingPosition);
        if(!causingCheck) {
            // If the current piece isn't causing a check, check if any other piece is
            for(Piece piece : Game.getPlayer(color).getPieces()) {
                if(piece.isValidMove(board, oppKingPosition)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Checks if moving a piece causes the current player to get checked or if moving the piece causes the check to get blocked
     * Assumes the piece has already been moved
     * @param board - The board on which the current game is being played
     * @return - True if the current player is getting checked given the pieces on the board, else False
     */
    protected boolean isGettingChecked(Board board) {
        // Check every opponent piece and see if they can possibly attack the king
        Constants.Color opposingColor = getOpposingColor(color);
        ArrayList<Piece> opposingPieces = Game.getPlayer(opposingColor).getPieces();
        for(Piece piece : opposingPieces) {
            if(piece.isChecking(board)) {
                return true;
            }
        }
        return false;
    }

    protected int getRow() {
        return convertToCoordinates(position)[Constants.ROW];
    }

    protected int getCol() {
        return convertToCoordinates(position)[Constants.COLUMN];
    }

    protected int[] getCoordinates() {
        return convertToCoordinates(position);
    }

    /**
     * Checks if it's possible for piece to reach new position
     * Performs the following sub tasks
     * - Checks if position is valid or not
     * - If position is valid, checks if it's possible for current piece to reach that position in one turn
     * - If above is possible, checks if there is any obstacle in its path or not
     * @param board - The board on which the current game is being played
     * @param newRow - The row the player is trying to move the piece to
     * @param newCol - The column the player is trying to move the piece to
     * @return - True if piece can be moved to the new position else false
     */
    protected boolean isValidMove(Board board, int newRow, int newCol) {
        return isValidMove(board, convertToPosition(newRow, newCol));
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
    protected abstract boolean isValidMove(Board board, int newPosition);

    /**
     * Checks if there is an obstacle in the path of the piece and the new position
     * Obstacles come in the form of other positions
     * Assumes the position is valid and that its possible for the piece to be placed on it
     * @param board - The board on which the current game is being played
     * @param newPosition - The position the player is trying to move the piece to
     * @return - True if there is an obstacle in the way else False
     */
    protected abstract boolean hasObstacle(Board board, int newPosition);

    public Constants.Color getColor() {
        return color;
    }

    public void updatePosition(int newPosition) {
        position = newPosition;
    }

    public int getPosition() {
        return position;
    }

    /**
     * Return a list of all possible positions the piece can move to given the current state of the game
     * @param board - The board on which the current game is being played
     * @return - List of possible moves
     */
    public abstract ArrayList<Integer> getPossibleMoves(Board board);

    /**
     * Add position to array list if its a valid move for the piece
     * Used while returning a list of possible moves
     * @param arrayList - List we want to add the position to
     * @param board - The board on which the current game is being played
     * @param row - Row number of the position
     * @param col - Column number of the position
     */
    void addIfValid(ArrayList<Integer> arrayList, Board board, int row, int col) {

        // Don't add duplicate positions
        int posToAdd = convertToPosition(row, col);
        if(arrayList.contains(posToAdd))
            return;

        if(isValidMove(board, posToAdd)) {
            arrayList.add(posToAdd);
        }
    }

}
