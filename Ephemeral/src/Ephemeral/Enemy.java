package Ephemeral;

public class Enemy {
	
	public int xEnemy;
	public int yEnemy;
	public double damage;
	public double degree;
	public int stock; //# of lives
	public int knockbackCount;
	public boolean knockback;
	public int xBullet;
	public int yBullet;
	public int quad = 0;
	
	public Enemy(int x, int y, double d, double deg, int s, int kC, boolean k){
		xEnemy = x;
		yEnemy = y;
		damage = d;
		degree = deg;
		stock = s;
		knockbackCount = kC;
		knockback = k;
		xBullet = x;
		yBullet = y;
	}
}
