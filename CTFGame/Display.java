package ctfgame;

import java.awt.*;
import hsa.Console;

/*Alexander Li
 *16/01/2019
 *Display.java
 *Mr. Rosen
 *Displays scoreboard, background, and player objects to console. Updates screen
 */

public class Display extends Thread{

	Console c;
	Player p1;
	Player p2;
	GameClock clock;
	boolean isGameOver;
	String difficulty;
	String gamemode;
	
	

	//Class constructor
	public Display(Console cIn, Player p1In, Player p2In, GameClock clockIn, String difficultyIn, String gamemodeIn) { 
		isGameOver = false;

		c = cIn;
		p1 = p1In;
		p2 = p2In;
		clock = clockIn;
		difficulty = difficultyIn;
		gamemode = gamemodeIn;
	}

	public void run()
	{
		//Note: since the screen is updated at a different rate than calculations are made,
		//The entire background will be redrawn
		//(Ah, the troubles of having only one layer to work with)
		
		//Setup
		
		//During game
		while(true){
			try
			{
				//Redraw background
				c.setColor(Color.BLACK);
				for (int x = 0; x < 1024; x += 64){
					c.drawLine(x, 0, x, 767);
				}
				c.setColor(Color.GREEN);
				c.fillRect(0, 64, 1023, 767);
				c.setColor(Graphics.LIGHTGREEN);
				c.fillRect(Player.noMansLand[0], 64, Player.noMansLand[1]-Player.noMansLand[0], 959);
				
				//Redraw scoreboard
				c.setColor(Color.GRAY);
				c.fillRect(0, 0, 1023, 63);
				//Text
				c.setColor(Color.WHITE);
				c.setTextBackgroundColor(Color.GRAY);
				c.setFont(Graphics.mainFont);
				c.drawString("Capture the Flag", 400, 30);
				c.setFont(Graphics.sideFont);
				if (gamemode.equals("timed")) c.drawString("Time: " + clock.time, 480, 54);
				c.drawString("3", 200, 40);
				c.drawString("3", 800, 40);
				//Flag graphics in scoreboard
				int sbP1FlagX[] = {150, 165, 150};
				int sbP2FlagX[] = {850, 865, 850};
				int sbFlagY[] = {20, 30, 40};
				c.setColor(Color.BLACK);
				c.drawLine(150, 20, 150, 50);
				c.drawLine(850, 20, 850, 50);
				c.setColor(Color.RED);
				c.fillPolygon(sbP1FlagX, sbFlagY, p1.captures);
				c.setColor(Color.BLUE);
				c.fillPolygon(sbP2FlagX, sbFlagY, p2.captures);
				
				//Redraw flag area and other items
				//Flag area
				c.setColor(Color.RED);
				c.fillRect(Player.p1FlagCoords[0], Player.p1FlagCoords[1], Player.p1FlagCoords[2], Player.p1FlagCoords[3]);
				c.fillRect(Player.p1JailCoords[0], Player.p1JailCoords[1], Player.p1JailCoords[2], Player.p1JailCoords[3]);
				c.setColor(Color.BLUE);
				c.fillRect(Player.p2FlagCoords[0], Player.p2FlagCoords[1], Player.p2FlagCoords[2], Player.p2FlagCoords[3]);
				c.fillRect(Player.p2JailCoords[0], Player.p2JailCoords[1], Player.p2JailCoords[2], Player.p2JailCoords[3]);
				c.setColor(Color.GRAY);
				c.fillRect(Player.p1JailCoords[0]+4, Player.p1JailCoords[1]+4, 56, 56);
				c.fillRect(Player.p2JailCoords[0]+4, Player.p2JailCoords[1]+4, 56, 56);
				
				//Redraw obstacles
				
				//Draw player 1
				
				//Draw player 2
				
				Thread.sleep(40);
				
			}
			catch(Exception e){
				  
			}
		}

	}



}
