package com.iapp.chess.model.ai;

import com.iapp.chess.model.Color;
import com.iapp.chess.model.Figure;
import com.iapp.chess.model.Transition;

import java.util.List;

public class HardAI extends AI {

    private int DEPTH = 3;

    public HardAI(Color color) {
        super(color);
    }

    @Override
    public Transition getMove() {
        return getMove(DEPTH);
    }
}
