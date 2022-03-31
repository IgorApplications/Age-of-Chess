package com.iapp.chess.model.ai;

import com.iapp.chess.model.*;

public class MiddleAI extends AI {
    private int DEPTH = 2;

    public MiddleAI(Color color) {
        super(color);
    }

    @Override
    public Thread getMove(AIListener aiListener) {
        return getMove(DEPTH, aiListener);
    }
}