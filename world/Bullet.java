package world;

import java.awt.Color;

import creature.Creature;
import screen.PlayScreen;

public class Bullet extends Thing {

	private PlayScreen screen;
	private int dir;
	private int damage;
	// private Creature parent;
	
	public Bullet(Color color, World world, PlayScreen screen, int dir, Creature c) {
		super(color, (char)7, world);
		this.screen = screen;
		this.dir = dir;
		this.damage = c.getPower();
		// this.parent = c;
	}
	
	public void moveTo(int xPos, int yPos) {
    	int x = this.getX();
    	int y = this.getY();
        world.put(this, xPos, yPos);
        world.put(new Floor(world), x, y);
    }
	
	public void action() {
		int x = getX() + Thing.dirs[dir][0];
		int y = getY() + Thing.dirs[dir][1];
		int type = world.posJudge(x, y);
		if(type == 1)
			this.moveTo(x, y);
		else if(type == 0 || type == 2 || type == -1) {
			//screen.deleteBullet(this);
			this.dir = changeDir(dir);
		}
		else if(type == 3) {
			Creature target = (Creature) world.get(x, y);
			target.beHit(damage);
			screen.deleteBullet(this);
		}
		else if(type == 4) {
			Bullet b = (Bullet)world.get(x, y);
			screen.deleteBullet(b);
			screen.deleteBullet(this);
		}
	}

}
