package com.iapp.chess.standart_ui;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class UIUtils {

    public static Texture createTexture(int width, int height, int color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, width, height);
        Texture texture =  new Texture(pixmap);
        pixmap.dispose();

        return texture;
    }

    public static float getSizeByPercentage(float size, float percentage) {
        return size * percentage;
    }
}
