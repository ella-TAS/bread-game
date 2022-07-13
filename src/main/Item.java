package main;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;

import java.io.IOException;

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
    void update(int delta) throws SlickException, IOException;
    void render(Graphics g);
    void delete();
    void action();
    Shape getHitbox();
    Image getImage();
    int getX();
    int getY();
}