package com.group9.spaceinvaders.view;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

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
    private int NumberShoot = 5;
  
    private Stage hudStage;
    private Label scoreLabelP1;
    private Label scoreLabelP2;
    private BitmapFont font;
    private Label livesLabelP1;
    private Label livesLabelP2;
    int difficult = 1;

    public GameScreen(int difficulty) {
        playerOne = new Player(300, 50, 50, 50,  Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.SPACE);
        playerOneController = new PlayerController(playerOne);

        playerTwo = new Player(500, 50, 50, 50, Input.Keys.A, Input.Keys.D, Input.Keys.W);
        playerTwoController = new PlayerController(playerTwo);

        NumberShoot = difficulty;

        for(int i = 0; i < 5; i++){
            playerBullets.add(new PlayerBullet(-10, -10, 5, 15));
        }
        
        for(int i = 0; i < NumberShoot; i++){
            enemyBullets.add(new EnemyBullet(-10, -10, 5, 15));
        }

        // 3. Inicializa a nuvem de inimigos
        // Parâmetros: startX, startY, width, height, padding (espaçamento)
        // Coloquei Y = 400 para eles começarem na parte de cima da tela
        swarms.add(new Swarm(50, 400, 30, 30, 15, (float)1.5*difficulty));
        
        // O Controller precisa da largura da tela. 
        // O Gdx.graphics.getWidth() pega o tamanho exato da janela do seu jogo!
        swarmControllers.add(new SwarmController(swarms.get(0), Gdx.graphics.getWidth()));
        playerBulletControllers.add(new PlayerBulletController(playerBullets.get(0)));
        playerBulletControllers.add(new PlayerBulletController(playerBullets.get(1)));

        for(int i = 0; i < NumberShoot; i++){
            enemyBulletControllers.add(new EnemyBulletController(enemyBullets.get(i)));
        }
        shapeRenderer = new ShapeRenderer();
        
        hudStage = new Stage(new ScreenViewport());
        // 2. Cria a fonte e o estilo da Label
        font = new BitmapFont(); // Fonte padrão do LibGDX (branca)
        font.getData().setScale(1.5f); // Ajusta o tamanho se precisar
        
        // O estilo define qual fonte e qual cor o texto terá
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        // 3. Instancia as Labels com o texto inicial
        scoreLabelP1 = new Label("P1: 0", labelStyle);
        scoreLabelP2 = new Label("P2: 0", labelStyle);

        // 4. Posiciona na tela (Exemplo: no topo da tela)
        scoreLabelP1.setPosition(20, Gdx.graphics.getHeight() - 40);
        scoreLabelP2.setPosition(Gdx.graphics.getWidth() - 120, Gdx.graphics.getHeight() - 40);

        // 5. Adiciona as labels ao palco para que possam ser desenhadas
        hudStage.addActor(scoreLabelP1);
        hudStage.addActor(scoreLabelP2);
        
        livesLabelP1 = new Label("Vidas: XXX", labelStyle);
        livesLabelP2 = new Label("Vidas: XXX", labelStyle);

        livesLabelP1.setPosition(180, Gdx.graphics.getHeight() - 40);

        livesLabelP2.setPosition(Gdx.graphics.getWidth() - 280, Gdx.graphics.getHeight() - 40);

        hudStage.addActor(livesLabelP1);
        hudStage.addActor(livesLabelP2);
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

        for(int i = 0; i < NumberShoot; i++){
            enemyBulletControllers.get(i).EnemyShootUpdate(swarms.get(0));
            enemyBulletControllers.get(i).update(delta);
        }

        scoreLabelP1.setText("P1: " + playerOne.points);
        scoreLabelP2.setText("P2: " + playerTwo.points);

        livesLabelP1.setText("Vidas: " + formatarVidas(playerOne.lives));
        livesLabelP2.setText("Vidas: " + formatarVidas(playerTwo.lives));

        if(playerOne.lives <= 0 && playerTwo.lives <= 0) {
            // Ambos os jogadores perderam, você pode mostrar uma tela de Game Over ou reiniciar o jogo
            // Por enquanto, vamos apenas imprimir no console
            System.out.println("Game Over! Ambos os jogadores perderam.");
        } else if (playerOne.lives <= 0) {
            System.out.println("Game Over! Jogador 1 perdeu.");
        } else if (playerTwo.lives <= 0) {
            System.out.println("Game Over! Jogador 2 perdeu.");
        }
        if(swarms.get(0).aliveCount <= 0){
            playerOne.points += 100;
            playerTwo.points += 100;
            playerOne.lives += 1;
            playerTwo.lives += 1;
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
        for(int i = 0; i < NumberShoot; i++){
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
        hudStage.act(delta);
        hudStage.draw();
    }

    private String formatarVidas(int quantidadeVidas) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < quantidadeVidas; i++) {
            sb.append("X "); // Use "X ", "<3 " ou "O " como símbolo temporário
        }
        return sb.toString();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose(); // Mantendo o gerenciamento de memória limpo!
        hudStage.dispose();
        font.dispose();
    }
}