package com.iapp.chess.model.ai;

import com.iapp.chess.model.*;

public class MiddleAI extends AI {
    private int DEPTH = 2;

    public MiddleAI(Color color) {
        super(color);
    }

    @Override
    public Transition getMove() {
        return getMove(DEPTH);
    }
}