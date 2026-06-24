package com.group9.spaceinvaders.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

/**
 * Represents the collective group (horde) of enemies. Handles the grid formation,
 * group movement direction, and global speed.
 */
public class Swarm {
    // Matrix to store our 5x11 grid
    public Enemy[][] enemies;
    
    // Swarm configurations
    public int rows = 5;
    public int cols = 11;
    public float speed = 15; // Pixels per second

    public boolean movingRight = true; // Current movement direction
    public int aliveCount = rows * cols; // How many enemies are still alive
    
    public List<TextureRegion> enemySprites; // Sprites for the enemies
    
    // How much the swarm drops when it hits a wall
    public float dropDistance = 15f; 

    /**
     * Constructs a Swarm of enemies.
     *
     * @param startX       the starting X coordinate for the entire block
     * @param startY       the starting Y coordinate for the bottom of the block
     * @param enemyWidth   the width of each individual enemy
     * @param enemyHeight  the height of each individual enemy
     * @param padding      the space between enemies
     * @param difficulty   multiplier affecting the swarm's speed
     * @param sprites      the list of textures for different alien types
     * @param maxHealth    the starting health for the aliens
     * @param bulletSprites textures for enemy bullets
     * @param bulletSpeed  speeds for enemy bullets depending on alien type
     */
    public Swarm(float startX, float startY, float enemyWidth, float enemyHeight, float padding, float difficulty, List<TextureRegion> sprites, int maxHealth, List<TextureRegion> bulletSprites, List<Float> bulletSpeed) {
        enemies = new Enemy[rows][cols];
        
        // Calculates the total height of the Swarm's giant bounding box
        float totalHeight = (rows * enemyHeight) + ((rows - 1) * padding);
        speed = difficulty * speed;
        enemySprites = sprites;
        
        int enemyType;
        int enemyHealthModdifier;

        // Fills the grid with enemies
        for (int r = 0; r < rows; r++) {
            // Assigns alien type based on row index
            if (r == 0) {
                enemyType = 4;
                enemyHealthModdifier = 2;
            } else if (r < 3) {
                enemyType = 2;
                enemyHealthModdifier = 1;
            } else {
                enemyType = 0;
                enemyHealthModdifier = 0;
            }

            for (int c = 0; c < cols; c++) {
                // Calculates the X and Y position of each enemy based on its column and row
                float enemyX = startX + (c * (enemyWidth + padding));
                
                // In libGDX, Y grows upwards. Here we place row 0 at the top.
                float enemyY = startY + totalHeight - enemyHeight - (r * (enemyHeight + padding));
                
                enemies[r][c] = new Enemy(enemyX, enemyY, enemyWidth, enemyHeight, sprites.get(enemyType), maxHealth + (100 * enemyHealthModdifier), bulletSprites.get(0), bulletSpeed.get(enemyType % 3));
            }
        }
    }

    /**
     * Moves both the global bounding concept and each alive enemy inside the grid.
     *
     * @param deltaX the horizontal movement
     * @param deltaY the vertical movement
     */
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