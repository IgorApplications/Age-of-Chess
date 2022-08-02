package com.iapp.chess.view;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.iapp.chess.util.Orientation;

public class BoardMatrix {

    // 56.62_650_602f
    // 8.4_939_759f
    // 60.240_963_86f
    // 9.036_144_578f;
    public static float WIDTH = Orientation.figureSize;
    public static float HEIGHT = Orientation.figureSize;
    private static float boardX = Orientation.boardX + Orientation.boardEdge;
    private static float boardY = Orientation.boardY + Orientation.boardEdge;

    public static void reInit() {
        boardX = Orientation.boardX + Orientation.boardEdge;
        boardY = Orientation.boardY + Orientation.boardEdge;
        WIDTH = Orientation.figureSize;
        HEIGHT = Orientation.figureSize;
    }

    public static float getPositionX(int x) {
        if (x > 7 || x < 0) {
            throw new IllegalArgumentException();
        }

        return boardX + WIDTH * x;
    }

    public static float getPositionY(int y) {
        if (y > 7 || y < 0) {
            throw new IllegalArgumentException();
        }

        y = 7 - y;
        if (Gdx.app.getType() == Application.ApplicationType.Desktop && y == 4)
            return boardY + HEIGHT * y - 0.1f;
        return boardY + HEIGHT * y;
    }

    public static float getSizeWidth(int x) {
        return WIDTH;
    }

    public static float getSizeHeight(int y) {
        return HEIGHT;
    }
}
