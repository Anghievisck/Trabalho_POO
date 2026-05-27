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

public class MainMenuScreen extends ScreenAdapter {
    // O Stage substitui o SpriteBatch, ShapeRenderer e as lógicas de Rectangle
    private Stage stage;

    public MainMenuScreen(SpaceInvadersGame game) {
        // 1. Configurando o Stage e permitindo que ele receba os cliques do mouse
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // 2. Resgatando a nossa Skin diretamente do AssetManager central
        Skin skin = game.assets.get("ui/spaceinvaders.json", Skin.class);

        // 3. Criando uma Table para organizar os botões automaticamente
        Table table = new Table();
        table.setFillParent(true); // Faz a tabela ocupar a tela inteira para centralizar tudo
        // table.setDebug(true); // Dica: Descomente esta linha se quiser ver as linhas da tabela!

        // 4. Criando os botões com a nossa skin
        TextButton btnNovoJogo = new TextButton("Novo Jogo", skin);
        TextButton btnDoisJogadores = new TextButton("Novo Jogo (2-Jogadores)", skin);
        TextButton btnCarregar = new TextButton("Carregar Jogo Salvo", skin);
        TextButton btnSair = new TextButton("Sair", skin);

        // btnNovoJogo.getLabel().setFontScale(1.5f);

        // 5. Adicionando a lógica de clique (ChangeListener) para cada botão
        btnNovoJogo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, 1, false));
            }
        });

        btnDoisJogadores.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, 1, true));
            }
        });

        btnCarregar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Carregamento em breve...");
            }
        });

        btnSair.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit(); // Fecha a janela do jogo
            }
        });

        // 6. Adicionando os botões na tabela (definindo o tamanho padrão e um espaçamento)
        float btnWidth = 300f;
        float btnHeight = 50f;
        float padding = 20f;

        table.add(btnNovoJogo).width(btnWidth).height(btnHeight).padBottom(padding).row();
        table.add(btnDoisJogadores).width(btnWidth).height(btnHeight).padBottom(padding).row();
        table.add(btnCarregar).width(btnWidth).height(btnHeight).padBottom(padding).row();
        table.add(btnSair).width(btnWidth).height(btnHeight);

        // 7. Por fim, adicionamos a tabela pronta ao nosso palco (Stage)
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        // Limpa a tela com o fundo azul marinho escuro
        ScreenUtils.clear(0.1f, 0.1f, 0.2f, 1);

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
        // Sempre descartar o stage! 
        // Lembre-se: não descartamos a Skin aqui porque o AssetManager é o dono dela.
        stage.dispose();
    }
}