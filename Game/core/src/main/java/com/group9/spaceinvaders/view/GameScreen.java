package com.group9.spaceinvaders.view;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

import com.group9.spaceinvaders.model.Player;
import com.group9.spaceinvaders.controller.PlayerController;

import com.group9.spaceinvaders.model.PlayerBullet;
import com.group9.spaceinvaders.controller.PlayerBulletController;

public class GameScreen extends ScreenAdapter {
    // Nossas instâncias do MVC
    private Player playerOne;
    private PlayerController playerOneController;

    private PlayerBullet playerBullet;
    private PlayerBulletController playerBulletController;
    
    // Ferramenta da View para desenhar formas geométricas
    private ShapeRenderer playerRenderer;
    private ShapeRenderer playerBulletRenderer;

    public GameScreen() {
        // Inicializa a nave no centro inferior da tela (x=300, y=50)
        playerOne = new Player(300, 50, 50, 50);
        playerBullet = new PlayerBullet(-10, -10, 5, 15);

        playerOneController = new PlayerController(playerOne);
        playerBulletController = new PlayerBulletController(playerBullet);

        playerRenderer = new ShapeRenderer();
        playerBulletRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        // 1. Controller TRABALHA: Atualiza a matemática
        playerOneController.update(delta);
        playerBulletController.update(playerOne, delta);

        // 2. VIEW TRABALHA: Limpa a tela com fundo preto
        ScreenUtils.clear(0, 0, 0, 1);

        // 3. VIEW TRABALHA: Pinta a nave baseada nos dados do playerOne
        playerRenderer.begin(ShapeRenderer.ShapeType.Filled);
        playerRenderer.setColor(Color.GREEN);

        playerBulletRenderer.begin(ShapeRenderer.ShapeType.Filled);
        playerBulletRenderer.setColor(Color.WHITE);
        
        // Extrai as coordenadas X, Y, Largura e Altura direto do Rectangle do playerOne
        playerRenderer.rect(playerOne.bounds.x, playerOne.bounds.y, playerOne.bounds.width, playerOne.bounds.height);

        if(playerBullet.isValid){
            playerOne.canShoot = false;
            playerBulletRenderer.rect(playerBullet.bounds.x, playerBullet.bounds.y, playerBullet.bounds.width, playerBullet.bounds.height);
        } else {
            playerOne.canShoot = true;
        }
        
        playerRenderer.end();
        playerBulletRenderer.end();
    }

    @Override
    public void dispose() {
        playerRenderer.dispose(); // Previne vazamento de memória do i5!
        playerBulletRenderer.dispose(); // Previne vazamento de memória do i5!
    }
}