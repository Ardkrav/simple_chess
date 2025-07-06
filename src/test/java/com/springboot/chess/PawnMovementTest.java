package com.springboot.chess;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.springboot.chess.model.Game;

public class PawnMovementTest {
    
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game("Partida1", "secreta123");
    }

    @Test
    void testFirstPawnMove() {
        Game game = new Game("test", "123");
        boolean resultado = game.movePiece("A2", "A3");
        assertTrue(resultado);
        assertEquals("WP1", game.getPiece("A3"));  
    }

    @Test
    void testFirstKingMove() {
        Game game = new Game("test", "123");

        game.movePiece("E2", "E3");  
        game.movePiece("E7", "E6");

        boolean resultado = game.movePiece("E1", "E2");  
        assertTrue(resultado);
        assertEquals("WK", game.getPiece("E2"));  
    }
    
     @Test
    void testPawnFirstMoveTwoCells() {
        boolean result = game.movePiece("A2", "A4");
        assertTrue(result);
        assertEquals("WP1", game.getPiece("A4")); 
    }

    @Test
    void testPawnFirstMoveOneCell() {
        boolean result = game.movePiece("B2", "B3");
        assertTrue(result);
        assertEquals("WP2", game.getPiece("B3"));  
    }

    @Test
    void testPawnSecondMoveTwoCellsFails() {
        game.movePiece("A2", "A3");
        game.movePiece("A7", "A6");  
        boolean result = game.movePiece("A3", "A5");  
        assertFalse(result);
    }

    @Test
    void testPawnDiagonalCapture() {
        String[][] board = new String[8][8];
        board[1][2] = "WP3*";
        board[2][3] = "BP5*";
        game.setBoard(board);

        boolean result = game.movePiece("C2", "D3");
        assertTrue(result);
        assertEquals("WP3", game.getPiece("D3"));
    }

    @Test
    void testPawnDiagonalWithoutCaptureFails() {
        // Solo un peón blanco en C2, diagonal vacía en D3
        String[][] board = new String[8][8];
        board[1][2] = "WP3*"; // C2
        game.setBoard(board);

        boolean result = game.movePiece("C2", "D3");  // No hay nada para capturar
        assertFalse(result);
    }

    @Test
    void testPawnMoveBackwardFails() {
        String[][] board = new String[8][8];
        board[4][0] = "WP1"; 
        game.setBoard(board);

        boolean result = game.movePiece("A4", "A3"); 
        assertFalse(result);
    }

    @Test
    void testBlackPawnForwardValid() {
        game.movePiece("A2", "A3");
        boolean result2 = game.movePiece("G7", "G6");

        assertTrue(result2);
        assertEquals("BP7", game.getPiece("G6")); 
    }

    @Test
    void testBlackPawnDiagonalCapture() {
        String[][] board = new String[8][8];
        board[6][2] = "BP3*"; // C7
        board[5][1] = "WP2";  // B6
        game.setBoard(board);

        game.setMovesCounter(1);  // Turno de negras

        boolean result = game.movePiece("C7", "B6");
        assertTrue(result);
        assertEquals("BP3", game.getPiece("B6"));
    }

}
