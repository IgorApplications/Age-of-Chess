package com.iapp.chess.standart_ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.iapp.chess.util.Orientation;
import com.iapp.chess.util.Settings;

public class ScoreBoard extends Stage {

    private Actor grayLineActor;
    private Actor blackLineActor;

    private Texture grayLine;
    private Texture blackLine;

    private Label coins;
    private Image coinImg;
    private Label userLevel;

    private String userLevelText;
    private String coinsText;

    public ScoreBoard(String userLevelText, String coinsText) {
        this.userLevelText = userLevelText;
        this.coinsText = coinsText;

        grayLine = UIUtils.createTexture(100, 100, Color.rgba8888(0.2f, 0.2f, 0.2f, 1));
        blackLine = UIUtils.createTexture(100, 100, Color.rgba8888(0, 0, 0, 1));

        grayLineActor = new Actor() {
            @Override
            public void draw (Batch batch, float parentAlpha) {
                batch.draw(grayLine, getX(), getY(), getWidth(), getHeight());
            }
        };
        grayLineActor.setPosition(Orientation.cameraWidth - 95, Orientation.cameraHeight - 50);
        grayLineActor.setSize(95, 50);

        blackLineActor = new Actor() {
            @Override
            public void draw (Batch batch, float parentAlpha) {
                batch.draw(blackLine, getX(), getY(), getWidth(), getHeight());
            }
        };
        blackLineActor.setPosition(Orientation.cameraWidth - 95, Orientation.cameraHeight - 25);
        blackLineActor.setSize(95, 2.5f);

        addActor(grayLineActor);
        addActor(blackLineActor);
        initLabels();
    }

    public void setCoinText(String coinsText) {
        this.coinsText = coinsText;
        coins.setText(coinsText);
    }

    public void setUserLevel(String text) {
        userLevel.setText(text);
    }

    private void initLabels() {
        coins = new Label(coinsText, Settings.gdxGame.getLabelSkin());
        coins.setFontScale(0.5f);
        coins.setColor(Color.ORANGE);
        coins.setWidth(63);
        coins.setAlignment(Align.left);
        coins.setPosition(Orientation.cameraWidth - 67, Orientation.cameraHeight - 34);

        coinImg = new Image(Settings.gdxGame.findRegion("coin"));
        coinImg.setPosition(Orientation.cameraWidth - 91, Orientation.cameraHeight - 20);
        coinImg.setSize(17, 17);

        userLevel = new Label(userLevelText, Settings.gdxGame.getLabelSkin());
        if (userLevelText.length() <= 7) {
            userLevel.setFontScale(0.5f);
        } else {
            userLevel.setFontScale(0.35f);
        }
        userLevel.setPosition(Orientation.cameraWidth - 90, Orientation.cameraHeight - 60);
        userLevel.setWidth(90);
        userLevel.setColor(Color.ORANGE);

        addActor(coins);
        addActor(coinImg);
        addActor(userLevel);
    }
}
