import java.util.ArrayDeque;

public final class Scorer {
    private static final int N = 9;

    private Scorer() {}

    public static int score(TaijiState s, Color color) {
        boolean[] visited = new boolean[81];
        int best1 = 0, best2 = 0;

        for (int i = 0; i < 81; i++) {
            if (visited[i]) continue;
            if (s.colorAt(i) != color) continue;

            int size = bfsComponentSize(s, i, color, visited);
            if (size > best1) {
                best2 = best1;
                best1 = size;
            } else if (size > best2) {
                best2 = size;
            }
        }

        return best1 + best2;
    }

    public static int outcome(TaijiState s) {
        int light = score(s, Color.LIGHT);
        int dark = score(s, Color.DARK);

        if (light > dark) return 1;
        if (dark >= light) return -1; // Dark wins ties
        throw new IllegalStateException();
    }

    private static int bfsComponentSize(TaijiState s, int start, Color color, boolean[] visited) {
        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.add(start);
        visited[start] = true;
        int count = 0;

        while (!q.isEmpty()) {
            int x = q.removeFirst();
            count++;

            int r = x / N, c = x % N;

            if (r > 0) tryVisit(s, (r - 1) * N + c, color, visited, q);
            if (r + 1 < N) tryVisit(s, (r + 1) * N + c, color, visited, q);
            if (c > 0) tryVisit(s, r * N + (c - 1), color, visited, q);
            if (c + 1 < N) tryVisit(s, r * N + (c + 1), color, visited, q);
        }

        return count;
    }

    private static void tryVisit(TaijiState s, int i, Color color, boolean[] visited, ArrayDeque<Integer> q) {
        if (!visited[i] && s.colorAt(i) == color) {
            visited[i] = true;
            q.addLast(i);
        }
    }
}