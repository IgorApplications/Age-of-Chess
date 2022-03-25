package com.iapp.chess.model;

import java.util.List;

public class Cage extends Figure {

    private Cage() {
        super(null, -1, -1, null, 0);
    }

    private static final Cage INSTANCE = new Cage();

    static Cage getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Move> getMoves() { throw new AssertionError("IgorApplication Error: This is a chess cage!"); }

    @Override
    public Color getColor() { return Color.BLACK; }
}
