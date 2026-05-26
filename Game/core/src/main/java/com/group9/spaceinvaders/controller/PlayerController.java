package com.group9.spaceinvaders.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.group9.spaceinvaders.model.Player;
import com.group9.spaceinvaders.model.EnemyBullet;

public class PlayerController {
    private Player player;

    public PlayerController(Player player) {
        this.player = player;
    }

    // O parâmetro 'delta' é o tempo (em frações de segundo) desde o último frame.
    // Isso garante que a nave mova na mesma velocidade num PC da NASA ou no seu i5.
    public void update(float delta, EnemyBullet bullet) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.moveLeft(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.moveRight(delta);
        }
        if(player.checkCollision(bullet)){
            player.isAlive = false;
            bullet.isValid = false;
        } 
    }
}