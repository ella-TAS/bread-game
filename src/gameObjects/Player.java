package gameObjects;

import main.GameObject;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * player and controls
 * @author Ella
 */

public class Player extends GameObject {
    private final float speed_cap = 15f;
    private final float friction_ground = speed_cap * 5/27;
    private final float friction_air_hard = speed_cap * 13/108;
    private final float friction_air_soft = speed_cap * 13/270;
    private final float jump_speed = -speed_cap;
    private final float jump_bonus = speed_cap * 2/9;
    private final float gravity = speed_cap * 1/6;
    private final float gravity_peak = speed_cap * 9/9;
    private final float terminal_falling_velocity = speed_cap * 12/9;
    private final int jump_length = 10;
    private final int floor_level = 300;
    private final int buffer_leniency = 7;
    private final Input input;

    private float speedX, speedY, posX, posY;
    private int jumpTimer, jumpBuffer;
    private boolean grounded;

    public Player(Input i) throws SlickException {
        super(new Image("assets/textures/player0.png", false, 2).getScaledCopy(5), 300, 300, 200, 100);
        speedX = speedY = 0;
        input = i;
        posX = 300;
        posY = floor_level;
        grounded = true;
    }

    /**
     * updates the object every frame
     */
    public void update() {
        buffer();
        move();
        moveHitbox();
    }

    /**
     * movement and controls
     */
    private void move() {
        boolean input_left = input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT);
        boolean input_right = input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT);
        boolean jump_held = (input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_SPACE));

        //horizontal movement
        float friction = grounded ? friction_ground : friction_air_hard;
        if(input_right) {
            //held right
            if(speedX < speed_cap) {
                //acceleration
                speedX += friction;
            }
            //above speed cap
            else speedX -= friction_air_soft;
        } else if(input_left) {
            //held left
            if(speedX > -speed_cap) {
                //acceleration
                speedX -= friction;
            }
            //below negative speed cap
            else speedX += friction_air_soft;
        } else {
            //held neutral - deceleration
            if (friction < speedX) speedX -= friction;
            else if (speedX < -friction) speedX += friction;
            else speedX = 0;
        }

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
                if(-gravity_peak < speedY && speedY < gravity_peak && jump_held) {
                    //quarter gravity on jump peak
                    speedY += gravity / 4;
                }
                speedY += gravity;
            } else if (!jump_held) {
                //released jump
                jumpTimer = 0;
            } else jumpTimer--;
        }
        if(speedY > terminal_falling_velocity) speedY = terminal_falling_velocity;
        posY += speedY;
        posX += speedX;
        if (!grounded && posY > floor_level) {
            grounded = true;
            posY = floor_level;
            speedY = 0;
        }
        setLoc(Math.round(posX), Math.round(posY));
    }

    /**
     * allowing to buffer a jump for up to 7f
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