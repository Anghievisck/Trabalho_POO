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

public class GameOverScreen extends ScreenAdapter {
    // Responsável por desenhar na tela
    private Stage stage;

    public GameOverScreen(SpaceInvadersGame game, boolean won, boolean twoPlayers, int difficulty){
        // 1. Configurando o Stage e permitindo que ele receba os cliques do mouse
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // 2. Resgatando a nossa Skin diretamente do AssetManager central
        Skin skin = game.assets.get("ui/spaceinvaders.json", Skin.class);

        // 3. Criando uma Table para organizar os botões automaticamente
        Table table = new Table();
        table.setFillParent(true); // Faz a tabela ocupar a tela inteira para centralizar tudo

        TextButton btnTentarNovamente = new TextButton("Tentar Novamente", skin);
        TextButton btnVoltar = new TextButton("Voltar para o Menu", skin);

        btnTentarNovamente.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, difficulty, twoPlayers));
            }
        });

        btnVoltar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        // 6. Adicionando os botões na tabela (definindo o tamanho padrão e um espaçamento)
        float btnWidth = 300f;
        float btnHeight = 50f;
        float padding = 20f;

        table.add(btnTentarNovamente).width(btnWidth).height(btnHeight).padBottom(padding).row();
        table.add(btnVoltar).width(btnWidth).height(btnHeight).padBottom(padding).row();

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        // Limpa a tela com o fundo preto
        ScreenUtils.clear(0f, 0f, 0f, 1);

        // Atualiza a lógica da UI (animações, hover, cliques) e a desenha na tela
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Garante que a UI se reajuste caso o jogador redimensione a janela
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
