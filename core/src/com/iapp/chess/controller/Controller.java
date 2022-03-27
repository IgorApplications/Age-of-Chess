package com.iapp.chess.controller;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.iapp.chess.model.*;
import com.iapp.chess.model.ai.*;
import com.iapp.chess.util.Bool;
import com.iapp.chess.util.Settings;
import com.iapp.chess.util.Text;
import com.iapp.chess.view.FigureView;
import com.iapp.chess.view.GameView;

import java.util.*;

public class Controller {

    private Game game;
    private GameView gameView;
    private Level level;
    private final Bool blockedGame = new Bool();

    private Color aiColor;
    private Color userColor;
    private AI promptingAI;
    private Move promotingMove;

    /**
     * function for get value of field {@link Controller#gameView}
     * @return current class that renders game graphics
     * */
    public GameView getGameView() {
        return gameView;
    }

    /**
     * procedure that defines class that will render the game gui {@link Controller#gameView}
     * @param gameView - game gui
     */
    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    /**
     * function for get value of field {@link Controller#level}
     * @return level at which the user is playing {@see Level}
     * The level is directly related to the difficulty of the AI {@see AI}
     * */
    public Level getLevel() {
        return level;
    }

    /**
     * procedure that defines level at which the user is playing {@see Level}
     * @param level - complexity AI {@see AI}
     * */
    public void setLevel(Level level) {
        this.level = level;
    }

    /**
     * function for get turn of game {@link Controller#game}
     * @return integer turn. One turn is moves of black and white players together {@link Game#getTurn()}
     * */
    public long getTurn() {
        if (game == null) return -1;
        return game.getTurn();
    }

    public Color getAIColor() {
        return aiColor;
    }

