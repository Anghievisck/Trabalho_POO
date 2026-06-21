package com.group9.spaceinvaders.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

import com.group9.spaceinvaders.model.Player;
import com.group9.spaceinvaders.controller.PlayerController;

import com.group9.spaceinvaders.model.PlayerBullet;
import com.group9.spaceinvaders.controller.PlayerBulletController;

// 1. Importando as novas classes do Swarm
import com.group9.spaceinvaders.model.Swarm;
import com.group9.spaceinvaders.controller.SwarmController;
import com.group9.spaceinvaders.model.Enemy;

// ----- BARRIER IMPORTS -----
import com.group9.spaceinvaders.model.BarricadeManager;
import com.group9.spaceinvaders.model.Barricade;
import com.group9.spaceinvaders.model.BarricadeBlock;
import com.group9.spaceinvaders.util.BarricadeTextureGenerator;

/**
 * Main game screen containing all gameplay logic, rendering, and UI.
 * Manages players, enemies (swarms), bullets, power‑ups, and barricades.
 * <p>
 * The barricades are saved and restored along with the rest of the game state.
 */
public class GameScreen extends ScreenAdapter {
<<<<<<< Updated upstream
=======

    // -------------------- FIELDS --------------------

    private SpaceInvadersGame game;
    private boolean twoPlayers;
    private int difficulty;

>>>>>>> Stashed changes
    private Player playerOne;
    private PlayerController playerOneController;

<<<<<<< Updated upstream
    // 2. Declarando o Swarm e seu Controller
    private Swarm swarm;
    private SwarmController swarmController;  
    private PlayerBullet playerBullet;
    private PlayerBulletController playerBulletController;

    private ShapeRenderer shapeRenderer;
  
