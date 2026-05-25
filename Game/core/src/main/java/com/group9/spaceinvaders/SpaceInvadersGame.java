package com.group9.spaceinvaders;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class SpaceInvadersGame extends Game {
    @Override
    public void create() {
        setScreen(new FirstScreen());
    }
}