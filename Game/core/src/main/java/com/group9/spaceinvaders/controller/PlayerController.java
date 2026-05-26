package com.group9.spaceinvaders.controller;

import com.badlogic.gdx.Gdx;
import com.group9.spaceinvaders.model.Player;
import com.group9.spaceinvaders.model.EnemyBullet;
import java.util.List;

public class PlayerController {
    private Player player;

    public PlayerController(Player player) {
        this.player = player;
    }

    // O parâmetro 'delta' é o tempo (em frações de segundo) desde o último frame.
    // Isso garante que a nave mova na mesma velocidade num PC da NASA ou no seu i5.
    public void update(float delta, List<EnemyBullet> bullets) {
        if (Gdx.input.isKeyPressed(player.leftKey)) {
            player.moveLeft(delta);
        }
        if (Gdx.input.isKeyPressed(player.rightKey)) {
            player.moveRight(delta);
        }
        for(EnemyBullet bullet : bullets){
            if(player.checkCollision(bullet)){
                player.isAlive = false;
                bullet.isValid = false;
            }
        }
    }
}