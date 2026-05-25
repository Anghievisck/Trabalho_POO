package com.group9.spaceinvaders.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.group9.spaceinvaders.SpaceInvadersGame;

public class MainMenuScreen implements Screen {
    private final SpaceInvadersGame game;
    private Stage stage;
    private Skin skin;
    private Table table;

    /**
     * passa a referencia para o main Game controller para possibilitar trocas de
     * tela posteirores
     * 
     * @param
     */
    public MainMenuScreen(SpaceInvadersGame game) {
        this.game = game;
    }

    @Override
    public void show() {

        // 1. Inicia Estagio e Skin
        // Inicia estagio
        stage = new Stage(new ScreenViewport());

        // Assinala nova skin
        // [NOTE] Espera que o arquivo uiskin esteja dentro de assets/ui
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // 2. Redireciona os clicks do mouse do usuario e inputs para o Estagio UI
        Gdx.input.setInputProcessor(stage);

        // 3. Cria a tabela de layout para agrupar os elementos UI
        table = new Table();
        table.setFillParent(true); // Makes the table stretch to fit the window size
        stage.addActor(table);

        // Optional: Turn this on if you want to see visual layout boundaries for
        // [OPCIONAL] Ligue para ver os layouts das fronteiras
        // debugging
        // table.setDebug(true);

        // 4. Cria os Elementos de UI
        Label titleLabel = new Label("SPACE INVADERS", skin);
        TextButton playButton = new TextButton("PLAY", skin);
        TextButton controlsButton = new TextButton("CONTROLS", skin);
        TextButton creditsButton = new TextButton("CREDITS", skin);
        TextButton exitButton = new TextButton("EXIT", skin);

        // 5. Add Click Listeners to handle button routing logic
        // 5. Adciona Listeners para lidar com a logica dos botoes
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // When we build GameScreen later, this will switch views
                // Placeholder para quando fizermos a GameScreen, vai mudar a tela
                // game.setScreen(new GameScreen(game));
                System.out.println("Jogo iniciando! Se prepare!");
            }
        });

        controlsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // game.setScreen(new ControlsScreen(game));
                System.out.println("Controls clicked!");
            }
        });

        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // game.setScreen(new CreditsScreen(game));
                System.out.println("Credits clicked!");
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit(); // Fecha a janela do programa
            }
        });

        // 6. Constroi o Layout de Grid dentro da Tabela
        // .row() age como um line break, avisando a tabela para mover para a proxima
        // linha
        table.add(titleLabel).padBottom(60).row();

        // uniformX makes all buttons match the width of the longest text string
        table.add(playButton).fillX().uniformX().padBottom(15).row();
        table.add(controlsButton).fillX().uniformX().padBottom(15).row();
        table.add(creditsButton).fillX().uniformX().padBottom(15).row();
        table.add(exitButton).fillX().uniformX();
    }

    @Override
    public void render(float delta) {
        // Limpa a tela com uma cor solida
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Atualiza os frames de animacao e calculos da UI
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        // Renderiza as camadas da UI no topo
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Mantem a UI centrada e formatada em casos de tela cheia ou resizing
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        // Para de buscar por inputs enquanto troca de telas para
        // previnir ghost clicks
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        // CRITICAL FOR MEMORY MANAGEMENT: Clear native C bindings to prevent memory
        // Gerenciamento de Memoria: Limpa Bindiings para previnir memory leaks
        stage.dispose();
        skin.dispose();
    }
}
