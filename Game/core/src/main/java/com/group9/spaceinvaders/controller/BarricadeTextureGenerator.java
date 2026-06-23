package com.group9.spaceinvaders.controller;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Utility class to generate simple coloured textures for barricade blocks.
 * Useful when no specific sprites are available.
 */
public class BarricadeTextureGenerator {

    /**
     * Creates a solid colour texture of the given size and RGBA colour.
     *
     * @param width  texture width in pixels
     * @param height texture height in pixels
     * @param color  packed RGBA colour (e.g. 0x888888FF for opaque light grey)
     * @return a TextureRegion of the generated texture
     */
    public static TextureRegion createSolidColourTexture(int width, int height, int color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegion(texture);
    }

    /**
     * Creates an intact block texture (light grey).
     *
     * @param size the side length of the square
     * @return a TextureRegion with light grey colour
     */
    public static TextureRegion createIntact(int size) {
        return createSolidColourTexture(size, size, 0x888888FF); // light grey
    }

    /**
     * Creates a damaged block texture (dark grey).
     *
     * @param size the side length of the square
     * @return a TextureRegion with dark grey colour
     */
    public static TextureRegion createDamaged(int size) {
        return createSolidColourTexture(size, size, 0x555555FF); // dark grey
    }

    /**
     * Creates a destroyed block texture (fully transparent).
     *
     * @param size the side length of the square
     * @return a TextureRegion with transparent pixels
     */
    public static TextureRegion createDestroyed(int size) {
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(0x00000000); // fully transparent
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegion(texture);
    }
}