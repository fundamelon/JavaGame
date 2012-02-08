package main.pathfinding;

import main.GameBase;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

public class NodeMap implements Cloneable {

	private int width, height, interval;
	private Node[][] map;
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public NodeMap(int interval, int width, int height) {
		this.width = width;
		this.height = height;
		this.interval = interval;
		generate();
	}
	
	public void generate() {
		if(width == 0 || height == 0 || interval == 0) {
			System.out.println("Invalid arguments");
			return;
		}
		
		map = new Node[width][height];
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				map[x][y] = new Node(x, y, false);
			}
		}
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				if(GameBase.getZone().blocked(x * (32/interval), y * (32/interval))) {
					if(x+1 < width && y+1 < height) {
						map[x+1][y+1].setBlocked(true);
					}
					
					if(x+2 < width && y+2 < height) {
						map[x+2][y+1].setBlocked(true);
						map[x+2][y+2].setBlocked(true);
						map[x+1][y+2].setBlocked(true);
					}
				}
			}
		}
	}
	
	public void render(Graphics g) {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				Color oldColor = g.getColor();
				g.setColor(map[x][y].isBlocked() ? Color.red : Color.white);
				g.draw(new Rectangle((x-1) * interval, (y-1) * interval, 3, 3));
				g.setColor(oldColor);
			}
		}
	}
	
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public int getInterval() {return interval;}
	
	public Node getNodeAt(int x, int y) {
		return map[x][y];
	}
	
	public Node getNodeByID(int id) {
		return map[(id - id%width)/width][id%width];
	}
	
	public Node[][] toArray() {
		return map;
	}
	
	public void setNode(Node newNode) {
		map[newNode.getX()][newNode.getY()] = newNode;
	}
	
	public void setNode(int x, int y, boolean block) {
		setNode(new Node(x, y, block));
	}
}
