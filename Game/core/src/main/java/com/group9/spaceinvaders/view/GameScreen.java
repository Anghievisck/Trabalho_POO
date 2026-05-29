package com.group9.spaceinvaders.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.group9.spaceinvaders.model.Player;
import com.group9.spaceinvaders.controller.PlayerController;
import com.group9.spaceinvaders.model.Bullet;
import com.group9.spaceinvaders.model.Swarm;
import com.group9.spaceinvaders.controller.SwarmController;
import com.group9.spaceinvaders.model.Enemy;

public class GameScreen extends ScreenAdapter {
    private SpaceInvadersGame game;
    private boolean twoPlayers;
    private int difficulty;

    private Player playerOne;
    private Player playerTwo;
    private PlayerController playerOneController;
    private PlayerController playerTwoController;

    private List<Swarm> swarms = new ArrayList<>(); 
    private List<SwarmController> swarmControllers = new ArrayList<>();  
    
    // A única lista de balas necessária agora!
    private List<Bullet> activeBullets = new ArrayList<>();

    // Sai o ShapeRenderer, entra o SpriteBatch para desenhar as Texturas
    private SpriteBatch batch;
  
    private Stage hudStage;
    private Label scoreLabelP1;
    private Label scoreLabelP2;
    private BitmapFont font;
    private Label livesLabelP1;
    private Label livesLabelP2;

    private boolean nextLevel;
    private int swarmIndex = 0;


    public GameScreen(SpaceInvadersGame game, int difficulty, boolean twoPlayers) {
        this.game = game;
        this.difficulty = difficulty;
        this.twoPlayers = twoPlayers;

        // Inicializa o Pintor
        batch = new SpriteBatch();
        TextureAtlas atlas = game.assets.get("sprites/gameplay.atlas", TextureAtlas.class);

        TextureRegion playerSprite = atlas.findRegion("player_placeholder");
        TextureRegion bulletSprite = atlas.findRegion("bullet_placeholder");

        // NOTA: Como você adicionou TextureRegion nos construtores do Player e do Swarm, 
        // você precisará passar as imagens corretas aqui (substituindo os "null").
        playerOne = new Player(300, 50, 50, 50, playerSprite, bulletSprite, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.SPACE);
        playerOneController = new PlayerController(playerOne);

        if(this.twoPlayers){
            playerTwo = new Player(500, 50, 50, 50, playerSprite, bulletSprite, Input.Keys.A, Input.Keys.D, Input.Keys.W);
            playerTwoController = new PlayerController(playerTwo);
        }

        List<TextureRegion> enemySprites = new ArrayList<>();
        enemySprites.add(atlas.findRegion("alien_crab1"));
        enemySprites.add(atlas.findRegion("alien_crab2"));

        List<TextureRegion> enemyBulletSprites = new ArrayList<>();
        enemyBulletSprites.add(atlas.findRegion("bullet_placeholder"));

        List<Float> enemyBulletSpeeds = new ArrayList<>();
        enemyBulletSpeeds.add(-250f);

        for(int i = 0; i < 3; i++){
            // Lembre-se de passar a lista de sprites e velocidades corretas no lugar dos nulls
            swarms.add(new Swarm((float)50, (float)400, (float)30, (float)30, (float)15, i + (float)1.5*difficulty, enemySprites, 100, enemyBulletSprites, enemyBulletSpeeds));
            swarmControllers.add(new SwarmController(swarms.get(i), Gdx.graphics.getWidth(), difficulty));
        }
        
        hudStage = new Stage(new ScreenViewport());
        font = new BitmapFont(); 
        font.getData().setScale(1.5f); 
        
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        scoreLabelP1 = new Label("P1: 0", labelStyle);
        scoreLabelP1.setPosition(20, Gdx.graphics.getHeight() - 40);
        hudStage.addActor(scoreLabelP1);
        
        livesLabelP1 = new Label("Vidas: XXX", labelStyle);
        livesLabelP1.setPosition(180, Gdx.graphics.getHeight() - 40);
        hudStage.addActor(livesLabelP1);

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
            if(swarmIndex < 2){ // Alterado de 3 para 2, pois a lista tem tamanho 3 (0, 1, 2)
                swarmIndex++;
                nextLevel = false;
            } else {
                game.setScreen(new GameOverScreen(game, true, twoPlayers, this.difficulty));
                return; // Impede que o resto do método rode após trocar de tela
            }
        }

        // --- LÓGICA (CONTROLLERS) ---
        // Passamos a lista geral de balas para todos os controllers interagirem
        playerOneController.update(delta, activeBullets); 

        if(this.twoPlayers){
            playerTwoController.update(delta, activeBullets); 
            scoreLabelP2.setText("P2: " + playerTwo.points);
            livesLabelP2.setText("Vidas: " + formatarVidas(playerTwo.lives));
        }

        swarmControllers.get(swarmIndex).update(delta, activeBullets); 

        scoreLabelP1.setText("P1: " + playerOne.points);
        livesLabelP1.setText("Vidas: " + formatarVidas(playerOne.lives));

        // Verificação de Morte
        if(this.twoPlayers){
            if(playerOne.lives <= 0 && playerTwo.lives <= 0){
                game.setScreen(new GameOverScreen(game, false, twoPlayers, this.difficulty));
                return;
            }
        } else {
            if (playerOne.lives <= 0) {
                game.setScreen(new GameOverScreen(game, false, twoPlayers, this.difficulty));
                return;
            }
        }

        // Verificação de Vitória da Fase
        if(swarms.get(swarmIndex).aliveCount <= 0){
            playerOne.points += 100;
            playerOne.lives += 1;
            if(twoPlayers){
                playerTwo.points += 100;
                playerTwo.lives += 1;
            }
            nextLevel = true;
        }

        // --- DESENHO (VIEW) ---
        ScreenUtils.clear(0, 0, 0, 1);

        // Iniciamos o lote de pintura
        batch.begin();
        
        // 1. Desenha os Players (O método draw já existe na classe pai Entity!)
        if (playerOne.lives > 0) playerOne.draw(batch);
        if (this.twoPlayers && playerTwo.lives > 0) playerTwo.draw(batch);

        // 2. Desenha o Swarm atual
        Swarm currentSwarm = swarms.get(swarmIndex);
        for (int r = 0; r < currentSwarm.rows; r++) {
            for (int c = 0; c < currentSwarm.cols; c++) {
                Enemy enemy = currentSwarm.enemies[r][c];
                // Checa a health em vez do isAlive (já que removemos o isAlive do Enemy)
                if (enemy != null && enemy.health > 0) {
                    enemy.draw(batch);
                }
            }
        }

        // 3. Move, Desenha e Limpa as Balas usando o Iterator
        Iterator<Bullet> iter = activeBullets.iterator();
        while(iter.hasNext()){
            Bullet b = iter.next();
            b.update(delta); // Voa
            b.draw(batch);   // Pinta na tela
            
            // O Garbage Collector agindo: Se a bala bateu em algo ou saiu da tela, deleta.
            if(!b.isValid){
                iter.remove();
            }
        }

        batch.end();

        // Desenha a Interface de Usuário por cima do jogo
        hudStage.act(delta);
        hudStage.draw();
    }

    private String formatarVidas(int quantidadeVidas) {
        StringBuilder sb = new StringBuilder();
        // Evita imprimir vidas negativas na UI se o jogador tomar dano a mais
        for (int i = 0; i < Math.max(0, quantidadeVidas); i++) {
            sb.append("X "); 
        }
        return sb.toString();
    }

    @Override
    public void dispose() {
        batch.dispose(); 
        hudStage.dispose();
        font.dispose();
    }
}