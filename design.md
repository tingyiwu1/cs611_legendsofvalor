# Design Doc for Legends: Heroes And Monsters for CS611

This project is a turn-based role-playing game (RPG) built with object-oriented principles in Java. The system is modular, scalable, and supports dynamic gameplay through polymorphism, inheritance, and composition.

---

## High-Level Architecture Overview

### Main Files

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

### Object Oriented Design

- Encapsulation: Entities all contain their own logic and data, which is only interacted with through each entities respective interface
- Inheritance: `Hero` and `Monster` extend `Entity`. Weapons, Armor, Spell, Consumables, all extend `Item`
- Polymorphism: Screens are initialized and continuously rendered through the polymorphic Screen Strategy interfaces
- Abstraction: The Screens abstract away the core Game module logic, providing simply the interface to interact with the game

### Seperation of Concerns
- A primary motivation behind the design of the assignment was seperation of concerns. Namely, that the Main Game state controller was seperate from the game models in the core, which was seperate from the view controllers. Hence, we have: Top level controller(MainGame), communicating with view models(Screens), interacting with logic models(Game controller modules), interacting with lower-level data modules(Items, Heroes, Monsters).
  - This makes each component more independent, and easier to test.
  - Also, further additions of components would be more straightforward, as a new screen would simply require creating a new screen view itself, and then writing the logic for when this screen should be shown.
  - Factories for Monsters and Items are implemented to read in from a CSV file, making game balance and creation more straightforward. 


### Testing Consideration
- Playtested all input scenarios for every screen: battle, inventory, market, and map.
- Tested edge cases including out-of-bound inputs, invalid item usage, and attempting unequips from empty slots.
- Used visual inspection of printed status logs and debugging tools to verify dodge chance calculations, leveling formulas, and equipment effects.



### Gameplay Decisions
- There are 3 strategies to bring to scale to victory. One is maximizing strenght, second is maximizing defense, and 3rd is maximizing dodge.
- Heroes are set and are in a set order within the party to support these strategies. Because the first Warrior has high defense, he is effective at taking a hit and then swapping. The mage has high strength, and when well equipped can one-shot enemies. The Assassin has high dodge, and can dodge-tank through the enemies attacks.
  - Through finding stat-boosting potions of the specific stat, one can maximize a heroes stats to always win.
- I created a Boss. Otherwise, the player simply walks around in circles looking for enemies. With a boss, the player has an intermediary goal.  


### Future Expansions
- Gameplay-related expansion: the framework to build more additional damage types, more complex battle calculations, and more are there, and if there was more time for this assignment that is what I would do
- Adding more ASCII Art and prettifying some of the UI pieces that currently simply dump a lot of infomration on the player.
- Making Map Generation using a better algorithm rather than randomly generated(currently there exists a chance of bad maps)



---

## Detailed Architecture Overview

To understand the architecture, we can start from a top-down overview, and continue to a detailed description of the implementations of each relevant class.

### Top Level Entry layers

- `Main.java` is the entry point to the program. It simply calls and instantiates the Main Game
- `src.service.game.MainGame.java` is the primary State Machine for the game screens.
  - In the constructor, it initializes an 8 by 8 game board, and then a Screen strategy context.
  - It provides the core logic for when the strategy should switch screens.
- `src.service.screens.ScreenContext.java` This is the Screen strategy context that `MainGame.java` utilizes to display each screen. It primarily implements the following methods:
  - `setScreen()` will set the screen to a new screen
  - `displayScreen()` will call the current screens `displayAndProgress()` method, abstracting the displayed screen from the main state machine logic.
  - `getLastInput()` will return the current screens last seen input, which is how the Main state machine logic progresses.

### Mid Level Modules

Of the Middle level modules, there are 2 types; there are the many Screens and the corresponding classes for game logic. Screens wrap the game logic controllers, and provide the UI.

#### Screens

- Screens are built off of `src.services.screens.ScreenInterfaces.Screen.java`. This interface exposes:

  - `displayAndProgress()` for displaying the game logic controllers' status / a UI for the current game state.
  - `displayStatuses()` for printing out the current statuses that the game logic produces.
  - `displayPauseAndProgress()` for when an intermediary screen may be needed, such as to confirm a choice or to inform the player of an event.
  - `getLastInput()` provides the screen's last seen input, allowing both the parent strategy to access it for state-based decisions or to push the latest event down to the game logic to make a change to the game state.

- Screens that require input can extend `InputInterface.java`, which provides:

  - An overloaded static method `DisplayInputOption()` to print different colored input options to the screen.
  - A `DisplayInputs()` method to display all possible inputs and receive the player's input in turn.

- `InnerInput.java` provides additional methods for screens that need nested input handling, such as switching between submenus or managing complex interactions.

#### Key Screens

Each of these Screens wraps another piece of inner game logic and renders the state of the game based off of that object.

1. **IntroScreen**

- `src.service.screens.IntroScreen.java`
- Purpose: Introduces the game and initializes the player.
- Allows the player to select difficulty and the number of heroes in their party.
  - `initializePlayer()`: Prompts the player to select difficulty and party size, returning a `Player` object.
  - `DisplayIntroToGame()`: Displays the introductory narrative and waits for player input to proceed.

