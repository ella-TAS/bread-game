package gameObjects;

import main.GameObject;
import main.Item;
import main.Main;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * the powerup item - 2
 * @author Ella
 */

public class PowerUp extends GameObject implements Item {
    public final byte item_type = 2;
    public boolean delete;

    private static int spawn_y = -80;
    private final int floor_level = 800;
    private final float gravity = 0.2f;
    private final float terminal_velocity = 6f;
    private float posY,speedY;

    public PowerUp(int x) {
        super(null, x, spawn_y, 25);
        posY = spawn_y;
        speedY = 0.5f;
        delete = false;
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
        g.setColor(Color.orange);
        g.draw(getHitbox());
        g.fillOval(getX()-10, getY()-10, 20, 20);
    }

    /**
     * moves the object down
     */
    private void move() {
        if(speedY < terminal_velocity) speedY += gravity;
        else speedY = terminal_velocity;
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
            delete = true;
        }
    }

    public void delete() {delete = true;}
    public boolean isDelete() {return delete;}
    public byte getType() {return item_type;}
}