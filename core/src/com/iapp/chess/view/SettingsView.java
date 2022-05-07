package com.iapp.chess.view;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.iapp.chess.controller.Account;
import com.iapp.chess.controller.MenuController;
import com.iapp.chess.model.ai.AI;
import com.iapp.chess.standart_ui.ChoiceButton;
import com.iapp.chess.standart_ui.QuestionDialog;
import com.iapp.chess.standart_ui.ScoreBoard;
import com.iapp.chess.standart_ui.UIDialog;
import com.iapp.chess.util.*;

public class SettingsView implements Screen {

    private MenuController menuController;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage stage;
    private ScoreBoard scoreBoard;

    private UIDialog uiDialog;
    private TextureAtlas.AtlasRegion background;

    private ImageButton backButton;
    private ChoiceButton orientation;
    private ChoiceButton gameMode;
    private ChoiceButton performanceAI;
    private TextButton clearingAccount;

    private ImageButton saveWindowSize;
    private ImageButton outlineFigure;
    private ImageButton outlineFelledFigure;
    private ImageButton hintMoves;
    private ImageButton castleHint;
    private ImageButton checkHint;
    private ImageButton greenCross;

    private ImageButton soundClick;
    private ImageButton soundMove;
    private ImageButton soundWin;
    private ImageButton soundWinMaster;
    private ImageButton soundCastle;
    private ImageButton soundCheck;
    private ImageButton soundLose;

    private ImageButton showFPS;
    private ImageButton showRAM;

    private ScrollPane boards;
    private ImageButton board1;
    private ImageButton board2;

    public SettingsView(MenuController menuController) {
        this.menuController = menuController;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Orientation.cameraWidth, Orientation.cameraHeight);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        background = Settings.gdxGame.findRegion("background");

