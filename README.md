# Space Invaders - Comprehensive Project Report

**Group Identification: Group 9**
* Gabriel Inumaru Esteves - USP Number: 15453487
* Matheus Spineli de Paiva - USP Number: 14598682
* Pedro Luís Anghievisck - USP Number: 15656521

---

## 1. Requirements

The application is a comprehensive 2D Space Invaders clone that strictly fulfills all assigned academic requirements:
* **Framework:** Utilizes LibGDX for screen management, asset loading, and 2D graphics rendering.
* **Core Mechanics:** Accurate entity movement, shooting logic, and strict Rectangle-based collision detection.
* **Multiplayer:** Features a local two-player cooperative mode (Player 1 and Player 2) sharing the same screen but maintaining individual controls, coordinate tracking, and independent life counters.
* **Progression System:** A 3-level progression system (Swarm index 0 to 2) with increasing difficulty. Swarm speed and shooting frequency scale mathematically based on the difficulty multiplier.
* **Game Loop & State:** Real-time scoring, dynamic HUD updates, and seamless transitions between multiple states.
* **Persistence:** A persistent Save/Load system to write and read game progress natively.

### 🌟 Extra Feature: Unique Ammunition Management System
Instead of the traditional infinite shooting mechanic, we introduced a unique ammunition management system. Players start with limited ammo and must strategically catch random **Ammo Drops** (`AmmoDrop.java`) that spawn dynamically across the screen. These drops rotate 90 degrees graphically, stay on the ground for exactly 8.0 seconds, and add 3 bullets to the player's inventory upon collection, adding a deep layer of resource management to the core gameplay.

---

## 2. Project Description

### 2.1 Brief Architecture & Description
Developed in Java using the LibGDX framework, players control spaceships at the bottom of the screen to defend against alien formations (`Swarm`) that move downwards and shoot periodically. 

The application utilizes a rigorous **MVC (Model-View-Controller)** architecture:
* **Model (`model` package):** Handles spatial data, `Rectangle` hitboxes, and business logic (`Entity`, `Player`, `Enemy`, `Barricade`, etc.).
* **View (`view` package):** Manages screen states via `ScreenAdapter` (`MainMenuScreen`, `GameScreen`, `GameOverScreen`, `LevelCompleteScreen`) and renders the UI using LibGDX `Stage` and `Table`.
* **Controller (`controller` package):** Intermediates input and logic (`PlayerController`, `SwarmController`, `BarricadeTextureGenerator`).

The game tracks real-time scores and lives, ending the game *only* when all player lives are lost or if the alien swarm breaches the player's Y-axis defense line (Y <= 50f).

### 2.2 OOP Concepts Applied
* **Composition:** 
  * The `Swarm` class is composed of a 2D array (`Enemy[][]`) of `Enemy` objects. It acts as a manager for the entire 5x11 grid, moving them as a single global unit while still allowing individual enemies to maintain their own health, collision logic, and sprite states (e.g., Crab, Octopus, Squid).
  * *Extended Composition:* The `BarricadeManager` contains a `List<Barricade>`, and each `Barricade` contains a 2D `List<List<BarricadeBlock>>`.
* **Polymorphism:** 
  * Demonstrated graphically in the `AmmoDrop` class, which overrides the `draw(SpriteBatch batch)` method from the abstract `Entity` class to render its sprite rotated by 90 degrees.
  * Demonstrated logically via the `update(float delta)` abstract method, which forces unique physics updates across `Player`, `Bullet`, and `AmmoDrop`.
  * Collision checks dynamically evaluate the `origin` property of a `Bullet` using the `instanceof` keyword (e.g., `bullet.origin instanceof Enemy`) to distinguish friendly fire from enemy attacks, avoiding self-damage.
* **Encapsulation:** Spatial coordinates (`x`, `y`) are hidden within the `Entity` class. Direct modification is restricted, forcing subclasses to use specific spatial setters that guarantee physical consistency.

---

## 3. Comments About the Code (Technical Details)

* **Hitbox Synchronization:** To prevent collision bugs and "ghosting", the abstract `Entity` class encapsulates positional data. By using `protected` methods (`updateHitbox()`), the bounding box (`Rectangle`) is automatically and strictly synced whenever `setX()` or `setY()` are called by any subclasses.
* **Safe Object Removal (Flags):** We use boolean flags like `isValid` in `Bullet` and `isCollected` in `AmmoDrop`. Instead of forcefully removing objects from memory the exact moment they collide (which breaks the render loop), we flag them as invalid. An `Iterator` in the main loop (`GameScreen.render`) safely sweeps and removes them later, ensuring memory safety.
* **Procedural Texture Generation:** We implemented `BarricadeTextureGenerator` to dynamically create textures at runtime using LibGDX's `Pixmap`. This allows Barricade blocks to visually degrade from *Intact* (Light Grey) to *Damaged* (Dark Grey) to *Destroyed* (Transparent) using an Enum `State` machine.
* **I-Frames Logic:** Player invulnerability was manually coded. Upon losing a life, a 2.0-second timer is triggered. During this window, `Player.draw()` multiplies the timer by 15 and uses a modulus operator (`% 2`) to create a classic arcade "blinking" effect while ignoring new collisions.

---

## 4. Testing (Plan & Results)

The test plan utilizes the **JUnit** framework (specifically `GameLogicTest`) to validate core mathematical and physical interactions without requiring the LibGDX graphical context (*headless testing*). 

Tests heavily focus on:
1. Hitbox synchronization precision.
2. Accurate collision detection between player hitboxes and enemy bullets.
3. Proper instantiation and coordinate mapping of enemies within a generated `Swarm` grid.

**Results:** All automated tests passed successfully, confirming that the collision logic robustly ignores inactive entities (e.g., dead enemies and destroyed bullets do not trigger false positive collisions or crash the physics engine).

---

## 5. Build Procedures

To run this application locally from the source:

1. Ensure you have the latest **Java Development Kit (JDK 26+)** and `make` installed on your system.
2. Clone the repository containing the source code and graphical/audio assets.
```bash
   git clone [https://github.com/Anghievisck/Trabalho_POO.git](https://github.com/Anghievisck/Trabalho_POO.git)
   cd Trabalho_POO
