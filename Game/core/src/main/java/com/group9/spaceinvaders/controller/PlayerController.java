package com.group9.spaceinvaders.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.group9.spaceinvaders.model.Player;
import com.group9.spaceinvaders.model.Bullet;
import com.group9.spaceinvaders.model.Enemy;
import com.group9.spaceinvaders.view.SpaceInvadersGame;

import java.util.List;

public class PlayerController {
    private Player player;
    private SpaceInvadersGame game;

    public PlayerController(SpaceInvadersGame game, Player player) {
        this.game = game;
        this.player = player;
    }

    public void update(float delta, List<Bullet> bullets) {
        if(Gdx.input.isKeyPressed(player.leftKey)) {
            player.moveLeft(delta);
        }

        if(Gdx.input.isKeyPressed(player.rightKey)) {
            player.moveRight(delta);
        }

        if(Gdx.input.isKeyJustPressed(player.shootKey) && player.ammo > 0) {
            Bullet novaBala = new Bullet(
                player.getX() + (player.getWidth() / 2), 
                player.getY() + player.getHeight(), 
                5, 15, player.bulletSprite, 400f, 100, player
            );

            bullets.add(novaBala);
            player.ammo--;

            // Reproduz o som de tiro carregado no AssetManager
            if (game.assets.isLoaded("audio/sfx/shoot.wav", Sound.class)) {
                game.assets.get("audio/sfx/shoot.wav", Sound.class).play(0.4f);
            }
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