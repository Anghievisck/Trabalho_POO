package com.group9.spaceinvaders.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy extends Entity{
    public int health;

    public TextureRegion bulletSprite;
    public float bulletSpeed;

    public Enemy(float startX, float startY, float width, float height, TextureRegion sprite, int maxHealth, TextureRegion bulletSprite, float bulletSpeed) {
        super(startX, startY, width, height, sprite);

        this.health = maxHealth;
        this.bulletSprite = bulletSprite;
        this.bulletSpeed = bulletSpeed;
    }

    public boolean checkCollision(Bullet bullet){
        if(this.health > 0 && bullet.isValid){
            return this.getHitbox().overlaps(bullet.getHitbox());
        }

        return false;
    }

    public void update(float delta){};
}