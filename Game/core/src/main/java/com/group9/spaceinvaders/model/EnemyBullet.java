package com.group9.spaceinvaders.model;
public class EnemyBullet extends PlayerBullet {
    public EnemyBullet(float startX, float startY, int width, int height) {
        super(startX, startY, width, height);
    }
    private float speed = -200f;
    public boolean isValid = false;
    @Override
    public void Move(float delta){
        this.hitbox.y += speed * delta;

        if(this.hitbox.y <= 0){
            this.isValid = false;
        }
    } 
    
}
