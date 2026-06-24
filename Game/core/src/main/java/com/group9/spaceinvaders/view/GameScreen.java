package com.group9.spaceinvaders.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.group9.spaceinvaders.model.Player;
import com.group9.spaceinvaders.model.Bullet;
import com.group9.spaceinvaders.model.Swarm;
import com.group9.spaceinvaders.model.Enemy;
import com.group9.spaceinvaders.model.AmmoDrop;
import com.group9.spaceinvaders.model.BarricadeManager;
import com.group9.spaceinvaders.model.Barricade;
import com.group9.spaceinvaders.model.BarricadeBlock;

import com.group9.spaceinvaders.controller.PlayerController;
import com.group9.spaceinvaders.controller.BarricadeTextureGenerator;
import com.group9.spaceinvaders.controller.SwarmController;

/**
 * The main gameplay screen containing the core game loop, rendering, 
 * and entity management logic.
 */
public class GameScreen extends ScreenAdapter {
    private SpaceInvadersGame game;
    private boolean twoPlayers;
    private int difficulty;

    private Player playerOne;
    private Player playerTwo;
    private PlayerController playerOneController;
    private PlayerController playerTwoController;

    private List<Swarm> swarms = new ArrayList<>(); 
    private List<SwarmController> swarmControllers = new ArrayList<>();  
    
    private List<Bullet> activeBullets = new ArrayList<>();
    private List<AmmoDrop> activeDrops = new ArrayList<>();

    private BarricadeManager barricadeManager;

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

    private TextureRegion ammoSprite;

    private int swarmIndex = 0;
    private float ammoSpawnTimer = 0f;

    // Pause Menu state and stage
    private boolean isPaused = false;
    private Stage pauseStage;

    /**
     * Constructs a new game screen for a fresh session.
     *
     * @param game       the main game instance
     * @param difficulty the difficulty level
     * @param twoPlayers whether the game is played in local co-op
     */
    public GameScreen(SpaceInvadersGame game, int difficulty, boolean twoPlayers) {
        this(game, difficulty, twoPlayers, 0, 0, 3, 5, 0, 3, 5, false);
    }

    /**
     * Constructs a game screen with specific states, useful for loading saves or transitioning levels.
     *
     * @param game         the main game instance
     * @param difficulty   the difficulty level
     * @param twoPlayers   whether the game is played in local co-op
     * @param initialLevel the starting level/horde index
     * @param p1Points     player 1 current score
     * @param p1Lives      player 1 current lives
     * @param p1Ammo       player 1 current ammo
     * @param p2Points     player 2 current score
     * @param p2Lives      player 2 current lives
     * @param p2Ammo       player 2 current ammo
     * @param loadFromSave whether the enemies should be loaded from the save file
     */
    public GameScreen(SpaceInvadersGame game, int difficulty, boolean twoPlayers, int initialLevel,
                      int p1Points, int p1Lives, int p1Ammo,
                      int p2Points, int p2Lives, int p2Ammo, boolean loadFromSave) {
        this.game = game;
        this.difficulty = difficulty;
        this.twoPlayers = twoPlayers;
        this.swarmIndex = initialLevel; 

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);

        TextureAtlas atlas = game.assets.get("sprites/gameplay.atlas", TextureAtlas.class);
        TextureRegion bulletSprite = atlas.findRegion("bullet_placeholder");
        ammoSprite = atlas.findRegion("ammo_drop");

        playerOne = new Player(200, 50, 50, 24, atlas.findRegion("player1"), bulletSprite, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.UP);
        playerOne.points = p1Points;
        playerOne.lives = p1Lives;
        playerOne.ammo = p1Ammo;
        playerOneController = new PlayerController(game, playerOne);

        if (this.twoPlayers) {
            playerTwo = new Player(500, 50, 50, 24, atlas.findRegion("player2"), bulletSprite, Input.Keys.A, Input.Keys.D, Input.Keys.W);
            playerTwo.points = p2Points;
            playerTwo.lives = p2Lives;
            playerTwo.ammo = p2Ammo;
            playerTwoController = new PlayerController(game, playerTwo);
        }

        TextureRegion intact = BarricadeTextureGenerator.createIntact(15);
        TextureRegion damaged = BarricadeTextureGenerator.createDamaged(15);
        TextureRegion destroyed = BarricadeTextureGenerator.createDestroyed(15);