2. **MapScreen**

- `src.service.screens.MapScreen.java`
- Purpose: Displays the game board and handles player navigation.
- Renders the 8x8 game board with walls, markets, and the boss location.
- Handles player movement and validates moves.
- Detects encounters with markets or enemies and transitions to the appropriate screen.
  - `displayAndProgress()`: Displays the map and processes player movement.
  - `displayMap()`: Renders the game board with all its elements.
  - `DisplayInputs()`: Displays movement options and captures player input.

3. **MarketScreen**

- `src.service.screens.MarketScreen.java`
- Purpose: Allows the player to purchase items for their heroes.
- Displays available items for purchase.
- Validates purchases based on the player's gold and item availability.
- Updates the player's inventory and gold after a successful purchase.
  - `displayAndProgress()`: Displays the market and processes player actions.
  - `displayInnerQuery()`: Displays the list of items and handles purchase input.
  - `MarketInput()`: Captures and validates player input for item selection.

4. **InventoryScreen**

- `src.service.screens.InventoryScreen.java`
- Purpose: Manages the player's inventory and hero equipment.
- Displays the current inventory and equipped items for each hero.
- Allows the player to equip, unequip, or consume items.
- Updates hero stats based on equipped items.
  - `displayAndProgress()`: Displays the inventory and processes player actions.
  - `displayInnerQuery()`: Displays hero inventory details and handles item management input.
  - `InventoryInput()`: Captures and validates player input for inventory actions.

5. **BattleScreen**

- `src.service.screens.BattleScreen.java`
- Purpose: Handles turn-based combat between heroes and monsters.
- Displays hero and monster stats, including health bars and attributes.
- Allows the player to select attacks or use items during their turn.
- Tracks the battle's progress and determines victory or defeat.
  - `displayAndProgress()`: Displays the battle state and processes player actions.
  - `DisplayInputs()`: Displays attack and item options, capturing player input.
  - `displayBattle()`: Renders the battle details, including hero and monster stats.
  - `getHealthBar()`: Generates a visual health bar for entities.

#### Game controller modules

Each screen wraps a corresponding game controller module that encapsulates the core game logic. These modules are responsible for managing the state of the game and processing player actions.

Each module implements specific interfaces, such as `PlayerControl` for handling player actions and `StatusDisplay` for managing and displaying statuses. They are designed to encapsulate their respective logic, ensuring modularity and separation of concerns. The modules maintain internal state, such as the current hero, inventory, or board layout, and provide methods to validate and process player actions. They also interact with utility classes like `StatsTracker` to record gameplay statistics and `TextColor` for status display formatting.

Each Screen provides the game module the players input. In turn, the modules implement `PlayerControl` to handle the inputs, and provide `StatusDisplay` to pass information about the game state back to the Screen for the Screen to display. 

1. **Market**

- `src.service.game.market.Market.java`
- Wrapped by: `MarketScreen`
- Purpose: Manages the logic for purchasing items in the market.
- Structure: The `Market` module maintains a list of available items (`marketOfferings`) and the active hero interacting with the market. It uses methods to validate purchases, process transactions, and update the hero's inventory and gold.
  - `getMarketOfferings()`: Returns the list of items available for purchase.
  - `makeMove(Character input)`: Processes the player's purchase based on their input.

2. **Inventory**

- `src.service.game.inventory.Inventory.java`
- Wrapped by: `InventoryScreen`
- Purpose: Handles the player's inventory and hero equipment.
- Structure: The `Inventory` module manages the active hero's inventory and equipped items. It validates player actions, such as equipping or consuming items, and updates the hero's stats accordingly. It also tracks and displays statuses related to inventory actions.
  - `makeMove(int slot, int index)`: Processes the player's action to equip, unequip, or use an item.

3. **Battle**

- `src.service.game.battle.Battle.java`
- Wrapped by: `BattleScreen`
- Purpose: Manages turn-based combat between heroes and monsters.
- Structure: The `Battle` module tracks the current hero, monster, and their respective stats. It processes player and monster actions, calculates damage, and determines the outcome of the battle. It also handles special cases like leveling up heroes or reviving them after a battle.
  - `makeMove(Character input)`: Processes the player's selected action during their turn.
  - `isBattleOver()`: Checks if the battle has concluded.
  - `isGameOver()`: Checks if the game has ended due to the player's defeat.

4. **GameBoard**

- `src.service.game.board.GameBoard.java`
- Wrapped by: `MapScreen`
- Purpose: Represents the 8x8 game board and manages player navigation.
- Structure: The `GameBoard` module maintains a 2D array of `MapPiece` objects representing the board layout. It tracks the player's position and validates moves. It also dynamically updates the board, such as spawning new bosses or clearing defeated ones.
  - `setNewBoss()`: Spawns a new boss on the board.

5. **Player**

- `src.service.entities.Player.java`
- Wrapped by: Multiple screens (e.g., `IntroScreen`, `InventoryScreen`, `BattleScreen`)
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
