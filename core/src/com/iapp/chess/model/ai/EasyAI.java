package com.iapp.chess.model.ai;

import com.iapp.chess.model.Color;
import com.iapp.chess.model.Transition;

public class EasyAI extends AI {

    private static final int DEPTH = 2;

    public EasyAI(Color color) {
        super(color);
    }

    @Override
    public Transition getMove() {
        return getMove(DEPTH);
    }
}
