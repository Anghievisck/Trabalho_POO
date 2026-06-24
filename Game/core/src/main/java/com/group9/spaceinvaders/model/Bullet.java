package com.group9.spaceinvaders.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Represents a projectile fired by either the player or an enemy.
 */
public class Bullet extends Entity {
    private float speed; 
    public int damage;
    public boolean isValid = true;

    // Tracks who fired the bullet to prevent self-damage
    public Entity origin;

    /**
     * Constructs a new Bullet entity.
     *
     * @param posX   the starting X position
     * @param posY   the starting Y position
     * @param width  the width of the bullet
     * @param height the height of the bullet
     * @param sprite the visual texture of the bullet
     * @param speed  the vertical speed (positive for up, negative for down)
     * @param damage the amount of damage this bullet deals
     * @param origin the Entity that shot the bullet
     */
    public Bullet(float posX, float posY, int width, int height, TextureRegion sprite, float speed, int damage, Entity origin) {
        super(posX, posY, width, height, sprite);
    
        this.speed = speed;
        this.origin = origin;
        this.damage = damage;
    }

    /**
     * Updates the bullet's position and marks it invalid if it leaves the screen.
     *
     * @param delta the time elapsed since the last frame
     */
    @Override
    public void update(float delta) {
        this.setY(speed * delta);

        // This + 50 is a magic number that hotfixes a minor bug where the bullet was vanishing prematurely
        if (this.getY() <= 0 || this.getY() + this.getHeight() >= Gdx.graphics.getHeight() + 50) {
            this.isValid = false;
        }
    }
}