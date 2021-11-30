package world;

import java.awt.Color;
import java.util.Random;

public class Cure extends Prop {

	private int effect;
	
	public Cure(World world) {
		super(new Color(255, 0, 0), (char)3, world);
		Random r = new Random();
		effect = r.nextInt(3) + 1;
	}
	
	public int getEffect() {
		return effect;
	}

}
