package com.iapp.chess.model;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Figure {

    private final Rook rook;
    private final Bishop bishop;

    public Queen(Game game, int x, int y, Color color, int evaluation) {
        super(game, x, y, color, evaluation);

        rook = new Rook(game, x, y, color, evaluation);
        bishop = new Bishop(game, x, y, color, evaluation);
    }

    public Queen(Game game, int x, int y, Color color) {
        this(game, x, y, color, game.getAIColor() == color ? 90 : -90);
    }

    @Override
    public List<Move> getMoves() {
        updateXAndY();
        List<Move> moves = new ArrayList<>();

        moves.addAll(rook.getMoves());
        moves.addAll(bishop.getMoves());

        return moves;
    }

    private void updateXAndY() {
        rook.setX(getX());
        rook.setY(getY());
        bishop.setX(getX());
        bishop.setY(getY());
    }
}
