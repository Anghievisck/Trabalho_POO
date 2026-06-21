package com.group9.spaceinvaders.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class LevelCompleteScreen extends ScreenAdapter {
    private SpaceInvadersGame game;
    private int difficulty;
    private boolean twoPlayers;
    private int currentLevel;

    private int p1Points, p1Lives, p1Ammo;
    private int p2Points, p2Lives, p2Ammo;

    private Stage stage;
    private float displayTimer = 0f;
    private final float DISPLAY_DURATION = 3.0f; 

    public LevelCompleteScreen(SpaceInvadersGame game, int difficulty, boolean twoPlayers, int currentLevel,
                                int p1Points, int p1Lives, int p1Ammo,
                                int p2Points, int p2Lives, int p2Ammo) {
        this.game = game;
        this.difficulty = difficulty;
        this.twoPlayers = twoPlayers;
        this.currentLevel = currentLevel;
        this.p1Points = p1Points;
        this.p1Lives = p1Lives;
        this.p1Ammo = p1Ammo;
        this.p2Points = p2Points;
        this.p2Lives = p2Lives;
        this.p2Ammo = p2Ammo;

        stage = new Stage(new FitViewport(800, 600));
        BitmapFont font = new BitmapFont();
        font.getData().setScale(2.0f);

        Label.LabelStyle style = new Label.LabelStyle(font, Color.GREEN);
        Label label = new Label("FASE CONCLUIDA!\n\nPreparando proxima horda...", style);
        label.setAlignment(com.badlogic.gdx.utils.Align.center);
        label.setPosition(400f - label.getWidth() / 2f, 300f - label.getHeight() / 2f);

        stage.addActor(label);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        stage.act(delta);
        stage.draw();

        displayTimer += delta;
        if (displayTimer >= DISPLAY_DURATION) {
            // Repassa false no último parâmetro para carregar a nova fase com layout original nativo
            game.setScreen(new GameScreen(game, difficulty, twoPlayers, currentLevel + 1,
                    p1Points, p1Lives, p1Ammo, p2Points, p2Lives, p2Ammo, false));
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}