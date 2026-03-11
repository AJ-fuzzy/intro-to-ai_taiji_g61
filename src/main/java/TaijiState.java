public final class TaijiState {
    private long lightLo, lightHi; // represent cells 0-63 and 64-80 holding white
    private long darkLo, darkHi;// represent cells 0-63 and 64-80 holding black
    private Color sideToMove; // useful for search, even though both place same piece types

    public TaijiState() {
        this.sideToMove = Color.LIGHT; // Light starts
    }

    public Color sideToMove() {
        return sideToMove;
    }

    public boolean isEmpty(int idx) {
        return !getBit(lightLo, lightHi, idx) && !getBit(darkLo, darkHi, idx);
    }

    public Color colorAt(int idx) {
        if (getBit(lightLo, lightHi, idx)) return Color.LIGHT;
        if (getBit(darkLo, darkHi, idx)) return Color.DARK;
        return null;
    }

    public boolean isTerminal() {
        return !MoveGenerator.hasAnyMove(this);
    }

    public void applyMove(Move move) {
        int a = move.a();
        int b = move.b();

        if (!isEmpty(a) || !isEmpty(b)) {
            throw new IllegalArgumentException("Cells must be empty");
        }
        if (!areAdjacent(a, b)) {
            throw new IllegalArgumentException("Cells must be be orthogonally adjacent");
        }

        Color ca = move.colorAtA();
        Color cb = ca.opponent();

        setColor(a, ca);
        setColor(b, cb);

        sideToMove = sideToMove.opponent();
    }

    public TaijiState copy() {
        TaijiState s = new TaijiState();
        s.lightLo = this.lightLo;
        s.lightHi = this.lightHi;
        s.darkLo = this.darkLo;
        s.darkHi = this.darkHi;
        s.sideToMove = this.sideToMove;
        return s;
    }

    private void setColor(int idx, Color c) {
        if (c == Color.LIGHT) {
            long[] pair = setBit(lightLo, lightHi, idx);
            lightLo = pair[0];
            lightHi = pair[1];
        } else {
            long[] pair = setBit(darkLo, darkHi, idx);
            darkLo = pair[0];
            darkHi = pair[1];
        }
    }

    static boolean areAdjacent(int a, int b) {
        int ra = a / 9, ca = a % 9;
        int rb = b / 9, cb = b % 9;
        return Math.abs(ra - rb) + Math.abs(ca - cb) == 1;
    }

    static boolean getBit(long lo, long hi, int idx) {
        if (idx < 64) return ((lo >>> idx) & 1L) != 0;
        return ((hi >>> (idx - 64)) & 1L) != 0;
    }

    static long[] setBit(long lo, long hi, int idx) {
        if (idx < 64) lo |= (1L << idx);
        else hi |= (1L << (idx - 64));
        return new long[]{lo, hi};
    }

    public long lightLo() { return lightLo; }
    public long lightHi() { return lightHi; }
    public long darkLo() { return darkLo; }
    public long darkHi() { return darkHi; }
}