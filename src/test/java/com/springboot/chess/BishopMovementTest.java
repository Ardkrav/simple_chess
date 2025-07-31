package com.springboot.chess;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.springboot.chess.model.Game;

public class BishopMovementTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game("PartidaBishop", "secreta123");
    }

    private void placeBishop(String pos, String color) {
        String[][] board = new String[8][8];
        int[] position = Game.convertPos(pos);
        board[position[0]][position[1]] = color.equals("W") ? "WBK" : "BBK";
        game.setBoard(board);
        game.setMovesCounter(color.equals("W") ? 0 : 1);
    }

    @Test
    void testMoveTopLeftDiagonalWithoutObstacles() {
        placeBishop("D4", "W");
        boolean result = game.movePiece("D4", "A7");
        assertTrue(result);
        assertEquals("WBK", game.getPiece("A7"));
    }

    @Test
    void testMoveTopRightDiagonalWithoutObstacles() {
        placeBishop("D4", "W");
        boolean result = game.movePiece("D4", "G7");
        assertTrue(result);
        assertEquals("WBK", game.getPiece("G7"));
    }

    @Test
    void testMoveBottomLeftDiagonalWithoutObstacles() {
        placeBishop("D4", "W");
        boolean result = game.movePiece("D4", "A1");
        assertTrue(result);
        assertEquals("WBK", game.getPiece("A1"));
    }

    @Test
    void testMoveBottomRightDiagonalWithoutObstacles() {
        placeBishop("D4", "W");
        boolean result = game.movePiece("D4", "G1");
        assertTrue(result);
        assertEquals("WBK", game.getPiece("G1"));
    }

    @Test
    void testCannotJumpOverAlly() {
        String[][] board = new String[8][8];
        board[4][3] = "WBK";
        board[2][5] = "WP5";
        game.setBoard(board);
        game.setMovesCounter(0);

        boolean result = game.movePiece("D5", "F3");
        assertFalse(result);
    }

    @Test
    void testCannotJumpOverEnemy() {
        String[][] board = new String[8][8];
        board[4][3] = "WBK";
        board[3][4] = "BP5";
        game.setBoard(board);
        game.setMovesCounter(0);

        boolean result = game.movePiece("D5", "F3");
        assertFalse(result);
    }

    @Test
    void testCaptureEnemyAtDestination() {
        String[][] board = new String[8][8];
        board[4][3] = "WBK"; // D5
        board[0][7] = "BP1"; // A8
        game.setBoard(board);
        game.setMovesCounter(0);

        boolean result = game.movePiece("D5", "A8");
        assertTrue(result);
        assertEquals("WBK", game.getPiece("A8"));
    }

    @Test
    void testInvalidHorizontalMove() {
        placeBishop("D4", "W");
        boolean result = game.movePiece("D4", "A4");
        assertFalse(result);
    }

    @Test
    void testInvalidVerticalMove() {
        placeBishop("D4", "W");
        boolean result = game.movePiece("D4", "D8");
        assertFalse(result);
    }

    @Test
    void testInvalidNonStraightOrDiagonalMove() {
        placeBishop("D4", "W");
        boolean result = game.movePiece("D4", "E6");
        assertFalse(result);
    }

}
