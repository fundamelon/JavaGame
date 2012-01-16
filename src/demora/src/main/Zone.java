package main;

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
}
