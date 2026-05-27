package com.group9.spaceinvaders.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Entity {
    // Usamos 'protected' para que as classes filhas (Player, Enemy) 
    // possam ler e modificar esses valores diretamente.
    protected float x;
    protected float y;
    protected float width;
    protected float height;

    protected TextureRegion sprite;

    public Rectangle hitbox;

    // O Construtor base
    public Entity(float x, float y, float width, float height, TextureRegion sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
        
        // A hitbox é inicializada automaticamente com os valores passados
        this.hitbox = new Rectangle(x, y, width, height);
    }

    // Toda vez que a entidade se mover, as classes filhas devem chamar 
    // este método para garantir que a hitbox acompanhe o sprite.
    protected void updateHitbox() {
        hitbox.setPosition(x, y);
    }

    // Métodos abstratos: Força todas as classes filhas a implementarem 
    // suas próprias lógicas de atualização e desenho.
    //     public abstract void update(float delta);
    //     public abstract void draw(SpriteBatch batch);

    // Getters básicos que servem para todas as entidades
    public Rectangle getHitbox() {
        return hitbox;
    }

    public void draw(SpriteBatch batch) {
        if (sprite != null) {
            batch.draw(sprite, x, y, width, height);
        }
    }

    public float getX() { return x; }
    public float getY() { return y; }
}