package com.taiji;

import java.util.*;

public class MinimaxSolver {
    private final int maxDepth;

    public MinimaxSolver(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public int[] findBestMove(TaijiBoard board, int aiColor) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int[] move : getPossibleMoves(board)) {
            board.placeTile(move[0], move[1], move[2], move[3], aiColor);
            int score = minimax(board, maxDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false, aiColor);
            board.removeTile(move[0], move[1], move[2], move[3]);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int minimax(TaijiBoard board, int depth, int alpha, int beta, boolean isMax, int aiColor) {
        if (depth == 0) return evaluate(board, aiColor);

        List<int[]> moves = getPossibleMoves(board);
        if (moves.isEmpty()) return evaluate(board, aiColor);

        int opponentColor = (aiColor == 1) ? 2 : 1;
        int turnColor = isMax ? aiColor : opponentColor;

        int extremeEval = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int[] m : moves) {
            board.placeTile(m[0], m[1], m[2], m[3], turnColor);
            int eval = minimax(board, depth - 1, alpha, beta, !isMax, aiColor);
            board.removeTile(m[0], m[1], m[2], m[3]);

            if (isMax) {
                extremeEval = Math.max(extremeEval, eval);
                alpha = Math.max(alpha, eval);
            } else {
                extremeEval = Math.min(extremeEval, eval);
                beta = Math.min(beta, eval);
            }
            if (beta <= alpha) break;
        }
        return extremeEval;
    }

    private int evaluate(TaijiBoard board, int aiColor) {
        int oppColor = (aiColor == 1) ? 2 : 1;
        List<Integer> my = board.getClusterSizes(aiColor);
        List<Integer> opp = board.getClusterSizes(oppColor);

        // Scoring: Largest group - Second largest group
        int myScore = my.get(0) - (my.size() > 1 ? my.get(1) : 0);
        int oppScore = opp.get(0) - (opp.size() > 1 ? opp.get(1) : 0);
        return myScore - oppScore;
    }

    private List<int[]> getPossibleMoves(TaijiBoard board) {
        List<int[]> moves = new ArrayList<>();
        int n = board.getSize();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board.getGrid()[i][j] == 0) {
                    if (board.isValidMove(i, j, i, j + 1)) moves.add(new int[]{i, j, i, j + 1});
                    if (board.isValidMove(i, j, i + 1, j)) moves.add(new int[]{i, j, i + 1, j});
                }
            }
        }
        return moves;
    }
}