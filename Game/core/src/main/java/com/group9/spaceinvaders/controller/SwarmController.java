package com.group9.spaceinvaders.controller;

import com.group9.spaceinvaders.model.Enemy;
import com.group9.spaceinvaders.model.PlayerBullet;
import com.group9.spaceinvaders.model.Swarm;
import java.util.List;



public class SwarmController {
    private Swarm swarm;
    private float screenWidth;

    public SwarmController(Swarm swarm, float screenWidth) {
        this.swarm = swarm;
        this.screenWidth = screenWidth;
    }

    public void update(float delta, List<PlayerBullet> playerBullets) {
        float moveX = swarm.speed * delta;
        if (!swarm.movingRight) {
            moveX = -moveX; 
        }

        boolean hitEdge = false;

        // 1. Varre o grid para ver se algum alien VIVO bate na parede
        for (int r = 0; r < swarm.rows; r++) {
            for (int c = 0; c < swarm.cols; c++) {
                Enemy enemy = swarm.enemies[r][c];
                if (enemy.isAlive) {
                    // Se indo para a direita, checa a borda direita do inimigo
                    if (swarm.movingRight && (enemy.bounds.x + enemy.bounds.width + moveX > screenWidth)) {
                        hitEdge = true;
                        break; // Já achou um que bateu, pode parar de procurar
                    } 
                    // Se indo para a esquerda, checa a borda esquerda do inimigo
                    else if (!swarm.movingRight && (enemy.bounds.x + moveX < 0)) {
                        hitEdge = true;
                        break; 
                    }
                    for (PlayerBullet bullet : playerBullets) {
                        if (enemy.checkCollision(bullet)) {
                            enemy.isAlive = false;
                            bullet.isValid = false;
                            swarm.aliveCount--; // Decrementa o contador de inimigos vivos
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