package com.group9.spaceinvaders.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single barricade composed of a grid of blocks.
 * The grid follows a classic Space Invaders pattern (6 rows × 8 columns).
 */
public class Barricade {
    private List<List<BarricadeBlock>> grid; // 2D grid of blocks
    private float x, y; // top‑left position of the barricade
    private float blockSize; // size of each block

    /**
     * Constructs a new barricade at the given position.
     *
     * @param x               the X coordinate of the top‑left corner of the
     *                        barricade
     * @param y               the Y coordinate of the top‑left corner of the
     *                        barricade
     * @param blockSize       the side length of each block (square)
     * @param intactSprite    texture for intact blocks
     * @param damagedSprite   texture for damaged blocks
     * @param destroyedSprite texture for destroyed blocks (should be transparent)
     */
    public Barricade(float x, float y, float blockSize,
            TextureRegion intactSprite,
            TextureRegion damagedSprite,
            TextureRegion destroyedSprite) {
        this.x = x;
        this.y = y;
        this.blockSize = blockSize;
        this.grid = new ArrayList<>();
        initializeBarricade(intactSprite, damagedSprite, destroyedSprite);
    }

    /**
     * Initialises the grid with the classic Space Invaders pattern.
     * 0 = empty (no block), 1 = solid block.
     *
     * @param intactSprite    texture for intact blocks
     * @param damagedSprite   texture for damaged blocks
     * @param destroyedSprite texture for destroyed blocks
     */
    private void initializeBarricade(TextureRegion intactSprite,
            TextureRegion damagedSprite,
            TextureRegion destroyedSprite) {
        int[][] pattern = {
                { 1, 1, 0, 0, 0, 0, 1, 1 },
                { 1, 1, 0, 0, 0, 0, 1, 1 },
                { 1, 1, 1, 0, 0, 1, 1, 1 },
                { 1, 1, 1, 1, 1, 1, 1, 1 },
                { 0, 1, 1, 1, 1, 1, 1, 0 },
                { 0, 0, 1, 1, 1, 1, 0, 0 }
        };

        for (int row = 0; row < pattern.length; row++) {
            List<BarricadeBlock> rowBlocks = new ArrayList<>();
            for (int col = 0; col < pattern[row].length; col++) {
                float posX = x + col * blockSize;
                float posY = y + row * blockSize;

                BarricadeBlock block = new BarricadeBlock(posX, posY, blockSize,
                        intactSprite, damagedSprite, destroyedSprite);
                rowBlocks.add(block);

                // If the pattern says it should be empty, we destroy it immediately
                // so it won't be drawn and won't block collisions.
                if (pattern[row][col] == 0) {
                    block.destroy();
                }
            }
            grid.add(rowBlocks);
        }
    }

    /**
     * Draws all non‑destroyed blocks of this barricade.
     *
     * @param batch the SpriteBatch to draw with
     */
    public void draw(SpriteBatch batch) {
        for (List<BarricadeBlock> row : grid) {
            for (BarricadeBlock block : row) {
                block.draw(batch);
            }
        }
    }

    /**
     * Checks for collision between a bullet and any non‑destroyed block of this
     * barricade.
     * If a collision occurs, the block is damaged and the method returns
     * {@code true}.
     *
     * @param bulletHitbox the hitbox (bounding rectangle) of the bullet
     * @return {@code true} if a collision occurred and a block was damaged,
     *         {@code false} otherwise
     */
    public boolean checkCollision(Rectangle bulletHitbox) {
        for (List<BarricadeBlock> row : grid) {
            for (BarricadeBlock block : row) {
                if (!block.isDestroyed() && bulletHitbox.overlaps(block.getHitbox())) {
                    block.damage();
                    return true; // only one block per bullet
                }
            }
        }
        return false;
    }

    /**
     * Returns the entire grid of blocks (including destroyed ones).
     *
     * @return a 2D list of all BarricadeBlock objects
     */
    public List<List<BarricadeBlock>> getGrid() {
        return grid;
    }

    /**
     * Checks whether the entire barricade is completely destroyed.
     *
     * @return {@code true} if every block is destroyed, {@code false} otherwise
     */
    public boolean isFullyDestroyed() {
        for (List<BarricadeBlock> row : grid) {
            for (BarricadeBlock block : row) {
                if (!block.isDestroyed()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Updates the position of the whole barricade (moves every block).
     *
     * @param deltaX the horizontal shift
     * @param deltaY the vertical shift
     */
    public void move(float deltaX, float deltaY) {
        this.x += deltaX;
        this.y += deltaY;
        for (List<BarricadeBlock> row : grid) {
            for (BarricadeBlock block : row) {
                Rectangle hb = block.getHitbox();
                hb.setPosition(hb.x + deltaX, hb.y + deltaY);
            }
        }
    }
}