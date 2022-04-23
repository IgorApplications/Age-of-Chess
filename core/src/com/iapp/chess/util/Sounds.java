package com.iapp.chess.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

public class Sounds implements Disposable {

    private Sound click;
    private Sound move;
    private Sound castle;
    private Sound check;
    private Sound win;
    private Sound winMaster;
    private Sound lose;

    private static final Sounds sounds = new Sounds();

    private boolean playWinSound;

    public static Sounds getInstance() {
        return sounds;
    }

    private Sounds() {}

    public void initSounds() {
        click = Settings.ASSETS.get(Assets.CLICK_SOUND);
        move = Settings.ASSETS.get(Assets.MOVE_SOUND);
        castle = Settings.ASSETS.get(Assets.CASTLE_SOUND);
        check = Settings.ASSETS.get(Assets.CHECK_SOUND);
        win = Settings.ASSETS.get(Assets.WIN_SOUND);
        winMaster = Settings.ASSETS.get(Assets.WIN_MASTER_SOUND);
        lose = Settings.ASSETS.get(Assets.LOSE_SOUND);
    }

    public void playClick() {
        if (Settings.account.isBlockedSoundClick()) return;
        click.play(1.0f);
    }

    public void playMove() {
        if (Settings.account.isBlockedSoundMove()) return;
        move.play(1.0f);
    }

    public void playCastle() {
        if (Settings.account.isBlockedSoundCastle()) return;
        castle.play(1.0f);
    }

    public void playCheck() {
        if (Settings.account.isBlockedSoundCheck()) return;
        check.play(1.0f);
    }

    public void playWin() {
        if (Settings.account.isBlockedSoundWin()) return;
        playWinSound = true;
        win.play(1.0f);
    }

    public void stopWinSounds() {
        if (playWinSound) win.stop();
        else winMaster.stop();
    }

    public void playWinMaster() {
        if (Settings.account.isBlockedSoundWinMaster()) return;
        playWinSound = false;
        winMaster.play(1.0f);
    }

    public void playLose() {
        if (Settings.account.isBlockedSoundLose()) return;
        lose.play(1.0f);
    }

    public void stopLose() {
        lose.stop();
    }

    @Override
    public void dispose() {
        if (click != null) click.dispose();
        if (move != null) move.dispose();
        if (castle != null) castle.dispose();
        if (check != null) check.dispose();
    }
}
