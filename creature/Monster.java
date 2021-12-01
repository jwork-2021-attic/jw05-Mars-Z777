package creature;

import java.awt.Color;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import screen.PlayScreen;
import world.*;

public class Monster extends Creature {

	public Monster(Color color, World world, PlayScreen screen) {
		super(color, (char)1, world, screen);
		health = 5;
		maxHp = 5;
		Random r = new Random();
		power = r.nextInt(3) + 1;
	}

	@Override
	public void run() {
		
		try {
			while(health > 0) {
				TimeUnit.MILLISECONDS.sleep(500);
				action();
			}
		}catch(InterruptedException e) {
			System.out.println("Monster thread error");
		}finally {
			screen.deleteMonster(this);
			Random r = new Random();
			int chance = r.nextInt(4);
			int x = getX();
			int y = getY();
			if(chance == 0)
				world.put(new Cure(world), x, y);
			else if(chance == 1)
				world.put(new Power_up(world), x, y);
			else
				world.put(new Floor(world), x, y);
		}
		
	}
	
	void action() {
		Random r = new Random();
		int n = r.nextInt(5);
		if(n == 4)
			attack();
		else
			move(n);
	}
	
	public synchronized void move(int dir) {
		if(dir < 0 || dir >=4)
			return;
		this.dir = dir;
		int x = getX() + Thing.dirs[dir][0];
		int y = getY() + Thing.dirs[dir][1];
		int status = world.posJudge(x, y);
		if(status == 1 || status == 5)
			this.moveTo(x, y);
	}
	
	public synchronized void attack() {
		int x = getX() + Thing.dirs[dir][0];
		int y = getY() + Thing.dirs[dir][1];
		int type = world.posJudge(x, y);
		if(type != 0 && type != 2) {
			if(type == 1) {
				Bullet b = new Bullet(new Color(0, 255, 0), world, screen, dir, this);
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

}
