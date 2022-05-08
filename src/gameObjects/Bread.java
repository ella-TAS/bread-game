package gameObjects;

import main.GameObject;
import main.Main;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

/**
 * the bread collectible
 * @author Ella
 */

public class Bread extends GameObject {
    private static final int spawn_y = 500;
    private byte type;

    public Bread(int x, byte type) throws SlickException {
        super(new Image("assets/textures/bread" + type + ".png", false, 2).getScaledCopy(5), x, spawn_y, 0, 0);
        this.type = type;
        switch(type) {
            case 0: resizeHitbox(100, 100); break;
            case 1: resizeHitbox(80, 150); break;
        }
        moveHitbox();
    }
}
