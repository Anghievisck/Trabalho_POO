# Space Invaders - Comprehensive Technical Project Report

[cite_start]**Group Identification: Group 9** 
* [cite_start]Gabriel Inumaru Esteves - USP Number: 15453487 
* [cite_start]Matheus Spineli de Paiva - USP Number: 14598682 
* [cite_start]Pedro Luís Anghievisck - USP Number: 15656521 

---

## 1. Requirements

[cite_start]The application is a fully functional 2D Space Invaders clone designed and implemented to satisfy all core academic requirements:
* [cite_start]**Framework Integration:** Uses LibGDX for seamless screen state management, asset lifecycle control, and 2D hardware-accelerated graphics rendering.
* [cite_start]**Core Mechanics:** Accurate real-time entity movement, automated and user-driven shooting physics, and pixel-consistent bounding box collision detection.
* [cite_start]**Multiplayer Cooperative Mode:** Features a local, shared-screen two-player cooperative experience where Player 1 and Player 2 possess completely separate control bindings, independent coordinate handling, and individual life counters.
* **Progression System:** A structured 3-level progression system (Hordes/Swarms 0 through 2). The difficulty scales dynamically, affecting enemy movement velocity and shooting frequency multipliers.
* [cite_start]**State Management:** Seamless real-time transitions across multiple screen states (Main Menu, Active Gameplay, Pause Menu, Level Transition, and Game Over)[cite: 44].
* [cite_start]**Persistence:** A persistent Save/Load subsystem that captures and reconstitutes the exact state of a match via local file serialization[cite: 40, 41].

### 🌟 Extra Feature: Tactical Ammunition Management System
[cite_start]Departing from the traditional infinite-shooting mechanic of classic arcades, this project introduces an original ammunition management layer[cite: 41, 42]. Players start with limited ammo and must actively monitor their reserves. **Ammo Drops** (`AmmoDrop.java`) spawn randomly along the horizontal play area. [cite_start]Players must navigate toward these drops to reload[cite: 42]. [cite_start]This design introduces resource scarcity and tactical risk-reward calculations into the core gameplay[cite: 42].

---

## 2. Project Description

### 2.1 Brief Architecture Overview
[cite_start]Developed in Java using the LibGDX framework, players control defensive spaceships situated at the bottom of the screen to protect Earth against synchronized alien formations that advance downward and execute period attacks[cite: 43]. 

The system strictly adheres to the **Model-View-Controller (MVC)** architectural pattern to untangle data representation from user input and rendering systems:
1. **Model:** Encapsulates core state variables, spatial vectors, and geometric hitboxes (`Entity`, `Player`, `Enemy`, `Barricade`, etc.).
2. **View:** Manages visual context, UI layouts (`Stage`, `Table`, `Label`), and graphics drawing loops.
3. **Controller:** Processes physical input polled from hardware devices and executes algorithmic rules upon the Model.

Real-time scores, ammunition counts, and health tracking dictate the state machine; the session terminates in a defeat screen only when both players exhaust their lives or when the alien formation breaches the vital defensive boundary line established at $Y \le 50$.

### 2.2 OOP Concepts Applied
* [cite_start]**Composition:** * The `Swarm` class is composed of a 2D array (`Enemy[][]`) of individual `Enemy` objects[cite: 46]. [cite_start]It serves as a unified group controller, computing macro movement sweeps as a single unit while allowing separate alien instances to maintain independent internal health values, unique shooting speeds, and row-based visual sprites[cite: 47].
  * The `BarricadeManager` class is composed of a collection of `Barricade` instances, which in turn are composed of a nested 2D list (`List<List<BarricadeBlock>>`) outlining a physical defense matrix.
* **Polymorphism:** * **Method Overriding:** Demonstrated in the `AmmoDrop` class, which overrides the base `draw(SpriteBatch batch)` method from the abstract `Entity` class to modify rendering behaviors, drawing its sprite rotated at a fixed 90-degree angle. Additionally, child entities implement customized variations of the abstract `update(float delta)` signature.
  * **Dynamic Runtime Evaluation:** Collision handlers analyze the `origin` field of a `Bullet` using the `instanceof` keyword (e.g., `bullet.origin instanceof Enemy`). [cite_start]This allows the physics engine to differentiate between hostile fire and friendly fire, completely neutralizing self-damage or friendly-fire exploits[cite: 49].
