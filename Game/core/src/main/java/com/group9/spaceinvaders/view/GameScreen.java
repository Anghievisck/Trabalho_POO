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

    private PlayerBullet bullet;
    private PlayerBulletController bulletController;
    
    // Ferramenta da View para desenhar formas geométricas
    private ShapeRenderer shapeRenderer;

    public GameScreen() {
        // Inicializa a nave no centro inferior da tela (x=300, y=50)
        playerOne = new Player(300, 50, 50, 50);
        playerOneController = new PlayerController(playerOne);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        // 1. Controller TRABALHA: Atualiza a matemática
        playerOneController.update(delta);

        // 2. VIEW TRABALHA: Limpa a tela com fundo preto
        ScreenUtils.clear(0, 0, 0, 1);

        // 3. VIEW TRABALHA: Pinta a nave baseada nos dados do playerOne
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        
        // Extrai as coordenadas X, Y, Largura e Altura direto do Rectangle do playerOne
        shapeRenderer.rect(playerOne.bounds.x, playerOne.bounds.y, playerOne.bounds.width, playerOne.bounds.height);
        
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose(); // Previne vazamento de memória do i5!
    }
}