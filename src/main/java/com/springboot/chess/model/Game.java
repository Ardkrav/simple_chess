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
    // private List movesRecord;
    private int movesCounter;

    public Game() {
    }

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
     * public List getMovesRecord() {
     * return movesRecord;
     * }
     * 
     * public void setMovesRecord(List movesRecord) {
     * this.movesRecord = movesRecord;
     * }
     */
    public int getMovesCounter() {
        return movesCounter;
    }

    public void setMovesCounter(int moves) {
        this.movesCounter = moves;
    }

    public void initializeBoard() {
        // this.movesRecord = new LinkedList();
        this.movesCounter = 0;

        board = new String[8][8];

        // Fila 0: piezas negras
        board[7] = new String[] { "BRQ*", "BNQ", "BBQ", "BQ", "BK*", "BBK", "BNK", "BRK*" };

        // Fila 1: peones negros
        for (int col = 0; col < 8; col++) {
            board[6][col] = "BP" + (col + 1) + "*";
        }

        // Fila 6: peones blancos
        for (int col = 0; col < 8; col++) {
            board[1][col] = "WP" + (col + 1) + "*";
        }

        // Fila 7: piezas blancas
        board[0] = new String[] { "WRQ*", "WNQ", "WBQ", "WQ", "WK*", "WBK", "WNK", "WRK*" };
    }

    public boolean movePiece(String startPos, String endPos) {
        boolean moved = false;
        int startCol = startPos.charAt(0) - 'A';
        int startRow = Character.getNumericValue(startPos.charAt(1)) - 1;
        int endCol = endPos.charAt(0) - 'A';
        int endRow = Character.getNumericValue(endPos.charAt(1)) - 1;
        String piece = board[startRow][startCol];

        if(piece==null){
            return false;
        }

        if (startRow < 0 || startRow > 7 || startCol < 0 || startCol > 7 ||
                endRow < 0 || endRow > 7 || endCol < 0 || endCol > 7) {
            System.out.println("Posición inválida");
            System.out.println("startRow: " + startRow + "startCol: " + startCol);
            System.out.println("endRow: " + endRow + "endCol: " + endCol);
            return false;
        }

        if (validateMove(startCol, startRow, endCol, endRow)) {
            if(piece.charAt(piece.length()-1) == '*'){
                piece = piece.substring(0, piece.length() - 1);
            }
            board[endRow][endCol] = piece;
            board[startRow][startCol] = null;
            movesCounter++;
            moved = true;
        }
        return moved;
    }

    private boolean validateMove(int startCol, int startRow, int endCol, int endRow) {
        String piece = board[startRow][startCol];
        boolean valid = true;

        if (!(piece.charAt(0) == 'W' && this.movesCounter % 2 == 0)
                && !(piece.charAt(0) == 'B' && this.movesCounter % 2 == 1)) {
            return false;
        }
        // Validar que es el turno del jugador que realiza el movimiento
        // Validar que la pieza que se mueve pertenece al jugador
        if (valid) {
            switch (piece.charAt(1)) {
                case 'P':
                    valid = validatePawnMove(startCol, startRow, endCol, endRow);
                    break;
                case 'R':
                    valid = validateRookMove(startCol, startRow, endCol, endRow);
                    break;
                case 'N':
                    valid = validateKnightMove(startCol, startRow, endCol, endRow);
                    break;
                case 'B':
                    valid = validateBishopMove(startCol, startRow, endCol, endRow);
                    break;
                case 'Q':
                    valid = validateQueenMove(startCol, startRow, endCol, endRow);
                    break;
                case 'K':
                    valid = validateKingMove(startCol, startRow, endCol, endRow);
                    break;
            }
        }

        return valid;
    }

    private boolean validatePawnMove(int startCol, int startRow, int endCol, int endRow) {
        boolean valid = false;
        // Si el peon es blanco moves es par, el peon se puede mover 1 fila arriba.
        // Sino se puede mover una fila abajo.
        char color = board[startRow][startCol].charAt(0);
        boolean firstMove = false;
        int range = -2 * (this.movesCounter % 2) + 1;
        if(board[startRow][startCol].length() == 4){
            firstMove = true;
        }

        if (endCol == startCol
                && (endRow == startRow + range 
                || (firstMove && endRow == startRow + range*2 
                && board[startRow+range][startCol] == null))
                && board[endRow][endCol] == null) {
            valid = true;
        } else if ((endCol == startCol + 1 || endCol == startCol - 1)
                && endRow == startRow + range
                && board[endRow][endCol] != null
                && board[endRow][endCol].charAt(0) != color) {
            // Falta agregar validacion de on passant
            valid = true;
        }

        return valid;
    }

    private boolean validateRookMove(int startCol, int startRow, int endCol, int endRow) {
        return true;
    }

    private boolean validateKnightMove(int startCol, int startRow, int endCol, int endRow) {
        return true;
    }

    private boolean validateBishopMove(int startCol, int startRow, int endCol, int endRow) {
        return true;
    }

    private boolean validateQueenMove(int startCol, int startRow, int endCol, int endRow) {
        return true;
    }

    private boolean validateKingMove(int startCol, int startRow, int endCol, int endRow) {
        return true;
    }

}
