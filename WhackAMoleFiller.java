// Help from Mr. Friedman and Youtuber for Progress Bar
// Special Feature of different levels and color changing screen with progress bar

// Filler code for Whack a Mole by Mr. Friedman

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.Timer;

public class WhackAMoleFiller {

	// size of the display area
    private int windowWidth = 800, windowHeight = 600, textHeight = 35; 
    
    // variable for your "mole" image - do you need more?
    private Image skunkTarget, backyardBackground, trashClose, trashOpen;

    // constants for the number of moles, number of moles appearing at any time, and the 
    // time gap between new moles popping up (in milliseconds)
    private final int NUMMOLES = 5, NUMAPPEARING = 2; 
    int TIMEGAP, PERCENTTIMEGAP;
    
    Timer timer, percentTimer;

    //score keeper
    private int score = 0;
    
    // locations of each of the trash cans and skunks
	int[] targetX = new int [NUMMOLES];
	int[] targetY = new int [NUMMOLES];
	int[] trashX = new int [NUMMOLES];
	int[] trashY = new int [NUMMOLES];
	
	// the velocity of skunk out of trash can movement
	int[] popUpVel = new int [NUMMOLES];
	
	// to allow showing, movement, or indicate whether skunk was clicked
	boolean[] showSkunk = new boolean [NUMMOLES];
	boolean[] skunkMovement = new boolean [NUMMOLES];
	boolean[] clicked = new boolean [NUMMOLES];
	
	// indicating size of target and subject
	int skunkSize = 75, trashWidth = 100, trashHeight = 120;
	
	// to allow space for the skunk head to "pop up"
	int skunkUnderTrash = 50, skunkHeadUp = 20;
	int randomSkunkUp;
	
	// variable to show how opaque screen is
	int opacity;
	int amountTint = 10;
    
	// variable for the progress bar
	int percentNumber;
	JProgressBar progressBar;
	int percentMinimum = 0, percentMaximum = 100, progressBarWidth = 160, progressBarHeight = 100, unitProgressBar = 100;
	
	// determining what screen shows up and at what time
	boolean endGameWin, endGameLoss, menuOn;
	// for visual aesthetics
	int endTextLength = 150, startTextLength = 50;
	
