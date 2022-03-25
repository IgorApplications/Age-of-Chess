package com.iapp.chess.model;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Figure {

    public Rook(Game game, int x, int y, Color color, int evaluation) {
        super(game, x, y, color, evaluation);
    }

    public Rook(Game game, int x, int y, Color color) {
        this(game, x, y, color, game.getAIColor() == color ? 50 : -50);
    }

    @Override
    public List<Move> getMoves() {
        List<Move> moves = new ArrayList<>();

        moves.addAll(getRightMoves());
        moves.addAll(getLeftMoves());
        moves.addAll(getUpMoves());
        moves.addAll(getDownMoves());

        return moves;
    }

    private List<Move> getRightMoves() {
        List<Move> rightMoves = new ArrayList<>();

        for (int i = getX() + 1; i < 8; i++) {
            Figure figure = getGame().getFigure(i, getY());
            if (!(figure instanceof Cage)) {
                if (getColor() == Color.BLACK && figure.getColor() == Color.WHITE)
                    rightMoves.add(new Move(i, getY()));
                if (getColor() == Color.WHITE && figure.getColor() == Color.BLACK)
                    rightMoves.add(new Move(i, getY()));
                break;
            }
            rightMoves.add(new Move(i, getY()));
        }

        return rightMoves;
    }

    private List<Move> getLeftMoves() {
        List<Move> leftMoves = new ArrayList<>();

        for (int i = getX() - 1; i > -1; i--) {
            Figure figure = getGame().getFigure(i, getY());
            if (!(figure instanceof Cage)) {
                if (getColor() == Color.BLACK && figure.getColor() == Color.WHITE)
                    leftMoves.add(new Move(i, getY()));
                if (getColor() == Color.WHITE && figure.getColor() == Color.BLACK)
                    leftMoves.add(new Move(i, getY()));
                break;
            }
            leftMoves.add(new Move(i, getY()));
        }

        return leftMoves;
    }

    private List<Move> getUpMoves() {
        List<Move> upMoves = new ArrayList<>();

        for (int i = getY() + 1; i < 8; i++) {
            Figure figure = getGame().getFigure(getX(), i);
            if (!(figure instanceof Cage)) {
                if (getColor() == Color.BLACK && figure.getColor() == Color.WHITE)
                    upMoves.add(new Move(getX(), i));
                if (getColor() == Color.WHITE && figure.getColor() == Color.BLACK)
                    upMoves.add(new Move(getX(), i));
                break;
            }
            upMoves.add(new Move(getX(), i));
        }

        return upMoves;
    }

    private List<Move> getDownMoves() {
        List<Move> downMoves = new ArrayList<>();

        for (int i = getY() - 1; i > -1; i--) {
            Figure figure = getGame().getFigure(getX(), i);
            if (!(figure instanceof Cage)) {
                if (getColor() == Color.BLACK && figure.getColor() == Color.WHITE)
                    downMoves.add(new Move(getX(), i));
                if (getColor() == Color.WHITE && figure.getColor() == Color.BLACK)
                    downMoves.add(new Move(getX(), i));
                break;
            }
            downMoves.add(new Move(getX(), i));
        }

        return downMoves;
    }
}
