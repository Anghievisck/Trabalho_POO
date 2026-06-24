package com.group9.spaceinvaders.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Represents a single block of a barricade.
 * A block can be in three states: INTACT, DAMAGED, or DESTROYED.
 * Each state uses a different texture for visual feedback.
 */
public class BarricadeBlock {

    /**
     * Enumeration of possible states of a barricade block.
     */
    public enum State {
        INTACT,
        DAMAGED,
        DESTROYED
    }

    private State state;
    private Rectangle hitbox; // collision bounds
    private TextureRegion currentSprite; // texture for the current state
    private TextureRegion intactSprite;
    private TextureRegion damagedSprite;
    private TextureRegion destroyedSprite; // usually transparent

    /**
     * Constructs a new barricade block at the specified position with the given size.
     *
     * @param x               the X coordinate of the top-left corner (in world units)
     * @param y               the Y coordinate of the top-left corner
     * @param size            the side length of the square block
     * @param intactSprite    texture for the intact state
     * @param damagedSprite   texture for the damaged state
     * @param destroyedSprite texture for the destroyed state (should be transparent/empty)
     */
    public BarricadeBlock(float x, float y, float size,
            TextureRegion intactSprite,
            TextureRegion damagedSprite,
            TextureRegion destroyedSprite) {
        this.hitbox = new Rectangle(x, y, size, size);
        this.state = State.INTACT;
        this.intactSprite = intactSprite;
        this.damagedSprite = damagedSprite;
        this.destroyedSprite = destroyedSprite;
        this.currentSprite = intactSprite;
    }

    /**
     * Damages the block, progressing from INTACT -> DAMAGED -> DESTROYED.
     * If already DESTROYED, nothing happens.
     */
    public void damage() {
        if (state == State.INTACT) {
            state = State.DAMAGED;
            currentSprite = damagedSprite;
        } else if (state == State.DAMAGED) {
            state = State.DESTROYED;
            currentSprite = destroyedSprite;
        }
    }

    /**
     * Instantly destroys the block (makes it invisible and non-collidable).
     */
    public void destroy() {
        state = State.DESTROYED;
        currentSprite = destroyedSprite;
    }

    /**
     * Draws the block using the current sprite (if not destroyed).
     *
     * @param batch the SpriteBatch to draw with
     */
    public void draw(SpriteBatch batch) {
        if (state != State.DESTROYED) {
            batch.draw(currentSprite, hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        }
    }

    /**
     * Returns the current state of the block.
     *
     * @return the current State
     */
    public State getState() {
        return state;
    }

    /**
     * Returns the hitbox (bounding rectangle) of this block.
     *
     * @return the Rectangle used for collision detection
     */
    public Rectangle getHitbox() {
        return hitbox;
    }

    /**
     * Checks whether the block is destroyed.
     *
     * @return {@code true} if the block is in DESTROYED state, {@code false} otherwise
     */
    public boolean isDestroyed() {
        return state == State.DESTROYED;
    }

    /**
     * Updates the position of the block (useful if the barricade moves).
     *
     * @param newX the new X coordinate
     * @param newY the new Y coordinate
     */
    public void setPosition(float newX, float newY) {
        hitbox.setPosition(newX, newY);
    }
}