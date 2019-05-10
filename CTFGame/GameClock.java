package ctfgame;

/*Alexander Li
 *16/01/2019
 *GameClock.java
 *Mr. Rosen
 *Timer counts seconds since start of game. Also controls Player ability cooldowns
 */

public class GameClock extends Thread{
	int time;

	public GameClock() { 
		time = 0;
	}
	
	//Increments timer approx. once every second
	public void run(){
		while (true)
		{
				try {
					Thread.sleep(9999);
				} 
				catch (InterruptedException e) {
				}
			time++;
		}
	}

	
	/* ADD YOUR CODE HERE */

}
