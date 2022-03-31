package com.iapp.chess.model.ai;

import com.iapp.chess.model.*;
import com.iapp.chess.util.Settings;

import java.io.*;
import java.util.*;

/**
 * Generalized class for all AI
 * @authoor IgorAppliactions
 * @version pre-Alpha
 */
public abstract class AI implements Serializable {

    private  float[][] pawnEvalWhite = {
            {0.0f,  0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
            {5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f},
            {1.0f, 1.0f, 2.0f, 3.0f, 3.0f, 2.0f, 1.0f, 1.0f},
            {0.5f, 0.5f, 1.0f, 2.5f, 2.5f, 1.0f, 0.5f, 0.5f},
            {0.0f, 0.0f, 0.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f},
            {0.5f, -0.5f, -1.0f, 0.0f, 0.0f, -1.0f, -0.5f, 0.5f},
            {0.5f, 1.0f, 1.0f, -2.0f, -2.0f, 1.0f, 1.0f, 0.5f},
            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}
    };

    private float[][] pawnEvalBlack = reverseMatrix(pawnEvalWhite);

    private float[][] knightEval = {
            {-5.0f, -4.0f, -3.0f, -3.0f, -3.0f, -3.0f, -4.0f, -5.0f},
            {-4.0f, -2.0f, 0.0f, 0.0f, 0.0f, 0.0f, -2.0f, -4.0f},
            {-3.0f, 0.0f, 1.0f, 1.5f, 1.5f, 1.0f, 0.0f, -3.0f},
            {-3.0f, 0.5f, 1.5f, 2.0f, 2.0f, 1.5f, 0.5f, -3.0f},
            {-3.0f, 0.0f, 1.5f, 2.0f, 2.0f, 1.5f, 0.0f, -3.0f},
            {3.0f, 0.5f, 1.0f, 1.5f, 1.5f, 1.0f, 0.5f, -3.0f},
            {-4.0f, -2.0f, 0.0f, 0.5f, 0.5f, 0.0f, -2.0f, -4.0f},
            {-5.0f, -4.0f, -3.0f, -3.0f, -3.0f, -3.0f, -4.0f, -5.0f}
    };

    private float[][] bishopEvalWhite = {
            {-2.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -2.0f},
            {-1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f},
            {-1.0f, 0.0f, 0.5f, 1.0f, 1.0f, 0.5f, 0.0f, -1.0f},
            {-1.0f, 0.5f, 0.5f, 1.0f, 1.0f, 0.5f, 0.5f, -1.0f},
            {-1.0f, 0.0f, 1.0f,  1.0f,  1.0f, 1.0f, 0.0f, -1.0f},
            {1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f},
            {-1.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, -1.0f},
            {-2.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -2.0f}
    };

    private float[][] bishopEvalBlack = reverseMatrix(bishopEvalWhite);

