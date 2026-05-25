package com.group9.spaceinvaders.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class PlayerBullet {
    private Rectangle bounds;

    private float speed = 150f; 

    public boolean isValid = true;

    public PlayerBullet(float posX, float posY){
        this.bounds = new Rectangle(posX, posY, 5, 10);
    }

    public void Move(float delta){
        this.bounds.y += speed * delta;

        if(this.bounds.y >= Gdx.graphics.getHeight()){
            this.isValid = false;
        }
    } 
}
