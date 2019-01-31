package GameFiles;
import utils.Constants;

import static utils.Constants.Color;

public class Game {

    private static Player playerWhite = null;
    private static Player playerBlack = null;
    private static Board board = null;
    private static Player currentPlayer = null;
    private static int turn;


    private enum GameState {
        ONGOING, STALEMATE, CHECKMATE
    }

    public static void main(String[] args) {

        board = setupNewGame();

        turn = 0;

        while(getGameState(board, currentPlayer) == GameState.ONGOING) {
            //current player makes move
            //Keep trying move until it returns true

            turn++;
            turn %= 2;
            if(turn == 0)
                currentPlayer = playerWhite;
            else
                currentPlayer = playerBlack;

        }
    }

    /**
     * Get the player object given the color
     * @param color - Color of the player we want returned
     * @return - Player object of input color
     */
    public static Player getPlayer(Color color) {
        if(color == Color.WHITE) {
            return playerWhite;
        }
        else {
            return playerBlack;
        }
    }

    private static void setPlayer(Player player, Color color) {
        if(color == Color.WHITE) {
            playerWhite = player;
        }
        else if(color == Color.BLACK) {
            playerBlack = player;
        }
    }

    /**
     * Checks and returns the current state of the game
     * @param board - The board on which the current game is being played
     * @param currentPlayer - The player whose turn it is
     * @return - State of the game condition
     */
    private static GameState getGameState(Board board, Player currentPlayer) {
        if(currentPlayer.isCheckmate(board)) {
            return GameState.CHECKMATE;
        }
        else if(currentPlayer.isStalemate(board)) {
            return GameState.STALEMATE;
        }
        return GameState.ONGOING;
    }

    public static Board getGameBoard() {
        return board;
    }

    public static Board setupNewGame() {
        Game.setPlayer(new Player(Constants.Color.WHITE), Constants.Color.WHITE);
        Game.setPlayer(new Player(Constants.Color.BLACK), Constants.Color.BLACK);
        currentPlayer = playerWhite;
        return new Board();
    }
}
