package com.iapp.chess.controller;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.iapp.chess.util.Settings;
import com.iapp.chess.util.Text;
import com.iapp.chess.view.GameView;

public class MenuController {

    public MenuController() {}

    public void goToGame(Screen lastScreen, Stage lastStage) {
        GameController gameController = new GameController(Settings.account.getChoiceLevel());

        GameView gameView = new GameView();
        gameController.setGameView(gameView);
        gameView.initGUI(gameController);

        Settings.gdxGame.goToScreen(lastStage, Actions.run(() -> {
            Settings.gdxGame.setScreen(gameView);
            lastScreen.dispose();
        }), 0.20f);
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
}
