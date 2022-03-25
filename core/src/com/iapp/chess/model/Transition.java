package com.iapp.chess.model;

import java.io.Serializable;
import java.util.Objects;

public class Transition implements Serializable {

    private Game game;
    private int figureX, figureY;
    private Move moveFigure;

    private int evaluation;
    private Figure felledFigure;

    private  int rookX, rookY, lastRookX, lastRookY;
    private boolean castlingMove;
    private Figure updatedPawn;

    public Transition(Game game, int figureX, int figureY, Move move) {
        this.game = game;
        this.figureX = figureX;
        this.figureY = figureY;
        moveFigure = move;

        felledFigure = game.getFigure(move);
        evaluation = felledFigure.getEvaluation();
    }

    public Transition(Game game, Figure figure, Move move) {
        this(game, figure.getX(), figure.getY(), move);
    }

    public Game getGame() {
        return game;
    }
    public boolean isAIMove() {
        return game.getFigure(figureX, figureY).getColor() == game.getAIColor();
    }

    public boolean isUserMove() {
        return !isAIMove();
    }

    public int getFigureX() {
        return figureX;
    }

    public int getFigureY() {
        return figureY;
    }

    public Move getMove() {
        return moveFigure;
    }

    public int getMoveX() {
        return moveFigure.getX();
    }

    public int getMoveY() {
        return moveFigure.getY();
    }

    public Figure getFelledFigure() {
        return felledFigure;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setCastlingMove(int lastRookX, int lastRookY, int rookX) {
        this.lastRookX = lastRookX;
        this.lastRookY = lastRookY;
        this.rookX = rookX;
        rookY = lastRookY;

        castlingMove = true;
    }

    public int getRookX() {
        return rookX;
    }

    public void setRookX(int rookX) {
        this.rookX = rookX;
    }

    public int getRookY() {
        return rookY;
    }

    public void setRookY(int rookY) {
        this.rookY = rookY;
    }

    public boolean isCastlingMove() {
        return castlingMove;
    }

    public int getLastRookX() {
        return lastRookX;
    }

    public int getLastRookY() {
        return lastRookY;
    }


    public void setUpdatedPawn(Figure updatedPawn) {
        this.updatedPawn = updatedPawn;
    }

    public Figure getUpdatedPawn() {
        return updatedPawn;
    }

    public boolean isUpdatedPawnMove() {
        return updatedPawn != null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transition)) return false;
        Transition that = (Transition) o;
        return lastRookX == that.lastRookX && lastRookY == that.lastRookY && rookX == that.rookX && figureX == that.figureX &&
                figureY == that.figureY && moveFigure.equals(that.moveFigure)
                && Objects.equals(felledFigure, that.felledFigure) && Objects.equals(updatedPawn, that.updatedPawn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(figureX, figureY, moveFigure, lastRookX, lastRookY, felledFigure, updatedPawn);
    }

    @Override
    public String toString() {
        return String.format("evaluation = %d", evaluation);
    }
}
