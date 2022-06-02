package main;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;

/**
 * Interface to manage all items within one list
 * @author Ella
 */

public interface Item {
    /**
     * 0 - bread
     * 1 - bomb
     * 2 - powerup
     */
    byte getType();
    boolean isDelete();
    void update() throws SlickException;
    void delete();
    Shape getHitbox();
    Image getImage();
    int getX();
    int getY();
}