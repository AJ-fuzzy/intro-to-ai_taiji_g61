# Getting started
Navigate into the project root and run:
mvn clean compile


Then run:
mvn exec:java "-Dexec.mainClass=com.taiji.Main"

# How to play
The Grid: You'll see a 6x6 grid of dots.

Input: When it says Enter r1 c1 r2 c2, you are defining the two squares for your domino.

Example: 0 0 0 1 places Black at (0,0) and White at (0,1).

The AI: After your move, the console will say "AI is thinking..." while it runs the _____ algorithm to find the best counter-move.
