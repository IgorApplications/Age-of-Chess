package com.iapp.chess.model;

import com.badlogic.gdx.utils.Array;

import java.io.Serializable;
import java.util.*;

public class Game implements Serializable {

    private final boolean[] pawns = {false, false, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false};
    private final boolean[] rooks = {false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false, false};
    private final boolean[] knights = {false, false, false, true, false, false, false, false, false, false, false, false, false, true, false, false, false};
    private final boolean[] bishops = {false, false, true, false, false, false, false, false, false, false, false, false, false, false, true, false, false};
    private final boolean[] queens = {false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false};
    private final boolean[] kings = {true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true};
    private final boolean[] cages = {false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false};

    private Color upper;
    private Color lower;

    private Color colorMove = Color.WHITE;
    private BoardMatrix current;
    private final LinkedList<BoardMatrix> matrices;

    public Game(Color upper) {
        this.upper = upper;
        lower = reverse(upper);

        current = new BoardMatrix(upper);
        matrices = new LinkedList<>();
    }

    private Game(Color colorMove, Color upper, Color lower, BoardMatrix current,
                 LinkedList<BoardMatrix> matrices) {
        this.colorMove = colorMove;
        this.upper = upper;
        this.lower = lower;
        this.current = current;
        this.matrices = matrices;
    }

    public Color getUpper() {
        return upper;
    }

    public Color getLower() {
        return lower;
    }

    public Color getColorMove() {
        return colorMove;
    }

    public void updateUpperColor(Color upper) {
        if (this.upper == upper) return;

        this.upper = upper;
        lower = reverse(upper);
        if (!matrices.isEmpty()) {
            colorMove = reverse(colorMove);
        }

        for (BoardMatrix matrix : matrices) {
            matrix.updateColor(upper);
        }
        current.updateColor(upper);
    }

    public void makeMove(Move move) {
        colorMove = reverse(colorMove);
        matrices.offer(current.cloneMatrix());

        if (isTakeOnPass(move)) {
            current.setCage(move.getMoveX(), move.getFigureY());
        }

        if (isCastleMove(move)) {
            if (move.getMoveX() < move.getFigureX()) {
                current.setFigure(0, move.getFigureY(), move.getMoveX() + 1, move.getFigureY());
                current.setCage(0, move.getFigureY());
            } else {
                current.setFigure(7, move.getFigureY(), move.getMoveX() - 1, move.getFigureY());
                current.setCage(7, move.getFigureY());
            }
        }

        current.setFigure(move.getFigureX(), move.getFigureY(), move.getMoveX(), move.getMoveY());
        current.setCage(move.getFigureX(),move.getFigureY());
    }

    public Array<Move> getMoves(int x, int y) {
        Array<Move> moves = getFigureMoves(x, y);

        byte[] position = getKingPosition(colorMove);
        if (isCheckKing(position[0], position[1]) || isKing(x, y)) {
            moves = getMovesToSaveKing(moves);
        }

        return moves;
    }

    private Array<Move> getFigureMoves(int x, int y) {
        if (getColor(x, y) != colorMove) return new Array<>(0);

        Array<Move> moves;
        if (isPawn(x, y)) {
            moves = getPawnMoves(x, y);
        }
        else if (isRook(x, y)) {
            moves = getRookMoves(x, y);
        }
        else if (isKnight(x, y)) {
            moves = getKnightMoves(x, y);
        }
        else if (isBishop(x, y)) {
            moves =  getBishopMoves(x, y);
        }
        else if (isQueen(x, y)) {
            moves = getQueenMoves(x, y);
        }
        else if (isKing(x, y)) {
            moves = getKingMoves(x, y);

            if (!isCheckKing(x, y)) {
                moves.addAll(getCastleMoves(x, y));
            }
        }
        else {
            moves = new Array<>(0);
        }

        return moves;
    }

    public byte[][] getMatrix() {
        return current.getMatrix();
    }

    public byte[][] getLastMatrix(int depth) {
        return matrices.get(matrices.size() - depth).getMatrix();
    }

    public boolean isPawn(int x, int y) {
        return pawns[current.getFigure(x, y) + 8];
    }

    public boolean isRook(int x, int y) {
        return rooks[(current.getFigure(x, y)) + 8];
    }

