package com.iapp.chess.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.iapp.chess.model.*;
import com.iapp.chess.model.ai.AI;
import com.iapp.chess.util.Bool;
import com.iapp.chess.util.CallListener;
import com.iapp.chess.util.FigureSet;
import com.iapp.chess.util.Settings;
import com.iapp.chess.view.DialogView;
import com.iapp.chess.view.FigureView;
import com.iapp.chess.view.GameView;
import com.iapp.chess.view.MenuView;

public class GameController {

    private Game game;
    private AI ai;
    private AI hintAI;
    private boolean hintLoaded;
    private GameView gameView;
    private DialogView dialogView;
    private final Level level;
    private boolean aiMakeMove;
    private Bool blockedGame = new Bool();

    private FigureSet figureSet;
    private final TextureAtlas.AtlasRegion[] figureImages = new TextureAtlas.AtlasRegion[17];
    private final FigureView[] figureViews = new FigureView[32];

    public GameController(Level level) {
        this.level = level;
        resetGame();
    }

    public GameView getGameView() {
        return gameView;
    }
    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    public void setDialogView(DialogView dialogView) {
        this.dialogView = dialogView;
    }

    public DialogView getDialogView() {
        return dialogView;
    }

    public FigureView[] getFigureViews() {
        return figureViews;
    }

    public Level getLevel() {
        return level;
    }

    public boolean isAIMakeMove() {
        return aiMakeMove;
    }

    public int getTurn() {
        return game.getTurn();
    }

    public Color getColorMove() {
        return game.getColorMove();
    }

    public boolean isBlockedGame() {
        return blockedGame.get();
    }

    public void blockGame() {
        blockedGame.addPositive();
    }

    public void unblockGame() {
        blockedGame.addNegative();
    }

    public Color getColor(FigureView figureView) {
        return game.getColor(figureView.getX(), figureView.getY());
    }

    public Array<Move> getMoves(int x, int y) {
        return game.getMoves(x, y);
    }

    /**
     * Intent
     * */
    public void goToMenu(GameView gameView, boolean saveGame) {
        if (ai != null) ai.interrupt();

        if (saveGame) updateSavedGame();
        else clearSavedGame();

        MenuView mainView = new MenuView(new MenuController());
        Settings.gdxGame.setScreen(mainView);
        gameView.dispose();
    }

    /**
     * Game methods
     **/
    public void createNewGame() {
        if (ai != null) ai.interrupt();

        gameView.setDrawableHintMoves(true);
        gameView.setDrawableHintCastle(true);
        clearGameView();
        blockedGame = new Bool(false);

        GameController gameController = new GameController(level);
        gameController.setGameView(gameView);
        gameView.initGraphics(gameController);
    }

    public void makeMove(Move move) {
        gameView.setDrawableHintMoves(true);
        gameView.setDrawableHintCastle(true);
        gameView.clearBlueHint();

        if (isUpdated(move)) {
            if (ai != null && ai.getAIColor() == game.getColorMove()) {
                updatePawn(move, BoardMatrix.QUEEN);
            } else {
                dialogView.showChoiceFigureDialog(move);
            }
            return;
        }

        boolean castleMove = game.isCastleMove(move);
        if (castleMove) {
            addOnFinishSound(move.getFigureX(), move.getFigureY(), Settings.SOUNDS::playCastle);
        }

        game.makeMove(move);
        updateFigureViews(true, true, !castleMove);

        if (isFinish()) {
            dialogView.showFinishDialog(defineResult());
            blockedGame.addPositive();
            aiMakeMove = false;
            return;
        }

        if (ai != null && ai.getAIColor() == game.getColorMove()) {
            makeMoveAI();
        }
    }

    public void showHint() {
        if (hintLoaded || isBlockedGame() || gameView.getBlueHint() != null) return;
        hintAI = defineAI(Level.MIDDLE, game.getColorMove());
        hintLoaded = true;

        hintAI.getMove(game, moveAI -> Gdx.app.postRunnable(() -> {
            gameView.setDrawableHintMoves(false);
            gameView.setDrawableHintCastle(false);

            gameView.clearMoves();
            gameView.drawChosenFigure(getFigureView(moveAI.getFigureX(), moveAI.getFigureY()));
            gameView.getMoves().add(moveAI);
            gameView.drawBlueHint(moveAI);

            hintLoaded = false;
        }));
    }

