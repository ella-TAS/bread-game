package main;

import gameObjects.*;
import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;

/**
 * main class for handling GameContainer, render and update instance
 * @author Ella
 *
 * @version alpha 0.1
 */

public class Main extends BasicGame {
    //debug tools
    private final boolean frame_advance = false; //press i to frame advance

    //constants
    private final float bread_spawn_rate = .1f;
    private final float bomb_spawn_rate = .001f;

    //util
    public static Random random;
    public static AppGameContainer app;
    public Input input;
    public UnicodeFont hopeGold;

    //const sprites
    private Image background;
    private Image fade;
    private Image arrow;

    //sounds
    public static Sound sound_eat;

    //gameObjects
    public static Player player;
    public static Counter counter;
    public static List<Item> items = new LinkedList<>();
    public Boss boss;

    //properties
    /** UIstate
     * 0 - menu
     * 1 - game
     * 2 - paused
     */
    public static byte UIstate, sfxVolume;
    public static boolean gameover;
    private static byte menuSelect;
    private int last_bread;
    private boolean bread_direction, isBoss;

    public Main(String title) throws SlickException {
        super(title);
    }

    /**
     * initializes the game
     */
    @Override
    public void init(GameContainer gc) throws SlickException {
        info("Starting game");
        background = new Image("assets/textures/bg.png", false, 2).getScaledCopy(5);
        fade = new Image("assets/textures/fade.png", false, 2).getScaledCopy(1280);
        arrow = new Image("assets/textures/arrow.png", false, 2).getScaledCopy(3);
        input = gc.getInput();
        random = new Random();
        counter = new Counter();
        player = new Player(input);
        gameover = bread_direction = false;
        sfxVolume = 10;
        sound_eat = new Sound("assets/sounds/eat.ogg");
        UIstate = menuSelect = 0;
        last_bread = 0;

        //font
        try {
            java.awt.Font UIFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream("assets/fonts/hopeGold.ttf"));
            UIFont = UIFont.deriveFont(java.awt.Font.PLAIN, 64.f);
            hopeGold = new UnicodeFont(UIFont);
            hopeGold.getEffects().add(new ColorEffect(java.awt.Color.white));
            hopeGold.addAsciiGlyphs();
            hopeGold.loadGlyphs();
        } catch(Exception e) {
            e.printStackTrace();
        }