    public boolean isKnight(int x, int y) {
        return knights[current.getFigure(x, y) + 8];
    }

    public boolean isBishop(int x, int y) {
        return bishops[current.getFigure(x, y) + 8];
    }

    public boolean isQueen(int x, int y) {
        return queens[current.getFigure(x, y) + 8];
    }

    public boolean isKing(int x, int y) {
        return kings[current.getFigure(x, y) + 8];
    }

    public boolean isCage(int x, int y) {
        return cages[current.getFigure(x, y) + 8];
    }

    public Color getColor(int x, int y) {
        if (current.getFigure(x, y) < -2) return Color.WHITE;
        else if (current.getFigure(x, y) > 2) return Color.BLACK;
        return null;
    }

    public int getTurn() {
        return matrices.size() / 2 + 1;
    }

    public int getMove() {
        return matrices.size();
    }

    public byte getId(int x, int y) {
        return current.getId(x, y);
    }

    public void cancelMove() {
        if (matrices.isEmpty()) return;

        current = matrices.removeLast();
        colorMove = reverse(colorMove);
    }

    public boolean isUpdated(Move move) {
        return isPawn(move.getFigureX(), move.getFigureY()) && (move.getMoveY() == 0 || move.getMoveY() == 7);
    }

    public void updatePawn(int pawnX, int pawnY, byte type) {
        int sign = current.getFigure(pawnX, pawnY) > 0 ? 1 : -1;
        current.setFigure(pawnX, pawnY, (byte) (type * sign));
    }

