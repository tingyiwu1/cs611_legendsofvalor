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

### Screens

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

1. **IntroScreen**

   - Purpose: Introduces the game and initializes the player.
   - Key Features:
     - Displays the game's introduction and story.
     - Allows the player to select difficulty and the number of heroes in their party.

2. **MapScreen**

   - Purpose: Displays the game board and handles player navigation.
   - Key Features:
     - Renders the 8x8 game board with walls, markets, and the boss location.
     - Handles player movement and validates moves.
     - Detects encounters with markets or enemies and transitions to the appropriate screen.

3. **MarketScreen**

   - Purpose: Allows the player to purchase items for their heroes.
   - Key Features:
     - Displays available items for purchase.
     - Validates purchases based on the player's gold and item availability.
     - Updates the player's inventory and gold after a successful purchase.

4. **InventoryScreen**

   - Purpose: Manages the player's inventory and hero equipment.
   - Key Features:
     - Displays the current inventory and equipped items for each hero.
     - Allows the player to equip, unequip, or consume items.
     - Updates hero stats based on equipped items.

5. **BattleScreen**

   - Purpose: Handles turn-based combat between heroes and monsters.
   - Key Features:
     - Displays hero and monster stats, including health bars and attributes.
     - Allows the player to select attacks or use items during their turn.
     - Tracks the battle's progress and determines victory or defeat.

---
