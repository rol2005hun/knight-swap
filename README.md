[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/UkdUJk5L)
# Knight Swap Chess Game

We are given a 4-row by 3-column chessboard. In the top row, we place three black knights, and in the bottom row, we place three white knights.

The goal is to swap the positions of the two sets of knights.

Rules:
- Knights move according to the [standard rules](https://en.wikipedia.org/wiki/Knight_(chess)) of chess (L-shaped knight moves).
- A knight cannot move to a square threatened by an opposing knight.
- Players take turns: white moves first, then black, and so on.

The puzzle is to find a valid sequence of moves that completes the swap under these constraints.

## Building from Source

Building the project requires JDK 24 or later. Make sure you have the correct JDK version installed before proceeding.

### Prerequisites

- JDK 24 or later
- Apache Maven 3.8.0 or later
- Git (not necessary)

### Dependencies

> [!IMPORTANT]
> You must install these two repositories to your local Maven repository, in this order:
1. [homework-project-utils-2025](https://github.com/rol2005hun/knight-swap-utils)
2. [homework-project-jfxutils](https://github.com/rol2005hun/knight-swap-utils2)

### Build Instructions

1. Clone this repository:
   ```bash
   git clone https://github.com/rol2005hun/knight-swap.git
   cd knight-swap
   ```

2. Build the project:
   ```bash
   mvn clean install
   mvn package
   ```

3. The built JAR file will be available in the `target` directory.

## How to Run

After successfully building the project, you can run the game in two modes:

### Using Maven

From your project's root directory:

**Console version**

To run the puzzle solver that uses the Breadth-First Search (BFS) algorithm to find a solution:

```bash
mvn exec:java -Dexec.mainClass="knightswap.ConsoleGame"
```

**GUI version**

To launch the graphical user interface and play the puzzle game interactively:

```bash
mvn exec:java -Dexec.mainClass="knightswap.GuiGame"
```

### Using the JAR file directly

After building with `mvn clean install`, you can run the JAR file directly:

```bash
java -jar target\knight-swap-1.0.jar
```

This will launch the GUI version by default. To run the console version:

```bash
java -cp target\knight-swap-1.0.jar knightswap.ConsoleGame
```

## How to Play

Upon launching the application, you will be presented with the **Welcome screen**:

![Welcome screen](https://i.imgur.com/qBsPYnF.png)

To begin, enter your name into the designated field and then click the "Start Game" button.

---

Upon successful game launch, the **Main game screen** will appear:

![Main screen](https://i.imgur.com/XsC85BI.png)

To make a move, first click on the cell containing the knight you wish to move. Your second click will designate the empty destination cell where you want to place the knight.

---

A valid initial cell selection (i.e., clicking on a knight that can potentially move) will be highlighted as follows:

![Main screen - Legal selection](https://i.imgur.com/GY2lcmb.png)

If the move you attempt is legal, the knight will be placed at the destination. Additionally, you will receive real-time feedback from the game regarding the move's validity and status:

![Main screen - Comments](https://i.imgur.com/pCEWR6h.png)

---

If you need assistance with the game rules, objectives, or the board's coordinate system, click the "Help" button, located on the Main screen.

![Help screen - Comments](https://i.imgur.com/SleY1C3.png)

---

To see how you stack up against other players or to view the best solutions achieved, click the "Leaderboard" button on the Main screen. This feature allows you to track and compare performance among users.

![Leaderboard - Comments](https://i.imgur.com/L9DHe6M.png)

## Solution Steps

**Understanding the Move Format:**
`Step_Number. (Starting_Row, Starting_Column) -> (Target_Row, Target_Column)`

* **`Step_Number.`**: The sequential number of the move.
* **`(Starting_Row, Starting_Column)`**: The row and column coordinates of the knight's initial position.
* **`->`**: Indicates the movement direction.
* **`(Target_Row, Target_Column)`**: The row and column coordinates of the knight's destination position.

**Coordinate System:**
Rows (Y-coordinates) increase from top to bottom, starting from 0.
Columns (X-coordinates) increase from left to right, starting from 0.

1.  (3, 0) -> (1, 1)
2.  (0, 1) -> (2, 2)
3.  (3, 2) -> (2, 0)
4.  (0, 0) -> (2, 1)
5.  (3, 1) -> (1, 2)
6.  (0, 2) -> (1, 0)
7.  (1, 1) -> (3, 2)
8.  (2, 1) -> (0, 2)
9.  (1, 2) -> (0, 0)
10. (2, 2) -> (3, 0)
11. (2, 0) -> (1, 2)
12. (1, 0) -> (2, 2)
13. (3, 2) -> (2, 0)
14. (0, 2) -> (1, 0)
15. (0, 0) -> (2, 1)
16. (3, 0) -> (1, 1)
17. (1, 2) -> (0, 0)
18. (1, 0) -> (3, 1)
19. (2, 1) -> (0, 2)
20. (2, 2) -> (3, 0)
21. (2, 0) -> (0, 1)
22. (1, 1) -> (3, 2)
