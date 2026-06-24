package com.group9.spaceinvaders.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.audio.Sound;

/**
 * Main application class and entry point that inherits from libGDX's Game.
 * Handles top-level asset loading and screen delegation.
 */
public class SpaceInvadersGame extends Game {
    public AssetManager assets;

    @Override
    public void create() {
        assets = new AssetManager();

        // Loading a UI skin
        assets.load("ui/spaceinvaders.json", Skin.class);

        // Loads the player and the barricade sprites
        assets.load("sprites/gameplay.atlas", TextureAtlas.class);

        // Loading sound effects
        assets.load("audio/sfx/shoot.wav", Sound.class);
        assets.load("audio/sfx/explosion.wav", Sound.class);

        assets.finishLoading();

        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        assets.dispose();
    }
}