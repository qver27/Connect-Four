// Evan Lee
// 1/29/2024
// CSE 123
// C1: Abstract Strategy Games
// TA: Kieran Rullman
// This class represents a ConnectFour game, which extends AbstractStrategyGame, and 
// contains all the necessary fields and methods for getting instructions, making moves,
// and checking for a winner

import java.util.*;

public class ConnectFour extends AbstractStrategyGame {

    public static final char PLAYER_1_TOKEN = 'R';
    public static final char PLAYER_2_TOKEN = 'Y';
    public static final String PLAYER_1_TOKEN_EMOJI = "🔴";
    public static final String PLAYER_2_TOKEN_EMOJI = "🟡";
    public static final String BLANK_SPACE_EMOJI = "⚫";
    public static final int PLAYER_1 = 1;
    public static final int PLAYER_2 = 2;
    public static final int TIE = 0;
    public static final int GAME_IS_OVER = -1;
    public static final int GAME_NOT_OVER = -1;
    public static final int BOARD_WIDTH = 7;
    public static final int BOARD_HEIGHT = 6;

    private char[][] board;
    private boolean isRedTurn;

    // Behavior: Constructor for a ConnectFour game, creates a 6x7 board,
    //           and has player 1 move first
    // Exceptions: None
    // Returns: None
    // Parameters: None
    public ConnectFour() {
        board = new char[6][7];
        isRedTurn = true;
    }

    // Behavior: Returns instructions for the game, providing information about the rules,
    //           making moves, and the win condition of the game
    // Exceptions: None
    // Returns: String, the instructions for the game
    // Parameters: None;
    public String instructions() {
        String tutorial = "Player 1 is red and drops a token down a column ";
        tutorial += "by inputting the provided column number between 1 and 7.\n";
        tutorial += "Player 2 is yellow and moves afterwards. ";
        tutorial += "A space represented by an black circle is an empty space.\n";
        tutorial += "The game is won when one player gets 4 tokens in a row, ";
        tutorial += "horizontally, vertically, or diagonally, if the board is full";
        tutorial += " and has no winner, it is a tie.";

        return tutorial;
    }

    // Behavior: Take in a player provided column and drops a token into it,
    //           a red one if it is player 1's turn, and a yellow one if it is player 2's turn,
    //           and drops the token to the bottom of the column, unless there is another token,
    //           in which case it goes on top of the other token, unless the column is full,
    //           and then changes the player turn
    // Exceptions: Throws IllegalArgumentException if input is null
    //             or the inputted column is out of bounds
    //             or the inputted column is full
    // Returns: None
    // Parameters: input, a Scanner in order to read user input to get the column
    public void makeMove(Scanner input) {

        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        System.out.print("Column? ");
        int column = input.nextInt();   

        if (column > board[0].length || column < 1) {
            throw new IllegalArgumentException("Column out of bounds");
        }
        if (!(board[0][column - 1] == '\0')) {
            throw new IllegalArgumentException("Column is full!");
        }

        char currToken = isRedTurn ? PLAYER_1_TOKEN : PLAYER_2_TOKEN;
        for (int i = board.length - 1; i >= 0; i--) {
            if (board[i][column - 1] == '\0') {
                board[i][column - 1] = currToken;
                i = 0;
            }
        }
        isRedTurn = !isRedTurn;
    }

