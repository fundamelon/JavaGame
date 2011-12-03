package main;

import java.io.File;

public class Level {
	public static int id, width = 40, height = 30;
	public static String levelData = (new File("src/maps/01.dma")).toString();
	public static void load(int id) {
		System.out.println(levelData);
	}
	
	public static int getWidth() {return width;}
	public static int getHeight() {return height;}
}
