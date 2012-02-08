package main;

import java.util.ArrayList;

import main.pathfinding.NodeMap;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.*;

public class Zone {
	private TiledMap currentZone;
	
	public Zone(String path) {
		readFromFile(path);
	}
	
	public Zone() {
		readFromFile();
	}
	
	public void readFromFile(String path) {
		try {
			currentZone = new TiledMap(path);
		} catch (Exception e) {	e.printStackTrace(); }
	}
	
	public void readFromFile() {
		readFromFile("lib/map/test.tmx");
	}
	
	public void init() {
		ArrayList torchPositions;
	}
	
	public TiledMap getData() {
		return currentZone;
	}
	
	public int getWidth() {
		return currentZone.getWidth();
	}
	
	public int getHeight() {
		return currentZone.getHeight();
	}
	
	public int getWidthPixels() {
		return currentZone.getWidth() * currentZone.getTileWidth();
	}
	
	public int getHeightPixels() {
		return currentZone.getHeight() * currentZone.getTileHeight();
	}
	
	public void render(int x, int y) {
		currentZone.render(x, y);
	}
	
	public int getTileAtX(float ox) {
		return (int)Math.floor(ox / currentZone.getTileWidth());
	}
	
	public int getTileAtY(float oy) {
		return (int)Math.floor(oy / currentZone.getTileHeight());
	}
	
	public Rectangle[] buildCollisionArray() {
		Rectangle[] outArray = new Rectangle[getWidth() * getHeight()];
		for(int x = 0; x < getWidth(); x++) {
			for(int y = 0; y < getHeight(); y++) {
				if(blocked(x, y)) {
					outArray[y*getWidth() + x] = new Rectangle(x*32, y*32, 32, 32);
				//	System.out.println(y*getWidth() + x);
				}
			}
		}
		return outArray;
	}
	
	public boolean blocked(int x, int y) {
		return 1 == Integer.parseInt(getData().getTileProperty(getData().getTileId(x, y, GameBase.getZone().getData().getLayerIndex("util")), "collision", "0"));
	}
}
