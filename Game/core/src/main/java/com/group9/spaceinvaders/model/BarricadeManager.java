package com.group9.spaceinvaders.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of barricades.
 * Responsible for creating, storing, and handling collision detection
 * for all barricades in the game.
 */
public class BarricadeManager {
    private List<Barricade> barricades; // all barricades
    private float blockSize; // size of each block (shared across barricades)
    private TextureRegion intactSprite;
    private TextureRegion damagedSprite;
    private TextureRegion destroyedSprite;

    /**
     * Constructs a new BarricadeManager with a specific block size and textures.
     *
     * @param blockSize       the side length of each block (square)
     * @param intactSprite    texture for intact blocks
     * @param damagedSprite   texture for damaged blocks
     * @param destroyedSprite texture for destroyed blocks (should be transparent)
     */
    public BarricadeManager(float blockSize,
            TextureRegion intactSprite,
            TextureRegion damagedSprite,
            TextureRegion destroyedSprite) {
        this.barricades = new ArrayList<>();
        this.blockSize = blockSize;
        this.intactSprite = intactSprite;
        this.damagedSprite = damagedSprite;
        this.destroyedSprite = destroyedSprite;
    }

    /**
     * Creates a specified number of barricades evenly spaced across the screen.
     *
     * @param count       the number of barricades to create
     * @param screenWidth the total width of the game screen (used for spacing)
     * @param baseY       the Y coordinate where the barricades will be placed
     */
    public void createBarricades(int count, float screenWidth, float baseY) {
        float spacing = screenWidth / (count + 1);

        for (int i = 1; i <= count; i++) {
            // Offset X so the barricade is centered around the calculated position.
            // The barricade is 8 blocks wide, so half width = 4 * blockSize.
            float xPos = spacing * i - (blockSize * 4);
            Barricade barricade = new Barricade(xPos, baseY, blockSize,
                    intactSprite, damagedSprite, destroyedSprite);
            barricades.add(barricade);
        }
    }

    /**
     * Draws all non‑destroyed blocks of every barricade.
     *
     * @param batch the SpriteBatch to draw with
     */
    public void draw(SpriteBatch batch) {
        for (Barricade barricade : barricades) {
            barricade.draw(batch);
        }
    }

    /**
     * Checks for collision between a bullet and any barricade managed by this
     * manager.
     *
     * @param bulletHitbox the hitbox (bounding rectangle) of the bullet
     * @return {@code true} if a collision occurred with any barricade,
     *         {@code false} otherwise
     */
    public boolean checkBulletCollision(Rectangle bulletHitbox) {
        for (Barricade barricade : barricades) {
            if (barricade.checkCollision(bulletHitbox)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the list of all barricades.
     *
     * @return a List of Barricade objects
     */
    public List<Barricade> getBarricades() {
        return barricades;
    }

    /**
     * Removes a barricade from the manager (e.g. when it is fully destroyed).
     *
     * @param barricade the barricade to remove
     * @return {@code true} if the barricade was found and removed
     */
    public boolean removeBarricade(Barricade barricade) {
        return barricades.remove(barricade);
    }

    /**
     * Moves all barricades by the given offset (useful for scrolling or dynamic
     * positioning).
     *
     * @param deltaX horizontal shift
     * @param deltaY vertical shift
     */
    public void moveAll(float deltaX, float deltaY) {
        for (Barricade barricade : barricades) {
            barricade.move(deltaX, deltaY);
        }
    }
}