* **Encapsulation:** Positional coordinates (`x`, `y`) and spatial parameters are declared private within the `Entity` class. Subclasses cannot modify these variables directly; they must communicate via protected mutators that strictly enforce physical and mathematical constraints.

---

## 3. Comprehensive Class Breakdown (MVC Architecture)

### 📦 3.1 The MODEL Package (State & Rules)

#### `Entity.java`
The abstract foundation for every tangible object inhabiting the game world. It encapsulates private positional coordinates (`x`, `y`) and bounding boxes (`width`, `height`). It instantiates a LibGDX `Rectangle` object acting as the official collision hitbox. Whenever spatial modifiers like `setX()` or `setY()` are processed, an internal protected method `updateHitbox()` is automatically triggered, binding the physical bounding box to the visual representation and preventing clipping errors.

#### `Player.java`
Represents the user's ship asset, maintaining fields for points, lives, remaining ammo, and specific integers representing assigned input keycodes (allowing separate left, right, and shoot bindings for Player 1 and Player 2). It governs post-hit invulnerability: when struck, an `invulnerableTimer` is set to 2.0 seconds. Its `draw()` method evaluates this timer multiplied by 15; a modulus operation (`% 2 == 0`) toggles the texture visibility on alternate frames, producing a classic flashing arcade invulnerability effect.

#### `Enemy.java`
Models individual alien entities within the horde. It stores health limits, unique laser sprites, and downward flight speeds. It features a `checkCollision(Bullet)` method that validates whether the alien is currently alive before evaluating overlapping hitboxes, preventing dead entities from absorbing shots.

#### `Bullet.java`
Controls projectile entities generated by firing actions. It features a `speed` vector that is positive for upward movement (player shots) and negative for downward paths (alien fire). It preserves an `origin` reference pointing back to its parent `Entity`. Its `update()` sequence moves the bullet across the Y-axis relative to frame delta-time and flags `isValid = false` if it travels beyond a 50-pixel safety margin off-screen, preventing memory bloat.

#### `AmmoDrop.java`
Manages the temporary physical reload item. It keeps an accumulated `timeAlive` counter. If an ammo drop remains uncollected on the battlefield for more than 8.0 seconds (`timeAlive > 8f`), its internal flag `isCollected` is flipped to true, triggering its automatic deletion. It overrides `draw()` to manipulate sprite rotation profiles natively.

#### `Swarm.java`
An organizational model managing the 5x11 grid of invaders. During instantiation, it calculates the macro bounds of the grid and assigns alien categories based on row placement: Row 0 is mapped to Type 4 (Squids), Rows 1-2 to Type 2 (Octopuses), and Rows 3-4 to Type 0 (Crabs). Its `move(deltaX, deltaY)` method runs nested loops that increment positions exclusively for individual aliens whose health registers above zero.

#### `BarricadeBlock.java`
The atomic unit of the base defense shields. It relies on a `State` enum tracking three structural phases: `INTACT`, `DAMAGED`, or `DESTROYED`. It holds three distinct texture pointers. Calling `damage()` advances the structural state and switches the active sprite. Once marked as `DESTROYED`, it is omitted from rendering passes and collision pipelines.

#### `Barricade.java`
Assembles atomic blocks into a complete, structured bunker layout. It structures blocks within a nested list matrix (`List<List<BarricadeBlock>>`). Bunkers are initialized via a static binary template (`int[][] pattern`) of 6 rows by 8 columns that outlines the iconic hollowed-out arch shape; positions holding a `0` value are immediately flagged as destroyed upon generation. The `checkCollision()` routine terminates and returns true after damaging the first overlapping block, ensuring a bullet destroys exactly one block at a time.

#### `BarricadeManager.java`
The top-level factory and lifecycle manager for all defensive bunkers. It handles a list of active `Barricade` models. The `createBarricades()` method calculates uniform horizontal spacing by dividing the window width by the desired bunker count plus one, offsetting the starting point by `blockSize * 4` to center each structure perfectly around its calculated coordinate.

---

### 🕹️ 3.2 The CONTROLLER Package (Input & Logic)

#### `PlayerController.java`
Intercepts real-time hardware keyboard events and translates them into model adjustments. During its `update()` loop, it polls `Gdx.input.isKeyPressed()` using the player's custom keymaps to trigger left or right movement. Upon validating a shoot trigger, it verifies that the player is alive and possesses remaining ammo; it then creates a new `Bullet` object centered relative to the ship, decrements the ammo count, and plays `shoot.wav` at a 0.1f volume profile using the `AssetManager`. It also continuously tests enemy bullets against the player's ship hitbox.

