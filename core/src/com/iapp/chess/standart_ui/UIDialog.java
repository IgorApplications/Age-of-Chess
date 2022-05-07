package com.iapp.chess.standart_ui;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.iapp.chess.util.Settings;

public class UIDialog extends Actor {

    private final UIScrollPane uiScrollPane;

    private final TextureAtlas.AtlasRegion dialogUI;
    private final Texture grayLine;
    private final Texture whiteLine;

    private final boolean grayLineVisible;

    private static final float MARGIN_DIALOG_X = 7;
    private static final float MARGIN_DIALOG_Y = 6;

    public UIDialog(Stage stage) {
        this(stage, true);
    }

    public UIDialog(Stage stage, boolean grayLineVisible) {
        dialogUI = Settings.gdxGame.findUIKitRegion("ui_dialog");
        uiScrollPane = new UIScrollPane(stage);

        grayLine = UIUtils.createTexture(100, 500, Color.rgba8888(0, 0, 0, 0.3f));
        whiteLine = UIUtils.createTexture(100, 500, Color.rgba8888(1, 1, 1, 0.15f));

        this.grayLineVisible = grayLineVisible;
    }

    @Override
    public void setSize(float width, float height) {
        System.out.println("SIZE updated!");
        uiScrollPane.setSize(width - MARGIN_DIALOG_X * 2, height - MARGIN_DIALOG_Y * 2);

        super.setSize(width, height);
    }

    @Override
    public void setPosition(float x, float y) {
        System.out.println("POSITION updated!");
        uiScrollPane.setPosition(x + MARGIN_DIALOG_X, y + MARGIN_DIALOG_Y);

        super.setPosition(x, y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(dialogUI, getX(), getY(), getWidth(), getHeight());
        if (grayLineVisible) {
           batch.draw(grayLine, getX() + MARGIN_DIALOG_X, getY() + MARGIN_DIALOG_Y,
                   getWidth() / 2 + MARGIN_DIALOG_X / 2, getHeight() - MARGIN_DIALOG_Y * 2);
        }
    }

    public void addTitle(String text) {
        addTitle(text, 0);
    }

    public void addTitle(String text, int marginTop) {
        Label title = new Label(text, Settings.gdxGame.getUIKit());
        title.setWrap(true);
        title.setFontScale(0.4f);
        title.setColor(Color.ORANGE);
        title.setX(12);
        title.setY(-7);

        Image line = new Image(grayLine);
        line.setSize(uiScrollPane.getWidth(), 30);

        Group group = new Group();
        group.addActor(line);
        group.addActor(title);

        uiScrollPane.getElements().add(group).colspan(15).fillX().expandX().height(30).padTop(marginTop).row();
    }

    public void addElement(String text, Actor actor) {
        Label textElement = new Label(text, Settings.gdxGame.getLabelSkin(), "default");
        textElement.setFontScale(0.35f);
        textElement.setColor(Color.WHITE);
        textElement.setWrap(true);

        uiScrollPane.getElements().add(textElement).padRight(10).size(getWidth() / 2 - 30, 50).center();
        uiScrollPane.getElements().add(actor).size(35, 35).center().row();
    }

    public Cell<Group> addWhiteLine(Actor actor, float heightLine, float heightActor) {
        Image image = new Image(whiteLine);

        image.setX(image.getX() - MARGIN_DIALOG_X);
        image.setSize(getWidth() - MARGIN_DIALOG_X, heightLine);

        actor.setX(actor.getX() - MARGIN_DIALOG_X);
        actor.setSize(getWidth() - MARGIN_DIALOG_X, heightActor);

        Group group = new Group();
        group.addActor(image);
        group.addActor(actor);

        return uiScrollPane.getElements().add(group);
    }

    public Table getContent() {
        return uiScrollPane.getElements();
    }

    public void pushElements() {
       uiScrollPane.pushElements();
    }
}
