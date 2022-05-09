package gameObjects;

import main.GameObject;
import main.Main;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * the bread collectible
 * @author Ella
 */

public class Bread extends GameObject {
    public boolean delete;
    private static final int spawn_y = 500;
    private final float speedY = 3.0f;
    private final byte type;
    private int posY;

    public Bread(int x, byte type) throws SlickException {
        super(new Image("assets/textures/bread" + type + ".png", false, 2).getScaledCopy(5), x, spawn_y, 0, 0);
        this.type = type;
        delete = false;
        switch(type) {
            case 0: resizeHitbox(100, 100); break;
            case 1: resizeHitbox(80, 150); break;
        }
        moveHitbox();
    }

    /**
     * updates the object every frame
     */
    public void update() {
        move();
        collide();
        moveHitbox();
    }

    /**
     * moves the object down
     */
    private void move() {
        posY += speedY;
        setLoc(getX(), Math.round(posY));
    }

    /**
     * checks collision with the player
     */
    private void collide() {
        if(getHitbox().intersects(Main.player.getHitbox())) {
            delete = true;
        }
    }
}