#### `SwarmController.java`
Maintains the artificial intelligence, movement loops, and shooting behavior of the alien grid. It shifts the alien formation laterally by multiplying base speed by the frame's delta time. It evaluates individual alien coordinates against screen boundaries ($0$ and `screenWidth`); if an edge collision occurs, it flips the group's `movingRight` direction flag and commands the entire grid to drop downward by a fixed `dropDistance`. For offensive behavior, it polls a `shootTimer`; when this exceeds a value scaled by the level difficulty, it randomly selects a surviving alien using `MathUtils.random(1, swarm.aliveCount)` to spawn a downward-traveling bullet. It also swaps alien animation frames based on systemic delta thresholds.

#### `BarricadeTextureGenerator.java`
A graphics utility designed to construct colored block textures procedurally without relying on external image files. It allocates a LibGDX `Pixmap` buffer to manipulate raw pixel memory using the standard RGBA8888 channel format. It fills square regions with specific hexadecimal color values (`0x888888FF` for intact light grey; `0x555555FF` for damaged dark grey). Once the pixel data is bound to a GPU `TextureRegion`, it calls `pixmap.dispose()` to clear native memory allocations and prevent critical memory leaks.

---

### 🖥️ 3.3 The VIEW Package (Rendering & Interface)

#### `SpaceInvadersGame.java`
The primary application bootstrapper and lifecycle supervisor inheriting from LibGDX’s `Game` class. It constructs and owns the global **`AssetManager`** instance, which asynchronously loads UI skins, gameplay texture atlases, and audio wave files. Once loading completes inside `create()`, it delegates display rendering by pushing `MainMenuScreen` into the active view slot.

#### `MainMenuScreen.java`
An interactive launch screen constructed using a Scene2D `Stage` and assigned as the primary application input processor. It arranges widgets like `Table` layouts and `TextButton` components to build user options for starting fresh games, launching 2-Player modes, loading saved states, or exiting. Buttons contain `ChangeListener` elements that route game state transitions based on selection.

#### `GameScreen.java`
The core engine loop responsible for updating physics, tracking timers, processing entity lists, and drawing graphics every frame.
* **Pause Menu Integration:** Captures `ESCAPE` keystrokes to halt gameplay updates by toggling `isPaused = true`, subsequently routing pointer inputs to an overlay menu layout.
* **Safe Memory Management:** To eliminate crashes caused by modifying collections while reading them (`ConcurrentModificationException`), `GameScreen` uses Java **`Iterator`** structures. Iterators loop through bullet and drop lists, executing safe deletions (`bulletIter.remove()`) only after all collision tests are finalized for that frame.
* **Defeat Evaluation:** Monitors alien coordinates during rendering sweeps; if any active enemy intersects a player's ship or drops past the lower threshold ($Y \le 50f$), the gameplay loop terminates and redirects to `GameOverScreen`.

#### `LevelCompleteScreen.java`
A transitional display shown when an alien swarm's `aliveCount` drops to zero. It captures a state snapshot of the players' points, ammo, and remaining lives, granting bonus scores and a free life. It renders a green status announcement using Scene2D widgets, displays it for a fixed 3.0-second `DISPLAY_DURATION`, and re-instantiates `GameScreen` with the next level configurations.

#### `GameOverScreen.java`
The terminal display for victories or defeats. It flushes the frame buffer using `ScreenUtils.clear(0, 0, 0, 1)` to render a solid black background. It exposes centered options via an isolated `Stage`: "Try Again" builds a fresh `GameScreen` state from scratch, whereas "Back to Menu" releases active assets and restores the main menu state machine.

---

## 4. Save/Load Architecture and Persistence Lifecycle

### 4.1 The Save Process (Serialization)
Match state persistence is driven by LibGDX's **`Preferences`** API. This interface abstractly writes data as an XML-formatted key-value dictionary stored within the user's operating system environment. 

