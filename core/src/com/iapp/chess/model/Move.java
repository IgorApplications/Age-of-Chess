package com.iapp.chess.model;

import java.io.Serializable;

public abstract class Move implements Serializable {

    public abstract byte getFigureY();

    abstract void setFigureY(byte figureY);

    public abstract byte getFigureX();

    abstract void setFigureX(byte figureX);

    public abstract byte getMoveX();

    abstract void setMoveX(byte moveX);

    public abstract byte getMoveY();

    abstract void setMoveY(byte moveY);

    abstract void reset();

    abstract void init(int figureX, int figureY, int moveX, int moveY);
}
