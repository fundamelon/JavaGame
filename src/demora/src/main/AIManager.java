package main;

import org.newdawn.slick.Graphics;

import main.pathfinding.*;

public class AIManager {
	private static NodeMap nodeMap;
	private static AStarHeuristic testPath;
	
	public static void init() {
		
	}
	
	public static void generateNodeMap(int interval, int width, int height) {
		nodeMap = new NodeMap(interval, width, height);
	}
	
	public static void generateNodeMap(int interval) {
		generateNodeMap(interval, GameBase.getZone().getWidth(), GameBase.getZone().getHeight());
	}
	
	public static NodeMap getNodeMap() {
		return nodeMap;
	}
	
	public static void renderNodeMap(Graphics g) {
		if(nodeMap != null)
			nodeMap.render(g);
		else
			System.out.println("Cannot render: Node map has not been created!");
	}
	
	public static void testPathfinder() {
		testPath = new AStarHeuristic(nodeMap);
	}
}
