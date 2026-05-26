package com.group9.spaceinvaders.model;

import com.badlogic.gdx.math.Rectangle;

public class Enemy {
    public Rectangle bounds;
    public boolean isAlive = true;

    public Enemy(float startX, float startY, float width, float height) {
        this.bounds = new Rectangle(startX, startY, width, height);
    }
    public boolean checkCollision(PlayerBullet bullet){
        if(this.isAlive && bullet.isValid){
            return this.bounds.overlaps(bullet.bounds);
        }
        return false;
    }
}