package main.gui;

public class Event {
	public static final int GAME_QUIT = 0;
	public static final int GAME_START = 1;
	public static final int GAME_PAUSE = 2;
	public static final int OPEN_MAINMENU = 3;
	public static final int OPEN_OPTIONS = 4;	
	
	public static void fire(int action) {
		switch(action) {
		case GAME_QUIT:
			//TODO: Quit the game
			break;
		case GAME_START:
			//TODO: Switch modes
			break;
		case GAME_PAUSE:
			//TODO: Pause game
			break;
		case OPEN_MAINMENU:
			//TODO: Switch modes
			break;
		case OPEN_OPTIONS:
			//TODO: Advance into option menu
			break;
		default:
			throw new IllegalArgumentException("Invalid action key");
		}
	}
}
