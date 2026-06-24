package com.group9.spaceinvaders.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Abstract base class for all game entities (Player, Enemy, Bullet, etc.).
 * Handles basic properties like position, dimensions, sprite rendering, and hitboxes.
 */
public abstract class Entity {
    // We use 'protected' so child classes (Player, Enemy) 
    // can read and modify these values directly if needed.
    private float x;
    private float y;
    private float width;
    private float height;
    private Rectangle hitbox;

    protected TextureRegion sprite;

    /**
     * The base constructor for an Entity.
     *
     * @param x      the initial X coordinate
     * @param y      the initial Y coordinate
     * @param width  the width of the entity
     * @param height the height of the entity
     * @param sprite the visual texture of the entity
     */
    public Entity(float x, float y, float width, float height, TextureRegion sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
        
        // The hitbox is automatically initialized with the passed dimensions
        this.hitbox = new Rectangle(x, y, width, height);
    }

    /**
     * Every time the entity moves, child classes must call this method 
     * to ensure the hitbox follows the sprite's position.
     */
    protected void updateHitbox() {
        hitbox.setPosition(x, y);
    }

    /**
     * Draws the entity on the screen.
     *
     * @param batch the SpriteBatch used for rendering
     */
    public void draw(SpriteBatch batch) {
        if (sprite != null) {
            batch.draw(sprite, x, y, width, height);
        }
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getHeight() { return height; }
    public float getWidth() { return width; }
    public Rectangle getHitbox() { return hitbox; }

    public void setX(float deltaX) {
        this.x += deltaX;
        updateHitbox();
    }

    public void setY(float deltaY) {
        this.y += deltaY;
        updateHitbox();
    }

    /**
     * Abstract method: Forces all child classes to implement 
     * their own specific update logic.
     *
     * @param delta the time elapsed since the last frame
     */
    public abstract void update(float delta);
}