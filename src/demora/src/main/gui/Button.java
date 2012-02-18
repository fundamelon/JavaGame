package main.gui;

import main.ControlManager;

import org.newdawn.slick.geom.Rectangle;

public class Button implements Clickable{

	private Rectangle bounds;
	private int eventKey;
	private boolean status;
	private boolean prevStatus;
	private boolean mouseEntered;
	private boolean mouseExited;
	private boolean mouseClicked;
	private boolean mouseReleased;
	private boolean mouseOver;
	private boolean toggleable;
	private boolean visible = true;
	
	private String name;
	public int id;

	public Button(int newId, String newName, Rectangle newBounds) {
		name = newName;
		id = newId;
		bounds = newBounds;
	}
	
	public Button(int newId, Rectangle newBounds) {
		this(newId, ""+newId, newBounds);
	}
	
	public Button(int newId, float x, float y, float width, float height) {
		this(newId, new Rectangle(x, y, width, height));
	}
	
	public Button(int newId) { 
		this(newId, new Rectangle(0, 0, 100, 100));
	}
	
	public void update() {
		if(mouseHover() != mouseOver) {
			if(mouseHover()) {
				mouseEntered = true;
			} else {
				mouseExited = true;
			}
			mouseOver = mouseHover();
		} else {
			mouseEntered = false;
			mouseExited = false;
		}
		
		status = mouseDown();		
		mouseClicked = mouseClick();
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	
	public boolean getStatus() {
		return status;
	}
	
	public boolean mouseClick() {
		return mouseHover() && ControlManager.mouseButtonClicked(ControlManager.mousePrimary);
	}
	
	public boolean mouseRelease() {
		return mouseExited || ControlManager.mouseButtonReleased(ControlManager.mousePrimary);
	}
	
	public boolean mouseDown() {
		return mouseHover() && ControlManager.mouseButtonStatus(ControlManager.mousePrimary);
	}
	
	public boolean mouseHover() {
		return bounds.contains(ControlManager.getMouseX(), ControlManager.getMouseY());
	}

	public boolean mouseEnter() {		
		return mouseEntered;
	}

	public boolean mouseExit() {
		return mouseExited;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle newBounds) {
		bounds = newBounds;
	}

	public void setEventKey(int key) {
		eventKey = key;
	}

	public int getEventKey() {
		return eventKey;
	}

	public boolean getToggleMode() {
		return toggleable;
	}

	public void setToggleMode(boolean mode) {
		toggleable = mode;		
	}

	public String getName() {
		return name;
	}

}
