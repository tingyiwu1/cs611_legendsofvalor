# CS611 Legends of Valor!

---

- Charles Li / TingYi Wu
- cli50@bu.edu / tingyiwu@bu.edu
- U23128455 / U85278299

---

## Notable Files / Directories

- `Main.java` : Initializes and begins the Game.
- `src.service.process` : All UI screen logic and interfaces, extended to a recursive set of Processes to display and progress the game
- `src.service.entities` : All relevant entity logic.
- `src.service.entities.items` : Logic for interactable items that the player can manipulate.
- `src.service.entities.monsters` : Logic for monsters in combat.
- `src.service.entities.heroes`: Logic for player-controllable heroes in the player's party.
- `src.service.entities.attribtues` : Logic for attributes of entities, including Attacking, Inventory, etc.
- `src.service.entities.Player.java` : Logic for player controlled entities, as well as player-chosen game settings such as difficulty.
- `src.service.game` : All Battle, Map Board, Inventory, Market models, as well as player input logic.
- `src.service.game.MainGame.java` : Primary logic controller of entities, screens, and models.
- `src.utils` : Helper functions utilized to track game statistics, print tables, print in color, and more.

---

## Notes

Implemented a complete skill-based role-playing game with object-oriented principles and design in mind. Heroes will aim to beat successive levels of bosses, while gaining experience and gold to spent at various Markets. Difficulty scales as game-time increases, but space for certain strategies are theoretically infinitely scalable. A complex battle system allows creativity in Hero inventory builds and utilization.

The game has been playtested with the goal of providing a nuanced strategy to continue to survive in the world of heroes and monsters.

The design is extended off of the previous Heros and Monsters assignment, with a new set of game control and UI logic in `processes`.

#### Design highlights, covered more in design.md:

- Modular architecture for future content expansion
- Screen Management via the `processes` modules.
- Random initialization and reinitialization of gamestate as players progress

---

## How to Compile and Run

From the source directory:

- Compile the code using: `mkdir -p out && javac -d out $(find . -name "*.java") `
- Run the program: `java -cp out Main`
- See file structure if gradescope messes it up:
  - [https://github.com/charlesli50/cs611_heroesnmonsters/tree/main](https://github.com/tingyiwu1/cs611_legendsofvalor)

---

## UML Diagram

Please view the attached UML Diagrams:

- `fullUML.png`: Full UML Diagram for entire repository
- `miniDiagram.png`: Smaller UML Diagram with less info

---

## Input/Output Example

Provide a sample execution showcasing expected inputs and outputs.

```
Welcome to the game!!

This is the Map Screen!
+-------+-------+-------+-------+-------+-------+-------+-------+
|     M |       | XXXXX |     M |       | XXXXX |     M |       |
| NEXUS | NEXUS | XXXXX | NEXUS | NEXUS | XXXXX | NEXUS | NEXUS |
|       |       | XXXXX |       |       | XXXXX |       |       |
+-------+-------+-------+-------+-------+-------+-------+-------+
|       |       | XXXXX |       |       | XXXXX |       |       |
|  _K_  |       | XXXXX |  [C]  |       | XXXXX |       |  ~B~  |
|       |       | XXXXX |       |       | XXXXX |       |       |
+-------+-------+-------+-------+-------+-------+-------+-------+
|       |       | XXXXX |       |       | XXXXX |       |       |
|       |  [C]  | XXXXX |       |       | XXXXX |       |       |
|       |       | XXXXX |       |       | XXXXX |       |       |
+-------+-------+-------+-------+-------+-------+-------+-------+
|       |       | XXXXX |       |       | XXXXX |       |       |
|       |  [C]  | XXXXX |       |       | XXXXX |       |       |
|       |       | XXXXX |       |       | XXXXX |       |       |
+-------+-------+-------+-------+-------+-------+-------+-------+
|       |       | XXXXX |       |       | XXXXX |       |       |
|  [C]  |       | XXXXX |       |       | XXXXX |       |       |
|       |       | XXXXX |       |       | XXXXX |       |       |
+-------+-------+-------+-------+-------+-------+-------+-------+
|       |       | XXXXX |       |       | XXXXX |       |       |
|       |       | XXXXX |  [C]  |       | XXXXX |       |       |
|       |       | XXXXX |       |       | XXXXX |       |       |
+-------+-------+-------+-------+-------+-------+-------+-------+
|       |       | XXXXX |       |       | XXXXX |       |       |
|  ~B~  |  [C]  | XXXXX |       |       | XXXXX |       |       |
|       |       | XXXXX |       |       | XXXXX |       |       |
+-------+-------+-------+-------+-------+-------+-------+-------+
|AH     |       | XXXXX | H     |       | XXXXX | H     |       |
| NEXUS | NEXUS | XXXXX | NEXUS | NEXUS | XXXXX | NEXUS | NEXUS |
|       |       | XXXXX |       |       | XXXXX |       |       |
+-------+-------+-------+-------+-------+-------+-------+-------+
H = Hero, M = Monster, NEXUS = Nexus, X = Wall
Current Hero: The Warrior
Active Hero: The Warrior
Select an action:
[w] Move Up
[a] Move Left
[s] Move Down
[d] Move Right
[i] Access Inventory
[m] Access Nexus Market
[p] Pass the Turn
[q] Quit
```

---

## Dependencies and Requirements

- Tested on Java 1.8.432 and macOS Sonoma 14.5

---

## Testing Strategy

- Playtested code with various inputs to all screens
- Aimed to provide seamless gameplay experience, even with malinformed inputs for a given gamestate

---

#Known Issues

---

## References and Attribution

- Used Stack Overflow for troubleshooting file I/O issues.
- Used ChatGPT for debugging assistance.
- Used GitHub Copilot for boilerplate generation and autocomplete while coding.
