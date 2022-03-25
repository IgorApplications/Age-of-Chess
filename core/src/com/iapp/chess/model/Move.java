package com.iapp.chess.model;

import java.io.Serializable;
import java.util.Objects;

public class Move implements Serializable {

    private int moveX;
    private int moveY;
    private boolean castlingMove;

    public Move(int moveX, int moveY) {
        this.moveX = moveX;
        this.moveY = moveY;
    }

    public int getX() { return moveX; }

    void setX(int moveX) { this.moveX = moveX; }

    public int getY() { return moveY; }

    void setY(int moveY) { this.moveY = moveY; }

    public void setCastlingMove(boolean cancerMove) {
        this.castlingMove = cancerMove;
    }

    public boolean isCastlingMove() {
        return castlingMove;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return moveX == move.moveX && moveY == move.moveY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(moveX, moveY);
    }

    @Override
    public String toString() {
        return String.format("moveX=%d moveY=%d", moveX, moveY);
    }
}