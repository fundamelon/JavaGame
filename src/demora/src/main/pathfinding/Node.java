package main.pathfinding;

public class Node {
	private int x, y, cost, depth;
	private boolean blocked;
	
	private Node parent;
	
	public Node(int nx, int ny, boolean nblocked) {
		x = nx;
		y = ny;
		blocked = nblocked;
	}
	
	public Node(int nx, int ny) {
		this(nx, ny, false);
	}
	
	public int getX() {return x;}
	public int getY() {return y;}
	public int getCost() {return cost;}
	public int getDepth() {return depth;}
	public Node getParent() {return parent;}
	public boolean isBlocked() {return blocked;}
	
	public void setX(int nx) {x = nx;}
	public void setY(int ny) {y = ny;}
	public void setCost(int ncost) {cost = ncost;}
	public void setDepth(int ndepth) {depth = ndepth;}
	public void setParent(Node newParent) {parent = newParent;}
	public void setBlocked(boolean nblocked) {blocked = nblocked;}
	
	public String toString() {
		return "Node ["+x+", "+y+"] "+(blocked?"[obstruct]":"[free]");
	}
}