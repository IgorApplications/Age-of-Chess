package com.iapp.chess.model.ai;

import com.iapp.chess.model.*;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class EasyAI extends OldAI {

    public EasyAI(Color color) {
        super(color);
    }

    public Transition getMove() {
        Transition savingTransit = getMoveForSaveFigure();
        Transition safeTransit = getSafeMove();
        if (savingTransit == null && safeTransit != null) {
            return safeTransit;
        } else if (safeTransit == null && savingTransit != null) {
            return savingTransit;
        } else if (savingTransit != null){
            Figure felledFigure = this.findFelledFigure(this.aiColor);
            Figure safeFigure = this.cloneGame.getFigure(safeTransit.getMove());

            return this.sortAscendingOrder.compare(felledFigure, safeFigure) > 0 ? savingTransit : safeTransit;
        }

        return getRandomMove();
    }

    private Transition getMoveForSaveFigure() {
        Figure felledFigure = this.findFelledFigure(this.aiColor);
        if (felledFigure != null && !this.isFigureProtectedByAnotherFigure(felledFigure)) {

            for (Move cutDownMove : this.getAttackingMoves(felledFigure)) {
                if (this.sortAscendingOrder.compare(this.cloneGame.getFigure(cutDownMove), felledFigure) >= 0) {
                    return new Transition(cloneGame, felledFigure.getX(), felledFigure.getY(), cutDownMove);
                }

                if (this.isMoveSavingForFigure(felledFigure, felledFigure, cutDownMove)) {
                    return new Transition(cloneGame, felledFigure.getX(), felledFigure.getY(), cutDownMove);
                }
            }

            Set<Figure> aiFigures = new TreeSet<>(this.sortDescendingOrder);
            aiFigures.addAll(this.cloneGame.getActiveFigures(this.aiColor));
            Iterator<Figure> var8 = aiFigures.iterator();

            Figure aiFigure;
            Iterator<Move> var5;
            Move aiMove;
            while(var8.hasNext()) {
                aiFigure = var8.next();
                var5 = getAttackingMoves(aiFigure).iterator();

                while(var5.hasNext()) {
                    aiMove = (Move)var5.next();
                    if (this.isMoveSavingForFigure(felledFigure, aiFigure, aiMove)) {
                        return new Transition(cloneGame, aiFigure.getX(), aiFigure.getY(), aiMove);
                    }
                }
            }

            var8 = aiFigures.iterator();

            while(var8.hasNext()) {
                aiFigure = (Figure)var8.next();
                var5 = this.cloneGame.getMoves(aiFigure).iterator();

                while(var5.hasNext()) {
                    aiMove = (Move)var5.next();
                    if (this.isMoveSavingForFigure(felledFigure, aiFigure, aiMove)) {
                        return new Transition(cloneGame, aiFigure.getX(), aiFigure.getY(), aiMove);
                    }
                }
            }

        }
        return null;
    }

    private boolean isFigureProtectedByAnotherFigure(Figure aiFelledFigure) {
        Transition attackingUserTransit = this.findAttackingUserFigure(aiFelledFigure);
        if (attackingUserTransit == null) {
            return true;
        } else {
            Figure attackingUserFigure = this.cloneGame.getFigure(attackingUserTransit);
            if (this.sortAscendingOrder.compare(aiFelledFigure, attackingUserFigure) <= 0) {
                this.move(attackingUserFigure, attackingUserTransit.getMove());

                for (Figure aiFigure : this.cloneGame.getActiveFigures(this.aiColor)) {

                    for (Move aiMove : this.cloneGame.getMoves(aiFigure)) {
                        if (this.cloneGame.getFigure(aiMove) == attackingUserFigure) {
                            this.backMove();
                            return true;
                        }
                    }
                }

                this.backMove();
            }
            return false;
        }
    }

    private Transition getSafeMove() {
        Transition attackedTransit = getAttackedMoves();
        if (attackedTransit != null) {
            return attackedTransit;
        } else {
            Transition nextAttackedTransit = getNextAttackedMove();
            if (nextAttackedTransit != null) {
                return nextAttackedTransit;
            } else {
                for(int i = 0; i < 100; ++i) {
                    Transition randTransit = getRandomMove();
                    if (randTransit == null) {
                        return null;
                    }

                    Figure aiFigure = this.cloneGame.getFigure(randTransit);
                    this.move(aiFigure, randTransit.getMove());
                    Figure cutDownFigure = this.findFelledFigure(this.aiColor);
                    this.backMove();
                    if (cutDownFigure == null || this.isFigureProtectedByAnotherFigure(cutDownFigure) || cutDownFigure instanceof Pawn) {
                        return new Transition(cloneGame, aiFigure.getX(), aiFigure.getY(), randTransit.getMove());
                    }
                }

                return null;
            }
        }
    }

    private Transition getAttackedMoves() {
        TreeMap<Figure, Transition> userFelledFigures = new TreeMap<>(this.sortDescendingOrder);
        Iterator<Figure> var2 = cloneGame.getActiveFigures(this.aiColor).iterator();

        label40:
        while(var2.hasNext()) {
            Figure aiFigure = var2.next();
            Iterator<Move> var4 = cloneGame.getMoves(aiFigure).iterator();

            while(true) {
                Move aiMove;
                Figure aiFelledFigure;
                Figure userFelledFigure;
                do {
                    do {
                        if (!var4.hasNext()) {
                            continue label40;
                        }

                        aiMove = var4.next();
                    } while(!this.cloneGame.isCutDownMove(aiMove));

                    move(aiFigure, aiMove);
                    aiFelledFigure = findFelledFigure(this.aiColor);
                    backMove();
                    userFelledFigure = cloneGame.getFigure(aiMove);
                } while(aiFelledFigure != null && (userFelledFigure == null || sortAscendingOrder.compare(userFelledFigure, aiFelledFigure) < 0));

                Transition attackedTransit = new Transition(cloneGame, aiFigure.getX(), aiFigure.getY(), aiMove);
                userFelledFigures.put(userFelledFigure, attackedTransit);
            }
        }

        if (userFelledFigures.isEmpty()) {
            return null;
        } else {
            Figure mostPriorityFigure = userFelledFigures.navigableKeySet().iterator().next();
            return userFelledFigures.get(mostPriorityFigure);
        }
    }

    private Transition getNextAttackedMove() {

        for (Figure aiFigure : this.cloneGame.getActiveFigures(this.aiColor)) {
            for (Move aiMove : this.cloneGame.getMoves(aiFigure)) {
                move(aiFigure, aiMove);
                Figure userCutDownFigure = this.findFelledFigure(this.userColor);
                Figure aiCutDownFigure = this.findFelledFigure(this.aiColor);
                this.backMove();
                if (aiCutDownFigure == null && userCutDownFigure != null || userCutDownFigure != null && this.sortAscendingOrder.compare(userCutDownFigure, aiCutDownFigure) >= 0 && aiCutDownFigure != aiFigure) {
                    return new Transition(cloneGame, aiFigure.getX(), aiFigure.getY(), aiMove);
                }
            }
        }

        return null;
    }
}