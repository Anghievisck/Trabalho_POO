package com.group9.spaceinvaders.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy extends Entity{
    public boolean isAlive = true;

    public Enemy(float startX, float startY, float width, float height, TextureRegion sprite) {
        super(startX, startY, width, height, sprite);
    }

    public boolean checkCollision(PlayerBullet bullet){
        if(this.isAlive && bullet.isValid){
            return this.hitbox.overlaps(bullet.hitbox);
        }
        return false;
    }
}