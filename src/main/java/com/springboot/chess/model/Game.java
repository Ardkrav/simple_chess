package com.springboot.chess.model;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Lob
    @Convert(converter = BoardConverter.class)
    private String[][] board;
    //private List movesRecord;
    private int moves;

    public Game() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String[][] getBoard() {
        return board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }
/* 
    public List getMovesRecord() {
        return movesRecord;
    }

    public void setMovesRecord(List movesRecord) {
        this.movesRecord = movesRecord;
    }
*/
    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public void initializeBoard() {
        //this.movesRecord = new LinkedList();
        this.moves = 0;

        board = new String[8][8];

        // Fila 0: piezas negras
        board[7] = new String[] { "BR", "BN", "BB", "BK", "BQ", "BB", "BN", "BR" };

        // Fila 1: peones negros
        for (int col = 0; col < 8; col++) {
            board[6][col] = "BP";
        }

        // Fila 6: peones blancos
        for (int col = 0; col < 8; col++) {
            board[1][col] = "WP";
        }

        // Fila 7: piezas blancas
        board[0] = new String[] { "WR", "WN", "WB", "WK", "WQ", "WB", "WN", "WR" };
    }
}
