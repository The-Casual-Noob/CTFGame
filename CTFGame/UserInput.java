package ctfgame;

/*Alexander Li
 *16/01/2019
 *UserInput.java
 *Mr. Rosen
 *Gets user input
 */

import java.util.*;
import hsa.Console;
public class UserInput extends Thread{
  
	Console c;
	private char inputKey;
	
	public UserInput(Console cIn) { 
		c = cIn;
		inputKey = ' ';	//Default value if no input is given
	}
  
	public void run(){
		inputKey = c.getChar();
	}
	
	public char getInputKey(){
		return inputKey;
	}
	
	/* ADD YOUR CODE HERE */
	
}
