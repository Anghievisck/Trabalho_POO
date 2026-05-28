package com.group9.spaceinvaders.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Entity {
    // Velocidade em pixels por segundo
    private float speed = 300f; 

    public int points = 0;
    public int lives = 3;

    public int leftKey;
    public int rightKey;
    public int shootKey;

    public int ammo = 1;
    public TextureRegion bulletSprite;
    
    public Player(float startX, float startY, int width, int height, TextureRegion sprite, TextureRegion bulletSprite, int leftRight, int rightRight, int shootKey) {
        // Inicializa a nave com 50x50 pixels
        super(startX, startY, width, height, sprite);

        this.bulletSprite = bulletSprite;

        this.leftKey = leftRight;
        this.rightKey = rightRight;
        this.shootKey = shootKey; 
    }

    public void update(float delta){
        float deltaX = speed * delta;
        if(this.getX() + deltaX >= 0 && this.getX() + deltaX + this.getWidth() <= Gdx.graphics.getWidth()){
            this.setX(deltaX);
        }
    }

    public void moveLeft(float delta) {
        this.update(-delta);
    }

    public void moveRight(float delta) {
        this.update(delta);
    }

    public boolean checkCollision(Bullet bullet){
        if(this.lives > 0 && bullet.isValid){
            return this.getHitbox().overlaps(bullet.getHitbox());
        }
        return false;
    }
}