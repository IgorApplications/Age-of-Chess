package com.iapp.chess.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

public class Font implements Disposable {

    private BitmapFont bitmapFont;

    private FreeTypeFontGenerator fontGeneratorArial;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameterArial;

    private FreeTypeFontGenerator fontGeneratorAlgerian;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameterAlgerian;

    private String FONT_CHARS = "";
    private static final int DEFAULT_SIZE = 40;

    private static final Font INSTANCE = new Font();

    public static Font getInstance() {
        return INSTANCE;
    }

    private Font() {
        for (int i = 0x20; i < 0x7B; i++) FONT_CHARS += (char)i;
        for (int i = 0x401; i < 0x452; i++) FONT_CHARS += (char)i;

        fontGeneratorArial = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arial.ttf"));
        fontGeneratorAlgerian = new FreeTypeFontGenerator(Gdx.files.internal("fonts/algerian.ttf"));

        initArial();
        initAlgerian();
    }

    private void initAlgerian() {
        fontParameterAlgerian = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameterAlgerian.minFilter = Texture.TextureFilter.Linear;
        fontParameterAlgerian.magFilter = Texture.TextureFilter.Linear;

        fontParameterAlgerian.size = (int)Math.ceil(DEFAULT_SIZE);
        fontParameterAlgerian.color = Color.WHITE;
        fontParameterAlgerian.characters = FONT_CHARS;
    }

    private void initArial() {
        fontParameterArial = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameterArial.minFilter = Texture.TextureFilter.Linear;
        fontParameterArial.magFilter = Texture.TextureFilter.Linear;

        fontParameterArial.size = (int)Math.ceil(DEFAULT_SIZE);
        fontParameterArial.color = Color.WHITE;
        fontParameterArial.characters = FONT_CHARS;
    }

    public BitmapFont createArial() {
        bitmapFont = fontGeneratorArial.generateFont(fontParameterArial);
        bitmapFont.setUseIntegerPositions(false);
        bitmapFont.setColor(Color.WHITE);
        bitmapFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        bitmapFont.getData().markupEnabled = true;

        return bitmapFont;
    }

    public BitmapFont createAlgerian() {
        bitmapFont = fontGeneratorAlgerian.generateFont(fontParameterArial);
        bitmapFont.setUseIntegerPositions(false);
        bitmapFont.setColor(Color.WHITE);
        bitmapFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        bitmapFont.getData().markupEnabled = true;

        return bitmapFont;
    }

    public BitmapFont createArial(int size) {
        fontParameterArial.size = (int)Math.ceil(size);
        BitmapFont arial = createArial();
        fontParameterArial.size = (int)Math.ceil(DEFAULT_SIZE);

        return arial;
    }

    public BitmapFont createAlgerian(int size) {
        fontParameterAlgerian.size = (int)Math.ceil(size);
        BitmapFont algerian = createAlgerian();
        fontParameterAlgerian.size = (int)Math.ceil(DEFAULT_SIZE);

        return algerian;
    }

    @Override
    public void dispose() {
        bitmapFont.dispose();
    }
}
