package com.iapp.chess.model;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Figure {

    public Knight(Game game, int x, int y, Color color, int evaluation) {
        super(game, x, y, color, evaluation);
    }

    public Knight(Game game, int x, int y, Color color) {
        this(game, x, y, color, game.getAIColor() == color ? 30 : -30);
    }

    @Override
    public synchronized List<Move> getMoves() {
        List<Move> moves = new ArrayList<>();

        if (getX() - 1 > -1 && getY() + 2 < 8) {
            moves.addAll(getMove(getGame().getFigure(getX() - 1, getY() + 2), getX() - 1, getY() + 2));
        }

        if (getX() + 1 < 8 && getY() + 2 < 8) {
            moves.addAll(getMove(getGame().getFigure(getX() + 1, getY() + 2), getX() + 1, getY() + 2));
        }

        if (getX() - 2 > -1 && getY() - 1 > -1) {
            moves.addAll(getMove(getGame().getFigure(getX() - 2, getY() - 1), getX() - 2, getY() - 1));
        }

        if (getX() - 2 > -1 && getY() + 1 < 8) {
            moves.addAll(getMove(getGame().getFigure(getX() - 2, getY() + 1), getX() -2, getY() + 1));
        }

        if (getX() + 2 < 8 && getY() + 1 < 8) {
            moves.addAll(getMove(getGame().getFigure(getX() + 2, getY() + 1), getX() + 2, getY() + 1));
        }

        if (getX() + 2 < 8 && getY() - 1 > -1) {
            moves.addAll(getMove(getGame().getFigure(getX() + 2, getY() - 1), getX() + 2, getY() - 1));
        }

        if (getX() - 1 > -1 && getY() - 2 > -1) {
            moves.addAll(getMove(getGame().getFigure(getX() - 1, getY() - 2), getX() -1, getY() - 2));
        }

        if (getX() + 1 < 8 && getY() - 2 > -1) {
            moves.addAll(getMove(getGame().getFigure(getX() + 1, getY() - 2), getX() + 1, getY() - 2));
        }

        return moves;
    }

    private List<Move> getMove(Figure figure, int x, int y) {
        List<Move> move = new ArrayList<>();

        if ((figure instanceof Cage) ||
                (getColor() == Color.BLACK && figure.getColor() == Color.WHITE) ||
                    (getColor() == Color.WHITE && figure.getColor() == Color.BLACK))
            move.add(new Move(x, y));

        return move;
    }
}