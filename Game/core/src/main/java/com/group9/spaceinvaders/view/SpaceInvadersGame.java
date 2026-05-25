package com.group9.spaceinvaders.view;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class SpaceInvadersGame extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}