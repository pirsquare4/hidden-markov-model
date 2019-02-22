package model;

import java.util.concurrent.ThreadLocalRandom;
import java.util.*;

/**
 * Robot Simulator Object
 * Simulated Robot
 *
 * @author: Ben Chu & Giordano Bonora Groome
 */
public class robot {
    public int position;
    public int heading;

    /**
     * Robot constructor with the position and the heading.
     */
    public robot() {
        position = ThreadLocalRandom.current().nextInt(0, 64);
        heading = ThreadLocalRandom.current().nextInt(0, 4);
    }

    public int[] change_direction = {0, 1, 2, 3};
    public Random rnd = new Random();


    /**
     * Sets the intial position and heading then prints the information.
     */
    public void setRandom_position_heading() {
        System.out.println("Starting Position is " + (position));
        System.out.println("Starting Heading is " + (heading));
    }

    /**
     * Takes a single step in the direction given. |8| = vertical and |1| = horizontal
     * @param head
     * @return integer for step
     */
    public int step_in_heading(int head) {
        if (head == 0) {
            return 8;
        } else if (head == 1) {
            return 1;
        } else if (head == 2) {
            return -8;
        } else {
            return -1;
        }
    }

    /**
     * Evaluates if the location is a corner
     * @param position
     * @return boolean
     */
    public boolean isCorner(int position) {
        if (position == 0 || position == 7 || position == 56 || position == 63) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This function moves the robot for a round. It calculates the
     * probabilities of changing headings and takes a step.
     * @return integer
     */
    public int move_robot() {
        int a = rnd.nextInt(100);
        if (Board.willHitWall(heading, position) || a < 30) {
            int[] newHeadings = Board.validHeadings(position);
            if (a < 30) {
                newHeadings = Board.removeCurrentDirection(newHeadings, heading);
            }
            heading = newHeadings[rnd.nextInt(newHeadings.length)];
        }

        position += step_in_heading(heading);
        System.out.println(position);
        System.out.println(heading);
        return heading;

    }
}