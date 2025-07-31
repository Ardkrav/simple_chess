package com.springboot.chess;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.springboot.chess.model.Game;

public class KnightMovementTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game("Caballos", "clave123");
    }

    private void placeKnight(String pos, String color) {
        String[][] board = new String[8][8];
        int[] position = Game.convertPos(pos);
        board[position[0]][position[1]] = color.equals("W") ? "WNK" : "BNK";
        game.setBoard(board);
        game.setMovesCounter(color.equals("W") ? 0 : 1);
    }

    @Test
    void testValidKnightMoves() {
        placeKnight("D4", "W");

        // Todos los movimientos válidos desde D4
        String[] destinos = {"C6", "E6", "B5", "F5", "B3", "F3", "C2", "E2"};

        for (String destino : destinos) {
            setUp(); // reiniciar juego limpio
            placeKnight("D4", "W");
            assertTrue(game.movePiece("D4", destino), "Debe poder moverse a " + destino);
            assertEquals("WNK", game.getPiece(destino));
        }
    }

    @Test
    void testInvalidKnightMoves() {
        placeKnight("D4", "W");

        // Movimientos inválidos: en línea recta, diagonal, misma casilla, etc.
        String[] invalidos = {"D5", "D3", "C4", "E4", "C3", "E5", "D4"};

        for (String destino : invalidos) {
            setUp();
            placeKnight("D4", "W");
            assertFalse(game.movePiece("D4", destino), "No debe poder moverse a " + destino);
        }
    }

    @Test
    void testKnightCapture() {
        String[][] board = new String[8][8];
        board[4][3] = "WNK";   // D5
        board[2][4] = "BP5";   // E3
        game.setBoard(board);
        game.setMovesCounter(0);

        boolean result = game.movePiece("D5", "E3");
        assertTrue(result);
        assertEquals("WNK", game.getPiece("E3"));
    }

    @Test
    void testKnightJump() {
        String[][] board = new String[8][8];
        board[4][3] = "WNK";  // D5 (caballo)
        board[3][3] = "WP2";  // D4
        board[3][4] = "BP3";  // E4
        board[5][3] = "BP4";  // D6

        // Movimiento válido "por encima"
        game.setBoard(board);
        game.setMovesCounter(0);
        boolean result = game.movePiece("D5", "C7"); // Salta a C7
        assertTrue(result);
        assertEquals("WNK", game.getPiece("C7"));
    }
}
