package gameObjects;

import main.GameObject;
import main.Item;
import main.Main;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * the bread item - 0
 * @author Ella
 */

public class Bread extends GameObject implements Item {
    public final byte item_type = 0;
    public boolean delete;

    private static final int spawn_y = -80;
    private final int floor_level = 800;
    private final byte type;
    private final float gravity = 0.08f;
    private final float terminal_velocity = 6f;
    private float posY,speedY;

    public Bread(int x, byte type) throws SlickException {
        super(new Image("assets/textures/items/bread_" + type + ".png", false, 2).getScaledCopy(4), x, spawn_y, 0, 0);
        this.type = type;
        delete = false;
        posY = spawn_y;
        speedY = 0.5f;
        switch (type) {
            case 0: resizeHitbox(50, 50); break;
            case 1:
                resizeHitbox(60, 20);
                offY = 20;
                break;
            case 2:
                resizeHitbox(60, 30);
                offY = 16;
            break;
        }
        moveHitbox();
    }

    /**
     * updates the object every frame
     */
    public void update(int delta) {
        move();
        moveHitbox();
        collide();
    }

    /**
     * renders the object every frame
     */
    public void render(Graphics g) {
        getImage().drawCentered(getX(), getY());
        //g.draw(getHitbox());
    }

    /**
     * moves the object down
     */
    private void move() {
        if(speedY < terminal_velocity) speedY += gravity;
        else speedY = terminal_velocity;
        posY += speedY;
        if(posY > floor_level){
            delete = true;
            switch (type) {
                case 0: Main.addScore(-7); break;
                case 1: Main.addScore(-3); break;
                case 2: Main.addScore(-10); break;
            }
        }
        setLoc(getX(), Math.round(posY));
    }

    /**
     * checks collision with player
     */
    private void collide() {
        //player
        if(getHitbox().intersects(Main.player.getHitbox()) && !Main.gameover) {
            switch (type) {
                case 0: Main.addScore(3); break;
                case 1: Main.addScore(1); break;
                case 2: Main.addScore(7); break;
            }
            Main.sound_eat.play(1 - Main.random.nextFloat()/3 , Main.sfxVolume/10f);
            delete = true;
        }
    }

    public void delete() {delete = true;}
    public boolean isDelete() {return delete;}
    public byte getType() {return item_type;}
    public void action() {}
}