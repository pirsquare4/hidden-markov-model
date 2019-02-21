package model;

import control.EstimatorInterface;
import java.util.*;

public class DummyLocalizer implements EstimatorInterface {
		
	private int rows, cols, head;
	private robot myRobot;
	private Board board;
	private Sensor sensor;
	private int currentScan;
	public double[][] transitionMatrix;
	public double[][] allObservationMatrixes;
	private double[] emptyUpdate;

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
		return this.transitionMatrix[x][y] + this.transitionMatrix[x][y+1] + this.transitionMatrix[x][y+2] + this.transitionMatrix[x][y+3];
	}

	public void update() {
		int new_heading = myRobot.move_robot();
		board.move(new_heading);
		currentScan = sensor.scan(board);
		int[] XYScan = sensor.scanTranslate(currentScan);
		if (XYScan == null) {
			this.transitionMatrix = Board.multiplicar(this.transitionMatrix, vectorToMatrix4(emptyUpdate));
			this.transitionMatrix = normalizeNested(this.transitionMatrix);
		} else {
			double[] dynamicUpdate = getProbs(XYScan);
			this.transitionMatrix = Board.multiplicar(this.transitionMatrix, vectorToMatrix4(dynamicUpdate));
			this.transitionMatrix = normalizeNested(this.transitionMatrix);

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

	public static double[] getProbs(int[] readings) {
		double[] myarr = new double[64];
		Arrays.fill(myarr, 1.0);
		return myarr;
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
				transition[i][j] = this.probability(i,j);//PROB OF GOING FROM COL J TO ROW I
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
		if (!Board.isNWSE(posB, posA)) {
			return 0.0;
		}
		if (headB == headA) { //going straight currently
			int movement = getMovement(headB);
			if (posB + movement == posA) {
				return 0.7;
			} else {
				return 0.0;
			}
		} else if (headA == 0 && posB + 8 == posA) {
			return 0.1;
		} else if (headA == 1 && posB + 1 == posA) {
			return 0.1;
		} else if (headA == 2 && posB - 8 == posA) {
			return 0.1;
		} else if (headA == 3 && posB - 1 == posA) {
			return 0.1;
		} else {
			return 0.0;
		}
	}

	public int getMovement(int heading) {
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
		double sum = 0;
		emptyUpdate = new double[64];
		for (int i = 0; i < 64; i++) {
			double newVal = Board.GioTest(i);
			sum += newVal;
			emptyUpdate[i] = newVal;
		}
		result[getNumCols()*getNumRows()] = emptyUpdate;
		for (int i = 0; i < getNumRows()*getNumCols(); i++) {
			result[i] = createObsMatrix(i);
		}
		return result;
	}

	public double[] createObsMatrix(int pos) {
		double[] result = new double[getNumCols()*getNumRows()];
		for (int i = 0; i < result.length; i++) {
			result[i] = 0;
		}
		result[pos] = 0.1;
		int[] movements = {18,17,16,15,14,
							10,9,8,7,6,
							-2,-1,1,2
							-10,-9,-8,-7,-6
							-18,-17,-16,-15,-14};
		for (int i = 0; i < movements.length; i++) {
			if (Board.isAdjacent(pos, pos+movements[i])) {
				result[i] = 0.05;
			} else if (Board.isAdjacent2(pos, pos + movements[i])) {
				result[i] = 0.025;
			} else {
				result[i] = 0.0;
			}
		}
		return result;
	}





}