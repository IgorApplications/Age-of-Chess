package com.iapp.chess.util;

import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

public final class Bool implements Serializable {

    private Deque<Boolean> blockedGame = new LinkedList<>();

    public Bool() {}

    public Bool(boolean value) {
        add(value);
    }

    public boolean get() {
        if (blockedGame.isEmpty()) return false;
        return blockedGame.getLast();
    }

    public void addNegative() {
        if (!blockedGame.isEmpty()) blockedGame.removeLast();
        if (blockedGame.isEmpty()) blockedGame.offer(false);
    }

    public void addPositive() {
        blockedGame.offer(true);
    }

    public void add(boolean value) {
        if (value) addPositive();
        else addNegative();
    }
}
