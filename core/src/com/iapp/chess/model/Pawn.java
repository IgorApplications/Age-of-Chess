package com.iapp.chess.model;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Figure {

    private boolean firstMove = true;

    public Pawn(Game game, int x, int y, Color color, int evaluation) {
        super(game, x, y, color, evaluation);
    }

    public Pawn(Game game, int x, int y, Color color) {
        this(game, x, y, color, game.getAIColor() == color ? 10 : -10);
    }

    public void setFirstMove(boolean firstMove) { this.firstMove = firstMove; }

    @Override
    public List<Move> getMoves() {
        List<Move> moves = new ArrayList<>();

        if (getColor() == getGame().getUpperColor() && getY() + 1 >= 8) return moves;

        if (getColor() == getGame().getLowerColor() && getY() - 1 <= -1) return moves;

        moves.addAll(getCutDownMoves());

        if (firstMove) {
            moves.addAll(getFirstMoves());
        } else {
            if (getColor() == getGame().getUpperColor()  && getGame().getFigure(getX(), getY() + 1) instanceof Cage)
                moves.add(new Move(getX(), getY() + 1));
            else if (getColor() == getGame().getLowerColor() && getGame().getFigure(getX(), getY() - 1) instanceof Cage)
                moves.add(new Move(getX(), getY() - 1));
        }

        return moves;
    }

    private List<Move> getFirstMoves() {
        List<Move> firstMoves = new ArrayList<>();

        Move move1 = null;
        Move move2 = null;

        if (getColor() == getGame().getUpperColor()) {
            if (getGame().getFigure(getX(), getY() + 1) instanceof Cage) {
                move1 = new Move(getX(), getY() + 1);

                if (getGame().getFigure(getX(), getY() + 2) instanceof Cage)
                    move2 = new Move(getX(), getY() + 2);
            }
        } else  {
            if (getGame().getFigure(getX(), getY() - 1) instanceof Cage) {
                move1 = new Move(getX(), getY() - 1);

                if (getGame().getFigure(getX(), getY() - 2) instanceof Cage)
                    move2 = new Move(getX(), getY() - 2);
            }
        }

        if (move1 != null)
            firstMoves.add(move1);
        if (move2 != null)
            firstMoves.add(move2);

        return firstMoves;
    }

    private List<Move> getCutDownMoves() {
        List<Move> cutDownMoves = new ArrayList<>();

        Figure cutDown1 = null;
        Figure cutDown2 = null;

        if (getColor() == getGame().getUpperColor()) {
            if (getX() - 1 > -1) cutDown1=  getGame().getFigure(getX() - 1, getY() + 1);
            if (getX() + 1 < 8) cutDown2 = getGame().getFigure(getX() + 1, getY() + 1);

            if (cutDown1 != null && !(cutDown1 instanceof Cage) && cutDown1.getColor() != getColor())
                cutDownMoves.add(new Move(getX() - 1, getY() + 1));
            if (cutDown2 != null && !(cutDown2 instanceof Cage) && cutDown2.getColor() != getColor())
                cutDownMoves.add(new Move(getX() + 1, getY() + 1));
        } else {
            if (getX() - 1 > -1) cutDown1 = getGame().getFigure(getX() - 1, getY() - 1);
            if (getX() + 1 < 8) cutDown2 = getGame().getFigure(getX() + 1, getY() - 1);

            if (cutDown1 != null && !(cutDown1 instanceof Cage) && cutDown1.getColor() != getColor())
                cutDownMoves.add(new Move(getX() - 1, getY() - 1));
            if (cutDown2 != null && !(cutDown2 instanceof Cage) && cutDown2.getColor() != getColor())
                cutDownMoves.add(new Move(getX() + 1, getY() - 1));
        }

        return cutDownMoves;
    }

    List<Move> getCutDownFakeMoves() {
        List<Move> cutDownMoves = new ArrayList<>();

        if (getColor() == getGame().getUpperColor()) {

            if (getX() - 1 > -1)
                cutDownMoves.add(new Move(getX() - 1, getY() + 1));
            if (getX() + 1 < 8)
                cutDownMoves.add(new Move(getX() + 1, getY() + 1));
        } else {

            if (getX() - 1 > -1)
                cutDownMoves.add(new Move(getX() - 1, getY() - 1));
            if (getX() + 1 < 8)
                cutDownMoves.add(new Move(getX() + 1, getY() - 1));
        }

        return cutDownMoves;
    }
}
