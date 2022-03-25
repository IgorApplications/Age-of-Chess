package com.iapp.chess.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.iapp.chess.controller.Level;
import com.iapp.chess.controller.Result;
import com.iapp.chess.model.*;
import com.iapp.chess.standart_ui.QuestionDialog;
import com.iapp.chess.standart_ui.UIUtils;
import com.iapp.chess.util.Orientation;
import com.iapp.chess.util.Settings;
import com.iapp.chess.util.Text;

public class DialogView {

    private GameView gameView;
    private Stage stage;
    private Dialog descriptionDialog;

    private Texture yellowLine;
    private Texture redLine;

    public DialogView(GameView gameView, Stage stage) {
        this.gameView = gameView;
        this.stage = stage;

        yellowLine = UIUtils.createTexture(100, 10, Color.rgba8888(1, 1, 0, 1));
        redLine = UIUtils.createTexture(100, 10, Color.rgba8888(1, 0, 0, 1));
    }

    /**
     * FinishDialog
     * */
    public void showFinishDialog(Result result) {
        Runnable task = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (Settings.MUTEX) {
                if (result == null) throw new RuntimeException("Iapp: result can't be null");
                else if (result == Result.VICTORY) nextUserLevel();
                Settings.controller.blockGame();

                Dialog dialog = new Dialog("", Settings.gdxGame.getDialogSkin(), "res_dialog");
                dialog.setPosition(Orientation.finishDialogX, Orientation.finishDialogY);
                dialog.setSize(Orientation.finishDialogWidth, Orientation.finishDialogHeight);

                if (result == Result.VICTORY || result == Result.BLACK_VICTORY || result == Result.WHITE_VICTORY) {
                    Settings.account.addCoins(Settings.account.getChoiceLevel());
                    if (Settings.account.getChoiceLevel() == Level.MASTER) {
                        Settings.SOUNDS.playWinMaster();
                    } else {
                        Settings.SOUNDS.playWin();
                    }
                } else {
                    Settings.SOUNDS.playLose();
                }

                int turns = (int) Settings.controller.getTurn();
                Level level = Settings.controller.getLevel();

                initFinishTitle(dialog, result);
                if (result == Result.VICTORY || result == Result.BLACK_VICTORY || result == Result.WHITE_VICTORY) {
                    initVictoryText(dialog, turns, level, getStars());
                } else {
                    initLoseText(dialog, result, level, turns);
                }

                stage.addActor(dialog);

                if (result != Result.LOSE && result != Result.DRAW) {
                    int bestRes = Settings.account.getRecordTurnsOnLevel(level);
                    int currentTurn = (int) Settings.controller.getTurn();
                    if (bestRes > currentTurn) Settings.account.updateTurnsByLevel(level, currentTurn);
                }
            }
        };
        Settings.gdxGame.execute(task);
    }

    private void initFinishTitle(Dialog dialog, Result result) {
        Label resultText = new Label(result.toString(), Settings.gdxGame.getLabelSkin());
        resultText.setFontScale(0.5f);

        ImageButton buttonHide = new ImageButton(Settings.gdxGame.getUIKit(), "gray_cancel");
        buttonHide.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                dialog.hide();
                Settings.controller.unblockGame();
            }
        });

        Image line;
        if (result == Result.VICTORY || result ==   Result.BLACK_VICTORY || result ==  Result.WHITE_VICTORY) {
            line = new Image(yellowLine);
            resultText.setColor(Color.GOLD);
        } else {
            line = new Image(redLine);
            resultText.setColor(Color.RED);
        }

        dialog.getTitleTable().defaults().space(0);
        dialog.getTitleTable().setWidth(dialog.getWidth());
        dialog.getTitleTable().add(resultText).padTop(70).fillX().expandX().center();
        dialog.getTitleTable().add(buttonHide).size(32, 33).padTop(70).padRight(Orientation.finishDialogMarginCancel).fillX().row();
        dialog.getTitleTable().add(line).size(Orientation.finishDialogWidthLine, 2.5f).colspan(2).padRight(7).padTop(10);
    }

    private void initVictoryText(Dialog dialog, int turns, Level level, Group stars) {
        Label text1 = new Label(Text.FINISHED_GAME_IN + "[GOLD]" + turns + "[]" + Text.MOVES + ":", Settings.gdxGame.getUIKit());
        text1.setFontScale(0.4f);
        text1.setColor(Color.WHITE);

        Label text2 = new Label(Text.REWARD, Settings.gdxGame.getUIKit());
        text2.setFontScale(0.4f);
        text2.setColor(Color.WHITE);

        Image coin = new Image(Settings.gdxGame.findRegion("coin"));
        coin.setSize(18, 18);

        Label text3 = new Label(String.valueOf(Settings.controller.getLevel().getCoins()), Settings.gdxGame.getUIKit());
        text3.setFontScale(0.4f);
        text3.setSize(100, 20);
        text3.setX(25);
        text3.setY(-1.5f);
        text3.setColor(Color.GOLD);

        Group award = new Group();
        award.addActor(coin);
        award.addActor(text3);

        Label text4 = new Label("", Settings.gdxGame.getUIKit());
        if (Settings.account.getChoiceLevel() != Level.TWO_PLAYERS) {
            text4.setText(Text.DEFEATED_AI);
        } else {
            text4.setText(Text.DEFEATED_OPPONENT);
        }
        text4.setFontScale(0.4f);
        text4.setWrap(true);

        Label levelAI = new Label(level.toString(), Settings.gdxGame.getUIKit());
        levelAI.setColor(Color.GOLD);
        levelAI.setFontScale(0.4f);

        int recordTurns = Settings.account.getRecordTurnsOnLevel(level);
        Label text5 = new Label("", Settings.gdxGame.getUIKit());
        text5.setFontScale(0.4f);
        text5.setWrap(true);
        if (recordTurns > turns) {
            text5.setText(Text.BEST_SCORE);
        } else {
            text5.setText(Text.BEST_SCORE_AT_LEVEL + "[GOLD]" + recordTurns + "[]" + Text.MOVES + ".");
        }

        dialog.getContentTable().left();
        dialog.getContentTable().add(text1).fillX().padLeft(20).row();
        dialog.getContentTable().add(stars).left().size(300, 25).fillX().padLeft(20).row();

        dialog.getContentTable().add(text2).padLeft(20).fillX().row();
        dialog.getContentTable().add(award).left().size(300, 15).padLeft(20).fillX().row();

        dialog.getContentTable().add(text4).fillX().padLeft(20).row();
        dialog.getContentTable().add(levelAI).left().size(300, 25).padLeft(20).fillX().row();

        dialog.getContentTable().add(text5).fillX().padLeft(20).row();
    }

    private void initLoseText(Dialog dialog, Result result, Level level, int turns) {
        Label text1 = new Label("", Settings.gdxGame.getUIKit());
        if (result == Result.DRAW) {
            text1.setText(Text.DRAW_TEXT);
        } else {
            text1.setText(Text.LOSS_TEXT);
        }
        text1.setFontScale(0.4f);
        text1.setWrap(true);

        Label text2 = new Label(Text.FINISHED_GAME_IN  + "[RED]" + turns + "[]" + Text.MOVES + ":", Settings.gdxGame.getUIKit());
        text2.setFontScale(0.4f);
        text2.setColor(Color.WHITE);

        Label text3 = new Label("", Settings.gdxGame.getUIKit());
        text3.setFontScale(0.4f);
        text3.setColor(Color.WHITE);
        text3.setWrap(true);

        int recordTurns = Settings.account.getRecordTurnsOnLevel(level);
        if (recordTurns == 200) {
            text3.setText(Text.NEVER_VICTORY);
        } else {
            text3.setText(Text.BEST_SCORE_AT_LEVEL + "[RED]" + recordTurns + "[]" + Text.MOVES + ".");
        }

        dialog.getContentTable().left();
        dialog.getContentTable().add(text1).fillX().width(290).padLeft(20).padBottom(10).row();
        dialog.getContentTable().add(text2).fillX().width(290).padLeft(20).row();
        dialog.getContentTable().add(text3).fillX().width(290).padLeft(20).padBottom(30).row();
    }

    private Group getStars() {
        Group stars = new Group();
        Image star1 = Settings.controller.getResImage(100),
                star2 = Settings.controller.getResImage(50),
                star3  = Settings.controller.getResImage(25);
        star1.setSize(25, 25);
        star2.setSize(25, 25);
        star2.setX(33.3f);
        star3.setSize(25, 25);
        star3.setX(66.5f);

        stars.addActor(star1);
        stars.addActor(star2);
        stars.addActor(star3);

        return stars;
    }
    /**
     * ChoiceFigureDialog
     * */
    public void showChoiceFigureDialog(FigureView pawn, Move movePawn) {
        Settings.controller.blockGame();

        Dialog dialog = new Dialog("", Settings.gdxGame.getDialogSkin(), "rect");
        dialog.setColor(com.badlogic.gdx.graphics.Color.BLACK);
        dialog.setPosition(Orientation.choiceDialogX, Orientation.choiceDialogY);
        dialog.setSize(410, 320);

        Image imageQueen = new Image(gameView.findFigure("white_queen"));
        Image imageRook = new Image(gameView.findFigure("white_rook"));
        Image imageBishop = new Image(gameView.findFigure("white_bishop"));
        Image imageKnight = new Image(gameView.findFigure("white_knight"));

        Label title = new Label(Text.TRANSFORM_PAWN, Settings.gdxGame.getLabelSkin());
        title.setFontScale(0.5f);

        ImageButton buttonQueen = new ImageButton(Settings.gdxGame.getDialogSkin(), "whitechoice");
        Label text1 = new Label(Text.QUEEN, Settings.gdxGame.getLabelSkin());
        text1.setFontScale(0.5f);
        text1.setColor(com.badlogic.gdx.graphics.Color.BLACK);
        buttonQueen.add(imageQueen).colspan(2).size(70, 70).center();
        buttonQueen.row();
        buttonQueen.add(text1).colspan(4).center().padTop(10);
        buttonQueen.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                gameView.updateFigure(pawn, movePawn, Queen.class);
                dialog.hide();
                dialog.setVisible(false);
            }
        });

        ImageButton buttonRook = new ImageButton(Settings.gdxGame.getDialogSkin(), "greenchoice");
        Label text2 = new Label(Text.ROOK, Settings.gdxGame.getLabelSkin());
        text2.setFontScale(0.5f);
        text2.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        buttonRook.add(imageRook).colspan(2).size(70, 70).center();
        buttonRook.row();
        buttonRook.add(text2).colspan(4).center().padTop(10);
        buttonRook.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                gameView.updateFigure(pawn, movePawn, Rook.class);
                dialog.hide();
                dialog.setVisible(false);
            }
        });

        ImageButton buttonBishop = new ImageButton(Settings.gdxGame.getDialogSkin(), "greenchoice");
        Label text3 = new Label(Text.BISHOP, Settings.gdxGame.getLabelSkin());
        text3.setFontScale(0.5f);
        text3.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        buttonBishop.add(imageBishop).colspan(2).size(70, 70).center();
        buttonBishop.row();
        buttonBishop.add(text3).colspan(4).center().padTop(10);
        buttonBishop.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                gameView.updateFigure(pawn, movePawn, Bishop.class);
                dialog.hide();
                dialog.setVisible(false);
            }
        });

        ImageButton buttonKnight = new ImageButton(Settings.gdxGame.getDialogSkin(), "whitechoice");
        Label text4 = new Label(Text.KNIGHT, Settings.gdxGame.getLabelSkin());
        text4.setFontScale(0.5f);
        text4.setColor(com.badlogic.gdx.graphics.Color.BLACK);
        buttonKnight.add(imageKnight).colspan(2).size(70, 70).center();
        buttonKnight.row();
        buttonKnight.add(text4).colspan(4).center().padTop(10);
        buttonKnight.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                gameView.updateFigure(pawn, movePawn, Knight.class);
                dialog.hide();
                dialog.setVisible(false);
            }
        });

        dialog.getContentTable().add(title).center().padTop(12);
        dialog.getButtonTable().defaults().space(0);
        dialog.getButtonTable().add(buttonQueen).size(205, 150);
        dialog.getButtonTable().add(buttonRook).size(205, 150);
        dialog.getButtonTable().row();
        dialog.getButtonTable().add(buttonBishop).size(205, 150);
        dialog.getButtonTable().add(buttonKnight).size(205, 150);
        dialog.row();

        addDialogHandler(dialog, () -> {
            dialog.hide();
            Settings.controller.unblockGame();
        });

        stage.addActor(dialog);
    }

    /**
     * ReplayDialog
     * */
    public void showReplayGameDialog() {
        Settings.controller.blockGame();

        QuestionDialog questionDialog = new QuestionDialog(Settings.gdxGame.getUIKit());
        questionDialog.setPosition(Orientation.replayDialogX, Orientation.replayDialogY);
        questionDialog.setSize(460, 300);

        questionDialog.setTitle(Text.GAME_MENU)
                .setOnCancel(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Settings.SOUNDS.playClick();
                        questionDialog.hide();
                        Settings.controller.unblockGame();
                    }
                })
                .setQuestion(Text.REPLAY_TEXT)
                .setPositive(Text.YES, new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Settings.SOUNDS.playClick();
                        Settings.controller.removeSavedGame();
                        Settings.gdxGame.goToGame(gameView);
                        Settings.controller.unblockGame();
                    }
                })
                .setNegative(Text.CANCEL, new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Settings.SOUNDS.playClick();
                        questionDialog.hide();
                        Settings.controller.unblockGame();
                    }
                })
                .build();

        stage.addActor(questionDialog);
    }

    /**
     * MenuDialog
     * */
    public Dialog showMenuDialog() {
        Settings.controller.blockGame();

        Dialog dialog = new Dialog("", Settings.gdxGame.getUIKit());
        dialog.setPosition(Orientation.menuDialogX, Orientation.menuDialogY);
        dialog.setSize(460, 300);

        Label title = new Label(Text.GAME_MENU, Settings.gdxGame.getUIKit());
        title.setFontScale(0.3f);
        title.setColor(Color.BLACK);

        ImageButton cancelButton = new ImageButton(Settings.gdxGame.getUIKit(), "cancel");
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                dialog.hide();
                Settings.controller.unblockGame();
            }
        });

        Label info = new Label(Text.MENU_TEXT, Settings.gdxGame.getLabelSkin());
        info.setWrap(true);
        info.setFontScale(0.5f);

        TextButton savedButton = new TextButton(Text.SAVE, Settings.gdxGame.getUIKit(), "button");
        savedButton.getLabel().setFontScale(0.4f);
        savedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.gdxGame.goToMenu(gameView, true);

                stopResultSounds();
            }
        });

        TextButton menuButton = new TextButton(Text.ON_THE_MENU, Settings.gdxGame.getUIKit(), "button");
        menuButton.getLabel().setFontScale(0.4f);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                Settings.gdxGame.goToMenu(gameView, false);

                stopResultSounds();
            }
        });

        TextButton cancelDialogButton = new TextButton(Text.CANCEL, Settings.gdxGame.getUIKit(), "button");
        cancelDialogButton.getLabel().setFontScale(0.4f);
        cancelDialogButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                dialog.hide();
                dialog.setVisible(false);
                Settings.controller.unblockGame();
            }
        });

        dialog.getTitleTable().add(title).padRight(330).padTop(20);
        dialog.getTitleTable().add(cancelButton).size(18, 17).padTop(26).padRight(13);
        dialog.getContentTable().add(info).width(380).padBottom(90);
        dialog.getButtonTable().add(menuButton).size(120, 33).padBottom(13.65f);
        dialog.getButtonTable().add(savedButton).size(120, 33).padBottom(13.65f);
        dialog.getButtonTable().add(cancelDialogButton).size(120, 33).padBottom(13.65f);

        stage.addActor(dialog);
        return dialog;
    }

    private void stopResultSounds() {
        Settings.SOUNDS.stopWinSounds();
        Settings.SOUNDS.stopLose();
    }

    /**
     * launching a DescriptionDialog for some time
     * */
    public void showStartDescriptionDialog() {
        /*Runnable task = () -> {
          try {
              Thread.sleep(200);
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
          showDescriptionDialog();
        };
        Settings.gdxGame.execute(task);

        Runnable task2 = () -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            descriptionDialog.hide();
        };
        Settings.gdxGame.execute(task2);*/
    }

    /**
     * DescriptionDialog
     * */
    public void showDescriptionDialog() {
        Settings.controller.blockGame();

        descriptionDialog = new Dialog("", Settings.gdxGame.getDialogSkin(), "transparent_rect");
        descriptionDialog.setPosition(Orientation.descriptionDialogX, Orientation.descriptionDialogY);
        descriptionDialog.setSize(500, 100);

        Image chessLog = new Image(Settings.gdxGame.findRegion("chess_logo"));

        Label title = new Label(Text.TITLE, Settings.gdxGame.getLabelSkin());
        title.setFontScale(0.7f);

        Label levelGame = new Label(Settings.controller.getLevel().toString(), Settings.gdxGame.getLabelSkin());
        levelGame.setFontScale(0.5f);

        descriptionDialog.getTitleTable().add(chessLog).padRight(370).padTop(100).size(80, 80);
        descriptionDialog.getContentTable().add(title).padBottom(10).padLeft(120).row();
        descriptionDialog.getContentTable().add(levelGame).padLeft(110);

        stage.addActor(descriptionDialog);
        addDialogHandler(descriptionDialog, () -> {
            Settings.controller.unblockGame();
            descriptionDialog.hide();
        });
    }

    /**
     * The task will run until the callback is fired.
     * To force installation, set visible to false.
     * */
    private void addDialogHandler(Dialog dialog, DialogAction dialogAction) {
        Sprite sprite = new Sprite();
        sprite.setPosition(dialog.getX(), dialog.getY());
        sprite.setSize(dialog.getWidth(), dialog.getHeight());

        Vector3 touchPoint = new Vector3();

        Runnable task = () -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (dialog.isVisible()) {
                if (Gdx.input.isTouched()) {
                    gameView.getCamera().unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

                    if (!sprite.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
                        dialogAction.action();
                        return;
                    }
                }
                Thread.yield();
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    System.out.println("debug: Dialog handler thread interrupted");
                }
            }
        };
        Settings.gdxGame.execute(task);
    }

    private void nextUserLevel() {
        if (Settings.account.getUserLevel() == Settings.account.getChoiceLevel()) {
            Settings.account.nextUserLevel();
        }
    }

    private interface DialogAction {
        void action();
    }
}
