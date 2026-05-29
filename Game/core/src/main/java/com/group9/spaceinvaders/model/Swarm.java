package com.group9.spaceinvaders.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.List;

public class Swarm {
    // Matriz para guardar nosso grid 5x11
    public Enemy[][] enemies;
    
    // Configurações do Swarm
    public int rows = 5;
    public int cols = 11;
    public float speed = 15; // Pixels por segundo
    public boolean movingRight = true; // Direção atual do movimento
    public int aliveCount = rows * cols; // Quantos inimigos ainda estão vivos
    
    public List<TextureRegion> enemySprites; // Sprites para os inimigos
    
    // Quanto o swarm desce quando bate na parede
    public float dropDistance = 15f; 

    private enum EnemyType {
        BASIC,
        BASIC2,
        BASIC3
    };


    public Swarm(float startX, float startY, float enemyWidth, float enemyHeight, float padding, float speedModifier,  List<TextureRegion> sprites, int maxHealth, List<TextureRegion> bulletSprites, List<Float> bulletSpeed) {
        enemies = new Enemy[rows][cols];
        
        // Calcula a largura e altura total do retângulo gigante do Swarm
        float totalWidth = (cols * enemyWidth) + ((cols - 1) * padding);
        float totalHeight = (rows * enemyHeight) + ((rows - 1) * padding);
        speed = speedModifier*speed;
        enemySprites = sprites;

        EnemyType enemyType;

        // Preenche o grid com os inimigos
        for (int r = 0; r < rows; r++) {
            if(r < rows){
                enemyType = EnemyType.BASIC;
            } else if(r > 0 && r < 3){
                enemyType = EnemyType.BASIC2;
            } else {
                enemyType = EnemyType.BASIC3;
            }

            for (int c = 0; c < cols; c++) {
                // Calcula a posição X e Y de cada inimigo baseado na sua coluna e linha
                float enemyX = startX + (c * (enemyWidth + padding));
                
                // No libGDX, o Y cresce para cima. Aqui estamos colocando a linha 0 no topo.
                float enemyY = startY + totalHeight - enemyHeight - (r * (enemyHeight + padding));
                
                enemies[r][c] = new Enemy(enemyX, enemyY, enemyWidth, enemyHeight, sprites.get(enemyType.ordinal()), maxHealth, bulletSprites.get(enemyType.ordinal()), bulletSpeed.get(enemyType.ordinal()));
            }
        }
    }

    // Move tanto a bounding box global quanto cada inimigo vivo
    public void move(float deltaX, float deltaY) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (enemies[r][c].health > 0) {
                    enemies[r][c].setX(deltaX);
                    enemies[r][c].setY(deltaY);
                }
            }
        }
    }
}