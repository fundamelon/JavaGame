package main.pathfinding;

import java.util.ArrayList;

import main.AIManager;
import main.GameBase;

public class Path {
	public ArrayList<Node> steps = new ArrayList<Node>(1);
	
	public Path(Node[] nodeArr) {
		for(Node n : nodeArr)
			steps.add(n);
	}
	
	public Path() {
		
	}
	
	public void addStep(Node newStep) {
		steps.add(newStep);
	}
	
	public void removeStep(Node targetStep) {
		steps.remove(targetStep);
	}
	
	public Node getFirst() {
		return steps.get(0);
	}
	
	public Node getLast() {
		return steps.get(steps.size()-1);
	}
	
	public Node[] getNodesArr() {
		return (Node[]) steps.toArray();
	}
	
	public ArrayList<Node> getNodes() {
		return steps;
	}
	
	public void render(org.newdawn.slick.Graphics g) {
		int interval = AIManager.getNodeMap().getInterval();
		for(int i = 1; i < steps.size(); i++) {
			g.drawLine(	
				steps.get(i).getX()*interval, 
				steps.get(i).getY()*interval, 
				steps.get(i-1).getX()*interval, 
				steps.get(i-1).getY()*interval
			);
		}
	}
}
