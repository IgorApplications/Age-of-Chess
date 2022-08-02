package com.iapp.chess.view;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.*;
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

public class ModdingView implements Screen {

    private final MenuController menuController;
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private Stage stage;
    private ScoreBoard scoreBoard;
    private final TextureAtlas.AtlasRegion background;

    private Image isometricCoin, royalCoin;
    private Label isometricPrice, royalPrice;
    private boolean updatedOrientationScreen;

    private ImageButton backButton;
    private ScrollPane boards;
    private ImageButton isometricBoard;
    private ImageButton royalBoard;
    private ImageButton standardBoard;
    private ChoiceButton orientation;
    private ChoiceButton gameMode;
    private ChoiceButton performanceAI;
    private ImageButton reversedTwoPlayers;

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
    private TextButton clearingAccount;

    public ModdingView(MenuController menuController) {
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

        UIDialog uiDialog = new UIDialog(stage);
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
        uiDialog.addElement(Text.REVERSED_TWO_PLAYERS, reversedTwoPlayers);

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
        uiDialog.addElement(Text.FPS_TEXT, showFPS);
        uiDialog.addElement(Text.RAM_TEXT, showRAM);
        uiDialog.addElement(Text.INFO_DELETE_ACCOUNT, group);

        stage.addActor(uiDialog);
        uiDialog.pushElements();
    }

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

        reversedTwoPlayers = createCheckButton();
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

        performanceAI = new ChoiceButton("50%", "75%", "100%");
        performanceAI.setSize(185, 35);
        performanceAI.setFontScale(0.3f);

        showFPS = createCheckButton(false);
        showRAM = createCheckButton(false);

        clearingAccount = new TextButton(Text.DELETE_ACCOUNT, Settings.gdxGame.getUIKit(), "button");
        clearingAccount.getLabel().setFontScale(0.3f);
        clearingAccount.setSize(120, 35);

