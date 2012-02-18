package main.gui;

import java.util.ArrayList;


import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class GUIManager {
	private static ArrayList<Clickable> clickables = new ArrayList<Clickable>();
	
	public static void init() {
		clickables.add(new Button(clickables.size(), 200, 200, 100, 100));
		clickables.add(new Button(clickables.size(), 200, 300, 100, 100));
		clickables.add(new Button(clickables.size(), 300, 200, 100, 100));
		clickables.add(new Button(clickables.size(), 300, 300, 100, 100));
	}
	
	public static void update() {
		for(Clickable item : clickables) {
			item.update();
		}
	}

	public static void render(Graphics g, int delta) {
		for(Clickable item : clickables) {
			if(item.isVisible()) {
				if(main.GameBase.debug_menu) {
					if(item.mouseClick()) {
						System.out.println(item.getName() + ": clicked");
					}
					
					if(item.mouseExit()) {
						System.out.println(item.getName() + ": exited");
					} else if(item.mouseEnter()) {
						System.out.println(item.getName() + ": entered");
					}
				}
				
				
				
				Color oldColor = g.getColor();
				if(item.mouseDown()) {
					g.setColor(Color.darkGray);
					Event.fire(Event.GAME_START);
				} else if(item.mouseHover()) {
					g.setColor(Color.gray);
				} else {
					g.setColor(Color.white);
				}
				g.fill(item.getBounds());
				g.setColor(oldColor);
			}
		}
	}
}
