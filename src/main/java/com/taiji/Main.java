package com.taiji;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TaijiBoard board = new TaijiBoard(6); // 6x6 is a good standard size
        MinimaxSolver ai = new MinimaxSolver(4); // Search depth
        Scanner scanner = new Scanner(System.in);

        int currentPlayer = 1; // 1 = Black, 2 = White

        while (true) {
            printBoard(board);
            if (currentPlayer == 1) {
                System.out.println("Your turn (Black). Enter r1 c1 r2 c2:");
                int r1 = scanner.nextInt(), c1 = scanner.nextInt();
                int r2 = scanner.nextInt(), c2 = scanner.nextInt();

                if (board.isValidMove(r1, c1, r2, c2)) {
                    board.placeTile(r1, c1, r2, c2, 1);
                    currentPlayer = 2;
                } else {
                    System.out.println("Invalid move! Try again.");
                }
            } else {
                System.out.println("AI is thinking...");
                int[] move = ai.findBestMove(board, 2);
                if (move == null) break; // No moves left
                board.placeTile(move[0], move[1], move[2], move[3], 2);
                currentPlayer = 1;
            }
        }
        System.out.println("Game Over!");
    }

    private static void printBoard(TaijiBoard board) {
        for (int[] row : board.getGrid()) {
            for (int cell : row) {
                String symbol = (cell == 0) ? "." : (cell == 1 ? "B" : "W");
                System.out.print(symbol + " ");
            }
            System.out.println();
        }
    }
}