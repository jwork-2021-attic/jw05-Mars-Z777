package creature;

import java.awt.Color;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import screen.PlayScreen;
import world.Bullet;
import world.Floor;
import world.Thing;
import world.World;

public class Monster extends Creature {

	public Monster(Color color, World world, PlayScreen screen) {
		super(color, (char)2, world, screen);
		health = 10;
		maxHp = 10;
		Random r = new Random();
		power = r.nextInt(3) + 1;
	}

	@Override
	public void run() {
		
		try {
			while(health > 0) {
				TimeUnit.MILLISECONDS.sleep(200);
				action();
			}
		}catch(InterruptedException e) {
			System.out.println("Monster thread error");
		}finally {
			screen.deleteMonster(this);
			world.put(new Floor(world), getX(), getY());
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
		if(status == 1)
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
		}
	}

}
