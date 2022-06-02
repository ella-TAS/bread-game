package gameObjects;

import main.GameObject;
import main.Item;
import main.Main;
import org.newdawn.slick.Graphics;

/**
 * the powerup item - 2
 * @author Ella
 */

public class PowerUp extends GameObject implements Item {
    public boolean delete;
    public final byte item_type = 2;

    private static int spawn_y = -80;
    private final int floor_level = 800;
    private final float speedY = 5.0f;
    private int posY;

    public PowerUp(int x) {
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
     * renders the object every frame
     */
    public void render(Graphics g) {
        //g.drawImage();
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

    public void delete() {delete = true;}
    public boolean isDelete() {return delete;}
    public byte getType() {return item_type;}
}