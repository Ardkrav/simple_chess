package com.springboot.chess.model;

import java.io.Console;
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
    private int movesCounter; // Eliminar y usar movesRecord en su lugar

    public Game() {
    }

    public Game(String name, String password) {
        this.name = name;
        this.password = password;
        this.initializeBoard();
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

    public String getPiece(String algebraicPos) {
        int[] positionIndexes = convertPos(algebraicPos);
        if (!validatePos(algebraicPos)) {
            return "";
        }
        return board[positionIndexes[0]][positionIndexes[1]];
    }

    public boolean setPiece(String algebraicPos, String piece) {
        int[] positionIndexes;
        if (!validatePos(algebraicPos))
            return false;
        positionIndexes = convertPos(algebraicPos);
        board[positionIndexes[0]][positionIndexes[1]] = piece;

        return true;
    }

    public static int[] convertPos(String algebraicPos) {
        int col = algebraicPos.charAt(0) - 'A';
        int row = Character.getNumericValue(algebraicPos.charAt(1)) - 1;
        return new int[] { row, col };
    }

    public static boolean validatePos(String algebraicPos) {
        int[] indexes = convertPos(algebraicPos);
        if (indexes[0] < 0 || indexes[0] > 7 || indexes[1] < 0 || indexes[1] > 7)
            return false;

        return true;
    }

    private void initializeBoard() {
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
        String piece;
        String auxPiece = null;

        if (!validatePos(startPos) || !validatePos(endPos)) {
            return false;
        }

        piece = this.getPiece(startPos);

        if (piece == null) {
            return false;
        }

        if (validateMove(startPos, endPos)) {
            if (piece.charAt(piece.length() - 1) == '*') {
                piece = piece.substring(0, piece.length() - 1);
            }
            
            // Si muevo peon a borde del tablero, promociona
            if(piece.charAt(1) == 'P' && (endPos.contains("8") || endPos.contains("1"))){
                piece = piece.replace('P', 'Q');
            }

            this.setPiece(endPos, piece);
            this.setPiece(startPos, auxPiece);
            movesCounter++;
            moved = true;
        }
        return moved;
    }

    private boolean validateMove(String startPos, String endPos) {
        String piece = this.getPiece(startPos);
        boolean valid = true;
        int[] startIndexes;
        int[] endIndexes;

        if (startPos.equals(endPos)) {
            return false;
        }
        // Validar que es el turno del jugador que realiza el movimiento
        char[] turn = { 'W', 'B' };
        if (piece.charAt(0) != turn[this.movesCounter % 2]) {
            return false;
        }
        if (this.getPiece(endPos) != null
                && piece.charAt(0) == this.getPiece(endPos).charAt(0)) {
            return false;
        }

        // Validar que la pieza que se mueve pertenece al jugador
        startIndexes = convertPos(startPos);
        endIndexes = convertPos(endPos);

        switch (piece.charAt(1)) {
            case 'P':
                valid = validatePawnMove(startIndexes, endIndexes);
                break;
            case 'R':
                valid = validateRookMove(startIndexes, endIndexes);
                break;
            case 'N':
                valid = validateKnightMove(startIndexes, endIndexes);
                break;
            case 'B':
                valid = validateBishopMove(startIndexes, endIndexes);
                break;
            case 'Q':
                valid = validateQueenMove(startIndexes, endIndexes);
                break;
            case 'K':
                valid = validateKingMove(startIndexes, endIndexes);
                break;
        }

        return valid;
    }

    private boolean validatePawnMove(int[] startIndexes, int[] endIndexes) {
        boolean valid = false;
        // Si el peon es blanco moves es par, el peon se puede mover 1 fila arriba.
        // Sino se puede mover una fila abajo.
        boolean firstMove = false;
        int range = -2 * (this.movesCounter % 2) + 1;
        if (board[startIndexes[0]][startIndexes[1]].length() == 4) {
            firstMove = true;
        }

        if (endIndexes[1] == startIndexes[1]
                && (endIndexes[0] == startIndexes[0] + range
                        || (firstMove && endIndexes[0] == startIndexes[0] + range * 2
                                && board[startIndexes[0] + range][startIndexes[1]] == null))
                && board[endIndexes[0]][endIndexes[1]] == null) {
            valid = true;
        } else if ((endIndexes[1] == startIndexes[1] + 1 || endIndexes[1] == startIndexes[1] - 1)
                && endIndexes[0] == startIndexes[0] + range
                && board[endIndexes[0]][endIndexes[1]] != null) {
            // Falta agregar validacion de en passant
            valid = true;
        }

        return valid;
    }

    private boolean validateRookMove(int[] startIndexes, int[] endIndexes) {
        int factor;
        if (startIndexes[1] > endIndexes[1] || startIndexes[0] > endIndexes[0]) {
            factor = -1;
        } else {
            factor = 1;
        }
        if (startIndexes[1] == endIndexes[1] || startIndexes[0] == endIndexes[0]) {
            for (int i = startIndexes[1]; i != endIndexes[1]; i += factor) {
                if (i != startIndexes[1] && board[startIndexes[0]][i] != null) {
                    return false;
                }
            }
            for (int i = startIndexes[0]; i != endIndexes[0]; i += factor) {
                if (i != startIndexes[0] && board[i][startIndexes[1]] != null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean validateKnightMove(int[] startIndexes, int[] endIndexes) {
        if ((Math.abs(startIndexes[1] - endIndexes[1]) == 1 && Math.abs(startIndexes[0] - endIndexes[0]) == 2)
                || (Math.abs(startIndexes[1] - endIndexes[1]) == 2 && Math.abs(startIndexes[0] - endIndexes[0]) == 1)) {
            return true;
        }
        return false;
    }

    private boolean validateBishopMove(int[] startIndexes, int[] endIndexes) {
        int colFactor;
        int rowFactor;
        int i;
        int j;

        if (Math.abs(startIndexes[0] - endIndexes[0]) != Math.abs(startIndexes[1] - endIndexes[1])) {
            return false;
        }

        if (startIndexes[1] < endIndexes[1]) {
            colFactor = 1;
        } else {
            colFactor = -1;
        }
        if (startIndexes[0] < endIndexes[0]) {
            rowFactor = 1;
        } else {
            rowFactor = -1;
        }

        i = startIndexes[0] + rowFactor;
        j = startIndexes[1] + colFactor;

        while (i != endIndexes[0] && j != endIndexes[1]) {
            if (board[i][j] != null) {
                return false;
            }
            i += rowFactor;
            j += colFactor;
        }

        return true;
    }

    private boolean validateQueenMove(int[] startIndexes, int[] endIndexes) {
        return validateBishopMove(startIndexes, endIndexes)
                || validateRookMove(startIndexes, endIndexes);
    }

    private boolean validateKingMove(int[] startIndexes, int[] endIndexes) {
        // Validar si el movimiento es valido
        if ((Math.abs(startIndexes[1] - endIndexes[1]) <= 1 && Math.abs(startIndexes[0] - endIndexes[0]) <= 1)) {
            return true;
        }
        
        // Comprobar enroque
        if ((Math.abs(startIndexes[1] - endIndexes[1]) == 2 && Math.abs(startIndexes[0] - endIndexes[0]) == 0)
            && board[startIndexes[0]][startIndexes[1]].contains("*")) {
            
            if(endIndexes[1] == 2){
                String rook = board[startIndexes[0]][0];
                if (!rook.contains("*")) return false;
                board[startIndexes[0]][3] = rook.substring(0, rook.length() - 1);
                board[startIndexes[0]][0] = null;
            }
            else{
                String rook = board[startIndexes[0]][7];
                if (!rook.contains("*")) return false;
                board[startIndexes[0]][5] = rook.substring(0, rook.length() - 1);
                board[startIndexes[0]][7] = null;
            }

            return true;
        }

        return false;
    }

}
