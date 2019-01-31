package GameFiles;

import pieces.*;
import utils.Constants;
import utils.Utils;

import java.util.HashMap;

import static utils.Constants.NUM_ROWS;
import static utils.Constants.NUM_COLS;
import static utils.Constants.Color;
import static utils.ErrorMessages.NO_PLAYER;
import static utils.Utils.*;

public class Board {
    private Piece gameboard[][];
    private HashMap<Color, Integer> kingPosition;
    private int lastStartingPosition;
    private Piece lastCapturedPiece;
    private Piece lastPieceMoved;

    /**
     * Constructs a new board that has all the pieces set up
     */
    public Board() {
        gameboard = new Piece[NUM_ROWS][NUM_COLS];
        kingPosition = new HashMap<>();
        lastStartingPosition = -1;
        lastCapturedPiece = null;
        lastPieceMoved = null;
        setupGame();
    }

    /**
     * Sets up the game by initializing all requirements
     */
    private void setupGame() {
        populatePieces();
    }

    /**
     * Adds pieces to the board in their correct initial position
     */
    private void populatePieces() {

        for(int col = 0; col < NUM_COLS; col++) {
            Piece blackPiece = null;
            Piece whitePiece = null;
            // Populate 1'st row with Black Pawns and (N-1)th row with White Pawns
            gameboard[1][col] = new Pawn(Color.BLACK, convertToPosition(1, col));
            gameboard[NUM_ROWS - 2][col] = new Pawn(Constants.Color.WHITE, convertToPosition(NUM_ROWS - 2, col));


            int blackPosition = convertToPosition(0, col);
            int whitePosition = convertToPosition(NUM_ROWS - 1, col);

            // Add Rooks
            if(col == 0 || col == Constants.NUM_COLS - 1) {
                blackPiece = new Rook(Color.BLACK, blackPosition);
                whitePiece = new Rook(Color.WHITE, whitePosition);
            }

            // Add Knights
            if(col == 1 || col == Constants.NUM_COLS - 2) {
                blackPiece = new Knight(Constants.Color.BLACK, blackPosition);
                whitePiece = new Knight(Constants.Color.WHITE, whitePosition);
            }

            // Add Bishop
            if(col == 2 || col == Constants.NUM_COLS - 3) {
                blackPiece = new Bishop(Constants.Color.BLACK, blackPosition);
                whitePiece = new Bishop(Constants.Color.WHITE, whitePosition);
            }

            // Add Queens
            if(col == Constants.NUM_COLS/2 - 1) {
                blackPiece = new Queen(Constants.Color.BLACK, blackPosition);
                whitePiece = new Queen(Constants.Color.WHITE, whitePosition);
            }

            // Add Kings
            if(col == Constants.NUM_COLS/2) {
                blackPiece = new King(Constants.Color.BLACK, blackPosition);
                whitePiece = new King(Constants.Color.WHITE, whitePosition);
                kingPosition.put(Color.BLACK, blackPosition);
                kingPosition.put(Color.WHITE, whitePosition);
            }

            gameboard[0][col] = blackPiece;
            gameboard[Constants.NUM_ROWS - 1][col] = whitePiece;

            // Populate every empty space with null
            for(int row = 2; row < Constants.NUM_ROWS - 2; row++) {
                gameboard[row][col] = null;
            }
        }
    }

    /**
     * @param position - Position on the board
     * @return - Piece at the given position or null if no piece is found
     */
    public Piece getPiece(int position) {
        if(assertPosition(position)) {
            int[] coordinates = convertToCoordinates(position);
            return gameboard[coordinates[0]][coordinates[1]];
        }
        return null;
    }

    /**
     * @param row - Row of the game board
     * @param col - Column of the game board
     * @return - Piece at the given position or null if no piece is found
     */
    public Piece getPiece(int row, int col) {
        return getPiece(Utils.convertToPosition(row, col));
    }

