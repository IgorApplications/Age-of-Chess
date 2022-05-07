package com.iapp.chess.model;

import java.io.Serializable;
import java.util.Objects;

public class Move implements Serializable {

    private byte figureX;
    private byte figureY;

    private byte moveX;
    private byte moveY;

    public Move(int figureX, int figureY, int moveX, int moveY) {
        this.moveX = (byte) moveX;
        this.moveY = (byte) moveY;

        this.figureX = (byte) figureX;
        this.figureY = (byte) figureY;
    }

    public byte getFigureY() {
        return figureY;
    }

    void setFigureY(byte figureY) {
        this.figureY = figureY;
    }

    public byte getFigureX() {
        return figureX;
    }

    void setFigureX(byte figureX) {
        this.figureX = figureX;
    }

    public byte getMoveX() {
        return moveX;
    }

    void setMoveX(byte moveX) {
        this.moveX = moveX;
    }

    public byte getMoveY() {
        return moveY;
    }

    void setMoveY(byte moveY) {
        this.moveY = moveY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return figureX == move.getFigureX() && figureY == move.getFigureY() && moveX == move.getMoveX() && moveY == move.getMoveY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(figureX, figureY, moveX, moveY);
    }

    @Override
    public String toString() {
        return String.format("figureX = %d figureY = %d moveX = %d moveY = %d", figureX, figureY, moveX, moveY);
    }
}
