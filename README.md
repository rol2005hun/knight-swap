[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/UkdUJk5L)
# Knight Swap Chess Game

We are given a 4-row by 3-column chessboard. In the top row, we place 3 black knights, and in the bottom row, we place 3 white knights.

The goal is to swap the positions of the two sets of knights.

Rules:
- Knights move according to the standard rules of chess (L-shaped knight moves).
- A knight cannot move to a square that is threatened by an opposing knight.
- Players take turns: white moves first, then black, and so on.

The puzzle is to find a valid sequence of moves that completes the swap under these constraints.

## Solution Steps

This section provides a sequential list of moves to solve the Knight Swap puzzle. Each step describes a single knight movement from a starting square to a target square.

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

## Building from Source

Building the project requires JDK 24 or later and access to [GitHub Packages](https://docs.github.com/en/packages).

GitHub Packages requires authentication using a personal access token (classic) that can be created [here](https://github.com/settings/tokens).

> [!IMPORTANT]
> You must create a personal access token (PAT) with the `read:packages` scope.

You need a `settings.xml` file with the following content to store your PAT:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>github</id>
            <username><!-- Your GitHub username --></username>
            <password><!-- Your GitHub personal access token (classic) --></password>
        </server>
    </servers>
</settings>
```

The `settings.xml` file must be placed in the `.m2` directory in your home directory, i.e., in the same directory that stores your local Maven repository.

## How to Run

After successfully building the project (`mvn clean install`), you can run the game in two modes from your project's root directory:

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