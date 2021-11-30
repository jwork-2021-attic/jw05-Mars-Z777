package screen;

import asciiPanel.AsciiPanel;
import creature.*;
import thread.GameControl;
import world.Bullet;
import world.Finish;
import world.Floor;
import world.Prop;
import world.World;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayScreen implements Screen {

	private World world;
	private Player player;
	private Finish finish;
	private boolean win, lose;
	private ArrayList<Monster> monsters;
	private CopyOnWriteArrayList<Bullet> bullets;
	private CopyOnWriteArrayList<Bullet> deleteBullets;
	private CopyOnWriteArrayList<Prop> props;
	private CopyOnWriteArrayList<Prop> deleteProps;
	private GameControl controller;
	private int speedcount = 0;
	private int speed = 5;
	private int top;
	private int left;
	private int monsterNum;
    
    public PlayScreen() {
    	win = false;
    	lose = false;
    	world = new World();
    	player = new Player(new Color(255, 0, 0), world, this);
    	world.put(player, 0, 0);
    	finish = new Finish(new Color(255, 0, 0), world);
    	world.put(finish, world.WIDTH - 1, world.HEIGHT - 1);
    	bullets = new CopyOnWriteArrayList<Bullet>();
    	deleteBullets = new CopyOnWriteArrayList<Bullet>();
    	props = new CopyOnWriteArrayList<Prop>();
    	deleteProps = new CopyOnWriteArrayList<Prop>();
    	monsters = new ArrayList<Monster>();
    	top = 6;
    	left = 43;
    	monsterNum = 15;
    	
    	controller = new GameControl(this);
    	controller.start();
    	for(int i = 0; i < monsterNum; i++)
    		addMonster();
    }
	
    private void messageUpdate(AsciiPanel terminal) {
    	String h = String.format("Hp: %2d/%2d", player.getHealth(), player.getMaxHp());
    	String p = String.format("Power: %d", player.getPower());
    	String ins = "Instructions:";
    	String w = "W: up";
    	String s = "S: down";
    	String a = "A: left";
    	String d = "D: right";
    	String j = "J: shoot";
    	terminal.write(h, left, top);
    	terminal.write(p, left, top + 2);
    	terminal.write(ins, left, top + 10);
    	terminal.write(w, left, top + 12);
    	terminal.write(s, left, top + 14);
    	terminal.write(a, left, top + 16);
    	terminal.write(d, left, top + 18);
    	terminal.write(j, left, top + 20);
    }
    
	@Override
	public void displayOutput(AsciiPanel terminal) {
		for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());

            }
        }
		messageUpdate(terminal);
	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
		if(win)
			return new WinScreen();
		else if(lose)
			return new LoseScreen();
		switch(key.getKeyCode()) {
		case KeyEvent.VK_W:
			player.move(0);
			break;
		case KeyEvent.VK_S:
			player.move(1);
			break;
		case KeyEvent.VK_A:
			player.move(2);
			break;
		case KeyEvent.VK_D:
			player.move(3);
			break;
		case KeyEvent.VK_J:
			player.attack();
			break;
		}
		return this;
	}
	
	public void win() {
		win = true;
	}
	
    public void lose() {
    	lose = true;
    }
    
    public void addBullet(Bullet b) {
    	bullets.add(b);
    }
    
    public void deleteBullet(Bullet b) {
    	deleteBullets.add(b);
    }
    
    public void addProp(Prop p) {
    	props.add(p);
    }
    
    public void deleteProp(Prop p) {
    	deleteProps.add(p);
    }
    
    public void update() {
    	speedcount++;
    	if(speedcount == speed) {
    		for(Bullet b : bullets)
    			b.action();
    		speedcount = 0;
    	}
    	for(Bullet b: deleteBullets) {
    		bullets.remove(b);
    		int x = b.getX();
    		int y = b.getY();
    		world.put(new Floor(world), x, y);
    	}
    	deleteBullets.clear();
    	for(Prop p: deleteProps) {
    		props.remove(p);
    	}
    	deleteProps.clear();
    }
	
    public Player getPlayer() {
    	return player;
    }
    
    public void addMonster() {
    	Random r = new Random();
    	int x = r.nextInt(World.WIDTH);
    	int y = r.nextInt(World.HEIGHT);
    	while(world.posJudge(x, y) != 1) {
    		x = r.nextInt(World.WIDTH);
    		y = r.nextInt(World.HEIGHT);
    	}
    	Monster m = new Monster(new Color(0, 255, 0), world, this);
    	monsters.add(m);
    	world.put(m, x, y);
    	controller.addMonster(m);
    }
    
    public void deleteMonster(Monster m) {
    	monsters.remove(m);
    }
    
}
