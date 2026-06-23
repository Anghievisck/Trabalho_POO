package com.group9.spaceinvaders.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen extends ScreenAdapter {
    private Stage stage;

    public MainMenuScreen(SpaceInvadersGame game) {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = game.assets.get("ui/spaceinvaders.json", Skin.class);

        Table table = new Table();
        table.setFillParent(true); 

        TextButton btnNovoJogo = new TextButton("Novo Jogo", skin);
        TextButton btnDoisJogadores = new TextButton("Novo Jogo (2-Jogadores)", skin);
        TextButton btnCarregar = new TextButton("Carregar Jogo Salvo", skin);
        TextButton btnSair = new TextButton("Sair", skin);

        btnNovoJogo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Último parâmetro indica que NÃO está carregando de um arquivo de save (false)
                game.setScreen(new GameScreen(game, 1, false, 0, 0, 3, 5, 0, 3, 5, false));
            }
        });

        btnDoisJogadores.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, 1, true, 0, 0, 3, 5, 0, 3, 5, false));
            }
        });

        btnCarregar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Preferences prefs = Gdx.app.getPreferences("SpaceInvadersSaveFile");
                
                if (prefs.getBoolean("hasSave", false)) {
                    int difficulty = prefs.getInteger("difficulty", 1);
                    boolean twoPlayers = prefs.getBoolean("twoPlayers", false);
                    int savedLevel = prefs.getInteger("swarmIndex", 0);

                    int p1Points = prefs.getInteger("p1Points", 0);
                    int p1Lives = prefs.getInteger("p1Lives", 3);
                    int p1Ammo = prefs.getInteger("p1Ammo", 5);

                    int p2Points = prefs.getInteger("p2Points", 0);
                    int p2Lives = prefs.getInteger("p2Lives", 3);
                    int p2Ammo = prefs.getInteger("p2Ammo", 5);

                    // Último parâmetro indica que DEVE carregar o layout dos inimigos salvos (true)
                    game.setScreen(new GameScreen(game, difficulty, twoPlayers, savedLevel, 
                            p1Points, p1Lives, p1Ammo, p2Points, p2Lives, p2Ammo, true));
                } else {
                    System.out.println("Nenhum arquivo de salvamento encontrado no sistema.");
                }
            }
        });

        btnSair.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit(); 
            }
        });

        float btnWidth = 300f;
        float btnHeight = 50f;
        float padding = 20f;

        table.add(btnNovoJogo).width(btnWidth).height(btnHeight).padBottom(padding).row();
        table.add(btnDoisJogadores).width(btnWidth).height(btnHeight).padBottom(padding).row();
        table.add(btnCarregar).width(btnWidth).height(btnHeight).padBottom(padding).row();
        table.add(btnSair).width(btnWidth).height(btnHeight);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.2f, 1);
        stage.act(delta);
        stage.draw();
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