    // Behavior: Returns a string representation of the game, being a board, 
    //           the tokens, and labels for the column numbers
    // Exceptions: None
    // Returns: String, the string representation of the game
    // Parameters: None
    public String toString() {

        String gameString = "";
        for (int i = 1; i < board.length + 2; i++) {
            gameString += "| "+ i + "  ";
        }
        
        gameString += "|\n";
        gameString += "------------------------------------\n";
        for (int i = 0; i < board.length; i++) {
            gameString += "| ";
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 'R') {
                    gameString += PLAYER_1_TOKEN_EMOJI;
                } else if (board[i][j] == 'Y') {
                    gameString += PLAYER_2_TOKEN_EMOJI;
                } else {
                    gameString += BLANK_SPACE_EMOJI;
                }
                gameString += " | ";
            }
            gameString += "\n------------------------------------\n";
        }
        return gameString;
    }

    // Behavior: Returns an integer representing the player that won by getting 
    //           4 tokens consecutively, either horizontally, vertically, or diagonally, 
    //           if there is no winner, checks for a tie, returning 0 if the game was tie,
    //           and -1 if the game is not over
    // Exceptions: None
    // Returns: int, the integer representing the player that won, 
    //          a 1 if it was player 1, and a 2 if it was player 2
    //          the integer representing a tie (a 0), or the integer 
    //          representing that the game is not over yet (-1)
    // Parameters: None
    public int getWinner() {
        int horzWinner = getHorizontalWinner();
        int vertWinner = getVerticalWinner();
        int diagWinner = getDiagonalWinner();
        if (horzWinner != GAME_NOT_OVER) {
            return horzWinner;
        }
        if (vertWinner != GAME_NOT_OVER) {
            return vertWinner;
        }
        if (diagWinner != GAME_NOT_OVER) {
            return diagWinner;
        }
        return checkTie();  
    }

    // Behavior: Helper method for getting a winner, checks 
    //           each row for 4 of the same token consecutively in a row
    // Exceptions: None
    // Returns: int, an integer representation of the player who got
    //          4 in a row (1 if player 1, 2 if player 2), or -1, 
    //          which represents the game not being over
    // Parameters: None
    private int getHorizontalWinner() {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH - 3; j++) {
                if (board[i][j] == board[i][j + 1] && 
                    board[i][j] == board[i][j + 2] && 
                    board[i][j] == board[i][j + 3] && 
                    board[i][j] != '\0') {
                    return getPlayer(board[i][j]);
                }
            }
        }
        return GAME_NOT_OVER;
    }

    // Behavior: Helper method for getting a winner, checks 
    //           each colmumn for 4 of the same token consecutively in a column
    // Exceptions: None
    // Returns: int, an integer representation of the player who got
    //          4 consecutive tokens (1 if player 1, 2 if player 2), or -1, 
    //          which represents the game not being over
    // Parameters: None
    private int getVerticalWinner() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT - 3; j++) {
                if (board[j][i] == board[j + 1][i] && 
                    board[j][i] == board[j + 2][i] && 
                    board[j][i] == board[j + 3][i] && 
                    board[j][i] != '\0') {
                    return getPlayer(board[j][i]);
                }
            }
        }
        return GAME_NOT_OVER;
    }

    // Behavior: Helper method for getting a winner, checks 
    //           each diagonal for 4 of the same token consecutively diagonally
    // Exceptions: None
    // Returns: int, an integer representation of the player who got
    //          4 consecutive tokens (1 if player 1, 2 if player 2), or -1, 
    //          which represents the game not being over
    // Parameters: None
    private int getDiagonalWinner() {
        for (int i = 0; i < BOARD_HEIGHT / 2; i++) {
            for (int j = 0; j < (BOARD_WIDTH / 2) + 1; j++) {
                if (board[i][j] == board[i + 1][j + 1] && 
                    board[i][j] == board[i + 2][j + 2] && 
                    board[i][j] == board[i + 3][j + 3] && 
                    board[i][j] != '\0') {
                    return getPlayer(board[i][j]);
                }
            }
            for (int j = BOARD_WIDTH - 1; j >= (BOARD_WIDTH / 2); j--) {
                if (board[i][j] == board[i + 1][j - 1] && 
                    board[i][j] == board[i + 2][j - 2] && 
                    board[i][j] == board[i + 3][j - 3] && 
                    board[i][j] != '\0') {
                    return getPlayer(board[i][j]);
                }
            }
        }

        return GAME_NOT_OVER;
    }

    // Behavior: Helper method for getting a winner, checks 
    //           if the board is full, and there is a tie
    // Exceptions: None
    // Returns: int, an integer representation of a potential tie, 0 if the board is full,
    //          -1, if the game is not over yet
    // Parameters: None
    private int checkTie() {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] == '\0') {
                    return GAME_NOT_OVER;
                }
            }
        } 
        return TIE;
    }

    // Behavior: Helper method, returns a player based on a corresponding token
    // Exceptions: None
    // Returns: int, 1 if the token corresponds to player 1, 
    //          and 2 if the token corresponds to player 2
    //          and -1 if the space is empty
    // Parameters: token, the token to analyze
    private int getPlayer(char token) {
        if (token == PLAYER_1_TOKEN) {
            return PLAYER_1;
        } else if (token == PLAYER_2_TOKEN) {
            return PLAYER_2;
        }
        return GAME_NOT_OVER;
    }

    // Behavior: Returns the player who has the next turn
    // Exceptions: None
    // Returns: int, the next player (1 if player 1, 2 if player 2), -1 if the game is over
    // Parameters: None
    public int getNextPlayer() {
        if (isGameOver()) {
            return GAME_IS_OVER;
        }
        return isRedTurn ? PLAYER_1 : PLAYER_2;
    }
}
