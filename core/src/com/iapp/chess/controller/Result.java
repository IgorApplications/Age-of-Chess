package com.iapp.chess.controller;

import com.iapp.chess.util.Text;

public enum Result {
    VICTORY(Text.VICTORY),
    LOSE(Text.LOSE),
    DRAW(Text.DRAW),
    BLACK_VICTORY(Text.BLACK_VICTORY),
    WHITE_VICTORY(Text.WHITE_VICTORY);

    private final String result;

    Result(String result) {
        this.result = result;
    }

    public String getText() {
        return result;
    }
}
