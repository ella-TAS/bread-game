package gameObjects;

import main.GameObject;
import main.Item;
import main.Main;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;

/**
 * the bomb item - 1
 * @author Ella
 */

public class Bomb extends GameObject implements Item {
    public final byte item_type = 1;
    public boolean delete;

    private static final int spawn_y = -80;
    private final int floor_level = 800;
    private int timer;
    private byte sprite;
    private boolean ignited;
    private final float gravity = 0.2f;
    private final float terminal_velocity = 6f;
    private float posY,speedY;

    public Bomb(int x) throws SlickException {
        super(new Image("assets/textures/items/bomb_0.png", false, 2).getScaledCopy(4), x, spawn_y, 50, 50);
        speedY = 2f;
        sprite = 0;
        timer = 250;
        ignited = false;
        delete = false;
    }

    /**
     * updates the object every frame
     */
    public void update() throws SlickException {
        timer();
        if(!ignited) move();
        moveHitbox();
        collide();
    }

    /**
     * renders the object every frame
     */
    public void render(Graphics g) {
        g.setColor(Color.white);
        g.draw(getHitbox());
        getImage().drawCentered(getX(), getY());
    }

    /**
     * counts down and controls the bomb explosion
     */
    private void timer() throws SlickException {
        if(ignited) {
            timer--;
            if(timer < 0) {
                setHitbox(new Circle(getX(), getY(), 120));
                renewSize();
            }
            spriteMachine();
        }
    }

    /**
     * changes the sprite dependent on the timer
     */
    private void spriteMachine() throws SlickException {
        if(timer > 200) sprite = 1;
        else if(timer > 150) sprite = 2;
        else if(timer > 100) sprite = 3;
        else if(timer > 50) sprite = 4;
        else if(timer > 0) sprite = 5;
        else if(timer > -5) sprite = -1;
        else if(timer > -10) sprite = -2;
        else if(timer > -15) sprite = -3;
        else if(timer > -20) sprite = -4;
        else delete = true;
        if(sprite > 0) setImage(new Image("assets/textures/items/bomb_" + sprite + ".png", false, 2).getScaledCopy(4));
        else setImage(new Image("assets/textures/items/bomb_" + sprite + ".png", false, 2).getScaledCopy(10));
    }

    /**
     * moves the object down
     */
    private void move() {
        if(speedY < terminal_velocity) speedY += gravity;
        else speedY = terminal_velocity;
        posY += speedY;
        if(posY > floor_level) {
            posY = floor_level;
            ignited = true;
        }
        setLoc(getX(), Math.round(posY));
    }

    /**
     * checks collision with the player, breads and powerups
     */
    private void collide() {
        //player
        if(getHitbox().intersects(Main.player.getHitbox()) && !Main.gameover) {
            Main.gameover();
            timer = 0;
            ignited = true;
        }

        //bread
        if(timer < 0) {
            for (Item e : Main.items) {
                if(e.getType() == 0 || e.getType() == 2) {
                    if (getHitbox().intersects(e.getHitbox())) {
                        e.delete();
                    }
                }
            }
        }
    }

    public void delete() {delete = true;}
    public boolean isDelete() {return delete;}
    public byte getType() {return item_type;}
}