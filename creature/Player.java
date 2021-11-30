package creature;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import screen.PlayScreen;
import world.Bullet;
import world.Thing;
import world.World;

public class Player extends Creature {
	
	public Player(Color color, World world, PlayScreen screen) {
		super(color, (char)2, world, screen);
		health = 5;
		maxHp = 5;
		power = 1;
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
		if(status == 1)
			this.moveTo(x, y);
	}
	
	public synchronized void attack() {
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
		}
	}

}
