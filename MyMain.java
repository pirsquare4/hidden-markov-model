import java.util.*;

public class MyMain {
	public static void main(String[] args) {
		robot myRobot = new robot();
	 	Board board = new Board(myRobot.position);
	 	Sensor sensor = new Sensor();
	 	while (true) {
	 		int new_heading = myRobot.move_robot();
	 		board.move(new_heading);
	 		int place = sensor.scan(board);
	 		System.out.println("Scanned position is " + Board.getStringPosition(place));
	 		board.PrintBoard();
	 		System.out.println("Push enter to see another step");
			Scanner scanner = new Scanner(System.in);
			String username = scanner.nextLine();
			board.PrintBoard();
	 	}
	}
}