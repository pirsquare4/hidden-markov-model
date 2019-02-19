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

    public robot() {
        position = ThreadLocalRandom.current().nextInt(0, 64);
        heading = ThreadLocalRandom.current().nextInt(0, 4);
    }

    public int[] change_direction = {0, 1, 2, 3};
    public Random rnd = new Random();


    //Set the intial position and heading then print
    public void setRandom_position_heading() {
        System.out.println("Starting Position is " + (position));
        System.out.println("Starting Heading is " + (heading));
    }

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

    public boolean isCorner(int position) {
        if (position == 0 || position == 7 || position == 56 || position == 63) {
            return true;
        } else {
            return false;
        }
    }

    // Robot moves 1 round
    public int move_robot() {
        int[] newHeadings = Board.validHeadings(position);
        if (Board.willHitWall(heading, position) || rnd.nextInt(100) < 30) {
            heading = newHeadings[rnd.nextInt(newHeadings.length)];
        }
        position += step_in_heading(heading);
        System.out.println(position);
        System.out.println(heading);
        return heading;

    }

    public boolean loop = true;
}