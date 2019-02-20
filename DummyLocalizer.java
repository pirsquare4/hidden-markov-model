package model;

import control.EstimatorInterface;

public class DummyLocalizer implements EstimatorInterface {
		
	private int rows, cols, head;
	private robot myRobot;
	private Board board;
	private Sensor sensor;

	public DummyLocalizer( int rows, int cols, int head) {
		this.rows = rows;
		this.cols = cols;
		this.head = head;
		myRobot = new robot();
		board = new Board(myRobot.position);
		sensor = new Sensor;

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
		
		int[] ret = new int[3];
		ret[0] = rows/2;
		ret[1] = cols/2;
		ret[2] = head;
		return ret;

	}

	public int[] getCurrentReading() {
		int[] ret = null;
		return ret;
	}


	public double getCurrentProb( int x, int y) {
		double ret = 0.0;
		return ret;
	}

	public void update() {
		int new_heading = myRobot.move_robot();
		board.move(new_heading);
	}


}