# Space Invaders - Comprehensive Technical Project Report

**Group Identification: Group 9**
* Gabriel Inumaru Esteves - USP Number: 15453487
* Matheus Spineli de Paiva - USP Number: 14598682
* Pedro Luís Anghievisck - USP Number: 15656521

---

## 1. Requirements

The application is a fully functional 2D Space Invaders clone designed and implemented to satisfy all core academic requirements:
* **Framework Integration:** Uses LibGDX for seamless screen state management, asset lifecycle control, and 2D hardware-accelerated graphics rendering.
* **Core Mechanics:** Accurate real-time entity movement, automated and user-driven shooting physics, and pixel-consistent bounding box collision detection.
* **Multiplayer Cooperative Mode:** Features a local, shared-screen two-player cooperative experience where Player 1 and Player 2 possess completely separate control bindings, independent coordinate handling, and individual life counters.
* **Progression System:** A structured 3-level progression system (Hordes/Swarms 0 through 2). The difficulty scales dynamically, affecting enemy movement velocity and shooting frequency multipliers.
* **State Management:** Seamless real-time transitions across multiple screen states (Main Menu, Active Gameplay, Pause Menu, Level Transition, and Game Over).
* **Persistence:** A persistent Save/Load subsystem that captures and reconstitutes the exact state of a match via local file serialization.

### 🌟 Extra Feature: Tactical Ammunition Management System
Departing from the traditional infinite-shooting mechanic of classic arcades, this project introduces an original ammunition management layer. Players start with limited ammo and must actively monitor their reserves. **Ammo Drops** (`AmmoDrop.java`) spawn randomly along the horizontal play area. Players must navigate toward these drops to reload. This design introduces resource scarcity and tactical risk-reward calculations into the core gameplay.

---

## 2. Project Description

### 2.1 Brief Architecture Overview
Developed in Java using the LibGDX framework, players control defensive spaceships situated at the bottom of the screen to protect Earth against synchronized alien formations that advance downward and execute period attacks. 

The system strictly adheres to the **Model-View-Controller (MVC)** architectural pattern to untangle data representation from user input and rendering systems:
1. **Model:** Encapsulates core state variables, spatial vectors, and geometric hitboxes (`Entity`, `Player`, `Enemy`, `Barricade`, etc.).
2. **View:** Manages visual context, UI layouts (`Stage`, `Table`, `Label`), and graphics drawing loops.
3. **Controller:** Processes physical input polled from hardware devices and executes algorithmic rules upon the Model.

Real-time scores, ammunition counts, and health tracking dictate the state machine; the session terminates in a defeat screen only when both players exhaust their lives or when the alien formation breaches the vital defensive boundary line established at Y <= 50.

### 2.2 OOP Concepts Applied
* **Composition:** * The `Swarm` class is composed of a 2D array (`Enemy[][]`) of individual `Enemy` objects. It serves as a unified group controller, computing macro movement sweeps as a single unit while allowing separate alien instances to maintain independent internal health values, unique shooting speeds, and row-based visual sprites.
  * The `BarricadeManager` class is composed of a collection of `Barricade` instances, which in turn are composed of a nested 2D list (`List<List<BarricadeBlock>>`) outlining a physical defense matrix.
* **Polymorphism:** * **Method Overriding:** Demonstrated in the `AmmoDrop` class, which overrides the base `draw(SpriteBatch batch)` method from the abstract `Entity` class to modify rendering behaviors, drawing its sprite rotated at a fixed 90-degree angle. Additionally, child entities implement customized variations of the abstract `update(float delta)` signature.
  * **Dynamic Runtime Evaluation:** Collision handlers analyze the `origin` field of a `Bullet` using the `instanceof` keyword (e.g., `bullet.origin instanceof Enemy`). This allows the physics engine to differentiate between hostile fire and friendly fire, completely neutralizing self-damage or friendly-fire exploits.
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
Controls projectile entities generated by firing actions. It features a `speed` vector that is positive for upward movement (player shots) and negative for downward paths (alien fire). It preserves an `origin
