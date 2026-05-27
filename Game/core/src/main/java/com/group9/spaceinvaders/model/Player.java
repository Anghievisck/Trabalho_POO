package com.group9.spaceinvaders.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Entity {
    // Velocidade em pixels por segundo
    private float speed = 300f; 

    public int points = 0;
    public int lives = 3;

    public boolean canShoot = true;
    public boolean isAlive = true;
    
    public int leftKey;
    public int rightKey;
    public int shootKey;

    
    public Player(float startX, float startY, int width, int height, TextureRegion sprite, int leftRight, int rightRight, int shootKey) {
        // Inicializa a nave com 50x50 pixels
        super(startX, shootKey, width, height, sprite);

        this.leftKey = leftRight;
        this.rightKey = rightRight;
        this.shootKey = shootKey; 
    }

    // O Model fornece as regras de como a entidade pode ser alterada
    public void moveLeft(float delta) {
        this.x -= speed * delta;
        this.updateHitbox();
    }

    public void moveRight(float delta) {
        this.x += speed * delta;
        this.updateHitbox();
    }
    public boolean checkCollision(EnemyBullet bullet){
        if(this.isAlive && bullet.isValid){
            return this.hitbox.overlaps(bullet.hitbox);
        }
        return false;
    }
}