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

    private int dynamicDepth(Board board) {
        int totalCells = board.getSize() * board.getSize();
        int emptyCells = board.countCells(Board.Cell.EMPTY);
        double fillRatio = 1.0 - (double) emptyCells / totalCells;

        if (fillRatio < 0.6) return 3;
        if (fillRatio < 0.8) return 4;
        if (fillRatio < 0.85) return 5;
        return emptyCells / 2; // endgame: search entire remaining tree
    }

    public Board getBestMove(Board position, int aiPlayer) {
        int depth = dynamicDepth(position);
        System.out.println("Search depth: " + depth);
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

    // Minimax with alpha-beta pruning
    private int minimax(Board position, int depth, boolean maximizingPlayer, int alpha, int beta, int aiPlayer) {
        if (depth == 0 || position.isFull())
            return evaluate(position, aiPlayer);

        List<Board> children = getChildren(position);
        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Board child : children){
                maxEval = Math.max(maxEval, minimax(child, depth - 1, false, alpha, beta, aiPlayer)); // Pruning because move is good so opponent will not allow us to reach it anyways
                alpha = Math.max(alpha, maxEval);
                if(beta <= alpha){
                    break;
                }
            }
            return maxEval;

        } else {
            int minEval = Integer.MAX_VALUE;
            for (Board child : children){
                minEval = Math.min(minEval, minimax(child, depth - 1, true, alpha, beta, aiPlayer)); // Pruning becasuse move is bad so we dont need to evaluate the rest of the tree
                beta = Math.min(beta,minEval);
                if(beta <= alpha){
                break;
                }
            }
            return minEval;
        }
    }
    
    // Simple heuristic: difference in largest groups
    private int evaluate(Board board, int aiPlayer) {
        if (aiPlayer == 2){
            return board.largestGroup(Board.Cell.WHITE) - board.largestGroup(Board.Cell.BLACK);
        } else {
            return board.largestGroup(Board.Cell.BLACK) - board.largestGroup(Board.Cell.WHITE);
        }
    }
}