        barricadeManager = new BarricadeManager(8, intact, damaged, destroyed);
        // Place barricades at y = 300 (between enemies and player)
        barricadeManager.createBarricades(4, 800, 150);

        List<TextureRegion> enemySprites = new ArrayList<>();
        // 0
        enemySprites.add(atlas.findRegion("alien_crab1"));
        enemySprites.add(atlas.findRegion("alien_crab2"));
        // 2
        enemySprites.add(atlas.findRegion("alien_octopus1"));
        enemySprites.add(atlas.findRegion("alien_octopus2"));
        // 4
        enemySprites.add(atlas.findRegion("alien_squid1"));
        enemySprites.add(atlas.findRegion("alien_squid2"));

        List<TextureRegion> enemyBulletSprites = new ArrayList<>();
        enemyBulletSprites.add(atlas.findRegion("bullet_placeholder"));

        List<Float> enemyBulletSpeeds = new ArrayList<>();
        enemyBulletSpeeds.add(-250f);
        enemyBulletSpeeds.add(-300f);
        enemyBulletSpeeds.add(-350f);

        for (int i = 0; i < 3; i++) {
            swarms.add(new Swarm((float)50, (float)300, (float)30, (float)30, (float)15, (i + 1) * 1.5f * difficulty, enemySprites, 100, enemyBulletSprites, enemyBulletSpeeds));
            swarmControllers.add(new SwarmController(swarms.get(i), 800f, difficulty, game)); 
        }

