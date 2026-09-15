# 👑 Conquest. A Queens Puzzle Game

## What It Is
A single-player logic puzzle built with Java Swing, inspired by LinkedIn's "Queens" game. You place crowns on a grid so that no two crowns share a row, column, or touch diagonally, and each colored region on the board must contain exactly one crown. Solve it, and the puzzle grows to a bigger, harder grid.

## Technologies Involved
•	**Java**.  The language the whole project is written in<br>
•	**Swing**. Builds the window, header, status label, and reset button<br>
•	**AWT / Graphics2D**. Handles the custom 2D drawing of the grid, colored regions, crowns, and X-markers<br>


## Features
•	Procedurally generated puzzles that are always guaranteed to be solvable<br>
•	Colored regions that shape each puzzle differently every time<br>
•	Click-to-cycle cells: empty → crown → X-mark → empty (the X lets you mark "not here" while solving)<br>
•	Realtime rule checking — conflicts are detected the moment you place a crown<br>
•	Win detection with a success popup<br>
•	Progressive difficulty — each win increases the grid size (up to 9x9)<br>
•	"New Puzzle" button to reset at any time<br>


## Process
The project is split into two responsibilities:
1. **The window (`ConquestGame`)**, a `JFrame` that holds the header (level display + reset button) and hosts the game canvas.<br>
2. **The game logic and rendering (`QueensCanvas`)**, a `JPanel` that generates each puzzle, listens for clicks, checks the rules, and draws everything.

Puzzle generation works in two steps. First, the game randomly picks valid crown positions (no two touching, like a solved board) to use as "seeds." Then it flood-fills color outward from each seed, each empty cell has an 80% chance of joining a neighboring region, which is what gives every puzzle its organic, irregular shapes instead of plain uniform squares.

Rule-checking happens after every click: the game scans the whole board, confirms no two crowns share a row, column, or diagonal neighbor, and confirms each colored region has exactly one crown, before declaring the puzzle won.

## What I Learned
•	Building a desktop GUI with Java Swing, including custom components and layout management<br>
•	Drawing custom 2D graphics and shapes with `Graphics2D`<br>
•	Handling user input with `MouseListener` and `ActionListener`<br>
•	Designing a procedural generation algorithm (region flood-fill) to create solvable puzzles from scratch<br>
•	Structuring constraint-checking logic (rows, columns, diagonals, regions) cleanly<br>


## How It Can Be Improved
•	Add a timer and scoring system<br>
•	Add an undo button<br>
•	Visually highlight exactly which crowns are in conflict, instead of just flagging that one exists<br>
•	Support more than 9 regions with a larger color palette<br>
•	Add a hint system for stuck players<br>
•	Save/resume progress between sessions<br>


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
