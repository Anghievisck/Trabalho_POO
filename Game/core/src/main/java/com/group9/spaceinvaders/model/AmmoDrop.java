package com.group9.spaceinvaders.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Represents an ammunition drop entity that players can collect.
 * It remains on the screen for a limited time before disappearing.
 */
public class AmmoDrop extends Entity {
    // Variable to track how long the ammo stays on the ground
    private float timeAlive = 0f;
    public boolean isCollected = false;

    /**
     * Constructs a new AmmoDrop.
     *
     * @param x      the X coordinate of the drop
     * @param y      the Y coordinate of the drop
     * @param width  the width of the drop's hitbox and sprite
     * @param height the height of the drop's hitbox and sprite
     * @param sprite the texture representing the ammo drop
     */
    public AmmoDrop(float x, float y, float width, float height, TextureRegion sprite) {
        super(x, y, width, height, sprite);
    }

    /**
     * Updates the ammo drop's lifetime timer.
     *
     * @param delta the time elapsed since the last frame
     */
    @Override
    public void update(float delta) {
        timeAlive += delta;
        
        // If the ammo stays on the ground for 8 seconds without being picked up, it disappears
        if (timeAlive > 8f) {
            this.isCollected = true;
        }
    }

    /**
     * Draws the ammo drop on the screen.
     *
     * @param batch the SpriteBatch used for rendering
     */
    @Override
    public void draw(SpriteBatch batch) {
        if (sprite != null) {
            // Draws the sprite rotated by 90 degrees
            batch.draw(sprite, getX(), getY(), getWidth() / 2, getHeight() / 2, 
                       getWidth(), getHeight(), 1f, 1f, 0f);
        }
    }
}