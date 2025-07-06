package com.springboot.chess;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.springboot.chess.model.Game;

public class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game("Partida1", "secreta123");
    }

    @Test
    void testGameInitialization() {
        assertNotNull(game);
        assertEquals("Partida1", game.getName());
        assertEquals("secreta123", game.getPassword());
        assertEquals(0, game.getMovesCounter());
    }

    @Test
    void testBoardInitialization() {
        String[][] board = game.getBoard();

        assertNotNull(board);
        assertEquals(8, board.length);
        for (String[] row : board) {
            assertEquals(8, row.length);
        }

        // Verificar primeras filas (blancas)
        // Peones
        assertEquals("WP1*", board[1][0]);
        assertEquals("WP2*", board[1][1]);
        assertEquals("WP3*", board[1][2]);
        assertEquals("WP4*", board[1][3]);
        assertEquals("WP5*", board[1][4]);
        assertEquals("WP6*", board[1][5]);
        assertEquals("WP7*", board[1][6]);
        assertEquals("WP8*", board[1][7]);
        // Otras
        assertEquals("WRQ*", board[0][0]);
        assertEquals("WNQ", board[0][1]);
        assertEquals("WBQ", board[0][2]);
        assertEquals("WQ", board[0][3]);
        assertEquals("WK*", board[0][4]);
        assertEquals("WBK", board[0][5]);
        assertEquals("WNK", board[0][6]);
        assertEquals("WRK*", board[0][7]);

        // Filas intermedias vacías
        for (int i = 2; i <= 5; i++) {
            for (int j = 0; j < 8; j++) {
                assertNull(board[i][j], "board[" + i + "][" + j + "] must be null");
            }
        }

        // Verificar filas negras
        // Peones
        assertEquals("BP1*", board[6][0]);
        assertEquals("BP2*", board[6][1]);
        assertEquals("BP3*", board[6][2]);
        assertEquals("BP4*", board[6][3]);
        assertEquals("BP5*", board[6][4]);
        assertEquals("BP6*", board[6][5]);
        assertEquals("BP7*", board[6][6]);
        assertEquals("BP8*", board[6][7]);
        // Otras
        assertEquals("BRQ*", board[7][0]);
        assertEquals("BNQ", board[7][1]);
        assertEquals("BBQ", board[7][2]);
        assertEquals("BQ", board[7][3]);
        assertEquals("BK*", board[7][4]);
        assertEquals("BBK", board[7][5]);
        assertEquals("BNK", board[7][6]);
        assertEquals("BRK*", board[7][7]);
    }

    @Test
    void testSetAndGetBoard() {
        String[][] newBoard = new String[8][8];
        newBoard[0][0] = "X"; // Valor ficticio de prueba
        game.setBoard(newBoard);
        String[][] board = game.getBoard();

        assertSame(newBoard, board);
        assertEquals("X", board[0][0]);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (i != 0 && j != 0)
                    assertNull(board[i][j], "board[" + i + "][" + j + "] must be null");
            }
        }
    }

}
