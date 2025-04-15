# Design Doc for Legends of Valor for CS611

This project is a turn-based role-playing game (RPG) built with object-oriented principles in Java. The system is modular, scalable, and supports dynamic gameplay through polymorphism, inheritance, and composition.

Building off of our previous Legends And Monsters assignment, this design introduces a new controller view system - replacing screens with recursive `processes` that allow for reusable and extensible game-state input and display. 

---

## Notable Design Patterns

Composite `Process`:
- Process is an abstract class that defines a `run()` method. All processes implement this `run` method based on their required activity
- `Process.run()` recursively calls other processes based on user input. e.g. `HeroTurnProcess` calls `inventoryProcess` or `battleProcess`
- The game runs through recursively calling processes until game end or user quit

Facade `Main`
- Main.java simply starts the main process, which reduces complexity from initialization

Singleton `StatsTracker`
- Allows any part of the code to increment arbitrary statistics that are displayed to the user at the end.

`InputProcess.Option` parsing Strategy
- Client passes parsing function to each `Option` declared when constructing an `InputProcess`
- `InputProcess` calls the parsing function on each `Option` to decide which one the user chose

Monster/Item/Market Factory
- The creation of Monsters is based off of the `BattleMonsterFactory` class which exposes static method `generateRandomMonster()`, pulling a monster from a list of options from a text file
- This hides the complexity of Monster Creation from the client, reducing complexity, while also allowing for random creation of monsters for battle. 
- `ItemFactory` takes care of generating a set of random MarketItems whenever a market needs to be refreshed. 
- `MarketFactory` creating and maintaining `Market` instances that correspond to each `Hero` to allow each hero to access it's own `Market` instance. Also handles creating new `Market`s, allowing items to refresh when a hero buys out all the items or the hero levels up.
  - Hides complexity of maintaining different instances, reducing the client code to a simple `getMarket()` call.

---

## High-Level Architecture Overview

The underlying game entity representation is mostly unchanged - the design of Players, Entites, Heros and Monsters at the model level are mostly untouched, as are the game state representations with `Battle`, `Board`, etc.. Compared to the previous Legend: Heroes and Monsters design, the primary focus lies on how each of these modules are displayed and initialized with new processes. It replaces a large amount of hardcoded state logic with dynamic, extensible Composite objects. 

The primary issues with the screens system was how it handled user input. User input was tied to the main object and propogated between each object, which resulted in each screen being dependent on the state of the other. Thus, making them extensible was difficult, as the primary Main Game state logic would need to account for all state possibilities. This way, we have seperation of concerns between every process, reducing bugs and majorily simplifying the state logic. 

Finally, many changes are also clean up on previous bloat of classes from the previous assignment. 

### Main Files

- `Main.java` : Initializes and begins the Game.
- `src.service.process` : All process logic
- `src.service.process.display` : All UI displays called by processes
- `src.service.process.display.MainProcess.java` : Primary process which initalizes the game
- `src.service.entities` : All relevant entity logic.
- `src.service.entities.items` : Logic for interactable items that the player can manipulate.
- `src.service.entities.monsters` : Logic for monsters in combat.
- `src.service.entities.heroes`: Logic for player-controllable heroes in the player's party.
- `src.service.entities.attribtues` : Logic for attributes of entities, including Attacking, Inventory, etc.
- `src.service.entities.Player.java` : Logic for player controlled entities, as well as player-chosen game settings such as difficulty.
- `src.service.game` : All Battle, Map Board, Inventory, Market models, as well as player input logic.
- `src.utils` : Helper functions utilized to track game statistics, print tables, print in color, and more.

### Object Oriented Design

- Encapsulation: Entities all contain their own logic and data, which is only interacted with through each entities respective interface
- Inheritance: `Hero` and `Monster` extend `Entity`. Weapons, Armor, Spell, Consumables, all extend `Item`
- Polymorphism: Processes are initialized and continuously called through the polymorphic Process classes
- Abstraction: The Processes abstract away the core Game module logic, providing simply the interface to interact with the game

### Seperation of Concerns
- A primary motivation behind the design of the assignment was seperation of concerns. Namely, that the Main Game state controller was seperate from the game models in the core, which was seperate from the view controllers. Hence, we have: Top level process(MainGame), communicating with view/state models(Processes), interacting with logic models(Game controller modules), interacting with lower-level data modules(Items, Heroes, Monsters).
  - This makes each component more independent, and easier to test.
  - Also, further additions of components would be more straightforward, as a new feature would simply require creating a new process itself, and then writing the logic for when this process should be called.
  - Factories for Monsters and Items are implemented to read in from a CSV file, making game balance and creation more straightforward. 


