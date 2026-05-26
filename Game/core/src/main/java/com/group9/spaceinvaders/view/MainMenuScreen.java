package com.group9.spaceinvaders.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen extends ScreenAdapter {
    
    private Game game; // Referência ao jogo principal para podermos trocar de tela
    private SpriteBatch batch;
    private BitmapFont font;

    // Recebemos o objeto Game principal no construtor
    public MainMenuScreen(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont(); // Usa a fonte padrão do LibGDX
        this.font.getData().setScale(2f); // Deixa a fonte um pouco maior
    }

    @Override
    public void render(float delta) {
        // Limpa a tela com uma cor de fundo (aqui, um azul bem escuro)
        ScreenUtils.clear(0, 0, 0.2f, 1);

        batch.begin();
        
        // Desenhando o título
        font.setColor(Color.YELLOW);
        font.getData().setScale(3f);
        // Os valores X e Y determinam a posição do texto na tela. Você pode ajustá-los.
        font.draw(batch, "SPACE INVADERS", 150, 400); 

        // Desenhando a instrução para o jogador
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);
        font.draw(batch, "Pressione ENTER para começar", 200, 250);
        
        batch.end();

        // --- LÓGICA DE TRANSIÇÃO DE TELA ---
        // Se o jogador apertar a tecla ENTER (ou SPACE), iniciamos o jogo
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            // Troca a tela atual para a GameScreen
            game.setScreen(new GameScreen());
            
            // Descarta os recursos do menu da memória, pois não precisamos mais deles
            dispose(); 
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}