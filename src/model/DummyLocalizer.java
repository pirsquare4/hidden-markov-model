package model;

import control.EstimatorInterface;

public class DummyLocalizer implements EstimatorInterface {
		
	private int rows, cols, head;
	private robot myRobot;
	private Board board;
	private Sensor sensor;
	private int currentScan;
	public double[][] myProbs;

	public DummyLocalizer( int rows, int cols, int head) {
		this.rows = rows;
		this.cols = cols;
		this.head = head;
		myRobot = new robot();
		board = new Board(myRobot.position);
		sensor = new Sensor();
		currentScan = sensor.scan(board);
		myProbs = new double[this.rows*this.cols*4][this.rows*this.cols*4];

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
		double ret = 1/64;
		return ret;
	}

	public void update() {
		int new_heading = myRobot.move_robot();
		board.move(new_heading);
		currentScan = sensor.scan(board);

	}


}