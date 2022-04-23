package com.iapp.chess.model;

import com.iapp.chess.util.Text;

public enum Color {
    BLACK(Text.BLACK), WHITE(Text.WHITE);

    private String title;

    Color(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
