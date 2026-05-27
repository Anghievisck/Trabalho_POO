package com.group9.spaceinvaders.controller;

import com.badlogic.gdx.math.MathUtils;
import com.group9.spaceinvaders.model.EnemyBullet;
import com.group9.spaceinvaders.model.Swarm;

public class EnemyBulletController{
    private EnemyBullet bullet;
    public EnemyBulletController(EnemyBullet bullet) {
        this.bullet = bullet;
    }
    public void update(float delta) {
        if(bullet.isValid){
            bullet.Move(delta);
        }
    }
    public void EnemyShootUpdate(Swarm swarm){
        if(bullet.isValid) return; // Se a bala já estiver ativa, não faça nada.
        int cont = 0;
        int rand = MathUtils.random(0, swarm.aliveCount - 1);
        for (int r = 0; r < swarm.rows; r++) {
            for (int c = 0; c < swarm.cols; c++) {
                if (swarm.enemies[r][c].isAlive) {
                    cont++;
                    if(cont == rand){
                        bullet.isValid = true;
                        bullet.hitbox.x = swarm.enemies[r][c].hitbox.x + (swarm.enemies[r][c].hitbox.width / 2);
                        bullet.hitbox.y = swarm.enemies[r][c].hitbox.y;
                    }
                }
            }
        }
    }
}
