package model;
import java.util.*;

/**
 * This class is a sensor that calculates the
 * probability of a location containing the robot.
 * @author: Giordano Bonora Groome & Ben Chu
 */
class Sensor {
	/**
	 * A Random object.
	 */
	public Random rnd;

	/**
	 * The given sensor that utilizes the random object.
	 */
	public Sensor() {
		rnd = new Random();
	}

	/**
	 * This function scans the board and evaluates the
	 * probabilities of a robot being at the given location.
	 */
	public int scan(Board board) {
		int robotPosition = board.getRobotPosition();
		int rand = rnd.nextInt(1000);
		if (rand < 100) {
			return robotPosition;//10% chance of ret true position
		}
		int nLn1 = 0;
		int nLn2 = 0;
		int size = board.getBoardSize();
		ArrayList<Integer> valid1 = new ArrayList<Integer>();
		ArrayList<Integer> valid2 = new ArrayList<Integer>();
		int[] possibleLn2s = {
			size*2 - 2, size*2 - 1, size *2, size*2 + 1, size * 2 + 2,
			size - 2, size + 2,
			-2, 2,
			size*-1 - 2, size*-1 + 2,
			size*-2 - 2, size*-2 - 1, size*-2, size*-2 + 1, size*-2 + 2};
		int[] possibleLn1s = {size - 1, size, size + 1,
							-1, 1,
							size*-1 - 1, size*-1, size*-1 + +1 };
		for (int possibleLn2: possibleLn2s) {
			if (Board.isAdjacent2(robotPosition, robotPosition + possibleLn2)) {
				nLn2 += 1;
				valid2.add(robotPosition + possibleLn2);
			}
		}
		for (int possibleLn1: possibleLn1s) {
			if (Board.isAdjacent(robotPosition, robotPosition + possibleLn1)) {
				nLn1 += 1;
				valid1.add(robotPosition + possibleLn1);
			}
		}
		int thresholdLn1 = 100 + nLn1*50;
		int thresholdLn2 = 100 + nLn1*50 + nLn2*25;
		if (rand < thresholdLn1) { //pick randomLn1
			int randompick = rnd.nextInt(valid1.size());
			return valid1.get(randompick);

		} else if (rand >= thresholdLn1 && rand < thresholdLn2) { //pick randomLn2
			int randompick = rnd.nextInt(valid2.size());
			return valid2.get(randompick);
		} else {
			return -1;
		}
	}

	/**
	 * This helper function translates the scanned position
	 * into x/y coordinates and places it into an array.
	 */
	public int[] scanTranslate(int scanPos) {
		if (scanPos == -1) {
			return null;
		}
		int[] result = new int[2];
		int X = scanPos % 8;
		int Y = scanPos / 8;
		result[0] = X;
		result[1] = Y;
		return result;
	}
}
