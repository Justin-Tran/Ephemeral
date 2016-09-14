/* Ephemeral --- 'Eff fem urr ul'
 * Created by Justin Tran on 11/19/14
 * Dimensions: Scale 9 - 1440W x 810H
 * Updates:
 * - Shift allows you to become Ephemeral
 * - Imported images
 * - Made enemy be able to follow Character
 * - Made knockback effect
 * - Added Stock and knockback affected by % damage
 * - Added double jump feature
*/
package Ephemeral;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class EphemeralMain extends Canvas implements Runnable, ActionListener, KeyListener {
	
	private static final long serialVersionUID = 1L;
	
	public static final int SCALE = 9;
	public static final int WIDTH = SCALE * 160;
	public static final int HEIGHT = WIDTH/16*9;
	public static int xChar = WIDTH/2+200;
	public static int yChar = HEIGHT/2;
	public static int charSpeed = 1;
	public static int xSpirit = xChar;
	public static int ySpirit = yChar;
	public static int spiritSpeed = 3;
	public static int jumpCount = 0;
	public static int reapCount = 0;
	public static int charStock = 3;
	public static int eSpeed = 1; // DIFFICULTY --- 1: Hard, 2: Very Hard, 3: You're our of your mind, 4: Oh Dear God
	public static int knockbackCount = 0;
	
	public static double gravity = -3.0;
	public static double charDamage = 0;
	
	private boolean running = false;
	public static boolean pressedW = false;
	public static boolean pressedA = false;
	public static boolean pressedS = false;
	public static boolean pressedD = false;
	public static boolean pressedSpiritW = false;
	public static boolean pressedSpiritA = false;
	public static boolean pressedSpiritS = false;
	public static boolean pressedSpiritD = false;
	public static boolean pressedShift = false;
	public static boolean ephemeral = false;
	public static boolean faceRight = false;
	public static boolean faceLeft = true;
	public static boolean grounded = false;
	public static boolean jumping = false;
	public static boolean jumping2 = false;
	public static boolean jumped2 = false;
	public static boolean reap = false;
	public static boolean knockback = false;
	public static boolean paused = true;
	
	public static BufferedImage background;
	public static BufferedImage stage;
	public static BufferedImage charLeft;
	public static BufferedImage charRight;
	public static BufferedImage charLeftAttack1;
	public static BufferedImage charRightAttack1;
	public static BufferedImage charLeftAttack2;
	public static BufferedImage charRightAttack2;
	public static BufferedImage charLeftAttack3;
	public static BufferedImage charRightAttack3;
	public static BufferedImage enemyLeft;
	public static BufferedImage enemyRight;
	
	public static ArrayList <Enemy> enemies = new ArrayList <Enemy> ();
	
	public static void main(String args[]) {
		JFrame frame = new JFrame("Ephemeral");
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null); //puts window in the middle of the screen
		frame.setResizable(false); //user cannot resize window
		frame.setLayout(null);
		
		importImages();
		
		JMenuBar  bar = new JMenuBar();
		bar.setBounds(0, 0, WIDTH, 25);
		
		JMenu menu = new JMenu("Menu");
		menu.setBounds(0, 0, 45, 24);
		
		JMenuItem newGame = new JMenuItem("New Game");
		newGame.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//Code for New Game
				for(Enemy en: enemies){
					en.stock = 3;
					en.damage = 0;
					en.xEnemy = WIDTH/2-200;
					en.yEnemy = HEIGHT/2;
				}
				charDamage = 0;
				charStock = 3;
				xChar = WIDTH/2+200;
				yChar = HEIGHT/2;
				paused = true;
				System.out.println("Starting New Game...");
			}
		});
		
		JMenuItem howToPlay = new JMenuItem("How To Play");
		howToPlay.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				final JFrame alert = new JFrame("How To Play");
				alert.setSize(250, 300);
				alert.setLayout(null);
				alert.setLocationRelativeTo(null);
				
				JLabel directions = new JLabel("             Add Instructions Here Later");
				directions.setBounds(0, 0, 200, 50);
				
				JButton okay = new JButton("Okay");
				okay.setBounds(75, 230, 100, 30);
				okay.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						alert.dispose();
					}
				});
				
				alert.add(directions);
				alert.add(okay);
				alert.setResizable(false);
				alert.setVisible(true);
			}
		});

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println("Closing Game...");
				System.exit(0);
			}
		});
		
		EphemeralMain em = new EphemeralMain();
		em.setBounds(0, 25, WIDTH, HEIGHT - 25);
		//Make Players 2, 3, and 4
		enemies.add(new Enemy(WIDTH/2-200, HEIGHT/2, 0, 0, 3, 0, false)); //Player 2
		//enemies.add(new Enemy(WIDTH/2-400, HEIGHT/2, 0, 0, 3, 0, false)); //Player 3
		//enemies.add(new Enemy(WIDTH/2+400, HEIGHT/2, 0, 0, 3, 0, false)); //Player 4		
		frame.add(em);
		menu.add(newGame);
		menu.add(howToPlay);
		menu.add(exit);
		bar.add(menu);
		frame.add(bar);
		frame.setVisible(true);
		em.start();
	}
	
	public static void importImages(){
		try 
	    {background = ImageIO.read(new File("C:\\Users\\trand_000\\Desktop\\Justin's Files\\ComSci\\Personal Projects\\Games\\Ephemeral\\res\\volcanoBG.gif"));
	     stage = ImageIO.read(new File("C:\\Users\\trand_000\\Desktop\\Justin's Files\\ComSci\\Personal Projects\\Games\\Ephemeral\\res\\finalStage.png"));
		 charLeft = ImageIO.read(new File("C:\\Users\\trand_000\\Desktop\\Justin's Files\\ComSci\\Personal Projects\\Games\\Ephemeral\\res\\CharLeft.png"));
	     charRight = ImageIO.read(new File("C:\\Users\\trand_000\\Desktop\\Justin's Files\\ComSci\\Personal Projects\\Games\\Ephemeral\\res\\CharRight.png"));
	     charLeftAttack1 = ImageIO.read(new File("C:\\Users\\trand_000\\Desktop\\Justin's Files\\ComSci\\Personal Projects\\Games\\Ephemeral\\res\\CharLeftAttack1.png"));
	     charRightAttack1 = ImageIO.read(new File("C:\\Users\\trand_000\\Desktop\\Justin's Files\\ComSci\\Personal Projects\\Games\\Ephemeral\\res\\CharRightAttack1.png"));
	     charLeftAttack2 = ImageIO.read(new File("C:\\Users\\trand_000\\Desktop\\Justin's Files\\ComSci\\Personal Projects\\Games\\Ephemeral\\res\\CharLeftAttack3.png"));
	     charRightAttack2 = ImageIO.read(new File("C:\\Users\\trand_000\\Desktop\\Justin's Files\\ComSci\\Personal Projects\\Games\\Ephemeral\\res\\CharRightAttack3.png"));
	     charLeftAttack3 = ImageIO.read(new File("C:\\Users\\trand_000\\Desktop\\Justin's Files\\ComSci\\Personal Projects\\Games\\Ephemeral\\res\\CharLeftAttack5.png"));
	     charRightAttack3 = ImageIO.read(new File("C:\\Users\\trand_000\\Desktop\\Justin's Files\\ComSci\\Personal Projects\\Games\\Ephemeral\\res\\CharRightAttack5.png"));
	     enemyLeft = ImageIO.read(new File("C:\\Users\\trand_000\\Desktop\\Justin's Files\\ComSci\\Personal Projects\\Games\\Ephemeral\\res\\wraithLeft.png"));
	     enemyRight = ImageIO.read(new File("C:\\Users\\trand_000\\Desktop\\Justin's Files\\ComSci\\Personal Projects\\Games\\Ephemeral\\res\\wraithRight.png"));} 
	    catch (IOException e){}
	}
	
	public synchronized void start() {
		Thread t = new Thread(this);
		t.setPriority(Thread.MAX_PRIORITY);
		t.start(); //will call run method of w/e you passed into thread
	}
	
	public void run() {
		running = true;
		addKeyListener(this);
		while(running){
			update();
			BufferStrategy buf = getBufferStrategy();
			if(buf == null) {
				createBufferStrategy(3); //3 buffers per render
				continue;
			}
			Graphics2D g = (Graphics2D)buf.getDrawGraphics();
			render(g);
			buf.show();
		}
	}
	
	public void update() {
		if(jumping){
			yChar -= 2;
			jumpCount++;
			if(jumpCount >= 60){
				jumpCount = 0;
				jumping = false;
			}
		}
		if(jumping2){
			yChar -= 2;
			jumpCount++;
			if(jumpCount >= 60){
				jumpCount = 0;
				jumping2 = false;
			}
		}
		
		if(pressedA) xChar -= charSpeed;
		if(pressedS) yChar += charSpeed;
		if(pressedD) xChar += charSpeed;
		if(!ephemeral){
			xSpirit = xChar;
			ySpirit = yChar;
		}
		
		if(pressedSpiritW) ySpirit -= spiritSpeed;
		if(pressedSpiritA) xSpirit -= spiritSpeed;
		if(pressedSpiritS) ySpirit += spiritSpeed;
		if(pressedSpiritD) xSpirit += spiritSpeed;
		
		//if distance is > some #, teleport to spirit	
		if(Math.sqrt(Math.pow(xSpirit-xChar, 2) + Math.pow(ySpirit-yChar, 2)) > 300){
			xChar = xSpirit;
			yChar = ySpirit;
			ephemeral = false;
			grounded = false;
		}
		
		if(!jumping && !jumping2) yChar -= gravity; //applies gravity to character
	}
	
	public void render(Graphics2D g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		drawStageAndBG(g);
		checkCollisions();
		
		if(ephemeral) drawSpiritCharacter(g, xSpirit, ySpirit);
		
		if(reap){
			reapCount++;
			characterReap(g);
			if(reapCount >= 30){
				reapCount = 0;
				reap = false;
			}
		}
		else
			drawCharacter(g, xChar, yChar);
		
		drawEnemy(g);
		drawDmgAndStock(g);
	}
	
	public void checkCollisions(){
		//Checking Character Collision with Stage
		if(yChar+20 >= 500 && yChar-20 <= 550 && xChar+20 >= 270 && xChar-20 <= 1170){
			yChar = 500-20;
			grounded = true;
		}
		else
			grounded = false;
		//Removes Stock if Out of Bounds for Character
		if(xChar > WIDTH+100 || xChar < 0-100 || yChar > HEIGHT+100 || yChar < 0-100){
			charStock--;
			charDamage = 0;
			xChar = xSpirit = WIDTH/2;
			yChar = ySpirit = HEIGHT/2;
		}
		//Checking Enemy Collision With Stage and Character
		for(Enemy e: enemies){
			//Removes Stock if Out of Bounds for Enemy
			if(e.xEnemy > WIDTH+100 || e.xEnemy < 0-100 || e.yEnemy > HEIGHT+100 || e.yEnemy < 0-100){
				e.stock--;
				e.damage = 0;
				e.xEnemy = WIDTH/2 - 300;
				e.yEnemy = HEIGHT/2;
			}
			//Keeps enemy grounded
			if(e.yEnemy+20 >= 500 && e.yEnemy-20 <= 550 && e.xEnemy+20 >= 270 && e.xEnemy-20 <= 1170){
				e.yEnemy = 500-20;
			}
			
			//Checking Enemy Collision with Character and Knockback
			if(e.yEnemy > yChar-40 && e.yEnemy < yChar+40 && e.xEnemy < xChar+40 && e.xEnemy > xChar-40){
				if(!knockback)
					charDamage += 0.25;
				knockback = true;
			}
			
			//Applies Knockback effect if hit to Character
			if(knockback){
				knockbackCount++;
				if(e.xEnemy > xChar) //from right
					xChar -= 1.0*charDamage;
				if(e.xEnemy <= xChar) //from left
					xChar += 1.0*charDamage;
				if(knockbackCount > 50){
					knockbackCount = 0;
					knockback = false;
				}
			}
			
			//Checking Reap Collision with Enemy
			if(reap){
				if(faceRight){
					if(e.yEnemy > yChar-40 && e.yEnemy < yChar+40 && e.xEnemy < xChar+70 && e.xEnemy > xChar+20){ //Right Reap
						if(!e.knockback)
							e.damage += 0.25;
						e.knockback = true;
					}
				}
				if(faceLeft){
					if(e.yEnemy > yChar-40 && e.yEnemy < yChar+40 && e.xEnemy < xChar-20 && e.xEnemy > xChar-70){ //Left Reap
						if(!e.knockback)
							e.damage += 0.25;
						e.knockback = true;
					}
				}
			}
			
			//Checking Bullet Collision with Character
			if(e.yBullet+10 > yChar-40 && e.yBullet-10 < yChar+15 && e.xBullet-10 < xChar+30 && e.xBullet+10 > xChar-25){
				if(!knockback)
					charDamage += 0.25;
				knockback = true;
				e.quad = 0;
				e.xBullet = e.xEnemy;
				e.yBullet = e.yEnemy;
			}
			
			//Applies Knockback effect if hit to Enemy
			if(e.knockback){
				e.knockbackCount++;
				if(e.xEnemy <= xChar) //from right
					e.xEnemy -= 1.0*e.damage;
				if(e.xEnemy > xChar) //from left
					e.xEnemy += 1.0*e.damage;
				if(e.knockbackCount > 50){
					e.knockbackCount = 0;
					e.knockback = false;
				}
			}
		}
	}
	
	public void drawCharacter(Graphics2D g, int x, int y){
		if(faceRight){
			g.drawImage(charRight, x-70, y-60, 140, 140, null);
		}
		if(faceLeft){
			g.drawImage(charLeft, x-70, y-60, 140, 140, null);
		}
	}
	
	public void drawSpiritCharacter(Graphics2D g, int x, int y){
		g.setComposite(AlphaComposite.SrcOver.derive(0.5f)); 
		if(faceRight){
			g.drawImage(charRight, x-70, y-60, 140, 140, null);
		}
		if(faceLeft){
			g.drawImage(charLeft, x-70, y-60, 140, 140, null);
		}
		g.setComposite(AlphaComposite.SrcOver.derive(1.0f));
	}
	
	public void characterReap(Graphics2D g){
		if(reapCount < 10){
			if(faceRight)	g.drawImage(charRightAttack1, xChar-70, yChar-60, 140, 140, null); //Reap Animation 1
			if(faceLeft)	g.drawImage(charLeftAttack1, xChar-70, yChar-60, 140, 140, null);
		}
		else if(reapCount >= 10 && reapCount < 20){
			if(faceRight)	g.drawImage(charRightAttack2, xChar-70, yChar-60, 140, 140, null); //Reap Animation 2
			if(faceLeft)	g.drawImage(charLeftAttack2, xChar-70, yChar-60, 140, 140, null);
		}
		else{
			if(faceRight)	g.drawImage(charRightAttack3, xChar-70, yChar-60, 140, 140, null); //Reap Animation 3
			if(faceLeft)	g.drawImage(charLeftAttack3, xChar-70, yChar-60, 140, 140, null);
		}
	}
	
	public void drawEnemy(Graphics2D g){
		for(Enemy e: enemies){
		
			if(!paused){
				////////////////////////////////////////////////////
				if(e.xEnemy < xChar && e.yEnemy < yChar) // Quad I
				{
					e.xEnemy += eSpeed;
					e.yEnemy += eSpeed;
				}
				if(e.xEnemy > xChar && e.yEnemy < yChar) // Quad II
				{
					e.xEnemy -= eSpeed;
					e.yEnemy += eSpeed;
				}
				if(e.xEnemy > xChar && e.yEnemy > yChar) // Quad III
				{
					e.xEnemy -= eSpeed;
					e.yEnemy -= eSpeed;
				}
				if(e.xEnemy < xChar && e.yEnemy > yChar) // Quad IV
				{
					e.xEnemy += eSpeed;
					e.yEnemy -= eSpeed;
				}
				////////////////////////////////////////////////////
				if(e.xEnemy < xChar && e.yEnemy == yChar) // RIGHT
					e.xEnemy += eSpeed;
				if(e.xEnemy > xChar && e.yEnemy == yChar) // LEFT 
					e.xEnemy -= eSpeed;
				if(e.xEnemy == xChar && e.yEnemy > yChar) // BELOW
					e.yEnemy -= eSpeed;
				if(e.xEnemy == xChar && e.yEnemy < yChar) // ABOVE
					e.yEnemy += eSpeed;			
				////////////////////////////////////////////////////
				// Controls trajectory of enemy bullet
				////////////////////////////////////////////////////
				if(e.quad == 0){
					e.degree = Math.atan((double)(yChar - e.yEnemy)/(xChar - e.xEnemy));
					if(e.xEnemy < xChar && e.yEnemy < yChar) // Quad I
						e.quad = 1;
					if(e.xEnemy > xChar && e.yEnemy < yChar) // Quad II
						e.quad = 2;
					if(e.xEnemy > xChar && e.yEnemy > yChar) // Quad III
						e.quad = 3;
					if(e.xEnemy < xChar && e.yEnemy > yChar) // Quad IV
						e.quad = 4;
					if(e.xEnemy < xChar && e.yEnemy == yChar) // RIGHT - 5
						e.quad = 5;
					if(e.xEnemy > xChar && e.yEnemy == yChar) // LEFT - 6
						e.quad = 6;
					if(e.xEnemy == xChar && e.yEnemy > yChar) // BELOW - 7
						e.quad = 7;
					if(e.xEnemy == xChar && e.yEnemy < yChar) // ABOVE - 8
						e.quad = 8;
				}
				////////////////////////////////////////////////////
				/*if(e.quad == 1){
					e.xBullet += 3*Math.cos(e.degree);
					e.yBullet += 3*Math.sin(e.degree);
				}
				if(e.quad == 2){
					e.xBullet -= 3*Math.cos(e.degree);
					e.yBullet += 3*Math.sin(e.degree);
				}
				if(e.quad == 3){
					e.xBullet -= 3*Math.cos(e.degree);				
					e.yBullet -= 3*Math.sin(e.degree);
				}
				if(e.quad == 4){
					e.xBullet += 3*Math.cos(e.degree);
					e.yBullet -= 3*Math.sin(e.degree);
				}
				if(e.quad == 5)
					e.xBullet += 3*Math.cos(e.degree);
				if(e.quad == 6)
					e.xBullet -= 3*Math.cos(e.degree);
				if(e.quad == 7)
					e.yBullet -= 3*Math.sin(e.degree);
				if(e.quad == 8)
					e.yBullet += 3*Math.sin(e.degree);	
					
				//Checks if bullet is a certain distance away from enemy 
				if(Math.sqrt(Math.pow(e.xBullet-e.xEnemy, 2) + Math.pow(e.yBullet-e.yEnemy, 2)) > 800){
					e.quad = 0;
					e.xBullet = e.xEnemy;
					e.yBullet = e.yEnemy;
				} */
			}
			
//			//Draws the enemy's bullet
//			g.setColor(Color.RED);
//			g.fillOval(e.xBullet-10, e.yBullet-10, 20, 20);
			
			if(e.xEnemy > xChar) //Facing Left
				g.drawImage(enemyLeft, e.xEnemy-25, e.yEnemy-30, 50, 50, null);
			else // Facing Right
				g.drawImage(enemyRight, e.xEnemy-25, e.yEnemy-30, 50, 50, null);
			
			if(paused){ //DRAWS PAUSE ICON
				g.setColor(Color.WHITE);
				g.fillRect(WIDTH/2-150, HEIGHT/2-150, 100, 300);
				g.fillRect(WIDTH/2+50, HEIGHT/2-150, 100, 300);
			}
		}
	}
	
	public void drawDmgAndStock(Graphics2D g){
		//Character Stock & Lives
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 60));
		g.drawString(Integer.toString((int)(charDamage*100)) + "%", 200, 700);
		
		for(int j = 0; j < charStock; j++){
			g.drawImage(charRight, 190 + 50*j, 610, 70, 70, null);
		}
		//Enemy Stock & Lives
		for(Enemy e: enemies){
			g.drawString(Integer.toString((int)(e.damage*100)) + "%", 1050, 700);
			
			for(int j = 0; j < e.stock; j++){
				g.drawImage(enemyRight, 1040 + 50*j, 610, 35, 35, null);
			}
		}
	}
	
	public void drawStageAndBG(Graphics2D g){
		g.drawImage(background, 0, 0, WIDTH, HEIGHT, null); //draws Background
		
		g.drawImage(stage, WIDTH/2 - 480+2, 460, 960, 180, null); //draws Stage
	}
	
	public void keyPressed(KeyEvent e) {
		int keyPressed = e.getKeyCode();
		
		if(!ephemeral){
			if(!paused){
				if(keyPressed==KeyEvent.VK_W) { //UP
					pressedW = true;
					if(grounded){
						jumping = true;
						jumped2 = false;
					}
					grounded = false;
					if(!jumping && !jumping2)
						if(!jumped2){
							jumping2 = true;
							jumped2 = true;
						}
				}
				if(keyPressed==KeyEvent.VK_A) { //LEFT
					pressedA = true;
					faceLeft = true;
					faceRight = false;
				}
				if(keyPressed==KeyEvent.VK_S) { //DOWN
					pressedS = true;
				}
				if(keyPressed==KeyEvent.VK_D) { //RIGHT
					pressedD = true;
					faceRight = true;
					faceLeft = false;
				}
				if(keyPressed==KeyEvent.VK_SHIFT) { //SHIFT
					pressedShift = true;
					ephemeral = true;
				}
				if(keyPressed==KeyEvent.VK_ENTER) { //ENTER - REAP
					reap = true;
				}
			}
			if(keyPressed==KeyEvent.VK_BACK_SPACE) { //BACKSPACE - EMERGENCY TELEPORT TO CENTER SCREEN FOR CHARACTER
				xChar = WIDTH/2;
				yChar = HEIGHT/2;
			}
			if(keyPressed==KeyEvent.VK_P) { //P - PAUSE
				if(!paused){
					paused = true;
					gravity = 0;
				}
				else{
					paused = false;
					gravity = -2.0;
				}
			}
		}
		else{
			if(keyPressed==KeyEvent.VK_W) { //UP
				pressedSpiritW = true;
			}
			if(keyPressed==KeyEvent.VK_A) { //LEFT
				pressedSpiritA = true;
				faceLeft = true;
				faceRight = false;
			}
			if(keyPressed==KeyEvent.VK_S) { //DOWN
				pressedSpiritS = true;
			}
			if(keyPressed==KeyEvent.VK_D) { //RIGHT
				pressedSpiritD = true;
				faceRight = true;
				faceLeft = false;
			}
			if(keyPressed==KeyEvent.VK_SHIFT) { //SHIFT - Ends Ephemeral Early
				ephemeral = false;
				xChar = xSpirit;
				yChar = ySpirit;
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		int keyReleased = e.getKeyCode();
		if(keyReleased==KeyEvent.VK_W) { //UP
			pressedW = false;
			pressedSpiritW = false;
			jumping = false;
			jumpCount = 0;
		}
		if(keyReleased==KeyEvent.VK_A) { //LEFT
			pressedA = false;
			pressedSpiritA = false;
		}
		if(keyReleased==KeyEvent.VK_S) { //DOWN
			pressedS = false;
			pressedSpiritS = false;
		}
		if(keyReleased==KeyEvent.VK_D) { //RIGHT
			pressedD = false;
			pressedSpiritD = false;
		}
		if(keyReleased==KeyEvent.VK_SHIFT) { //SHIFT
			xSpirit = xChar;
			ySpirit = yChar;
			pressedShift = false;
		}
	}
	public void keyTyped(KeyEvent e) {}
	public void actionPerformed(ActionEvent e) {}
}
