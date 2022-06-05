package gameObjects;

import main.GameObject;
import main.Main;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * player, movement and controls
 * @author Ella
 */

public class Player extends GameObject {
    private static final int floor_level = 800;

    private final float speed_cap = 12f;
    private final float friction_hard = speed_cap * 5/27;
    private final float friction_soft = -speed_cap * 1/20;
    private final float jump_speed = -speed_cap;
    private final float jump_bonus = speed_cap * 4/9;
    private final float jump_peak = speed_cap * 4/9;
    private float gravity = speed_cap * 1/6;
    private float terminal_falling_velocity = speed_cap * 12/9;
    private final int jump_length = 10;
    private final int buffer_leniency = 5;
    private final Input input;

    public static float speedY;
    public float posY;
    private float speedX, posX;
    private int jumpTimer, jumpBuffer;
    private boolean grounded;

    public Player(Input i) throws SlickException {
        super(new Image("assets/textures/player/player_0.png", false, 2).getScaledCopy(4), 300, floor_level, 60, 150);
        speedX = speedY = 0;
        input = i;
        posX = 640;
        posY = floor_level;
        grounded = true;
    }

    /**
     * updates the object every frame
     */
    public void update() {
        if(!Main.gameover) {
            moveX();
            wallBounce();
            buffer();
        } else {
            jumpBuffer = 0;
            jumpTimer = 0;
            grounded = false;
            terminal_falling_velocity = 2.5f * speed_cap;
        }
        moveY();
        setLoc(Math.round(posX), Math.round(posY));
        moveHitbox();
    }

    /**
     * renders the object every frame
     */
    public void render(Graphics g) {
        g.draw(getHitbox());
        if(Main.gameover) getImage().getScaledCopy(-1).drawCentered(getX(), getY());
        else getImage().drawCentered(getX(), getY());
    }

    /**
     * horizontal movement
     */
    private void moveX() {
        boolean input_left = input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT);
        boolean input_right = input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT);

        if (input_right) {
            //held right
            speedX += speedX < speed_cap ? friction_hard : friction_soft;
            if (speedX < speed_cap + friction_hard && speedX > speed_cap - friction_hard) {
                speedX = speed_cap;
            }
        } else if (input_left) {
            //held left
            speedX -= speedX > -speed_cap ? friction_hard : friction_soft;
            if (speedX > -speed_cap - friction_hard && speedX < -speed_cap + friction_hard) {
                speedX = -speed_cap;
            }
        } else {
            //held neutral
            if (friction_hard < speedX) speedX -= friction_hard;
            else if (speedX < -friction_hard) speedX += friction_hard;
            else speedX = 0;
        }
        posX += speedX;
        //System.out.println(speedX);
    }

    /**
     * vertical movement
     */
    private void moveY() {
        boolean jump_held = (input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_SPACE));
        boolean input_left = input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT);
        boolean input_right = input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT);

        //vertical movement
        if (jumpBuffer > 0 && grounded) {
            //start jump
            jumpBuffer = 0;
            jumpTimer = jump_length;
            speedY = jump_speed;
            if(input_left) speedX -= jump_bonus;
            else if(input_right) speedX += jump_bonus;
            grounded = false;
        } else if (!grounded) {
            //airborne
            if(jumpTimer == 0) {
                //falling
                if(-jump_peak < speedY && speedY < jump_peak && jump_held) {
                    //quarter gravity on jump peak
                    speedY += gravity / 2;
                } else {
                    speedY += gravity;
                }
            } else if (!jump_held) {
                //released jump
                jumpTimer = 0;
            } else jumpTimer--;
        }
        if(speedY > terminal_falling_velocity) speedY = terminal_falling_velocity;
        posY += speedY;
        if (!grounded && posY > floor_level && !Main.gameover) {
            grounded = true;
            posY = floor_level;
            speedY = 0;
        }
        //System.out.println(speedY);
    }

    /**
     * stop on the wall
     */
    private void wallBounce() {
        if(posX < width / 2f) {
            posX = width / 2f;
            speedX = 0;
        }
        if(posX > 1280 - width / 2f) {
            posX = 1280 - width / 2f;
            speedX = 0;
        }
    }

    /**
     * allowing to buffer a jump for up to 5f
     */
    private void buffer() {
        if (input.isKeyPressed(Input.KEY_W) || input.isKeyPressed(Input.KEY_UP) || input.isKeyPressed(Input.KEY_SPACE)) {
            jumpBuffer = buffer_leniency;
        }
        if (input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_SPACE)) {
            if(jumpBuffer > 0) jumpBuffer--;
        } else {
            jumpBuffer = 0;
        }
    }
}