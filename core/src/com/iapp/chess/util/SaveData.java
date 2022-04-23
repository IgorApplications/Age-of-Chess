package com.iapp.chess.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.iapp.chess.controller.Account;
import com.iapp.chess.model.Game;

import java.io.*;

public class SaveData {

    private final FileHandle accountHandler;

    public SaveData() {
        accountHandler = Gdx.files.external("account.dat");
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

    public Game cloneGame(Game game) {
        try {
            return (Game) deserialize(serialize(game));
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
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
}
