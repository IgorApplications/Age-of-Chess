package com.iapp.chess.model;

import com.iapp.chess.model.ai.AI;
import com.iapp.chess.model.ai.AIListener;
import com.iapp.chess.util.Settings;

import java.io.*;
import java.util.*;

public class Game implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * do not use figure != CAGE as the link will be different when you restart the program.
     * use !(figure instanceof Cage)
     * */
    private static final Cage CAGE = Cage.getInstance();

    private Figure[][] game;
    private final LinkedList<Transition> transitions = new LinkedList<>();
    private Color colorMove;
    private Color upperColor;
    private Color lowerColor;
    private AI ai;
    // turn = 1
    private long move = 0;

    public Game(AI ai) {
        if (ai != null) {
            this.ai = ai;
            ai.setGame(this);
            upperColor = ai.getAIColor();
        } else {
            upperColor = Color.BLACK;
        }
        lowerColor = reverseColor(upperColor);
        colorMove = Color.WHITE;

        initBoard(upperColor, lowerColor);
    }

    private void initBoard(Color upper, Color lower) {
        game = new Figure[][]
                {
                        {new Rook( this,0, 0, upper, -50), new Knight(this,1, 0, upper, -30), new Bishop(this,2, 0, upper, -30), new Queen(this,3, 0, upper, -90), new King(this,4, 0, upper, -900), new Bishop(this,5, 0, upper, -30), new Knight(this,6, 0, upper, -30), new Rook(this,7, 0, upper, -50)},
                        {new Pawn(this,0, 1, upper, -10), new Pawn(this,1, 1, upper, -10), new Pawn(this,2, 1, upper, -10), new Pawn(this,3, 1, upper, -10), new Pawn(this,4, 1, upper, -10), new Pawn(this,5, 1, upper, -10), new Pawn(this,6, 1, upper, -10), new Pawn(this,7, 1, upper, -10)},
                        {CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE},
                        {CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE},
                        {CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE},
                        {CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE, CAGE},
                        {new Pawn(this,0, 6, lower, 10), new Pawn(this,1, 6, lower, 10), new Pawn(this,2, 6, lower, 10), new Pawn(this,3, 6, lower, 10), new Pawn(this,4, 6, lower, 10), new Pawn(this,5, 6, lower, 10), new Pawn(this,6, 6, lower, 10), new Pawn(this,7, 6, lower, 10)},
                        {new Rook(this,0, 7, lower, 50), new Knight(this,1, 7, lower, 30), new Bishop(this,2, 7, lower, 30), new Queen(this,3, 7, lower, 90), new King(this,4, 7, lower, 900), new Bishop(this,5, 7, lower, 30), new Knight(this,6, 7, lower, 30), new Rook(this,7, 7, lower, 50)},
                };

        if (upper == Color.WHITE) {
            Figure queen = game[7][3];
            queen.setX(4);

            Figure king  = game[7][4];
            king.setX(3);

            game[7][3] = king;
            game[7][4] = queen;

            queen = game[0][3];
            queen.setX(4);

            king = game[0][4];
            king.setX(3);

            game[0][3] = king;
            game[0][4] = queen;
        }
    }

    public Color getAIColor() {
        return ai.getAIColor();
    }

    public Color getUpperColor() {
        return upperColor;
    }

    public Color getLowerColor() {
        return lowerColor;
    }

    public boolean isEmptyTransitions() {
        return transitions.isEmpty();
    }

    public Color getColorMove() {
        return colorMove;
    }

    public void setColorMove(Color colorMove) { this.colorMove = colorMove; }

    public AI getAI() {
        return ai;
    }

    public List<Figure> getGameFigures() {
        List<Figure> figures = new ArrayList<>();
        for (Figure[] line : game) {
            for (Figure figure : line) {
                if (!(figure instanceof Cage)) {
                    figures.add(figure);
                }
            }
        }
        return figures;
    }

    // turn = move / 2
    public long getTurn() {
        return move / 2 + 1;
    }

    public long getMove() {
        return move;
    }

    public void move(Transition transition) {
        transitions.offer(transition);
        Figure figure = getFigure(transition.getFigureX(), transition.getFigureY());

        if (figure instanceof Pawn) ((Pawn) figure).setFirstMove(false);

        repositionFigure(figure, transition.getMove());

        reverseColorMove();
        move++;
    }

    public Transition backMove() {
        if (transitions.isEmpty()) return null;

        Transition lastTransition = transitions.getLast();
        Figure figure = getFigure(lastTransition.getMove());
        if (figure instanceof Pawn && (lastTransition.getFigureY() == 1 || lastTransition.getFigureY() == 6)) {
            ((Pawn) figure).setFirstMove(true);
        }

        backRepositionFigure();
        move--;

        return lastTransition;
    }

    private void repositionFigure(Figure figure, Move move) {
        Transition lastTransition = transitions.getLast();
        int figureX = figure.getX();
        int figureY = figure.getY();

        if (getCastlingMoves(figure).contains(move)) {
            performCastle(figureX, figureY, move);
        } else {
            game[move.getY()][move.getX()] = figure;
            game[figureY][figureX] = CAGE;
            figure.setX(move.getX());
            figure.setY(move.getY());
        }

        if (ai != null && ai.getAIColor() == colorMove && game[move.getY()][move.getX()] instanceof Pawn &&
                (game[move.getY()][move.getX()].getY() == 0 || game[move.getY()][move.getX()].getY() == 7)) {
            lastTransition.setUpdatedPawn(figure);

            if (colorMove == upperColor) {
                game[figure.getY()][figure.getX()] = new Queen(this, figure.getX(), figure.getY(), figure.getColor(), -50);
            } else {
                game[figure.getY()][figure.getX()] = new Queen(this, figure.getX(), figure.getY(), figure.getColor(), 50);
            }
        }
    }

    private void backRepositionFigure() {
        Transition lastTransition = transitions.removeLast();
        int moveX = lastTransition.getMoveX(), moveY = lastTransition.getMoveY();
        int figureX = lastTransition.getFigureX(), figureY = lastTransition.getFigureY();

        Figure moveFigure;
        if (lastTransition.isUpdatedPawnMove()) moveFigure = lastTransition.getUpdatedPawn();
        else moveFigure = getFigure(lastTransition.getMove());

        moveFigure.setX(figureX);
        moveFigure.setY(figureY);
        game[figureY][figureX] = moveFigure;

        game[moveY][moveX] = lastTransition.getFelledFigure();

        if (lastTransition.isCastlingMove()) {
            Figure rook = game[lastTransition.getRookY()][lastTransition.getRookX()];
            game[rook.getY()][rook.getX()] = CAGE;
            rook.setX(lastTransition.getLastRookX());
            rook.setY(lastTransition.getLastRookY());
            game[lastTransition.getLastRookY()][lastTransition.getLastRookX()] = rook;
        }
    }

    public Thread makeMoveAI(AIListener aiListener) {
        Runnable task = () -> {
            synchronized (Settings.MUTEX) {
                if (ai != null && ((ai.getAIColor() == Color.BLACK && colorMove == Color.BLACK) || (ai.getAIColor() == Color.WHITE && colorMove == Color.WHITE))) {
                    Transition moveAI = ai.getMove();
                    if (moveAI == null) return;

                    move(moveAI);

                    if (!Thread.currentThread().isInterrupted()) aiListener.finishMove(new Transition(this, moveAI.getFigureX(), moveAI.getFigureY(), moveAI.getMove()));
                } else {
                    if (!Thread.currentThread().isInterrupted()) aiListener.finishMove(null);
                }
            }
        };

        Thread threadAI = new Thread(task);
        threadAI.start();

        return threadAI;
    }
    public boolean isFiguresHaveNotMoves() {
        for (Figure figure : getGameFigures()) {
            if (!getMoves(figure.getX(), figure.getY()).isEmpty())
                return false;
        }
        return true;
    }

    public List<Move> getMoves(Figure figure) {
        return getMoves(figure.getX(), figure.getY());
    }

    public List<Move> getMoves(int x, int y) {
        Figure figure = game[y][x];
        try {
            if (colorMove == Color.BLACK && figure.getColor() == Color.WHITE ||
                    colorMove == Color.WHITE && figure.getColor() == Color.BLACK)
                return new ArrayList<>();
        } catch (Exception e) {
            System.out.println(getFigure(x, y));
        }

        List<Move> moves;

        boolean kingIsCheck = isCheckKing(game[y][x].getColor());

        if (kingIsCheck && game[y][x] instanceof King) moves = game[y][x].getMoves();
        else if (kingIsCheck) moves = getMovesSaveKing(x, y);
        else moves = figure.getMoves();

        moves.addAll(getCastlingMoves(figure));

        List<Move> notCheckMoves = new ArrayList<>();
        for (Move move : moves) {
            if (isNotCheckMove(x, y, move))
                notCheckMoves.add(move);
        }

        return notCheckMoves;
    }

    public List<Figure> getActiveFigures(Color color) {
        List<Figure> activeFigures = new ArrayList<>();
        for (Figure figure : getGameFigures()) {
            if (!getMoves(figure).isEmpty() && figure.getColor() == color)
                activeFigures.add(figure);
        }
        return activeFigures;
    }

    public boolean isCutDownMove(Move move) { return !(getFigure(move) instanceof Cage); }

    public Figure getFigure(Transition transition) { return getFigure(transition.getFigureX(), transition.getFigureY()); }

    public Figure getFigure(Move move) { return getFigure(move.getX(), move.getY()); }

    public Figure getFigure(int x, int y) { return game[y][x]; }

    public boolean isCheckKing(Color color) {
        if (color == null) throw new RuntimeException("Iapp: color can't be null");
        List<Figure> figures = getGameFigures();
        King king = getKingForColor(color);

        for (Figure figure : figures) {

            if (figure.getColor() != color) {

                if (figure instanceof Pawn) {

                    Pawn pawn = (Pawn) figure;
                    for (Move move : pawn.getCutDownFakeMoves()) {
                        if (move.getX() == king.getX() && move.getY() == king.getY()) {
                            return true;
                        }
                    }
                } else if (figure instanceof King) {
                    King enemyKing = (King) figure;

                    for (Move move : enemyKing.getNoSafeMoves()) {
                        if (move.getX() == king.getX() && move.getY() == king.getY())
                            return true;
                    }
                } else {
                    for (Move move : figure.getMoves()) {
                        if (move.getX() == king.getX() && move.getY() == king.getY())
                            return true;
                    }
                }
            }
        }

        return false;
    }

    public King getKingForColor(Color colorKing) {
        List<Figure> figures = getGameFigures();

        King king = null;

        for (Figure figure : figures) {
            if (figure instanceof King && figure.getColor() == colorKing) {
                king = (King) figure;
                break;
            }
        }

        if (king == null) throw new RuntimeException("Iapp: king is null in current game");

        return king;
    }

    public void updatePawn(int pawnX, int pawnY, Figure updateFigure) {
        Pawn pawn = (Pawn) getFigure(pawnX, pawnY);
        transitions.getLast().setUpdatedPawn(pawn);
        game[updateFigure.getY()][updateFigure.getX()] = updateFigure;
    }

    boolean isSafeMoveKing(int x, int y, King king, Color enemyColor) {
        List<Figure> figures = getGameFigures();

        for (Figure figure : figures) {
            if (figure.getColor() == enemyColor) {
                if (figure instanceof Pawn) {
                    Pawn pawn = (Pawn) figure;
                    for (Move move : pawn.getCutDownFakeMoves()) {
                        if (move.getX() == x && move.getY() == y) {
                            return false;
                        }
                    }
                } else if (figure instanceof King) {
                    King enemyKing = (King) figure;
                    for (Move move : enemyKing.getNoSafeMoves()) {
                        if (move.getX() == x && move.getY() == y) {
                            return false;
                        }
                    }
                } else {
                    for (Move move : figure.getMoves()) {
                        if (move.getX() == x && move.getY() == y) {
                            return false;
                        }
                    }
                }
            }
        }

        int lastX = king.getX();
        int lastY = king.getY();

        Figure saveFigure = game[y][x];
        game[y][x] = king;
        game[lastY][lastX] = CAGE;

        king.setX(x);
        king.setY(y);

        boolean res = isCheckKing(king.getColor());

        game[y][x] = saveFigure;
        game[lastY][lastX] = king;

        king.setX(lastX);
        king.setY(lastY);

        return !res;
    }

    private boolean isNotCheckMove(int x, int y, Move move) {
        Figure figure = game[y][x];
        Figure saveFigure = game[move.getY()][move.getX()];
        boolean isNotCheckMove = true;

        game[y][x] = CAGE;
        game[move.getY()][move.getX()] = figure;
        figure.setX(move.getX());
        figure.setY(move.getY());

        if (isCheckKing(figure.getColor())) isNotCheckMove = false;

        game[y][x] = figure;
        game[move.getY()][move.getX()] = saveFigure;
        figure.setX(x);
        figure.setY(y);

        return isNotCheckMove;
    }

    private List<Move> getMovesSaveKing(int x, int y) {
        List<Move> movesSaveKing = new ArrayList<>();

        for (Move move : game[y][x].getMoves()) {
            Figure saveFigure = game[move.getY()][move.getX()];
            game[move.getY()][move.getX()] = game[y][x];
            game[y][x] = CAGE;

            if (!isCheckKing(game[move.getY()][move.getX()].getColor()))
                movesSaveKing.add(move);

            game[y][x] = game[move.getY()][move.getX()];
            game[move.getY()][move.getX()] = saveFigure;
        }

        return movesSaveKing;
    }

    private List<Move> getCastlingMoves(Figure self) {
        List<Move> castlingMoves = new ArrayList<>();
        if (!(self instanceof King)) return castlingMoves;

        if (isCastlingPossible(7, TypeCastling.LEFT)) {
            Move castlingMove = new Move(2, 7);
            castlingMove.setCastlingMove(true);
            castlingMoves.add(castlingMove);
        }

        if (isCastlingPossible(7, TypeCastling.RIGHT)) {
            Move castlingMove = new Move(6, 7);
            castlingMove.setCastlingMove(true);
            castlingMoves.add(castlingMove);
        }

        if (isCastlingPossible(0, TypeCastling.LEFT)) {
            Move castlingMove = new Move(2, 0);
            castlingMove.setCastlingMove(true);
            castlingMoves.add(castlingMove);
        }

        if (isCastlingPossible(0, TypeCastling.RIGHT)) {
            Move castlingMove = new Move(6, 0);
            castlingMove.setCastlingMove(true);
            castlingMoves.add(castlingMove);
        }

        return castlingMoves;
    }

    private boolean isCastlingPossible(int y, TypeCastling typeCastling) {
        boolean castlingPossible = game[y][typeCastling.rookX] instanceof Rook && game[y][typeCastling.kingX] instanceof King;
        for (int x : typeCastling.cageX) {
            castlingPossible = castlingPossible && game[y][x] instanceof Cage;
        }
        return castlingPossible;
    }

    private void performCastle(int kingX, int kingY, Move kingMove) {
        Figure king = game[kingY][kingX];
        kingMove.setCastlingMove(true);

        game[kingMove.getY()][kingMove.getX()] = king;
        king.setX(kingMove.getX());
        game[kingY][kingX] = CAGE;

        if (kingMove.getX() > kingX) {
            game[kingMove.getY()][kingMove.getX() - 1] = game[kingMove.getY()][kingMove.getX() + 1];
            game[kingMove.getY()][kingMove.getX() + 1] = CAGE;

            Figure rookFigure = game[kingMove.getY()][kingMove.getX() - 1];
            if (!(rookFigure instanceof Rook)) throw new RuntimeException("When back castling, the figure must be a rook");
            Rook rook = (Rook) rookFigure;

            transitions.getLast().setCastlingMove(rook.getX(), rook.getY(), kingMove.getX() - 1);
            rook.setX(kingMove.getX() - 1);
        } else {
            game[kingMove.getY()][kingMove.getX() + 1] = game[kingMove.getY()][kingMove.getX() - 2];
            game[kingMove.getY()][kingMove.getX() - 2] = CAGE;

            Figure rookFigure = game[kingMove.getY()][kingMove.getX() + 1];
            if (!(rookFigure instanceof Rook)) throw new RuntimeException("When back castling, the figure must be a rook");
            Rook rook = (Rook) rookFigure;

            transitions.getLast().setCastlingMove(rook.getX(), rook.getY(), kingMove.getX() + 1);
            rook.setX(kingMove.getX() + 1);
        }
    }

    public void reverseColorMove() {
        if (colorMove == Color.BLACK) colorMove = Color.WHITE;
        else colorMove = Color.BLACK;
    }

    private Color reverseColor(Color color) {
        return color == Color.BLACK ? Color.WHITE : Color.BLACK;
    }

    private enum TypeCastling {
        LEFT(0, 4, 1, 2, 3),
        RIGHT(7, 4, 5, 6);

        final int rookX;
        final int kingX;
        final int[] cageX;

        TypeCastling(int rookX, int kingX, int... cageX) {
            this.rookX = rookX;
            this.kingX = kingX;
            this.cageX = cageX;
        }
    }

    private void reverseMatrix() {
        for (int i = game.length - 1; i >= 0; i--) {
            for (int j = 0; j < game[i].length; j++) {
                Figure figure = game[i][j];
                if (!(figure instanceof Cage)) {
                    if (figure.getColor() == Color.WHITE) {
                        figure.setColor(Color.BLACK);
                    } else {
                        figure.setColor(Color.WHITE);
                    }
                }
            }
        }
    }

    public Game deepClone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Game) ois.readObject();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
