package com.springboot.chess;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.springboot.chess.model.Game;

public class GeneralMovementTest {
    
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game("Partida1", "secreta123");
    }

     @Test
    void testWrongTurn() {
        Game game = new Game("test", "123");
        boolean resultado = game.movePiece("A7", "A6"); 
        assertFalse(resultado); 
    }

    @Test
    void testMoveToSamePosition() {
        Game game = new Game("test", "123");
        boolean resultado = game.movePiece("E2", "E2");
        assertFalse(resultado);
    }

    @Test
    void testCaptureAlly() {
        Game game = new Game("test", "123");
        game.movePiece("E2", "E3");
        game.movePiece("E7", "E6");
        boolean resultado = game.movePiece("D2", "E3");  
        assertFalse(resultado);
    }

}
