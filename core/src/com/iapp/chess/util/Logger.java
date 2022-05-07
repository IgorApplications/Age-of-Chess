package com.iapp.chess.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;

import java.io.*;
import java.util.Arrays;

public class Logger implements Disposable {

    private static final Logger INSTANCE = new Logger();

    private final OrthographicCamera loggerCamera;
    private final SpriteBatch loggerBatch;
    private final BitmapFont loggerFont;
    private final BitmapFont debugFont;

    private String textLog;
    private String fps = "";
    private String usedMB = "";

    private long fpsStartTime;
    private long ramStartTime;

    private final ByteArrayOutputStream stdOut = new ByteArrayOutputStream();
    private final ByteArrayOutputStream stdErr = new ByteArrayOutputStream();

    private Logger() {
        loggerCamera = new OrthographicCamera();
        loggerBatch = new SpriteBatch();

        loggerFont = Settings.FONT.createArial(18);
        debugFont = Settings.FONT.createArial(12);
        debugFont.setColor(Color.GREEN);

        fpsStartTime = TimeUtils.nanoTime();
        ramStartTime = TimeUtils.nanoTime();

        //TODO
        //System.setOut(new PrintStream(stdOut));
        //System.setErr(new PrintStream(stdErr));
    }

    public String getOutLogs() {
        return stdOut.toString();
    }

    public String getErrLogs() {
        return stdErr.toString();
    }

    public static Logger getInstance() {
        return INSTANCE;
    }

    public void log(Throwable t) {
        loggerCamera.setToOrtho(false, Orientation.cameraWidth, Orientation.cameraHeight);
        loggerBatch.setProjectionMatrix(loggerCamera.combined);

        textLog = t.toString() + Arrays.toString(t.getStackTrace());
        t.printStackTrace();
    }

    public boolean isEmpty() {
        return textLog == null;
    }

    public String getText() {
        return textLog;
    }

    public void render() {
        loggerCamera.update();
        loggerCamera.setToOrtho(false, Orientation.cameraWidth, Orientation.cameraHeight);
        loggerBatch.setProjectionMatrix(loggerCamera.combined);

        logFPS();
        logUsedMB();

        loggerBatch.begin();
        if (textLog != null) {
            loggerFont.draw(loggerBatch, textLog, 20.0f, Orientation.cameraHeight - 20.0f, Orientation.cameraWidth - 30.0f, Align.left, true);
        } else {
            if (Settings.account.isShowFPS()) {
                debugFont.draw(loggerBatch, fps, 11.0f, Orientation.cameraHeight - 2.5f, 9f, Align.left, true);
            }
            if (Settings.account.isShowRAM()) {
                debugFont.draw(loggerBatch, usedMB, 34.0f, Orientation.cameraHeight - 2.5f, 9f, Align.left, true);
            }
        }
        loggerBatch.end();
    }

    @Override
    public void dispose() {
        loggerBatch.dispose();
        loggerFont.dispose();

        try {
            stdOut.close();
            stdErr.close();
        } catch (IOException ignored) {}
    }

    private void logFPS() {
        final long nanoTime = TimeUtils.nanoTime();
        if (nanoTime - fpsStartTime > 1000000000) {
            fps = String.valueOf(Gdx.graphics.getFramesPerSecond());
            fpsStartTime = nanoTime;
        }
    }

    private void logUsedMB() {
        final long nanoTime = TimeUtils.nanoTime();
        if (nanoTime - ramStartTime > 1000000000) {
            long bytes  = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            usedMB = bytes / 1024 / 1024 + " Mb";
            ramStartTime = nanoTime;
        }
    }
}

