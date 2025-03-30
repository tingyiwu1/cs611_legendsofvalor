# Design Doc for Legends: Heroes And Monsters for CS611

This project is a turn-based role-playing game (RPG) built with object-oriented principles in Java. The system is modular, scalable, and supports dynamic gameplay through polymorphism, inheritance, and composition.

---

## High-Level Architecture Overview

### Main Files

- ``` Main.java ``` : Initializes and begins the Game.
- ``` src.service.screens ``` : All UI screen logic and interfaces.
- ``` src.service.ScreenInterfaces ``` : Interfaces of relevant attributes of game screens.
- ``` src.service.ScreenContext.java ``` : Strategy context for screens, used in Main Game logic.
- ``` src.service.entities ``` : All relevant entity logic.
- ``` src.service.entities.items ``` : Logic for interactable items that the player can manipulate.
- ``` src.service.entities.monsters ``` : Logic for monsters in combat.
- ``` src.service.entities.heroes ```: Logic for player-controllable heroes in the player's party.
- ``` src.service.entities.attribtues ``` : Logic for attributes of entities, including Attacking, Inventory, etc.
- ``` src.service.entities.Player.java ``` : Logic for player controlled entities, as well as player-chosen game settings such as difficulty.
- ``` src.service.game ``` : All Battle, Map Board, Inventory, Market models, as well as player input logic.
- ``` src.service.game.MainGame.java ``` : Primary logic controller of entities, screens, and models.
- ``` src.utils ``` : Helper functions utilized to track game statistics, print tables, print in color, and more. 

### Object Oriented Design

- Encapsulation: Entities all contain their own logic and data, which is only interacted with through each entities respective interface
- Inheritance: ```Hero``` and ```Monster``` extend ```Entity```. Weapons, Armor, Spell, Consumables, all extend ```Item```
- Polymorphism: Screens are initialized and continuously rendered through the polymorphic Screen Strategy interfaces
- Abstraction: The Screens abstract away the core Game module logic, providing simply the interface to interact with the game

---

## Detailed Architecture Overview

To understand the architecture, we can start from a top-down overview, and continue to a detailed description of the implementations of each relevant class. 

### Top Level Entry layers

- ``` Main.java ``` is the entry point to the program. It simply calls and instantiates the Main Game
- ``` src.service.game.MainGame.java ``` is the primary State Machine for the game screens. 
  -  In the constructor, it initializes an 8 by 8 game board, and then a Screen strategy context.
  -  It provides the core logic for when the strategy should switch screens. 