	// setting font sizes, placement of words and rectangles
	int menuFont = 10;
	int levelButtonWidth = 50, levelButtonHeight = 30;
	int easyX = windowWidth/3, mediumX = easyX + 100, difficultX = mediumX + 100;
	int levelsY = windowHeight - 200;
	int menuDescriptionX = windowWidth/2 - 150, menuDescriptionY = windowHeight/2 + 200;
	
    
    public void setup() {
    	for (int i = 0; i < NUMMOLES; i++) {
    		//random y value for dirt mounds in range(allowing multiple targets to have the same y value)
    		trashY[i] = (int)(Math.random() * (windowHeight - 2 * (trashHeight + textHeight))) + (trashHeight + textHeight);
		
			//putting the dirt mounds in random X positions in range
    		trashX[i] = (int)(Math.random() * (windowWidth - 2 * trashWidth)) + trashWidth;
		
    		//basing mole location from dirt mounds
    		targetX[i] = trashX[i];
    		targetY[i] = trashY[i] + skunkUnderTrash;
			
			//intializing the score
			score = 0;
			
			// declares that we have not clicked any skunks yet
			clicked[i] = false;
			
			//keeps screen transparent to start with
			percentNumber = 0;
			
			// game is not over
			endGameWin = false;
			endGameLoss = false;
			
			//turn menu on
			menuOn = true;
    	}
    	
    	// loads your 3 images - all you need to do is match file names.
		try {
			skunkTarget = ImageIO.read(new File("skunkTarget.png"));
			backyardBackground = ImageIO.read(new File("backyardBackground.png"));
			trashClose = ImageIO.read(new File("trashClose-removebg-preview.png"));
			trashOpen = ImageIO.read(new File("trashOpen-removebg-preview.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    //method for mole movement
    public void popUp () {
    	for (int i = 0; i < NUMMOLES; i++) {
    		//only move when skunk is on the screen
    		if (showSkunk[i] == true) {
    			targetY[i] -= popUpVel[i];	
    			//stops movement once above the dirt mound
    			if (targetY[i] <= (trashY[i]) - skunkHeadUp) {
    				popUpVel[i] = 0;
    			}
    		}
    	}
    }
    
    public void draw(Graphics g) {
    	
    	//drawing background image
    	g.drawImage(backyardBackground, 0,  0, windowWidth, windowHeight, null);
    	
        for (int i = 0; i < NUMMOLES; i++) {
        	if (showSkunk[i] == true) {
            	//moles for the game
            	g.drawImage(skunkTarget, targetX[i], targetY[i], skunkSize, skunkSize, null);
            	//declares pop up method 
				popUp();
        	}

        	//the dirt mounds as trash cans
        	g.drawImage(trashClose, trashX[i], trashY[i], trashWidth, trashHeight, null);
        }      
		
        // base the tint of the screen based on the progress bar
		opacity = (percentNumber * 255) / 100;
		
        //tinting the screen with a rectangle
        g.setColor(new Color(0, 255, 0, opacity));
        g.fillRect(0, 0, windowWidth, windowHeight);
        
        // determine which screens to show
        if (menuOn == true) {
        	drawStartMenu(g);
        }
        else if (endGameWin == true) {
			endScreenWin(g);
        }
        else if (endGameLoss == true) {
        	endScreenLoss(g);
        }
       
    }
    
	public void drawStartMenu(Graphics g) {
		//Whack-A-Mole Start Page Introduction
    	g.setColor(Color.black);
    	g.fillRect(0, 0, windowWidth, windowHeight); 
    	
		g.setColor(Color.white);
		g.drawString("Whack-A-Mole", windowWidth/2 - startTextLength, windowHeight/2);
		
		//Description of Whack-a-Mole
		g.setColor(Color.white);
		g.drawString("Hit the mole to get points! Get to 20 to Win.", menuDescriptionX, menuDescriptionY);
		
		// Rectangles for Buttons
		g.setColor(Color.white);
		g.fillRect(easyX, levelsY, levelButtonWidth, levelButtonHeight);
		g.drawString("Easy", easyX, levelsY - startTextLength/2);
		
		g.setColor(Color.white);
		g.fillRect(mediumX, levelsY, levelButtonWidth, levelButtonHeight);
		g.drawString("Medium", mediumX, levelsY - startTextLength/2);
		
		g.setColor(Color.white);
		g.fillRect(difficultX, levelsY, levelButtonWidth, levelButtonHeight);
		g.drawString("Difficult", difficultX, levelsY - startTextLength/2);
	}
	
	//drawing end screen if get 20 points
    public void endScreenWin (Graphics g) {
    	g.setColor(Color.black);
    	g.fillRect(0, 0, windowWidth, windowHeight); 
    	
    	g.setColor(Color.white);
		g.drawString("Good Job You Passed! Press Reset to Return to Menu Screen.", windowWidth/2 - endTextLength, windowHeight/2);
    }
    
    // drawing losing end screen
    public void endScreenLoss (Graphics g) {
    	g.setColor(Color.black);
    	g.fillRect(0, 0, windowWidth, windowHeight); 
    	
    	g.setColor(Color.white);
		g.drawString("Sorry, You Lost! Press Reset to Return to Menu Screen.", windowWidth/2 - endTextLength, windowHeight/2);
    }
    
    public void easyMode() {
    	//turns off menu screen to show game
    	menuOn = false;
    	
    	for (int i = 0; i < NUMMOLES; i++) {
    		popUpVel[i] = 5;
    	}
    	
		TIMEGAP = 2000;
		PERCENTTIMEGAP = 150;
		
		timer.setDelay(TIMEGAP);
		percentTimer.setDelay(PERCENTTIMEGAP);
    }
    
    public void mediumMode() {
    	menuOn = false;
    	
    	for (int i = 0; i < NUMMOLES; i++) {
    		popUpVel[i] = 7;
    	}
    	
		TIMEGAP = 1000;
		PERCENTTIMEGAP = 100;
		
		timer.setDelay(TIMEGAP);
		percentTimer.setDelay(PERCENTTIMEGAP);
    }
    
    public void difficultMode() {
    	menuOn = false;
    	
    	for (int i = 0; i < NUMMOLES; i++) {
    		popUpVel[i] = 10;
    	}
    	
		TIMEGAP = 500;
		PERCENTTIMEGAP = 50;
		
		timer.setDelay(TIMEGAP);
		percentTimer.setDelay(PERCENTTIMEGAP);
    }
    

    // what you want to happen when the mouse is clicked
    public void click(int mouseX, int mouseY) {
    	//make local variables
    	int mousePressX = mouseX;
    	int mousePressY = mouseY;
    	
    	// to choose level when pressedd
    	if (menuOn == true) {
    		if ((mousePressX >= easyX) && (mousePressX <= easyX + levelButtonWidth) && (mousePressY >= levelsY) && (mousePressY <= levelsY + levelButtonHeight)) {
    			easyMode();
    		}
    		else if ((mousePressX >= mediumX) && (mousePressX <= mediumX + levelButtonWidth) && (mousePressY >= levelsY) && (mousePressY <= levelsY + levelButtonHeight)) {
    			mediumMode();
    		}
    		else if ((mousePressX >= difficultX) && (mousePressX <= difficultX + levelButtonWidth) && (mousePressY >= levelsY) && (mousePressY <= levelsY + levelButtonHeight)) {
    			difficultMode();
    		}
    	}
    	else {
        	//makes a rectangle around the skunk to check if clicked
        	for (int i = 0; i < NUMMOLES; i++) {
        		if ((showSkunk[i] == true) && (endGameWin == false) && (endGameLoss == false) && ((mousePressX < (targetX[i] + skunkSize) && mousePressX > (targetX[i] - skunkSize/2)) && (mousePressY < (targetY[i] + skunkSize) && mousePressY > (targetY[i] - skunkSize/2)))) {
        			showSkunk[i] = false;
        			score++;
        			// show program that we have clicked that specific mole
        			clicked[i] = true;
        		}
        	}
        	
        	//when score is 20, win the game
        	if (score == 20) {
        		endGameWin = true;
        	}
    	}
    }
    
    // what you want to happen when the time for the current round ends
    public void timeAdvance() {
		for (int i = 0; i < NUMMOLES; i++) {
			//make all skunks showing false
			showSkunk[i] = false;
			
			//reset the skunk so it can pop-up again
			popUpVel[i] = 5;
			targetY[i] = trashY[i] + skunkUnderTrash;
		}
    	
		int a = 0;
		//randomize which moles come up
		while (a < NUMAPPEARING) {
			//finding random skunk to pop up
			randomSkunkUp = (int)(Math.random() * NUMMOLES + 0);
			//check if it is used or not
			if (showSkunk[randomSkunkUp] == false) {
				showSkunk[randomSkunkUp] = true;
				a++;
			}
		}
    }
    
    // reset the game by setting up again with menu screen (molehills relocate, score, progress bar reset)
    public void reset() {
    	setup();
    }
    
    // method to allow progress bar number to change
    public void progressBarMovement () {
    	progressBar.setValue(percentNumber);
    	// when nothing is pressed, percent number still increases except when end game
    	if (endGameWin == false && endGameLoss == false && menuOn == false) {
        	percentNumber += 1;
        	
        	// allows the click to "un"-tint the screen
        	for (int i = 0; i < NUMMOLES; i ++) {
            	if (clicked[i] == true && percentNumber > amountTint) {
            		percentNumber -= amountTint;
            		clicked[i] = false;
        		}
        	}
        	
        	//lose when tint is fully implemented 
        	if (percentNumber == 100) {
        		endGameLoss = true;
        	}
    	}
    }

    // DO NOT TOUCH BELOW CODE //

    public WhackAMoleFiller() {
    	
    	setup();

        JFrame window = new JFrame();
        window.setTitle("Whack A Mole");
        window.setSize(windowWidth, windowHeight + textHeight);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JTextArea scoreDisplay = new JTextArea();
        scoreDisplay.setEditable(false);
        scoreDisplay.setText("\t\tScore: 0");
        
        JTextArea skunkOdor = new JTextArea();
        skunkOdor.setEditable(false);
        skunkOdor.setText("Amount of Skunk Odor");
        
        //making reset button and running code
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
                window.getContentPane().repaint();

			}
        });

        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(windowWidth, textHeight));
        topPanel.add(resetButton);
        scoreDisplay.setBackground(topPanel.getBackground());
        
        topPanel.add(scoreDisplay);

		progressBar = new JProgressBar(percentMinimum, percentMaximum);
		
		progressBar.setBounds(unitProgressBar, unitProgressBar, progressBarWidth, progressBarHeight);
		
		//show current value in progress bar
		progressBar.setStringPainted(true);
		progressBar.setValue(percentMinimum);
		
		topPanel.add(progressBar);
        topPanel.add(skunkOdor);
        
        skunkOdor.setBackground(topPanel.getBackground());

        JPanel canvas = new JPanel() {
            public void paint(Graphics g) {
                draw(g);
                scoreDisplay.setText("\t\tScore: " + score);
            }
        };
        canvas.setPreferredSize(new Dimension(windowWidth, windowHeight));

        canvas.addMouseListener(new MouseListener() {

            @Override
            public void mousePressed(MouseEvent e) {
                click(e.getX(), e.getY());
            }

            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
        
        window.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                windowWidth = window.getWidth();
                windowHeight = window.getHeight() - textHeight;
                topPanel.setPreferredSize(new Dimension(windowWidth, textHeight));
                canvas.setPreferredSize(new Dimension(windowWidth, windowHeight));
            }
        });

        container.add(topPanel);
        container.add(canvas);
        window.add(container);
 
        
        window.setVisible(true);
        canvas.revalidate();
        
        timer = new Timer(TIMEGAP, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            	timeAdvance();
            }
        });
        timer.start();
        
        // new timer that updates the progress bar
        percentTimer = new Timer(PERCENTTIMEGAP, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            	progressBarMovement();
            }
        });
        percentTimer.start(); 
        

        
        try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        while (true) {
        	window.getContentPane().repaint();
            try {
    			Thread.sleep(20);
    		} catch (InterruptedException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
        }
    }
    
    
    public static void main(String[] args) {
        new WhackAMoleFiller();
    }
}
