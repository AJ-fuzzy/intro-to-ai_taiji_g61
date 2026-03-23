import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        int boardSize = 7;

        try (Scanner sc = new Scanner(System.in)) {
            if (args.length > 0) {
                try {
                    boardSize = Integer.parseInt(args[0]);
                    if (boardSize < 2 || boardSize > 19) {
                        System.out.println("Board size must be between 2 and 19. Using default 7.");
                        boardSize = 7;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid board size argument. Using default 7.");
                }
            } else {
                System.out.print("Board size (default 7, press Enter to use default): ");
                String input = sc.nextLine().trim();
                if (!input.isEmpty()) {
                    try {
                        boardSize = Integer.parseInt(input);
                        if (boardSize < 2 || boardSize > 19) {
                            System.out.println("Out of range. Using default 7.");
                            boardSize = 7;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Using default 7.");
                    }
                }
            }

            System.out.print("Mode: (1) Human vs AI  (2) AI vs AI  (3) Human vs Human: ");
            String modeChoice = sc.nextLine().trim();
            int aiPlayer = 0;
            if (modeChoice.equals("1")) {
                System.out.print("AI plays as player 1 (Black) or 2 (White)? (1/2): ");
                String playerChoice = sc.nextLine().trim();
                aiPlayer = playerChoice.equals("1") ? 1 : 2;
            } else if (modeChoice.equals("2")) {
                aiPlayer = 3; // both players are AI
            }

            Game game = new Game(boardSize, sc, aiPlayer);
            game.run();
        }
    }
}
