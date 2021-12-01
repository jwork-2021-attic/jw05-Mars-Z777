package thread;

import javax.swing.JFrame;

import java.util.concurrent.TimeUnit;

public class ScreenUpdate implements Runnable {

	private JFrame frame;
	
	public ScreenUpdate(JFrame jf) {
		frame = jf;
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				TimeUnit.MILLISECONDS.sleep(50);
				frame.repaint();
			}
		}catch(InterruptedException e) {
			System.out.println("ScreenUpdate thread error");
		}
	}

}
