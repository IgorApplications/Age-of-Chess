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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.iapp.chess.controller.Controller;
import com.iapp.chess.controller.Level;
import com.iapp.chess.model.*;
import com.iapp.chess.util.FigureSet;
import com.iapp.chess.util.Orientation;
import com.iapp.chess.util.Settings;
import com.iapp.chess.util.Text;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

public class GameView implements Screen {

    private DialogView dialogView;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private ShapeRenderer renderer;

    private Stage stage;
    private Label countMoves;
    private Label aiMove;

    private TextureAtlas figures;
    private TextureAtlas.AtlasRegion background;
    private TextureAtlas.AtlasRegion board;
    private TextureAtlas.AtlasRegion greenFrame;
    private TextureAtlas.AtlasRegion yellowFrame;
    private TextureAtlas.AtlasRegion redFrame;
    private TextureAtlas.AtlasRegion blueFrame;

    private final List<Move> moves = new CopyOnWriteArrayList<>();
    private List<FigureView> figureViews = new CopyOnWriteArrayList<>();
    private Move blueFrameMove;
    private boolean canCutFigures = true;

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
            figures = Settings.gdxGame.getStandardFigures();
        } else if (Settings.account.getChosenFigureSet() == FigureSet.MODE) {
            figures = Settings.gdxGame.getModeFigures();
        }
    }

    public void initGUI(Controller controller) {
        initStage();
        initFigures();

        dialogView = new DialogView(this, stage);

        King king = controller.findCheckKing();
        if (king != null) drawCheckKing(findFigure(king.getX(), king.getY()));
    }

    public DialogView getDialogView() {
        return dialogView;
    }

    public void setDialogView(DialogView dialogView) {
        this.dialogView = dialogView;
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

    public void setFigureViews(List<FigureView> figureViews) {
        this.figureViews = figureViews;
    }

    public List<FigureView> getFigureViews() {
        return figureViews;
    }

    public void setCanCutFigures(boolean canCutFigures) {
        this.canCutFigures = canCutFigures;
    }

    /**
     * draw methods
     * */

    public void drawMoves(List<Move> moves) {
        this.moves.clear();
        this.moves.addAll(moves);
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

    public void drawCheckKing(FigureView checkKing) {
        this.checkKing = checkKing;
    }

    public void clearCheckKing() {
        checkKing = null;
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

    public void clearChosenFigure() {
        chosenFigure = null;
    }

    @Override
    public void show() {
        dialogView.showStartDescriptionDialog();
    }

    @Override
    public void render(float delta) {
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, Orientation.cameraWidth, Orientation.cameraHeight);
        batch.draw(board, Orientation.boardX, Orientation.boardY, Orientation.boardWidth, Orientation.boardHeight);
        figureViews.forEach(FigureView::draw);
        batch.end();

        if (Settings.account.getOrientationType() == Orientation.Type.VERTICAL) drawLines();
        drawRedHint();
        if (!isAnyFigureDoMove()) drawGreenHint();
        drawMoves();
        drawGreenCross();
        drawBlueFrame();

        if (chosenFigure != null && !Settings.controller.isBlockedGame()) chosenFigure.showMoves();
        if (Gdx.input.isTouched() && !Settings.controller.isBlockedGame()) processingTouchFigures();

        Runnable task2 = () -> {
            synchronized (Settings.MUTEX) {
                long turn = Settings.controller.getTurn();
                if (turn != -1) {
                    countMoves.setText(turn + ". " + Settings.controller.defineColorMove());
                }
            }
        };
        Settings.gdxGame.execute(task2);

        if (Settings.controller.isAIMakesMove()) {
            aiMove.setText(Text.MOVE_AI);
        } else {
            aiMove.setText("");
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().setScreenSize(width, height);
    }

    @Override
    public void pause() {}

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

    public void cutDown(FigureView selfFigure, Move cutDownMove) {
        if (!canCutFigures) return;

        for (FigureView figure : figureViews) {
            if (figure != selfFigure && figure.getX() == cutDownMove.getX() && figure.getY() == cutDownMove.getY()) {
                figure.setVisible(false);
            }
        }
    }

    public FigureView findFigure(int x, int y) {
        for (FigureView figureView : figureViews) {
            if (figureView.getX() == x && figureView.getY() == y && figureView.isVisible())
                return figureView;
        }
        throw new RuntimeException("Iapp: FigureView dont found");
    }

    public FigureView findInVisibleFigure(Figure figure) {
        for (FigureView figureView : figureViews) {
            if (figureView.getType() == figure)
                return figureView;
        }
        throw new RuntimeException("Iapp: FigureView dont found");
    }

    public void updateFigure(FigureView updatedPawn, Move move, Class<? extends Figure> updateType) {
        Settings.controller.moveUser(updatedPawn, move);
        Settings.controller.updatePawn(move.getX(), move.getY(), updatedPawn.getColor(), updateType);
        updatedPawn.setType(Settings.controller.getFigure(move.getX(), move.getY()));
        updateSprite(updatedPawn, updateType);

        Settings.controller.unblockGame();

        Settings.controller.moveAI();
        Settings.controller.drawCheckKingIfFound();
        drawRedHint();
    }

    private void updateSprite(FigureView figureView, Class<? extends Figure> updateType) {
        if (Queen.class == updateType) {
            setSprite(figureView, findFigure("black_queen"),  findFigure("white_queen"));
        } else if (Rook.class == updateType) {
            setSprite(figureView,  findFigure("black_rook"),  findFigure("white_rook"));
        } else if (Bishop.class == updateType) {
            setSprite(figureView,  findFigure("black_bishop"),  findFigure("white_bishop"));
        } else if (Knight.class == updateType) {
            setSprite(figureView,  findFigure("black_knight"),  findFigure("white_knight"));
        }
        else throw new RuntimeException("Iapp: Try update Sprite figure. It's new class figure or it's Pawn class!");
    }

    private void drawBlueFrame() {
        if (blueFrameMove != null) {
            batch.begin();
            batch.draw(blueFrame, BoardMatrix.getPositionX(blueFrameMove.getX()), BoardMatrix.getPositionY(blueFrameMove.getY()),
                    BoardMatrix.getSizeWidth(blueFrameMove.getX()), BoardMatrix.getSizeHeight(blueFrameMove.getY()));
            batch.end();

            if (Settings.account.isBlockedOutlineFigure()) {
                batch.begin();
                batch.draw(greenFrame, BoardMatrix.getPositionX(chosenFigure.getX()), BoardMatrix.getPositionY(chosenFigure.getY()), BoardMatrix.getSizeWidth(chosenFigure.getX()), BoardMatrix.getSizeHeight(chosenFigure.getY()));
                batch.end();
            }
        }
    }

    private void drawLines() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setProjectionMatrix(camera.combined);

        renderer.setColor(0, 0, 0, 0.8f);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.rect(0, Orientation.blackLineY, Orientation.cameraWidth, Orientation.blackLineHeight);
        renderer.end();

        renderer.setColor(0.2f, 0.09f, 0, 1.0f);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.rect(0, 0, Orientation.cameraWidth, 40);
        renderer.end();
    }

    private void drawGreenHint() {
        if (chosenFigure != null && Settings.account.isDrawableOutlineFigure()) {
            if (!isCheckKing(chosenFigure)) {
                batch.begin();
                batch.draw(greenFrame, BoardMatrix.getPositionX(chosenFigure.getX()), BoardMatrix.getPositionY(chosenFigure.getY()), BoardMatrix.getSizeWidth(chosenFigure.getX()), BoardMatrix.getSizeHeight(chosenFigure.getY()));
                batch.end();
            }
        }
    }

    private boolean isCheckKing(FigureView figureView) {
        if (checkKing != null && figureView.getX() == checkKing.getX() && figureView.getY() == checkKing.getY())
            return true;
        return false;
    }

    private void drawRedHint() {
        if (checkKing != null && Settings.account.isDrawableHintCheck()) {
            batch.begin();
            batch.draw(redFrame, BoardMatrix.getPositionX(checkKing.getX()), BoardMatrix.getPositionY(checkKing.getY()), BoardMatrix.getSizeWidth(checkKing.getX()), BoardMatrix.getSizeHeight(checkKing.getY()));
            batch.end();
        }
    }

    private void drawMoves() {
        for (Move move : moves) {
            if (!(Settings.controller.getFigure(move.getX(), move.getY()) instanceof Cage) && Settings.account.isDrawableOutlineFelledFigure()) {
                int moveX = move.getX();
                int moveY = move.getY();
                batch.begin();
                batch.draw(yellowFrame, BoardMatrix.getPositionX(moveX), BoardMatrix.getPositionY(moveY), BoardMatrix.getSizeWidth(moveX), BoardMatrix.getSizeHeight(moveY));
                batch.end();
            } else if (move.isCastlingMove() && Settings.account.isDrawableHintCastle()) {
                renderer.setColor(0.25f, 0.41f, 1, 1);
                renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.circle(BoardMatrix.getPositionX(move.getX()) + Orientation.moveMarginX, BoardMatrix.getPositionY(move.getY()) + Orientation.moveMarginY, 6.5f);
                renderer.end();
            } else if (Settings.account.isDrawableHintMoves()) {
                renderer.setColor(0, 0.5f, 0, 1);
                renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.circle(BoardMatrix.getPositionX(move.getX()) + Orientation.moveMarginX, BoardMatrix.getPositionY(move.getY()) + Orientation.moveMarginY, 6.5f);
                renderer.end();
            };
        }
    }

    private void drawGreenCross() {
        if (figureGreenCross != null && Settings.account.isDrawableGreenCross()) {
            batch.begin();
            batch.draw(Settings.gdxGame.findRegion("green_cross"),
                    BoardMatrix.getPositionX(figureGreenCross.getLastX()), BoardMatrix.getPositionY(figureGreenCross.getLastY()), BoardMatrix.WIDTH, BoardMatrix.HEIGHT);
            batch.end();
        }
    }

    private void processingTouchFigures() {
        for (FigureView figureView : figureViews) {
            if (!figureView.isVisible()) continue;

            Color aiColor = Settings.controller.getAIColor();
            Color userColor  = Settings.controller.getUserColor();
            if (figureView.isTouched(camera) &&
                    ((figureView.getColor() == aiColor && Settings.controller.defineColorMove() == aiColor)
                            || (figureView.getColor() == userColor && Settings.controller.defineColorMove() == userColor))) {
                chosenFigure = figureView;
                if (blueFrameMove != null && !Settings.account.isDrawableHintMoves()) {
                    clearBlueHint();
                    Settings.account.setDrawableHintMoves(true);
                }
            }
        }
    }

    private boolean isAnyFigureDoMove() {
        for (FigureView figureView : figureViews) {
            if (figureView.isDoMove()) return true;
        }
        return false;
    }

    private void initStage() {
        stage = new Stage(new StretchViewport(Orientation.cameraWidth, Orientation.cameraHeight, camera));
        Gdx.input.setInputProcessor(stage);

        initButtons();
        initLabels();
    }

    private void initLabels() {
        countMoves = new Label("", Settings.gdxGame.getLabelSkin());
        countMoves.setText("1. " + Settings.controller.defineColorMove());
        countMoves.setFontScale(Orientation.labelCountMovesFontScale);
        countMoves.setPosition(Orientation.labelCountMovesX, Orientation.labelCountMovesY);

        aiMove = new Label("", Settings.gdxGame.getLabelSkin());
        aiMove.setFontScale(Orientation.labelLevelFontScale);
        aiMove.setPosition(Orientation.moveAIX, Orientation.moveAIY);

        Table header = new Table();
        header.setPosition(0, Orientation.headerY);
        header.setWidth(Orientation.cameraWidth);

        Label level = new Label(Settings.controller.getLevel().toString(), Settings.gdxGame.getLabelSkin());
        level.setFontScale(Orientation.labelLevelFontScale);

        if (Settings.account.getOrientationType() == Orientation.Type.VERTICAL) {
            Label title = new Label(Text.TITLE, Settings.gdxGame.getLabelSkin());
            title.setFontScale(0.7f);
            header.add(title).center().row();
            header.add(level).padTop(8).center();
        } else {
            header.add(level).padLeft(30).center();
        }

        stage.addActor(header);
        stage.addActor(countMoves);
        if (Settings.account.getChoiceLevel() != Level.TWO_PLAYERS) {
            stage.addActor(aiMove);
        }
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

                if (Settings.controller.getTurn() < 2) {
                    Settings.gdxGame.goToMenu(GameView.this, false);
                } else {
                    dialogView.showMenuDialog();
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
                dialogView.showReplayGameDialog();
            }
        });

        ImageButton buttonBack = new ImageButton(Settings.gdxGame.getUIKit(), "rect");
        addImage(buttonBack, Settings.gdxGame.findUIKitRegion("undo"), 40, 40);
        buttonBack.setPosition(Orientation.buttonBackX, Orientation.buttonBackY);
        buttonBack.setSize(Orientation.buttonBackWidth, Orientation.buttonBackHeight);
        buttonBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (Settings.controller.getLevel() == Level.TWO_PLAYERS) Settings.controller.cancelMove();
                else Settings.controller.cancelTurn();
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
                dialogView.showDescriptionDialog();
            }
        });

        ImageButton buttonHint = new ImageButton(Settings.gdxGame.getUIKit(), "rect");
        addImage(buttonHint, Settings.gdxGame.findUIKitRegion("bulb"), 26, 44);
        buttonHint.setPosition(Orientation.buttonHintX, Orientation.buttonHintY);
        buttonHint.setSize(Orientation.buttonHintWidth, Orientation.buttonHintHeight);
        buttonHint.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (Settings.controller.isBlockedGame()) return;
                Settings.SOUNDS.playClick();
                Settings.controller.showPromptingMove();
            }
        });

        stage.addActor(buttonMenu);
        stage.addActor(buttonReplay);
        stage.addActor(buttonBack);
        stage.addActor(buttonDescription);
        stage.addActor(buttonHint);

        Settings.launcher.addOnFinish(() -> Gdx.app.postRunnable(() -> {
            if (Settings.controller.getTurn() < 2|| (onFinish != null && onFinish.isVisible())) {
                Settings.gdxGame.goToMenu(GameView.this, false);
            } else {
                onFinish = dialogView.showMenuDialog();
            }
        }));
    }

    public TextureAtlas.AtlasRegion findFigure(String region) {
        return figures.findRegion(region);
    }

    private void initFigures() {
        TextureAtlas.AtlasRegion imagePawnBlack = figures.findRegion("black_pawn");
        TextureAtlas.AtlasRegion imagePawnWhite = figures.findRegion("white_pawn");

        TextureAtlas.AtlasRegion imageRookBlack = figures.findRegion("black_rook");
        TextureAtlas.AtlasRegion imageRookWhite = figures.findRegion("white_rook");

        TextureAtlas.AtlasRegion imageKnightBlack = figures.findRegion("black_knight");
        TextureAtlas.AtlasRegion imageKnightWhite = figures.findRegion("white_knight");

        TextureAtlas.AtlasRegion imageBishopBlack = figures.findRegion("black_bishop");
        TextureAtlas.AtlasRegion imageBishopWhite = figures.findRegion("white_bishop");

        TextureAtlas.AtlasRegion imageQueenBlack = figures.findRegion("black_queen");
        TextureAtlas.AtlasRegion imageQueenWhite = figures.findRegion("white_queen");

        TextureAtlas.AtlasRegion imageKingBlack = figures.findRegion("black_king");
        TextureAtlas.AtlasRegion imageKingWhite = figures.findRegion("white_king");

        for (Figure figure : Settings.controller.getGameFigures()) {
            if (figure instanceof Pawn) addFigure(imagePawnBlack, imagePawnWhite, figure);
            else if (figure instanceof Rook) addFigure(imageRookBlack, imageRookWhite, figure);
            else if (figure instanceof Knight) addFigure(imageKnightBlack, imageKnightWhite, figure);
            else if (figure instanceof Bishop) addFigure(imageBishopBlack, imageBishopWhite, figure);
            else if (figure instanceof Queen) addFigure(imageQueenBlack, imageQueenWhite, figure);
            else addFigure(imageKingBlack, imageKingWhite, figure);
        }
    }

    private void addFigure(TextureAtlas.AtlasRegion imageBlack, TextureAtlas.AtlasRegion imageWhite, Figure figure) {
        if (figure.getColor() == Color.BLACK) {
            FigureView figureBlack = new FigureView(this, batch, imageBlack, figure, Color.BLACK);
            figureViews.add(figureBlack);
        } else {
            FigureView figureWhite = new FigureView(this, batch, imageWhite, figure, Color.WHITE);
            figureViews.add(figureWhite);
        }
    }

    void addImage(ImageButton imageButton, TextureAtlas.AtlasRegion region) {
        Image image = new Image(region);
        imageButton.center().add(image).padTop(3).padRight(2.7f);
    }

    void addImage(ImageButton imageButton, TextureAtlas.AtlasRegion region, float width, float height) {
        Image image = new Image(region);
        imageButton.center().add(image).size(width, height).padTop(3).padRight(2.7f);
    }

    public void setSprite(FigureView figureView, TextureAtlas.AtlasRegion imageBlack, TextureAtlas.AtlasRegion imageWhite) {
        if (figureView.getColor() == Color.BLACK) figureView.setSprite(imageBlack);
        else figureView.setSprite(imageWhite);
    }
}