    /**
     * Checks if the position the player is trying to move the piece to is valid
     * Performs the following sub tasks
     * - Checks if input position lies on the board
     * - If it does, checks if the position is occupied by piece of same colour or not
     * @param currentPiece - Piece the player is trying to move
     * @param position - The position to which the player is trying to move the piece
     * @return - True if its a valid position else False
     */
    public boolean isValidPosition(Piece currentPiece, int position) {
        if(assertPosition(position)) {
            // If the move lies within the board
            //TODO: Remove below line. Assert handles it
            if(0 <= position && position < Constants.NUM_ROWS * Constants.NUM_COLS) {
                // Position is valid only if the piece at the new position is of a different color or there is no piece at that position
                if((!hasPiece(position)) || (hasPiece(position) && getPiece(position).getColor() != currentPiece.getColor())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if there's a piece at the given position
     * @param position - Position on the board
     * @return - True if there's a piece else False
     */
    public boolean hasPiece(int position) {
        if(assertPosition(position)) {
            Piece piece = getPiece(position);
            if(piece == null) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if there's a piece at the given position
     * @param row - Row on the board
     * @param col - Column on the board
     * @return True if there's a piece else False
     */
    public boolean hasPiece(int row, int col) {
        return hasPiece(Utils.convertToPosition(row, col));
    }

    /**
     * Get the position of the king (of the given color) on the board
     * @param color - Color of the king whose position we're trying to find
     * @return - Position of the king of the given color
     */
    public int getKingPosition(Color color) {
        return kingPosition.get(color);
    }

    /**
     * Change the position value of the king (of the given color) on the board
     * @param color - Color of the king whose position we're trying to change
     * @param newPosition - Position to which we're moving the king to
     */
    private void setKingPosition(Color color, int newPosition) {
        kingPosition.replace(color, newPosition);
    }

    /**
     * Moves piece to new position and returns a piece if captured
     * Removes the captured piece from the board
     * @param piece - Piece the player is trying to move
     * @param newPosition - Position to which the player is moving the piece
     * @return - Opponents piece if captured else null
     */
    public Piece movePiece(Piece piece, int newPosition) {
        // Remove piece from old position
        int oldPosition = piece.getPosition();
        lastStartingPosition = oldPosition;
        lastPieceMoved = piece;
        
        removePiece(oldPosition);
        Piece capturedPiece = getPiece(newPosition);
        lastCapturedPiece = capturedPiece;
        // Remove piece from opponents list
        try {
            Game.getPlayer(getOpposingColor(piece.getColor())).removePiece(capturedPiece);
        }
        catch(NullPointerException npe) {
            System.err.println(NO_PLAYER);
        }
        updateBoardPosition(newPosition, piece);

        if(piece.toString().equals("KING")) {
            setKingPosition(piece.getColor(), newPosition);
        }

        return capturedPiece;
    }

    /**
     * Removes piece from board at specified position
     * @param position - Position from which we want to remove the piece
     */
    private void removePiece(int position) {
        updateBoardPosition(position, null);
    }

    public void undoLastMove() {
        if(lastPieceMoved == null)
            return;
        updateBoardPosition(lastPieceMoved.getPosition(), lastCapturedPiece);
        try {
            Game.getPlayer(getOpposingColor(lastPieceMoved.getColor())).addPiece(lastCapturedPiece);
            Game.getPlayer(lastPieceMoved.getColor()).removePiece(lastCapturedPiece);
        }
        catch(NullPointerException npe) {
            System.err.println(NO_PLAYER);
        }
        updateBoardPosition(lastStartingPosition, lastPieceMoved);

        if(lastPieceMoved.toString().equals("KING")) {
            setKingPosition(lastPieceMoved.getColor(), lastStartingPosition);
        }

        lastStartingPosition = -1;
        lastCapturedPiece = null;
        lastPieceMoved = null;
    }

    /**
     * Update the position of the piece on the board
     * @param position - New position the piece is being moved to
     * @param piece - Piece that's being moved
     * @return - True if the position was updated else false
     */
    private void updateBoardPosition(int position, Piece piece) {
        if(assertPosition(position)) {
            int coordinates[] = convertToCoordinates(position);
            gameboard[coordinates[0]][coordinates[1]] = piece;
            if(piece != null)
                piece.updatePosition(position);
        }
    }

    /**
     * Print board for testing purposes
     */
    public void printGameBoard() {
        for(int i = 0; i < NUM_ROWS; i++) {
            for(int j = 0; j < NUM_COLS; j++) {
                Piece piece = getPiece(convertToPosition(i, j));
                String value = "null";
                String c = "   ";
                if(piece != null) {
                    value = piece.toString();
                    if(piece.getColor() == Color.WHITE) {
                        c = "(w)";
                    }
                    else {
                        c = "(b)";
                    }
                }

                //System.out.print(value + ", " + i + ", " + j + ", " + convertToPosition(i, j) +  "\t");
                System.out.print(value + " " + c + "\t");
            }
            System.out.println();
        }
    }


}
