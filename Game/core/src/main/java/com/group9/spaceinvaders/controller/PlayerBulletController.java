package com.group9.spaceinvaders.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.group9.spaceinvaders.model.Player;
import com.group9.spaceinvaders.model.PlayerBullet;

public class PlayerBulletController {
    private PlayerBullet bullet;

    public PlayerBulletController(PlayerBullet bullet) {
        this.bullet = bullet;
    }

    // O parâmetro 'delta' é o tempo (em frações de segundo) desde o último frame.
    // Isso garante que a nave mova na mesma velocidade num PC da NASA ou no seu i5.
    public void update(Player player, float delta) {
        if(bullet.isValid){
            bullet.Move(delta);
        }

        if(player.canShoot && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            bullet.isValid = true;
            bullet.bounds.x = player.bounds.x + (player.bounds.width / 2);
            bullet.bounds.y = player.bounds.y + player.bounds.height;
        } 
    }
}