When `saveGame()` is invoked from the pause overlay, the following sequence runs:
1. **Global Flags:** A `hasSave` flag is set to true, alongside primitives for active `difficulty`, `twoPlayers` mode, and the current `swarmIndex` horde level.
2. **Player Profiles:** Scores, live pools, and ammunition values for Player 1 (and Player 2 if active) are extracted and saved.
3. **Swarm State:** The system records `swarmAliveCount` and the horizontal direction flag `movingRight`.
4. **Grid Matrix Iteration:** The system runs nested loops across the 5x11 `Enemy[][]` matrix. For every invader slot, it builds dynamic string keys containing row and column coordinates (e.g., `enemy_r_c_x`, `enemy_r_c_y`, `enemy_r_c_health`), saving the exact location and health of every surviving alien.
5. **Bunker Matrix Mapping:** The system traverses all barricades and their constituent `BarricadeBlock` lists, saving the integer equivalent of their structural status via `.ordinal()` (0 for `INTACT`, 1 for `DAMAGED`, 2 for `DESTROYED`) under structured keys like `barricade_b_row_col`.
6. **Synchronous Commit:** It executes `prefs.flush()`, forcing the immediate synchronous writing of memory data onto physical disk sectors to prevent save loss on application exit.

### 4.2 The Load Process (Reconstitution)
Selecting "Load Saved Game" from the main menu opens the `SpaceInvadersSaveFile` preference block. If `hasSave` reads true, variables are retrieved and forwarded to a specialized `GameScreen` constructor with `loadFromSave` set to true. 

During initialization, because entity position methods (`setX()`, `setY()`) function incrementally via compound additions (`+=`), the setup routine calculates spatial differences (`savedX - enemy.getX()`) to snap aliens back to their precise coordinates. Barricade grids loop through their blocks, repeatedly calling `.damage()` until the block's current state matches the stored ordinal index.

### 4.3 Physical Save File Storage Locations
Because the game runs on a desktop backend, LibGDX isolates user save preferences in hidden system directories rather than inside the project folder:
* **Windows OS:** `C:\Users\<Your_Username>\.prefs\SpaceInvadersSaveFile`
* **Linux & macOS:** `/home/<Your_Username>/.prefs/SpaceInvadersSaveFile` (or simply `~/.prefs/SpaceInvadersSaveFile`)

*Implementation Note:* The file is structured as a readable XML document containing clean `<entry key="X">Y</entry>` parameters. Deleting this file manually resets the system state.

---

## 5. Engineering Decisions & Code Optimization

* **Hitbox Synchronization:** To eliminate "phantom hitboxes" or visual misalignment bugs, the abstract `Entity` class encapsulates spatial bounds. By restricting coordinate mutation to protected methods, the underlying `Rectangle` bounding box updates automatically whenever a subclass shifts position.
* [cite_start]**Flag-Based Entity Disposal:** Instantaneous memory deallocation during collision loops causes instability in the rendering thread[cite: 53]. [cite_start]This project solves this by using boolean state flags (`isValid`, `isCollected`)[cite: 52]. [cite_start]Objects are marked as dead on impact and swept out of active lists during a separate cleanup pass driven by an `Iterator` loop[cite: 53].
* **Memory Leak Prevention:** Procedural graphics generation can quickly exhaust graphical memory channels. The `BarricadeTextureGenerator` resolves this by calling `.dispose()` on its `Pixmap` buffers immediately after pushing the data to the GPU texture cache, keeping the application's memory footprint light.

---

## 6. Testing Plan & Results

[cite_start]Automated test procedures are built using the **JUnit** framework (found within `GameLogicTest`), allowing developers to validate physical equations and logic rules in a *headless* environment completely detached from LibGDX's graphical engine[cite: 54].

### Test Matrix Scope:
1. [cite_start]Verification of bounding box synchronization following rapid entity coordinate changes[cite: 55].
2. [cite_start]Collision evaluation accuracy between player bounds and downward-traveling projectiles[cite: 55].
3. [cite_start]Structural correctness of generated `Swarm` grids during level initialization[cite: 55].

[cite_start]**Results:** All automated unit tests pass successfully[cite: 56]. [cite_start]The test verification confirms that collision logic ignores inactive entities (such as dead aliens or out-of-bounds bullets), avoiding false-positive collision loops[cite: 56].

---

## 7. Build and Execution Procedures

To compile and launch the game locally:

1. [cite_start]Verify that your system has **Java Development Kit (JDK 26 or higher)** and the `make` utility installed[cite: 57].
2. Clone the repository including all code files and asset structures:
   ```bash
   git clone [https://github.com/Anghievisck/Trabalho_POO.git](https://github.com/Anghievisck/Trabalho_POO.git)
   cd Trabalho_POO
