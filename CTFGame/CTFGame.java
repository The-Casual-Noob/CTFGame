package ctfgame;

/*Alexander Li
 *16/01/2019
 *CTFGame.java
 *Mr. Rosen
 *The main class of Capture the Flag. Contains most of the game setup methods.
 */

import java.io.*;
import java.awt.*;
import java.util.*;
import hsa.Console;
import hsa.Message;

public class CTFGame
{

	Console c;
	String difficulty;
	String gamemode;

	//Class constructor
	public CTFGame ()
	{
		//Equivalent to 760*1024: each column is 8 pixels, each row is 20 pixels
		c = new Console (38, 128, "CTFGame.java");
	}

	//Main method
	public static void main (String[] args)
	{
		CTFGame runtime = new CTFGame ();
		runtime.splashScreen();
		try{
			while(true){
				runtime.mainMenu();
				runtime.mainGame();
			}
		}
		catch (Exception e){
			System.out.println("exception was thrown at mainMenu. If did not choose to quit, this is unexpected behaviour");
			e.printStackTrace();
		}
		runtime.goodbye ();
	}

	//Prints title
	public void title ()
	{
		c.clear ();
		c.print (' ', 56);
		c.println ("Capture the Flag");
		c.println ();
	}

	//Pauses program execution until user enters input
	public void pauseProgram ()
	{
		char ch;
		c.println ();
		c.println ("Press any key to continue...");
		ch = c.getChar ();
	}

	//Splash screen static graphics
	private void splashScreen ()
	{
		//Text, static images
		SplashScreen s = new SplashScreen (c);
		s.start();
		c.println("CTFGame");
		//try {
		//	Thread.sleep(5000);
		//} catch (InterruptedException e) {
		//}
	}

	//Main menu: includes options for level difficulty and gamemode
	public void mainMenu () throws Exception
	{
		char choice;
		while (true)
		{
			title ();
			c.print (' ', 60);
			c.println ("Main Menu");
			c.println ();
			c.print (' ', 58);
			c.println ("1. New Game");
			c.print (' ', 58);
			c.println ("2. Instructions");
			c.print (' ', 58);
			c.println ("3. High Scores");
			c.print (' ', 58);
			c.println ("4. Quit");
			c.println ();
			c.println ("Enter an option number:");
			choice = c.getChar ();

			if (choice == '1')
				break;
			else if (choice == '2')
				instructions ();
			else if (choice == '3')
				highScores();
			else
				throw new Exception ();

		}
		
		title ();
		c.print (' ', 40);
		c.println ("Choose the level difficulty (different maps)");
		c.println ("1. Easy");
		c.println ("2. Medium");
		c.println ("3. Hard");
		c.println ();
		c.println ("Enter an option number: ");
		choice = c.getChar ();
		if (choice == '1'){
			difficulty = "easy";
		}
		else if (choice == '2')
			difficulty = "medium";
		else
			difficulty = "hard";
		c.println(difficulty + " difficulty chosen");
		
		Thread.sleep(100);

		c.println ();
		c.println ("Choose gamemode");
		c.println ("1. Timed (10 minutes)");
		c.println ("2. Untimed (3 captures)");
		c.println ();
		c.println ("Enter an option number: ");
		choice = c.getChar ();
		if (choice == '1')
			gamemode = "timed";
		else
			gamemode = "untimed";
		c.println(gamemode + " gamemode chosen");

		mainGame();
	}


	public void mainGame ()
	{
		boolean isP1Tagging;
		boolean isP2Tagging;
		int goalCaptures = 5;
		
		//Obstacle array: pass to player objects
		
		
		
		GameClock clock = new GameClock();	//Thread for game timer
		Player p1 = new Player('1', difficulty, clock);	//Player 1 object
		Player p2 = new Player('2', difficulty, clock);	//Player 2 object
		Display d = new Display(c, p1, p2, clock, difficulty, gamemode);	//Thread for displaying all graphics in the game to the console
		//Display can access the clock and player objects (same memory location)
		clock.start();
		d.start();
		
		//Processing
		while (!(p1.captures >= goalCaptures || p2.captures >= goalCaptures)){	//End game condition
			UserInput in = new UserInput(c);
			in.start();
			try{
				Thread.sleep(30);
				in.interrupt();	//If no input is received, the next calculations are made
			}
			catch(InterruptedException e){
			}
			//Handles tagging the other player: the other player object must be passed from here
			isP1Tagging = p1.calculateMove(in.getInputKey());
			isP2Tagging = p2.calculateMove(in.getInputKey());
			if (isP1Tagging) p1.tag(p2);
			else if (isP2Tagging) p2.tag(p1);
		}
		//Terminate the other threads
		clock.interrupt();
		d.interrupt();
		//To give time for threads to stop running
		try{
			Thread.sleep(200);
		}catch(Exception e){
			
		}
		
		if (p1.captures >= goalCaptures)
			c.println("Player 1 won!");
		else
			c.println("Player 2 win!");
		pauseProgram();
		
		
		if (gamemode.equals("timed"))
			createHighScore(clock.time);
	}

