package ctfgame;

import java.awt.*;
import hsa.Console;

public class SplashScreen extends Thread{
  Console c;
  
  public SplashScreen(Console cIn) { 
    c = cIn;
  }
  
  public void run(){
	  c.setCursor(2, 1);
	  c.println("SplashScreen Thread Executed");
  }
  /* ADD YOUR CODE HERE */
  
}
