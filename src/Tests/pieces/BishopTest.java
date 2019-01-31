package pieces;

import GameFiles.Board;
import GameFiles.Game;
import GameFiles.Player;
import junit.framework.TestCase;
import utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;

public class BishopTest extends TestCase {

    private static final int BISHOP_ROW = 7;
    private static final int BISHOP_COL = 5;
    private Board board;

    /**
     * Tests whether the isValidMove() method of the Bishop class returns the correct output or not
     */
    public void testIsValidMove() {
        Bishop bishop = (Bishop) board.getPiece(BISHOP_ROW, BISHOP_COL);
        movePawnForward(board);

        // Check a normal move
        assertTrue(bishop.isValidMove(board, BISHOP_ROW - 1, BISHOP_COL - 1));

        // Check position outside board
        assertFalse(bishop.isValidMove(board, -1, 2));

        // Check if moving to an invalid position for Bishop
        assertFalse(bishop.isValidMove(board, BISHOP_ROW + 2, BISHOP_COL + 1));

        // Check if setting the new position to be the same as the old position causes the piece to move or not
        assertFalse(bishop.isValidMove(board, BISHOP_ROW, BISHOP_COL));

        // Check if moving to a position occupied by own piece
        Pawn otherPawn = (Pawn)board.getPiece(6, 3);
        otherPawn.movePiece(board, 5, 3);
        assertFalse(bishop.isValidMove(board,5, 3));
    }

    /**
     * Tests whether the list of possible moves that the bishop can move is correct or not
     */
    public void testGetPossibleMoves() {
        Bishop bishop = (Bishop) board.getPiece(BISHOP_ROW, BISHOP_COL);

        // Check if list of possible moves returned is correct or not
        ArrayList<Integer> expectedOutput = new ArrayList<>();
        assertEquals(expectedOutput, bishop.getPossibleMoves(board));

        // Move pawn forward and then check possible moves
        movePawnForward(board);
        expectedOutput = new ArrayList<>(Arrays.asList(52, 43, 34, 25, 16));
        assertEquals(expectedOutput, bishop.getPossibleMoves(board));
    }

    public void setUp() throws Exception {
        board = Game.setupNewGame();
    }

    /**
     * Moves pawn forward to give the bishop some place to move
     * @param board - The board on which the tests will be conducted
     */
    private void movePawnForward(Board board) {
        Pawn pawn = (Pawn) board.getPiece(BISHOP_ROW - 1, BISHOP_COL - 1);
        pawn.movePiece(board, BISHOP_ROW - 2, BISHOP_COL - 1);
    }
}