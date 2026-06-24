package com.group9.spaceinvaders.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Represents the player's ship. Handles movement, lives, points, ammo, 
 * shooting keys, and invulnerability states.
 */
public class Player extends Entity {
    // Speed in pixels per second
    private float speed = 300f; 

    public int points = 0;
    public int lives = 3;

    public int leftKey;
    public int rightKey;
    public int shootKey;

    // Timer for post-hit invulnerability
    private float invulnerableTimer = 0f;

    public int ammo = 1;
    public TextureRegion bulletSprite;
    
    /**
     * Constructs a Player entity.
     *
     * @param startX       the initial X position
     * @param startY       the initial Y position
     * @param width        the width of the player ship
     * @param height       the height of the player ship
     * @param sprite       the main texture of the ship
     * @param bulletSprite the texture for the bullets fired by this player
     * @param leftKey      the Input.Key code for moving left
     * @param rightKey     the Input.Key code for moving right
     * @param shootKey     the Input.Key code for shooting
     */
    public Player(float startX, float startY, int width, int height, TextureRegion sprite, TextureRegion bulletSprite, int leftKey, int rightKey, int shootKey) {
        super(startX, startY, width, height, sprite);
        this.bulletSprite = bulletSprite;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.shootKey = shootKey; 
    }

    /**
     * Updates the player's position based on movement requests.
     * Prevents the player from moving off the screen.
     *
     * @param delta the time/distance to move (can be negative for left movement)
     */
    public void update(float delta) {
        float deltaX = speed * delta;
        if (this.getX() + deltaX >= 0 && this.getX() + deltaX + this.getWidth() <= 800) {
            this.setX(deltaX);
        }
    }

    public void moveLeft(float delta) {
        this.update(-delta);
    }

    public void moveRight(float delta) {
        this.update(delta);
    }

    /**
     * Checks if the player's hitbox overlaps with a bullet's hitbox.
     * Ignores collisions if the player is currently invulnerable or dead.
     *
     * @param bullet the bullet to check against
     * @return true if a valid collision occurred, false otherwise
     */
    public boolean checkCollision(Bullet bullet) {
        if (this.lives > 0 && bullet.isValid && this.invulnerableTimer <= 0) {
            return this.getHitbox().overlaps(bullet.getHitbox());
        }
        return false;
    }

    /**
     * Activates a temporary invulnerability window for the player.
     */
    public void triggerInvulnerability() {
        this.invulnerableTimer = 2.0f; 
    }

    /**
     * Reduces the invulnerability timer every frame.
     *
     * @param delta the time elapsed since the last frame
     */
    public void updateInvulnerability(float delta) {
        if (this.invulnerableTimer > 0) {
            this.invulnerableTimer -= delta;
        }
    }

    /**
     * Draws the player ship. Applies a blinking effect if invulnerable.
     *
     * @param batch the SpriteBatch used for rendering
     */
    @Override
    public void draw(SpriteBatch batch) {
        if (sprite != null) {
            if (invulnerableTimer > 0) {
                // Multiplying by 15 defines the blinking speed.
                if ((int)(invulnerableTimer * 15) % 2 == 0) {
                    batch.draw(sprite, getX(), getY(), getWidth(), getHeight());
                }
            } else {
                // Normal behavior: draws the static ship
                batch.draw(sprite, getX(), getY(), getWidth(), getHeight());
            }
        }
    }
}