package main;

import tiled.core.Map;
import tiled.io.TMXMapReader;

public class Zone {
	private TMXMapReader reader = new TMXMapReader();
	private Map currentZone;
	
	public void readFromFile() {
		try {
			currentZone = reader.readMap("lib/map/untitled.tmx");
		} catch (Exception e) {	e.printStackTrace(); }
	}
	
	public Map getData() {
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
}
