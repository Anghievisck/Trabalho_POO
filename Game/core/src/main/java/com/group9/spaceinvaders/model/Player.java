package com.group9.spaceinvaders.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends Entity {
    // Velocidade em pixels por segundo
    private float speed = 300f; 

    public int points = 0;
    public int lives = 3;

    public int leftKey;
    public int rightKey;
    public int shootKey;

    // Apenas a variável do timer é necessária
    private float invulnerableTimer = 0f;

    public int ammo = 1;
    public TextureRegion bulletSprite;
    
    public Player(float startX, float startY, int width, int height, TextureRegion sprite, TextureRegion bulletSprite, int leftRight, int rightRight, int shootKey) {
        super(startX, startY, width, height, sprite);
        this.bulletSprite = bulletSprite;
        this.leftKey = leftRight;
        this.rightKey = rightRight;
        this.shootKey = shootKey; 
    }

    public void update(float delta){
        float deltaX = speed * delta;
        if(this.getX() + deltaX >= 0 && this.getX() + deltaX + this.getWidth() <= 800){
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
        if(this.lives > 0 && bullet.isValid && this.invulnerableTimer <= 0){
            return this.getHitbox().overlaps(bullet.getHitbox());
        }
        return false;
    }

    public void triggerInvulnerability() {
        this.invulnerableTimer = 2.0f; 
    }

    // Reduz o timer a cada frame
    public void updateInvulnerability(float delta) {
        if (this.invulnerableTimer > 0) {
            this.invulnerableTimer -= delta;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (sprite != null) {
            if (invulnerableTimer > 0) {
                // Multiplicar por 15 define a velocidade do piscar.
                if ((int)(invulnerableTimer * 15) % 2 == 0) {
                    batch.draw(sprite, getX(), getY(), getWidth(), getHeight());
                }
            } else {
                // Comportamento normal: desenha a nave fixa
                batch.draw(sprite, getX(), getY(), getWidth(), getHeight());
            }
        }
    }
}