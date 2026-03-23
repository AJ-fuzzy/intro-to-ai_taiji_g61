import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AI {

    private final HeuristicConfig config;
    private final Map<String, Integer> memo = new HashMap<>();

    public AI(HeuristicConfig config) {
        this.config = config;
    }

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
        return emptyCells / 2; 
    }

    public Board getBestMove(Board position, int aiPlayer) {
        int depth = dynamicDepth(position);
        System.out.println("Search depth: " + depth);
        
        // Clear cache at the start of a new turn to save memory 
        // and ensure evaluation is relevant to the current search depth.
        memo.clear(); 
        
        List<Board> children = getChildren(position);
        Board best = null;
        int maxEval = Integer.MIN_VALUE;
        for (Board child : children) {
            int eval = minimax(child, depth - 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE, aiPlayer);
            if (eval >= maxEval) {
                maxEval = eval;
                best = child;
            }
        }
        return best;
    }

    private int minimax(Board position, int depth, boolean maximizingPlayer, int alpha, int beta, int aiPlayer) {
        // Generate a unique key for the current board state
        String boardKey = position.toString() + depth + maximizingPlayer;
        if (memo.containsKey(boardKey)) {
            return memo.get(boardKey);
        }

        if (depth == 0 || position.isFull()) {
            int score = evaluate(position, aiPlayer);
            memo.put(boardKey, score);
            return score;
        }

        List<Board> children = getChildren(position);
        int resultEval;

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Board child : children){
                maxEval = Math.max(maxEval, minimax(child, depth - 1, false, alpha, beta, aiPlayer));
                alpha = Math.max(alpha, maxEval);
                if(beta <= alpha){
                    break;
                }
            }
            resultEval = maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Board child : children){
                minEval = Math.min(minEval, minimax(child, depth - 1, true, alpha, beta, aiPlayer));
                beta = Math.min(beta, minEval);
                if(beta <= alpha){
                    break;
                }
            }
            resultEval = minEval;
        }

        memo.put(boardKey, resultEval);
        return resultEval;
    }
    
    private int evaluate(Board board, int aiPlayer) {
        Board.Cell my  = aiPlayer == 2 ? Board.Cell.WHITE : Board.Cell.BLACK;
        Board.Cell opp = aiPlayer == 2 ? Board.Cell.BLACK : Board.Cell.WHITE;

        double score = 0;
        score += config.largestGroup  * (board.largestGroup(my)         - board.largestGroup(opp));
        score += config.weightedConn  * (board.weightedConnectivity(my) - board.weightedConnectivity(opp));
        score += config.mobility      * (board.expansionFrontier(my)    - board.expansionFrontier(opp));
        score += config.centrality    * board.centralityScore(my);
        score += config.blocking      * board.influence(my, opp);
        return (int) score; 
    }
}