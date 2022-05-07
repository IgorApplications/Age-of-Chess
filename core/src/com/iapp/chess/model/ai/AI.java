package com.iapp.chess.model.ai;

import com.badlogic.gdx.utils.Array;
import com.iapp.chess.model.Color;
import com.iapp.chess.model.Game;
import com.iapp.chess.model.Move;
import com.iapp.chess.util.Pair;
import jdk.nashorn.internal.ir.IdentNode;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AI {

    private final float[][] pawnEvalWhite = {
            {0.0f,  0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
            {5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f},
            {1.0f, 1.0f, 2.0f, 3.0f, 3.0f, 2.0f, 1.0f, 1.0f},
            {0.5f, 0.5f, 1.0f, 2.5f, 2.5f, 1.0f, 0.5f, 0.5f},
            {0.0f, 0.0f, 0.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f},
            {0.5f, -0.5f, -1.0f, 0.0f, 0.0f, -1.0f, -0.5f, 0.5f},
            {0.5f, 1.0f, 1.0f, -2.0f, -2.0f, 1.0f, 1.0f, 0.5f},
            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}
    };

    private final float[][] pawnEvalBlack = reverseMatrix(pawnEvalWhite);

    private final float[][] knightEval = {
            {-5.0f, -4.0f, -3.0f, -3.0f, -3.0f, -3.0f, -4.0f, -5.0f},
            {-4.0f, -2.0f, 0.0f, 0.0f, 0.0f, 0.0f, -2.0f, -4.0f},
            {-3.0f, 0.0f, 1.0f, 1.5f, 1.5f, 1.0f, 0.0f, -3.0f},
            {-3.0f, 0.5f, 1.5f, 2.0f, 2.0f, 1.5f, 0.5f, -3.0f},
            {-3.0f, 0.0f, 1.5f, 2.0f, 2.0f, 1.5f, 0.0f, -3.0f},
            {3.0f, 0.5f, 1.0f, 1.5f, 1.5f, 1.0f, 0.5f, -3.0f},
            {-4.0f, -2.0f, 0.0f, 0.5f, 0.5f, 0.0f, -2.0f, -4.0f},
            {-5.0f, -4.0f, -3.0f, -3.0f, -3.0f, -3.0f, -4.0f, -5.0f}
    };

    private final float[][] bishopEvalWhite = {
            {-2.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -2.0f},
            {-1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f},
            {-1.0f, 0.0f, 0.5f, 1.0f, 1.0f, 0.5f, 0.0f, -1.0f},
            {-1.0f, 0.5f, 0.5f, 1.0f, 1.0f, 0.5f, 0.5f, -1.0f},
            {-1.0f, 0.0f, 1.0f,  1.0f,  1.0f, 1.0f, 0.0f, -1.0f},
            {1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f},
            {-1.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, -1.0f},
            {-2.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -2.0f}
    };

    private final float[][] bishopEvalBlack = reverseMatrix(bishopEvalWhite);

    private final float[][] rookEvalWhite = {
            {0.0f,  0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
            {0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.5f},
            {-0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f},
            {-0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f},
            {-0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f},
            {-0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f},
            {-0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f},
            {0.0f, 0.0f, 0.0f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f}
    };

    private final float[][] rookEvalBlack = reverseMatrix(rookEvalWhite);

    private final float[][] evalQueen = {
            {-2.0f, -1.0f, -1.0f, -0.5f, -0.5f, -1.0f, -1.0f, -2.0f},
            {-1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f},
            {-1.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.0f, -1.0f},
            {-0.5f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.0f, -0.5f},
            {0.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.0f, -0.5f},
            {-1.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.0f, -1.0f},
            {-1.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f},
            {-2.0f, -1.0f, -1.0f, -0.5f, -0.5f, -1.0f, -1.0f, -2.0f}
    };

    private final float[][] kingEvalWhite = {
            {-3.0f, -4.0f, -4.0f, -5.0f, -5.0f, -4.0f, -4.0f, -3.0f},
            {-3.0f, -4.0f, -4.0f, -5.0f, -5.0f, -4.0f, -4.0f, -3.0f},
            {-3.0f, -4.0f, -4.0f, -5.0f, -5.0f, -4.0f, -4.0f, -3.0f},
            {-3.0f, -4.0f, -4.0f, -5.0f, -5.0f, -4.0f, -4.0f, -3.0f},
            {-2.0f, -3.0f, -3.0f, -4.0f, -4.0f, -3.0f,-3.0f, -2.0f},
            {-1.0f, -2.0f, -2.0f, -2.0f, -2.0f, -2.0f, -2.0f, -1.0f},
            {2.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 2.0f},
            {2.0f, 3.0f, 1.0f, 0.0f, 0.0f, 1.0f, 3.0f, 2.0f}
    };

    private final float[][] kingEvalBlack = reverseMatrix(kingEvalWhite);

    private final ExecutorService aiThreadPool;
    private final int depth;
    private Color aiColor;
    private Color userColor;

    public AI(int depth, ThreadsMode threadsMode, Color color) {
        this.depth = depth;
        aiThreadPool = Executors.newFixedThreadPool((int) (Runtime.getRuntime().availableProcessors() * threadsMode.coefficient));

        aiColor = color;
        userColor = reverse(color);
    }

    public void setAIColor(Color aiColor) {
        this.aiColor = aiColor;
        userColor = reverse(aiColor);
    }

    public Color getAIColor() {
        return aiColor;
    }

    public void setUserColor(Color userColor) {
        this.userColor = userColor;
        aiColor = reverse(userColor);
    }

    public Color getUserColor() {
        return userColor;
    }

    private AtomicInteger countMinimaxThreads;
    private Map<Move, Integer> minimaxResult;

    public void getMove(Game game, AIListener callback) {
        countMinimaxThreads = new AtomicInteger(0);
        minimaxResult = new ConcurrentHashMap<>();
        Game cloneGame = game.cloneGame();

        Runnable task = () -> {
            try {
                int bestMove = Integer.MIN_VALUE;
                Move virtualAIMove = null;

                for (Move move : getAllMoves(cloneGame, aiColor)) {
                    cloneGame.makeMove(move);
                    getParallelMiniMax(cloneGame.cloneGame(), move, depth - 1, -10_000, 10_000, userColor);
                    countMinimaxThreads.incrementAndGet();
                    cloneGame.cancelMove();
                }

                while (countMinimaxThreads.get() != 0) {
                    Thread.yield();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.out);
                        return;
                    }
                }

                for (Map.Entry<Move, Integer> pair : minimaxResult.entrySet()) {
                    if (pair.getValue() >= bestMove) {
                        bestMove = pair.getValue();
                        virtualAIMove = pair.getKey();
                    }
                }

                if (virtualAIMove != null) {
                    callback.finish(virtualAIMove);
                }
            } catch (RejectedExecutionException e) {
                e.printStackTrace(System.out);
            }
        };
        aiThreadPool.execute(task);
    }

    public void interrupt() {
        aiThreadPool.shutdownNow();
    }

    private void getParallelMiniMax(Game cloneGame, Move move, int depth, int alpha, int beta, Color userColor) {
        Runnable task = () -> {
            try {
                minimaxResult.put(move, getMiniMax(cloneGame, depth, alpha, beta, userColor));
            } catch (InterruptedException | RejectedExecutionException e) {
                e.printStackTrace(System.out);
            }
            countMinimaxThreads.decrementAndGet();
        };
        aiThreadPool.execute(task);
    }


    private int evaluateBoard(Game game) {
        int totalEvaluation = 0;

        byte[][] matrix = game.getMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                totalEvaluation += evaluateFigure(game, j, i);
            }
        }
        return totalEvaluation;
    }

    private float evaluateFigure(Game game, int x, int y) {
        float eval = 0;

        if (game.isPawn(x, y)) {
            if (y == 7 || y == 0) eval =  90 + evalQueen[y][x];
            else eval = 10 + (game.getColor(x, y) == Color.BLACK ? pawnEvalBlack[y][x] : pawnEvalWhite[y][x]);
        } else if (game.isRook(x, y)) {
            eval = 50 + (game.getColor(x, y) == Color.BLACK ? rookEvalBlack[y][x] : rookEvalWhite[y][x]);
        } else if (game.isKnight(x, y)) {
            eval = 30 + knightEval[y][x];
        } else if (game.isBishop(x, y)) {
            eval =  30 + (game.getColor(x, y) == Color.BLACK ? bishopEvalBlack[y][x] : bishopEvalWhite[y][x]);
        } else if (game.isQueen(x, y)) {
            eval =  90 + evalQueen[y][x];
        } else if (game.isKing(x, y)) {
            eval = 900 + (game.getColor(x, y) == Color.BLACK ? kingEvalBlack[y][x] : kingEvalWhite[y][x]);
        }

        return game.getColor(x, y) == aiColor ? eval : -eval;
    }

    private int getMiniMax(Game cloneGame, int depth, int alpha, int beta, Color color) throws InterruptedException  {
        if (Thread.currentThread().isInterrupted()) throw new InterruptedException();

        if (depth == 0) {
            return evaluateBoard(cloneGame);
        }

        Array<Move> moves = getAllMoves(cloneGame, color);

        int bestMove;
        if (color == aiColor) {
            bestMove = Integer.MIN_VALUE;

            for (Move move : moves) {
                cloneGame.makeMove(move);

                int value = getMiniMax(cloneGame,depth - 1, alpha, beta, cloneGame.reverse(color));
                bestMove = Math.max(bestMove, value);

                cloneGame.cancelMove();

                alpha = Math.max(alpha, bestMove);
                if (beta <= alpha) {
                    return bestMove;
                }
            }
        } else {
            bestMove = Integer.MAX_VALUE;

            for (Move move : moves) {
                cloneGame.makeMove(move);

                int value = getMiniMax(cloneGame, depth - 1, alpha, beta, cloneGame.reverse(color));
                bestMove = Math.min(bestMove, value);

                cloneGame.cancelMove();
            }

            beta = Math.min(beta, bestMove);
            if (beta <= alpha) {
                return bestMove;
            }
        }

        return bestMove;
    }

    private Array<Move> getAllMoves(Game game, Color color) {
        Array<Move> moves = new Array<>();
        byte[][] matrix = game.getMatrix();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (game.getColor(j, i) == color) {
                    moves.addAll(game.getMoves(j, i));
                }
            }
        }

        return moves;
    }

    private float[][] reverseMatrix(float[][] matrix) {
        float[][] reversedMatrix = new float[matrix.length][matrix[0].length];

        for (int i = matrix.length - 1; i >= 0; i--) {
            System.arraycopy(matrix[i], 0, reversedMatrix[matrix.length - 1 - i], 0, matrix[i].length);
        }
        return reversedMatrix;
    }

    private Color reverse(Color first) {
        return first == Color.BLACK ? Color.WHITE : Color.BLACK;
    }

    public enum ThreadsMode {
        HALF_THREADS(0.5f),
        MORE_THREADS(0.75f),
        ALL_THREADS(1);

        final float coefficient;
        ThreadsMode(float coefficient) {
            this.coefficient = coefficient;
        }
    }
}
