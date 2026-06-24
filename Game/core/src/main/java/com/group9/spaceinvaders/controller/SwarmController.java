package com.group9.spaceinvaders.controller;

import com.badlogic.gdx.math.Rectangle;
import com.group9.spaceinvaders.model.Enemy;
import com.group9.spaceinvaders.model.Player;
import com.group9.spaceinvaders.model.Bullet;
import com.group9.spaceinvaders.model.Swarm;
import com.group9.spaceinvaders.model.AmmoDrop;
import com.group9.spaceinvaders.view.SpaceInvadersGame;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.audio.Sound;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for managing the logic, movement, and shooting 
 * behavior of the enemy swarm.
 */
public class SwarmController {
    private Swarm swarm;
    private float screenWidth;
    private int spriteCycle = 0;
    private float timeSinceLastSpriteUpdate = 0f;

    private SpaceInvadersGame game;

    // Enemy shooting controllers
    private float shootTimer = 0f;
    private float shootInterval; 
    /**
     * Constructs a SwarmController.
     *
     * @param swarm       the swarm model to control
     * @param screenWidth the width of the screen to handle edge bouncing
     * @param difficulty  the difficulty level which dictates the shooting rate
     * @param game        the main game instance for asset access
     */
    public SwarmController(Swarm swarm, float screenWidth, int difficulty, SpaceInvadersGame game) {
        this.swarm = swarm;
        this.screenWidth = screenWidth;
        this.game = game;
        
        // Difficulty dictates the swarm's shooting rate
        this.shootInterval = Math.max(0.5f, 2.0f - (difficulty * 0.2f)); 
    }
    /**
     * Updates the swarm's position, handles enemy shooting logic, and checks collisions.
     *
     * @param delta         the time elapsed since the last frame
     * @param activeBullets the list of active bullets in the game
     * @param activeDrops   the list of active ammo drops in the game
     */
    public void update(float delta, List<Bullet> activeBullets, List<AmmoDrop> activeDrops) {
        float moveX = swarm.speed * delta;
        if (!swarm.movingRight) {
            moveX = -moveX; 
        }

        boolean hitEdge = false;
        timeSinceLastSpriteUpdate += delta;
        
        // Updates the enemy shooting timer
        shootTimer += delta; 

        List<Bullet> enemyBulletsToSpawn = new ArrayList<>();

        // Logic to decide if an enemy shoots in this exact frame
        boolean willShootThisFrame = false;
        int targetEnemyToShoot = 0;
        
        if (shootTimer >= shootInterval && swarm.aliveCount > 0) {
            willShootThisFrame = true;
            shootTimer = 0f;
            // Randomly selects one of the alive enemies to shoot
            targetEnemyToShoot = MathUtils.random(1, swarm.aliveCount);
        }

        int aliveCounter = 0; // Auxiliary counter to find the selected shooter

        int enemyType;
        for (int r = 0; r < swarm.rows; r++) {
            for (int c = 0; c < swarm.cols; c++) {
                Enemy enemy = swarm.enemies[r][c];

                if (enemy.health > 0) {
                    aliveCounter++; // Counts only alive enemies

                    if(r == 0){
                        enemyType = 4;
                    } else if(r < 3){
                        enemyType = 2;
                    } else {
                        enemyType = 0;
                    }

                    if (timeSinceLastSpriteUpdate >= 30 * delta) { 
                        enemy.updateSprite(swarm.enemySprites.get(enemyType + spriteCycle));
                    }

                    Rectangle hitbox = enemy.getHitbox();

                    // Checks if the swarm hit the wall
                    if (swarm.movingRight && (hitbox.x + hitbox.width + moveX > screenWidth)) {
                        hitEdge = true;
                    } else if (!swarm.movingRight && (hitbox.x + moveX < 0)) {
                        hitEdge = true;
                    }

                    // Checks if hit by a Player's bullet
                    for (Bullet bullet : activeBullets) {
                        if (bullet.origin instanceof Player) {
                            Player player = (Player) bullet.origin;
                            if (enemy.checkCollision(bullet)) {
                                enemy.health -= bullet.damage;
                                bullet.isValid = false;

                                if (enemy.health <= 0) {
                                    swarm.aliveCount--; 

                                    // Plays the explosion sound loaded in the AssetManager
                                    if (game.assets.isLoaded("audio/sfx/explosion.wav", Sound.class)) {
                                        game.assets.get("audio/sfx/explosion.wav", Sound.class).play(0.4f);
                                    }
                                }

                                player.points += 10 + enemyType*10;
                            }
                        }
                    }

                    // If this is the selected enemy, it creates a bullet
                    if (willShootThisFrame && aliveCounter == targetEnemyToShoot) {
                        enemyBulletsToSpawn.add(new Bullet(
                            enemy.getX() + (enemy.getWidth() / 2), 
                            enemy.getY(), 
                            5, 10, 
                            enemy.bulletSprite, 
                            enemy.bulletSpeed, 
                            1, 
                            enemy
                        ));

                        if (game.assets.isLoaded("audio/sfx/shoot.wav", Sound.class)) {
                            game.assets.get("audio/sfx/shoot.wav", Sound.class).play(0.1f);
                        }
                    }
                }
            }
        }

        activeBullets.addAll(enemyBulletsToSpawn);

        if (hitEdge) {
            swarm.movingRight = !swarm.movingRight;
            swarm.move(0, -swarm.dropDistance);
        } else {
            swarm.move(moveX * delta * 30, 0);
        }

        if (timeSinceLastSpriteUpdate >= 30 * delta) {
            spriteCycle = (spriteCycle + 1) % 2;
            timeSinceLastSpriteUpdate = 0f;
        }
    }
}