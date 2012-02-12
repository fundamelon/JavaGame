package main.gui;


public interface Clickable {
	public boolean mouseClick();
	public boolean mouseRelease();
	public boolean mouseDown();
	public boolean mouseHover();
	public boolean mouseEnter();
	public boolean mouseExit();
	public boolean getToggleMode();
	public int getEventKey();

	public void setToggleMode(boolean mode);
	public void setEventKey(int key);
	
	public org.newdawn.slick.geom.Rectangle getBounds();
	public void setBounds(org.newdawn.slick.geom.Rectangle newBounds);
}
