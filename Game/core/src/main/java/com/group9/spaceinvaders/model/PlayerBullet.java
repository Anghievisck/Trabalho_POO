package com.group9.spaceinvaders.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class PlayerBullet {
    public Rectangle bounds;

    private float speed = 250f; 

    public boolean isValid = false;
    
    public Player player;

    public PlayerBullet(float posX, float posY, int width, int height) {
        this.bounds = new Rectangle(posX, posY, width, height);
    }

    public void Move(float delta){
        this.bounds.y += speed * delta;

        if(this.bounds.y >= Gdx.graphics.getHeight()){
            this.isValid = false;
        }
    } 
}
