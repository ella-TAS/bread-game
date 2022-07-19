package gameObjects;

import main.GameObject;
import main.Item;
import main.Main;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

import java.io.IOException;

/**
 * the bomb item - 1
 * @author Ella
 */

public class Bomb extends GameObject implements Item {
    public final byte item_type = 1;
    public boolean delete;

    private static final int spawn_y = -80;
    private final int floor_level = 800;
    public int timer;
    private byte sprite;
    private boolean ignited;
    private final float gravity = 0.2f;
    private final float terminal_velocity = 6f;
    private float posY,speedY;
    private final ParticleSystem pSystem;
    private final ConfigurableEmitter pEmitter;

    public Bomb(int x) throws SlickException, IOException {
        super(new Image("assets/textures/items/bomb_0.png", false, 2).getScaledCopy(4), x, spawn_y, 25);
        speedY = 2f;
        sprite = 0;
        timer = 250;
        posY = spawn_y;
        ignited = false;
        delete = false;
        pSystem = new ParticleSystem(new Image("assets/textures/particles/smoke.png", false, 2), 1000);
        pEmitter = ParticleIO.loadEmitter("assets/xmls/smoke.xml");
        pEmitter.setPosition(x, 765);
    }

    /**
     * updates the object every frame
     */
    public void update(int delta) throws SlickException, IOException {
        timer();
        if(!ignited) move();
        moveHitbox();
        collide();
        pSystem.update(delta);
    }

    /**
     * renders the object every frame
     */
    public void render(Graphics g) {
        //g.draw(getHitbox());
        getImage().drawCentered(getX(), getY());
        pSystem.render();
    }

    /**
     * counts down and controls the bomb explosion
     * @throws SlickException thrown if a problem with the image occurs
     */
    private void timer() throws SlickException {
        if(ignited) {
            timer--;
            if(timer < 0) {
                resizeHitbox(120);
            }
            spriteMachine();
        }
    }

    /**
     * changes the sprite dependent on the timer
     * @throws SlickException thrown if a problem with the image occurs
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
        if(sprite > 0){
            setImage(new Image("assets/textures/items/bomb_" + sprite + ".png", false, 2).getScaledCopy(4));
            pEmitter.setPosition(x+timer/8f, 765, false);
        }
        else {
            setImage(new Image("assets/textures/items/bomb_" + sprite + ".png", false, 2).getScaledCopy(10));
        }
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
            pSystem.addEmitter(pEmitter);
        }
        setLoc(getX(), Math.round(posY));
    }

    /**
     * explodes the bomb instantly
     */
    public void action() {
        timer = -2;
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
                    if(getHitbox().intersects(e.getHitbox())) {
                        e.delete();
                    }
                }
            }
        }

        //bomb
        if(timer == -1) {
            for (Item e : Main.items) {
                if(e.getType() == 1 && e != this) {
                    if(getHitbox().intersects(e.getHitbox())) e.action();
                }
            }
        }
    }

    public void delete() {delete = true;}
    public boolean isDelete() {return delete;}
    public byte getType() {return item_type;}
}