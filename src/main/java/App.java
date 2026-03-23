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

            AI ai1 = null, ai2 = null;
            if (modeChoice.equals("1")) {
                System.out.print("AI plays as player 1 (Black) or 2 (White)? (1/2): ");
                String playerChoice = sc.nextLine().trim();
                AI aiInstance = new AI(pickHeuristic(sc, "AI"));
                if (playerChoice.equals("1")) ai1 = aiInstance;
                else                          ai2 = aiInstance;
            } else if (modeChoice.equals("2")) {
                ai1 = new AI(pickHeuristic(sc, "Player 1 AI"));
                ai2 = new AI(pickHeuristic(sc, "Player 2 AI"));
            }

            Game game = new Game(boardSize, sc, ai1, ai2);
            game.run();
        }
    }

    private static HeuristicConfig pickHeuristic(java.util.Scanner sc, String label) {
        System.out.println(label + " heuristic:");
        HeuristicConfig.printMenu();
        System.out.print("  Choice (default 1): ");
        int choice = 1;
        try { choice = Integer.parseInt(sc.nextLine().trim()); } catch (NumberFormatException e) {}
        HeuristicConfig cfg = HeuristicConfig.fromChoice(choice);
        System.out.println("  -> " + cfg.name);
        return cfg;
    }
}
