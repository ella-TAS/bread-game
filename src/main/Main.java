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
 *
 * @version alpha 0.1
 */

public class Main extends BasicGame {
    //constants
    private final float bread_spawn_rate = .1f;

    //util
    public Random random;
    public Input input;
    public Image background;

    //gameObjects
    public static Player player;
    public static Counter counter;
    public static List<Item> items = new LinkedList<>();

    //properties
    public static boolean gameover;

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
        input = gc.getInput();
        random = new Random();
        counter = new Counter();
        player = new Player(input);
        gameover = false;
        UIstate = 1;
        background = new Image("assets/textures/bg.png", false, 2).getScaledCopy(5);

        items.add(new Bomb(300));
    }

    /**
     * updates the game every frame
     */
    @Override
    public void update(GameContainer gc, int arg) throws SlickException {
        //game
        pauseCheck();
        if(UIstate == 1) {
            //player
            player.update();
            if(gameover && player.posY > 3000) UIstate = 0;
            spawnItems();

            //gameObjects
            for(Item e : items) {
                if(!e.isDelete()) e.update();
            }
            for(Item e : items) {
                if(e.isDelete()) {
                    items.remove(e);
                    break;
                }
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
            g.drawString("Menu", 300, 300);
        }

        //game
        else {
            //background
            g.drawImage(background, 0, 0);

            //score
            g.drawString("Weight: " + counter.getScore(), 10, 40);

            //player
            g.draw(player.getHitbox());
            if(gameover) player.getImage().getScaledCopy(-1).drawCentered(player.getX(), player.getY());
            else player.getImage().drawCentered(player.getX(), player.getY());

            //items
            for(Item e : items) {
                if(!e.isDelete()) {
                    g.draw(e.getHitbox());
                    e.getImage().drawCentered(e.getX(), e.getY());
                }
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
            if (UIstate == 0) return;
            UIstate = (byte) (-UIstate + 3);
        }
    }

    private void spawnItems() throws SlickException {
        //bread
        if(random.nextFloat() < bread_spawn_rate) {
            items.add(new Bread(random.nextInt(1280), (byte) random.nextInt(3)));
        }
        //bomb
    }

    /**
     * called on game over
     */
    public static void gameover() {
        if(!gameover) {
            Player.speedY = -35;
            //play sound
        }
        gameover = true;
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