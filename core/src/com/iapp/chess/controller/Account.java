package com.iapp.chess.controller;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iapp.chess.model.*;
import com.iapp.chess.model.ai.AI;
import com.iapp.chess.util.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Account {

    private static final Random random = new Random();

    private long coins = 100;
    private boolean saveWindowSize = true;
    private Pair<Integer, Integer> windowSize;

    private Orientation.Type orientation;
    private Level userLevel = Level.NOVICE;
    private Level choiceLevel = Level.NOVICE;

    private final Map<Level, Game> savedGames = new HashMap<>();
    private Map<Level, Integer> turnsByLevel = new HashMap<>();
    private Map<FigureSet.FigureSetType, Pair<Boolean, Boolean>> figureSets = new HashMap<>();

    private AI.ThreadsMode threadsMode = AI.ThreadsMode.HALF_THREADS;
    private AIColorMode aiColorMode = AIColorMode.BLACK;
    private boolean reversedTwoPlayers = true;

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

    public Account() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            orientation = Orientation.Type.HORIZONTAL;

            int value = Gdx.graphics.getDisplayMode().height - 160;
            int key = (int) Math.round(value * 1.8);

            windowSize = new Pair<>(key, value);
        } else {
            orientation = Orientation.Type.VERTICAL;

            int key = Gdx.graphics.getDisplayMode().width - 70;
            int value = (int) Math.round(key * 1.8);

            windowSize = new Pair<>(key, value);
        }

        figureSets.put(FigureSet.FigureSetType.ISOMETRIC, new Pair<>(false, false));
        figureSets.put(FigureSet.FigureSetType.ROYAL, new Pair<>(false, false));
        figureSets.put(FigureSet.FigureSetType.STANDARD, new Pair<>(true, true));
    }

    /**
     * Buying methods
     * */
    public void buyIsometricFigures() {
        figureSets.get(FigureSet.FigureSetType.ISOMETRIC).setKey(true);
        Settings.account.subtractCoins(1000);
    }

    public void buyRoyalFigures() {
        figureSets.get(FigureSet.FigureSetType.ROYAL).setKey(true);
        Settings.account.subtractCoins(1000);
    }

    public boolean isAvailableIsometricFigures() {
        return figureSets.get(FigureSet.FigureSetType.ISOMETRIC).getKey();
    }

    public boolean isAvailableRoyalFigures() {
        return figureSets.get(FigureSet.FigureSetType.ROYAL).getKey();
    }

    public void setChosenFigureSet(FigureSet.FigureSetType figureSet) {
        figureSets.values().forEach(pair -> pair.setValue(false));
        figureSets.get(figureSet).setValue(true);
    }

    public FigureSet.FigureSetType getChosenFigureSet() {
        Optional<FigureSet.FigureSetType> figureSet = figureSets.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getValue())
                .map(Map.Entry::getKey)
                .findAny();

        if (!figureSet.isPresent()) throw new IllegalArgumentException();
        return figureSet.get();
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

    public void setReversedTwoPlayers(boolean reversedTwoPlayers) {
        this.reversedTwoPlayers = reversedTwoPlayers;
    }

    public boolean isReversedTwoPlayers() {
        return reversedTwoPlayers;
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
        this.orientation = type;
    }

    public Orientation.Type getOrientation() {
        return orientation;
    }

    public List<Level> availableLevels() {
        return Stream.of(Level.values())
                .filter(level -> level.ordinal() <= userLevel.ordinal())
                .collect(Collectors.toList());
    }

    public void nextUserLevel() {
        if ((Level.values().length - 1) > userLevel.ordinal())
            userLevel = Level.values()[userLevel.ordinal() + 1];
    }

    public void nextChoiceLevel() {
        if ((Level.values().length - 1) > choiceLevel.ordinal())
            choiceLevel = Level.values()[choiceLevel.ordinal() + 1];
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

    public void saveGame(Level level, Game game) {
        savedGames.put(level, game);
    }

    public Game getSavedGame(Level level) {
        return savedGames.get(level);
    }

    public void removeSavedGame(Level level) {
        savedGames.put(level, null);
    }

    public enum AIColorMode {
        BLACK,
        WHITE,
        RANDOM
    }

    public static Account fromJson(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        Account account = new Account();

        if (jsonObject.has("windowSize")) {
            JsonArray sizeProperty = jsonObject.get("windowSize").getAsJsonArray();
            account.windowSize = new Pair<>(sizeProperty.get(0).getAsInt(), sizeProperty.get(1).getAsInt());
        }

        if (jsonObject.has("turnsByLevel")) {
            Map<Level, Integer> newTurnsByLevel = new HashMap<>();
            JsonArray levelProperty = jsonObject.get("turnsByLevel").getAsJsonArray();
            for (int i = 0; i < levelProperty.size(); i++) {
                newTurnsByLevel.put(
                        Level.valueOf(levelProperty.get(i).getAsJsonArray().get(0).getAsString()),
                        levelProperty.get(i).getAsJsonArray().get(1).getAsInt());
            }
            account.turnsByLevel = newTurnsByLevel;
        }


        if (jsonObject.has("figureSets")) {
            Map<FigureSet.FigureSetType, Pair<Boolean, Boolean>> newFigureSets = new HashMap<>();
            JsonArray figureProperty = jsonObject.get("figureSets").getAsJsonArray();
            for (int i = 0; i < figureProperty.size(); i++) {
                newFigureSets.put(
                        FigureSet.FigureSetType.valueOf(figureProperty.get(i).getAsJsonArray().get(0).getAsString()),
                        new Pair<>(figureProperty.get(i).getAsJsonArray().get(1).getAsJsonArray().get(0).getAsBoolean(),
                                figureProperty.get(i).getAsJsonArray().get(1).getAsJsonArray().get(1).getAsBoolean())
                );
            }
            account.figureSets = newFigureSets;
        }

        if (jsonObject.has("coins"))
            account.coins = jsonObject.get("coins").getAsInt();
        if (jsonObject.has("saveWindowSize"))
            account.saveWindowSize = jsonObject.get("saveWindowSize").getAsBoolean();
        if (jsonObject.has("orientation"))
            account.orientation = Orientation.Type.valueOf(jsonObject.get("orientation").getAsString());
        if (jsonObject.has("userLevel"))
            account.userLevel = Level.valueOf(jsonObject.get("userLevel").getAsString());
        if (jsonObject.has("choiceLevel"))
            account.choiceLevel = Level.valueOf(jsonObject.get("choiceLevel").getAsString());
        if (jsonObject.has("threadsMode"))
            account.threadsMode = AI.ThreadsMode.valueOf(jsonObject.get("threadsMode").getAsString());
        if (jsonObject.has("aiColorMove"))
            account.aiColorMode = AIColorMode.valueOf(jsonObject.get("aiColorMove").getAsString());
        if (jsonObject.has("reversedTwoPlayers"))
            account.reversedTwoPlayers = jsonObject.get("reversedTwoPlayers").getAsBoolean();

        if (jsonObject.has("blockedOutlineFigure"))
            account.blockedOutlineFigure = jsonObject.get("blockedOutlineFigure").getAsBoolean();
        if (jsonObject.has("blockedOutlineFelledFigure"))
            account.blockedOutlineFelledFigure = jsonObject.get("blockedOutlineFelledFigure").getAsBoolean();
        if (jsonObject.has("blockedHintMoves"))
            account.blockedHintMoves = jsonObject.get("blockedHintMoves").getAsBoolean();
        if (jsonObject.has("blockedHintCastle"))
            account.blockedHintCastle = jsonObject.get("blockedHintCastle").getAsBoolean();
        if (jsonObject.has("blockedHintCheck"))
            account.blockedHintCheck = jsonObject.get("blockedHintCheck").getAsBoolean();
        if (jsonObject.has("blockedGreenCross"))
            account.blockedGreenCross = jsonObject.get("blockedGreenCross").getAsBoolean();
        if (jsonObject.has("blockedSoundClick"))
            account.blockedSoundClick = jsonObject.get("blockedSoundClick").getAsBoolean();
        if (jsonObject.has("blockedSoundMove"))
            account.blockedSoundMove = jsonObject.get("blockedSoundMove").getAsBoolean();
        if (jsonObject.has("blockedSoundCastle"))
            account.blockedSoundCastle = jsonObject.get("blockedSoundCastle").getAsBoolean();
        if (jsonObject.has("blockedSoundCheck"))
            account.blockedSoundCheck = jsonObject.get("blockedSoundCheck").getAsBoolean();
        if (jsonObject.has("blockedSoundWin"))
            account.blockedSoundWin = jsonObject.get("blockedSoundWin").getAsBoolean();
        if (jsonObject.has("blockedSoundWinMaster"))
            account.blockedSoundWinMaster = jsonObject.get("blockedSoundWinMaster").getAsBoolean();
        if (jsonObject.has("blockedSoundLose"))
            account.blockedSoundLose = jsonObject.get("blockedSoundLose").getAsBoolean();
        if (jsonObject.has("showFPS"))
            account.showFPS = jsonObject.get("showFPS").getAsBoolean();
        if (jsonObject.has("showRAM"))
            account.showRAM = jsonObject.get("showRAM").getAsBoolean();

        return account;
    }

    public String parseJson() {
        JsonArray sizeProperty = new JsonArray();
        sizeProperty.add(windowSize.getKey());
        sizeProperty.add(windowSize.getValue());

        JsonArray levelProperty = new JsonArray();
        turnsByLevel.keySet().stream()
                .map(level -> {
                    JsonArray arr = new JsonArray();
                    arr.add(level.toString());
                    arr.add(turnsByLevel.get(level));
                    return arr;
                })
                .forEach(levelProperty::add);

        JsonArray figureProperty = new JsonArray();
        figureSets.keySet().stream()
                .map(figureSet -> {
                    Pair<Boolean, Boolean> pair = figureSets.get(figureSet);
                    JsonArray pairArr = new JsonArray();
                    pairArr.add(pair.getKey());
                    pairArr.add(pair.getValue());

                    JsonArray arr = new JsonArray();
                    arr.add(figureSet.toString());
                    arr.add(pairArr);
                    return arr;
                })
                .forEach(figureProperty::add);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("coins", String.valueOf(coins));
        jsonObject.addProperty("saveWindowSize", String.valueOf(saveWindowSize));
        jsonObject.add("windowSize", sizeProperty);
        jsonObject.addProperty("orientation", orientation.toString());
        jsonObject.addProperty("userLevel", userLevel.toString());
        jsonObject.addProperty("choiceLevel", choiceLevel.toString());
        jsonObject.add("turnsByLevel", levelProperty);
        jsonObject.add("figureSets", figureProperty);
        jsonObject.addProperty("threadsMode", threadsMode.toString());
        jsonObject.addProperty("aiColorMove", aiColorMode.toString());
        jsonObject.addProperty("reversedTwoPlayers", reversedTwoPlayers);

        jsonObject.addProperty("blockedOutlineFigure", blockedOutlineFigure);
        jsonObject.addProperty("blockedOutlineFelledFigure", blockedOutlineFelledFigure);
        jsonObject.addProperty("blockedHintMoves", blockedHintMoves);
        jsonObject.addProperty("blockedHintCastle", blockedHintCastle);
        jsonObject.addProperty("blockedHintCheck", blockedHintCheck);
        jsonObject.addProperty("blockedGreenCross", blockedGreenCross);
        jsonObject.addProperty("blockedSoundClick", blockedSoundClick);
        jsonObject.addProperty("blockedSoundMove", blockedSoundMove);
        jsonObject.addProperty("blockedSoundCastle", blockedSoundCastle);
        jsonObject.addProperty("blockedSoundCheck", blockedSoundCheck);
        jsonObject.addProperty("blockedSoundWin", blockedSoundWin);
        jsonObject.addProperty("blockedSoundWinMaster", blockedSoundWinMaster);
        jsonObject.addProperty("blockedSoundLose", blockedSoundLose);
        jsonObject.addProperty("showFPS", showFPS);
        jsonObject.addProperty("showRAM", showRAM);

        return jsonObject.toString();
    }
}