	//Displays instructions
	public void instructions ()
	{
		title ();
		c.print (' ', 34);
		c.println ("Instructions");
		c.println ("There are two players, each on an opposing side. Player 1 is on the left,"
				+ "\nPlayer 2 is on the right. ");

		c.println ("The goal is to capture the opponent's flag by taking it from their flag area"
				+ "\nand bringing it to their own.The flag is dropped if the flag carrier is caught"
				+ "\nand it will return to the opponent 's flag room when they touch it."
				+ "\nThe flag carrier has a 10 % movement speed reduction. ");
		c.println ("The first player to get 5 flag captures wins.");

		c.println ("You can tag the opponent in your territory or in no man's land to send them to"
				+ "\nyour jail area.They are kept there for 15 seconds, then they are released."
				+ "\nA jailbroken opponent cannot be tagged until they touch their own territory but"
				+ "\ncan 't pick up your flag or tag you until then.");

		c.println ("The land might contain obstacles that slow movement or are impassable.");

		c.println ("Controls:");
		c.println ("Player 1:                               Player 2");
		c.println ("Movement: WASD                          Movement: Num8, Num5, Num4, Num6");
		c.println ("STOP MOVEMENT: E, Q, Space              STOP MOVEMENT: Num7, Num9, Num0");
		c.println ("Tag opponent: G                         Tag opponent: P");
		c.println ("Dash ability: H                         Dash ability: [");
		c.println ("Dodge ability: J                        Dash ability: ]");

		c.println ("Note: turn number keys on for player 2 controls to work");
		c.println ("Do not hold down keys: movement will continue until you press the stop button");
		
		pauseProgram();
	}

	//DEBUG: INPUT IS READ INCORRECTLY
	//Displays high scores for all difficulties (lowest times). Reads from files
	public void highScores ()
	{
		char choice;
		//Array contains the line to insert into the char
		//Data is formatted while entering into file to be 20 characters long
		String dataArr[] = new String[30];

		title();
		c.print(' ', 35);
		c.println("High Scores");

		//do 3 loops: one for each file
		//Read files
		//0-9 is Easy, 10-19 is Medium, 20-29 is Hard
		/*
		try
		{
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		*/
		
		//Scores on easy difficulty
		try
		{
			String fileName = "C:\\Users\\dli20\\Desktop\\ICS Java Workspace\\ICS-3U3\\AlexanderLi22--Culminating\\CTFGame\\src\\ctfgame\\highScoresEasy.txt";
			BufferedReader inputEasy = new BufferedReader(new FileReader(fileName));
			//Reads from file to storage array
			for (int i = 0; i < 10; i++)
			{
				String inputLine = inputEasy.readLine();
				if (!inputLine.equals(null)) 
					dataArr[i] = inputLine;
				else
					dataArr[i] = (" ");
			}
			inputEasy.close();
		}
		catch(Exception e)
		{
			new Message("Something bugged out with highScores() highScoresEasy.txt");
			e.printStackTrace();
		}
		//Scores on medium difficulty
		try
		{
			String fileName = "C:\\Users\\dli20\\Desktop\\ICS Java Workspace\\ICS-3U3\\AlexanderLi22--Culminating\\CTFGame\\src\\ctfgame\\highScoresMedium.txt";
			BufferedReader inputMedium = new BufferedReader(new FileReader(fileName));
			//Reads from file to storage array
			for (int i = 10; i < 20; i++)
			{
				String inputLine = inputMedium.readLine();
				dataArr[i] = (i+1 + ".  ");
				if (!inputLine.equals(null)) 
					dataArr[i] = inputLine;
				else
					dataArr[i] = (" ");
			}
			inputMedium.close();
		}
		catch(Exception e)
		{
			new Message("Something bugged out with highScores() -- highScoresMedium.txt");
			e.printStackTrace();
		}
		//Scores on hard difficulty
		try
		{
			String fileName = "C:\\Users\\dli20\\Desktop\\ICS Java Workspace\\ICS-3U3\\AlexanderLi22--Culminating\\CTFGame\\src\\ctfgame\\highScoresHard.txt";
			BufferedReader inputHard = new BufferedReader(new FileReader(fileName));
			//Reads from file to storage array
			for (int i = 20; i < 30; i++)
			{
				String inputLine = inputHard.readLine();
				dataArr[i] = (i+1 + ".  ");
				if (!inputLine.equals(null)) 
					dataArr[i] = inputLine;
				else
					dataArr[i] = (" ");
			}
			inputHard.close();
		}
		catch(Exception e)
		{
			new Message("Something bugged out with highScoresHard()");
			e.printStackTrace();
		}
		
		//print the table
		//titles and headers
		title();
		c.print(' ', 30);
		c.println("High Scores");
		c.print(' ', 10);
		//difficulties
		c.print("Easy");
		c.print(' ', 20);
		c.print("Medium");
		c.print(' ', 20);
		c.println("Hard");
		c.println();
		//headers: place, name, and time
		//Values
		for (int i = 0; i < 10; i++){
			c.print((i+1) + ". ", 4);
			//c.print(' ', 2);
			c.print(dataArr[i], 25);
			//c.print(' ', 6);
			c.print(dataArr[i+10], 25);
			//c.print(' ', 6);
			c.println(dataArr[i+20], 25);
		}
		pauseProgram();
		
	}
	
