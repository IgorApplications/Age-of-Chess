package com.iapp.chess.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.iapp.chess.controller.GameController;
import com.iapp.chess.controller.Level;
import com.iapp.chess.model.*;
import com.iapp.chess.util.*;

import java.util.Arrays;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

public class GameView implements Screen {

    private GameController gameController;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private final ShapeRenderer renderer;

    private boolean drawableOutlineFigure = true;
    private boolean drawableOutlineFelledFigure = true;
    private boolean drawableHintMoves = true;
    private boolean drawableHintCastle = true;
    private boolean drawableHintCheck = true;
    private boolean drawableGreenCross = true;

    private Stage stage;
    private Label countTurns;

    private TextureAtlas figureSet;
    private final TextureAtlas.AtlasRegion background;
    private final TextureAtlas.AtlasRegion board;
    private final TextureAtlas.AtlasRegion greenFrame;
    private final TextureAtlas.AtlasRegion yellowFrame;
    private final TextureAtlas.AtlasRegion redFrame;
    private final TextureAtlas.AtlasRegion blueFrame;

    private final Array<Move> moves = new Array<>();
    private Move blueFrameMove;

    private FigureView chosenFigure;
    private FigureView checkKing;
    private FigureView figureGreenCross;

    public GameView() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Orientation.cameraWidth, Orientation.cameraHeight);
        renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(camera.combined);

        batch = new SpriteBatch();
        background = Settings.gdxGame.findRegion("background");
        board = Settings.gdxGame.findRegion("board");

        greenFrame = Settings.gdxGame.findRegion("green_frame");
        yellowFrame = Settings.gdxGame.findRegion("yellow_frame");
        redFrame = Settings.gdxGame.findRegion("red_frame");
        blueFrame = Settings.gdxGame.findRegion("blue_frame");

        if (Settings.account.getChosenFigureSet() == FigureSet.STANDARD) {
            figureSet = Settings.gdxGame.getStandardFigures();
        } else if (Settings.account.getChosenFigureSet() == FigureSet.MODE) {
            figureSet = Settings.gdxGame.getModeFigures();
        }
    }

    public void initGUI(GameController gameController) {
        this.gameController = gameController;
        initStage();
        gameController.initViewFigures(figureSet);

        DialogView dialogView = new DialogView(gameController,this, stage);
        gameController.setDialogView(dialogView);
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public Array<Move> getMoves() {
        return moves;
    }

    public TextureAtlas getFigureSet() {
        return figureSet;
    }

    public Stage getStage() {
        return stage;
    }

    /**
     * draw methods
     * */

    public void drawMoves(Array<Move> newMoves) {
        moves.clear();
        moves.addAll(newMoves);
    }

    public void clearMoves() {
        moves.clear();
    }

    public void drawBlueHint(Move move) {
        blueFrameMove = move;
    }

    public void clearBlueHint() {
        blueFrameMove = null;
    }

    public Move getBlueHint() {
        return blueFrameMove;
    }

    public void drawCheckKing(FigureView checkKing) {
        this.checkKing = checkKing;
    }

    public void clearCheckKing() {
        checkKing = null;
    }

    public FigureView getCheckKing() {
        return checkKing;
    }

    public void drawGreenCross(FigureView figureGreenCross) {
        this.figureGreenCross = figureGreenCross;
    }

    public void clearGreenCross() {
        figureGreenCross = null;
    }

    public void drawChosenFigure(FigureView chosenFigure) {
        this.chosenFigure = chosenFigure;
    }

    public void unselectChosenFigure() {
        chosenFigure = null;
    }

    /**
     * Drawable methods
     * */
    public boolean isDrawableOutlineFigure() {
        return drawableOutlineFigure && !Settings.account.isBlockedOutlineFigure();
    }

    public void setDrawableOutlineFigure(boolean drawableOutlineFigure) {
        this.drawableOutlineFigure = drawableOutlineFigure;
    }

    public boolean isDrawableOutlineFelledFigure() {
        return drawableOutlineFelledFigure && !Settings.account.isBlockedOutlineFelledFigure();
    }

    public void setDrawableOutlineFelledFigure(boolean drawableOutlineFelledFigure) {
        this.drawableOutlineFelledFigure = drawableOutlineFelledFigure;
    }

    public boolean isDrawableHintMoves() {
        return drawableHintMoves && !Settings.account.isBlockedHintMoves();
    }

    public void setDrawableHintMoves(boolean drawableHintMoves) {
        this.drawableHintMoves = drawableHintMoves;
    }

    public boolean isDrawableHintCastle() {
        return drawableHintCastle && !Settings.account.isBlockedHintCastle();
    }

    public void setDrawableHintCastle(boolean drawableHintCastle) {
        this.drawableHintCastle = drawableHintCastle;
    }

    public boolean isDrawableHintCheck() {
        return drawableHintCheck && !Settings.account.isBlockedHintCheck();
    }

    public void setDrawableHintCheck(boolean drawableHintCheck) {
        this.drawableHintCheck = drawableHintCheck;
    }

    public boolean isDrawableGreenCross() {
        return drawableGreenCross && !Settings.account.isBlockedGreenCross();
    }

    public void setDrawableGreenCross(boolean drawableHintGreenCross) {
        this.drawableGreenCross = drawableHintGreenCross;
    }

    @Override
    public void show() {
        gameController.getDialogView().showStartDescriptionDialog();
    }

    @Override
    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(background, 0, 0, Orientation.cameraWidth, Orientation.cameraHeight);
        batch.draw(board, Orientation.boardX, Orientation.boardY, Orientation.boardWidth, Orientation.boardHeight);
        Arrays.stream(gameController.getFigureViews()).forEach(f -> f.draw(batch));
        drawRedHint();
        drawGreenHint();
        drawGreenCross();
        drawYellowHint();
        drawBlueHint();
        batch.end();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        if (Settings.account.getOrientationType() == Orientation.Type.VERTICAL) drawLines();
        drawMoves();
        renderer.end();

        if (chosenFigure != null && !gameController.isBlockedGame()) chosenFigure.showMoves(gameController, this);
        if (Gdx.input.isTouched() && !gameController.isBlockedGame()) processingTouchFigures();

        String separator;
        if (gameController.isAIMakeMove()) separator = "... ";
        else separator = ". ";
        countTurns.setText(gameController.getTurn() + separator + gameController.getColorMove());

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().setScreenSize(width, height);
    }

    @Override
    public void pause() {
        gameController.updateSavedGame();
        Settings.DATA.saveAccount(Settings.account);
    }

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        renderer.dispose();
        Settings.launcher.addOnFinish(null);
    }

    TextureAtlas.AtlasRegion findImageFigure(String region) {
        return figureSet.findRegion(region);
    }

    void addImage(ImageButton imageButton, TextureAtlas.AtlasRegion region) {
        Image image = new Image(region);
        imageButton.center().add(image).padTop(3).padRight(2.7f);
    }

    void addImage(ImageButton imageButton, TextureAtlas.AtlasRegion region, float width, float height) {
        Image image = new Image(region);
        imageButton.center().add(image).size(width, height).padTop(3).padRight(2.7f);
    }

    /**
     * Batch draw methods
     * */
    private void drawBlueHint() {
        if (blueFrameMove != null) {
            batch.draw(blueFrame, BoardMatrix.getPositionX(blueFrameMove.getMoveX()), BoardMatrix.getPositionY(blueFrameMove.getMoveY()),
                    BoardMatrix.getSizeWidth(blueFrameMove.getMoveX()), BoardMatrix.getSizeHeight(blueFrameMove.getMoveY()));

            if (Settings.account.isBlockedOutlineFigure()) {
                batch.draw(greenFrame, BoardMatrix.getPositionX(chosenFigure.getX()), BoardMatrix.getPositionY(chosenFigure.getY()), BoardMatrix.getSizeWidth(chosenFigure.getX()), BoardMatrix.getSizeHeight(chosenFigure.getY()));
            }
        }
    }

    private void drawRedHint() {
        if (checkKing != null && !checkKing.isMakesMove() && isDrawableHintCheck()) {
            batch.draw(redFrame, BoardMatrix.getPositionX(checkKing.getX()), BoardMatrix.getPositionY(checkKing.getY()),
                    BoardMatrix.getSizeWidth(checkKing.getX()), BoardMatrix.getSizeHeight(checkKing.getY()));
        }
    }

    private void drawGreenHint() {
        if (chosenFigure != null && !chosenFigure.isMakesMove() && isDrawableOutlineFigure()) {
            if (!gameController.isCheckKing(chosenFigure)) {
                batch.draw(greenFrame, BoardMatrix.getPositionX(chosenFigure.getX()), BoardMatrix.getPositionY(chosenFigure.getY()),
                        BoardMatrix.getSizeWidth(chosenFigure.getX()), BoardMatrix.getSizeHeight(chosenFigure.getY()));
            }
        }
    }

    private void drawYellowHint() {
        for (Move move : moves) {
            if (gameController.isThereFigure(move) && isDrawableOutlineFelledFigure()) {
                batch.draw(yellowFrame, BoardMatrix.getPositionX(move.getMoveX()), BoardMatrix.getPositionY(move.getMoveY()),
                        BoardMatrix.getSizeWidth(move.getMoveX()), BoardMatrix.getSizeHeight(move.getMoveY()));
            }
        }
    }

    private void drawGreenCross() {
        if (figureGreenCross != null && isDrawableGreenCross()) {
            batch.draw(Settings.gdxGame.findRegion("green_cross"), BoardMatrix.getPositionX(figureGreenCross.getLastX()),
                    BoardMatrix.getPositionY(figureGreenCross.getLastY()), BoardMatrix.WIDTH, BoardMatrix.HEIGHT);
        }
    }

    /**
     * Render draw methods
     * */
    private void drawMoves() {
        for (Move move : moves) {
            if (gameController.isCastleMove(move) && isDrawableHintCastle()) {
                renderer.setColor(0, 0, 1, 1);
                renderer.circle(BoardMatrix.getPositionX(move.getMoveX()) + Orientation.moveMarginX,
                        BoardMatrix.getPositionY(move.getMoveY()) + Orientation.moveMarginY, 6.5f);
            } else if (isDrawableHintMoves() && !gameController.isThereFigure(move)) {
                renderer.setColor(0, 0.5f, 0, 1);
                renderer.circle(BoardMatrix.getPositionX(move.getMoveX()) + Orientation.moveMarginX,
                        BoardMatrix.getPositionY(move.getMoveY()) + Orientation.moveMarginY, 6.5f);
            }
        }
    }

    private void drawLines() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setProjectionMatrix(camera.combined);

        renderer.setColor(0, 0, 0, 0.8f);
        renderer.rect(0, Orientation.blackLineY, Orientation.cameraWidth, Orientation.blackLineHeight);

        renderer.setColor(0.2f, 0.09f, 0, 1.0f);
        renderer.rect(0, 0, Orientation.cameraWidth, 40);
    }

    private void processingTouchFigures() {
        for (FigureView figureView : gameController.getFigureViews()) {
            if (!figureView.isVisible()) continue;

            if (figureView.isTouched(camera) && gameController.getColor(figureView) == gameController.getColorMove()) {

                if (blueFrameMove != null && !isDrawableHintMoves()) {
                    setDrawableHintMoves(true);
                    setDrawableHintCastle(true);
                    drawMoves(gameController.getMoves(figureView.getX(), figureView.getY()));
                } else if (figureView != chosenFigure) {
                    drawMoves(gameController.getMoves(figureView.getX(), figureView.getY()));
                }

                chosenFigure = figureView;
                clearBlueHint();
            }
        }
    }

    private void initStage() {
        stage = new Stage(new StretchViewport(Orientation.cameraWidth, Orientation.cameraHeight, camera));
        Gdx.input.setInputProcessor(stage);

        initButtons();
        initLabels();
    }

    private void initLabels() {
        countTurns = new Label("", Settings.gdxGame.getLabelSkin());
        countTurns.setText("1. " + gameController.getColorMove());
        countTurns.setFontScale(Orientation.labelCountMovesFontScale);
        countTurns.setPosition(Orientation.labelCountMovesX, Orientation.labelCountMovesY);

        Table header = new Table();
        header.setPosition(0, Orientation.headerY);
        header.setWidth(Orientation.cameraWidth);

        Label level = new Label(gameController.getLevel().toString(), Settings.gdxGame.getLabelSkin());
        level.setFontScale(Orientation.labelLevelFontScale);

        if (Settings.account.getOrientationType() == Orientation.Type.VERTICAL) {
            Label title = new Label(Text.TITLE, Settings.gdxGame.getLabelSkin());
            title.setFontScale(0.7f);
            header.add(title).center().row();
            header.add(level).padTop(8).center();
        } else {
            header.add(level).center();
        }

        stage.addActor(header);
        stage.addActor(countTurns);
    }

    private Dialog onFinish;

    private void initButtons() {
        ImageButton buttonMenu = new ImageButton(Settings.gdxGame.getUIKit(), "rect");
        addImage(buttonMenu, Settings.gdxGame.findUIKitRegion("menu"));
        buttonMenu.setPosition(Orientation.buttonMenuX, Orientation.buttonMenuY);
        buttonMenu.setSize(Orientation.buttonMenuWidth, Orientation.buttonMenuHeight);
        buttonMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                if (gameController.getTurn() < 2) {
                    gameController.goToMenu(GameView.this, false);
                } else {
                    gameController.getDialogView().showMenuDialog();
                }
            }
        });

        ImageButton buttonReplay = new ImageButton(Settings.gdxGame.getUIKit(), "rect");
        addImage(buttonReplay, Settings.gdxGame.findUIKitRegion("replay"), 50, 50);
        buttonReplay.setPosition(Orientation.buttonReplayX, Orientation.buttonReplayY);
        buttonReplay.setSize(Orientation.buttonReplayWidth, Orientation.buttonReplayHeight);
        buttonReplay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                gameController.getDialogView().showReplayGameDialog();
            }
        });

        ImageButton buttonBack = new ImageButton(Settings.gdxGame.getUIKit(), "rect");
        addImage(buttonBack, Settings.gdxGame.findUIKitRegion("undo"), 40, 40);
        buttonBack.setPosition(Orientation.buttonBackX, Orientation.buttonBackY);
        buttonBack.setSize(Orientation.buttonBackWidth, Orientation.buttonBackHeight);
        buttonBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                if (gameController.getLevel() == Level.TWO_PLAYERS) gameController.cancelMove();
                else gameController.cancelTurn();
            }
        });

        ImageButton buttonDescription = new ImageButton(Settings.gdxGame.getUIKit(), "rect");
        addImage(buttonDescription, Settings.gdxGame.findUIKitRegion("figure"), 26, 44);
        buttonDescription.setPosition(Orientation.buttonDescriptionX, Orientation.buttonDescriptionY);
        buttonDescription.setSize(Orientation.buttonDescriptionWidth, Orientation.buttonDescriptionHeight);
        buttonDescription.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                gameController.getDialogView().showDescriptionDialog();
            }
        });

        ImageButton buttonHint = new ImageButton(Settings.gdxGame.getUIKit(), "rect");
        addImage(buttonHint, Settings.gdxGame.findUIKitRegion("bulb"), 26, 44);
        buttonHint.setPosition(Orientation.buttonHintX, Orientation.buttonHintY);
        buttonHint.setSize(Orientation.buttonHintWidth, Orientation.buttonHintHeight);
        buttonHint.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.SOUNDS.playClick();
                gameController.showHint();
            }
        });

        stage.addActor(buttonMenu);
        stage.addActor(buttonReplay);
        stage.addActor(buttonBack);
        stage.addActor(buttonDescription);
        stage.addActor(buttonHint);

        Settings.launcher.addOnFinish(() -> Gdx.app.postRunnable(() -> {
            if (gameController.getTurn() < 2|| (onFinish != null && onFinish.isVisible())) {
                gameController.goToMenu(GameView.this, false);
            } else {
                onFinish = gameController.getDialogView().showMenuDialog();
            }
        }));
    }
}