        try {
            boss = new Boss();
        } catch(IOException e) {
            e.printStackTrace();
        }
        isBoss = true;
    }

    /**
     * updates the game every frame
     */
    @Override
    public void update(GameContainer gc, int arg) throws SlickException {
        if(input.isKeyPressed(Input.KEY_ESCAPE) && UIstate != 0) {
            UIstate = (byte) (-UIstate + 3);
            menuSelect = 0;
        }

        //game
        if(UIstate == 1 && (!frame_advance || input.isKeyPressed(Input.KEY_I))) {

            //player
            player.update();
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

            //boss
            if(isBoss) boss.update();
        }

        //menu
        menuing(gc);
    }

    /**
     * renders the game every frame
     */
    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        g.setColor(Color.white);

        //menu
        if(UIstate == 0) {
            hopeGold.drawString(586, 200, "Menu", Color.orange);
            hopeGold.drawString(580, 300, "Start");
            hopeGold.drawString(640 - hopeGold.getWidth("SFX Volume: " + sfxVolume/10f)/2f, 400, "SFX Volume: " + sfxVolume/10f);
            hopeGold.drawString(594, 500, "Exit");
            switch(menuSelect) {
                case 0:
                    arrow.drawCentered(540, 330);
                    break;
                case 1:
                    arrow.drawCentered(600 - hopeGold.getWidth("SFX Volume: " + sfxVolume/10f)/2f, 430);
                    break;
                case 2:
                    arrow.drawCentered(554, 530);
                    break;
            }
        }

        //game
        else {
            //background
            g.drawImage(background, 0, 0);

            //score
            hopeGold.drawString(10, 40, "Weight: " + counter.getScore(), new Color(0, 100, 12));

            //player
            player.render(g);

            //items
            for(Item e : items) {
                if(!e.isDelete()) e.render(g);
            }

            //boss
            boss.render(g);
        }

        //paused
        if(UIstate == 2) {
            fade.draw();
            hopeGold.drawString(562, 200, "Paused", Color.orange);
            hopeGold.drawString(562, 300, "Resume");
            hopeGold.drawString(640 - hopeGold.getWidth("SFX Volume: " + sfxVolume/10f)/2f, 400, "SFX Volume: " + sfxVolume/10f);
            hopeGold.drawString(462, 500, "Return to Menu");
            hopeGold.drawString(594, 600, "Exit");
            switch(menuSelect) {
                case 0:
                    arrow.drawCentered(522, 330);
                    break;
                case 1:
                    arrow.drawCentered(600 - hopeGold.getWidth("SFX Volume: " + sfxVolume/10f)/2f, 430);
                    break;
                case 2:
                    arrow.drawCentered(422, 530);
                    break;
                case 3:
                    arrow.drawCentered(554, 630);
                    break;
            }
        }
    }

    private void spawnItems() throws SlickException {
        //bread
        if(random.nextFloat() < bread_spawn_rate) {
            double xdelta = 1/sqrt(2*PI) * (exp(-0.5*pow(random.nextFloat()*3,2))) * 600;
            if(!bread_direction) last_bread += xdelta;
            else last_bread -= xdelta;
            if(last_bread > 1200){
                last_bread -= 2 * xdelta;
                bread_direction = true;
            }
            else if(last_bread < 0){
                last_bread += 2 * xdelta;
                bread_direction = false;
            }
            items.add(new Bread(last_bread + 40, (byte) random.nextInt(3)));
        }

        //bomb
        if(random.nextFloat() < bomb_spawn_rate) {
            items.add(new Bomb(random.nextInt(1200)+40));
        }
    }

    /**
     * handles the menu navigation
     */
    private void menuing(GameContainer gc) throws SlickException {
        boolean input_d = input.isKeyPressed(Input.KEY_DOWN) || input.isKeyPressed(Input.KEY_S);
        boolean input_u = input.isKeyPressed(Input.KEY_UP) || input.isKeyPressed(Input.KEY_W);
        boolean input_r = input.isKeyPressed(Input.KEY_RIGHT) || input.isKeyPressed(Input.KEY_D);
        boolean input_l = input.isKeyPressed(Input.KEY_LEFT) || input.isKeyPressed(Input.KEY_A);
        boolean input_yes = input.isKeyPressed(Input.KEY_ENTER) || input.isKeyPressed(Input.KEY_SPACE);

        //menu
        if(UIstate == 0) {
            if(input_d) {
                if(menuSelect == 2) menuSelect = 0;
                else menuSelect++;
            } else if(input_u) {
                if(menuSelect == 0) menuSelect = 2;
                else menuSelect--;
            }
            switch(menuSelect) {
                case 0:
                    if(input_yes) {
                        reset(gc);
                    }
                    break;
                case 1:
                    sfxControl(input_r, input_l);
                    break;
                case 2:
                    if(input_yes) app.exit();
                    break;
            }
        }

        //paused
        else if(UIstate == 2) {
            if(input_d) {
                if(menuSelect == 3) menuSelect = 0;
                else menuSelect++;
            } else if(input_u) {
                if(menuSelect == 0) menuSelect = 3;
                else menuSelect--;
            }
            switch(menuSelect) {
                case 0:
                    if(input_yes) UIstate = 1;
                    break;
                case 1:
                    sfxControl(input_r, input_l);
                    break;
                case 2:
                    if(input_yes) menu();
                    break;
                case 3:
                    if(input_yes) app.exit();
                    break;
            }
        }
    }

    /**
     * raises or lowers the sfx volume in the menu
     * @param input_r whether right is pressed
     * @param input_l whether left is pressed
     */
    private void sfxControl(boolean input_r, boolean input_l) {
        if(sfxVolume < 10 && input_r){
            sfxVolume += 1;
            sound_eat.play(1f, sfxVolume/10f);
        }
        else if(sfxVolume > 1 && input_l) {
            sfxVolume -= 1;
            sound_eat.play(1f, sfxVolume/10f);
        }
    }

    /**
     * returns to menu
     */
    public static void menu() {
        UIstate = 0;
        menuSelect = 0;
    }

    /**
     * resets and restarts the game
     */
    private void reset(GameContainer gc) throws SlickException {
        init(gc);
        UIstate = 1;
        items.removeAll(items);
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
    public static void info(String info) {
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
     * gives the AppGameContainer to the Main class
     */
    public static void setApp(AppGameContainer a) {
        app = a;
    }

    /**
     * starts the game engine
     */
    public static void main(String[] args) {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println(new Date() + " INFO:Stopping Game Engine")));
            AppGameContainer app = new AppGameContainer(new Main("Brojeggd Brohd und Brödchen"));
            Main.setApp(app);
            app.setDisplayMode(1280,920, false);
            app.setTargetFrameRate(60);
            app.setVSync(true);
            app.setShowFPS(true);
            app.start();
        } catch(Exception e) {e.printStackTrace();}
    }
}