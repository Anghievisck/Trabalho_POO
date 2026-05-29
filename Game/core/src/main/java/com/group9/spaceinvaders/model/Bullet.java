package com.group9.spaceinvaders.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Bullet extends Entity{
    private float speed; 
    public int damage;
    public boolean isValid = true;

    public Entity origin;

    public Bullet(float posX, float posY, int width, int height, TextureRegion sprite, float speed, int damage, Entity origin) {
        super(posX, posY, width, height, sprite);
    
        this.speed = speed;
        this.origin = origin;
        this.damage = damage;
    }

    public void update(float delta){
        this.setY(speed * delta);

        if(this.getY() <= 0 || this.getY() + this.getHeight() >= Gdx.graphics.getHeight() ){
            this.isValid = false;
        }

    }
}
