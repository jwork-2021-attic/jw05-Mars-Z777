package thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import creature.Monster;
import creature.Player;
import screen.PlayScreen;

public class GameControl extends Thread {
	
	private Player player;
	private ExecutorService exec;
	
	public GameControl(PlayScreen screen) {
		player = screen.getPlayer();
		exec = Executors.newCachedThreadPool();
	}
	
	public void start() {
		exec.execute(player);
	}
	
	public void addMonster(Monster m) {
		exec.execute(m);
	}
	
}
