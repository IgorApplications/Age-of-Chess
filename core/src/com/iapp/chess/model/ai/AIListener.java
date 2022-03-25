package com.iapp.chess.model.ai;

import com.iapp.chess.model.Game;
import com.iapp.chess.model.Transition;

public interface AIListener {

    void finishMove(Transition transition);
}