### Testing Consideration
- Playtested all input scenarios for every process: battle, inventory, market, and map.
- Tested edge cases including out-of-bound inputs, invalid item usage, and attempting unequips from empty slots.
- Used visual inspection of printed status logs and debugging tools to verify dodge chance calculations, leveling formulas, and equipment effects.



### Gameplay Decisions
- TODO


### Future Expansions
- TODO



---

## Detailed Architecture Overview

To understand the architecture, we can start from a top-down overview, and continue to a detailed description of the implementations of each relevant class.

### Top Level Entry layers

- `Main.java` is the entry point to the program. It simply calls and instantiates the Main Game process
- `src.service.process.MainProcess.java` is the primary intializer of the game. It handles results: Game Quit, Game Win, and Game Loss
- `src.service.process.GameProcess.java` This is the primary state processer. It continuously makes recursive calls to `MonsterTurnProcess` and `HeroTurnProcess` to keep the game going, until either of those return a state that ends the game. 
 
### Context Modules

Many of the processes and other modules require knowledge of the entire gamestate. To reduce coupling between all of these objects, we pass in a `GameContext` module which wraps many other game controller modules. This way, each object can have the required information without being dependent on access to other modules. 

1. **TurnKeeper**
- `src.service.game.TurnKeeper`
- Purpose: Keep track of turn state between hero turn and monster turn
- Structure: tracks and exposes various information on turn counts and a `CurrentTurn` enum for player and monster turn.
  - `progressTurn()` increments a `teamTurnCount` counter. If this iterates through every entity of a Hero or Monster team, it switches the turn to the other team.
 
2. **GameContext**
- `src.service.game.GameContext`
- Purpose: packages many modules together, to pass information to other submodules
- Structure: provides public access to `gameBoard`, `player`, `marketFactory`, `monsterTeam`, `turnKeeper`. 


### Mid Level Modules

This is where the bulk of the development went to. Aside from brief edits to the game logic controllers for the new game, implementing Processes are the primary change from the previous assignment, reworking and removing the screens. 

Of the Middle level modules, there are 2 types; there are the many Processes and the corresponding classes for game logic. Processes wrap the game logic controllers, and provide the UI. 

#### Process Modules

Each process is responsible for handling a specific aspect of the game's functionality. Below is an overview of how these processes work and a list of key processes.

Processes in this project follow a structured approach:
1. **Initialization**: Each process is initialized with the necessary resources (e.g., input scanners, `gameContext`).
2. **Execution**: The `run()` method is called to execute the process. This method contains the main logic for the process.
3. **Result Handling**: Processes return a result that indicates the outcome of their execution. This result can be used by other processes to determine the next steps.

Processes are designed to be modular and reusable, making it easier to manage the game's flow and logic.

1. **MainProcess**
- `src.service.process.MainProcess.java`
- **Purpose**: Serves as the entry point of the game, managing the overall game flow and end-of-game logic.
- **Structure**: The `MainProcess` initializes the game, runs the main game loop, and handles the game's conclusion. It interacts with other processes like `GameProcess` and utility classes for output and statistics tracking.
  - `run()`: Executes the main game process and handles the game's conclusion.
 
2. **GameProcess**

- `src.service.process.GameProcess.java`
- **Purpose**: Manages the core gameplay loop, including player actions, enemy interactions, and game state updates.
- **Structure**: The `GameProcess` handles the main gameplay logic, interacting with other processes like `BattleProcess` and `ShopProcess` as needed. It tracks the game's state and determines the outcome (win, lose, or quit).
  - `run()`: Executes the main gameplay logic and returns a `GameResult`.
 
3. **BattleProcess**

- `src.service.process.BattleProcess.java`
- **Purpose**: Handles turn-based combat between the player and enemies.
- **Structure**: The `BattleProcess` manages the combat loop, processing player and enemy actions, calculating damage, and determining the outcome of battles. It also handles special cases like leveling up or reviving heroes.
  - `run()`: Executes the battle logic and returns the battle outcome.

4. **MarketProcess**

- `src.service.process.MarketProcess.java`
- **Purpose**: Manages interactions with the in-game market, allowing players to buy and sell items.
- **Structure**: The `MarketProcess` tracks available items and the player's inventory. It validates purchases, processes transactions, and updates the player's inventory and gold.
  - `run()`: Executes the market interaction logic and returns the result of the transaction.
 
