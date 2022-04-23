package com.iapp.chess.model;

import java.io.Serializable;
import java.util.Objects;

public class MoveUtils implements Serializable {

    private static final int SIZE_BUFFER = 20_480;

    private final Move[] bufferMoves = new Move[SIZE_BUFFER];

    public MoveUtils() {
        for (int i = 0; i < SIZE_BUFFER; i++) {
            bufferMoves[i] = new GameMove(-1, -1, -1, -1);
        }
    }

    private int index;

   Move getMove() {
        if (index == SIZE_BUFFER) index = 0;

        //Move move = bufferMoves[index++];
        //move.reset();

        return new GameMove(-1, -1, -1, -1);
    }

    Move getMove(int figureX, int figureY, int moveX, int moveY) {
        Move move = getMove();
        move.init(figureX, figureY, moveX, moveY);
        return move;
    }

    private static class GameMove extends Move {

        private byte figureX;
        private byte figureY;

        private byte moveX;
        private byte moveY;

        private GameMove(int figureX, int figureY, int moveX, int moveY) {
            this.moveX = (byte) moveX;
            this.moveY = (byte) moveY;

            this.figureX = (byte) figureX;
            this.figureY = (byte) figureY;
        }

        @Override
        public byte getFigureY() {
            return figureY;
        }

        @Override
        void setFigureY(byte figureY) {
            this.figureY = figureY;
        }

        @Override
        public byte getFigureX() {
            return figureX;
        }

        @Override
        void setFigureX(byte figureX) {
            this.figureX = figureX;
        }

        @Override
        public byte getMoveX() {
            return moveX;
        }

        @Override
        void setMoveX(byte moveX) {
            this.moveX = moveX;
        }

        @Override
        public byte getMoveY() {
            return moveY;
        }

        @Override
        void setMoveY(byte moveY) {
            this.moveY = moveY;
        }

        @Override
        void reset() {
            figureX = -1;
            figureY = -1;
            moveX = -1;
            moveY = -1;
        }

        @Override
        void init(int figureX, int figureY, int moveX, int moveY) {
            this.moveX = (byte) moveX;
            this.moveY = (byte) moveY;

            this.figureX = (byte) figureX;
            this.figureY = (byte) figureY;
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
}
