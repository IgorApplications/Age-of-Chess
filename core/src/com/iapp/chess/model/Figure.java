package com.iapp.chess.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public abstract class Figure implements Serializable {

    private Game game;
    private int x;
    private int y;
    private Color color;
    private int evaluation;

    Figure(Game game, int x, int y, Color color, int evaluation) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.color = color;
        this.evaluation = evaluation;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getX() { return x; }

    void setX(int x) { this.x = x; }

    public int getY() { return y; }

    void setY(int y) { this.y = y; }

    public Color getColor() { return color; }

    void setColor(Color color) { this.color = color; }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public int getEvaluation() {
        return evaluation;
    }

    /**
     * this method only shows what moves this type of figure can have
     * */
    abstract List<Move> getMoves();

    Color reverseColor(Color color) {
        return color == Color.BLACK ? Color.WHITE : Color.BLACK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Figure)) return false;
        Figure figure = (Figure) o;
        return x == figure.x && y == figure.y && evaluation == figure.evaluation && color == figure.color && game.equals(figure.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, color, game, evaluation);
    }

    @Override
    public String toString() {
        return getClass() + " ( " + x + ", " + y + ", " + color + ", " + evaluation + " ); ";
    }
}