        // IF LOADED FROM A SAVE: Restores the custom positions and health of enemies on screen
        if (loadFromSave) {
            Preferences prefs = Gdx.app.getPreferences("SpaceInvadersSaveFile");
            Swarm currentSwarm = swarms.get(swarmIndex);
            currentSwarm.aliveCount = prefs.getInteger("swarmAliveCount", currentSwarm.aliveCount);
            currentSwarm.movingRight = prefs.getBoolean("movingRight", currentSwarm.movingRight);
            
            for (int r = 0; r < currentSwarm.rows; r++) {
                for (int c = 0; c < currentSwarm.cols; c++) {
                    Enemy enemy = currentSwarm.enemies[r][c];
                    float savedX = prefs.getFloat("enemy_" + r + "_" + c + "_x", enemy.getX());
                    float savedY = prefs.getFloat("enemy_" + r + "_" + c + "_y", enemy.getY());
                    
                    // Since setX/setY inherit incremental behavior (+=), we apply the vector difference
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
                        // We can't set state directly, so we call damage() until we reach the desired state
                        BarricadeBlock.State targetState = BarricadeBlock.State.values()[stateOrdinal];
                        while (block.getState() != targetState) {
                            block.damage();
                        }
                    }
                }
            }
        }
        
        // Regular game HUD initialization
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

        // PAUSE INTERFACE CONSTRUCTION (TRIGGERED BY 'ESC')
        pauseStage = new Stage(new FitViewport(800, 600));
        Skin skin = game.assets.get("ui/spaceinvaders.json", Skin.class);
        Table pauseTable = new Table();
        pauseTable.setFillParent(true);

        TextButton btnResume = new TextButton("Resume Game", skin);
        TextButton btnSave = new TextButton("Save Progress", skin);
        TextButton btnMenu = new TextButton("Exit to Menu", skin);

        btnResume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isPaused = false;
                Gdx.input.setInputProcessor(null); // Releases the mouse from the pause stage
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

    /**
     * Saves the current game state, including player stats, enemies, and barricades, into the preferences file.
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

        // COMPLETE ENEMY SAVING
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
        System.out.println("Game and enemy positions saved successfully via Pause Menu!");
    }

    @Override
    public void render(float delta) {
        // Captures the ESC key to toggle the game's Pause state
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused;
            if (isPaused) {
                Gdx.input.setInputProcessor(pauseStage); // Transfers mouse clicks to the pause menu
            } else {
                Gdx.input.setInputProcessor(null);
            }
        }

        // --- LOGIC UPDATED ONLY IF NOT PAUSED ---
        if (!isPaused) {

            float ammoSpawnLimit = this.twoPlayers ? 2.0f : 4.0f;
            ammoSpawnTimer += delta;

            if (ammoSpawnTimer >= ammoSpawnLimit) { // We use the new dynamic limit
                ammoSpawnTimer = 0f;
                float randomX = com.badlogic.gdx.math.MathUtils.random(20f, 780f); 
                activeDrops.add(new AmmoDrop(randomX, 50f, 30, 15, ammoSprite));
            }

            playerOneController.update(delta, activeBullets); 
            scoreLabelP1.setText("P1: " + playerOne.points);
            livesLabelP1.setText("Lives: " + formatLives(playerOne.lives));
            ammoLabelP1.setText("Ammo: " + playerOne.ammo);

            if (this.twoPlayers) {
                playerTwoController.update(delta, activeBullets); 
                scoreLabelP2.setText("P2: " + playerTwo.points);
                livesLabelP2.setText("Lives: " + formatLives(playerTwo.lives));
                ammoLabelP2.setText("Ammo: " + playerTwo.ammo);
            }

            swarmControllers.get(swarmIndex).update(delta, activeBullets, activeDrops); 

            // CONDITION: ENEMIES REACH YOU OR CROSS THE PLAYER'S LINE -> GAME OVER
            Swarm currentSwarm = swarms.get(swarmIndex);
            for (int r = 0; r < currentSwarm.rows; r++) {
                for (int c = 0; c < currentSwarm.cols; c++) {
                    Enemy enemy = currentSwarm.enemies[r][c];
                    if (enemy != null && enemy.health > 0) {
                        // Checks direct collision with the ship's hitboxes or if they went below Y=50 (player axis)
                        if ((playerOne.lives > 0 && enemy.getHitbox().overlaps(playerOne.getHitbox())) || enemy.getY() <= 50f) {
                            game.setScreen(new GameOverScreen(game, false, twoPlayers, this.difficulty));
                            return;
                        }
                        if (this.twoPlayers && playerTwo.lives > 0 && enemy.getHitbox().overlaps(playerTwo.getHitbox())) {
                            game.setScreen(new GameOverScreen(game, false, twoPlayers, this.difficulty));
                            return;
                        }
                    }
                }
            }

            // Traditional defeat check (No lives)
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

            // Level Transition / Horde Victory
            if (swarms.get(swarmIndex).aliveCount <= 0) {
                playerOne.points += 100; 
                playerOne.heal(1);
                if (twoPlayers) {
                    playerTwo.points += 100;
                    playerTwo.heal(1);
                }

                difficulty++;
                
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

        // --- DRAWING / GRAPHICS (Continues rendering even when paused) ---
        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        
        if (playerOne.lives > 0) playerOne.draw(batch);
        if (this.twoPlayers && playerTwo.lives > 0) playerTwo.draw(batch);

        Swarm currentSwarm = swarms.get(swarmIndex);
        for (int r = 0; r < currentSwarm.rows; r++) {
            for (int c = 0; c < currentSwarm.cols; c++) {
                Enemy enemy = currentSwarm.enemies[r][c];
                if (enemy != null && enemy.health > 0) {
                    enemy.draw(batch);
                    barricadeManager.checkEnemyCollision(enemy);
                }
            }
        }

        barricadeManager.draw(batch);

        // Drops and Bullets update physics only if the game is not paused
        Iterator<AmmoDrop> dropIter = activeDrops.iterator();
        while (dropIter.hasNext()) {
            AmmoDrop drop = dropIter.next();
            if (!isPaused) drop.update(delta);
            drop.draw(batch);

            if (!isPaused && playerOne.lives > 0 && playerOne.getHitbox().overlaps(drop.getHitbox())) {
                playerOne.ammo += 3;
                drop.isCollected = true;
            }

            if (!isPaused && this.twoPlayers && playerTwo.lives > 0 && playerTwo.getHitbox().overlaps(drop.getHitbox())) {
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

        // Draws the main HUD interface
        hudStage.act(delta);
        hudStage.draw();

        // If in Pause mode, draws the Menu window on top of everything
        if (isPaused) {
            pauseStage.act(delta);
            pauseStage.draw();
        }
    }

    /**
     * Formats the life count into a string of "X " characters.
     *
     * @param lifeCount the player's life count
     * @return a formatted string representing lives
     */
    private String formatLives(int lifeCount) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.max(0, lifeCount); i++) {
            sb.append("X "); 
        }
        return sb.toString();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudStage.getViewport().update(width, height, true);
        pauseStage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose(); 
        hudStage.dispose();
        pauseStage.dispose(); // Discards the pause stage cleanly
        font.dispose();
    }
}