    public void updatePawn(Move move, byte type) {
        game.makeMove(move);
        updateFigureViews();
        game.updatePawn(move.getMoveX(), move.getMoveY(), type);
        updateFigureViews();

        if (isFinish()) dialogView.showFinishDialog(defineResult());

        if (ai != null && ai.getAIColor() == game.getColorMove()) {
            makeMoveAI();
        }
    }

    public void addOnFinishSound(int x, int y, CallListener callBack) {
        figureViews[game.getId(x, y)].addOnFinishSound(callBack);
    }

    public void initViewFigures(FigureSet figureSet) {
        this.figureSet = figureSet;
        updateFlippedFigures();

        figureImages[5] = figureSet.getPawn(Color.WHITE);
        figureImages[4] = figureSet.getRook(Color.WHITE);
        figureImages[3] = figureSet.getKnight(Color.WHITE);
        figureImages[2] = figureSet.getBishop(Color.WHITE);
        figureImages[1] = figureSet.getQueen(Color.WHITE);
        figureImages[0] = figureSet.getKing(Color.WHITE);

        figureImages[11] = figureSet.getPawn(Color.BLACK);
        figureImages[12] = figureSet.getRook(Color.BLACK);
        figureImages[13] = figureSet.getKnight(Color.BLACK);
        figureImages[14] = figureSet.getBishop(Color.BLACK);
        figureImages[15] = figureSet.getQueen(Color.BLACK);
        figureImages[16] = figureSet.getKing(Color.BLACK);

        byte[][] matrix = game.getMatrix();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                byte figure = matrix[i][j];
                if (figure == BoardMatrix.CAGE) continue;

                FigureView figureView = new FigureView(j, i, game.getColor(j, i), figureImages[figure + 8]);
                figureViews[game.getId(j, i)] = figureView;
            }
        }

        for (int i = 0; i < figureViews.length; i++) {
            if (figureViews[i] == null) {
                figureViews[i] = new FigureView(false);
            }
        }
        updateFigureViews(true, false, true);
    }

    public boolean isCastleMove(Move move) {
        return game.isCastleMove(move);
    }

    public void updateSavedGame() {
        if (game != null) {
            Settings.account.saveGame(level, game);
        }
    }

    public void clearSavedGame() {
        Settings.account.removeSavedGame(level);
    }

    public void cancelMove() {
        if (blockedGame.get() && game.getMove() > 0) return;
        clearGameView();

        game.cancelMove();
        updateFigureViews(true, false, false);
    }

    public void cancelTurn() {
        if (blockedGame.get() || game.getMove() < 2) return;
        clearGameView();

        game.cancelMove();
        updateFigureViews(false, false, false);
        game.cancelMove();
        updateFigureViews(true, false, false);
    }

    public FigureView getFigureView(int x, int y) {
        int id = game.getId(x, y);
        if (id < 0 || id >= figureViews.length) return null;
        return figureViews[id];
    }

    public Image getResImage(int turnsForStar) {
        if (getTurn() < turnsForStar)
            return new Image(Settings.gdxGame.findRegion("star"));
        return new Image(Settings.gdxGame.findRegion("empty_star"));
    }

    public boolean isCheckKing(FigureView figureView) {
        FigureView checkKing = gameView.getCheckKing();
        return checkKing != null && figureView.getX() == checkKing.getX()
                && figureView.getY() == checkKing.getY();
    }

    public boolean isThereFigure(Move move) {
        return getFigureView(move.getMoveX(), move.getMoveY()) != null
                && getFigureView(move.getFigureX(), move.getFigureY()) != null;
    }

    public boolean isUpdated(Move move) {
        return game.isUpdated(move);
    }

    private void clearGameView() {
        gameView.unselectChosenFigure();
        gameView.clearMoves();
        gameView.clearBlueHint();
        gameView.clearGreenCross();
        gameView.clearCheckKing();
    }

    private void makeMoveAI() {
        blockGame();
        long start = System.currentTimeMillis();
        aiMakeMove = true;

        ai.getMove(game, moveAI -> {
            long end = System.currentTimeMillis();
            if ((end - start) < 2500) {
                try {
                    Thread.sleep((2500 - (end - start)));
                } catch (InterruptedException e) {
                    e.printStackTrace(System.out);
                    return;
                }
            }

            Gdx.app.postRunnable(() -> {
                FigureView figureView = getFigureView(moveAI.getFigureX(), moveAI.getFigureY());
                gameView.drawGreenCross(figureView);
                gameView.drawChosenFigure(figureView);

                makeMove(moveAI);
                unblockGame();

                aiMakeMove = false;
            });
        });
    }

    private void resetGame() {
        Game savedGame = Settings.account.getSavedGame(level);
        Color aiColor = Settings.account.getNewAIColor();

        if (savedGame != null) {
            savedGame.updateUpperColor(aiColor);
            game = savedGame;
        } else {
            game = new Game(aiColor);
        }

        hintAI = defineAI(Level.MIDDLE, game.reverse(aiColor));
        if (level != Level.TWO_PLAYERS) {
            ai = defineAI(level, aiColor);
        }

        if (isFinish()) {
            blockedGame.addPositive();
            aiMakeMove = false;
            return;
        }

        if (ai != null && ai.getAIColor() == game.getColorMove()) {
            makeMoveAI();
        }
    }

    private void updateFigureViews() {
        updateFigureViews(true, true, true);
    }

    private AI defineAI(Level level, Color aiColor) {
        if (level == Level.NOVICE) {
            return new AI(1, Settings.account.getAIPerformanceMode(), aiColor);
        } else if (level == Level.EASY) {
            return new AI(2, Settings.account.getAIPerformanceMode(), aiColor);
        } else if (level == Level.MIDDLE) {
            return new AI(2, Settings.account.getAIPerformanceMode(), aiColor);
        } else if (level == Level.HARD) {
            return new AI(3, Settings.account.getAIPerformanceMode(), aiColor);
        } else if (level == Level.MASTER) {
            return new AI(3, Settings.account.getAIPerformanceMode(), aiColor);
        }
        throw new IllegalArgumentException();
    }

    private void updateFigureViews(boolean effect, boolean checkSound, boolean soundMove) {
        CallListener callback = null;
        if (soundMove) callback = Settings.SOUNDS::playMove;

        byte[][] matrix = game.getMatrix();
        Array<Byte> usedId = new Array<>();

        gameView.clearCheckKing();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (game.isCage(j, i)) continue;

                byte id = game.getId(j, i);
                FigureView figureView = figureViews[id];
                usedId.add(id);

                if (game.isCheckKing(j, i)) {
                    gameView.drawCheckKing(figureView);
                    if (checkSound) Settings.SOUNDS.playCheck();
                }

                figureView.setImage(figureImages[matrix[i][j] + 8]);
                if (!figureView.isVisible()) figureView.setVisible(true);

                if ((figureView.getX() != j || i != figureView.getY())) {
                    if (callback != null) figureView.addOnFinishSound(callback);
                    callback = null;

                    if (effect) {
                        figureView.makeMove(j, i);
                    } else {
                        figureView.setPosition(j, i);
                    }
                }
            }
        }

        for (int i = 0; i < figureViews.length; i++) {
            if (!usedId.contains((byte) i, false)) {
                figureViews[i].setVisible(false);
            }
        }
    }

    private void updateFlippedFigures() {
        if (Settings.account.isReversedTwoPlayers() && level == Level.TWO_PLAYERS) {
            figureSet.updateFlippedUpper(true, game.getUpper());
        } else {
            figureSet.updateFlippedUpper(false, Color.BLACK);
            figureSet.updateFlippedUpper(false, Color.WHITE);
        }
    }

    private Result defineResult() {
        Color color = game.getColorCheckKing();

        if (level == Level.TWO_PLAYERS) {
            if (color == Color.BLACK) {
                return Result.BLACK_VICTORY;
            } else {
                return Result.WHITE_VICTORY;
            }
        } else {
            if (color == game.getUpper()) {
                return Result.VICTORY;
            } else if (color == game.getLower()) {
                return Result.LOSE;
            }
        }
        return Result.DRAW;
    }

    private boolean isFinish() {
        return game.isFinish();
    }
}
