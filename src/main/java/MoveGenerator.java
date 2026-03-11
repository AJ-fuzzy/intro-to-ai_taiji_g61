import java.util.ArrayList;
import java.util.List;

public final class MoveGenerator {
    private MoveGenerator() {}

    public static List<Move> generateMoves(TaijiState s) {
        List<Move> moves = new ArrayList<>();

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int a = r * 9 + c;
                if (!s.isEmpty(a)) continue;

                // right neighbor
                if (c + 1 < 9) {
                    int b = r * 9 + (c + 1);
                    if (s.isEmpty(b)) {
                        moves.add(new Move(a, b, Color.LIGHT));
                        moves.add(new Move(a, b, Color.DARK));
                    }
                }

                // down neighbor
                if (r + 1 < 9) {
                    int b = (r + 1) * 9 + c;
                    if (s.isEmpty(b)) {
                        moves.add(new Move(a, b, Color.LIGHT));
                        moves.add(new Move(a, b, Color.DARK));
                    }
                }
            }
        }

        return moves;
    }

    public static boolean hasAnyMove(TaijiState s) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int a = r * 9 + c;
                if (!s.isEmpty(a)) continue;

                if (c + 1 < 9 && s.isEmpty(r * 9 + (c + 1))) return true;
                if (r + 1 < 9 && s.isEmpty((r + 1) * 9 + c)) return true;
            }
        }
        return false;
    }
}