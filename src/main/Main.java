package main;

import gameObjects.*;
import org.newdawn.slick.*;
import java.util.Date;

/**
 * Main class for handling GameContainer, first render and update instance
 * @author Ella
 * @version alpha 0.1
 */

public class Main extends BasicGame {
    /**
     * 0 - menu
     * 1 - game
     * 2 - paused
     */
    private byte UIstate;

    private Player player;

    public Main(String title) {
        super(title);
    }

    /**
     * initializes the game
     */
    @Override
    public void init(GameContainer gc) {
        info("Starting game");
    }

    /**
     * updates the game every frame
     */
    @Override
    public void update(GameContainer gc, int arg) {

    }

    /**
     * renders the game every frame
     */
    @Override
    public void render(GameContainer gc, Graphics g) {
        if(UIstate == 0) {
        } else if(UIstate == 1) {
        } else {
        }

        //player
        g.draw(player.getHitbox());
    }


    /**
     * Logs a line to the console using the Slick2D format
     * @param info The info to be printed out
     */
    public void info(String info) {
        System.out.println(new Date() + " INFO:" + info);
    }

    public static void main(String[] args) {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println(new Date() + " INFO:Stopping Game Engine")));
            AppGameContainer app = new AppGameContainer(new Main("Brojeggd Brohd und Brödchen"));
            app.setDisplayMode(600, 400, false);
            app.setTargetFrameRate(60);
            app.setShowFPS(true);
            app.start();
        } catch(Exception e) {e.printStackTrace();}
    }
}