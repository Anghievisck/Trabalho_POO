package com.group9.spaceinvaders.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

import com.group9.spaceinvaders.model.Player;
import com.group9.spaceinvaders.controller.PlayerController;

// 1. Importando as novas classes do Swarm
import com.group9.spaceinvaders.model.Swarm;
import com.group9.spaceinvaders.controller.SwarmController;
import com.group9.spaceinvaders.model.Enemy;

public class GameScreen extends ScreenAdapter {
    private Player playerOne;
    private PlayerController playerOneController;

    // 2. Declarando o Swarm e seu Controller
    private Swarm swarm;
    private SwarmController swarmController;
    
    private ShapeRenderer shapeRenderer;

    public GameScreen() {
        playerOne = new Player(300, 50, 50, 50);
        playerOneController = new PlayerController(playerOne);

        // 3. Inicializa a nuvem de inimigos
        // Parâmetros: startX, startY, width, height, padding (espaçamento)
        // Coloquei Y = 400 para eles começarem na parte de cima da tela
        swarm = new Swarm(50, 400, 30, 30, 15);
        
        // O Controller precisa da largura da tela. 
        // O Gdx.graphics.getWidth() pega o tamanho exato da janela do seu jogo!
        swarmController = new SwarmController(swarm, Gdx.graphics.getWidth());

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        // --- 4. FASE DE ATUALIZAÇÃO LÓGICA (CONTROLLERS) ---
        playerOneController.update(delta);
        swarmController.update(delta); // Faz o zigue-zague matemático acontecer

        // --- 5. FASE DE DESENHO (VIEW) ---
        ScreenUtils.clear(0, 0, 0, 1);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Desenha o Player (Verde)
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(playerOne.bounds.x, playerOne.bounds.y, playerOne.bounds.width, playerOne.bounds.height);

        // Desenha o Swarm (Vermelho)
        shapeRenderer.setColor(Color.RED);
        
        // Percorre a nossa matriz 5x11
        for (int r = 0; r < swarm.rows; r++) {
            for (int c = 0; c < swarm.cols; c++) {
                Enemy enemy = swarm.enemies[r][c];
                
                // Só manda a placa de vídeo desenhar se o inimigo ainda estiver vivo
                if (enemy.isAlive) {
                    shapeRenderer.rect(enemy.bounds.x, enemy.bounds.y, enemy.bounds.width, enemy.bounds.height);
                }
            }
        }
        
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose(); // Mantendo o gerenciamento de memória limpo!
    }
}