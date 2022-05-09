package main;

import gameObjects.*;
import org.newdawn.slick.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * main class for handling GameContainer, render and update instance
 * @author Ella
 * @version alpha 0.1
 */

public class Main extends BasicGame {
    public Random random;
    public Input input;

    //gameObjects
    public static Player player;
    private List<Bread> bread_list = new LinkedList<>();

    /**
     * 0 - menu
     * 1 - game
     * 2 - paused
     */
    private byte UIstate;

    public Main(String title) {
        super(title);
    }

    /**
     * initializes the game
     */
    @Override
    public void init(GameContainer gc) throws SlickException {
        info("Starting game");
        random = new Random();
        input = gc.getInput();
        UIstate = 1;
        player = new Player(input);
        bread_list.add(new Bread(600, (byte) 0));
        bread_list.add(new Bread(800, (byte) 1));
    }

    /**
     * updates the game every frame
     */
    @Override
    public void update(GameContainer gc, int arg) {
        //game
        pauseCheck();
        if(UIstate == 1) {
            player.update();
            for(Bread b : bread_list) {
                b.update();
                if(b.delete) bread_list.remove(b);
            }
        }
    }

    /**
     * renders the game every frame
     */
    @Override
    public void render(GameContainer gc, Graphics g) {
        //menu
        if(UIstate == 0) {

        }

        //game
        else {
            //player
            g.draw(player.getHitbox());
            player.getImage().drawCentered(player.getX(), player.getY());

            for(Bread b : bread_list) {
                g.draw(b.getHitbox());
                b.getImage().drawCentered(b.getX(), b.getY());
            }
        }

        //paused
        if(UIstate == 2) {
            g.drawString("Paused", 640, 20);
        }
    }

    /**
     * pauses or unpauses the game when Esc is pressed
     */
    private void pauseCheck() {
        if(input.isKeyPressed(Input.KEY_ESCAPE)) {
            if (UIstate == 1) {
                //unpause
                UIstate = 2;
            } else if(UIstate == 2) {
                //pause
                UIstate = 1;
            }
        }
    }

    /**
     * logs a line to the console using the Slick2D format
     * @param info the info to be printed out
     */
    public void info(String info) {
        System.out.println(new Date() + " INFO:" + info);
    }

    /**
     * displays an error message and terminates the program
     * @param info the error message
     */
    public static void error(String info) {
        System.out.println(new Date() + " ERROR:" + info);
        System.exit(1);
    }

    /**
     * starts the game engine
     */
    public static void main(String[] args) {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println(new Date() + " INFO:Stopping Game Engine")));
            AppGameContainer app = new AppGameContainer(new Main("Brojeggd Brohd und Brödchen"));
            app.setDisplayMode(1280,920, false);
            app.setTargetFrameRate(60);
            app.setVSync(true);
            app.setShowFPS(true);
            app.start();
        } catch(Exception e) {e.printStackTrace();}
    }
}