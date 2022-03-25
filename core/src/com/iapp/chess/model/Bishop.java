package com.iapp.chess.model;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Figure {

    public Bishop(Game game, int x, int y, Color color, int evaluation) {
        super(game, x, y, color, evaluation);
    }

    public Bishop(Game game, int x, int y, Color color) {
        this(game, x, y, color, game.getAIColor() == color ? 30 : -30);
    }

    @Override
    public List<Move> getMoves() {
        List<Move> moves = new ArrayList<>();

        int downRightLength = 8 - getX() < 8 - getY() ? 8 - getX() : 8 - getY();
        moves.addAll(getMovesDownRight(downRightLength));

        int downLeftLength = getX() + 1 < 8 - getY() ? getX() + 1 : 8 - getY();
        moves.addAll(getMovesDownLeft(downLeftLength));

        int upRightLength = 8 - getX() < getY() + 1 ? 8 - getX() : getY() + 1;
        moves.addAll(getMovesUpRight(upRightLength));

        int upLeftLength = getX() + 1 < getY() + 1 ? getX() + 1 : getY() + 1;
        moves.addAll(getMovesUpLeft(upLeftLength));

        return moves;
    }

    private List<Move> getMovesDownRight(int length) {
        List<Move> moves = new ArrayList<>();

        for (int i = 1; i < length; i++) {
            Figure figure = getGame().getFigure(getX() + i, getY() + i);

            if (figure instanceof Cage)
                moves.add(new Move(getX() + i, getY() + i));
            else if (getColor() == Color.BLACK && figure.getColor() == Color.WHITE) {
                moves.add(new Move(getX() + i, getY() + i));
                break;
            }
            else if (getColor() == Color.WHITE && figure.getColor() == Color.BLACK) {
                moves.add(new Move(getX() + i, getY() + i));
                break;
            }
            else break;
        }

        return moves;
    }

    private List<Move> getMovesDownLeft(int length) {
        List<Move> moves = new ArrayList<>();

        for (int i = 1; i < length; i++) {
            Figure figure = getGame().getFigure(getX() - i, getY() + i);

            if (figure instanceof Cage)
                moves.add(new Move(getX() - i, getY() + i));
            else if (getColor() == Color.BLACK && figure.getColor() == Color.WHITE) {
                moves.add(new Move(getX() - i, getY() + i));
                break;
            }
            else if (getColor() == Color.WHITE && figure.getColor() == Color.BLACK) {
                moves.add(new Move(getX() - i, getY() + i));
                break;
            }
            else break;
        }

        return moves;
    }

    private List<Move> getMovesUpRight(int length) {
        List<Move> moves = new ArrayList<>();

        for (int i = 1; i < length; i++) {
            Figure figure = getGame().getFigure(getX() + i, getY() - i);

            if (figure instanceof Cage)
                moves.add(new Move(getX() + i, getY() - i));
            else if (getColor() == Color.BLACK && figure.getColor() == Color.WHITE) {
                moves.add(new Move(getX() + i, getY() - i));
                break;
            }
            else if (getColor() == Color.WHITE && figure.getColor() == Color.BLACK) {
                moves.add(new Move(getX() + i, getY() - i));
                break;
            }
            else break;
        }

        return moves;
    }

    private List<Move> getMovesUpLeft(int length) {
        List<Move> moves = new ArrayList<>();

        for (int i = 1; i < length; i++) {
            Figure figure = getGame().getFigure(getX() - i, getY() - i);

            if (figure instanceof Cage)
                moves.add(new Move(getX() - i, getY() - i));
            else if (getColor() == Color.BLACK && figure.getColor() == Color.WHITE) {
                moves.add(new Move(getX() - i, getY() - i));
                break;
            }
            else if (getColor() == Color.WHITE && figure.getColor() == Color.BLACK) {
                moves.add(new Move(getX() - i, getY() - i));
                break;
            }
            else break;
        }

        return moves;
    }
}
