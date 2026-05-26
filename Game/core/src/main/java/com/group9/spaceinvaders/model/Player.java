package com.group9.spaceinvaders.model;

import com.badlogic.gdx.math.Rectangle;

public class Player {
    // A Bounding Box matemática da nossa nave (guarda X, Y, Largura e Altura)
    public Rectangle bounds;
    
    // Velocidade em pixels por segundo
    public float speed = 300f; 
    public boolean canShoot = true;
    public boolean isAlive = true;

    public Player(float startX, float startY, int width, int height) {
        // Inicializa a nave com 50x50 pixels
        this.bounds = new Rectangle(startX, startY, width, height); 
    }

    // O Model fornece as regras de como a entidade pode ser alterada
    public void moveLeft(float delta) {
        bounds.x -= speed * delta;
    }

    public void moveRight(float delta) {
        bounds.x += speed * delta;
    }
    public boolean checkCollision(EnemyBullet bullet){
        if(this.isAlive && bullet.isValid){
            return this.bounds.overlaps(bullet.bounds);
        }
        return false;
    }
}