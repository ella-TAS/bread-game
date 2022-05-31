package gameObjects;

import main.GameObject;
import main.Main;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;

/**
 * the bomb item
 * @author Ella
 */

public class Bomb extends GameObject {
    public boolean delete;
    private static final int spawn_y = -80;
    private final int floor_level = 800;
    private final float speedY = 5.0f;
    private int posY, timer;
    private byte sprite;
    private boolean ignited;

    public Bomb(int x) throws SlickException {
        super(new Image("assets/textures/bomb_0.png", false, 2).getScaledCopy(4), x, spawn_y, 50, 50);
        sprite = 0;
        timer = 2500000;
        ignited = false;
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

    private void timer() throws SlickException {
        if(ignited) {
            timer--;
            if(timer < 0) {
                //sound
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
        if(sprite > 0) setImage(new Image("assets/textures/bomb_" + sprite + ".png", false, 2).getScaledCopy(4));
        else setImage(new Image("assets/textures/bomb_" + sprite + ".png", false, 2).getScaledCopy(10));
    }

    /**
     * moves the object down
     */
    private void move() {
        posY += speedY;
        if(posY > floor_level) {
            posY = floor_level;
            ignited = true;
        }
        setLoc(getX(), Math.round(posY));
    }

    /**
     * checks collision with the player and breads
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
            for (Bread b : Main.breadList) {
                if (getHitbox().intersects(b.getHitbox())) {
                    delete = true;
                }
            }
        }
    }
}