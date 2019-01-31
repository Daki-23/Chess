package pieces;

import GameFiles.Board;
import GameFiles.Game;
import GameFiles.Player;
import junit.framework.TestCase;
import utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;

public class PawnTest extends TestCase {

    private Board board;

    /**
     * Tests whether the isValidMove() method of the Pawn class returns the correct output or not
     */
    public void testIsValidMove() {

        Pawn pawn = (Pawn)board.getPiece(6, 4);

        // Check a normal move
        assertTrue(pawn.isValidMove(board, 5, 4));

        // Check position outside board
        assertFalse(pawn.isValidMove(board, -1, 2));

        // Check if moving to an invalid position for Pawn
        assertFalse(pawn.isValidMove(board, pawn.getRow() + 2, pawn.getCol() + 1));

        // Check if setting the new position to be the same as the old position causes the piece to move or not
        assertFalse(pawn.isValidMove(board, pawn.getRow(), pawn.getCol()));

        // Check if moving to a position occupied by own piece
        Pawn otherPawn = (Pawn)board.getPiece(6, 3);
        otherPawn.movePiece(board, 5, 3);
        assertFalse(pawn.isValidMove(board,5, 3));
    }

    /**
     * Tests whether the list of possible moves that the pawn can move is correct or not
     */
    public void testGetPossibleMoves() {
        Pawn pawn = (Pawn)board.getPiece(6, 4);

        // Check if list of possible moves returned is correct or not
        ArrayList<Integer> expectedOutput = new ArrayList<>(Arrays.asList(36, 44));
        assertEquals(expectedOutput, pawn.getPossibleMoves(board));
    }

    public void setUp() throws Exception {
        board = Game.setupNewGame();
    }
}