        initStage();
    }

    private void initStage() {
        StretchViewport stretchViewport = new StretchViewport(Orientation.cameraWidth, Orientation.cameraHeight, camera);

        scoreBoard = new ScoreBoard(menuController.defineUserLevelText(Settings.account.getUserLevel()),
                String.valueOf(Settings.account.getViewCoins()));
        scoreBoard.setViewport(stretchViewport);

        stage = new Stage(stretchViewport);
        Gdx.input.setInputProcessor(stage);

        initButtons();
        initChessBoards();
        loadSettings();
        addButtonListeners();
        addBoardListeners();

        uiDialog = new UIDialog(stage);
        uiDialog.setPosition(Orientation.uiDialogX, Orientation.uiDialogY);
        uiDialog.setSize(Orientation.uiDialogWidth, Orientation.uiDialogHeight);

        uiDialog.addTitle(Text.SELECTION_FIGURES);
        uiDialog.getContent().add(boards).height(155).colspan(15).fillX().expandX().left().padBottom(3).padLeft(10).row();

        uiDialog.addTitle(Text.GAME_MODE);
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            uiDialog.addElement(Text.SAVE_WINDOW_SIZE, saveWindowSize);
        } else if (Gdx.app.getType() == Application.ApplicationType.Android) {
            uiDialog.addElement(Text.ORIENTATION_SCREEN, orientation);
        }
        uiDialog.addElement(Text.AI_COLOR,  gameMode);

        uiDialog.addTitle(Text.GRAPHICS_EFFECTS, 5);
        uiDialog.addElement(Text.OUTLINE_FIGURES, outlineFigure);
        uiDialog.addElement(Text.OUTLINE_FELLED_FIGURE, outlineFelledFigure);
        uiDialog.addElement(Text.MOVE_HINTS, hintMoves);
        uiDialog.addElement(Text.CASTLE_HINTS, castleHint);
        uiDialog.addElement(Text.OUTLINE_CHECK, checkHint);
        uiDialog.addElement(Text.GREEN_CROSS, greenCross);

        uiDialog.addTitle(Text.SOUND_EFFECTS, 5);
        uiDialog.addElement(Text.SOUND_PRESSING, soundClick);
        uiDialog.addElement(Text.SOUND_MOVE, soundMove);
        uiDialog.addElement(Text.SOUND_VICTORY, soundWin);
        uiDialog.addElement(Text.LATEST_SOUND_VICTORY, soundWinMaster);
        uiDialog.addElement(Text.SOUND_CASTLE, soundCastle);
        uiDialog.addElement(Text.SOUND_CHECK, soundCheck);
        uiDialog.addElement(Text.SOUND_LOSE, soundLose);

        Group group = new Group();
        group.addActor(clearingAccount);

        uiDialog.addTitle(Text.DEBUG);
        uiDialog.addElement(Text.AI_PERFORMANCE, performanceAI);
        uiDialog.addElement("Колличество кадров в секунду", showFPS);
        uiDialog.addElement("Объём используемой ОЗУ", showRAM);
        uiDialog.addElement(Text.INFO_DELETE_ACCOUNT, group);

        stage.addActor(uiDialog);
        uiDialog.pushElements();
    }

    private boolean updatedOrientationScreen;

    private void initButtons() {
        backButton = new ImageButton(Settings.gdxGame.getUIKit(), "back");
        backButton.setPosition(10, Orientation.cameraHeight - 60);
        backButton.setSize(50, 50);

        orientation = new ChoiceButton(Text.VERTICAL, Text.HORIZONTAL);
        orientation.setSize(185, 35);
        orientation.setFontScale(0.3f);

        gameMode = new ChoiceButton(Text.BLACK, Text.WHITE, Text.RANDOM);
        gameMode.setSize(185, 35);
        gameMode.setFontScale(0.3f);

        performanceAI = new ChoiceButton("50%", "75%", "100%");
        performanceAI.setSize(185, 35);
        performanceAI.setFontScale(0.3f);

        clearingAccount = new TextButton(Text.DELETE_ACCOUNT, Settings.gdxGame.getUIKit(), "button");
        clearingAccount.getLabel().setFontScale(0.3f);
        clearingAccount.setSize(120, 35);

        saveWindowSize = createCheckButton();
        outlineFigure = createCheckButton();
        outlineFelledFigure  = createCheckButton();
        hintMoves = createCheckButton();
        castleHint = createCheckButton();
        checkHint = createCheckButton();
        greenCross = createCheckButton();
        soundClick = createCheckButton();
        soundMove = createCheckButton();
        soundWin = createCheckButton();
        soundWinMaster = createCheckButton();
        soundCastle = createCheckButton();
        soundCheck = createCheckButton();
        soundLose  = createCheckButton();
        showFPS = createCheckButton(false);
        showRAM = createCheckButton(false);

        stage.addActor(backButton);
    }

    private Image coin;
    private Label price;

    private void initChessBoards() {
        board1 = new ImageButton(Settings.gdxGame.getButtonSkin(), "board1");

        coin = new Image(Settings.gdxGame.findRegion("coin"));
        price = new Label("1000", Settings.gdxGame.getLabelSkin());
        price.setFontScale(0.5f);
        price.setColor(Color.GOLD);

        board2 = new ImageButton(Settings.gdxGame.getButtonSkin(), "board2");
        if (!Settings.account.isAvailableModeFigures()) {
            board2.add(coin).size(18, 18).center();
            board2.add(price).padLeft(5).center();
        }

        ButtonGroup<ImageButton> group = new ButtonGroup<>();
        group.add(board1, board2);

        Table content = new Table();
        content.add(board1).size(225, 150);
        content.add(board2).size(225, 150).padLeft(5);

        boards = new ScrollPane(content);
    }

    private void addBoardListeners() {
        board1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!board1.isChecked()) return;
                Settings.SOUNDS.playClick();
                Settings.account.setChosenFigureSet(FigureSet.STANDARD);
            }
        });

        board2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!board2.isChecked()) return;
                Settings.SOUNDS.playClick();

                if (!Settings.account.isAvailableModeFigures()) {
                    showQuestionDialog(Text.QUESTION_ABOUT_BUYING, dialog -> {
                        Settings.SOUNDS.playClick();
                        if (Settings.account.getCoins() < 1000) return;

                        Settings.account.buyModeFigures();
                        Settings.account.setChosenFigureSet(FigureSet.MODE);
                        scoreBoard.setCoinText(Settings.account.getViewCoins());

                        coin.setVisible(false);
                        price.setVisible(false);
                    }, dialog -> {
                        board1.setChecked(true);
                    });
                } else {
                    Settings.account.setChosenFigureSet(FigureSet.MODE);
                }
            }
        });
    }

    private void addButtonListeners() {
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.gdxGame.goToScreen(stage, Actions.run(() -> {
                    if (updatedOrientationScreen) {
                        Settings.orientation.init(Settings.account.getOrientationType());
                    }

                    MenuView mainView = new MenuView(menuController);
                    Settings.gdxGame.setScreen(mainView);
                    dispose();
                }), 0.15f);
            }
        });

        Settings.launcher.addOnFinish(() -> {
            Gdx.app.postRunnable(() -> {
                if (updatedOrientationScreen) {
                    Settings.orientation.init(Settings.account.getOrientationType());
                }

                MenuView mainView = new MenuView(menuController);
                Settings.gdxGame.setScreen(mainView);
                dispose();
            });
        });

        orientation.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!(actor instanceof TextButton) || !((TextButton) actor).isChecked()) return;

                updatedOrientationScreen = true;
                Settings.SOUNDS.playClick();
                int checkedIndex = orientation.getCheckedIndex();

                if (checkedIndex == 0) {
                    Settings.account.updateOrientationType(Orientation.Type.VERTICAL);
                } else {
                    Settings.account.updateOrientationType(Orientation.Type.HORIZONTAL);
                }
            }
        });

        gameMode.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!(actor instanceof TextButton) || !((TextButton) actor).isChecked()) return;

                Settings.SOUNDS.playClick();
                int checkedIndex = gameMode.getCheckedIndex();

                if (checkedIndex == 0) {
                    Settings.account.setAIColorMode(Account.AIColorMode.BLACK);
                } else if (checkedIndex == 1) {
                    Settings.account.setAIColorMode(Account.AIColorMode.WHITE);
                } else {
                    Settings.account.setAIColorMode(Account.AIColorMode.RANDOM);
                }
            }
        });

        saveWindowSize.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.account.setSaveWindowSize(saveWindowSize.isChecked());
            }
        });

        outlineFigure.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.account.setBlockedOutlineFigure(!outlineFigure.isChecked());
            }
        });

        outlineFelledFigure.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.account.setBlockedOutlineFelledFigure(!outlineFelledFigure.isChecked());
            }
        });

        hintMoves.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.account.setBlockedHintMoves(!hintMoves.isChecked());
            }
        });

        castleHint.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.account.setBlockedHintCastle(!castleHint.isChecked());
            }
        });

        checkHint.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.account.setBlockedHintCheck(!checkHint.isChecked());
            }
        });

        greenCross.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.account.setBlockedGreenCross(!greenCross.isChecked());
            }
        });

        soundClick.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.account.setBlockedSoundClick(!soundClick.isChecked());
            }
        });

        soundMove.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.account.setBlockedSoundMove(!soundMove.isChecked());
            }
        });

        soundWin.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.account.setBlockedSoundWin(!soundWin.isChecked());
            }
        });

        soundWinMaster.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.account.setBlockedSoundWinMaster(!soundWinMaster.isChecked());
            }
        });

        soundCastle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.account.setBlockedSoundCastle(!soundCastle.isChecked());
            }
        });

        soundCheck.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.account.setBlockedSoundCheck(!soundCheck.isChecked());
            }
        });

        soundLose.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.account.setBlockedSoundLose(!soundLose.isChecked());
            }
        });

        performanceAI.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int checkedIndex = performanceAI.getCheckedIndex();
                if (checkedIndex == -1) return;
                Settings.SOUNDS.playClick();
                Settings.account.setAIPerformanceMode(AI.ThreadsMode.values()[checkedIndex]);
            }
        });

        showFPS.addListener(new com.iapp.chess.util.ChangeListener() {
            @Override
            public void onChanged(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.account.setShowFPS(showFPS.isChecked());
            }
        });

        showRAM.addListener(new com.iapp.chess.util.ChangeListener() {
            @Override
            public void onChanged(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.account.setShowRAM(showRAM.isChecked());
            }
        });

        clearingAccount.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                showQuestionDialog(Text.DELETE_ACCOUNT_DIALOG, dialog -> {
                    Settings.SOUNDS.playClick();
                    Settings.account = new Account();
                    Settings.DATA.removeLogs();
                    updateScoreBoard();
                    dialog.hide();
                }, dialog -> Settings.SOUNDS.playClick());
            }
        });
    }

    private void showQuestionDialog(String question, DialogListener onAccept, DialogListener onCancel) {
        QuestionDialog questionDialog = new QuestionDialog(Settings.gdxGame.getUIKit());
        questionDialog.setPosition(Orientation.questionAboutBuyingX, Orientation.questionAboutBuyingY);
        questionDialog.setSize(460, 300);

        questionDialog.setTitle(Text.GAME_MENU)
                .setOnCancel(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (onCancel != null) onCancel.changed(questionDialog);
                        questionDialog.hide();
                    }
                })
                .setQuestion(question)
                .setPositive(Text.YES, new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (onAccept!= null) onAccept.changed(questionDialog);
                    }
                })
                .setNegative(Text.CANCEL, new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (onCancel != null) onCancel.changed(questionDialog);
                        questionDialog.hide();
                    }
                })
                .build();

        stage.addActor(questionDialog);
    }

    private void loadSettings() {
        if (Settings.account.getOrientationType() == Orientation.Type.HORIZONTAL) {
            orientation.setChecked(Text.HORIZONTAL);
        } else {
            orientation.setChecked(Text.VERTICAL);
        }

        if (Settings.account.getAIColorMode() == Account.AIColorMode.BLACK) {
            gameMode.setChecked(Text.BLACK);
        } else if (Settings.account.getAIColorMode() == Account.AIColorMode.WHITE) {
            gameMode.setChecked(Text.WHITE);
        } else {
            gameMode.setChecked(Text.RANDOM);
        }

        saveWindowSize.setChecked(Settings.account.isSaveWindowSize());
        outlineFigure.setChecked(!Settings.account.isBlockedOutlineFigure());
        outlineFelledFigure.setChecked(!Settings.account.isBlockedOutlineFelledFigure());
        hintMoves.setChecked(!Settings.account.isBlockedHintMoves());
        castleHint.setChecked(!Settings.account.isBlockedHintCastle());
        checkHint.setChecked(!Settings.account.isBlockedHintCheck());
        greenCross.setChecked(!Settings.account.isBlockedGreenCross());

        soundClick.setChecked(!Settings.account.isBlockedSoundClick());
        soundMove.setChecked(!Settings.account.isBlockedSoundMove());
        soundWin.setChecked(!Settings.account.isBlockedSoundWin());
        soundWinMaster.setChecked(!Settings.account.isBlockedSoundWinMaster());
        soundCastle.setChecked(!Settings.account.isBlockedSoundCastle());
        soundCheck.setChecked(!Settings.account.isBlockedSoundCheck());
        soundLose.setChecked(!Settings.account.isBlockedSoundLose());
        showFPS.setChecked(Settings.account.isShowFPS());
        showRAM.setChecked(Settings.account.isShowRAM());

        FigureSet figureSet = Settings.account.getChosenFigureSet();
        if (figureSet == FigureSet.STANDARD) {
            board1.setChecked(true);
        } else if (figureSet == FigureSet.MODE) {
            board2.setChecked(true);
        }

        performanceAI.setChecked(Settings.account.getAIPerformanceMode().ordinal());
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
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {
        Settings.DATA.saveAccount(Settings.account);
        Settings.DATA.appendLogs();
    }

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        Settings.launcher.addOnFinish(null);
        batch.dispose();
        stage.dispose();
    }

    private void updateScoreBoard() {
        scoreBoard.setUserLevel(menuController.defineUserLevelText(Settings.account.getUserLevel()));
        scoreBoard.setCoinText(String.valueOf(Settings.account.getViewCoins()));
    }

    private ImageButton createCheckButton() {
        return createCheckButton(true);
    }

    private ImageButton createCheckButton(boolean checked) {
        ImageButton imageButton =  new ImageButton(Settings.gdxGame.getUIKit(), "check");
        imageButton.setChecked(checked);
        return imageButton;
    }
}
