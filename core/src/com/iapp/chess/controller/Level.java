package com.iapp.chess.controller;

import com.iapp.chess.util.Text;

public enum Level {
    TWO_PLAYERS(Text.TWO_PLAYERS_LEVEL_CAPS, 0),
    NOVICE(Text.NOVICE_LEVEL_CAPS, 25),
    EASY(Text.EASY_LEVEL_CAPS, 50),
    MIDDLE(Text.MIDDLE_LEVEL_CAPS, 100),
    HARD(Text.HARD_LEVEL_CAPS, 150),
    MASTER(Text.MASTER_LEVEL_CAPS, 250);

    private final String level;
    private final int coins;

    Level(String level, int coins) {
        this.level = level;
        this.coins = coins;
    }

    public int getCoins() {
        return coins;
    }

    public String getText() {
        return level;
    }
}
