package com.iapp.chess.model;

import java.io.Serializable;

public class BoardMatrix implements Serializable {

    public static final byte PAWN = 3;
    public static final byte ROOK = 4;
    public static final byte KNIGHT = 5;
    public static final byte BISHOP = 6;
    public static final byte QUEEN = 7;
    public static final byte KING = 8;

    private static final byte WHITE_PAWN = -PAWN;
    private static final byte WHITE_ROOK = -ROOK;
    private static final byte WHITE_KNIGHT = -KNIGHT;
    private static final byte WHITE_BISHOP = -BISHOP;
    private static final byte WHITE_QUEEN = -QUEEN;
    private static final byte WHITE_KING = -KING;

    public static final byte CAGE = 1;
    private static final byte WALL = 0;

    private static final byte BLACK_PAWN = PAWN;
    private static final byte BLACK_ROOK = ROOK;
    private static final byte BLACK_KNIGHT = KNIGHT;
    private static final byte BLACK_BISHOP = BISHOP;
    private static final byte BLACK_QUEEN = QUEEN;
    private static final byte BLACK_KING = KING;

    private static final int FIGURE_SIZE = 12;
    private static final int ID_SIZE = 8;
    private static final int MARGIN_WALL = 2;

    private final byte[][] id;

    private final byte[][] matrix;
    private Color upper;

    BoardMatrix(Color upper) {
        this.upper = upper;

        id = new byte[][] {
                {0, 1, 2, 3, 4, 5, 6, 7},
                {8, 9, 10, 11, 12, 13, 14, 15},
                {-1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1, -1},
                {16, 17, 18, 19, 20, 21, 22, 23},
                {24, 25, 26, 27, 28, 29, 30, 31},};

        matrix = new byte[][] {
                        {WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL},
                        {WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL},
                        {WALL, WALL, BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK, WALL, WALL},
                        {WALL, WALL, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, WALL, WALL},
                        {WALL, WALL, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, WALL, WALL},
                        {WALL, WALL, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, WALL, WALL},
                        {WALL, WALL, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, WALL, WALL},
                        {WALL, WALL, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, WALL, WALL},
                        {WALL, WALL, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WALL, WALL},
                        {WALL, WALL, WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK, WALL, WALL},
                        {WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL},
                        {WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL},
        };

        if (upper == Color.WHITE) {
            byte[] whiteLine1  = matrix[8];
            byte[] whiteLine2 = matrix[9];

            matrix[9] = matrix[2];
            matrix[8] = matrix[3];

            matrix[2] = whiteLine2;
            matrix[3] = whiteLine1;

            matrix[2][5] = WHITE_KING;
            matrix[2][6] = WHITE_QUEEN;

            matrix[9][5] = BLACK_KING;
            matrix[9][6] = BLACK_QUEEN;
        }
    }

    void updateColor(Color upper) {
        if (this.upper == upper) return;
        this.upper = upper;

        for (int i = 0; i < id.length; i++) {
            for (int j = 0; j < id[i].length; j++) {
                if (id[i][j] != -1 && matrix[i + MARGIN_WALL][j + MARGIN_WALL] != CAGE) {
                    id[i][j] = (byte) (31 - id[i][j]);
                    matrix[i + MARGIN_WALL][j + MARGIN_WALL] = (byte) -matrix[i + MARGIN_WALL][j + MARGIN_WALL];
                }
            }
        }
    }

    public byte[][] getMatrix() {
        byte[][] newMatrix = new byte[ID_SIZE][ID_SIZE];
        for (int i = MARGIN_WALL; i < FIGURE_SIZE - MARGIN_WALL; i++) {
            System.arraycopy(matrix[i], MARGIN_WALL, newMatrix[i - MARGIN_WALL], 0, ID_SIZE);
        }
        return newMatrix;
    }

    private BoardMatrix(Color upper, byte[][] matrix, byte[][] id) {
        this.upper = upper;
        this.matrix = matrix;
        this.id = id;
    }

    byte getId(int x, int y) {
        return id[y][x];
    }

    byte getFigure(int x, int y) {
        return matrix[y + MARGIN_WALL][x + MARGIN_WALL];
    }

    byte getFigure(Move move) {
        return getFigure(move.getFigureX(), move.getFigureY());
    }

    void setFigure(int figureX, int figureY, int x, int y) {
        id[y][x] = id[figureY][figureX];
        matrix[y + MARGIN_WALL][x + MARGIN_WALL] = matrix[figureY + MARGIN_WALL][figureX + MARGIN_WALL];
    }

    void setFigure(int figureX, int figureY, byte figure) {
        if (figure == CAGE) throw new IllegalArgumentException();
        matrix[figureY + MARGIN_WALL][figureX + MARGIN_WALL] = figure;
    }

    void setCage(int x, int y) {
        id[y][x] = -1;
        matrix[y + MARGIN_WALL][x + MARGIN_WALL] = CAGE;
    }

    BoardMatrix cloneMatrix() {
        byte[][] newMatrix = new byte[FIGURE_SIZE][FIGURE_SIZE];
        for (int i = 0; i < matrix.length; i++) {
            newMatrix[i] = matrix[i].clone();
        }

        byte[][] newId = new byte[ID_SIZE][ID_SIZE];
        for (int i = 0; i < id.length; i++) {
            newId[i] = id[i].clone();
        }

        return new BoardMatrix(upper, newMatrix, newId);
    }
}
