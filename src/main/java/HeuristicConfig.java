public class HeuristicConfig {
    public final String name;
    public final double largestGroup;      // difference in largest connected group
    public final double weightedConn;      // sum of group_size^2 (rewards consolidation)
    public final double mobility;          // expansion frontier (empty cells adjacent to own color)
    public final double centrality;        // cells closer to center score higher
    public final double blocking;          // empty cells adjacent to own color but NOT opponent's

    public HeuristicConfig(String name, double largestGroup, double weightedConn,
                           double mobility, double centrality, double blocking) {
        this.name = name;
        this.largestGroup = largestGroup;
        this.weightedConn = weightedConn;
        this.mobility = mobility;
        this.centrality = centrality;
        this.blocking = blocking;
    }

    // Baseline: only the win condition
    public static HeuristicConfig defaultConfig() {
        return new HeuristicConfig("Default", 10, 0, 0, 0, 0);
    }

    // Prioritises cutting off opponent's expansion
    public static HeuristicConfig aggressive() {
        return new HeuristicConfig("Aggressive", 10, 0, 0, 0, 3);
    }

    // Prioritises owning the center and maintaining growth potential
    public static HeuristicConfig positional() {
        return new HeuristicConfig("Positional", 10, 0, 2, 0.3, 0);
    }

    // Mix of all components
    public static HeuristicConfig balanced() {
        return new HeuristicConfig("Balanced", 10, 0.1, 1, 0.15, 1.5);
    }

    public static HeuristicConfig fromChoice(int choice) {
        return switch (choice) {
            case 2 -> aggressive();
            case 3 -> positional();
            case 4 -> balanced();
            default -> defaultConfig();
        };
    }

    public static void printMenu() {
        System.out.println("  (1) Default    - largest group only");
        System.out.println("  (2) Aggressive - largest group + blocking opponent");
        System.out.println("  (3) Positional - largest group + mobility + centrality");
        System.out.println("  (4) Balanced   - all components");
    }
}
