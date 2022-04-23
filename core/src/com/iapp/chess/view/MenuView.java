package com.iapp.chess.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.iapp.chess.controller.Level;
import com.iapp.chess.controller.MenuController;
import com.iapp.chess.standart_ui.ScoreBoard;
import com.iapp.chess.standart_ui.UIDialog;
import com.iapp.chess.util.Orientation;
import com.iapp.chess.util.Settings;
import com.iapp.chess.util.Text;

import java.util.ArrayList;
import java.util.List;

public class MenuView implements Screen {

    private MenuController menuController;
    private Stage stage;
    private ScoreBoard scoreBoard;
    private UIDialog uiDialog;
    private ScrollPane scrollPane;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private TextureAtlas.AtlasRegion background;

    public MenuView(MenuController menuController) {
        this.menuController = menuController;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Orientation.cameraWidth, Orientation.cameraHeight);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        background = Settings.gdxGame.findRegion("background");

        initStage();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        camera.update();

        batch.begin();
        batch.draw(background, 0, 0, Orientation.cameraWidth, Orientation.cameraHeight);
        batch.end();

        stage.act(delta);
        stage.draw();

        scoreBoard.act(delta);
        scoreBoard.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().setScreenSize(width, height);
    }

    @Override
    public void pause() {
        Settings.DATA.saveAccount(Settings.account);
    }

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }

    private void initStage() {
        StretchViewport stretchViewport = new StretchViewport(Orientation.cameraWidth, Orientation.cameraHeight, camera);

        scoreBoard = new ScoreBoard(menuController.defineUserLevelText(Settings.account.getUserLevel()),
                String.valueOf(Settings.account.getViewCoins()));
        scoreBoard.setViewport(stretchViewport);

        stage = new Stage(stretchViewport);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new KeyInputProcessor());
        Gdx.input.setInputProcessor(inputMultiplexer);

        initLabels();
        initButtons();
    }

    private ButtonGroup<TextButton> buttonGroup = new ButtonGroup<>();
    private List<TextButton> levelButtons = new ArrayList<>();
    private TextButton buttonPlay;
    private Label infoLevel;
    private static final int COUNT_LEVEL = 9;

    private void initLabels() {
        infoLevel = new Label(Settings.account.getChoiceLevel().toString(), Settings.gdxGame.getLabelSkin(), "default");
        infoLevel.setFontScale(0.5f);
        infoLevel.setAlignment(Align.center);
        infoLevel.setColor(Color.WHITE);
    }

    private void initButtons() {
        uiDialog = new UIDialog(stage, false);
        uiDialog.setSize(Orientation.mainUIDialogWidth, Orientation.mainUIDialogHeight);
        uiDialog.setPosition(Orientation.mainUIDialogX, Orientation.mainUIDialogY);

        Table table = new Table();

        buttonPlay = new TextButton(Text.PLAY, Settings.gdxGame.getUIKit(), "play");
        buttonPlay.getLabel().setFontScale(0.5f);
        buttonPlay.getLabelCell().padLeft(10);
        buttonPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (buttonGroup.getChecked() != null) {
                    menuController.goToGame(MenuView.this, stage);
                }
            }
        });

        TextButton buttonSettings = new TextButton(Text.SETTINGS, Settings.gdxGame.getUIKit(), "settings");
        buttonSettings.getLabel().setFontScale(0.5f);
        buttonSettings.getLabelCell().padLeft(20);
        buttonSettings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.gdxGame.goToScreen(stage, Actions.run(() -> {
                    SettingsView settingsView = new SettingsView(menuController);
                    Settings.gdxGame.setScreen(settingsView);
                    dispose();
                }), 0.15f);
            }
        });

        for (Level level : Settings.account.availableLevels()) {
            TextButton levelButton = new TextButton(menuController.defineLevelText(level), Settings.gdxGame.getButtonSkin(), "toggle");
            levelButton.getLabel().setFontScale(0.5f);

            levelButton.row().colspan(3).padRight(16).padBottom(3);
            levelButton.add(menuController.getRecordResImage(level, 100)).size(12, 12).align(Align.bottomRight);
            levelButton.add(menuController.getRecordResImage(level, 50)).size(12, 12).align(Align.bottomRight);
            levelButton.add(menuController.getRecordResImage(level, 25)).size(12, 12).align(Align.bottomRight);
            levelButton.row();

            levelButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Settings.account.setChoiceLevel(level);
                    if (infoLevel != null) {
                        infoLevel.setText(level.toString());
                    }
                }
            });
            levelButtons.add(levelButton);

            if (level == Settings.account.getChoiceLevel()) {
                levelButton.setChecked(true);
            }
        }

        List<TextButton> choiceButtons = new ArrayList<>(levelButtons);
        final int countCloseLevels = COUNT_LEVEL - levelButtons.size();

        for (int i = 0; i < countCloseLevels; i++) {
            TextButton choiceLevelClose = new TextButton(Text.CLOSE, Settings.gdxGame.getButtonSkin(), "closed");
            choiceLevelClose.getLabel().setFontScale(0.5f, 0.5f);
            choiceLevelClose.getLabelCell().pad(0, 0,0 ,25);
            choiceButtons.add(choiceLevelClose);
        }

        for (TextButton levelButton : levelButtons)
            buttonGroup.add(levelButton);
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setUncheckLast(true);

        for (TextButton choiceButton : choiceButtons) {
            choiceButton.getLabel().setFontScale(0.45f);
            table.add(choiceButton).width(135).height(75).padLeft(10).padBottom(15);
            table.padRight(10);
        }

        scrollPane = new ScrollPane(table, Settings.gdxGame.getUIKit());
        scrollPane.setScrollingDisabled(false, true);
        scrollPane.setFadeScrollBars(false);

        uiDialog.addTitle(Text.LEVEL_SELECTION);
        uiDialog.addWhiteLine(infoLevel, 50, 50).size(uiDialog.getWidth(), 50).colspan(15).row();
        uiDialog.addWhiteLine(scrollPane,100, 100).size(uiDialog.getWidth(), 100).colspan(15).row();

        uiDialog.getContent().add(buttonPlay).align(Align.bottom).expandY().size(200, 40).colspan(7).padBottom(7);
        uiDialog.getContent().add(buttonSettings).align(Align.bottom).expandY().size(200, 40).colspan(6).padBottom(7).row();

        stage.addActor(uiDialog);
        uiDialog.pushElements();
    }

    public class KeyInputProcessor implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.RIGHT) {
                nextButton();
            } else if (keycode == Input.Keys.LEFT) {
                lastButton();
            } else if (keycode == Input.Keys.ENTER) {
                buttonPlay.getClickListener().clicked(null, 0, 0);
            }
            return true;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }

        private void nextButton() {
            TextButton choiceButton = buttonGroup.getChecked();
            if (choiceButton != null) {
                int index = levelButtons.indexOf(choiceButton);
                if (index != levelButtons.size() - 1) {
                    levelButtons.get(++index).setChecked(true);
                }
            }
        }

        private void lastButton() {
            TextButton choiceButton = buttonGroup.getChecked();
            if (choiceButton != null) {
                int index = levelButtons.indexOf(choiceButton);
                if (index != 0) {
                    levelButtons.get(--index).setChecked(true);
                }
            }
        }
    }
}
