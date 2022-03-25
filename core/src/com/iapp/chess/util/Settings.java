package com.iapp.chess.util;

import com.iapp.chess.GdxGame;
import com.iapp.chess.Launcher;
import com.iapp.chess.controller.Account;
import com.iapp.chess.controller.Controller;

public class Settings {

    public static final Font FONT = Font.getInstance();
    public static final Sounds SOUNDS = Sounds.getInstance();
    public static final Logger LOGGER = Logger.getInstance();
    public static final Assets ASSETS = Assets.getInstance();
    public static final SaveData DATA = new SaveData();
    public static final Object MUTEX = new Object();

    public static GdxGame gdxGame;
    public static Controller controller;
    public static Account account;
    public static Orientation orientation;
    public static Launcher launcher;
}
