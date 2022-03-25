package com.iapp.chess.standart_ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.iapp.chess.util.Settings;

public class UIScrollPane extends Actor {

    private Stage stage;
    private ScrollPane scrollPane;

    private Table content;

    public UIScrollPane(Stage stage) {
        this.stage = stage;

        content = new Table();
        content.align(Align.topLeft);

        scrollPane = new ScrollPane(content, Settings.gdxGame.getUIKit());
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
    }

    public Table getElements() {
        return content;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
    }

    @Override
    public void setSize(float width, float height) {
        scrollPane.setSize(width, height);
        content.setSize(width, height);

        super.setSize(width, height);
    }

    @Override
    public void setPosition(float x, float y) {
        scrollPane.setPosition(x, y);
        content.setPosition(x, y);

        super.setPosition(x, y);
    }

    public void pushElements() {
        stage.addActor(scrollPane);
    }
}
