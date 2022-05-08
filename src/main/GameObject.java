package main;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * General class for all game objects, defines hitbox types
 * @author Ella
 */

public class GameObject {
    protected int x, y, width, height, offX, offY;
    protected byte poly;
    private Shape hitbox;
    protected boolean center;
    protected Image img;

    /**
     * for objects with a rectangle hitbox
     * @param img sprite
     * @param x x position
     * @param y y position
     * @param width width
     * @param height height
     */
    public GameObject(Image img, int x, int y, int width, int height) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        hitbox = new Rectangle(x, y, width, height);
        poly = 0;
        offX = offY = 0;
        center = false;
    }

    /**
     * for objects with a circular hitbox
     * @param img sprite
     * @param x x position
     * @param y y position
     * @param radius radius
     */
    public GameObject(Image img, int x, int y, int radius) {
        this.img = img;
        this.x = x;
        this.y = y;
        width = radius;
        height = radius;
        hitbox = new Circle(x, y, radius);
        poly = 1;
        offX = offY = 0;
        center = false;
    }

    /**
     * for objects with a polynomial hitbox
     * @param img sprite
     * @param x x position
     * @param y y position
     * @param offX offset in x direction
     * @param offY offset in y direction
     * @param points array of points of the hitbox outline
     */
    public GameObject(Image img, int x, int y, int offX, int offY, float[] points) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.offX = offX;
        this.offY = offY;
        width = img.getWidth();
        height = img.getHeight();
        Polygon p = new Polygon(points);
        p.setClosed(true);
        hitbox = p;
        poly = 2;
        center = false;
    }

    /**
     * update the hitbox position based on the object position
     */
    protected void moveHitbox() {
        if(poly != 1) {hitbox.setLocation(x - width/2 + offX, y - height/2 + offY);}
        else if(!center) {hitbox.setLocation(x-width, y-height);}
        else {hitbox.setLocation(x, y);}
    }

    /**
     * update width and height to match the hitbox size
     */
    protected void renewSize() {
        width = (int) getHitbox().getWidth();
        height = (int) getHitbox().getHeight();
    }

    /**
     * resize the hitbox
     * @param width width of the new hitbox
     * @param height height of the new hitbox
     */
    protected void resizeHitbox(int width, int height) {
        hitbox = new Rectangle(x, y, width, height);
        this.width = width;
        this.height = height;
    }

    /**
     * resize the hitbox
     * @param radius radius of the new hitbox
     */
    protected void resizeHitbox(int radius) {
        hitbox = new Circle(x, y, radius);
        width = height = radius;
    }

    public Image getImage() {return img;}

    public Shape getHitbox() {return hitbox;}

    public int getX() {return x;}

    public int getY() {return y;}

    protected void setImage(Image img) {this.img = img;}

    protected void setHitbox(Shape s) {hitbox = s;}

    protected void setLoc(int x, int y) {
        this.x = x;
        this.y = y;
    }
}