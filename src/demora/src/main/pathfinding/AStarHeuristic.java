package main.pathfinding;

import java.util.ArrayList;

public class AStarHeuristic {
	private Path output;
	private NodeMap nodes;
	
	private Node start;
	private Node goal;
	
	private ArrayList<Node> open;
	private ArrayList<Node> closed;
	
	private int length;
	
	public AStarHeuristic(NodeMap initialSet) {
		output = new Path();
		nodes = initialSet;
	}
	
	public void pathfind(Node nstart, Node ngoal) {
		goal = ngoal;
		start = nstart;
		
		open.clear();
		closed.clear();
		length = 0;
		
		open.add(start);
		goal.setParent(null);
		
		int maxDepth = 0;
		Node current = null;
		
		while(open.size() != 0 && length <= maxDepth) {
			current = open.get(0);
			if(current.equals(goal)) break;
			
			open.remove(open.indexOf(current));
			closed.add(current);

			for(int x=-1; x<=1; x++) {
				for(int y=-1; y<=1; y++) {
					if(x == 0 && y == 0) continue;
					
					int tx = current.getX() + x;
					int ty = current.getY() + y;
					
					if(tx < nodes.getWidth() && ty < nodes.getHeight() && tx >= 0 && ty >= 0) {
						float cost = current.getCost() + 1;
						Node neighbor = nodes.getNodeAt(tx, ty);
						if(cost < neighbor.getCost()) {
							if(open.indexOf(neighbor) != -1)
								open.remove(neighbor);
							if(closed.indexOf(neighbor) != -1)
								closed.remove(neighbor);
						}
						
						
					}
				}
			}
		}
	}
	
	public Path createPath() {
		output = new Path();
		if(goal != null) {
			Node current = goal;
			while(current.getParent() != null) {
				output.addStep(current);
				current = current.getParent();
			}
		}
		return output;
	}
}
