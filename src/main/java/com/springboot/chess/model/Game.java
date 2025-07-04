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
    private String name;
    private String password;
    //private List movesRecord;
    private int movesCounter;

    public Game() {}

    public Game(String name, String password) {
        this.name = name;
        this.password = password;
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

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

/* 
    public List getMovesRecord() {
        return movesRecord;
    }

    public void setMovesRecord(List movesRecord) {
        this.movesRecord = movesRecord;
    }
*/
    public int getMovesCounter() {
        return movesCounter;
    }

    public void setMovesCounter(int moves) {
        this.movesCounter = moves;
    }

    public void initializeBoard() {
        //this.movesRecord = new LinkedList();
        this.movesCounter = 0;

        board = new String[8][8];

        // Fila 0: piezas negras
        board[7] = new String[] { "BRQ", "BNQ", "BBQ", "BQ", "BK", "BBK", "BNK", "BRK" };

        // Fila 1: peones negros
        for (int col = 0; col < 8; col++) {
            board[6][col] = "BP"+(col+1);
        }

        // Fila 6: peones blancos
        for (int col = 0; col < 8; col++) {
            board[1][col] = "WP"+(col+1);
        }

        // Fila 7: piezas blancas
        board[0] = new String[] { "WRQ", "WNQ", "WBQ", "WQ", "WK", "WBK", "WNK", "WRK" };
    }

    public boolean movePiece(String startPos, String endPos){
        int startCol = startPos.charAt(0)-'A';
        int startRow =  Character.getNumericValue(startPos.charAt(1))-1;
        int endCol = endPos.charAt(0)-'A';
        int endRow = Character.getNumericValue(endPos.charAt(1))-1;

        if (startRow < 0 || startRow > 7 || startCol < 0 || startCol > 7 ||
            endRow < 0 || endRow > 7 || endCol < 0 || endCol > 7) {
            System.out.println("Posición inválida");
            System.out.println("startRow: "+startRow+"startCol: "+startCol);
            System.out.println("endRow: "+endRow+"endCol: "+endCol);
            return false;
        }

        board[endRow][endCol] = board[startRow][startCol];
        board[startRow][startCol] = null;

        movesCounter++;

        return true;
    }
}
