package com.group9.spaceinvaders.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen extends ScreenAdapter {
    // Referência ao jogo principal para podermos trocar de tela depois
    private SpaceInvadersGame game; 

    // Nossos três pintores
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;

    // As caixas de colisão matemáticas dos nossos botões
    private Rectangle btnNovoJogo;
    private Rectangle btnDoisJogadores;
    private Rectangle btnCarregar;
    private Rectangle btnSair;

    private TextButton btnNewGame;
    private TextButton btnTwoPlayersNewGame;
    private TextButton btnLoad;
    private TextButton btnExit;

    public MainMenuScreen(SpaceInvadersGame game) {
        this.game = game;
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
        btnNovoJogo = new Rectangle(startX, sh - 200, btnWidth, btnHeight);
        btnDoisJogadores = new Rectangle(startX, sh - 280, btnWidth, btnHeight);
        btnCarregar = new Rectangle(startX, sh - 360, btnWidth, btnHeight);
        btnSair = new Rectangle(startX, sh - 440, btnWidth, btnHeight);
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
        shapeRenderer.rect(btnNovoJogo.x, btnNovoJogo.y, btnNovoJogo.width, btnNovoJogo.height);
        shapeRenderer.rect(btnDoisJogadores.x, btnDoisJogadores.y, btnDoisJogadores.width, btnDoisJogadores.height);
        shapeRenderer.rect(btnCarregar.x, btnCarregar.y, btnCarregar.width, btnCarregar.height);
        shapeRenderer.rect(btnSair.x, btnSair.y, btnSair.width, btnSair.height);
        shapeRenderer.end();

        // Depois pintamos os textos por cima (usando offsets fixos para "centralizar" o texto)
        batch.begin();
        font.draw(batch, "Novo Jogo", btnNovoJogo.x + 90, btnNovoJogo.y + 35);
        font.draw(batch, "Novo Jogo (2-Jogadores)", btnDoisJogadores.x + 20, btnDoisJogadores.y + 35);
        font.draw(batch, "Carregar Jogo Salvo", btnCarregar.x + 50, btnCarregar.y + 35);
        font.draw(batch, "Sair", btnSair.x + 130, btnSair.y + 35);
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
            if (btnNovoJogo.contains(touchX, touchY)) {
                // Inicia o jogo principal!
                game.setScreen(new GameScreen(this.game, 1, false));
            } else if (btnDoisJogadores.contains(touchX, touchY)) {
                game.setScreen(new GameScreen(this.game, 1, true));
            } else if (btnCarregar.contains(touchX, touchY)) {
                System.out.println("Carregamento em breve...");
            } else if (btnSair.contains(touchX, touchY)) {
                Gdx.app.exit(); // Fecha a janela do jogo
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