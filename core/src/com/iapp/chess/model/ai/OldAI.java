package com.iapp.chess.model.ai;

import com.iapp.chess.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public abstract class OldAI extends AI {
    protected transient Comparator<Figure> sortAscendingOrder = Comparator.comparingInt(f -> (int) Math.abs(evaluateFigure(f)));
    protected transient Comparator<Figure> sortDescendingOrder = Comparator.comparingInt(f -> (int) Math.abs(evaluateFigure(f)));

    public OldAI(Color color) {
        super(color);
    }

    public void setGame(Game cloneGame) {
        this.cloneGame = cloneGame;
    }

    public void setAIColor(Color aiColor) {
        this.aiColor = aiColor;
    }

    public Color getAIColor() {
        return this.aiColor;
    }

    public abstract Transition getMove();

    boolean isMoveSavingForFigure(Figure felledFigure, Figure aiFigure, Move aiMove) {
        this.move(aiFigure, aiMove);
        Figure felledFigure2 = this.findFelledFigure(this.aiColor);
        this.backMove();
        if (felledFigure != felledFigure2) {
            return felledFigure2 == null || this.sortAscendingOrder.compare(felledFigure, felledFigure2) >= 0;
        } else {
            return false;
        }
    }

    Figure findFelledFigure(Color color) {
        Set<Figure> cutDownFigures = new TreeSet(this.sortDescendingOrder);
        Color anotherColor = this.reverseColor(color);
        this.cloneGame.setColorMove(anotherColor);
        Iterator var4 = this.cloneGame.getActiveFigures(anotherColor).iterator();

        while(var4.hasNext()) {
            Figure userFigure = (Figure)var4.next();
            Iterator var6 = this.cloneGame.getMoves(userFigure).iterator();

            while(var6.hasNext()) {
                Move userMove = (Move)var6.next();
                if (this.cloneGame.isCutDownMove(userMove)) {
                    Figure cutDownFigure = this.cloneGame.getFigure(userMove);
                    if (!(cutDownFigure instanceof Pawn)) {
                        cutDownFigures.add(cutDownFigure);
                    }
                }
            }
        }

        this.cloneGame.setColorMove(color);
        if (cutDownFigures.isEmpty()) {
            return null;
        } else {
            return (Figure)cutDownFigures.iterator().next();
        }
    }

    Transition findAttackingUserFigure(Figure felledFigure) {
        TreeMap<Figure, Move> attackingUserFigures = new TreeMap(this.sortAscendingOrder);
        this.cloneGame.setColorMove(this.userColor);
        Iterator var3 = this.cloneGame.getActiveFigures(this.userColor).iterator();

        while(var3.hasNext()) {
            Figure userFigure = (Figure)var3.next();
            Iterator var5 = this.cloneGame.getMoves(userFigure).iterator();

            while(var5.hasNext()) {
                Move userMove = (Move)var5.next();
                if (felledFigure == this.cloneGame.getFigure(userMove)) {
                    this.cloneGame.setColorMove(this.userColor);
                    attackingUserFigures.put(userFigure, userMove);
                }
            }
        }

        this.cloneGame.setColorMove(this.aiColor);
        if (attackingUserFigures.isEmpty()) {
            return null;
        } else {
            Figure resFigure = attackingUserFigures.navigableKeySet().iterator().next();
            return new Transition(cloneGame, resFigure, (Move)attackingUserFigures.get(resFigure));
        }
    }

    List<Move> getAttackingMoves(Figure figure) {
        List<Move> cutDownMoves = new ArrayList();
        Iterator var3 = this.cloneGame.getMoves(figure).iterator();

        while(var3.hasNext()) {
            Move move = (Move)var3.next();
            if (this.cloneGame.isCutDownMove(move)) {
                cutDownMoves.add(move);
            }
        }

        return cutDownMoves;
    }

    void move(Figure figure, Move move) {
        this.cloneGame.move(new Transition(cloneGame, figure, move));
        this.cloneGame.reverseColorMove();
    }

    void backMove() {
        this.cloneGame.backMove();
    }

    Color reverseColor(Color color) {
        return color == Color.BLACK ? Color.WHITE : Color.BLACK;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.sortAscendingOrder = Comparator.comparingInt(f -> (int) Math.abs(evaluateFigure(f)));
        this.sortDescendingOrder = Comparator.comparingInt(f -> (int) Math.abs(evaluateFigure(f)));
    }
}