    public GameScreen() {
        playerOne = new Player(300, 50, 50, 50);
        playerBullet = new PlayerBullet(-10, -10, 5, 15);
=======
    // Barricade manager
    private BarricadeManager barricadeManager;

    private List<Swarm> swarms = new ArrayList<>();
    private List<SwarmController> swarmControllers = new ArrayList<>();

    private List<Bullet> activeBullets = new ArrayList<>();
    private List<AmmoDrop> activeDrops = new ArrayList<>();

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Stage hudStage;
    private Label scoreLabelP1;
    private Label scoreLabelP2;
    private Label livesLabelP1;
    private Label livesLabelP2;
    private Label ammoLabelP1;
    private Label ammoLabelP2;
    private BitmapFont font;
>>>>>>> Stashed changes

        playerOneController = new PlayerController(playerOne);

<<<<<<< Updated upstream
        // 3. Inicializa a nuvem de inimigos
        // Parâmetros: startX, startY, width, height, padding (espaçamento)
        // Coloquei Y = 400 para eles começarem na parte de cima da tela
        swarm = new Swarm(50, 400, 30, 30, 15);
        
        // O Controller precisa da largura da tela. 
        // O Gdx.graphics.getWidth() pega o tamanho exato da janela do seu jogo!
        swarmController = new SwarmController(swarm, Gdx.graphics.getWidth());
        playerBulletController = new PlayerBulletController(playerBullet);

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        // --- 4. FASE DE ATUALIZAÇÃO LÓGICA (CONTROLLERS) ---
        playerOneController.update(delta);
        swarmController.update(delta); // Faz o zigue-zague matemático acontecer
        playerBulletController.update(playerOne, delta);

        // --- 5. FASE DE DESENHO (VIEW) ---
        ScreenUtils.clear(0, 0, 0, 1);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Desenha o Player (Verde)
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(playerOne.bounds.x, playerOne.bounds.y, playerOne.bounds.width, playerOne.bounds.height);

        // Desenha o Swarm (Vermelho)
        shapeRenderer.setColor(Color.RED);
        
        // Percorre a nossa matriz 5x11
        for (int r = 0; r < swarm.rows; r++) {
            for (int c = 0; c < swarm.cols; c++) {
                Enemy enemy = swarm.enemies[r][c];
                
                // Só manda a placa de vídeo desenhar se o inimigo ainda estiver vivo
                if (enemy.isAlive) {
                    shapeRenderer.rect(enemy.bounds.x, enemy.bounds.y, enemy.bounds.width, enemy.bounds.height);
=======
    private boolean isPaused = false;
    private Stage pauseStage;

    // -------------------- CONSTRUCTORS --------------------

    /**
     * Default constructor – delegates to the full constructor with default values.
     */
    public GameScreen(SpaceInvadersGame game, int difficulty, boolean twoPlayers) {
        this(game, difficulty, twoPlayers, 0, 0, 3, 5, 0, 3, 5, false);
    }

    /**
     * Full constructor that initialises the entire game state.
     *
     * @param game         the parent game instance
     * @param difficulty   difficulty level (affects enemy speed and fire rate)
     * @param twoPlayers   whether this is a two‑player game
     * @param initialLevel starting swarm index (0‑based)
     * @param p1Points     initial points for player 1
     * @param p1Lives      initial lives for player 1
     * @param p1Ammo       initial ammo for player 1
     * @param p2Points     initial points for player 2
     * @param p2Lives      initial lives for player 2
     * @param p2Ammo       initial ammo for player 2
     * @param loadFromSave if {@code true}, restores enemy positions from
     *                     preferences
     */
    public GameScreen(SpaceInvadersGame game, int difficulty, boolean twoPlayers, int initialLevel,
            int p1Points, int p1Lives, int p1Ammo,
            int p2Points, int p2Lives, int p2Ammo, boolean loadFromSave) {
        this.game = game;
        this.difficulty = difficulty;
        this.twoPlayers = twoPlayers;
        this.swarmIndex = initialLevel;

        // ---- Graphics setup ----
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);

        // ---- Load assets ----
        TextureAtlas atlas = game.assets.get("sprites/gameplay.atlas", TextureAtlas.class);
        TextureRegion playerSprite = atlas.findRegion("player_placeholder");
        TextureRegion bulletSprite = atlas.findRegion("bullet_placeholder");

        // ---- Create players ----
        playerOne = new Player(200, 50, 50, 50, playerSprite, bulletSprite,
                Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.SPACE);
        playerOne.points = p1Points;
        playerOne.lives = p1Lives;
        playerOne.ammo = p1Ammo;
        playerOneController = new PlayerController(game, playerOne);

        if (this.twoPlayers) {
            playerTwo = new Player(500, 50, 50, 50, playerSprite, bulletSprite,
                    Input.Keys.A, Input.Keys.D, Input.Keys.W);
            playerTwo.points = p2Points;
            playerTwo.lives = p2Lives;
            playerTwo.ammo = p2Ammo;
            playerTwoController = new PlayerController(game, playerTwo);
        }

        // ---- Create barricades (using generated textures) ----
        // Use English method names for consistency
        TextureRegion intact = BarricadeTextureGenerator.createIntact(15);
        TextureRegion damaged = BarricadeTextureGenerator.createDamaged(15);
        TextureRegion destroyed = BarricadeTextureGenerator.createDestroyed(15);

        barricadeManager = new BarricadeManager(15, intact, damaged, destroyed);
        // Place barricades at y = 300 (between enemies and player)
        barricadeManager.createBarricades(4, 800, 300);

        // ---- Create enemy swarms ----
        List<TextureRegion> enemySprites = new ArrayList<>();
        enemySprites.add(atlas.findRegion("alien_crab1"));
        enemySprites.add(atlas.findRegion("alien_crab2"));

        List<TextureRegion> enemyBulletSprites = new ArrayList<>();
        enemyBulletSprites.add(atlas.findRegion("bullet_placeholder"));

        List<Float> enemyBulletSpeeds = new ArrayList<>();
        enemyBulletSpeeds.add(-250f);

        for (int i = 0; i < 3; i++) {
            swarms.add(new Swarm(50f, 400f, 30f, 30f, 15f,
                    (i + 1) * 1.5f * difficulty, enemySprites, 100,
                    enemyBulletSprites, enemyBulletSpeeds));
            swarmControllers.add(new SwarmController(swarms.get(i), 800f, difficulty));
        }

        // ---- Restore from save (if requested) ----
        if (loadFromSave) {
            restoreGameState();
        }

        // ---- HUD ----
        hudStage = new Stage(new FitViewport(800, 600));
        font = new BitmapFont();
        font.getData().setScale(1.2f);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        scoreLabelP1 = new Label("P1: 0", labelStyle);
        scoreLabelP1.setPosition(20, 560);
        hudStage.addActor(scoreLabelP1);

        livesLabelP1 = new Label("Lives: XXX", labelStyle);
        livesLabelP1.setPosition(120, 560);
        hudStage.addActor(livesLabelP1);

        ammoLabelP1 = new Label("Ammo: 5", labelStyle);
        ammoLabelP1.setPosition(260, 560);
        hudStage.addActor(ammoLabelP1);

        if (this.twoPlayers) {
            scoreLabelP2 = new Label("P2: 0", labelStyle);
            scoreLabelP2.setPosition(500, 560);
            hudStage.addActor(scoreLabelP2);

            livesLabelP2 = new Label("Lives: XXX", labelStyle);
            livesLabelP2.setPosition(600, 560);
            hudStage.addActor(livesLabelP2);

            ammoLabelP2 = new Label("Ammo: 5", labelStyle);
            ammoLabelP2.setPosition(710, 560);
            hudStage.addActor(ammoLabelP2);
        }

        // ---- Pause menu ----
        pauseStage = new Stage(new FitViewport(800, 600));
        Skin skin = game.assets.get("ui/spaceinvaders.json", Skin.class);
        Table pauseTable = new Table();
        pauseTable.setFillParent(true);

        TextButton btnResume = new TextButton("Resume", skin);
        TextButton btnSave = new TextButton("Save Progress", skin);
        TextButton btnMenu = new TextButton("Main Menu", skin);

        btnResume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isPaused = false;
                Gdx.input.setInputProcessor(null);
            }
        });

        btnSave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveGame();
            }
        });

        btnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        pauseTable.add(btnResume).width(280).height(45).padBottom(15).row();
        pauseTable.add(btnSave).width(280).height(45).padBottom(15).row();
        pauseTable.add(btnMenu).width(280).height(45);
        pauseStage.addActor(pauseTable);
    }

    // -------------------- SAVE / LOAD --------------------

    /**
     * Saves the current game state (players, swarms, barricades, difficulty) to
     * preferences.
     */
    private void saveGame() {
        Preferences prefs = Gdx.app.getPreferences("SpaceInvadersSaveFile");
        prefs.putBoolean("hasSave", true);
        prefs.putInteger("difficulty", this.difficulty);
        prefs.putBoolean("twoPlayers", this.twoPlayers);
        prefs.putInteger("swarmIndex", this.swarmIndex);

        prefs.putInteger("p1Points", playerOne.points);
        prefs.putInteger("p1Lives", playerOne.lives);
        prefs.putInteger("p1Ammo", playerOne.ammo);

        if (this.twoPlayers) {
            prefs.putInteger("p2Points", playerTwo.points);
            prefs.putInteger("p2Lives", playerTwo.lives);
            prefs.putInteger("p2Ammo", playerTwo.ammo);
        }

        // ---- Save swarm state ----
        Swarm currentSwarm = swarms.get(swarmIndex);
        prefs.putInteger("swarmAliveCount", currentSwarm.aliveCount);
        prefs.putBoolean("movingRight", currentSwarm.movingRight);

        for (int r = 0; r < currentSwarm.rows; r++) {
            for (int c = 0; c < currentSwarm.cols; c++) {
                Enemy enemy = currentSwarm.enemies[r][c];
                prefs.putFloat("enemy_" + r + "_" + c + "_x", enemy.getX());
                prefs.putFloat("enemy_" + r + "_" + c + "_y", enemy.getY());
                prefs.putInteger("enemy_" + r + "_" + c + "_health", enemy.health);
            }
        }

        // ---- Save barricade state ----
        List<Barricade> barricades = barricadeManager.getBarricades();
        for (int b = 0; b < barricades.size(); b++) {
            Barricade barricade = barricades.get(b);
            List<List<BarricadeBlock>> grid = barricade.getGrid();
            for (int row = 0; row < grid.size(); row++) {
                for (int col = 0; col < grid.get(row).size(); col++) {
                    BarricadeBlock block = grid.get(row).get(col);
                    // Save state as integer: 0=INTACT, 1=DAMAGED, 2=DESTROYED
                    prefs.putInteger("barricade_" + b + "_" + row + "_" + col, block.getState().ordinal());
                }
            }
        }

        prefs.flush();
        System.out.println("Game state (including barricades) saved successfully!");
    }

    /**
     * Restores the game state from preferences.
     */
    private void restoreGameState() {
        Preferences prefs = Gdx.app.getPreferences("SpaceInvadersSaveFile");

        // Restore swarm
        Swarm currentSwarm = swarms.get(swarmIndex);
        currentSwarm.aliveCount = prefs.getInteger("swarmAliveCount", currentSwarm.aliveCount);
        currentSwarm.movingRight = prefs.getBoolean("movingRight", currentSwarm.movingRight);

        for (int r = 0; r < currentSwarm.rows; r++) {
            for (int c = 0; c < currentSwarm.cols; c++) {
                Enemy enemy = currentSwarm.enemies[r][c];
                float savedX = prefs.getFloat("enemy_" + r + "_" + c + "_x", enemy.getX());
                float savedY = prefs.getFloat("enemy_" + r + "_" + c + "_y", enemy.getY());
                enemy.setX(savedX - enemy.getX());
                enemy.setY(savedY - enemy.getY());
                enemy.health = prefs.getInteger("enemy_" + r + "_" + c + "_health", enemy.health);
            }
        }

        // Restore barricades
        List<Barricade> barricades = barricadeManager.getBarricades();
        for (int b = 0; b < barricades.size(); b++) {
            Barricade barricade = barricades.get(b);
            List<List<BarricadeBlock>> grid = barricade.getGrid();
            for (int row = 0; row < grid.size(); row++) {
                for (int col = 0; col < grid.get(row).size(); col++) {
                    BarricadeBlock block = grid.get(row).get(col);
                    int stateOrdinal = prefs.getInteger("barricade_" + b + "_" + row + "_" + col,
                            BarricadeBlock.State.INTACT.ordinal());
                    // Restore the state by damaging the block appropriately
                    // We can't set state directly, so we call damage() until we reach the desired
                    // state
                    BarricadeBlock.State targetState = BarricadeBlock.State.values()[stateOrdinal];
                    while (block.getState() != targetState) {
                        block.damage();
                    }
                }
            }
        }
    }

    // -------------------- RENDER LOOP --------------------

    @Override
    public void render(float delta) {
        // ---- Pause toggle ----
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused;
            Gdx.input.setInputProcessor(isPaused ? pauseStage : null);
        }

        // ---- GAME LOGIC (only if not paused) ----
        if (!isPaused) {
            // Spawn ammo drops
            ammoSpawnTimer += delta;
            if (ammoSpawnTimer >= 4.0f) {
                ammoSpawnTimer = 0f;
                float randomX = com.badlogic.gdx.math.MathUtils.random(20f, 780f);
                activeDrops.add(new AmmoDrop(randomX, 50f, 15, 30, playerOne.bulletSprite));
            }

            // Update players
            playerOneController.update(delta, activeBullets);
            updateHUDP1();

            if (this.twoPlayers) {
                playerTwoController.update(delta, activeBullets);
                updateHUDP2();
            }

            // Update swarm
            swarmControllers.get(swarmIndex).update(delta, activeBullets, activeDrops);

            // Check for game over (enemies reaching player or crossing the line)
            Swarm currentSwarm = swarms.get(swarmIndex);
            for (int r = 0; r < currentSwarm.rows; r++) {
                for (int c = 0; c < currentSwarm.cols; c++) {
                    Enemy enemy = currentSwarm.enemies[r][c];
                    if (enemy != null && enemy.health > 0) {
                        if ((playerOne.lives > 0 && enemy.getHitbox().overlaps(playerOne.getHitbox()))
                                || enemy.getY() <= 50f) {
                            game.setScreen(new GameOverScreen(game, false, twoPlayers, this.difficulty));
                            return;
                        }
                        if (this.twoPlayers && playerTwo.lives > 0
                                && enemy.getHitbox().overlaps(playerTwo.getHitbox())) {
                            game.setScreen(new GameOverScreen(game, false, twoPlayers, this.difficulty));
                            return;
                        }
                    }
                }
            }

            // Check for player defeat (no lives)
            if (this.twoPlayers) {
                if (playerOne.lives <= 0 && playerTwo.lives <= 0) {
                    game.setScreen(new GameOverScreen(game, false, twoPlayers, this.difficulty));
                    return;
                }
            } else {
                if (playerOne.lives <= 0) {
                    game.setScreen(new GameOverScreen(game, false, twoPlayers, this.difficulty));
                    return;
                }
            }

            // Level transition / victory
            if (swarms.get(swarmIndex).aliveCount <= 0) {
                playerOne.points += 100;
                playerOne.lives += 1;
                if (twoPlayers) {
                    playerTwo.points += 100;
                    playerTwo.lives += 1;
                }

                if (swarmIndex < 2) {
                    game.setScreen(new LevelCompleteScreen(game, difficulty, twoPlayers, swarmIndex,
                            playerOne.points, playerOne.lives, playerOne.ammo,
                            this.twoPlayers ? playerTwo.points : 0,
                            this.twoPlayers ? playerTwo.lives : 0,
                            this.twoPlayers ? playerTwo.ammo : 0));
                    return;
                } else {
                    game.setScreen(new GameOverScreen(game, true, twoPlayers, this.difficulty));
                    return;
                }
            }
        }

        // ---- RENDERING (always happens, even when paused) ----
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // Draw players
        if (playerOne.lives > 0)
            playerOne.draw(batch);
        if (this.twoPlayers && playerTwo.lives > 0)
            playerTwo.draw(batch);

        // Draw enemies
        Swarm currentSwarm = swarms.get(swarmIndex);
        for (int r = 0; r < currentSwarm.rows; r++) {
            for (int c = 0; c < currentSwarm.cols; c++) {
                Enemy enemy = currentSwarm.enemies[r][c];
                if (enemy != null && enemy.health > 0) {
                    enemy.draw(batch);
>>>>>>> Stashed changes
                }
            }
        }