    public Color getUserColor() {
        return userColor;
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

    /**
     * function for get game figure of game {@link Controller#game}
     * @return list of game figures from the backend game matrix {@link Game#getGameFigures()}
     * The state of these figures cannot be change since their setters are closed to package levels {@see FigureView}
     * */
    public List<Figure> getGameFigures() {
        return game.getGameFigures();
    }

    /**
     * function to get the color move from the game{@link Controller#game}
     * @return current color of figures that can make moves {@link Game#getColorMove()}
     */
    public Color defineColorMove() {
        return game.getColorMove();
    }

    /**
     * function to get figure from the game {@link Controller#game}
     * @param x - position figure
     * @param y - position figure
     * @return found figure in the backend game at the specified position {@link Game#getFigure(int, int)}
     * */
    public Figure getFigure(int x, int y) {
        return game.getFigure(x, y);
    }

    /**
     * function to get list moves from the game {@link Controller#game}
     * @param x - position figure
     * @param y - position figure
     * @return possible moves of the figure located
     * in this position and in accordance with the rules of the game
     * */
    public List<Move> getMoves(int x, int y) {
        return game.getMoves(x, y);
    }

    /**
     * This procedure deletes the saved game and creates a new game {@link Controller#game}
     * @param level will determine at what level the saved game will be deleted
     * and what level of difficulty will the game have {@see Level}
     * */
    public void createNewGame(Level level) {
        removeSavedGame();
        resetGame(level);
    }

    /**
     * This procedure tries to find a saved game, if it is not found, it creates a new game
     * @param level is the save game search key, also is the difficulty level of
     * the new game if it is found
     * */
    public void resetGame(Level level) {
        this.level = level;
        Game savedGame = Settings.account.getSavedGame(gameView, level);

        if (savedGame != null) {
            if (savedGame.getAI() != null) {
                aiColor = savedGame.getAIColor();
                userColor = reverseColor(aiColor);
            }
            game = savedGame;
        } else {
            aiColor = Settings.account.getNewAIColor();
            userColor = reverseColor(aiColor);
            game = new Game(defineAI(level, aiColor));
        }
        promptingAI = new MiddleAI(userColor);
        promptingAI.setGame(game);

        if (game.getColorMove() == aiColor) {
            moveAI();
        }
    }

    /**
     * This procedure save current game in the player's account.
     * */
    public void saveGame() {
        if (aiThread != null) {
            aiThread.interrupt();
        }
        synchronized (Settings.MUTEX) {
            Settings.account.saveGame(gameView, level, game);
        }
    }

    /**
     * This procedure deletes the saved game in the player's account
     * at the current level game {@link Controller#level}
     * */
    public void removeSavedGame() {
        if (aiThread != null) {
            aiThread.interrupt();
        }
        synchronized (Settings.MUTEX) {
            Settings.account.removeSavedGame(level);
        }
    }

    /**
     * This procedure performs a castle in the gui of the current game,
     * namely, it moves the rook sprite to the right place
     * @param kingMove is the move of the king that leads to the castle
     * */
    public void castle(Move kingMove) {
        if (kingMove.getX() == 6 && kingMove.getY() == 7) {
            FigureView figureView = gameView.findFigure(7, 7);
            figureView.castle(kingMove, kingMove.getX() - 1);
        } else if (kingMove.getX() == 2 && kingMove.getY() == 7) {
            FigureView figureView = gameView.findFigure(0, 7);
            figureView.castle(kingMove, kingMove.getX() + 1);
        } else if (kingMove.getX() == 6 && kingMove.getY() == 0) {
            FigureView figureView = gameView.findFigure(7, 0);
            figureView.castle(kingMove, kingMove.getX() - 1);
        } else if (kingMove.getX() == 2 && kingMove.getY() == 0) {
            FigureView figureView = gameView.findFigure(0, 0);
            figureView.castle(kingMove, kingMove.getX() + 1);
        }
        Settings.SOUNDS.playCastle();
    }

    /**
     * The user has already made a move and a figure is located in the pawn position.
     * */
    public void updatePawn(int pawnX, int pawnY, Color pawnColor, Class<? extends Figure> updateType) {
        Figure updateFigure;
        if (updateType == Queen.class) updateFigure = new Queen(game, pawnX, pawnY, pawnColor);
        else if (updateType == Rook.class) updateFigure = new Rook(game, pawnX, pawnY, pawnColor);
        else if (updateType == Bishop.class) updateFigure = new Bishop(game, pawnX, pawnY, pawnColor);
        else if (updateType == Knight.class) updateFigure = new Knight(game, pawnX, pawnY, pawnColor);
        else throw new RuntimeException("Iapp: Try update figure. It's new class figure or it's Pawn class!");

        game.updatePawn(pawnX, pawnY, updateFigure);
    }

    public boolean haveAIFirstMove() {
        return game.getMove() <= 3 && aiColor == Color.WHITE;
    }

    public King findCheckKing() {
        if (game.isCheckKing(Color.BLACK)) {
            return game.getKingForColor(Color.BLACK);
        } else if (game.isCheckKing(Color.WHITE)) {
            return game.getKingForColor(Color.WHITE);
        }
        return null;
    }

    public void drawCheckKingIfFound() {
        King checkKing = findCheckKing();
        if (checkKing != null) {
            gameView.drawCheckKing(gameView.findFigure(checkKing.getX(), checkKing.getY()));
            Settings.SOUNDS.playCheck();
        }
    }

    private Thread aiThread;

    public void moveAI() {
        if (game.getAI() == null || isFinishGame()) return;
        blockGame();

        long start = System.currentTimeMillis();
        aiThread = game.makeMoveAI(transit -> {
            long end = System.currentTimeMillis();
            if ((end - start) < 2200) {
                try {
                    Thread.sleep(2200 - (end - start));
                } catch (InterruptedException e) {
                    System.out.println("debug: thread AI was interrupted");
                }
            }

            try {
                if (Thread.currentThread().isInterrupted()) return;

                FigureView figureView = gameView.findFigure(transit.getFigureX(), transit.getFigureY());
                figureView.doMove(transit.getMove());
                gameView.drawGreenCross(figureView);
                gameView.cutDown(figureView, transit.getMove());
                gameView.drawChosenFigure(figureView);
                if (getFigure(transit.getMoveX(), transit.getMoveY()) instanceof Queen && (figureView.getY() == 0 || figureView.getY() == 7)) {
                    transformPawnOnQueen(figureView.getX(), figureView.getY());
                } else if (transit.getMove().isCastlingMove()) {
                    castle(transit.getMove());
                }
                unblockGame();
                gameView.clearCheckKing();
                drawCheckKingIfFound();
                showDialogIfFinish();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        });
    }

    public void moveUser(FigureView figureView, Move move) {
        gameView.clearBlueHint();
        Settings.account.setDrawableHintMoves(true);

        if (move.equals(promotingMove)) {
            gameView.clearBlueHint();
        } else {
            promotingMove = null;
        }

        if (level == Level.TWO_PLAYERS) {
            promptingAI.setAIColor(reverseColor(figureView.getColor()));
        }

        move(figureView.getX(), figureView.getY(), move);
        figureView.moveView(move.getX(), move.getY());

        if (move.isCastlingMove()) castle(move);
        gameView.cutDown(figureView, move);
        gameView.drawMoves(new ArrayList<>());
        gameView.clearCheckKing();
        drawCheckKingIfFound();
        showDialogIfFinish();
    }

    public void backMove() {
        if (isBlockedGame() || game.isEmptyTransitions() || isFinishGame()) return;

        gameView.clearBlueHint();
        Settings.account.setDrawableHintMoves(true);

        Settings.account.setDrawableGreenCross(false);
        Settings.account.setDrawableOutlineFigure(false);
        gameView.clearMoves();
        gameView.clearCheckKing();

        backMove(game.getColorMove());
        game.reverseColorMove();
        Settings.SOUNDS.playClick();

        King checkKing = findCheckKing();
        if (checkKing != null) gameView.drawCheckKing(gameView.findFigure(checkKing.getX(), checkKing.getY()));

        gameView.clearGreenCross();
        gameView.clearChosenFigure();

        Settings.account.setDrawableGreenCross(true);
        Settings.account.setDrawableOutlineFigure(true);
    }

    public void backTurn() {
        if (aiColor == Color.WHITE && game.getMove() == 1) return;
        if (isBlockedGame() || game.isEmptyTransitions() || isFinishGame()) return;

        gameView.clearBlueHint();
        Settings.account.setDrawableHintMoves(true);

        Settings.account.setDrawableGreenCross(false);
        Settings.account.setDrawableOutlineFigure(false);
        gameView.clearMoves();
        gameView.clearCheckKing();

        if (aiColor == Color.BLACK) {
            backMove(aiColor);
            backMove(userColor);
        } else {
            backMove(aiColor);
            backMove(userColor);
            if (game.getMove() == 1) {
                backMove(aiColor);
                if (haveAIFirstMove()) {
                    game.setColorMove(aiColor);
                    moveAI();
                }
            }
        }
        Settings.SOUNDS.playClick();

        King checkKing = findCheckKing();
        if (checkKing != null) gameView.drawCheckKing(gameView.findFigure(checkKing.getX(), checkKing.getY()));

        gameView.clearGreenCross();
        gameView.clearChosenFigure();
        Settings.account.setDrawableGreenCross(true);
        Settings.account.setDrawableOutlineFigure(true);
    }

    private void backMove(Color moveColor) {
        Transition lastTransit = game.backMove();
        if (lastTransit == null) return;

        FigureView figureView = gameView.findFigure(lastTransit.getMoveX(), lastTransit.getMoveY());
        figureView.setMakeSoundMove(false);
        if (level == Level.TWO_PLAYERS) figureView.doMove(new Move(lastTransit.getFigureX(), lastTransit.getFigureY()));
        else if (moveColor == aiColor) figureView.setPosition(lastTransit.getFigureX(), lastTransit.getFigureY());
        else figureView.doMove(new Move(lastTransit.getFigureX(), lastTransit.getFigureY()));

        Figure saveFigure = lastTransit.getFelledFigure();
        if (saveFigure != null && !(saveFigure instanceof Cage)) {
            FigureView saveFigureView = gameView.findInVisibleFigure(saveFigure);
            saveFigureView.setVisible(true);
        }

        if (lastTransit.isUpdatedPawnMove()) {
            Figure updatedPawn = lastTransit.getUpdatedPawn();
            gameView.setSprite(figureView, gameView.findFigure("black_pawn"), gameView.findFigure("white_pawn"));
            figureView.setType(updatedPawn);
        }

        if (lastTransit.isCastlingMove()) {
            FigureView rookView = gameView.findFigure(lastTransit.getRookX(), lastTransit.getRookY());
            if (level == Level.TWO_PLAYERS) rookView.doMove(new Move(lastTransit.getLastRookX(), lastTransit.getLastRookY()));
            else if (moveColor == aiColor) rookView.setPosition(lastTransit.getLastRookX(), lastTransit.getLastRookY());
            else rookView.doMove(new Move(lastTransit.getLastRookX(), lastTransit.getLastRookY()));
        }
    }

    public void showPromptingMove() {
        if (blockedGame.get() || isFinishGame()) return;

        Transition promptingTransit = promptingAI.getMove();
        if (promptingTransit == null) return;
        promotingMove = promptingTransit.getMove();

        gameView.clearMoves();
        Settings.account.setDrawableHintMoves(false);
        FigureView figureView = gameView.findFigure(promptingTransit.getFigureX(), promptingTransit.getFigureY());
        gameView.drawChosenFigure(figureView);
        gameView.drawBlueHint(promptingTransit.getMove());
    }

    public String defineUserLevelText(Level level) {
        if (level == Level.NOVICE) return Text.USER_NOVICE;
        else if (level == Level.EASY) return Text.USER_PLAYER;
        else if (level == Level.MIDDLE) return Text.USER_ADVANCED;
        else if (level == Level.HARD) return Text.USER_EXPERIENCED;
        else if (level == Level.MASTER) return Text.USER_MASTER;
        throw new RuntimeException("Iapp: A new Level has been added, but no such Text exists");
    }

    public String defineLevelText(Level level) {
        if (level == Level.NOVICE) return Text.NOVICE_LEVEL;
        else if (level == Level.EASY) return Text.EASY_LEVEL;
        else if (level == Level.TWO_PLAYERS) return Text.TWO_PLAYERS_LEVEL;
        else if (level == Level.MIDDLE) return Text.MIDDLE_LEVEL;
        else if (level == Level.HARD) return Text.HARD_LEVEL;
        else if (level == Level.MASTER) return Text.MASTER_LEVEL;
        throw new RuntimeException("Iapp: A new Level has been added, but no such Text exists");
    }

    public Image getRecordResImage(Level level, int turnsForStar) {
        if (Settings.account.getRecordTurnsOnLevel(level) < turnsForStar)
            return new Image(Settings.gdxGame.findRegion("star"));
        return new Image(Settings.gdxGame.findRegion("empty_star"));
    }

    public Image getResImage(int turnsForStar) {
        if (Settings.controller.getTurn() < turnsForStar)
            return new Image(Settings.gdxGame.findRegion("star"));
        return new Image(Settings.gdxGame.findRegion("empty_star"));
    }

    private void showDialogIfFinish() {
        if (isFinishGame()) {
            gameView.getDialogView().showFinishDialog(defineResultGame());
        }
    }

    private boolean isFinishGame() {
        Figure gameCheckKing = null;

        if (game.isCheckKing(Color.BLACK)) gameCheckKing = game.getKingForColor(Color.BLACK);
        else if (game.isCheckKing(Color.WHITE)) gameCheckKing = game.getKingForColor(Color.WHITE);

        if (gameCheckKing != null && game.isFiguresHaveNotMoves()) {
            return true;
        }

        return game.isFiguresHaveNotMoves() || getGameFigures().size() == 2;
    }

    private void move(int x, int y, Move move) {
        game.move(new Transition(game, x, y, move));
    }

    private AI defineAI(Level level, Color aiColor) {
        AI defineAI;

        if (level == Level.NOVICE) defineAI = new NoviceAI(aiColor);
        else if (level == Level.EASY) defineAI = new EasyAI(aiColor);
        else if (level == Level.MIDDLE) defineAI = new MiddleAI(aiColor);
        else if (level == Level.HARD) defineAI = new HardAI(aiColor);
        else if (level == Level.MASTER) defineAI = new MasterAI(aiColor);
        else if (level == Level.TWO_PLAYERS) defineAI = null;
        else throw new RuntimeException("Iapp: A new Level has been added, but this AI does bot exist!");

        return defineAI;
    }

    private Result defineResultGame() {
        King checkKing = findCheckKing();
        if (checkKing == null) return Result.DRAW;
        Color winner = checkKing.getColor() ==  Color.BLACK ? Color.WHITE : Color.BLACK;

        if (getLevel() != Level.TWO_PLAYERS) {
            if (aiColor == Color.BLACK && winner == Color.BLACK) return Result.LOSE;
            else return Result.VICTORY;
        } else if (winner == Color.BLACK) {
            return Result.BLACK_VICTORY;
        } else {
            return Result.WHITE_VICTORY;
        }
    }

    private void transformPawnOnQueen(int x, int y) {
        FigureView figureView = gameView.findFigure(x, y);
        figureView.setSprite(gameView.findFigure("black_queen"));
    }

    private Color reverseColor(Color color) {
        return color == Color.BLACK ? Color.WHITE : Color.BLACK;
    }
}
