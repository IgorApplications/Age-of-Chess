package com.iapp.chess.model;

import java.util.ArrayList;
import java.util.List;

public class King extends Figure {

    public King(Game game, int x, int y, Color color, int evaluation) {
        super(game, x, y, color, evaluation);
    }

    public King(Game game, int x, int y, Color color) {
        this(game, x, y, color, game.getAIColor() == color ? 900 : -900);
    }
    @Override
    public List<Move> getMoves() {
        List<Move> moves = new ArrayList<>();

        moves.addAll(getMove(getX(), getY() + 1));
        moves.addAll(getMove(getX() + 1, getY() + 1));
        moves.addAll(getMove(getX() + 1, getY()));
        moves.addAll(getMove(getX() + 1, getY() - 1));
        moves.addAll(getMove(getX(), getY() - 1));
        moves.addAll(getMove(getX() - 1, getY() - 1));
        moves.addAll(getMove(getX() - 1, getY()));
        moves.addAll(getMove(getX() - 1, getY() + 1));

        List<Move> safeMoves = new ArrayList<>();
        Color enemyColor = reverseColor(getColor());

        for (Move move : moves) {
            if (getGame().isSafeMoveKing(move.getX(), move.getY(), this, enemyColor))
                safeMoves.add(move);
        }

        return safeMoves;
    }

    List<Move> getNoSafeMoves() {
        List<Move> moves = new ArrayList<>();

        moves.addAll(getMove(getX(), getY() + 1));
        moves.addAll(getMove(getX() + 1, getY() + 1));
        moves.addAll(getMove(getX() + 1, getY()));
        moves.addAll(getMove(getX() + 1, getY() - 1));
        moves.addAll(getMove(getX(), getY() - 1));
        moves.addAll(getMove(getX() - 1, getY() - 1));
        moves.addAll(getMove(getX() - 1, getY()));
        moves.addAll(getMove(getX() - 1, getY() + 1));

        return moves;
    }

    private List<Move> getMove(int x, int y) {
        List<Move> move = new ArrayList<>();

        if (x < 0 || y < 0 || x > 7 || y > 7) return move;

        Figure figure = getGame().getFigure(x, y);

        if (figure instanceof Cage || (getColor() != figure.getColor()))
            move.add(new Move(x, y));
        return move;
    }
}
