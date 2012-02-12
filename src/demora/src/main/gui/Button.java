package main.gui;

import main.ControlManager;

import org.newdawn.slick.geom.Rectangle;

public class Button implements Clickable{

	private Rectangle bounds;
	private int eventKey;
	private boolean status;
	private boolean mouseEntered;
	private boolean mouseExited;
	private boolean mouseOver;
	private boolean toggleable;

	public Button(Rectangle newBounds) {
		bounds = newBounds;
	}
	
	public Button(float x, float y, float width, float height) {
		this(new Rectangle(x, y, width, height));
	}
	
	public Button() {
		this(new Rectangle(0, 0, 100, 100));
	}
	
	public void update() {
		if(mouseHover()) {
			if(mouseEntered) {
				mouseEntered = false;
			} else {
				mouseEntered = true;
			}
			
			if(mouseDown()) {
				if(toggleable) {
					status = !status;
				} else {
					status = true;
				}
			}
		} else {
			if(mouseExited) {
				mouseExited = false;
			} else {
				mouseExited = true;
			}
		}
	}
	
	public boolean status() {
		return mouseDown();
	}
	
	public boolean mouseClick() {
		return false;
	}

	public boolean mouseRelease() {
		return ControlManager.mouseButtonReleased(ControlManager.mousePrimary);
	}

	public boolean mouseDown() {
		return mouseHover() && ControlManager.mouseButtonStatus(ControlManager.mousePrimary);
	}

	public boolean mouseHover() {
		return bounds.contains(ControlManager.getMouseX(), ControlManager.getMouseY());
	}

	public boolean mouseEnter() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean mouseExit() {
		// TODO Auto-generated method stub
		return false;
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

}
