package model;

import control.EstimatorInterface;
import java.util.*;
import java.util.stream.*;


public class DummyLocalizer implements EstimatorInterface {
		
	private int rows, cols, head;
	private robot myRobot;
	private Board board;
	private Sensor sensor;
	private int currentScan;
	public double[][] transitionMatrix;
	public double[][] allObservationMatrixes;
	private double[] emptyUpdate;
	private double normalizer;
	private double[][] f;

	public DummyLocalizer( int rows, int cols, int head) {
		this.rows = rows;
		this.cols = cols;
		this.head = head;
		myRobot = new robot();
		board = new Board(myRobot.position);
		sensor = new Sensor();
		currentScan = sensor.scan(board);
		this.transitionMatrix = new double[this.rows*this.cols*4][this.rows*this.cols*4];
		this.transitionMatrix = createTransition();
		this.allObservationMatrixes = createObsMatrixes();
		f = new double[1][this.rows*this.cols*this.head];
		Arrays.fill(f[0], 1.0/this.rows*this.cols*this.head);
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
	
	public double getTProb( int x, int y, int h, int nX, int nY, int nH) {
		return 0.0;
	}

	public double getOrXY( int rX, int rY, int x, int y, int h) {
		return 0.1;
	}


	public int[] getCurrentTrueState() {
		int[] ret = board.getXY(myRobot);
		ret[2] = board.translateDirection(ret[2]);
		return ret;
	}

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


	public double getCurrentProb( int x, int y) {
		int IntForm = (Board.XYtoInt(x, y));
		double sum = 0;
		for (int i = 0; i < getNumHead(); i++) {
			sum += f[0][IntForm + i];
		}
		return sum;
	}

	public void update() {
		int new_heading = myRobot.move_robot();
		board.move(new_heading);
		currentScan = sensor.scan(board);
		int[] XYScan = sensor.scanTranslate(currentScan);
		if (XYScan == null) {
			f = Board.multiplicar(Board.multiplicar(vectorToMatrix4(allObservationMatrixes[64]), transpose(transitionMatrix)), f);
		} else {
			f = Board.multiplicar(Board.multiplicar(vectorToMatrix4(allObservationMatrixes[currentScan]), transpose(transitionMatrix)), f);
		}
		double sum = 0;
		for (int i = 0; i < f[0].length; i++) {
			sum += f[0][i];
		}
		for (int i = 0; i < f[0].length; i++) {
			f[0][i] = f[0][i]/sum;
		}

	}

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

	public static double[][] normalizeNested(double[][] arr) {
		double sum = 0.0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				sum += arr[i][j];
			}
		}
		for (int i = 0; i < arr.length; i++) {
			for(int j = 0; j < arr.length; j++) {
				arr[i][j] = arr[i][j] / sum;
			}
		}
		System.out.print("SUM IS: ");
		System.out.println(sum);
		return arr;
	}

	public double[][] createTransition() {
		double[][] transition = new double[this.getNumRows()*this.getNumCols()*this.getNumHead()][this.getNumRows()*this.getNumCols()*this.getNumHead()];
		for (int i = 0; i < transition.length; i++) {
			for (int j = 0; j < transition[0].length; j++) {
				transition[i][j] = this.probability(j, i);//PROB OF GOING FROM J TO I
			}
		}
		return transition;
	}

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

	public double[][] createObsMatrixes() {
		double[][] result = new double[getNumCols()*getNumRows() + 1][getNumCols()*getNumRows()];
		emptyUpdate = new double[64];
		for (int i = 0; i < 64; i++) {
			double newVal = Board.GioTest(i);
			emptyUpdate[i] = newVal;
		}
		for (int i = 0; i < getNumRows()*getNumCols(); i++) {
			result[i] = createObsMatrix(i);
		}
		result[getNumCols()*getNumRows()] = emptyUpdate;
		return result;
	}

	public double[] createObsMatrix(int pos) {
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





}