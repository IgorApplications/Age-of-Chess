package com.iapp.chess.util;

import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class ChangeListener extends com.badlogic.gdx.scenes.scene2d.utils.ChangeListener {

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        try {
            onChanged(event, actor);
        } catch (Throwable t) {
            Settings.LOGGER.log(t);
        }
    }

    public abstract void onChanged(ChangeEvent event, Actor actor);
}
