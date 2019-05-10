package ctfgame;

/*Alexander Li
 *16/01/2019
 *Player.java
 *Mr. Rosen
 *Stores data for all player objects and contains player behaviours
 *Takes user input and decides what action to take
 */

public class Player{
	
	int captures = 0;
	int startSpeed = 1;
	boolean isMoving, isJailed, isJailBroken, hasFlag, isInvulnerable, isDashing, isDodging, isGameFinished = false;
	
	GameClock clock;
	int xPos;
	int yPos;
	int speed;
	char direction;
	char playerID;
	
	//Controls
	char keyStop;
	char keyStop2;
	char keyStop3;
	char keyWalkUp;
	char keyWalkDown;
	char keyWalkLeft;
	char keyWalkRight;
	char keyTag;
	char keyDash;
	char keyDodge;
	
	int dashUseTime = 0;
	int dodgeUseTime = 0;
	int jailReleaseTime = 0;
	int jailBrokenEndTime = 0;
	
	//Coordinate storage arrays
	static int p1Side[] = new int[2];
	static int p2Side[] = new int[2];
	static int noMansLand[] = new int[2];
	//Elements 0, 1, 2, 3, 4, and 5 are x, y, xDist, yDist, centerX, centerY (all are rectangles)
	static int p1FlagCoords[] = {64, 576, 63, 63};
	//opponent flag area
	static int p2FlagCoords[] = {896, 128, 63, 63};
	//center of this area is x+16, y+16
	//friendly jail area
	static int p1JailCoords[] = {128, 128, 63, 63, 160, 160};
	//opponent jail area
	static int p2JailCoords[] = {832, 576, 63, 63, 864, 608};
	
	//Make obstacle list
	
	
	//Class constructor
	public Player(char playerIDIn, String difficulty, GameClock clockIn) { 
		playerID = playerIDIn;
		clock = clockIn;
		//Coordinates of sides depending on level difficulty
		//No man's land is larger with increasing difficulty
		if (difficulty.equals("easy")){
			p1Side[0] = 0;
			p1Side[1] = 383;
			noMansLand[0] = 384;
			noMansLand[1] = 639;
			p2Side[0] = 640;
			p2Side[1] = 1023;
		}
		else if (difficulty.equals("medium")){
			p1Side[0] = 0;
			p1Side[1] = 351;
			noMansLand[0] = 352;
			noMansLand[1] = 671;
			p2Side[0] = 672;
			p2Side[1] = 1023;
		}
		else{	//hard
			p1Side[0] = 0;
			p1Side[1] = 319;
			noMansLand[0] = 320;
			noMansLand[1] = 671;
			p2Side[0] = 672;
			p2Side[1] = 1023;
		}
		if (playerID == '1'){
			direction = 'r';
			xPos = 576;
			yPos = 576;
			
			keyStop = 'e';
			keyStop2 = 'q';
			keyStop3 = ' ';
			keyWalkUp = 'w';
			keyWalkDown = 's';
			keyWalkLeft = 'a';
			keyWalkRight = 'd';
			keyTag = 'g';
			keyDash = 'h';
			keyDodge = 'j';
			
		}
		else if (playerID == 2){
			direction = 'l';
			xPos = 832;
			yPos = 192;
			
			keyStop = '9';
			keyStop2 = '7';
			keyStop3 = '0';
			keyWalkUp = '8';
			keyWalkDown = '5';
			keyWalkLeft = '4';
			keyWalkRight = '6';
			keyTag = 'p';
			keyDash = '[';
			keyDodge = ']';
			
		}
	}
	
