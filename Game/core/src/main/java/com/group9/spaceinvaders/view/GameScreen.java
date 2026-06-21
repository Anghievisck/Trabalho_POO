package com.group9.spaceinvaders.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.group9.spaceinvaders.model.Player;
import com.group9.spaceinvaders.controller.PlayerController;
import com.group9.spaceinvaders.model.Bullet;
import com.group9.spaceinvaders.model.Swarm;
import com.group9.spaceinvaders.controller.SwarmController;
import com.group9.spaceinvaders.model.Enemy;
import com.group9.spaceinvaders.model.AmmoDrop;

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
    
    private List<Bullet> activeBullets = new ArrayList<>();
    private List<AmmoDrop> activeDrops = new ArrayList<>();

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
  
    private Stage hudStage;
    private Label scoreLabelP1;
    private Label scoreLabelP2;
    private Label livesLabelP1;
    private Label livesLabelP2;
    private Label ammoLabelP1;
    private Label ammoLabelP2;
    private BitmapFont font;

    private int swarmIndex = 0;
    private float ammoSpawnTimer = 0f;

    // Estado e Palco do Menu de Pausa
    private boolean isPaused = false;
    private Stage pauseStage;

    public GameScreen(SpaceInvadersGame game, int difficulty, boolean twoPlayers) {
        this(game, difficulty, twoPlayers, 0, 0, 3, 5, 0, 3, 5, false);
    }

    public GameScreen(SpaceInvadersGame game, int difficulty, boolean twoPlayers, int initialLevel,
                      int p1Points, int p1Lives, int p1Ammo,
                      int p2Points, int p2Lives, int p2Ammo, boolean loadFromSave) {
        this.game = game;
        this.difficulty = difficulty;
        this.twoPlayers = twoPlayers;
        this.swarmIndex = initialLevel; 

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);

        TextureAtlas atlas = game.assets.get("sprites/gameplay.atlas", TextureAtlas.class);
        TextureRegion playerSprite = atlas.findRegion("player_placeholder");
        TextureRegion bulletSprite = atlas.findRegion("bullet_placeholder");

        playerOne = new Player(200, 50, 50, 50, playerSprite, bulletSprite, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.SPACE);
        playerOne.points = p1Points;
        playerOne.lives = p1Lives;
        playerOne.ammo = p1Ammo;
        playerOneController = new PlayerController(game, playerOne);

        if(this.twoPlayers){
            playerTwo = new Player(500, 50, 50, 50, playerSprite, bulletSprite, Input.Keys.A, Input.Keys.D, Input.Keys.W);
            playerTwo.points = p2Points;
            playerTwo.lives = p2Lives;
            playerTwo.ammo = p2Ammo;
            playerTwoController = new PlayerController(game, playerTwo);
        }

        List<TextureRegion> enemySprites = new ArrayList<>();
        enemySprites.add(atlas.findRegion("alien_crab1"));
        enemySprites.add(atlas.findRegion("alien_crab2"));

        List<TextureRegion> enemyBulletSprites = new ArrayList<>();
        enemyBulletSprites.add(atlas.findRegion("bullet_placeholder"));

        List<Float> enemyBulletSpeeds = new ArrayList<>();
        enemyBulletSpeeds.add(-250f);

        for(int i = 0; i < 3; i++){
            swarms.add(new Swarm((float)50, (float)400, (float)30, (float)30, (float)15, (i + 1) * 1.5f * difficulty, enemySprites, 100, enemyBulletSprites, enemyBulletSpeeds));
            swarmControllers.add(new SwarmController(swarms.get(i), 800f, difficulty)); 
        }

        // SE CARREGADO DE UM SAVE: Restaura as posições e vidas customizadas dos inimigos em tela
        if (loadFromSave) {
            Preferences prefs = Gdx.app.getPreferences("SpaceInvadersSaveFile");
            Swarm currentSwarm = swarms.get(swarmIndex);
            currentSwarm.aliveCount = prefs.getInteger("swarmAliveCount", currentSwarm.aliveCount);
            currentSwarm.movingRight = prefs.getBoolean("movingRight", currentSwarm.movingRight);
            
            for (int r = 0; r < currentSwarm.rows; r++) {
                for (int c = 0; c < currentSwarm.cols; c++) {
                    Enemy enemy = currentSwarm.enemies[r][c];
                    float savedX = prefs.getFloat("enemy_" + r + "_" + c + "_x", enemy.getX());
                    float savedY = prefs.getFloat("enemy_" + r + "_" + c + "_y", enemy.getY());
                    
                    // Como setX/setY herdam comportamento incremental (+=), aplicamos a diferença vetorial
                    enemy.setX(savedX - enemy.getX());
                    enemy.setY(savedY - enemy.getY());
                    enemy.health = prefs.getInteger("enemy_" + r + "_" + c + "_health", enemy.health);
                }
            }
        }
        
        // Inicialização do HUD de jogo regular
        hudStage = new Stage(new FitViewport(800, 600));
        font = new BitmapFont(); 
        font.getData().setScale(1.2f); 
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        scoreLabelP1 = new Label("P1: 0", labelStyle);
        scoreLabelP1.setPosition(20, 560);
        hudStage.addActor(scoreLabelP1);
        
        livesLabelP1 = new Label("Vidas: XXX", labelStyle);
        livesLabelP1.setPosition(120, 560);
        hudStage.addActor(livesLabelP1);

        ammoLabelP1 = new Label("Mun.: 5", labelStyle);
        ammoLabelP1.setPosition(260, 560);
        hudStage.addActor(ammoLabelP1);

        if(this.twoPlayers){
            scoreLabelP2 = new Label("P2: 0", labelStyle);
            scoreLabelP2.setPosition(500, 560);
            hudStage.addActor(scoreLabelP2);
            
            livesLabelP2 = new Label("Vidas: XXX", labelStyle);
            livesLabelP2.setPosition(600, 560);
            hudStage.addActor(livesLabelP2);

            ammoLabelP2 = new Label("Mun.: 5", labelStyle);
            ammoLabelP2.setPosition(710, 560);
            hudStage.addActor(ammoLabelP2);
        }

        // CONSTRUÇÃO DA INTERFACE DE PAUSA (DISPARADA POR 'ESC')
        pauseStage = new Stage(new FitViewport(800, 600));
        Skin skin = game.assets.get("ui/spaceinvaders.json", Skin.class);
        Table pauseTable = new Table();
        pauseTable.setFillParent(true);

        TextButton btnRetomar = new TextButton("Retomar Jogo", skin);
        TextButton btnSalvar = new TextButton("Salvar Progresso", skin);
        TextButton btnMenu = new TextButton("Sair para o Menu", skin);

        btnRetomar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isPaused = false;
                Gdx.input.setInputProcessor(null); // Libera o mouse do palco de pausa
            }
        });

        btnSalvar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveGame();
            }
        });

        btnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        pauseTable.add(btnRetomar).width(280).height(45).padBottom(15).row();
        pauseTable.add(btnSalvar).width(280).height(45).padBottom(15).row();
        pauseTable.add(btnMenu).width(280).height(45);
        pauseStage.addActor(pauseTable);
    }

    private void saveGame() {
        Preferences prefs = Gdx.app.getPreferences("SpaceInvadersSaveFile");
        prefs.putBoolean("hasSave", true);
        prefs.putInteger("difficulty", this.difficulty);
        prefs.putBoolean("twoPlayers", this.twoPlayers);
        prefs.putInteger("swarmIndex", this.swarmIndex);
        
        prefs.putInteger("p1Points", playerOne.points);
        prefs.putInteger("p1Lives", playerOne.lives);
        prefs.putInteger("p1Ammo", playerOne.ammo);
        
        if (this.twoPlayers) {
            prefs.putInteger("p2Points", playerTwo.points);
            prefs.putInteger("p2Lives", playerTwo.lives);
            prefs.putInteger("p2Ammo", playerTwo.ammo);
        }

        // SALVAMENTO COMPLETO DOS INIMIGOS
        Swarm currentSwarm = swarms.get(swarmIndex);
        prefs.putInteger("swarmAliveCount", currentSwarm.aliveCount);
        prefs.putBoolean("movingRight", currentSwarm.movingRight);
        
        for (int r = 0; r < currentSwarm.rows; r++) {
            for (int c = 0; c < currentSwarm.cols; c++) {
                Enemy enemy = currentSwarm.enemies[r][c];
                prefs.putFloat("enemy_" + r + "_" + c + "_x", enemy.getX());
                prefs.putFloat("enemy_" + r + "_" + c + "_y", enemy.getY());
                prefs.putInteger("enemy_" + r + "_" + c + "_health", enemy.health);
            }
        }
        
        prefs.flush(); 
        System.out.println("Jogo e posição dos inimigos salvos com sucesso via Menu de Pausa!");
    }

    @Override
    public void render(float delta) {
        // Captura a tecla ESC para alternar o estado de Pausa do jogo
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused;
            if (isPaused) {
                Gdx.input.setInputProcessor(pauseStage); // Transfere cliques de mouse para o menu de pausa
            } else {
                Gdx.input.setInputProcessor(null);
            }
        }

        // --- LÓGICA ATUALIZADA APENAS SE NÃO ESTIVER EM PAUSA ---
        if (!isPaused) {
            ammoSpawnTimer += delta;
            if(ammoSpawnTimer >= 4.0f) {
                ammoSpawnTimer = 0f;
                float randomX = com.badlogic.gdx.math.MathUtils.random(20f, 780f); 
                activeDrops.add(new AmmoDrop(randomX, 50f, 15, 30, playerOne.bulletSprite));
            }

            playerOneController.update(delta, activeBullets); 
            scoreLabelP1.setText("P1: " + playerOne.points);
            livesLabelP1.setText("Vidas: " + formatarVidas(playerOne.lives));
            ammoLabelP1.setText("Mun: " + playerOne.ammo);

            if(this.twoPlayers){
                playerTwoController.update(delta, activeBullets); 
                scoreLabelP2.setText("P2: " + playerTwo.points);
                livesLabelP2.setText("Vidas: " + formatarVidas(playerTwo.lives));
                ammoLabelP2.setText("Mun: " + playerTwo.ammo);
            }

            swarmControllers.get(swarmIndex).update(delta, activeBullets, activeDrops); 

            // CONDIÇÃO: INIMIGOS CHEGAM ATÉ VOCÊ OU ATRAVESSAM A LINHA DO JOGADOR -> GAME OVER
            Swarm currentSwarm = swarms.get(swarmIndex);
            for (int r = 0; r < currentSwarm.rows; r++) {
                for (int c = 0; c < currentSwarm.cols; c++) {
                    Enemy enemy = currentSwarm.enemies[r][c];
                    if (enemy != null && enemy.health > 0) {
                        // Verifica colisão direta com as hitboxes das naves ou se desceram além de Y=50 (eixo do player)
                        if ((playerOne.lives > 0 && enemy.getHitbox().overlaps(playerOne.getHitbox())) || enemy.getY() <= 50f) {
                            game.setScreen(new GameOverScreen(game, false, twoPlayers, this.difficulty));
                            return;
                        }
                        if (this.twoPlayers && playerTwo.lives > 0 && enemy.getHitbox().overlaps(playerTwo.getHitbox())) {
                            game.setScreen(new GameOverScreen(game, false, twoPlayers, this.difficulty));
                            return;
                        }
                    }
                }
            }

            // Verificação de Derrota tradicional (Sem vidas)
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

            // Transição de Nível / Vitória da Horda
            if(swarms.get(swarmIndex).aliveCount <= 0){
                playerOne.points += 100; 
                playerOne.lives += 1;
                if(twoPlayers){
                    playerTwo.points += 100;
                    playerTwo.lives += 1;
                }
                
                if (swarmIndex < 2) {
                    game.setScreen(new LevelCompleteScreen(game, difficulty, twoPlayers, swarmIndex, 
                        playerOne.points, playerOne.lives, playerOne.ammo,
                        this.twoPlayers ? playerTwo.points : 0, 
                        this.twoPlayers ? playerTwo.lives : 0, 
                        this.twoPlayers ? playerTwo.ammo : 0));
                    return;
                } else {
                    game.setScreen(new GameOverScreen(game, true, twoPlayers, this.difficulty));
                    return;
                }
            }
        }

        // --- DESENHO / GRÁFICOS (Continua renderizando mesmo pausado) ---
        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        
        if (playerOne.lives > 0) playerOne.draw(batch);
        if (this.twoPlayers && playerTwo.lives > 0) playerTwo.draw(batch);

        Swarm currentSwarm = swarms.get(swarmIndex);
        for (int r = 0; r < currentSwarm.rows; r++) {
            for (int c = 0; c < currentSwarm.cols; c++) {
                Enemy enemy = currentSwarm.enemies[r][c];
                if (enemy != null && enemy.health > 0) {
                    enemy.draw(batch);
                }
            }
        }

        // Drops e Balas atualizam a física apenas se o jogo não estiver pausado
        Iterator<AmmoDrop> dropIter = activeDrops.iterator();
        while(dropIter.hasNext()){
            AmmoDrop drop = dropIter.next();
            if (!isPaused) drop.update(delta);
            drop.draw(batch);

            if(!isPaused && playerOne.lives > 0 && playerOne.getHitbox().overlaps(drop.getHitbox())){
                playerOne.ammo += 3;
                drop.isCollected = true;
            }

            if(!isPaused && this.twoPlayers && playerTwo.lives > 0 && playerTwo.getHitbox().overlaps(drop.getHitbox())){
                playerTwo.ammo += 3;
                drop.isCollected = true;
            }

            if(drop.isCollected){
                dropIter.remove();
            }
        }

        Iterator<Bullet> iter = activeBullets.iterator();
        while(iter.hasNext()){
            Bullet b = iter.next();
            if (!isPaused) b.update(delta); 
            b.draw(batch);   
            
            if(!b.isValid){
                iter.remove();
            }
        }

        batch.end();

        // Desenha a interface do HUD principal
        hudStage.act(delta);
        hudStage.draw();

        // Se estiver em modo de Pausa, desenha a janela Menu por cima de tudo
        if (isPaused) {
            pauseStage.act(delta);
            pauseStage.draw();
        }
    }

    private String formatarVidas(int quantidadeVidas) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.max(0, quantidadeVidas); i++) {
            sb.append("X "); 
        }
        return sb.toString();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudStage.getViewport().update(width, height, true);
        pauseStage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose(); 
        hudStage.dispose();
        pauseStage.dispose(); // Descarta o palco de pausa de forma limpa
        font.dispose();
    }
}