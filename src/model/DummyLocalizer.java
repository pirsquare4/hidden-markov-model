package model;

import control.EstimatorInterface;

/**
 * Looks stupid, but is smart ;)
 * This "Dummy Localizer" tracks the robot and reports our best estimate
 * for where the robot actually is on the board.
 * @author: Giordano Bonora Groome & Ben Chu
 */
public class DummyLocalizer implements EstimatorInterface {
		
	private int rows, cols, head;
	private robot myRobot;
	private Board board;
	private Sensor sensor;
	private int currentScan;
	public double[][] transitionMatrix;
	public double[][] allObservationMatrixes;
	private double[] emptyUpdate;
	private double[][] f;
	private int manSum;
	private int numRounds;

	/**
	 * Localizer constructor for a ROWS*COLS board, with HEAD different headings
	 */
	public DummyLocalizer( int rows, int cols, int head) {
		this.rows = rows;
		this.cols = cols;
		this.head = head;
		myRobot = new robot();
		board = new Board(myRobot.position);
		sensor = new Sensor();
		currentScan = sensor.scan(board);
		this.transitionMatrix = new double[this.rows * this.cols * 4][this.rows * this.cols * 4];
		this.transitionMatrix = createTransition();
		this.allObservationMatrixes = createObsMatrix();
		f = new double[this.rows * this.cols * this.head][1];
		for (int i = 0; i < f.length; i++) {
			f[i][0] = 1.0 / (this.rows * this.cols * this.head);
		}
		manSum = 0;
		numRounds = 0;
	}
	
	public int getNumRows() {
		return rows;
	}
	
	public int getNumCols() {
		return cols;
	}
	
	public int getNumHead() {
		return head;
	}

	/**
	 * returns the probability entry (Tij) of the transition matrix T to go from pose
	 * 	 * i = (x, y, h) to pose j = (nX, nY, nH)
	 */
	public double getTProb( int x, int y, int h, int nX, int nY, int nH) {
		int i = (x*getNumRows() + y) * getNumHead() + board.translateDirection(h);
		int j = (nX * getNumRows() + nY) * getNumHead() + board.translateDirection(nH);
		return transitionMatrix[i][j];
	}

	/*
	 * returns the probability entry of the sensor matrices O to get reading r corresponding
	 * to position (rX, rY) when actually in position (x, y) (note that you have to take
	 * care of potentially necessary transformations from states i = <x, y, h> to
	 * positions (x, y)).
	 */
	public double getOrXY( int rX, int rY, int x, int y, int h) {
		if (rX == -1 && rY == -1) {
			return allObservationMatrixes[64][x*getNumRows() + y];
		}
		return allObservationMatrixes[rX*getNumRows() + rY][x*getNumRows() + y];
	}


	public int[] getCurrentTrueState() {
		int[] ret = board.getXY(myRobot);
		ret[2] = board.translateDirection(ret[2]);
		return ret;
	}

	/**
	 * Returns the current sensor reading if there is one, translated from an int to an x y pair
	 * and null if there is no reading.
	 */
	public int[] getCurrentReading() {
		if (currentScan == -1) {
			return null;
		}
		int[] result = new int[2];
		int X = currentScan % 8;
		int Y = currentScan / 8;
		result[0] = Y;
		result[1] = X;
		return result;
	}
	/**
	 * Returns the probability of the robot being at row x, col y on the board
	 * given the sensor readings and the HMM.
	 */
	public double getCurrentProb( int x, int y) {
		int IntForm = (Board.XYtoInt(x, y))*4;
		double sum = 0;
		for (int i = 0; i < getNumHead(); i++) {
			sum += f[IntForm + i][0];
		}
		return sum;
	}

	/**
	 * Updates f with the information given from the new scan
	 * f_t+1 = O[i]*tranpose(T)*f_t (uses forward filtering)
	 */
	public void update() {
		int new_heading = myRobot.move_robot();
		board.move(new_heading);
		currentScan = sensor.scan(board);
		int[] XYScan = sensor.scanTranslate(currentScan);
		int[] XYTrue = sensor.scanTranslate(board.getRobotPosition());
		if (XYScan == null) {
			f = Board.multiplicar(Board.multiplicar(vectorToMatrix4(allObservationMatrixes[64]), transpose(transitionMatrix)), f);
		} else {
			f = Board.multiplicar(Board.multiplicar(vectorToMatrix4(allObservationMatrixes[currentScan]), transpose(transitionMatrix)), f);
		}
		double sum = 0;
		for (int i = 0; i < f.length; i++) {
			sum += f[i][0];
		}
		for (int i = 0; i < f.length; i++) {
			f[i][0] = f[i][0]/sum;
		}

		sum = 0;
		for (int i = 0; i < f.length; i++) {
			sum += f[i][0];
		}
		double[] allNums = new double[cols*rows];
		for (int i = 0; i < rows*cols; i++) {
			allNums[i] = getCurrentProb(i/getNumRows(), i%getNumCols());
		}
		int largest = getIndexOfLargest(allNums);

		manSum +=  (Math.abs(largest/getNumRows() - XYTrue[1]) + Math.abs(largest%getNumCols() - XYTrue[0]));
		numRounds++;
		System.out.println((double) manSum/numRounds); //Prints out Manhattan Distance
	}