	//Controls all ability cooldown timers, player statuses, and 
	//Takes user input and converts it into whatever move is required of the player
	public boolean calculateMove(char inputKey){
		//Reset all abilities: they last for 1 second
		if (isDashing && clock.time >= dashUseTime + 2) isDashing = false;
		if (isDodging && clock.time >= dodgeUseTime + 2) isDodging = false;
		if (isJailed && clock.time >= jailReleaseTime) {
			isJailed = false;
			isJailBroken = true;
			jailBrokenEndTime = clock.time + 5;
		}
		if (isJailBroken && clock.time >= jailBrokenEndTime) isJailBroken = false;
		//Check if can take opponent flag
		if (!isJailBroken && ((playerID == '1' && xPos > p2FlagCoords[0] && xPos < p2FlagCoords[2] && yPos > p2FlagCoords[1] && yPos > p2FlagCoords[3])
			|| (playerID == '2' && xPos > p1FlagCoords[0] && xPos < p1FlagCoords[2] && yPos > p1FlagCoords[1] && yPos > p1FlagCoords[3])))
			hasFlag = true;
		//Check if can capture flag
		if (hasFlag && ((playerID == '1' && xPos > p1FlagCoords[0] && xPos < p1FlagCoords[2] && yPos > p1FlagCoords[1] && yPos > p1FlagCoords[3])
			|| (playerID == '2' && xPos > p2FlagCoords[0] && xPos < p2FlagCoords[2] && yPos > p2FlagCoords[1] && yPos > p2FlagCoords[3]))){
			hasFlag = false;
			captures++;
			//Print captured flag message
		}
			
		if (!isJailed){
			//tag
			if (inputKey == keyTag) return true;
			//dash
			if (inputKey == keyDash && clock.time >= dashUseTime + 10){
				dashUseTime = clock.time;
				isDashing = true;
			}
			//Dodge
			if (inputKey == keyDodge && clock.time >= dodgeUseTime + 10){
				dodgeUseTime = clock.time;
				isDodging = true;
			}
			
			//Direction
			if (inputKey == keyStop || inputKey == keyStop2 || inputKey == keyStop3) isMoving = false; 
			if (inputKey == keyWalkUp || inputKey == keyWalkDown || inputKey == keyWalkLeft || inputKey == keyWalkRight){
				isMoving = true;
				if (inputKey == keyWalkUp) direction = 'N';
				else if (inputKey == keyWalkDown) direction = 'S';
				else if (inputKey == keyWalkLeft) direction = 'W';
				else if (inputKey == keyWalkRight) direction = 'E';
			}
			//Speed
			/*if (!isMoving) speed = 0;
			else if (isDashing && hasFlag) speed = startSpeed*2-2;
			else if (isDashing) speed = startSpeed*2;
			else if (hasFlag) speed = startSpeed-1;
			else speed = startSpeed;
			*/
			if (!isMoving) speed = 0;
			else if (isDashing) speed = 2;
			else speed = 1;
			
			//Movement
			move();
		}
		else{
			
		}
		return false;
	}
	
	//changes position of player
	private void move(){
		if (direction == 'N') yPos -= speed;
		else if (direction == 'S') yPos += speed;
		else if (direction == 'W') xPos -= speed;
		else if (direction == 'E') xPos += speed;
		//Make restrictions on x/y values and obstacles
		if (xPos < 0) xPos = 0;
		else if (xPos > 1023) xPos = 1023;
		if (yPos < 0) yPos = 0;
		else if (yPos > 1023) yPos = 767;
	}
	
	//Player tags target player
	public void tag(Player p){
		boolean isInRange;
		//Calculate distance between this player and target player: tag can succeed if within 10 pixels
		if (!((p.playerID == '1' && p.xPos > p1Side[1]) || (p.playerID == '2' && p.xPos < p2Side[0])) && !p.isJailBroken && !p.isJailed){
			isInRange = (Math.sqrt(Math.pow(this.xPos - p.xPos, 2) + Math.pow(this.yPos - p.yPos, 2)) <= 10);
			if (!isInRange){
				//Print a "MISS!" message
			}
			else if(!p.isDodging){
				p.goToJail();
			}
			else{
				int rng = (int)(2*Math.random() - 1);
				if (rng == 1) p.goToJail();
				else{
					//Print a "DODGED!" message
				}
			}
		}
		
	}
	
	//Activated if player is tagged successfully
	public void goToJail(){
		isJailed = true;
		hasFlag = false;
		jailReleaseTime = clock.time + 10;
		if (playerID == '1'){
			xPos = p2JailCoords[4];
			yPos = p2JailCoords[5];
		}
		else if (playerID == '2'){
			xPos = p1JailCoords[4];
			yPos = p1JailCoords[5];
		}
	}
}
