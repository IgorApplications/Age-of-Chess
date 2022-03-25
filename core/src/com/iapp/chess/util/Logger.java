package com.iapp.chess.util;

import java.util.Arrays;

public class Logger {

    private static final Logger INSTANCE= new Logger();
    private String textLog = null;

    public static Logger getInstance() {
        return INSTANCE;
    }

    private Logger() {}

    public void log(Throwable t) {
        textLog = t.toString() + Arrays.toString(t.getStackTrace());
    }

    public boolean isEmpty() {
        return textLog == null;
    }

    public String getText() {
        return textLog;
    }
}
