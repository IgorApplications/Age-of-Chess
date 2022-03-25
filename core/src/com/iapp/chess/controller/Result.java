package com.iapp.chess.controller;

import com.iapp.chess.util.Text;

public enum Result {
    VICTORY(Text.VICTORY),
    LOSE(Text.LOSE),
    DRAW(Text.DRAW),
    BLACK_VICTORY(Text.BLACK_VICTORY),
    WHITE_VICTORY(Text.WHITE_VICTORY);

    private String result;

    Result(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return result;
    }
}
