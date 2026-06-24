package com.group9.spaceinvaders.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AmmoDrop extends Entity {
    // Variável para controlar quanto tempo a munição fica no chão
    private float timeAlive = 0f;
    public boolean isCollected = false;

    public AmmoDrop(float x, float y, float width, float height, TextureRegion sprite) {
        super(x, y, width, height, sprite);
    }

    @Override
    public void update(float delta) {
        timeAlive += delta;
        
        // Se a munição ficar 8 segundos no chão sem ninguém pegar, ela some
        if (timeAlive > 8f) {
            this.isCollected = true;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (sprite != null) {
            // Desenha a sprite rotacionada em 90 graus
            batch.draw(sprite, getX(), getY(), getWidth() / 2, getHeight() / 2, 
                       getWidth(), getHeight(), 1f, 1f, 0f);
        }
    }
}