        stage.addActor(backButton);
    }

    private void initChessBoards() {
        isometricBoard = new ImageButton(Settings.gdxGame.getButtonSkin(), "isometric");
        royalBoard = new ImageButton(Settings.gdxGame.getButtonSkin(), "royal");
        standardBoard = new ImageButton(Settings.gdxGame.getButtonSkin(), "standard");

        Label isometricName = new Label(Text.ISOMETRIC, Settings.gdxGame.getLabelSkin());
        isometricName.setFontScale(0.5f);
        Label royalName = new Label(Text.ROYAL, Settings.gdxGame.getLabelSkin());
        royalName.setFontScale(0.5f);
        Label standardName = new Label(Text.STANDARD, Settings.gdxGame.getLabelSkin());
        standardName.setFontScale(0.5f);

        isometricCoin = new Image(Settings.gdxGame.findRegion("coin"));
        isometricPrice = new Label("1000", Settings.gdxGame.getLabelSkin());
        isometricPrice.setFontScale(0.5f);
        isometricPrice.setColor(Color.GOLD);
        isometricCoin.setSize(18, 18);
        isometricCoin.setPosition(50, 13.5f);

        royalCoin = new Image(Settings.gdxGame.findRegion("coin"));
        royalPrice = new Label("1000", Settings.gdxGame.getLabelSkin());
        royalPrice.setFontScale(0.5f);
        royalPrice.setColor(Color.GOLD);
        royalCoin.setSize(18, 18);
        royalCoin.setPosition(50, 13.5f);

        if (!Settings.account.isAvailableIsometricFigures()) {
            Table table = new Table();
            Group group = new Group();
            group.addActor(isometricPrice);
            group.addActor(isometricCoin);
            table.add(group).center().padRight(70).padTop(90).row();
            table.add(isometricName).center().padTop(25);
            isometricBoard.add(table);
        } else {
            isometricBoard.add(isometricName).padTop(115);
        }

        if (!Settings.account.isAvailableRoyalFigures()) {
            Table table = new Table();
            Group group = new Group();
            group.addActor(royalPrice);
            group.addActor(royalCoin);
            table.add(group).center().padRight(70).padTop(90).row();
            table.add(royalName).center().padTop(25);
            royalBoard.add(table);
        } else {
            royalBoard.add(royalName).padTop(115);
        }

        standardBoard.add(standardName).padTop(115);

        ButtonGroup<ImageButton> group = new ButtonGroup<>();
        group.add(isometricBoard, royalBoard, standardBoard);

        Table content = new Table();

        content.add(isometricBoard).size(331, 150);
        content.add(royalBoard).size(331, 150).padLeft(5);
        content.add(standardBoard).size(331, 150).padLeft(5);

        boards = new ScrollPane(content);
        boards.setZIndex(0);
    }

    private void addBoardListeners() {
        standardBoard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!standardBoard.isChecked()) return;
                Settings.SOUNDS.playClick();
                Settings.account.setChosenFigureSet(FigureSet.FigureSetType.STANDARD);
            }
        });

        isometricBoard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!isometricBoard.isChecked()) return;
                Settings.SOUNDS.playClick();

                if (!Settings.account.isAvailableIsometricFigures()) {
                    showQuestionDialog(Text.QUESTION_ABOUT_BUYING, dialog -> {
                        Settings.SOUNDS.playClick();
                        if (Settings.account.getCoins() < 1000) return;

                        Settings.account.buyIsometricFigures();
                        Settings.account.setChosenFigureSet(FigureSet.FigureSetType.ISOMETRIC);
                        setCheckedCurrent();
                        scoreBoard.setCoinText(Settings.account.getViewCoins());

                        isometricCoin.setVisible(false);
                        isometricPrice.setVisible(false);
                        dialog.hide();
                    }, dialog -> setCheckedCurrent());
                } else {
                    Settings.account.setChosenFigureSet(FigureSet.FigureSetType.ISOMETRIC);
                }
            }
        });

        royalBoard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!royalBoard.isChecked()) return;
                Settings.SOUNDS.playClick();

                if (!Settings.account.isAvailableRoyalFigures()) {
                    showQuestionDialog(Text.QUESTION_ABOUT_BUYING, dialog -> {
                        Settings.SOUNDS.playClick();
                        if (Settings.account.getCoins() < 1000) return;

                        Settings.account.buyRoyalFigures();
                        Settings.account.setChosenFigureSet(FigureSet.FigureSetType.ROYAL);
                        setCheckedCurrent();
                        scoreBoard.setCoinText(Settings.account.getViewCoins());

                        royalCoin.setVisible(false);
                        royalPrice.setVisible(false);
                        dialog.hide();
                    }, dialog -> setCheckedCurrent());
                } else {
                    Settings.account.setChosenFigureSet(FigureSet.FigureSetType.ROYAL);
                }
            }
        });
    }

    private void setCheckedCurrent() {
        if (Settings.account.getChosenFigureSet() == FigureSet.FigureSetType.STANDARD) {
            standardBoard.setChecked(true);
            isometricBoard.setChecked(false);
            royalBoard.setChecked(false);
        } else if (Settings.account.getChosenFigureSet() == FigureSet.FigureSetType.ISOMETRIC) {
            standardBoard.setChecked(false);
            isometricBoard.setChecked(true);
            royalBoard.setChecked(false);
        } else if (Settings.account.getChosenFigureSet() == FigureSet.FigureSetType.ROYAL) {
            standardBoard.setChecked(false);
            isometricBoard.setChecked(false);
            royalBoard.setChecked(true);
        }
    }

    private void addButtonListeners() {
        Settings.launcher.addOnFinish(() -> {
            Gdx.app.postRunnable(() -> {
                if (updatedOrientationScreen) {
                    Settings.orientation.init(Settings.account.getOrientation());
                }

                MenuView mainView = new MenuView(menuController);
                Settings.gdxGame.setScreen(mainView);
                dispose();
            });
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.gdxGame.goToScreen(stage, Actions.run(() -> {
                    if (updatedOrientationScreen) {
                        Settings.orientation.init(Settings.account.getOrientation());
                    }

                    MenuView mainView = new MenuView(menuController);
                    Settings.gdxGame.setScreen(mainView);
                    dispose();
                }), 0.15f);
            }
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

        saveWindowSize.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.account.setSaveWindowSize(saveWindowSize.isChecked());
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

        reversedTwoPlayers.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.account.setReversedTwoPlayers(reversedTwoPlayers.isChecked());
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
        FigureSet.FigureSetType figureSet = Settings.account.getChosenFigureSet();
        if (figureSet == FigureSet.FigureSetType.STANDARD) {
            standardBoard.setChecked(true);
        } else if (figureSet == FigureSet.FigureSetType.ISOMETRIC) {
            isometricBoard.setChecked(true);
        } else if (figureSet == FigureSet.FigureSetType.ROYAL) {
            royalBoard.setChecked(true);
        }

        if (Settings.account.getOrientation() == Orientation.Type.HORIZONTAL) {
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

        reversedTwoPlayers.setChecked(Settings.account.isReversedTwoPlayers());
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
