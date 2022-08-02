package com.iapp.chess.util;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets extends AssetManager {

    private static final Assets INSTANCE = new Assets();

    public static final AssetDescriptor<TextureAtlas> CHESS_ATLAS =
            new AssetDescriptor<>("atlases/chess.atlas", TextureAtlas.class);

    public static final AssetDescriptor<Sound> CLICK_SOUND =
            new AssetDescriptor<>("sounds/click.mp3", Sound.class);

    public static final AssetDescriptor<Sound> MOVE_SOUND =
            new AssetDescriptor<>("sounds/move.ogg", Sound.class);

    public static final AssetDescriptor<Sound> CASTLE_SOUND =
            new AssetDescriptor<>("sounds/castle.mp3", Sound.class);

    public static final AssetDescriptor<Sound> CHECK_SOUND =
            new AssetDescriptor<>("sounds/check.mp3", Sound.class);

    public static final AssetDescriptor<Sound> WIN_SOUND =
            new AssetDescriptor<>("sounds/win.mp3", Sound.class);

    public static final AssetDescriptor<Sound> WIN_MASTER_SOUND =
            new AssetDescriptor<>("sounds/win_master.mp3", Sound.class);

    public static final AssetDescriptor<Sound> LOSE_SOUND =
            new AssetDescriptor<>("sounds/lose.mp3", Sound.class);

    public static final AssetDescriptor<TextureAtlas> UI_KIT =
            new AssetDescriptor<>("atlases/ui_kit.atlas",  TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> STANDARD_FIGURES =
            new AssetDescriptor<>("atlases/standard_figures.atlas",  TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> ISOMETRIC_FIGURES =
            new AssetDescriptor<>("atlases/isometric_figures.atlas",  TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> ROYALS_FIGURES =
            new AssetDescriptor<>("atlases/royal_figures.atlas",  TextureAtlas.class);

    public static Assets getInstance() {
        return INSTANCE;
    }

    private Assets() {}

    public void loadRes() {
        load(CHESS_ATLAS);
        load(CLICK_SOUND);
        load(MOVE_SOUND);
        load(CASTLE_SOUND);
        load(CHECK_SOUND);
        load(WIN_SOUND);
        load(WIN_MASTER_SOUND);
        load(LOSE_SOUND);
        load(UI_KIT);
        load(STANDARD_FIGURES);
        load(ROYALS_FIGURES);
        load(ISOMETRIC_FIGURES);
    }
}
