import java.util.Scanner;

public class Game {
    private final Board board;
    private final Scanner scanner;
    private int currentPlayer; // 1 = Black, 2 = White
    private final AI ai;
    private final int aiPlayer;
    private final int aiDepth;

    public Game(int boardSize, Scanner scanner, int aiPlayer, int aiDepth) {
        this.board = new Board(boardSize);
        this.scanner = scanner;
        this.currentPlayer = 1;
        this.ai = new AI();
        this.aiPlayer = aiPlayer;
        this.aiDepth = aiDepth;
    }

    public void run() {
        System.out.println("=== TAIJI ===");
        System.out.println("Players alternate placing a domino (1x2 tile).");
        System.out.println("Each domino has one Black half and one White half.");
        System.out.println("At the end, the largest connected group of your color wins.\n");

        while (!board.isGameOver()) {
            board.print();
            printScores();

            if (currentPlayer == aiPlayer) {
                System.out.println("\nAI is thinking...");
                long start = System.currentTimeMillis();
                Board next = ai.getBestMove(board, aiDepth, aiPlayer);
                if (next == null){
                    break;
                }
                board.copyFrom(next);
                long elapsed = System.currentTimeMillis() - start;
                System.out.println("AI placed its domino");
                System.out.println("Elapsed time: " + String.format("%d.%03d", elapsed / 1000, elapsed % 1000) + "s");

            } else {

                System.out.printf("\nPlayer %d's turn (%s)\n",
                    currentPlayer, currentPlayer == 1 ? "prefers Black" : "prefers White");
                System.out.println("Place a domino. Input format: row1 col1 row2 col2 firstColor");
                System.out.println("  firstColor: B = Black on (row1,col1), W = White on (row1,col1)");
                System.out.println("  (row2,col2) must be adjacent to (row1,col1)");
                System.out.print("> ");

                if (!readAndPlace()) {
                    System.out.println("Invalid placement. Try again.\n");
                    continue;
                }
            }

            currentPlayer = (currentPlayer == 1) ? 2 : 1;
        }

        board.print();
        printFinalResult();
    }

    private boolean readAndPlace() {
        try {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) return false;
            String[] parts = line.split("\\s+");
            if (parts.length != 5) {
                System.out.println("  Expected 5 values: row1 col1 row2 col2 firstColor");
                return false;
            }
            int r1 = Integer.parseInt(parts[0]);
            int c1 = Integer.parseInt(parts[1]);
            int r2 = Integer.parseInt(parts[2]);
            int c2 = Integer.parseInt(parts[3]);
            String colorStr = parts[4].toUpperCase();

            Board.Cell color1, color2;
            if (colorStr.equals("B")) {
                color1 = Board.Cell.BLACK;
                color2 = Board.Cell.WHITE;
            } else if (colorStr.equals("W")) {
                color1 = Board.Cell.WHITE;
                color2 = Board.Cell.BLACK;
            } else {
                System.out.println("  firstColor must be B or W.");
                return false;
            }

            boolean ok = board.placeDomino(r1, c1, color1, r2, c2, color2);
            if (!ok) {
                System.out.println("  Placement failed: cells out of bounds, occupied, or not adjacent.");
            }
            return ok;
        } catch (NumberFormatException e) {
            System.out.println("  Invalid number format.");
            return false;
        }
    }

    public boolean placeFromAI(int r1, int c1, int r2, int c2, String colorStr) {
        Board.Cell color1, color2;
        if (colorStr.equals("B")) {
            color1 = Board.Cell.BLACK;
            color2 = Board.Cell.WHITE;
        } else if (colorStr.equals("W")) {
            color1 = Board.Cell.WHITE;
            color2 = Board.Cell.BLACK;
        } else {
            return false;
        }
        return board.placeDomino(r1, c1, color1, r2, c2, color2);
    }

    private void printScores() {
        int blackGroup = board.largestGroup(Board.Cell.BLACK);
        int whiteGroup = board.largestGroup(Board.Cell.WHITE);
        int blackCells = board.countCells(Board.Cell.BLACK);
        int whiteCells = board.countCells(Board.Cell.WHITE);
        System.out.printf("  Black: largest group = %d  (total cells = %d)\n", blackGroup, blackCells);
        System.out.printf("  White: largest group = %d  (total cells = %d)\n", whiteGroup, whiteCells);

    }

    private void printFinalResult() {
        int blackGroup = board.largestGroup(Board.Cell.BLACK);
        int whiteGroup = board.largestGroup(Board.Cell.WHITE);

        System.out.println("\n=== GAME OVER ===");
        System.out.printf("Black's largest connected group: %d\n", blackGroup);
        System.out.printf("White's largest connected group: %d\n", whiteGroup);


        if (blackGroup > whiteGroup) {
            System.out.println(">> Black wins!");
        } else if (whiteGroup > blackGroup) {
            System.out.println(">> White wins!");
        } else {
            // Tiebreak: count total cells
            int blackCells = board.countCells(Board.Cell.BLACK);
            int whiteCells = board.countCells(Board.Cell.WHITE);
            System.out.printf("Tie on largest group! Tiebreak by total cells: Black=%d, White=%d\n",
                blackCells, whiteCells);
            if (blackCells > whiteCells) System.out.println(">> Black wins!");
            else if (whiteCells > blackCells) System.out.println(">> White wins!");
            else System.out.println(">> It's a draw!");
        }
    }

    public Board getBoard(){
        return board;
    }
}
