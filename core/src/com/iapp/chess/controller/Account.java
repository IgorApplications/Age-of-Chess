package com.iapp.chess.controller;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.iapp.chess.model.*;
import com.iapp.chess.model.ai.AI;
import com.iapp.chess.util.*;

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

    private final Level[] allLevels = Level.values();
    private final Map<Level, Game> savedGames = new HashMap<>();
    private final Map<Level, Integer> turnsByLevel = new HashMap<>();
    private final Map<FigureSet, Pair<Boolean, Boolean>> figureSets = new HashMap<>();

    private AI.ThreadsMode threadsMode = AI.ThreadsMode.HALF_THREADS;
    private AIColorMode aiColorMode = AIColorMode.BLACK;

    private boolean blockedOutlineFigure;
    private boolean blockedOutlineFelledFigure;
    private boolean blockedHintMoves;
    private boolean blockedHintCastle;
    private boolean blockedHintCheck;
    private boolean blockedGreenCross;

    private boolean blockedSoundClick;
    private boolean blockedSoundMove;
    private boolean blockedSoundCastle;
    private boolean blockedSoundCheck;
    private boolean blockedSoundWin;
    private boolean blockedSoundWinMaster;
    private boolean blockedSoundLose;

    private boolean showFPS;
    private boolean showRAM;

    private transient Random random = new Random();

    public Account() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            type = Orientation.Type.HORIZONTAL;

            int value = Gdx.graphics.getDisplayMode().height - 160;
            int key = (int) Math.round(value * 1.8);

            windowSize = new Pair<>(key, value);
        } else {
            type = Orientation.Type.VERTICAL;

            int key = Gdx.graphics.getDisplayMode().width - 70;
            int value = (int) Math.round(key * 1.8);

            windowSize = new Pair<>(key, value);
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
     * Debug methods
     * */

    public void setShowFPS(boolean showFPS) {
        this.showFPS = showFPS;
    }

    public boolean isShowFPS() {
        return showFPS;
    }

    public void setShowRAM(boolean showRAM) {
        this.showRAM = showRAM;
    }

    public boolean isShowRAM() {
        return showRAM;
    }

    /**
     * Account methods
     * */

    public void setAIPerformanceMode(AI.ThreadsMode threadsMode) {
        this.threadsMode = threadsMode;
    }

    public AI.ThreadsMode getAIPerformanceMode() {
        return threadsMode;
    }

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
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            if (!saveWindowSize || (windowSize.getKey() >= Gdx.graphics.getDisplayMode().width &&
                    (windowSize.getValue() >= Gdx.graphics.getDisplayMode().height - 90))) {
                windowSize.setValue(windowSize.getValue() - 70);
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

    void saveGame(Level level, Game game) {
        savedGames.put(level, game);
    }

    Game getSavedGame(Level level) {
        Game savedGame = savedGames.get(level);
        if (savedGame == null) return null;

        return savedGame;
    }

    void removeSavedGame(Level level) {
        savedGames.put(level, null);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        random = new Random();
    }

    public enum AIColorMode {
        BLACK,
        WHITE,
        RANDOM
    }
}
