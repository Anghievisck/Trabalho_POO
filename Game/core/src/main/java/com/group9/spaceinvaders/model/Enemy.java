package com.group9.spaceinvaders.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Represents a single alien enemy in the game.
 */
public class Enemy extends Entity {
    public int health;

    public TextureRegion bulletSprite;
    public float bulletSpeed;

    /**
     * Constructs an Enemy entity.
     *
     * @param startX      the initial X position
     * @param startY      the initial Y position
     * @param width       the width of the enemy
     * @param height      the height of the enemy
     * @param sprite      the texture of the enemy
     * @param maxHealth   the starting health of the enemy
     * @param bulletSprite the texture for the bullets fired by this enemy
     * @param bulletSpeed the speed at which this enemy's bullets travel
     */
    public Enemy(float startX, float startY, float width, float height, TextureRegion sprite, int maxHealth, TextureRegion bulletSprite, float bulletSpeed) {
        super(startX, startY, width, height, sprite);

        this.health = maxHealth;
        this.bulletSprite = bulletSprite;
        this.bulletSpeed = bulletSpeed;
    }

    /**
     * Checks if the enemy collides with a given bullet.
     *
     * @param bullet the bullet to check against
     * @return true if a collision occurs and the enemy is alive, false otherwise
     */
    public boolean checkCollision(Bullet bullet) {
        if (this.health > 0 && bullet.isValid) {
            return this.getHitbox().overlaps(bullet.getHitbox());
        }
        return false;
    }

    @Override
    public void update(float delta) {
        // Enemy specific updates are largely managed by SwarmController
    }

    /**
     * Updates the visual sprite of the enemy (used for animation).
     *
     * @param newSprite the new frame to render
     */
    public void updateSprite(TextureRegion newSprite) {
        if (this.health > 0) {
            this.sprite = newSprite;
        }
    }
}