import java.util.ArrayList;
import java.util.List;

public class AI {

    public List<Board> getChildren(Board position) {
        List<Board> children = new ArrayList<>();
        int size = position.getSize();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int[][] neighbors = {{i, j + 1}, {i + 1, j}};
                for (int[] nb : neighbors) {
                    int r2 = nb[0], c2 = nb[1];
                    Board copy1 = new Board(position);
                    if (copy1.placeDomino(i, j, Board.Cell.WHITE, r2, c2, Board.Cell.BLACK))
                        children.add(copy1);
                    Board copy2 = new Board(position);
                    if (copy2.placeDomino(i, j, Board.Cell.BLACK, r2, c2, Board.Cell.WHITE))
                        children.add(copy2);
                }
            }
        }
        return children;
    }

    public Board getBestMove(Board position, int depth, int aiPlayer) {
        List<Board> children = getChildren(position);
        Board best = null;
        int maxEval = Integer.MIN_VALUE;
        for (Board child : children) {
            int eval = minimax(child, depth - 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE, aiPlayer);
            if (eval > maxEval) {
                maxEval = eval;
                best = child;
            }
        }
        return best;
    }


    private int minimax(Board position, int depth, boolean maximizingPlayer, int alpha, int beta, int aiPlayer) {
        if (depth == 0 || position.isFull())
            return evaluate(position, aiPlayer);

        List<Board> children = getChildren(position);
        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Board child : children){
                maxEval = Math.max(maxEval, minimax(child, depth - 1, false, alpha, beta, aiPlayer));
                alpha = Math.max(alpha, maxEval);
                if(beta <= alpha){
                    break;
                }
            }
            return maxEval;

        } else {
            int minEval = Integer.MAX_VALUE;
            for (Board child : children){
                minEval = Math.min(minEval, minimax(child, depth - 1, true, alpha, beta, aiPlayer));
                beta = Math.min(beta,minEval);
                if(beta <= alpha){
                break;
                }
            }
            return minEval;
        }
    }

    private int evaluate(Board board, int aiPlayer) {
        if (aiPlayer == 2){
            return board.largestGroup(Board.Cell.WHITE) - board.largestGroup(Board.Cell.BLACK);
        } else {
            return board.largestGroup(Board.Cell.BLACK) - board.largestGroup(Board.Cell.WHITE);
        }
    }

    //Visual AID!
    public void printTree(Board position, int depth, int aiPlayer) {
        minimaxDebug(position, depth, true, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, aiPlayer);
    }

    private int minimaxDebug(Board position, int depth, boolean maximizingPlayer, int alpha, int beta, int indent, int aiPlayer) {
        String pad = "  ".repeat(indent);
        String player = maximizingPlayer ? "MAX" : "MIN";

        if (depth == 0 || position.isFull()) {
            int score = evaluate(position, aiPlayer);
            System.out.println(pad + "[" + player + "] LEAF score=" + score);
            return score;
        }

        List<Board> children = getChildren(position);
        System.out.println(pad + "[" + player + "] alpha=" + alpha + " beta=" + beta + " children=" + children.size());

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int i = 0; i < children.size(); i++) {
                System.out.println(pad + "  child " + i + ":");
                maxEval = Math.max(maxEval, minimaxDebug(children.get(i), depth - 1, false, alpha, beta, indent + 2, aiPlayer));
                alpha = Math.max(alpha, maxEval);
                if (beta <= alpha) {
                    System.out.println(pad + "  *** PRUNED remaining " + (children.size() - i - 1) + " children (beta=" + beta + " <= alpha=" + alpha + ") ***");
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int i = 0; i < children.size(); i++) {
                System.out.println(pad + "  child " + i + ":");
                minEval = Math.min(minEval, minimaxDebug(children.get(i), depth - 1, true, alpha, beta, indent + 2, aiPlayer));
                beta = Math.min(beta, minEval);
                if (beta <= alpha) {
                    System.out.println(pad + "  *** PRUNED remaining " + (children.size() - i - 1) + " children (beta=" + beta + " <= alpha=" + alpha + ") ***");
                    break;
                }
            }
            return minEval;
        }
    }
}