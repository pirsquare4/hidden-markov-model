import java.util.concurrent.ThreadLocalRandom;
import java.util.*;

public class robot {

    public static void main(String [ ] args) {

    public robot( int position, int heading) {
            this.position = position;
            this.heading = heading;
        }

        public int[] change_direction = {0, 1, 2, 3};
        public Random rnd = new Random();

        public void random_position_heading () {
            this.position = ThreadLocalRandom.current().nextInt(0, 64);
            this.heading = ThreadLocalRandom.current().nextInt(0, 5);
        }

        public int position = this.position;
        public int heading = this.heading;

        //Set the intial position and heading then print
        public void setRandom_position_heading () {
            random_position_heading();
            System.out.println("Starting Position is " + (position));
            System.out.println("Starting Heading is " + (heading));
        }

        public int step_in_heading(int head) {
            if (head == 0) {
                return 8;
            } else if (head == 1) {
                return 1;
            } else if (head == 1) {
                return 1;
            } else {
                return 1;
            }
        }

        // Robot moves 10 rounds
        public void move_robot () {
            int counter = 0;
            while (loop) {
                if (--isWall()--) {
                    new_heading = change_direction[rnd.nextInt(change_direction.length)];
                    while (heading == new_heading) {
                        new_heading = change_direction[rnd.nextInt(change_direction.length)];
                    }
                    heading = new_heading;
                } else if (--isCorner--) {

                }
                position += step_in_heading(new_heading);
                counter++;
                if (counter > 9) {
                    loop = false;
                }
            }
        }

        public boolean loop = true;
    }
}