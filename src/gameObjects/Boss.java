package gameObjects;

import main.GameObject;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

import java.io.IOException;

/**
 * the furnace boss enemy
 * @author Ella
 */

public class Boss extends GameObject {
    private boolean approach, facing;
    private float posX, posY;
    private final ParticleSystem pSystem;
    private final ConfigurableEmitter pEmitter;

    public Boss() throws SlickException, IOException {
        super(new Image("assets/textures/boss/boss_0.png", false, 2).getScaledCopy(5), 640, -40, 100, 100);
        approach = true;
        posX = 640;
        posY = -40;
        pSystem = new ParticleSystem(new Image("assets/textures/particles/fire.png", false, 1), 1000);
        pEmitter = ParticleIO.loadEmitter("assets/xmls/fire.xml");
        pEmitter.setPosition(500, 500);
        pSystem.addEmitter(pEmitter);
    }

    /**
     * updates the object every frame
     * @param delta time since the last frame
     */
    public void update(int delta) {
        move();
        setLoc(Math.round(posX), Math.round(posY));
        moveHitbox();
        pEmitter.setPosition(posX, posY+20, false);
        pSystem.update(delta);
    }

    /**
     * renders the object every frame
     * @param g the graphics to render to
     */
    public void render(Graphics g) {
        pSystem.render();
        //g.draw(getHitbox());
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