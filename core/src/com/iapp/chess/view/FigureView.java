package com.iapp.chess.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.iapp.chess.controller.GameController;
import com.iapp.chess.model.Move;
import com.iapp.chess.util.CallListener;
import com.iapp.chess.util.Orientation;
import com.iapp.chess.util.Settings;

import java.util.HashMap;
import java.util.Map;

public class FigureView {

    private GameController gameController;
    private boolean visible = true;
    private int x = -1, y = -1;
    private int lastX = -1, lastY = -1;
    private float effectX, effectY;

    private boolean makesMove;
    private Sprite sprite;
    private CallListener callback;

    public FigureView(int x, int y, TextureAtlas.AtlasRegion image) {
        this.gameController = gameController;
        this.x = x;
        this.y = y;
        sprite = new Sprite(image);
    }

    public FigureView(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setImage(TextureAtlas.AtlasRegion image) {
        this.sprite = new Sprite(image);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLastX() {
        return lastX;
    }

    public int getLastY() {
        return lastY;
    }

    public boolean isMakesMove() {
        return makesMove;
    }

    public void setMakesMove(boolean makesMove) {
        this.makesMove = makesMove;
    }

    public void addOnFinishSound(CallListener callback) {
        this.callback = callback;
    }

    public void setPosition(int x, int y) {
        lastX = this.x;
        lastY = this.y;
        this.x = x;
        this.y = y;
    }

    public void makeMove(int moveX, int moveY) {
        if (isValid()) {
            effectX = BoardMatrix.getPositionX(moveX) - BoardMatrix.getPositionX(x);
            effectY = BoardMatrix.getPositionY(moveY) - BoardMatrix.getPositionY(y);
        }
        setPosition(moveX, moveY);

        makesMove = true;
        effectCompleted = false;
    }

    public void showMoves(GameController gameController, GameView gameView) {
        if (!visible) return;

        Array<Move> moves = gameView.getMoves();
        if (!moves.isEmpty()) gameView.clearGreenCross();

        Map<Move, Sprite> sprites = new HashMap<>();
        addInvisibleMoves(moves, sprites);

        if (Gdx.input.isTouched()) {
            Vector3 touchPoint = new Vector3();

            for (Move move : moves) {
                gameView.getCamera().unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

                if (sprites.get(move).getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
                    gameController.makeMove(move);
                    if (!gameController.isUpdated(move)) gameView.clearMoves();
                    break;
                }
            }
        }
    }

    @Override
    public String toString() {
        return String.format("x = %d, y = %d", x, y);
    }

    private boolean effectCompleted = true;

    public void draw(SpriteBatch batch) {
        if (!visible) return;

        effectY -= effectY / 5;
        effectX -= effectX / 5;
        sprite.setPosition(BoardMatrix.getPositionX(x) - effectX, BoardMatrix.getPositionY(y) - effectY + 2);
        sprite.setSize(Orientation.figureSpriteSize, Orientation.figureSpriteSize);
        batch.draw(sprite, BoardMatrix.getPositionX(x) - effectX + Orientation.figureMarginX, BoardMatrix.getPositionY(y) - effectY + Orientation.figureMarginY,
                Orientation.figureSpriteSize, Orientation.figureSpriteSize);

        if (!effectCompleted && effectX < 0.1f && effectY < 0.1f && effectX > -0.1f && effectY > -0.1f) {
            effectX = 0;
            effectY = 0;
            effectCompleted = true;

            if (callback != null) callback.call();
            callback = null;
        }

        if (effectCompleted) {
            makesMove = false;
        }
    }

    public boolean isTouched(OrthographicCamera camera) {
        Vector3 touchPoint = new Vector3();

        camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),0));

        if (((int)sprite.getBoundingRectangle().x) == 0) return false;
        return sprite.getBoundingRectangle().contains(touchPoint.x, touchPoint.y);
    }

    private void addInvisibleMoves(Array<Move> moves, Map<Move, Sprite> sprites) {
        for (Move move : moves) {
            Sprite noVisibleMove = new Sprite();
            noVisibleMove.setPosition(BoardMatrix.getPositionX(move.getMoveX()), BoardMatrix.getPositionY(move.getMoveY()));
            noVisibleMove.setSize(Orientation.figureSpriteSize, Orientation.figureSpriteSize);
            sprites.put(move, noVisibleMove);
        }
    }

    private boolean isValid() {
        return x != -1 && y != -1;
    }
}
