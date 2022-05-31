package gameObjects;

import main.GameObject;
import main.Main;

/**
 * the powerup item
 * @author Ella
 */

public class Powerup extends GameObject {
    public boolean delete;
    private static int spawn_y = -80;
    private final int floor_level = 800;
    private final float speedY = 5.0f;
    private int posY;

    public Powerup(int x) {
        super(null, x, spawn_y, 50);
    }

    /**
     * updates the object every frame
     */
    public void update() {
        move();
        moveHitbox();
        collide();
    }

    /**
     * moves the object down
     */
    private void move() {
        posY += speedY;
        if(posY > floor_level) {
            posY = floor_level;
        }
        setLoc(getX(), Math.round(posY));
    }

    /**
     * checks collision with player
     */
    private void collide() {
        //player
        if(getHitbox().intersects(Main.player.getHitbox()) && !Main.gameover) {
            delete = true;
        }
    }
}