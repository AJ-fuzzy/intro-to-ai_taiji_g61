🏗️ Phase 1: Architecture & The Grid (Week 1)The goal this week is to define how the game "thinks." You need a coordinate system and a way to represent the board.
Person A (Lead Architect): Design the Board class. Use a 2D array (e.g., char[][]) to represent the $6 \times 6$ grid. Define the "Exit" point.
Person B (Entity Manager): Create the Vehicle class. It needs attributes like id, length, orientation (Horizontal/Vertical), and headPosition $(x, y)$.
Person C (Input Specialist): Build the command parser. How will players move? (e.g., A R 2 for "Car A, Right, 2 spaces").
Person D (Renderer): Create the Display class. Write the logic to print the 2D array to the terminal with colors (using ANSI escape codes) so players can distinguish cars.

🚦 Phase 2: Movement Logic & Rules (Week 2)This is where the game actually becomes a game. You need to prevent cars from jumping over each other or leaving the board.
Person A: Write the isValidMove() logic. Check for collisions and boundaries.
Person B: Implement the actual movement math. If a vertical car moves "Up 2," how do its coordinates change?
Person C: Create a GameManager to track the state. Is the Red Car at the exit? If yes, trigger the win condition.
Person D: Implement a "Move History" using a Stack. This will allow for an "Undo" feature later.

🧩 Phase 3: Level Design & Persistence (Week 3)A game with only one setup is boring. You need to load different puzzles.
Person A & B: Design a .txt or .json file format to store levels.Example: A, H, 2, 0, 0 (Car A, Horizontal, Length 2, at row 0, col 0).
Person C: Write a LevelLoader class to read these files and populate the Board.
Person D: Add a "Level Selection" menu and a "Moves Counter" to track player performance.

💅 Phase 4: Polish & "The Extra Mile" (Week 4)Final testing and adding the features that make the project stand out.
Person A: Bug hunting. Fix "teleporting" cars or crashes when players enter invalid text.
Person B: Add a "Reset" function and improve the UI (maybe a "help" screen showing controls).
Person C: Stretch Goal: Implement a simple Breadth-First Search (BFS) solver. If the player is stuck, can the AI calculate the minimum moves to win?
Person D: Documentation and Final Demo prep. Ensure the code is commented and the README is clean.

🛠️ Suggested Tech StackLanguage: 
Java 17+
Version Control: Git (GitHub or GitLab is a must for 4 people).
Build Tool: Maven or Gradle (to manage dependencies if you use JSON).
UI: ANSI colors for the terminal (e.g., \u001B[31m for Red).
