package com.iapp.chess.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.iapp.chess.model.Color;
import com.iapp.chess.model.Figure;
import com.iapp.chess.model.Move;
import com.iapp.chess.model.Pawn;
import com.iapp.chess.util.Orientation;
import com.iapp.chess.util.Settings;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FigureView implements Serializable {

    private int x, y, lastX, lastY;
    private float effectX, effectY;

    private Figure type;
    private Color color;
    private Move currentMove;
    private boolean visible = true;
    private boolean makeSoundMove = true;

    private transient GameView gameView;
    private transient SpriteBatch batch;
    private transient Sprite sprite;
    private boolean doMove;

    public FigureView(GameView gameView, SpriteBatch batch, TextureAtlas.AtlasRegion image, Figure type, Color color) {
        this.batch = batch;
        sprite = new Sprite(image);
        this.type = type;
        this.x = type.getX();
        this.y = type.getY();
        this.color = color;
        this.gameView = gameView;
    }

    public GameView getGameView() {
        return gameView;
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setSprite(TextureAtlas.AtlasRegion image) {
        this.sprite = new Sprite(image);
    }

    public Figure getType() {
        return type;
    }

    public void setType(Figure type) {
        this.type = type;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isDoMove() {
        return doMove;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getLastX() { return lastX; }

    public int getLastY() { return lastY; }

    public boolean isMakeSoundMove() {
        return makeSoundMove;
    }

    public void setMakeSoundMove(boolean makeSoundMove) {
        this.makeSoundMove = makeSoundMove;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        doMove = true;
    }

    public void doMove(Move move) {
        currentMove = move;
        effectX = BoardMatrix.getPositionX(move.getX()) - BoardMatrix.getPositionX(this.x);
        effectY = BoardMatrix.getPositionY(move.getY()) - BoardMatrix.getPositionY(this.y);
        lastX = this.x;
        lastY = this.y;
        this.x = move.getX();
        this.y = move.getY();
        doMove = true;
    }

    public void castle(Move castleMove, int x) {
        currentMove = castleMove;
        effectX = BoardMatrix.getPositionX(x) - BoardMatrix.getPositionX(this.x);
        lastX = this.x;
        this.x = x;
        doMove = true;
    }

    public void showMoves() {
        if (!visible) return;

        List<Move> moves = Settings.controller.getMoves(x, y);
        if (!moves.isEmpty()) gameView.clearGreenCross();
        Map<Move, Sprite> sprites = new HashMap<>();

        addInvisibleMoves(moves, sprites);
        gameView.drawMoves(moves);

        if (Gdx.input.isTouched()) {
            Vector3 touchPoint = new Vector3();

            for (Move move : moves) {
                gameView.getCamera().unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

                if (sprites.get(move).getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {

                    if (Settings.controller.getFigure(x, y) instanceof Pawn &&
                            (move.getY() == 7 || move.getY() == 0)) {
                        gameView.getDialogView().showChoiceFigureDialog(this, move);
                        return;
                    }

                    currentMove = move;
                    Settings.controller.moveUser(this, move);
                    Settings.controller.moveAI();

                    break;
                }
            }
        }
    }

    public void draw() {
        if (!visible && doMove) doMove = false;
        if (!visible) return;

        effectY -= effectY / 5;
        effectX -= effectX / 5;
        sprite.setPosition(BoardMatrix.getPositionX(x) - effectX, BoardMatrix.getPositionY(y) - effectY + 2);
        sprite.setSize(Orientation.figureSpriteSize, Orientation.figureSpriteSize);
        batch.draw(sprite, BoardMatrix.getPositionX(x) - effectX + Orientation.figureMarginX, BoardMatrix.getPositionY(y) - effectY + Orientation.figureMarginY,
                Orientation.figureSpriteSize, Orientation.figureSpriteSize);
        if (doMove && effectX < 0.1f && effectY < 0.1f && effectX > -0.1f && effectY > -0.1f) {
            effectX = 0;
            effectY = 0;
            doMove = false;
            if (currentMove != null && !currentMove.isCastlingMove() && makeSoundMove)
                Settings.SOUNDS.playMove();
            makeSoundMove = true;
        }
    }

    public boolean isTouched(OrthographicCamera camera) {
        Vector3 touchPoint = new Vector3();

        camera.unproject(touchPoint.set(Gdx.input.getX(),Gdx.input.getY(),0));
        return sprite.getBoundingRectangle().contains(touchPoint.x, touchPoint.y);
    }

    private void addInvisibleMoves(List<Move> moves, Map<Move, Sprite> sprites) {
        for (Move move : moves) {
            Sprite noVisibleMove = new Sprite();
            noVisibleMove.setPosition(BoardMatrix.getPositionX(move.getX()), BoardMatrix.getPositionY(move.getY()));
            noVisibleMove.setSize(Orientation.figureSpriteSize, Orientation.figureSpriteSize);
            sprites.put(move, noVisibleMove);
        }
    }

    public void moveView(int x, int y) {
        effectX = BoardMatrix.getPositionX(x) - BoardMatrix.getPositionX(this.x);
        effectY = BoardMatrix.getPositionY(y) - BoardMatrix.getPositionY(this.y);
        this.x = x;
        this.y = y;
        doMove = true;
    }

    private boolean existsCheckKing() {
        return Settings.controller.findCheckKing() != null;
    }
}