5. **ContinueProcess**

- `src.service.process.ContinueProcess.java`
- **Purpose**: Waits for the user to press Enter before proceeding. Optionally allows the user to quit by entering "q". Returns a `ScreenResult` indicating success or quit.
- **Structure**: If the `checkQuit` flag is enabled, the user can quit by entering "q". Returns a `ScreenResult` indicating success or quit.
  - `run()`: Displays the prompt, waits for user input, and handles the quit option if enabled.


6. **HeroTurnProcess**

- `src.service.process.HeroTurnProcess.java`
- **Purpose**: Manages the player's turn during the game, allowing the player to perform various actions such as moving, attacking, accessing inventory, or interacting with the market.
- **Structure**: The `HeroTurnProcess` interacts with the game board, turn keeper, and other processes like `MarketProcess` and `InventoryProcess`. It validates player input and executes the corresponding action. Handles special cases like recalling the hero or passing the turn.
  - `run()`: Executes the player's turn, processes input, and progresses the game state.

7. **InputProcess**

- `src.service.process.InputProcess.java`
- **Purpose**: Handles user input by presenting a list of options and validating the user's choice.
- **Structure**: The `InputProcess` is generic and can handle various types of input. It displays a list of options, waits for user input, and validates the input against the provided options. Supports a loop mechanism to repeatedly prompt the user until valid input is received.
  - `run()`: Displays the options, waits for input, and validates the input.

#### Game controller modules

Each process wraps a corresponding game controller module that encapsulates the core game logic. These modules are responsible for managing the state of the game and processing player actions.

Each module implements specific interfaces, such as `PlayerControl` for handling player actions and `StatusDisplay` for managing and displaying statuses. They are designed to encapsulate their respective logic, ensuring modularity and separation of concerns. The modules maintain internal state, such as the current hero, inventory, or board layout, and provide methods to validate and process player actions. They also interact with utility classes like `StatsTracker` to record gameplay statistics and `TextColor` for status display formatting.

Each process provides the game module the players input. In turn, the modules implement `PlayerControl` to handle the inputs, and provide `StatusDisplay` to pass information about the game state back to the Process for the Screen to process. 

1. **Market**

- `src.service.game.market.Market.java`
- Purpose: Manages the logic for purchasing items in the market.
- Structure: The `Market` module maintains a list of available items (`marketOfferings`) and the active hero interacting with the market. It uses methods to validate purchases, process transactions, and update the hero's inventory and gold.
  - `getMarketOfferings()`: Returns the list of items available for purchase.
  - `makeMove(Character input)`: Processes the player's purchase based on their input.

2. **Inventory**

- `src.service.game.inventory.Inventory.java`
- Purpose: Handles the player's inventory and hero equipment.
- Structure: The `Inventory` module manages the active hero's inventory and equipped items. It validates player actions, such as equipping or consuming items, and updates the hero's stats accordingly. It also tracks and displays statuses related to inventory actions.
  - `makeMove(int slot, int index)`: Processes the player's action to equip, unequip, or use an item.

3. **Battle**

- `src.service.game.battle.Battle.java`
- Purpose: Manages turn-based combat between heroes and monsters.
- Structure: The `Battle` module tracks the current hero, monster, and their respective stats. It processes player and monster actions, calculates damage, and determines the outcome of the battle. It also handles special cases like leveling up heroes or reviving them after a battle.
  - `makeMove(Character input)`: Processes the player's selected action during their turn.
  - `isBattleOver()`: Checks if the battle has concluded.
  - `isGameOver()`: Checks if the game has ended due to the player's defeat.

4. **GameBoard**

- `src.service.game.board.GameBoard.java`
- Purpose: Represents the 8x8 game board and manages player navigation.
- Structure: The `GameBoard` module maintains a 2D array of `MapPiece` objects representing the board layout. It tracks the hero's position and validates moves. It also dynamically updates the board, such as clearning obstacles and defeated Monsters. 

5. **Player**

- `src.service.entities.Player.java`
- Purpose: Represents the player and their party of heroes.
- Structure: The `Player` module stores the player's party, gold, and progress. It provides methods to access and update the active hero's stats, inventory, and abilities. It also tracks the player's overall progress and decisions throughout the game.
  - `getParty()`: Returns the player's party of heroes.
  - `getMonsterLevel()`: Returns the current level of monsters based on the player's progress.

---


### Lowest Level Architecture

#### Entities

The `src.service.entities` package contains the core logic for all entities in the game, including heroes, monsters, and items. These entities are designed with object-oriented principles to ensure modularity and scalability.

