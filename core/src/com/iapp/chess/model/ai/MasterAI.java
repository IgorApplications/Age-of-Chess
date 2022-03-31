package com.iapp.chess.model.ai;

import com.iapp.chess.model.Color;
import com.iapp.chess.model.Transition;

public class MasterAI extends AI {

    private static final int DEPTH = 3;

    public MasterAI(Color color) {
        super(color);
    }

    @Override
    public Thread getMove(AIListener aiListener) {
        return getMove(DEPTH, aiListener);
    }
}
