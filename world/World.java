package world;

import maze.*;
import java.lang.Math;

import creature.Creature;

public class World {

    public final static int WIDTH = 40;
    public final static int HEIGHT = 40;

    private Tile<Thing>[][] tiles;

    public World() {

        if (tiles == null) {
            tiles = new Tile[WIDTH][HEIGHT];
        }

        int dim = Math.min(WIDTH, HEIGHT);
        MazeGenerator MG = new MazeGenerator(dim);
        MG.generateMaze();
        int[][] Maze = MG.getIntMaze();
        
        for(int i = 0; i < WIDTH; i++) {
        	for(int j = 0; j < HEIGHT; j++) {
        		tiles[i][j] = new Tile<>(i, j);
        		tiles[i][j].setThing(Maze[i][j] == 1 ? new Floor(this): new Wall(this));
        	}
        }
    }

    public Thing get(int x, int y) {
        return this.tiles[x][y].getThing();
    }

    public void put(Thing t, int x, int y) {
        this.tiles[x][y].setThing(t);
    }
    
    public int posJudge(int x, int y) {
    	if(x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT)
    		return -1;
    	else {
    		Thing t = get(x, y);
    		if(t instanceof Finish)
    			return 0;
    		if(t instanceof Floor)
    			return 1;
    		if(t instanceof Wall)
    			return 2;
    		if(t instanceof Creature)
    			return 3;
    		if(t instanceof Bullet)
    			return 4;
    		if(t instanceof Prop)
    			return 5;
    	}
		return -2;
    }

}