1. **Entity**

- `src.service.entities.Entity.java`
- Purpose: Serves as the base class for all game entities, such as heroes and monsters.
- Structure: The `Entity` class encapsulates common attributes like health, level, strength, defense, and dodge. It provides methods for managing these attributes, such as taking damage, healing, and leveling up.
  - `takeDamage(int damage)`: Reduces the entity's health by the specified amount.
  - `healDamage(int amount)`: Restores the entity's health by the specified amount.
  - `levelUp()`: Increases the entity's level and improves its attributes.

2. **Hero**

- `src.service.entities.heroes.Hero.java`
- Purpose: Represents a player-controlled hero in the game.
- Structure: The `Hero` class extends `Entity` and implements additional interfaces like `Attacks`, `Inventory`, and `Shopper`. It manages the hero's inventory, equipped items, and combat abilities.
  - `getAttacksList()`: Returns a list of available attack options, including weapon attacks, spells, and potions.
  - `equipItem(int slot, int index)`: Equips an item to the specified slot.
  - `levelUpHealth()`, `levelUpStrength()`, etc.: Methods to improve specific attributes when leveling up.

3. **Monster**

- `src.service.entities.monsters.Monster.java`
- Purpose: Represents an enemy monster in the game.
- Structure: The `Monster` class extends `Entity` and implements the `Attacks` interface. It includes additional attributes like reward gold, reward XP, and a monster-specific weapon.
  - `mainHandAttack()`: Executes the monster's primary attack using its weapon.
  - `getRewardGold()`, `getRewardXP()`: Returns the rewards for defeating the monster.

4. **Item**

- `src.service.entities.items.Item.java`
- Purpose: Serves as the base class for all items in the game, such as weapons, armor, potions, and spells.
- Structure: The `Item` class defines common attributes like name, description, level requirement, and item type. It is extended by specific item types to provide additional functionality.
  - `getDamage()`: Returns the item's damage value (if applicable).
  - `getItemType()`: Returns the item's type (e.g., weapon, potion, spell).

5. **Potion**

- `src.service.entities.items.Potion.java`
- Purpose: Represents a consumable item that provides buffs or heals the hero.
- Structure: The `Potion` class extends `Item` and includes additional attributes like the type of buff it provides (e.g., HP, strength) and the number of uses remaining.
  - `use(Hero hero)`: Applies the potion's effect to the specified hero.
  - `getPotionType()`: Returns the type of buff the potion provides.

6. **Weapon**

- `src.service.entities.items.Weapon.java`
- Purpose: Represents a weapon that heroes or monsters can use in combat.
- Structure: The `Weapon` class extends `Item` and includes attributes like damage and bonus stats. It is used to calculate attack damage during combat.
  - `getDamage()`: Returns the weapon's damage value.

7. **Armor**

- `src.service.entities.items.Armor.java`
- Purpose: Represents protective gear that reduces damage taken by heroes.
- Structure: The `Armor` class extends `Item` and includes attributes like defense bonuses. It is equipped by heroes to improve their survivability.
  - `getDefenseBonus()`: Returns the armor's defense value.

8. **Spell**

- `src.service.entities.items.Spell.java`
- Purpose: Represents a magical attack that heroes can use in combat.
- Structure: The `Spell` class extends `Item` and includes attributes like mana cost and elemental type. It is used to perform magic-based attacks.
  - `cast(Hero hero, Monster monster)`: Executes the spell, consuming mana and dealing damage.

9. **Attributes**

- `src.service.entities.attributes`
- Purpose: Provides interfaces and helper classes for entity attributes and actions.
- Key Interfaces:
  - `Attacks`: Defines methods for entities that can perform attacks, such as `mainHandAttack()` and `getAttacksList()`.
  - `Inventory`: Defines methods for managing an entity's inventory, such as `addItem()` and `removeItem()`.

10. **InfoTextReader**

- `src.service.entities.InfoTextReader.java`
- Purpose: Handles the parsing of text files to initialize game data, such as items and monsters.
- Structure: The `InfoTextReader` class provides static methods to read and parse text files, returning lists of initialized objects.
  - `readItemTextFile(String filepath)`: Reads a file and returns a list of `MarketItem` objects.
  - `readMonsterTextFile(String filepath, int monsterLevel)`: Reads a file and returns a list of `Monster` objects.

This modular design ensures that entities are self-contained and interact with each other through well-defined interfaces, promoting maintainability and extensibility.
