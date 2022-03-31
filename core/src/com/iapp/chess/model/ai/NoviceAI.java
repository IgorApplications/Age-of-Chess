package com.iapp.chess.model.ai;

import com.iapp.chess.model.*;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class NoviceAI extends AI {

    private final int DEPTH = 1;

    public NoviceAI(Color color) {
        super(color);
    }

    @Override
    public Thread getMove(AIListener aiListener) {
        return getMove(DEPTH, aiListener);
    }
}
