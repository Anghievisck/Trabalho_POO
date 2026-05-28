package com.group9.spaceinvaders.controller;

import com.badlogic.gdx.Gdx;
import com.group9.spaceinvaders.model.Player;
import com.group9.spaceinvaders.model.Bullet;
import com.group9.spaceinvaders.model.Enemy;

import java.util.List;

public class PlayerController {
    private Player player;

    public PlayerController(Player player) {
        this.player = player;
    }

    // O parâmetro 'delta' é o tempo (em frações de segundo) desde o último frame.
    // Isso garante que a nave mova na mesma velocidade num PC da NASA ou no seu i5.
    public void update(float delta, List<Bullet> bullets) {
        if(Gdx.input.isKeyPressed(player.leftKey)) {
            player.moveLeft(delta);
        }

        if(Gdx.input.isKeyPressed(player.rightKey)) {
            player.moveRight(delta);
        }

        if(Gdx.input.isKeyPressed(player.shootKey) && player.ammo > 0) {
            Bullet novaBala = new Bullet(
                player.getX() + (player.getWidth() / 2), 
                player.getY() + player.getHeight(), 
                5, 15, player.bulletSprite, 400f, player
            );

            bullets.add(novaBala);
            player.ammo--;
        }

        for(Bullet bullet : bullets){
            if(bullet.origin instanceof Enemy){
                if(player.checkCollision(bullet)){
                    bullet.isValid = false;
                    player.lives--;
                }
            }
        }
    }
}