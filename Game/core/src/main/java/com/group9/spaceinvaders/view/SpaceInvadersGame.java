package com.group9.spaceinvaders.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.audio.Sound;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class SpaceInvadersGame extends Game {
    public AssetManager assets;

    @Override
    public void create() {
        assets = new AssetManager();

        // Loading a UI skin
        assets.load("ui/spaceinvaders.json", Skin.class);

        // Loads the player and the barricade
        assets.load("sprites/gameplay.atlas", TextureAtlas.class);

        // Carregando os efeitos sonoros
        assets.load("audio/sfx/shoot.wav", Sound.class);
        assets.load("audio/sfx/explosion.wav", Sound.class);

        assets.finishLoading();

        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose(){
        assets.dispose();
    }
}