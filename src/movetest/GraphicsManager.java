import java.awt.Graphics;


public class GraphicsManager {
	
	public GraphicsManager(grid panel, ControlManager oldControls, PlayerEnt oldPlayer) {
	}
	
	public void draw(Graphics g, PlayerEnt player, grid panel) {		//The object is intended to tell other objects it's time to re-draw themselves.
		player.draw(g, panel);
	}
	
	public void drawPlayer(Graphics g, PlayerEnt player) {
		
	}
}
