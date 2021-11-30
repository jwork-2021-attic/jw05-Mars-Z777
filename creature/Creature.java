package creature;

import java.awt.Color;

import screen.PlayScreen;
import world.Bullet;
import world.Floor;
import world.Thing;
import world.World;

public abstract class Creature extends Thing implements Runnable {

	protected int health, power, maxHp, dir;
	protected PlayScreen screen;
	
	Creature(Color color, char glyph, World world, PlayScreen screen) {
        super(color, glyph, world);
        this.screen = screen;
        dir = 0;
    }

    public void moveTo(int xPos, int yPos) {
    	int x = this.getX();
    	int y = this.getY();
        world.put(this, xPos, yPos);
        world.put(new Floor(world), x, y);
    }
	
	public abstract void run();
	
	public int getHealth() {
		return health;
	}
	
	public int getPower() {
		return power;
	}
	
	public void beHit(int damage) {
		health -= damage;
		if(health <= 0) {
			health = 0;
		}
	}
	
	public int getMaxHp() {
		return maxHp;
	}
	
}
