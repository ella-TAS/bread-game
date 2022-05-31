package gameObjects;

import main.GameObject;
import main.Main;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * the bread item
 * @author Ella
 */

public class Bread extends GameObject {
    public boolean delete;
    private static final int spawn_y = -80;
    private final int floor_level = 800;
    private final float speedY = 5.0f;
    private final byte type;
    private int posY;

    public Bread(int x, byte type) throws SlickException {
        super(new Image("assets/textures/bread_" + type + ".png", false, 2).getScaledCopy(4), x, spawn_y, 0, 0);
        this.type = type;
        delete = false;
        posY = spawn_y;
        switch (type) {
            case 0: resizeHitbox(100, 100); break;
            case 1: resizeHitbox(80, 150); break;
            case 2: resizeHitbox(120, 50); break;
        }
        moveHitbox();
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
        if(posY > floor_level) posY = floor_level;
        setLoc(getX(), Math.round(posY));
    }

    /**
     * checks collision with player
     */
    private void collide() {
        //player
        if(getHitbox().intersects(Main.player.getHitbox()) && !Main.gameover) {
            switch (type) {
                case 0: Main.counter.add(3); break;
                case 1: Main.counter.add(1); break;
                case 2: Main.counter.add(7); break;
            }
            delete = true;
        }
    }
}