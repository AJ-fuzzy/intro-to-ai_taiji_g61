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

    public Board getBestMove(Board position, int depth) {
        List<Board> children = getChildren(position);
        Board best = null;
        int maxEval = Integer.MIN_VALUE;
        for (Board child : children) {
            int eval = minimax(child, depth - 1, false);
            if (eval > maxEval) {
                maxEval = eval;
                best = child;
            }
        }
        return best;
    }


    private int minimax(Board position, int depth, boolean maximizingPlayer) {
        if (depth == 0 || position.isFull())
            return evaluate(position);

        List<Board> children = getChildren(position);
        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Board child : children)
                maxEval = Math.max(maxEval, minimax(child, depth - 1, false));
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Board child : children)
                minEval = Math.min(minEval, minimax(child, depth - 1, true));
            return minEval;
        }
    }

    private int evaluate(Board board) {
        return board.largestGroup(Board.Cell.WHITE) - board.largestGroup(Board.Cell.BLACK);
    }
}