    public Color getColorCheckKing() {
        byte[][] matrix = current.getMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (isCheckKing(j, i)) {
                    return getColor(j, i);
                }
            }
        }
        return null;
    }

    public boolean isCheckKing(int kingX, int kingY) {
        if (!isKing(kingX, kingY)) return false;
        byte[][] matrix = getMatrix();

        Color saved = colorMove;
        colorMove = reverse(getColor(kingX, kingY));

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Array<Move> moves;

                if (isKing(j, i)) {
                    moves = getKingMoves(j, i);
                } else {
                    moves = getFigureMoves(j, i);
                }

                for (Move move : moves) {
                    if (move.getMoveX() == kingX && move.getMoveY() == kingY) {
                        colorMove = saved;
                        return true;
                    }
                }
            }
        }

        colorMove = saved;
        return false;
    }

    public boolean isFinish() {
        byte[][] matrix = current.getMatrix();

        int count = 0;
        boolean finish = true;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (!isCage(j, i)) count++;

                if (!getMoves(j, i).isEmpty()) {
                    finish = false;
                }
            }
        }

        return finish || count == 2;
    }

    public boolean isCastleMove(Move move) {
        return getCastleMoves(move.getFigureX(), move.getFigureY()).contains(move, false);
    }

    public Game cloneGame() {
        LinkedList<BoardMatrix> cloneMatrices = new LinkedList<>();
        for (BoardMatrix boardMatrix : matrices) {
            cloneMatrices.add(boardMatrix.cloneMatrix());
        }

        return new Game(colorMove, upper, lower, current.cloneMatrix(), cloneMatrices);
    }

    public Color reverse(Color first) {
        return first == Color.BLACK ? Color.WHITE : Color.BLACK;
    }

    private Array<Move> getQueenMoves(int x, int y) {
        Array<Move> queenMoves = getRookMoves(x, y);
        queenMoves.addAll(getBishopMoves(x, y));

        return queenMoves;
    }

    private Array<Move> getKnightMoves(int x, int y) {
        Array<Move> knightMoves = new Array<>();

        addValidKnightMove(knightMoves, x, y, x + 1, y + 2);
        addValidKnightMove(knightMoves, x, y, x - 1, y + 2);

        addValidKnightMove(knightMoves, x, y, x + 1, y - 2);
        addValidKnightMove(knightMoves, x, y, x - 1, y - 2);

        addValidKnightMove(knightMoves, x, y, x + 2, y + 1);
        addValidKnightMove(knightMoves, x, y, x + 2, y - 1);

        addValidKnightMove(knightMoves, x, y, x - 2, y + 1);
        addValidKnightMove(knightMoves, x, y, x - 2, y - 1);

        return knightMoves;
    }

    private void addValidKnightMove(Array<Move> knightMoves, int x, int y, int moveX, int moveY) {
        Color color = getColor(moveX, moveY);
        if ((color != null && color != colorMove) || isCage(moveX, moveY)) {
            knightMoves.add(new Move(x, y, moveX, moveY));
        }
    }

    private Array<Move> getBishopMoves(int x, int y) {
        Array<Move> bishopMoves = new Array<>();
        int j = x + 1, i = y + 1;

        while (j < 8 && i < 8) {
            if (getColor(j, i) != colorMove) {
                bishopMoves.add(new Move(x, y, j, i));
            }

            if (!isCage(j, i)) break;

            j++;
            i++;
        }

        j = x + 1;
        i = y - 1;
        while (j < 8 && i >= 0) {
            if (getColor(j, i) != colorMove) {
                bishopMoves.add(new Move(x, y, j, i));
            }

            if (!isCage(j, i)) break;

            j++;
            i--;
        }

        j = x - 1;
        i = y + 1;
        while (j >= 0 && i < 8) {
            if (getColor(j, i) != colorMove) {
                bishopMoves.add(new Move(x, y, j, i));
            }

            if (!isCage(j, i)) break;

            j--;
            i++;
        }

        j = x - 1;
        i = y - 1;
        while (j >= 0 && i >= 0 ) {
            if (getColor(j, i) != colorMove) {
                bishopMoves.add(new Move(x, y, j, i));
            }

            if (!isCage(j, i)) break;

            j--;
            i--;
        }

        return bishopMoves;
    }

    private Array<Move> getRookMoves(int x, int y) {
        Array<Move> rookMoves = new Array<>();

        for (int i = y - 1; i >= 0; i--) {
            if (getColor(x, i) != colorMove) {
                rookMoves.add(new Move(x, y, x, i));
            }

            if (!isCage(x, i)) break;
        }

        for (int i = y + 1; i < 8; i++) {
            if (getColor(x, i) != colorMove) {
                rookMoves.add(new Move(x, y, x, i));
            }

            if (!isCage(x, i)) break;
        }

        for (int i = x - 1; i >= 0; i--) {
            if (getColor(i, y) != colorMove) {
                rookMoves.add(new Move(x, y, i, y));
            }

            if (!isCage(i, y)) break;
        }

        for (int i = x + 1; i < 8; i++) {
            if (getColor(i, y) != colorMove) {
                rookMoves.add(new Move(x, y, i, y));
            }

            if (!isCage(i, y)) break;
        }

        return rookMoves;
    }

    private Array<Move> getKingMoves(int x, int y) {
        Array<Move> kingMoves = new Array<>();
        int sign = colorMove == Color.BLACK ? -1 : 1;

        addKingMove(kingMoves, x, y, x - 1, y, sign);
        addKingMove(kingMoves, x, y, x - 1, y - 1, sign);
        addKingMove(kingMoves, x, y, x, y - 1, sign);
        addKingMove(kingMoves, x, y, x + 1, y - 1, sign);
        addKingMove(kingMoves, x, y, x + 1, y, sign);
        addKingMove(kingMoves, x, y, x + 1, y + 1, sign);
        addKingMove(kingMoves, x, y, x, y + 1, sign);
        addKingMove(kingMoves, x, y, x - 1, y + 1, sign);

        return kingMoves;
    }

    private Array<Move> getPawnMoves(int x, int y) {
        Array<Move> pawnMoves = new Array<>();

        int direction = isUpperColor(x, y) ? 1 : -1;
        Color figureColor = getColor(x, y);

        if (current.getFigure(x,y + direction) == BoardMatrix.CAGE) {
            pawnMoves.add(new Move(x, y, x, y + direction));

            if ((y == 1 || y == 6) && current.getFigure(x,y + direction * 2) == BoardMatrix.CAGE) {
                pawnMoves.add(new Move(x, y, x, y + direction * 2));
            }
        }

        Color left = getColor(x - 1, y + direction);
        if (left != null && left != colorMove) {
            pawnMoves.add(new Move(x, y, x - 1, y + direction));
        }

        Color right = getColor(x + 1, y + direction);
        if (right != null && right != colorMove) {
            pawnMoves.add(new Move(x, y, x + 1, y + direction));
        }

        if (((y == 3 && figureColor == lower) || (y == 4 && figureColor == upper))
                && ((getColor(x - 1, y)) != colorMove && getColor(x + 1, y) != colorMove)) {

            Move move = findChange();

            if ((move.getFigureY() + -direction * 2) == move.getMoveY()) {

                if (x - 1 == move.getMoveX() && y == move.getMoveY()) {
                    pawnMoves.add(new Move(x, y, x - 1, y + direction));
                }

                if (x + 1 == move.getMoveX() && y == move.getMoveY()) {
                    pawnMoves.add(new Move(x, y, x + 1, y + direction));
                }
            }
        }

        return pawnMoves;
    }

    private byte[] getKingPosition(Color color) {
        byte[][] matrix = current.getMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (isKing(j, i) && getColor(j, i) == color) {
                    return new byte[]{(byte) j, (byte) i};
                }
            }
        }
        return new byte[]{-1, -1};
    }

    private void addKingMove(Array<Move> moves, int figureX, int figureY, int x, int y, int sign) {
        if (current.getFigure(x, y) * sign >= 1 || current.getFigure(x, y) == BoardMatrix.CAGE) {
            moves.add(new Move(figureX, figureY, x, y));
        }
    }

    private Array<Move> getCastleMoves(int figureX, int figureY) {
        if (!isKing(figureX, figureY)) return new Array<>(0);

        Color kingColor = getColor(figureX, figureY);
        Array<Move> checkMoves = new Array<>();

        if ((kingColor == upper && figureY == 0) || (kingColor == lower && figureY == 7)) {
            if (figureX == 3) {
                if (checkTreePosition(figureX, figureY, -1)) {
                    checkMoves.add(new Move(figureX, figureY, figureX - 2, figureY));
                }

                if (checkFourPosition(figureX, figureY, 1)) {
                    checkMoves.add(new Move(figureX, figureY, figureX + 2, figureY));
                }
            } else if (figureX == 4) {
                if (checkFourPosition(figureX, figureY, -1)) {
                    checkMoves.add(new Move(figureX, figureY, figureX - 2, figureY));
                }

                if (checkTreePosition(figureX, figureY, 1)) {
                    checkMoves.add(new Move(figureX, figureY, figureX + 2, figureY));
                }
            }
        }

        return checkMoves;
    }

    private boolean checkFourPosition(int figureX, int figureY, int sign) {
        if (isCage(figureX + sign, figureY) && isCage(figureX + sign * 2, figureY)
                && isCage(figureX + sign * 3, figureY) && isRook(figureX + sign * 4, figureY)) {
            return true;
        }
        return false;
    }

    private boolean checkTreePosition(int figureX, int figureY, int sign) {
        if (isCage(figureX + sign, figureY) && isCage(figureX + sign * 2, figureY)
                && isRook(figureX + sign * 3, figureY)) {
            return true;
        }
        return false;
    }

    private Move findChange() {
        if (matrices.size() == 1) return new Move(-1, -1, -1, -1);

        byte[][] current = getMatrix();
        byte[][] last = getLastMatrix(1);

        int figureX = -1, figureY = -1, moveX = -1, moveY = -1;

        for (int i = 0; i < current.length; i++) {
            for (int j = 0; j < current[i].length; j++) {
                if (current[i][j] != last[i][j]) {
                    if (!isCage(j, i)) {
                        moveX = j;
                        moveY = i;
                    } else {
                        figureX = j;
                        figureY = i;
                    }
                }
            }
        }
        return new Move(figureX, figureY, moveX, moveY);
    }

    private Array<Move> getMovesToSaveKing(Array<Move> moves) {
        Array<Move> saving = new Array<>();

        for (Move move : moves) {
            makeMove(move);
            byte[] position = getKingPosition(reverse(colorMove));
            if (!isCheckKing(position[0], position[1])) saving.add(move);
            cancelMove();
        }

        return saving;
    }

    private boolean isUpperColor(int x, int y) {
        return getColor(x, y) == upper;
    }

    private boolean isTakeOnPass(Move move) {
        if (!isPawn(move.getFigureX(), move.getFigureY())) return false;
        return move.getMoveX() != move.getFigureX() && isCage(move.getMoveX(), move.getMoveY());
    }
}
