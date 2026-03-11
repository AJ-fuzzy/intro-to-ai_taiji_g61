public record Move(int a, int b, Color colorAtA) {
    // a and b are adjacent cell indices [0..80]
    // if colorAtA == LIGHT, then b is DARK, and vice versa
}