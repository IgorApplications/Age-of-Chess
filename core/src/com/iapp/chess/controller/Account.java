package com.iapp.chess.controller;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.iapp.chess.model.*;
import com.iapp.chess.util.*;
import com.iapp.chess.view.FigureView;
import com.iapp.chess.view.GameView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class Account implements Serializable {

    private long coins = 100;
    private boolean saveWindowSize = true;
    private Pair<Integer, Integer> windowSize;

    private Orientation.Type type;
    private Level userLevel = Level.NOVICE;
    private Level choiceLevel = Level.NOVICE;

    private Level[] allLevels = Level.values();
    private Map<Level, Game> savedGames = new HashMap<>();
    private Map<Level, List<FigureView>> invisibleFigureViews = new HashMap<>();
    private Map<Level, Integer> turnsByLevel = new HashMap<>();
    private Map<FigureSet, Pair<Boolean, Boolean>> figureSets = new HashMap<>();

    private AIColorMode aiColorMode = AIColorMode.BLACK;

    private volatile transient boolean drawableOutlineFigure = true;
    private volatile transient boolean drawableOutlineFelledFigure = true;
    private volatile transient boolean drawableHintMoves = true;
    private volatile transient boolean drawableHintCastle = true;
    private volatile transient boolean drawableHintCheck = true;
    private volatile transient boolean drawableGreenCross = true;

    private volatile boolean blockedOutlineFigure;
    private volatile boolean blockedOutlineFelledFigure;
    private volatile boolean blockedHintMoves;
    private volatile boolean blockedHintCastle;
    private volatile boolean blockedHintCheck;
    private volatile boolean blockedGreenCross;

    private volatile boolean blockedSoundClick;
    private volatile boolean blockedSoundMove;
    private volatile boolean blockedSoundCastle;
    private volatile boolean blockedSoundCheck;
    private volatile boolean blockedSoundWin;
    private volatile boolean blockedSoundWinMaster;
    private volatile boolean blockedSoundLose;

    private transient Random random = new Random();

    public Account() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            type = Orientation.Type.HORIZONTAL;
            windowSize = new Pair<>(1300, 722);
        } else {
            type = Orientation.Type.VERTICAL;
            windowSize = new Pair<>(722, 1300);
        }

        figureSets.put(FigureSet.STANDARD, new Pair<>(true, true));
        figureSets.put(FigureSet.MODE, new Pair<>(false, false));
    }

    /**
     * Buying methods
     * */

    public void buyModeFigures() {
        figureSets.get(FigureSet.MODE).setKey(true);
        Settings.account.subtractCoins(1000);
    }

    public boolean isAvailableModeFigures() {
        return figureSets.get(FigureSet.MODE).getKey();
    }

    public void setChosenFigureSet(FigureSet figureSet) {
        for (Pair<Boolean, Boolean> pair : figureSets.values()) {
            pair.setValue(false);
        }
        figureSets.get(figureSet).setValue(true);
    }

    public FigureSet getChosenFigureSet() {
        for (Map.Entry<FigureSet, Pair<Boolean, Boolean>> entry : figureSets.entrySet()) {
            if (entry.getValue().getValue()) return entry.getKey();
        }
        throw new RuntimeException("Iapp: There is no selected set of figures!");
    }

    /**
     * Drawable methods
     * */
    public boolean isDrawableOutlineFigure() {
        return drawableOutlineFigure && !blockedOutlineFigure;
    }

    public void setDrawableOutlineFigure(boolean drawableOutlineFigure) {
        this.drawableOutlineFigure = drawableOutlineFigure;
    }

    public boolean isDrawableOutlineFelledFigure() {
        return drawableOutlineFelledFigure && !blockedOutlineFelledFigure;
    }

    public void setDrawableOutlineFelledFigure(boolean drawableOutlineFelledFigure) {
        this.drawableOutlineFelledFigure = drawableOutlineFelledFigure;
    }

    public boolean isDrawableHintMoves() {
        return drawableHintMoves && !blockedHintMoves;
    }

    public void setDrawableHintMoves(boolean drawableHintMoves) {
        this.drawableHintMoves = drawableHintMoves;
    }

    public boolean isDrawableHintCastle() {
        return drawableHintCastle && !blockedHintCastle;
    }

    public void setDrawableHintCastle(boolean drawableHintCastle) {
        this.drawableHintCastle = drawableHintCastle;
    }

    public boolean isDrawableHintCheck() {
        return drawableHintCheck && !blockedHintCheck;
    }

    public void setDrawableHintCheck(boolean drawableHintCheck) {
        this.drawableHintCheck = drawableHintCheck;
    }

    public boolean isDrawableGreenCross() {
        return drawableGreenCross && !blockedGreenCross;
    }

    public void setDrawableGreenCross(boolean drawableHintGreenCross) {
        this.drawableGreenCross = drawableHintGreenCross;
    }

    /**
     * Blocked graphics methods
     * */
    public void setBlockedOutlineFigure(boolean blockedOutlineFigure) {
        this.blockedOutlineFigure = blockedOutlineFigure;
    }

    public boolean isBlockedOutlineFigure() {
        return blockedOutlineFigure;
    }

    public void setBlockedOutlineFelledFigure(boolean blockedOutlineFelledFigure) {
        this.blockedOutlineFelledFigure = blockedOutlineFelledFigure;
    }

    public boolean isBlockedOutlineFelledFigure() {
        return blockedOutlineFelledFigure;
    }

    public void setBlockedHintMoves(boolean blockedHintMoves) {
        this.blockedHintMoves = blockedHintMoves;
    }

    public boolean isBlockedHintMoves() {
        return blockedHintMoves;
    }

    public void setBlockedHintCastle(boolean blockedHintCastle) {
        this.blockedHintCastle = blockedHintCastle;
    }

    public boolean isBlockedHintCastle() {
        return blockedHintCastle;
    }

    public void setBlockedHintCheck(boolean blockedHintCheck) {
        this.blockedHintCheck = blockedHintCheck;
    }

    public boolean isBlockedHintCheck() {
        return blockedHintCheck;
    }

    public void setBlockedGreenCross(boolean blockedGreenCross) {
        this.blockedGreenCross = blockedGreenCross;
    }

    public boolean isBlockedGreenCross() {
        return blockedGreenCross;
    }

    /**
     * Sound blocked methods
     * */
    public void setBlockedSoundClick(boolean blockedSoundClick) {
        this.blockedSoundClick = blockedSoundClick;
    }

    public boolean isBlockedSoundClick() {
        return blockedSoundClick;
    }

    public void setBlockedSoundMove(boolean blockedSoundMove) {
        this.blockedSoundMove = blockedSoundMove;
    }

    public boolean isBlockedSoundMove() {
        return blockedSoundMove;
    }

    public void setBlockedSoundCastle(boolean blockedSoundCastle) {
        this.blockedSoundCastle = blockedSoundCastle;
    }

    public boolean isBlockedSoundCastle() {
        return blockedSoundCastle;
    }

    public void setBlockedSoundCheck(boolean blockedSoundCheck) {
        this.blockedSoundCheck = blockedSoundCheck;
    }

    public boolean isBlockedSoundCheck() {
        return blockedSoundCheck;
    }

    public void setBlockedSoundWin(boolean blockedSoundWin) {
        this.blockedSoundWin = blockedSoundWin;
    }

    public boolean isBlockedSoundWin() {
        return blockedSoundWin;
    }

    public void setBlockedSoundWinMaster(boolean blockedSoundWinMaster) {
        this.blockedSoundWinMaster = blockedSoundWinMaster;
    }

    public boolean isBlockedSoundWinMaster() {
        return blockedSoundWinMaster;
    }

    public void setBlockedSoundLose(boolean blockedSoundLose) {
        this.blockedSoundLose = blockedSoundLose;
    }

    public boolean isBlockedSoundLose() {
        return blockedSoundLose;
    }

    /**
     * Account methods
     * */
    public void setSaveWindowSize(boolean saveWindowSize) {
        this.saveWindowSize = saveWindowSize;
    }

    public boolean isSaveWindowSize() {
        return saveWindowSize;
    }

    public void addCoins(long coins) {
        this.coins += coins;
    }

    public void subtractCoins(long coins) {
        this.coins -= coins;
    }
    public void addCoins(Level level) {
        coins += level.getCoins();
    }

    public void updateCoins(long coins) {
        this.coins = coins;
    }

    public long getCoins() {
        return coins;
    }

    public String getViewCoins() {
        if (coins >= 1_000_000_000) return 999 + "m";
        else if (coins >= 1_000_000) return coins/1_000_000 + "m";
        else if (coins >= 10_000) return coins/1_000 + "k";
        return String.valueOf(coins);
    }

    public Pair<Integer, Integer> getWindowSize() {
        if (!saveWindowSize)  windowSize = new Pair<>(1300, 722);

        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            int[] size = Settings.launcher.getDeviceScreen();

            if (windowSize.getKey() >= size[0] && windowSize.getValue() >= size[1]) {
                windowSize.setValue(windowSize.getValue() - 45);
                windowSize.setKey((int) Math.round(windowSize.getValue() * 1.8));
            }
        }

        return windowSize;
    }

    public void saveWindowSize(int width, int height) {
        if (!saveWindowSize) return;
        this.windowSize = new Pair<>(width, height);
    }

    public Level getChoiceLevel() { return choiceLevel; }

    public void setChoiceLevel(Level choiceLevel) {
        this.choiceLevel = choiceLevel;
    }

    public Level getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Level userLevel) {
        this.userLevel = userLevel;
    }

    public AIColorMode getAIColorMode() {
        return aiColorMode;
    }

    public void setAIColorMode(AIColorMode aiColorMode) {
        this.aiColorMode = aiColorMode;
    }

    public Color getNewAIColor() {
        if (aiColorMode == AIColorMode.BLACK) {
            return Color.BLACK;
        } else if (aiColorMode == AIColorMode.WHITE) {
            return Color.WHITE;
        } else {
            int randChoice = random.nextInt(2);
            if (randChoice == 0) return Color.BLACK;
            return Color.WHITE;
        }
    }

    public void updateOrientationType(Orientation.Type type) {
        this.type = type;
    }

    public Orientation.Type getOrientationType() {
        return type;
    }

    public List<Level> availableLevels() {
        List<Level> levels = new ArrayList<>();
        for (int i = 0; i <= userLevel.ordinal(); i++) levels.add(allLevels[i]);
        return levels;
    }

    public void nextUserLevel() {
        if ((allLevels.length - 1) > userLevel.ordinal())
            userLevel = allLevels[userLevel.ordinal() + 1];
    }

    public void nextChoiceLevel() {
        if ((allLevels.length - 1) > choiceLevel.ordinal())
            choiceLevel = allLevels[choiceLevel.ordinal() + 1];
    }

    public void updateTurnsByLevel(Level level, int turns) {
        turnsByLevel.put(level, turns);
    }

    public int getRecordTurnsOnLevel(Level level) {
        if (!turnsByLevel.containsKey(level)) {
            updateTurnsByLevel(level, 200);
            return 200;
        }
        return turnsByLevel.get(level);
    }

    void saveGame(GameView gameView, Level level, Game game) {
        savedGames.put(level, game);

        List<FigureView> invisibleFigures = new ArrayList<>();
        for (FigureView figureView : gameView.getFigureViews()) {
            if (!figureView.isVisible()) invisibleFigures.add(figureView);
        }
        invisibleFigureViews.put(level, invisibleFigures);
    }

    Game getSavedGame(GameView gameView, Level level) {
        Game savedGame = savedGames.get(level);
        if (savedGame == null) return null;

        deserializationInvisibleFigureViews(gameView, level);
        gameView.getFigureViews().addAll(invisibleFigureViews.get(level));

        return savedGame;
    }

    void removeSavedGame(Level level) {
        savedGames.put(level, null);
        List<FigureView> invisibleFigures = invisibleFigureViews.get(level);
        if (invisibleFigures != null) invisibleFigures.clear();
    }

    private void deserializationInvisibleFigureViews(GameView gameView, Level level) {
        for (FigureView figureView : invisibleFigureViews.get(level)) {
            if (figureView.isVisible()) continue;

            figureView.setGameView(gameView);
            figureView.setBatch(gameView.getBatch());

            Figure type = figureView.getType();

            if (type instanceof Pawn) {
                gameView.setSprite(figureView, gameView.findFigure("black_pawn"), gameView.findFigure("white_pawn"));
            } else if (type instanceof Rook) {
                gameView.setSprite(figureView, gameView.findFigure("black_rook"), gameView.findFigure("white_rook"));
            } else if (type instanceof Knight) {
                gameView.setSprite(figureView, gameView.findFigure("black_knight"), gameView.findFigure("white_knight"));
            } else if (type instanceof Bishop) {
                gameView.setSprite(figureView, gameView.findFigure("black_bishop"), gameView.findFigure("white_bishop"));
            } else if (type instanceof Queen) {
                gameView.setSprite(figureView, gameView.findFigure("black_queen"), gameView.findFigure("white_queen"));
            } else {
                gameView.setSprite(figureView, gameView.findFigure("black_king"), gameView.findFigure("white_king"));
            }
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        random = new Random();

        drawableOutlineFigure = true;
        drawableOutlineFelledFigure = true;
        drawableHintMoves = true;
        drawableHintCastle = true;
        drawableHintCheck = true;
        drawableGreenCross = true;
    }

    public enum AIColorMode {
        BLACK,
        WHITE,
        RANDOM
    }
}