	/**
	 * Returns a vector.length*vector.length*4 by vector.length*vector.length*4 matrix
	 * which for each row i in vector, puts i on the diagonal 4 times repeated
	 * vectorToMatrix4([2]) =
	 * 		  [[2,0,0,0]
	 * 		   [0,2,0,0]
	 * 		   [0,0,2,0]
	 * 		   [0,0,0,2]]
	 */
	public static double[][] vectorToMatrix4(double[] vector) {
		int len = vector.length;
		double[][] matrix = new double[len*4][len*4];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = 0;
			}
		}
		for (int i = 0; i < len; i++) {
			matrix[4*i][4*i] = vector[i];
			matrix[4*i + 1][4*i + 1] = vector[i];
			matrix[4*i + 2][4*i + 2] = vector[i];
			matrix[4*i + 3][4*i + 3] = vector[i];
		}
		return matrix;
	}

	/**
	 * Creates the transition matrix T, where T(i,j) represents the probability of going from state i to state j
	 */
	public double[][] createTransition() {
		double[][] transition = new double[this.getNumRows()*this.getNumCols()*this.getNumHead()][this.getNumRows()*this.getNumCols()*this.getNumHead()];
		for (int i = 0; i < transition.length; i++) {
			for (int j = 0; j < transition[0].length; j++) {
				transition[i][j] = this.probability(j,i);//PROB OF GOING FROM I TO J
			}
		}
		return transition;
	}

	/**
	 * Returns the probability of going from state i to state j in the transition matrix
	 * where i/4 = x_pos, i%4 = x_heading
	 * and where j/4 = y_pos, y%4 = y_heading
	 */
	public double probability(int i, int j) {
		//FROM B TO A
		int posA = i/4;
		int headA = i%4;
		int posB = j/4;
		int headB = j%4;
		int[] headings = Board.validHeadings(posB);
		int val = headings.length;
		if (Board.contains(headings, headB)) {
			val -= 1;
		}
		if (!Board.isNWSE(posB, posA)) {
			return 0.0;
		}
		if (headB == headA) {
			int movement = getMovement(headB);
			if (posB + movement == posA) {
				return 0.7;
			} else {
				return 0.0;
			}
		} else if (headA == 0 && posB + 8 == posA) {
			if (Board.willHitWall(headB, posB)) {
				return 1.0/val;
			} else {
				return 0.3/val;
			}
		} else if (headA == 1 && posB + 1 == posA) {
			if (Board.willHitWall(headB, posB)) {
				return 1.0/val;
			} else {
				return 0.3/val;
			}
		} else if (headA == 2 && posB - 8 == posA) {
			if (Board.willHitWall(headB, posB)) {
				return 1.0/val;
			} else {
				return 0.3/val;
			}
		} else if (headA == 3 && posB - 1 == posA) {
			if (Board.willHitWall(headB, posB)) {
				return 1.0/val;
			} else {
				return 0.3/val;
			}
		} else {
			return 0.0;
		}
	}

	/**
	 * Converts the heading to the equivalent movement on the board
	 */
	public static int getMovement(int heading) {
		if (heading == 0) {
			return 8;
		} else if (heading == 1) {
			return 1;
		} else if (heading == 2) {
			return -8;
		} else if (heading == 3) {
			return -1;
		} else {
			return 1000;
		}
	}

	/**
	 * Creates an observation Matrix (rows*cols + 1 by rows*cols) for the given board.
	 * for vector i, from 1 to len - 1 , vector i represents the observation vector for the
	 * given i. The last vector (Vector rows*col + 1) is the observation vector for "nothing"
	 * observation.
	 */
	public double[][] createObsMatrix() {
		double[][] result = new double[getNumCols()*getNumRows() + 1][getNumCols()*getNumRows()];
		emptyUpdate = new double[getNumRows()*getNumCols()];
		for (int i = 0; i < emptyUpdate.length ; i++) {
			double newVal = Board.getEmptyProbabilities(i);
			emptyUpdate[i] = newVal;
		}
		for (int i = 0; i < getNumRows()*getNumCols(); i++) {
			result[i] = createObsVector(i);
		}
		result[getNumCols()*getNumRows()] = emptyUpdate;
		return result;
	}

	/**
	 * Creates an singular observation vector for the given observation
	 * at position POS.
	 */
	public double[] createObsVector(int pos) {
		double[] result = new double[getNumCols()*getNumRows()];
		for (int i = 0; i < result.length; i++) {
			result[i] = 0;
		}
		for (int i = 0; i < 64; i++) {
			if (pos == i) {
				result[i] = 0.1;
			} else if (Board.isAdjacent(pos, i)) {
				result[i] = 0.05;
			} else if (Board.isAdjacent2(pos, i)) {
				result[i] = 0.025;
			} else {
				result[i] = 0.0;
			}
		}
		return result;
	}

	/**
	 * Transposes the matrix ARRAY.
	 */
	public double[][] transpose (double[][] array) {
		if (array == null || array.length == 0)//empty or unset array, nothing do to here
			return array;

		int width = array.length;
		int height = array[0].length;

		double[][] array_new = new double[height][width];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				array_new[y][x] = array[x][y];
			}
		}
		return array_new;
	}

	/**
	 * returns the index with the largest number
	 */
	public int getIndexOfLargest( double[] array )
	{
		if ( array == null || array.length == 0 ) return -1; // null or empty

		int largest = 0;
		for ( int i = 1; i < array.length; i++ )
		{
			if ( array[i] > array[largest] ) largest = i;
		}
		return largest; // position of the first largest found
	}





}