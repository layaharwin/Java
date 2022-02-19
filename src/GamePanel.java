
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH=600;
    static final int SCREEN_HEIGHT=600;
    //size of objects in screen
    static final int UNIT_SIZE=25;
    //calculate how many objects we can fit in the screen
    static final int GAME_UNITS=(SCREEN_WIDTH*SCREEN_WIDTH)/UNIT_SIZE;
    //to create a delay -> HIGHER THE NUMBER SLOWER THE GAME
    static final int DELAY= 75;
    //two arrays to hold all the coordinates for all the body parts of the snake including the head of the snake
    final int x[]=new int [GAME_UNITS]; //GAME_UNITS because the size of the snake will not be bigger than the screem
    final int y[]=new int [GAME_UNITS]; // x for x coordinates and y for y coordinates
    //set the initial size or body parts of the snake as 6
    int bodyParts=6;
    int applesEaten;//Initially it will be zero
    int appleX;//x coordinate of the apple
    int appleY;//y coordinate of the apple
    //set the direction of where the snake is heading
    //R for right, L for left, U for up and D for down
    char direction='R';
    boolean running = false; //setting it to false as the game begins
    Timer timer;
    Random random;
    	
	GamePanel(){		
		random=new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT)); // setting screen size of the game
        this.setBackground(Color.GREEN);// setting background color as black
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	//to show the GUI 
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		if(running) {
			/*
			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			*/
			//color of apple
			g.setColor(Color.RED); // set color of graphics g as red
			//shape of apple
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); // takes coordinates of x and y as first two arguments and sizes for the next two
		
			//setting shape and color for snake
			for(int i = 0; i< bodyParts;i++) {
				if(i == 0) { // when i=0, then its the head of the snake
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else { // body of the snake
					//g.setColor(new Color(45,180,0));
					g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}			
			}
			g.setColor(Color.BLUE);
			g.setFont( new Font("Ink Free",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
		
	}
	//function to generate coordinates of new apple 
	//any time we begin the same or score a point or eat an apple
	public void newApple(){
		// x and y coordinates of apple
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	// function to move the snake
	public void move(){
		//iterating through the bodyparts to shift them
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		// switch to change the direction of where snake is heading
		//examine the direction variable
		switch(direction) {
		case 'U'://up
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D'://down
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L'://left
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R'://right
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		
	}
	//whenever snake eats apple, it scores a point according to the game rulebook
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;// to increase size of the snake
			applesEaten++;// to keep score count
			newApple();//get new apple
		}
	}
	//checking different collision cases
	public void checkCollisions() {
		//checks if head collides with body
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i])&& (y[0] == y[i])) {
				running = false;
			}
		}
		//check if head touches left border
		if(x[0] < 0) {
			running = false;
		}
		//check if head touches right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		//check if head touches top border
		if(y[0] < 0) {
			running = false;
		}
		//check if head touches bottom border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop(); 
		}
	}
	public void gameOver(Graphics g) {
		//Score
		g.setColor(Color.BLUE);
		g.setFont( new Font("Ink Free",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		//Game Over text
		g.setColor(Color.BLUE);
		g.setFont( new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	// function to perform actions in order 
	// so that it works autonomous until an exit strategy
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move(); //move the snake
			checkApple();//check if ran into apple
			checkCollisions();// check if collided with walls
		}
		repaint();// when game is no longer running
	}
	
	// to control the direction of snake by giving user input
	// user inputs are up arrow, down arrow, right arrow and left arrow
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {// e is the user input
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT: // left arrow
				if(direction != 'R') {// not to collide with each other R direction is disabled and moved left for all other cases
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:// right arrow
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:// up arrow
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:// down arrow
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
}