package com.group9.spaceinvaders.controller;

import com.badlogic.gdx.math.Rectangle;
import com.group9.spaceinvaders.model.Enemy;
import com.group9.spaceinvaders.model.Player;
import com.group9.spaceinvaders.model.Bullet;
import com.group9.spaceinvaders.model.Swarm;
import com.badlogic.gdx.math.MathUtils;
import java.util.List;

public class SwarmController {
    private Swarm swarm;
    private float screenWidth;

    public int ammo;

    public SwarmController(Swarm swarm, float screenWidth, int ammo) {
        this.swarm = swarm;
        this.screenWidth = screenWidth;
        this.ammo = ammo;
    }

    public void update(float delta, List<Bullet> activeBullets) {
        float moveX = swarm.speed * delta;
        if (!swarm.movingRight) {
            moveX = -moveX; 
        }

        boolean hitEdge = false;

        Rectangle hitbox;
        Player player;

        int cont = 0;
        int rand = MathUtils.random(0, swarm.aliveCount - 1);
        for (int r = 0; r < swarm.rows; r++) {
            for (int c = 0; c < swarm.cols; c++) {
                Enemy enemy = swarm.enemies[r][c];

                if(enemy.health > 0){
                    cont++;
                }

                hitbox = enemy.getHitbox();

                if (enemy.health > 0) {
                    if (swarm.movingRight && (hitbox.x + hitbox.width + moveX > screenWidth)) {
                        hitEdge = true;
                        break; // Já achou um que bateu, pode parar de procurar
                    } else if (!swarm.movingRight && (hitbox.x + moveX < 0)) {
                        hitEdge = true;
                        break; 
                    }

                    for (Bullet bullet : activeBullets) {
                        if(bullet.origin instanceof Player){
                            player = (Player)bullet.origin;
                            if (enemy.checkCollision(bullet)) {
                                enemy.health--;
                                bullet.isValid = false;

                                if(enemy.health == 0){
                                    swarm.aliveCount--; // Decrementa o contador de inimigos vivos
                                }

                                player.points += 10;
                                player.ammo++;
                            }
                        }
                    }

                    if(this.ammo > 0){
                        if(cont == rand){
                            activeBullets.add(new Bullet(enemy.getX() + (enemy.getWidth() / 2), enemy.getY(), 5, 10, enemy.bulletSprite, enemy.bulletSpeed, enemy));
                            this.ammo--;
                        }
                    }
                }
            }

            if (hitEdge) break; // Sai do loop externo também
        }

        // 2. Aplica o movimento baseado no resultado
        if (hitEdge) {
            // Inverte a direção e desce a nuvem inteira
            swarm.movingRight = !swarm.movingRight;
            swarm.move(0, -swarm.dropDistance);
        } else {
            // Caminho livre, continua andando de lado
            swarm.move(moveX, 0);
        }
    }

}