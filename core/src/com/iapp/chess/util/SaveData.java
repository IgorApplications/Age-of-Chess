package com.iapp.chess.util;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.iapp.chess.controller.Account;
import com.iapp.chess.model.Game;

import java.io.*;

public class SaveData {

    private static final String DIRECTORY = "chess-data" + File.separator;
    private final FileHandle accountHandler;

    public SaveData() {
        accountHandler = Gdx.files.external(DIRECTORY + "account.dat");
    }

    public void appendLogs() {
        writeLogos(Settings.LOGGER.getOutLogs(), Settings.LOGGER.getErrLogs(), true);
    }

    public void removeLogs() {
        writeLogos("", "", false);
    }

    public void saveAccount(Account account) {
        try {
            accountHandler.writeBytes(serialize(account), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Account readAccount() {
        if (!accountHandler.exists()) return null;
        try {
            return (Account) deserialize(accountHandler.readBytes());
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    private byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            ObjectOutputStream byteWriter = new ObjectOutputStream(byteArray)) {
            byteWriter.writeObject(obj);
            return byteArray.toByteArray();
        }
    }

    private Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
            ObjectInputStream bytesReader = new ObjectInputStream(byteArray)) {
            return bytesReader.readObject();
        }
    }

    private void writeLogos(String outText, String errText, boolean append) {
        FileHandle out = Gdx.files.external(DIRECTORY + "out.txt");
        out.writeString(Settings.LOGGER.getOutLogs(), true);

        FileHandle err = Gdx.files.external(DIRECTORY + "err.txt");
        err.writeString(Settings.LOGGER.getErrLogs(), true);
    }
}
