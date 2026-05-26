package com.group9.spaceinvaders.view;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input;

import com.group9.spaceinvaders.model.Player;
import com.group9.spaceinvaders.controller.PlayerController;

import com.group9.spaceinvaders.model.PlayerBullet;
import com.group9.spaceinvaders.controller.PlayerBulletController;

import com.group9.spaceinvaders.model.EnemyBullet;
import com.group9.spaceinvaders.controller.EnemyBulletController;

// 1. Importando as novas classes do Swarm
import com.group9.spaceinvaders.model.Swarm;
import com.group9.spaceinvaders.controller.SwarmController;
import com.group9.spaceinvaders.model.Enemy;

public class GameScreen extends ScreenAdapter {
    private Player playerOne;
    private Player playerTwo;

    private PlayerController playerOneController;
    private PlayerController playerTwoController;

    // 1. Declarando as listas que terao os objetos
    
    private List<Swarm> swarms = new ArrayList<>(); 
    private List<SwarmController> swarmControllers = new ArrayList<>();  
    private List<PlayerBullet> playerBullets = new ArrayList<>();
    private List<PlayerBulletController> playerBulletControllers = new ArrayList<>();
    private List<EnemyBullet> enemyBullets = new ArrayList<>();
    private List<EnemyBulletController> enemyBulletControllers = new ArrayList<>();
    private ShapeRenderer shapeRenderer;
  
    public GameScreen() {
        playerOne = new Player(300, 50, 50, 50,  Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.SPACE);
        playerOneController = new PlayerController(playerOne);

        playerTwo = new Player(500, 50, 50, 50, Input.Keys.A, Input.Keys.D, Input.Keys.W);
        playerTwoController = new PlayerController(playerTwo);

        for(int i = 0; i < 5; i++){
            playerBullets.add(new PlayerBullet(-10, -10, 5, 15));
        }
        
        for(int i = 0; i < 5; i++){
            enemyBullets.add(new EnemyBullet(-10, -10, 5, 15));
        }

        // 3. Inicializa a nuvem de inimigos
        // Parâmetros: startX, startY, width, height, padding (espaçamento)
        // Coloquei Y = 400 para eles começarem na parte de cima da tela
        swarms.add(new Swarm(50, 400, 30, 30, 15));
        
        // O Controller precisa da largura da tela. 
        // O Gdx.graphics.getWidth() pega o tamanho exato da janela do seu jogo!
        swarmControllers.add(new SwarmController(swarms.get(0), Gdx.graphics.getWidth()));
        playerBulletControllers.add(new PlayerBulletController(playerBullets.get(0)));
        playerBulletControllers.add(new PlayerBulletController(playerBullets.get(1)));

        for(int i = 0; i < 5; i++){
            enemyBulletControllers.add(new EnemyBulletController(enemyBullets.get(i)));
        }
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        // --- 4. FASE DE ATUALIZAÇÃO LÓGICA (CONTROLLERS) ---
        playerOneController.update(delta, enemyBullets); // Lê o teclado e move a nave
        playerTwoController.update(delta, enemyBullets); // Lê o teclado e move a nave

        swarmControllers.get(0).update(delta, playerBullets); // Faz o zigue-zague matemático acontecer
        
        playerBulletControllers.get(0).PlayerShootUpdate(playerOne);
        playerBulletControllers.get(0).update(delta);

        playerBulletControllers.get(1).PlayerShootUpdate(playerTwo);
        playerBulletControllers.get(1).update(delta);

        for(int i = 0; i < 5; i++){
            enemyBulletControllers.get(i).EnemyShootUpdate(swarms.get(0));
            enemyBulletControllers.get(i).update(delta);
        }

        // --- 5. FASE DE DESENHO (VIEW) ---
        ScreenUtils.clear(0, 0, 0, 1);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Desenha o Player (Verde)
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(playerOne.bounds.x, playerOne.bounds.y, playerOne.bounds.width, playerOne.bounds.height);

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(playerTwo.bounds.x, playerTwo.bounds.y, playerTwo.bounds.width, playerTwo.bounds.height);

        // Desenha o Swarm (Vermelho)
        shapeRenderer.setColor(Color.RED);
        
        // Percorre a nossa matriz 5x11
        for (int r = 0; r < swarms.get(0).rows; r++) {
            for (int c = 0; c < swarms.get(0).cols; c++) {
                Enemy enemy = swarms.get(0).enemies[r][c];
                // Só manda a placa de vídeo desenhar se o inimigo ainda estiver vivo
                if (enemy.isAlive) {
                    shapeRenderer.rect(enemy.bounds.x, enemy.bounds.y, enemy.bounds.width, enemy.bounds.height);
                }
            }
        }
        // Desenha o Tiro do Inimigo (Vermelho)
        shapeRenderer.setColor(Color.RED);
        for(int i = 0; i < 5; i++){
            if(enemyBullets.get(i).isValid){
                shapeRenderer.rect(enemyBullets.get(i).bounds.x, enemyBullets.get(i).bounds.y, enemyBullets.get(i).bounds.width, enemyBullets.get(i).bounds.height);
            }
        }

        // Desenha o Tiro (Branco)
        shapeRenderer.setColor(Color.WHITE);
        
        if(playerBullets.get(0).isValid){
            playerOne.canShoot = false;
            shapeRenderer.rect(playerBullets.get(0).bounds.x, playerBullets.get(0).bounds.y, playerBullets.get(0).bounds.width, playerBullets.get(0).bounds.height);
        } else {
            playerOne.canShoot = true;
        }
        if(playerBullets.get(1).isValid){
            playerTwo.canShoot = false;
            shapeRenderer.rect(playerBullets.get(1).bounds.x, playerBullets.get(1).bounds.y, playerBullets.get(1).bounds.width, playerBullets.get(1).bounds.height);
        } else {
            playerTwo.canShoot = true;
        }
        
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose(); // Mantendo o gerenciamento de memória limpo!
    }
}