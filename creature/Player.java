package creature;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import screen.PlayScreen;
import world.*;

public class Player extends Creature {
	
	private final long interval = 500;
	private long lastshoottime;
	
	public Player(Color color, World world, PlayScreen screen) {
		super(color, (char)2, world, screen);
		health = 5;
		maxHp = 5;
		power = 1;
		lastshoottime = -1;
	}

	@Override
	public void run() {
		try {
			while(true) {
				TimeUnit.MILLISECONDS.sleep(10);
				if(health <= 0)
					screen.lose();
			}
		}catch(InterruptedException e) {
			System.out.println("Player thread error");
		}	
	}
	
	public synchronized void move(int dir) {
		if(dir < 0 || dir >=4)
			return;
		this.dir = dir;
		int x = getX() + Thing.dirs[dir][0];
		int y = getY() + Thing.dirs[dir][1];
		int status = world.posJudge(x, y);
		if(status == 0)
			screen.win();
		else if(status == 1)
			this.moveTo(x, y);
		else if(status == 5) {
			Prop p = (Prop)world.get(x, y);
			if(p instanceof Cure) {
				Cure c = (Cure)p;
				this.beCure(c.getEffect());
			}
			else if(p instanceof Power_up) {
				this.powerup();
			}
			screen.deleteProp(p);
			this.moveTo(x, y);
		}
	}
	
	public synchronized void attack() {
		if(lastshoottime == -1)
			lastshoottime = System.currentTimeMillis();
		else {
			long now = System.currentTimeMillis();
			if(now - lastshoottime < interval)
				return;
			else
				lastshoottime = now;
		}
		int x = getX() + Thing.dirs[dir][0];
		int y = getY() + Thing.dirs[dir][1];
		int type = world.posJudge(x, y);
		if(type != 0 && type != 2) {
			if(type == 1) {
				Bullet b = new Bullet(new Color(255, 0, 0), world, screen, dir, this);
				world.put(b, x, y);
				screen.addBullet(b);
			}
			else if(type == 3) {
				Creature target = (Creature) world.get(x, y);
				target.beHit(power);
			}
			else if(type == 4) {
				Bullet b = (Bullet)world.get(x, y);
				screen.deleteBullet(b);
			}
			else if(type == 5) {
				Prop p = (Prop)world.get(x, y);
				screen.deleteProp(p);
			}
		}
	}
	
	private void beCure(int effect) {
		health += effect;
		if(health >= maxHp)
			health = maxHp;
	}
	
	private void powerup() {
		if(power < 3)
			power++;
	}

}
