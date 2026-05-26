package com.group9.spaceinvaders.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen extends ScreenAdapter {
    // Referência ao jogo principal para podermos trocar de tela depois
    private SpaceInvadersGame game; 

    // Nossos três pintores
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;

    // As caixas de colisão matemáticas dos nossos botões
    private Rectangle btnTentarNovamente;
    private Rectangle btnVoltar;

    private int difficulty;
    private boolean twoPlayers;

    public GameOverScreen(SpaceInvadersGame game, boolean won, boolean twoPlayers, int difficulty){
        this.game = game;
        this.difficulty = difficulty;
        this.twoPlayers = twoPlayers;

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont(); 
        
        // Aumentando o tamanho da fonte padrão um pouquinho
        font.getData().setScale(1.5f);

        // Descobrindo o tamanho da tela para centralizar a matemática
        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        float btnWidth = 300;
        float btnHeight = 50;
        float startX = (sw - btnWidth) / 2; // Centraliza no eixo X

        // Posicionando os botões de cima para baixo
        btnTentarNovamente = new Rectangle(startX, sh - 200, btnWidth, btnHeight);
        btnVoltar = new Rectangle(startX, sh - 280, btnWidth, btnHeight);
    }

    @Override
    public void render(float delta) {
        // 1. LÓGICA (O Controller do Menu)
        handleInput();

        // 2. DESENHO (A View do Menu)
        ScreenUtils.clear(0.1f, 0.1f, 0.2f, 1); // Fundo azul marinho escuro

        // Primeiro pintamos os retângulos de fundo
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(btnTentarNovamente.x, btnTentarNovamente.y, btnTentarNovamente.width, btnTentarNovamente.height);
        shapeRenderer.rect(btnVoltar.x, btnVoltar.y, btnVoltar.width, btnVoltar.height);
        shapeRenderer.end();

        batch.begin();
        font.draw(batch, "Tentar Novamente", btnTentarNovamente.x + 90, btnTentarNovamente.y + 35);
        font.draw(batch, "Voltar para o Menu", btnVoltar.x + 20, btnVoltar.y + 35);
        batch.end();
    }

    private void handleInput() {
        // justTouched() verifica se o clique esquerdo do mouse acabou de acontecer
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            
            // A ARMADILHA DA LIBGDX: 
            // O mouse do Windows lê o Y=0 no TOPO da tela.
            // O desenho da LibGDX lê o Y=0 na BASE da tela.
            // Precisamos inverter a coordenada Y do clique do mouse!
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // O método .contains() da classe Rectangle verifica se o ponto X,Y está dentro dele
            if (btnTentarNovamente.contains(touchX, touchY)) {
                // Inicia o jogo principal!
                game.setScreen(new GameScreen(game, difficulty, this.twoPlayers));
            } else if (btnVoltar.contains(touchX, touchY)) {
                game.setScreen(new MainMenuScreen(game));
            }
        }
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
    }
}
