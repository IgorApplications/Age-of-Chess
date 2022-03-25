package com.iapp.chess.model;

import com.iapp.chess.util.Text;

public enum Color {
    BLACK(Text.BLACK),
    WHITE(Text.WHITE);

    private String color;

    Color(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return color;
    }
}
