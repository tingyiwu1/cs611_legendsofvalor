# CS611 Legends: Monsters and Heroes Assignment

---

- Charles Li
- cli50@bu.edu
- U23128455

---

## Notable Files

- `Main.java` : Initializes and begins the Game.
- `src.service.screens` : All UI screen logic and interfaces.
- `src.service.ScreenInterfaces` : Interfaces of relevant attributes of game screens.
- `src.service.ScreenContext.java` : Strategy context for screens, used in Main Game logic.
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

#### Design highlights, covered more in design.md:

- Modular architecture for future content expansion
- Screen Management via the a Screen Context strategy pattern.
- Random initialization and reinitialization of gamestate as players progress

---

## How to Compile and Run

From the source directory:

- Compile the code using: `javac -d out $(find . -name "*.java") `
- Run the program: `java -cp out Main`
- See file structure if gradescope messes it up:
  - https://github.com/charlesli50/cs611_heroesnmonsters/tree/main

---

## Input/Output Example

Provide a sample execution showcasing expected inputs and outputs.

```
This is the Map Screen!
+-------+-------+-------+-------+-------+-------+-------+-------+
| H     |       |       |       |       |       | XXXXX | XXXXX |
|       | BOSS! |       |       |       |       | XXXXX | XXXXX |
|       |       |     M |       |       |       | XXXXX | XXXXX |
+-------+-------+-------+-------+-------+-------+-------+-------+
| XXXXX |       |       |       |       |       |       |       |
| XXXXX |       |       |       |       |       |       |       |
| XXXXX |       |     M |     M |       |       |     M |       |
+-------+-------+-------+-------+-------+-------+-------+-------+
|       |       |       | XXXXX |       | XXXXX |       | XXXXX |
|       |       |       | XXXXX |       | XXXXX |       | XXXXX |
|       |     M |       | XXXXX |       | XXXXX |       | XXXXX |
+-------+-------+-------+-------+-------+-------+-------+-------+
|       |       |       | XXXXX | XXXXX | XXXXX | XXXXX |       |
|       |       |       | XXXXX | XXXXX | XXXXX | XXXXX |       |
|       |       |       | XXXXX | XXXXX | XXXXX | XXXXX |       |
+-------+-------+-------+-------+-------+-------+-------+-------+
| XXXXX |       |       |       |       | XXXXX |       |       |
| XXXXX |       |       |       |       | XXXXX |       |       |
| XXXXX |       |       |     M |     M | XXXXX |       |       |
+-------+-------+-------+-------+-------+-------+-------+-------+
|       | XXXXX |       |       |       | XXXXX |       |       |
|       | XXXXX |       |       |       | XXXXX |       |       |
|       | XXXXX |     M |       |       | XXXXX |     M |       |
+-------+-------+-------+-------+-------+-------+-------+-------+
|       |       | XXXXX |       |       |       | XXXXX |       |
|       |       | XXXXX |       |       |       | XXXXX |       |
|     M |     M | XXXXX |       |       |       | XXXXX |       |
+-------+-------+-------+-------+-------+-------+-------+-------+
|       | XXXXX | XXXXX |       | XXXXX |       |       |       |
|       | XXXXX | XXXXX |       | XXXXX |       |       |       |
|       | XXXXX | XXXXX |       | XXXXX |       |       |       |
+-------+-------+-------+-------+-------+-------+-------+-------+
These are the inputs!
Move Hero North: W
Move Hero East: D
Move Hero West: A
Move Hero South: S
Access Inventory: I

Quit: Q
```

---

## Dependencies and Requirements

- Tested on Java 1.8.432 and macOS Sonoma 14.5

---

## Testing Strategy

- Playtested code with various inputs to all screens
- Aimed to provide seamless gameplay experience, even with malinformed inputs for a given gamestate

---

## References and Attribution

- Used Stack Overflow for troubleshooting file I/O issues.
- Used ChatGPT for debugging assistance.
- Used GitHub Copilot for boilerplate generation and autocomplete while coding.
