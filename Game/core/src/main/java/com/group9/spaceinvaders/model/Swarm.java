package com.group9.spaceinvaders.model;

import com.badlogic.gdx.math.Rectangle;

public class Swarm {
    // A Bounding Box matemática que engloba TODOS os inimigos
    public Rectangle bounds;
    
    // Matriz para guardar nosso grid 5x11
    public Enemy[][] enemies;
    
    // Configurações do Swarm
    public int rows = 1;
    public int cols = 1;
    public float speed = 15; // Pixels por segundo
    public boolean movingRight = true; // Direção atual do movimento
    public int aliveCount = rows * cols; // Quantos inimigos ainda estão vivos
    
    // Quanto o swarm desce quando bate na parede
    public float dropDistance = 15f; 

    public Swarm(float startX, float startY, float enemyWidth, float enemyHeight, float padding, float speedModifier) {
        enemies = new Enemy[rows][cols];
        
        // Calcula a largura e altura total do retângulo gigante do Swarm
        float totalWidth = (cols * enemyWidth) + ((cols - 1) * padding);
        float totalHeight = (rows * enemyHeight) + ((rows - 1) * padding);
        speed = speedModifier*speed;

        this.bounds = new Rectangle(startX, startY, totalWidth, totalHeight);

        // Preenche o grid com os inimigos
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // Calcula a posição X e Y de cada inimigo baseado na sua coluna e linha
                float enemyX = startX + (c * (enemyWidth + padding));
                
                // No libGDX, o Y cresce para cima. Aqui estamos colocando a linha 0 no topo.
                float enemyY = startY + totalHeight - enemyHeight - (r * (enemyHeight + padding));
                
                enemies[r][c] = new Enemy(enemyX, enemyY, enemyWidth, enemyHeight);
            }
        }
    }
    // public void reset() {
    //     aliveCount = rows * cols;
    //     for (int r = 0; r < rows; r++) {
    //         for (int c = 0; c < cols; c++) {
    //             enemies[r][c].isAlive = true;
    //             enemies[r][c].bounds.x = bounds.x + (c * (enemies[r][c].bounds.width + 15));
    //             enemies[r][c].bounds.y = bounds.y + bounds.height - enemies[r][c].bounds.height - (r * (enemies[r][c].bounds.height + 15));
    //         }
    //     }
    // }

    // Move tanto a bounding box global quanto cada inimigo vivo
    public void move(float deltaX, float deltaY) {
        bounds.x += deltaX;
        bounds.y += deltaY;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (enemies[r][c].isAlive) {
                    enemies[r][c].bounds.x += deltaX;
                    enemies[r][c].bounds.y += deltaY;
                }
            }
        }
    }
}