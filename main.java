import java.util.*;

public class main {
	public static void main(String[] args) {
		robot myRobot = new robot();
	 	Board board = new Board(myRobot.position);
	 	while (true) {
	 		int new_heading = myRobot.move_robot();
	 		board.move(new_heading);
	 		board.PrintBoard();
	 		System.out.println("Push enter to see another step");
			Scanner scanner = new Scanner(System.in);
			String username = scanner.nextLine();
			System.out.println("Your username is " + username);
			board.PrintBoard();
	 	}
	}
}