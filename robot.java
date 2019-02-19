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

    // Robot moves 10 rounds
    public int move_robot() {
        int counter = 0;
        while (loop) {
            if (isCorner(position)) {
                if (position == 0) {
                    int[] cornerone = {0, 1};
                    heading = cornerone[rnd.nextInt(2)];
                } else if (position == 7) {
                    int[] cornertwo = {0, 3};
                    heading = cornertwo[rnd.nextInt(2)];
                } else if (position == 56) {
                    int[] cornerthree = {1, 2};
                    heading = cornerthree[rnd.nextInt(2)];
                } else {
                    int[] cornerfour = {2, 3};
                    heading = cornerfour[rnd.nextInt(2)];
                }
            } else if (isEdge(position) || rnd.nextInt(100) < 30) {
                new_heading = change_direction[rnd.nextInt(change_direction.length)];
                while (heading == new_heading) {
                    new_heading = change_direction[rnd.nextInt(change_direction.length)];
                }
                heading = new_heading;
            }
            position += step_in_heading(heading);
            System.out.println(position);
            System.out.println(heading);
            counter++;
            if (counter > 9) {
                loop = false;
            }
            return heading;
        }
    }

    public boolean loop = true;
}