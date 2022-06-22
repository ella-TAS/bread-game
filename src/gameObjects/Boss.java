package gameObjects;

import main.GameObject;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * the furnace boss enemy
 * @author Ella
 */

public class Boss extends GameObject {
    private boolean approach, facing;
    private float posX, posY;

    public Boss() throws SlickException {
        super(new Image("assets/textures/boss/boss_0.png", false, 2).getScaledCopy(5), 640, -40, 100, 100);
        approach = true;
        posX = 640;
        posY = -40;
    }

    /**
     * updates the object every frame
     */
    public void update() {
        move();
        setLoc(Math.round(posX), Math.round(posY));
        moveHitbox();
    }

    /**
     * renders the object every frame
     */
    public void render(Graphics g) {
        g.draw(getHitbox());
        getImage().drawCentered(getX(), getY());
    }

    /**
     * controls the movement of the boss
     */
    private void move() {
        if(approach){
            posY += 3;
            if(posY > 150) approach = false;
        } else {
            if(facing) posX += 3;
            else posX -= 3;
            if(posX < 540 || posX > 740) facing = !facing;
        }
    }
}