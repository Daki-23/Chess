package GameFiles;

import pieces.Piece;
import utils.Constants;
import java.util.ArrayList;


public class Player {
    private Constants.Color color;
    private boolean checked;
    private ArrayList<Piece> pieces;
    private ArrayList<Piece> capturedPieces;

    /**
     * Creates a new player of the specified color
     * @param color - Color of the player
     */
    public Player(Constants.Color color) {
        this.color = color;
        checked = false;
        pieces = new ArrayList<>();
        capturedPieces = new ArrayList<>();
    }

    /**
     * @return - List of players pieces that are currently on the board
     */
    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    /**
     * Moves piece from old position to new position
     * Checks if the move specified is valid or not and performs it if it is
     * @param board - The board on which the current game is being played
     * @param oldPosition - Position from which the player wants to move the piece
     * @param newPosition - Position to which the player wants to move the piece
     * @return - True if the move was successful else False
     */
    public boolean movePiece(Board board, int oldPosition, int newPosition) {

        Piece piece = board.getPiece(oldPosition);

        if(piece == null || piece.getColor() != color) {
            return false;
        }
        else {
            return piece.movePiece(board, newPosition);
        }
    }

    /**
     * Add piece to list of players pieces
     * @param piece - Piece to add
     */
    public void addPiece(Piece piece) {
        if(piece != null) {
            pieces.add(piece);
        }
    }

    /**
     * Remove piece from list of players pieces
     * @param piece - Piece to remove
     */
    public void removePiece(Piece piece) {
        if(piece != null) {
            pieces.remove(piece);
        }
    }

    /**
     * Add captured piece to list of captured pieces
     * @param piece - Piece that was captured this turn
     */
    public void addCapturedPiece(Piece piece) {
        if(piece != null) {
            capturedPieces.add(piece);
        }
    }

    /**
     * Set whether player is checked or not
     * @param checked - Value of checked
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isCheckmate(Board board) {
        if(!checked)
            // Move king and see if that prevents checkmate
            return !isMovePossible(board);

        return false;
    }

    /**
     * Checks if any move is possible or not for the current player
     * @param board - The board on which the current game is being played
     * @return - True if no move possible else False
     */
    public boolean isStalemate(Board board) {
        if(!checked)
            return !isMovePossible(board);
        return false;
    }

    private void undoMove(Board board) {
        board.undoLastMove();
        setChecked(true);
    }

    /**
     * Checks if there is any move possible or not
     * @param board - The board on which the current game is being played
     * @return - True if a move is possible else False
     */
    private boolean isMovePossible(Board board) {
        ArrayList<Integer> positionsChecked = new ArrayList<>();
        for(Piece piece : getPieces()) {
            for(Integer position : piece.getPossibleMoves(board)) {
                // Don't need to check positions that have already been checked
                if(!positionsChecked.contains(position)) {
                    positionsChecked.add(position);
                    if(piece.movePiece(board, position)) {
                        undoMove(board);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
