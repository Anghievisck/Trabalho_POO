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

    private boolean twoPlayers;

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

    private boolean nextLevel;
    private int swarmIndex = 0;
    private int difficulty;

    private SpaceInvadersGame game;

    public GameScreen(SpaceInvadersGame game, int difficulty, boolean twoPlayers) {
        this.twoPlayers = twoPlayers;
        this.game = game;
        this.difficulty = difficulty;

        playerOne = new Player(300, 50, 50, 50,  Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.SPACE);
        playerOneController = new PlayerController(playerOne);

        if(this.twoPlayers){
            playerTwo = new Player(500, 50, 50, 50, Input.Keys.A, Input.Keys.D, Input.Keys.W);
            playerTwoController = new PlayerController(playerTwo);
        }

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
        for(int i = 0; i < 3; i++){
            swarms.add(new Swarm(50, 400, 30, 30, 15, i + (float)1.5*difficulty));
            swarmControllers.add(new SwarmController(swarms.get(i), Gdx.graphics.getWidth()));
        }
        
        // O Controller precisa da largura da tela. 
        // O Gdx.graphics.getWidth() pega o tamanho exato da janela do seu jogo!
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

        // 4. Posiciona na tela (Exemplo: no topo da tela)
        scoreLabelP1.setPosition(20, Gdx.graphics.getHeight() - 40);

        // 5. Adiciona as labels ao palco para que possam ser desenhadas
        hudStage.addActor(scoreLabelP1);
        livesLabelP1 = new Label("Vidas: XXX", labelStyle);
        livesLabelP1.setPosition(180, Gdx.graphics.getHeight() - 40);

        hudStage.addActor(livesLabelP1);

        // Realiza o mesmo processo acima para o player 2, se existir.
        if(this.twoPlayers){
            scoreLabelP2 = new Label("P2: 0", labelStyle);
            scoreLabelP2.setPosition(Gdx.graphics.getWidth() - 120, Gdx.graphics.getHeight() - 40);

            hudStage.addActor(scoreLabelP2);
            livesLabelP2 = new Label("Vidas: XXX", labelStyle);
            livesLabelP2.setPosition(Gdx.graphics.getWidth() - 280, Gdx.graphics.getHeight() - 40);

            hudStage.addActor(livesLabelP2);
        }
    }

    @Override
    public void render(float delta) {
        if(nextLevel){
            if(swarmIndex < 3){
                swarmIndex++;
            } else {
                game.setScreen(new GameOverScreen(game, true, twoPlayers, this.difficulty));
            }
        }
        // --- 4. FASE DE ATUALIZAÇÃO LÓGICA (CONTROLLERS) ---
        playerOneController.update(delta, enemyBullets); // Lê o teclado e move a nave

        if(this.twoPlayers){
            playerTwoController.update(delta, enemyBullets); // Lê o teclado e move a nave

            playerBulletControllers.get(1).PlayerShootUpdate(playerTwo);
            playerBulletControllers.get(1).update(delta);

            scoreLabelP2.setText("P2: " + playerTwo.points);

            livesLabelP2.setText("Vidas: " + formatarVidas(playerTwo.lives));
        }

        swarmControllers.get(swarmIndex).update(delta, playerBullets); // Faz o zigue-zague matemático acontecer
        
        playerBulletControllers.get(0).PlayerShootUpdate(playerOne);
        playerBulletControllers.get(0).update(delta);


        for(int i = 0; i < NumberShoot; i++){
            enemyBulletControllers.get(i).EnemyShootUpdate(swarms.get(0));
            enemyBulletControllers.get(i).update(delta);
        }

        scoreLabelP1.setText("P1: " + playerOne.points);


        livesLabelP1.setText("Vidas: " + formatarVidas(playerOne.lives));

        // Mudar System.out.println para uma mensagem na tela GameOverScreen.java
        if(this.twoPlayers){
            if(playerOne.lives <= 0 && playerTwo.lives <= 0){
                game.setScreen(new GameOverScreen(game, false, twoPlayers, this.difficulty));
            }
        } else {
            if (playerOne.lives <= 0) {
                game.setScreen(new GameOverScreen(game, false, twoPlayers, this.difficulty));
            }
        }

        if(swarms.get(0).aliveCount <= 0){
            playerOne.points += 100;
            playerOne.lives += 1;

            if(twoPlayers){
                playerTwo.points += 100;
                playerTwo.lives += 1;
            }

            nextLevel = true;
        }

        // --- 5. FASE DE DESENHO (VIEW) ---
        ScreenUtils.clear(0, 0, 0, 1);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Desenha o Player (Verde)
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(playerOne.hitbox.x, playerOne.hitbox.y, playerOne.hitbox.width, playerOne.hitbox.height);

        if(this.twoPlayers){
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.rect(playerTwo.hitbox.x, playerTwo.hitbox.y, playerTwo.hitbox.width, playerTwo.hitbox.height);
        }

        // Desenha o Swarm (Vermelho)
        shapeRenderer.setColor(Color.RED);
        
        // Percorre a nossa matriz 5x11
        for (int r = 0; r < swarms.get(swarmIndex).rows; r++) {
            for (int c = 0; c < swarms.get(swarmIndex).cols; c++) {
                Enemy enemy = swarms.get(swarmIndex).enemies[r][c];
                // Só manda a placa de vídeo desenhar se o inimigo ainda estiver vivo
                if (enemy.isAlive) {
                    shapeRenderer.rect(enemy.hitbox.x, enemy.hitbox.y, enemy.hitbox.width, enemy.hitbox.height);
                }
            }
        }
        // Desenha o Tiro do Inimigo (Vermelho)
        shapeRenderer.setColor(Color.RED);
        for(int i = 0; i < NumberShoot; i++){
            if(enemyBullets.get(i).isValid){
                shapeRenderer.rect(enemyBullets.get(i).hitbox.x, enemyBullets.get(i).hitbox.y, enemyBullets.get(i).hitbox.width, enemyBullets.get(i).hitbox.height);
            }
        }

        // Desenha o Tiro (Branco)
        shapeRenderer.setColor(Color.WHITE);
        
        if(playerBullets.get(0).isValid){
            playerOne.canShoot = false;
            shapeRenderer.rect(playerBullets.get(0).hitbox.x, playerBullets.get(0).hitbox.y, playerBullets.get(0).hitbox.width, playerBullets.get(0).hitbox.height);
        } else {
            playerOne.canShoot = true;
        }

        if(this.twoPlayers){
            if(playerBullets.get(1).isValid){
                playerTwo.canShoot = false;
                shapeRenderer.rect(playerBullets.get(1).hitbox.x, playerBullets.get(1).hitbox.y, playerBullets.get(1).hitbox.width, playerBullets.get(1).hitbox.height);
            } else {
                playerTwo.canShoot = true;
            }
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