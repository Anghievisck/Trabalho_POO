package com.group9.spaceinvaders.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Screen displayed when the player loses all lives or the enemies reach the bottom.
 */
public class GameOverScreen extends ScreenAdapter {
    // Responsible for drawing on the screen
    private Stage stage;

    /**
     * Constructs the Game Over screen.
     *
     * @param game       the main game instance
     * @param won        boolean indicating if the game was won or lost (currently unused logic)
     * @param twoPlayers boolean indicating if it was a two-player game
     * @param difficulty the current difficulty level
     */
    public GameOverScreen(SpaceInvadersGame game, boolean won, boolean twoPlayers, int difficulty) {
        // 1. Configuring the Stage and allowing it to receive mouse clicks
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // 2. Retrieving our Skin directly from the central AssetManager
        Skin skin = game.assets.get("ui/spaceinvaders.json", Skin.class);

        // 3. Creating a Table to organize the buttons automatically
        Table table = new Table();
        table.setFillParent(true); // Makes the table fill the entire screen to center everything

        TextButton btnTryAgain = new TextButton("Try Again", skin);
        TextButton btnBackToMenu = new TextButton("Back to Menu", skin);

        btnTryAgain.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, difficulty, twoPlayers));
            }
        });

        btnBackToMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        // 6. Adding the buttons to the table (setting default size and padding)
        float btnWidth = 300f;
        float btnHeight = 50f;
        float padding = 20f;

        table.add(btnTryAgain).width(btnWidth).height(btnHeight).padBottom(padding).row();
        table.add(btnBackToMenu).width(btnWidth).height(btnHeight).padBottom(padding).row();

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        // Clears the screen with a black background
        ScreenUtils.clear(0f, 0f, 0f, 1);

        // Updates UI logic (animations, hover, clicks) and draws it on the screen
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Ensures the UI readjusts if the player resizes the window
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}