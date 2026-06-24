package com.group9.spaceinvaders.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.group9.spaceinvaders.model.Player;
import com.group9.spaceinvaders.model.Bullet;
import com.group9.spaceinvaders.model.Enemy;
import com.group9.spaceinvaders.view.SpaceInvadersGame;

import java.util.List;
/**
 * Controller responsible for managing player inputs, movement, shooting mechanics,
 * and handling collisions with enemy bullets.
 */
public class PlayerController {
    private Player player;
    private SpaceInvadersGame game;
    /**
     * Constructs a PlayerController for a specific player instance.
     *
     * @param game   the main game instance used to access assets
     * @param player the player model to be controlled
     */
    public PlayerController(SpaceInvadersGame game, Player player) {
        this.game = game;
        this.player = player;
    }
    /**
     * Updates the player's state based on user inputs and checks for collisions.
     *
     * @param delta   the time elapsed since the last frame
     * @param bullets the list of active bullets currently in the game
     */
    public void update(float delta, List<Bullet> bullets) {
        player.updateInvulnerability(delta);
        if(Gdx.input.isKeyPressed(player.leftKey)) {
            player.moveLeft(delta);
        }

        if(Gdx.input.isKeyPressed(player.rightKey)) {
            player.moveRight(delta);
        }

        if(Gdx.input.isKeyJustPressed(player.shootKey) && player.ammo > 0 && player.lives > 0) {
            Bullet novaBala = new Bullet(
                player.getX() + (player.getWidth() / 2), 
                player.getY() + player.getHeight(), 
                5, 15, player.bulletSprite, 400f, 100, player
            );

            bullets.add(novaBala);
            player.ammo--;
            
            // Plays the shooting sound loaded in the AssetManager
            if (game.assets.isLoaded("audio/sfx/shoot.wav", Sound.class)) {
                game.assets.get("audio/sfx/shoot.wav", Sound.class).play(0.1f);
            }
        }

        for(Bullet bullet : bullets){
            if(bullet.origin instanceof Enemy){
                if(player.checkCollision(bullet)){
                    bullet.isValid = false;
                    player.lives--;
                    
                    player.triggerInvulnerability();

                    // Plays the explosion sound loaded in the AssetManager
                    if (game.assets.isLoaded("audio/sfx/explosion.wav", Sound.class)) {
                        game.assets.get("audio/sfx/explosion.wav", Sound.class).play(0.4f);
                    }
                }
            }
        }
    }
}