    private float[][] rookEvalWhite = {
            {0.0f,  0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
            {0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.5f},
            {-0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f},
            {-0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f},
            {-0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f},
            {-0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f},
            {-0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f},
            {0.0f, 0.0f, 0.0f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f}
    };

    private float[][] rookEvalBlack = reverseMatrix(rookEvalWhite);

    private float[][] evalQueen = {
            {-2.0f, -1.0f, -1.0f, -0.5f, -0.5f, -1.0f, -1.0f, -2.0f},
            {-1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f},
            {-1.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.0f, -1.0f},
            {-0.5f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.0f, -0.5f},
            {0.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.0f, -0.5f},
            {-1.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.0f, -1.0f},
            {-1.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f},
            {-2.0f, -1.0f, -1.0f, -0.5f, -0.5f, -1.0f, -1.0f, -2.0f}
    };

    private float[][] kingEvalWhite = {
            {-3.0f, -4.0f, -4.0f, -5.0f, -5.0f, -4.0f, -4.0f, -3.0f},
            {-3.0f, -4.0f, -4.0f, -5.0f, -5.0f, -4.0f, -4.0f, -3.0f},
            {-3.0f, -4.0f, -4.0f, -5.0f, -5.0f, -4.0f, -4.0f, -3.0f},
            {-3.0f, -4.0f, -4.0f, -5.0f, -5.0f, -4.0f, -4.0f, -3.0f},
            {-2.0f, -3.0f, -3.0f, -4.0f, -4.0f, -3.0f,-3.0f, -2.0f},
            {-1.0f, -2.0f, -2.0f, -2.0f, -2.0f, -2.0f, -2.0f, -1.0f},
            {2.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 2.0f},
            {2.0f, 3.0f, 1.0f, 0.0f, 0.0f, 1.0f, 3.0f, 2.0f}
    };

    private float[][] kingEvalBlack = reverseMatrix(kingEvalWhite);

    protected Game game;
    protected Game cloneGame;
    protected Color aiColor;
    protected Color userColor;
    private Random random = new Random();

    public AI(Color color) {
        this.aiColor = color;
        userColor = reverseColor(aiColor);
    }

    public void setGame(Game game) {
        this.game = game;
        this.cloneGame = game.deepClone();
    }

    public void cloneGame() {
        this.cloneGame = game.deepClone();
    }

    public void setAIColor(Color aiColor) {
        this.aiColor = aiColor;
        userColor = reverseColor(aiColor);
    }

    public Color getAIColor() { return aiColor; }

    public Thread getMove(int depth, AIListener aiListener) {
        cloneGame();

        Runnable task = () -> {
            synchronized (Settings.MUTEX) {
                try {
                    int bestMove = Integer.MIN_VALUE;
                    Transition virtualAIMove = null;

                    for (Transition transition : getAllTransitions(aiColor)) {

                        if (Thread.currentThread().isInterrupted()) return;

                        move(transition);
                        int value;

                        try {
                            value = getMiniMax(depth - 1, -10_000, 10_000, userColor);
                        } catch (InterruptedException e) {
                            System.out.println("debug: aiThread is interrupted");
                            aiListener.finishMove(null);
                            return;
                        }
                        backMove();

                        if (value >= bestMove) {
                            bestMove = value;
                            virtualAIMove = transition;
                        }
                    }

                    Figure figure = game.getFigure(virtualAIMove.getFigureX(), virtualAIMove.getFigureY());
                    Transition transition = new Transition(game, figure.getX(), figure.getY(), virtualAIMove.getMove());

                    if (transition.isCastlingMove()) {
                        transition.setCastlingMove(transition.getLastRookX(), transition.getLastRookY(), transition.getRookX());
                    }
                    if (transition.isUpdatedPawnMove()) {
                        Figure updatedPawn = game.getFigure(transition.getUpdatedPawn().getX(), transition.getUpdatedPawn().getY());
                        transition.setUpdatedPawn(updatedPawn);
                    }

                    aiListener.finishMove(transition);
                } catch (RuntimeException e) {
                    System.err.println("AI move failure, should be caused by thread interruption ->");
                }
            }
        };
        Thread aiThread = new Thread(task);
        aiThread.start();

        return aiThread;
    }
    private float[][] reverseMatrix(float[][] matrix) {
        float[][] reverseMatrix = new float[matrix.length][matrix[0].length];

        for (int i = matrix.length - 1; i >= 0; i--) {
            System.arraycopy(matrix[i], 0, reverseMatrix[matrix.length - 1 - i], 0, matrix[i].length);
        }

        return reverseMatrix;
    }

    private int evaluateBoard() {
        int totalEvaluation = 0;
        for (Figure figure : cloneGame.getGameFigures()) {
            totalEvaluation += evaluateFigure(figure);
        }
        return totalEvaluation;
    }

    public float evaluateFigure(Figure figure) {
        int x = figure.getY(), y = figure.getY();

        float eval = 0;
        if (figure instanceof Pawn) {
             eval = 10 + (figure.getColor() == Color.BLACK ? pawnEvalBlack[y][x] : pawnEvalWhite[y][x]);
        } else if (figure instanceof Rook) {
             eval = 50 + (figure.getColor() == Color.BLACK ? rookEvalBlack[y][x] : rookEvalWhite[y][x]);
        } else if (figure instanceof Knight) {
             eval = 30 + knightEval[y][x];
        } else if (figure instanceof Bishop) {
             eval =  30 + (figure.getColor() == Color.BLACK ? bishopEvalBlack[y][x] : bishopEvalWhite[y][x]);
        } else if (figure instanceof Queen) {
             eval =  90 + evalQueen[y][x];
        } else if (figure instanceof King) {
             eval = 900 + (figure.getColor() == Color.BLACK ? kingEvalBlack[y][x] : kingEvalWhite[y][x]);
        }

        return figure.getColor() == aiColor ? eval : -eval;
    }

    public int getMiniMax(int depth, int alpha, int beta, Color color) throws InterruptedException  {
        if (Thread.currentThread().isInterrupted()) throw new InterruptedException();

        if (depth == 0) {
            return evaluateBoard();
        }

        List<Transition> transitions = getAllTransitions(color);

        int bestMove;
        if (color == aiColor) {
            bestMove = Integer.MIN_VALUE;

            for (Transition transition : transitions) {
                move(transition);

                int value = getMiniMax(depth - 1, alpha, beta, reverseColor(color));
                bestMove = Math.max(bestMove, value);

                backMove();


                alpha = Math.max(alpha, bestMove);
                if (beta <= alpha) {
                    return bestMove;
                }
            }
        } else {
            bestMove = Integer.MAX_VALUE;

            for (Transition transition : transitions) {
                move(transition);

                int value = getMiniMax(depth - 1, alpha, beta, reverseColor(color));
                bestMove = Math.min(bestMove, value);

                backMove();

            }

            beta = Math.min(beta, bestMove);
            if (beta <= alpha) {
                return bestMove;
            }
        }

        return bestMove;
    }

    public List<Transition> getAllTransitions(Color color) {
        Color lastColor = cloneGame.getColorMove();
        cloneGame.setColorMove(color);
        List<Transition> transitions = new ArrayList<>();

        for (Figure figure : cloneGame.getActiveFigures(color)) {
            for (Move move : cloneGame.getMoves(figure)) {
                Transition transition = new Transition(cloneGame, figure, move);
                transitions.add(transition);
            }
        }
        cloneGame.setColorMove(lastColor);

        return transitions;
    }

    public abstract Thread getMove(AIListener aiListener);

    /**
     * @return Transition of a random move and a random figure
     * */
    Transition getRandomMove() {
        List<Figure> figures = cloneGame.getActiveFigures(aiColor);
        if (figures.size() == 0) return null;
        Figure randFigure = figures.get(random.nextInt(figures.size()));

        List<Move> moves = cloneGame.getMoves(randFigure.getX(), randFigure.getY());
        Move randMove = moves.get(random.nextInt(moves.size()));

        return new Transition(cloneGame, randFigure.getX(), randFigure.getY(), randMove);
    }

    void move(Transition transit) {
        cloneGame.move(transit);
        cloneGame.reverseColorMove();
    }

    void backMove() {
        cloneGame.backMove();
    }

    Color reverseColor(Color color) {
        return color == Color.BLACK ? Color.WHITE : Color.BLACK;
    }
}
