package com.iapp.chess.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.iapp.chess.controller.Account;
import com.iapp.chess.controller.Level;
import com.iapp.chess.model.Game;

import java.io.*;

public class SaveData {

    private static final String ACCOUNT_DIRECTORY = "chess-data" + File.separator;
    private static final String GAMES_DIRECTORY = "chess-data" + File.separator + "games" + File.separator;
    private static final String NAME = "account.json";
    private final FileHandle accountHandler;

    public SaveData() {
        accountHandler = Gdx.files.external(ACCOUNT_DIRECTORY + NAME);
    }

    public void appendLogs() {
        writeLogos(Settings.LOGGER.getOutLogs(), Settings.LOGGER.getErrLogs(), true);
    }

    public void removeLogs() {
        writeLogos("", "", false);
    }

    public void saveAccount(Account account) {
        accountHandler.writeString(account.parseJson(), false);
        for (Level level : Level.values()) {
            FileHandle levelHandle = Gdx.files.external(GAMES_DIRECTORY + level.toString());
            levelHandle.writeBytes(serialize(account.getSavedGame(level)), false);
        }
    }

    public Account readAccount() {
        if (!accountHandler.exists()) return null;
        Account account = Account.fromJson(accountHandler.readString());
        for (Level level : Level.values()) {
            FileHandle levelHandle = Gdx.files.external(GAMES_DIRECTORY + level.toString());
            if (levelHandle.exists()) {
                account.saveGame(level, (Game) deserialize(levelHandle.readBytes()));
            }
        }
        return account;
    }

    private byte[] serialize(Object obj)  {
        try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            ObjectOutputStream byteWriter = new ObjectOutputStream(byteArray)) {
            byteWriter.writeObject(obj);
            return byteArray.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Object deserialize(byte[] bytes) {
        try (ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
            ObjectInputStream bytesReader = new ObjectInputStream(byteArray)) {
            return bytesReader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeLogos(String outText, String errText, boolean append) {
        FileHandle out = Gdx.files.external(ACCOUNT_DIRECTORY + "stdout.txt");
        out.writeString(outText, append);

        FileHandle err = Gdx.files.external(ACCOUNT_DIRECTORY + "stderr.txt");
        err.writeString(errText, append);
    }
}
