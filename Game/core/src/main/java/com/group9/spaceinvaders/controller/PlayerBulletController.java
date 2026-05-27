package com.group9.spaceinvaders.controller;

import com.badlogic.gdx.Gdx;

import com.group9.spaceinvaders.model.Player;
import com.group9.spaceinvaders.model.PlayerBullet;

public class PlayerBulletController {
    private PlayerBullet bullet;

    public PlayerBulletController(PlayerBullet bullet) {
        this.bullet = bullet;
    }

    // O parâmetro 'delta' é o tempo (em frações de segundo) desde o último frame.
    // Isso garante que a nave mova na mesma velocidade num PC da NASA ou no seu i5.
    public void update(float delta) {
        if(bullet.isValid){
            bullet.Move(delta);
        }
    }
    public void PlayerShootUpdate(Player player){
        if(player.canShoot && Gdx.input.isKeyJustPressed(player.shootKey)){
            bullet.isValid = true;
            bullet.hitbox.x = player.hitbox.x + (player.hitbox.width / 2);
            bullet.hitbox.y = player.hitbox.y + player.hitbox.height;
            bullet.player = player;
        } 
    }
}

