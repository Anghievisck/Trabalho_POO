package com.group9.spaceinvaders.test;

import org.junit.Test;
import static org.junit.Assert.*;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.group9.spaceinvaders.model.Player;
import com.group9.spaceinvaders.model.Enemy;
import com.group9.spaceinvaders.model.Bullet;
import com.group9.spaceinvaders.model.Swarm;
import java.util.ArrayList;

public class GameLogicTest {

    // TEST 1: Verifies if the hitbox follows the entity when it moves
    @Test
    public void testEntityHitboxUpdatesOnMove() {
        // Arrange: Creates an enemy at an initial position (X=10, Y=10)
        Enemy alien = new Enemy(10f, 10f, 30f, 30f, null, 100, null, -200f);
        
        // Act: Since setX and setY use "+=", we only pass the amount we want to MOVE.
        // Current position 10 + Move 20 = New position 30
        alien.setX(20f); 
        
        // Current position 10 + Move 10 = New position 20
        alien.setY(10f); 
        
        // Assert: The hitbox must have updated to the new coordinates (X=30, Y=20)
        Rectangle hitbox = alien.getHitbox();
        assertEquals("The hitbox X position should be 30", 30f, hitbox.x, 0.01);
        assertEquals("The hitbox Y position should be 20", 20f, hitbox.y, 0.01);
    }

    // TEST 2: Verifies the collision logic between a bullet and the player
    @Test
    public void testPlayerCollisionWithEnemyBullet() {
        Player player = new Player(100f, 50f, 50, 50, null, null, 0, 0, 0);
        player.lives = 3; 
        
        Enemy shooter = new Enemy(100f, 400f, 30, 30, null, 100, null, -200f);
        Bullet enemyBullet = new Bullet(110f, 60f, 5, 15, null, -200f, 1, shooter); 

        assertTrue("Should detect collision because the hitboxes overlap", player.checkCollision(enemyBullet));
    }

    // TEST 3: Ensures that a dead enemy CANNOT suffer collision anymore (ghost)
    @Test
    public void testDeadEnemyIgnoresCollision() {
        Enemy deadAlien = new Enemy(200f, 200f, 30, 30, null, 0, null, 0f);
        Player player = new Player(200f, 50f, 50, 50, null, null, 0, 0, 0);
        Bullet playerBullet = new Bullet(205f, 205f, 5, 15, null, 400f, 100, player);

        assertFalse("Dead enemies should not collide with new bullets", deadAlien.checkCollision(playerBullet));
    }

    // TEST 4: Verifies if the Swarm initializes the alive count correctly
    @Test
    public void testSwarmInitializationCount() {
        // Creates dummy lists of the exact type required by the Swarm
        ArrayList<TextureRegion> dummySprites = new ArrayList<>();
        dummySprites.add(null); // For BASIC
        dummySprites.add(null); // For BASIC2
        dummySprites.add(null); // For BASIC3

        ArrayList<Float> dummySpeeds = new ArrayList<>();
        dummySpeeds.add(0f);
        dummySpeeds.add(0f);
        dummySpeeds.add(0f);
        
        // Passes the dummy lists into the correct parameters
        Swarm swarm = new Swarm(0, 0, 30, 30, 10, 1f, dummySprites, 100, dummySprites, dummySpeeds);
        
        int expectedAlive = swarm.rows * swarm.cols;
        assertEquals("The alive enemy count should be rows * columns", expectedAlive, swarm.aliveCount);
    }

    // TEST 5: Verifies the collision protection with an invalid (already destroyed) bullet
    @Test
    public void testCollisionWithInvalidBullet() {
        Player player = new Player(100f, 50f, 50, 50, null, null, 0, 0, 0);
        Enemy shooter = new Enemy(100f, 400f, 30, 30, null, 100, null, -200f);
        Bullet bullet = new Bullet(110f, 60f, 5, 15, null, -200f, 1, shooter); 
        
        bullet.isValid = false;

        assertFalse("Should not collide with bullets that are already marked as invalid", player.checkCollision(bullet));
    }
}