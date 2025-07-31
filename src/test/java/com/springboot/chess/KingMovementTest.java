package com.springboot.chess;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.springboot.chess.model.Game;

public class KingMovementTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game("Rey", "clave321");
    }

    private void placeKing(String pos, String color) {
        String[][] board = new String[8][8];
        int[] position = Game.convertPos(pos);
        board[position[0]][position[1]] = color.equals("W") ? "WK*" : "BK*";
        game.setBoard(board);
        game.setMovesCounter(color.equals("W") ? 0 : 1);
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
    void testValidKingMoves() {
        placeKing("D4", "W");

        // Todas las casillas adyacentes válidas
        String[] destinos = {"C3", "C4", "C5", "D3", "D5", "E3", "E4", "E5"};

        for (String destino : destinos) {
            setUp();
            placeKing("D4", "W");
            assertTrue(game.movePiece("D4", destino), "Debe poder moverse a " + destino);
            assertEquals("WK", game.getPiece(destino));
        }
    }

    @Test
    void testInvalidKingMoves() {
        placeKing("D4", "W");

        // Movimientos inválidos: más de una casilla
        String[] invalidos = {"D6", "F4", "B4", "A1", "F6", "D1", "D4"};

        for (String destino : invalidos) {
            setUp();
            placeKing("D4", "W");
            assertFalse(game.movePiece("D4", destino), "No debe poder moverse a " + destino);
        }
    }

    @Test
    void testKingCaptures() {
        String[][] board = new String[8][8];
        board[4][3] = "WK*";   // D5
        board[5][4] = "BP5";   // E6
        game.setBoard(board);
        game.setMovesCounter(0);

        boolean result = game.movePiece("D5", "E6");
        assertTrue(result);
        assertEquals("WK", game.getPiece("E6"));
    }

    @Test
    void testKingMovesToTread() {
        // Rey en E1, torre enemiga en E8 atacando la columna E
        String[][] board = new String[8][8];
        board[0][4] = "WK*";  // E1
        board[7][4] = "BRK*"; // E8 (enemiga)
        game.setBoard(board);

        boolean result = game.movePiece("E1", "E2");
        assertFalse(result, "El rey no puede moverse a una casilla bajo ataque");
    }

    @Test
    void testNeglectKing() {
        // Rey en E1, torre enemiga en E8, peón bloqueando el jaque
        String[][] board = new String[8][8];
        board[0][4] = "WK*";  // E1
        board[1][4] = "WP3";  // E2
        board[7][4] = "BRK*"; // E8
        game.setBoard(board);

        boolean result = game.movePiece("E2", "E3"); // Mueve el peón y deja al rey expuesto
        assertFalse(result, "No debe poder dejar al rey en jaque con una jugada propia");
    }

    @Test
    void testKingSideCastling() {
        // Rey blanco en E1, torre blanca en H1, sin piezas entre medio
        String[][] board = new String[8][8];
        board[0][4] = "WK*";  // E1
        board[0][7] = "WRK*"; // H1
        game.setBoard(board);

        boolean result = game.movePiece("E1", "G1"); // Enroque corto
        assertTrue(result);
        assertEquals("WK", game.getPiece("G1"));
        assertEquals("WRK", game.getPiece("F1")); // Torre se mueve a F1
    }

    @Test
    void testQueenSideCastling() {
        // Rey blanco en E1, torre blanca en A1, sin piezas entre medio
        String[][] board = new String[8][8];
        board[0][4] = "WK*";  // E1
        board[0][0] = "WRK*"; // A1
        game.setBoard(board);

        boolean result = game.movePiece("E1", "C1"); // Enroque largo
        assertTrue(result);
        assertEquals("WK", game.getPiece("C1"));
        assertEquals("WRK", game.getPiece("D1")); // Torre se mueve a D1
    }

    @Test
    void testKingsSecondMoveCastling() {
        String[][] board = new String[8][8];
        board[0][4] = "WK";   // E1 sin asterisco, ya se movió
        board[0][7] = "WRK*"; // H1
        game.setBoard(board);

        boolean result = game.movePiece("E1", "G1");
        assertFalse(result, "No debe enrocar si el rey ya se movió");
    }

    @Test
    void testRooksSecondMoveCastling() {
        String[][] board = new String[8][8];
        board[0][4] = "WK*";   // E1
        board[0][7] = "WRK";   // H1 sin asterisco, ya se movió
        game.setBoard(board);

        boolean result = game.movePiece("E1", "G1");
        assertFalse(result, "No debe enrocar si la torre ya se movió");
    }

    @Test
    void testCastlingWithTread() {
        // Rey en E1, torre en H1, torre enemiga en F8 (atacando F1)
        String[][] board = new String[8][8];
        board[0][4] = "WK*";   // E1
        board[0][7] = "WRK*";  // H1
        board[7][5] = "BRK*";  // F8
        game.setBoard(board);

        boolean result = game.movePiece("E1", "G1");
        assertFalse(result, "No debe enrocar si pasa por una casilla amenazada");
    }
}
