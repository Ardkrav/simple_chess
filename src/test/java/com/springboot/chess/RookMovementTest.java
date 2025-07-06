package com.springboot.chess;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.springboot.chess.model.Game;

public class RookMovementTest {
    
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game("Partida1", "secreta123");
    }

    private void colocarTorre(String pos, String color) {
        String[][] board = new String[8][8];
        int[] position = Game.convertPos(pos);
        board[position[0]][position[1]] = color.equals("W") ? "WRK*" : "BRK*";
        game.setBoard(board);
        game.setMovesCounter(color.equals("W") ? 0 : 1);
    }

    @Test
    void testMoverTorreArribaSinObstaculos() {
        colocarTorre("D4", "W");
        boolean result = game.movePiece("D4", "D1");
        assertTrue(result);
        assertEquals("WRK", game.getPiece("D1"));
    }

    @Test
    void testMoverTorreAbajoSinObstaculos() {
        colocarTorre("D4", "W");
        boolean result = game.movePiece("D4", "D8");
        assertTrue(result);
        assertEquals("WRK", game.getPiece("D8"));
    }

    @Test
    void testMoverTorreIzquierdaSinObstaculos() {
        colocarTorre("D4", "W");
        boolean result = game.movePiece("D4", "A4");
        assertTrue(result);
        assertEquals("WRK", game.getPiece("A4"));
    }

    @Test
    void testMoverTorreDerechaSinObstaculos() {
        colocarTorre("D4", "W");
        boolean result = game.movePiece("D4", "H4");
        assertTrue(result);
        assertEquals("WRK", game.getPiece("H4"));
    }

    @Test
    void testTorreNoPuedeAtravesarAliado() {
        String[][] board = new String[8][8];
        board[3][3] = "WRK*";
        board[1][3] = "WP5";
        game.setBoard(board);
        game.setMovesCounter(0);

        boolean result = game.movePiece("D5", "D1");
        assertFalse(result);
    }

    @Test
    void testTorreNoPuedeAtravesarEnemigo() {
        String[][] board = new String[8][8];
        board[3][3] = "WRK*";
        board[1][3] = "BP3";
        game.setBoard(board);
        game.setMovesCounter(0);

        boolean result = game.movePiece("D5", "D1");
        assertFalse(result); 
    }

    @Test
    void testTorreCapturaEnemigoFinal() {
        String[][] board = new String[8][8];
        board[4][3] = "WRK*";
        board[7][3] = "BP3"; 
        game.setBoard(board);
        game.setMovesCounter(0);

        boolean result = game.movePiece("D5", "D8");
        assertTrue(result);
        assertEquals("WRK", game.getPiece("D8")); 
    }

}