<<<<<<< Updated upstream
        // Desenha o Tiro (Branco)
        shapeRenderer.setColor(Color.WHITE);
        
        if(playerBullet.isValid){
            playerOne.canShoot = false;
            shapeRenderer.rect(playerBullet.bounds.x, playerBullet.bounds.y, playerBullet.bounds.width, playerBullet.bounds.height);
        } else {
            playerOne.canShoot = true;
        }
        
        shapeRenderer.end();
=======
        // ---- DRAW BARRICADES ----
        barricadeManager.draw(batch);

        // ---- UPDATE AND DRAW AMMO DROPS ----
        Iterator<AmmoDrop> dropIter = activeDrops.iterator();
        while (dropIter.hasNext()) {
            AmmoDrop drop = dropIter.next();
            if (!isPaused)
                drop.update(delta);
            drop.draw(batch);

            // Collision with players
            if (!isPaused && playerOne.lives > 0 && playerOne.getHitbox().overlaps(drop.getHitbox())) {
                playerOne.ammo += 3;
                drop.isCollected = true;
            }
            if (!isPaused && this.twoPlayers && playerTwo.lives > 0
                    && playerTwo.getHitbox().overlaps(drop.getHitbox())) {
                playerTwo.ammo += 3;
                drop.isCollected = true;
            }

            if (drop.isCollected) {
                dropIter.remove();
            }
        }

        // ---- UPDATE AND DRAW BULLETS (with barricade collision) ----
        Iterator<Bullet> bulletIter = activeBullets.iterator();
        while (bulletIter.hasNext()) {
            Bullet b = bulletIter.next();
            if (!isPaused)
                b.update(delta);
            b.draw(batch);

            // --- BARRICADE COLLISION ---
            if (b.isValid && barricadeManager.checkBulletCollision(b.getHitbox())) {
                b.isValid = false; // bullet stops and is removed
            }

            if (!b.isValid) {
                bulletIter.remove();
            }
        }

        batch.end();

        // ---- HUD and pause overlay ----
        hudStage.act(delta);
        hudStage.draw();

        if (isPaused) {
            pauseStage.act(delta);
            pauseStage.draw();
        }
    }

    // -------------------- HUD HELPERS --------------------

    /** Updates the HUD labels for player 1. */
    private void updateHUDP1() {
        scoreLabelP1.setText("P1: " + playerOne.points);
        livesLabelP1.setText("Lives: " + formatLives(playerOne.lives));
        ammoLabelP1.setText("Ammo: " + playerOne.ammo);
    }

    /** Updates the HUD labels for player 2. */
    private void updateHUDP2() {
        scoreLabelP2.setText("P2: " + playerTwo.points);
        livesLabelP2.setText("Lives: " + formatLives(playerTwo.lives));
        ammoLabelP2.setText("Ammo: " + playerTwo.ammo);
    }

    /**
     * Returns a string of 'X' characters representing the number of lives.
     *
     * @param lives number of lives
     * @return a string like "X X X" for 3 lives
     */
    private String formatLives(int lives) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.max(0, lives); i++) {
            sb.append("X ");
        }
        return sb.toString();
    }

    // -------------------- LIFECYCLE --------------------

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudStage.getViewport().update(width, height, true);
        pauseStage.getViewport().update(width, height, true);
>>>>>>> Stashed changes
    }

    @Override
    public void dispose() {
<<<<<<< Updated upstream
        shapeRenderer.dispose(); // Mantendo o gerenciamento de memória limpo!
=======
        batch.dispose();
        hudStage.dispose();
        pauseStage.dispose();
        font.dispose();
>>>>>>> Stashed changes
    }
}
