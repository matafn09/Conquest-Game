# 👑 Conquest. A Queens Puzzle Game

## What It Is
A single-player logic puzzle built with Java Swing, inspired by LinkedIn's "Queens" game. You place crowns on a grid so that no two crowns share a row, column, or touch diagonally — and each colored region on the board must contain exactly one crown. Solve it, and the puzzle grows to a bigger, harder grid.

## Technologies Involved
•	**Java** — the language the whole project is written in
•	**Swing** — builds the window, header, status label, and reset button
•	**AWT / Graphics2D** — handles the custom 2D drawing of the grid, colored regions, crowns, and X-markers


## Features
•	Procedurally generated puzzles that are always guaranteed to be solvable
•	Colored regions that shape each puzzle differently every time
•	Click-to-cycle cells: empty → crown → X-mark → empty (the X lets you mark "not here" while solving)
•	Real-time rule checking — conflicts are detected the moment you place a crown
•	Win detection with a success popup
•	Progressive difficulty — each win increases the grid size (up to 9x9)
•	"New Puzzle" button to reset at any time


## Process
The project is split into two responsibilities:
1. **The window (`ConquestGame`)**, a `JFrame` that holds the header (level display + reset button) and hosts the game canvas.
2. **The game logic and rendering (`QueensCanvas`)**, a `JPanel` that generates each puzzle, listens for clicks, checks the rules, and draws everything.

Puzzle generation works in two steps. First, the game randomly picks valid crown positions (no two touching, like a solved board) to use as "seeds." Then it flood-fills color outward from each seed, each empty cell has an 80% chance of joining a neighboring region — which is what gives every puzzle its organic, irregular shapes instead of plain uniform squares.

Rule-checking happens after every click: the game scans the whole board, confirms no two crowns share a row, column, or diagonal neighbor, and confirms each colored region has exactly one crown, before declaring the puzzle won.

## What I Learned
•	Building a desktop GUI with Java Swing, including custom components and layout management
•	Drawing custom 2D graphics and shapes with `Graphics2D`
•	Handling user input with `MouseListener` and `ActionListener`
•	Designing a procedural generation algorithm (region flood-fill) to create solvable puzzles from scratch
•	Structuring constraint-checking logic (rows, columns, diagonals, regions) cleanly


## How It Can Be Improved
•	Add a timer and scoring system
•	Add an undo button
•	Visually highlight exactly which crowns are in conflict, instead of just flagging that one exists
•	Support more than 9 regions with a larger color palette
•	Add a hint system for stuck players
•	Save/resume progress between sessions


## Running the Project
1. Install the **Java JDK** if you don't already have it.
2. Keep `ConquestGame.java` and `Project2Runner.java` in the same folder.
3. Compile:
   ```
   javac ConquestGame.java Project2Runner.java
   ```
4. Run:
   ```
   java Project2Runner
   ```

## Demo Video
<img src="https://raw.githubusercontent.com/matafn09/Conquest-Game/main/Conquest_game_vid.gif" width="600" alt="Conquest Game Demo">
---
⭐️ A project by [Fernand Mata](https://github.com/matafn09)
