package com.group9.spaceinvaders.controller;

import com.badlogic.gdx.math.Rectangle;
import com.group9.spaceinvaders.model.Enemy;
import com.group9.spaceinvaders.model.Player;
import com.group9.spaceinvaders.model.Bullet;
import com.group9.spaceinvaders.model.Swarm;
import com.group9.spaceinvaders.model.AmmoDrop;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import java.util.List;

public class SwarmController {
    private Swarm swarm;
    private float screenWidth;
    private int spriteCycle = 0;
    private float timeSinceLastSpriteUpdate = 0f;

    // Novos controladores de tiro dos inimigos
    private float shootTimer = 0f;
    private float shootInterval; 

    public SwarmController(Swarm swarm, float screenWidth, int difficulty) {
        this.swarm = swarm;
        this.screenWidth = screenWidth;
        
        // A dificuldade dita a velocidade dos tiros do enxame
        this.shootInterval = Math.max(0.5f, 2.0f - (difficulty * 0.2f)); 
    }

    public void update(float delta, List<Bullet> activeBullets, List<AmmoDrop> activeDrops) {
        float moveX = swarm.speed * delta;
        if (!swarm.movingRight) {
            moveX = -moveX; 
        }

        boolean hitEdge = false;
        timeSinceLastSpriteUpdate += delta;
        
        // Atualiza o relógio de tiro dos inimigos
        shootTimer += delta; 

        List<Bullet> enemyBulletsToSpawn = new ArrayList<>();

        // Lógica para decidir se um inimigo atira neste exato frame
        boolean willShootThisFrame = false;
        int targetEnemyToShoot = 0;
        
        if (shootTimer >= shootInterval && swarm.aliveCount > 0) {
            willShootThisFrame = true;
            shootTimer = 0f;
            // Sorteia um dos inimigos vivos para efetuar o disparo
            targetEnemyToShoot = MathUtils.random(1, swarm.aliveCount);
        }

        int aliveCounter = 0; // Contador auxiliar para achar o atirador sorteado

        for (int r = 0; r < swarm.rows; r++) {
            for (int c = 0; c < swarm.cols; c++) {
                Enemy enemy = swarm.enemies[r][c];

                if (enemy.health > 0) {
                    aliveCounter++; // Conta apenas os vivos

                    if (timeSinceLastSpriteUpdate >= 30 * delta) { 
                        enemy.updateSprite(swarm.enemySprites.get(spriteCycle));
                    }

                    Rectangle hitbox = enemy.getHitbox();

                    // Verifica se o enxame bateu na parede
                    if (swarm.movingRight && (hitbox.x + hitbox.width + moveX > screenWidth)) {
                        hitEdge = true;
                    } else if (!swarm.movingRight && (hitbox.x + moveX < 0)) {
                        hitEdge = true;
                    }

                    // Checa se tomou tiro do Player
                    for (Bullet bullet : activeBullets) {
                        if (bullet.origin instanceof Player) {
                            Player player = (Player) bullet.origin;
                            if (enemy.checkCollision(bullet)) {
                                enemy.health -= bullet.damage;
                                bullet.isValid = false;

                                if (enemy.health <= 0) {
                                    swarm.aliveCount--; 
                                    // A munição não "dropa" mais daqui
                                }

                                player.points += 10;
                            }
                        }
                    }

                    // Se este for o inimigo sorteado, ele cria uma bala
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