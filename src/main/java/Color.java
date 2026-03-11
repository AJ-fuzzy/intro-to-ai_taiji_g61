public enum Color {
    LIGHT, DARK;

    public Color opponent() {
        return this == LIGHT ? DARK : LIGHT;
    }
}