	//After games for timed mode, checks if a high score has been achieved
	public void createHighScore (int time)
	{
		BufferedReader input;
		PrintWriter output;
		String fileName;
		String name = "";
		String namesArr[];
		int scoresArr[];
		
		if (difficulty.equals("hard")) fileName = "C:\\Users\\dli20\\Desktop\\ICS Java Workspace\\ICS-3U3\\AlexanderLi22--Culminating\\CTFGame\\src\\ctfgame\\highScoresHard.txt";
		else if (difficulty.equals("medium")) fileName = "C:\\Users\\dli20\\Desktop\\ICS Java Workspace\\ICS-3U3\\AlexanderLi22--Culminating\\CTFGame\\src\\ctfgame\\highScoresMedium.txt";
		else if (difficulty.equals("easy")) fileName = "C:\\Users\\dli20\\Desktop\\ICS Java Workspace\\ICS-3U3\\AlexanderLi22--Culminating\\CTFGame\\src\\ctfgame\\highScoresEasy.txt";
		else{
			new Message("Error with createHighScore: difficulty undefined");
			fileName = "highScoresHard.txt";
		}
		
		try
		{
			input = new BufferedReader(new FileReader(fileName));
			output = new PrintWriter(new FileWriter(fileName));
			
			namesArr = new String[10];
			scoresArr = new int[10];
			
			for (int i = 0; i < 10; i++){
				String fileLine = input.readLine();
				if (!fileLine.equals(null)){
					String tokens[] = fileLine.split(" ");
					namesArr[i] = tokens[0];
					scoresArr[i] = Integer.parseInt(tokens[1]);
				}
			}
			
			//Note: the first person to get a certain scores gets higher spot on the list
			if (scoresArr[9] == 0 || time < scoresArr[9]){
				new Message("NEW TOP 10 TIME! \nTime: " + time);
				title();
				c.println("New top 10 time!");
				c.println("Enter your pseudonym: ");
				c.println("Name must be 14 chars or less. Use only alphanumeric characters and underscores");
				name = c.readLine();
				c.println();
				
				//Put score into correct spot of array
				for (int i = 0; i < 10; i++){
					//Array is not filled yet
					if (scoresArr[i] == 0){
						scoresArr[i] = time;
						namesArr[i] = name;
						break;
					}
					else if (time < scoresArr[i]){
						for (int j = 9; j > i; j--){
							scoresArr[j] = scoresArr[j-1];
							namesArr[j] = namesArr[j-1];
						}
						scoresArr[i] = time;
						namesArr[i] = name;
						break;
					}
				}
				
				//Write to file
				for (int i = 0; i < 10; i++){
					if (!namesArr[i].equals(null)){
						//Putting FileWriter in a for loop creates a null pointer exception
						String spacing = "";
						for (int x = 0; x < 16-namesArr[i].length(); x++) 
							spacing += (" ");
						output.print(namesArr[i]);
						output.print(spacing);
						output.println(scoresArr[i]);
					}
					else;
						//output.println(" ");
				}
				
				output.close();
				highScores();
			}
			output.close();
			
		}
		catch(Exception e)
		{
			new Message("Error with input or output for createHighScore");
			e.printStackTrace();
		}

	}
	
	//Prints ending messages and credits
	public void goodbye ()
	{
		title();
		c.println("Thanks for playing Capture the Flag!");
		c.println("CTFGame.java created by: Alexander Li");
		pauseProgram();
		c.close();
	}
	


}