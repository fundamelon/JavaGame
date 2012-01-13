package com.golden.gamedev;

// JFC
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.applet.Applet;
import java.util.Random;


/**
 * Provide the basic frame work to create a <b>Mini Applet Game</b> that
 * following GTGE <code>Game</code> class design. <p>
 *
 * This class is designed as small as possible, consists only
 * the essential engine (graphics and timer), and some important
 * built-in method, such as {@linkplain #getRandom(int, int) random number}
 * and {@linkplain #keyDown key input}. <p>
 *
 * The purpose of this class is to be the basic skeleton of a small game
 * (e.g: 4K game = 4096 bytes game). The game only need to extend this class,
 * and can immediatelly start making the game with ease. <br>
 * Of course after the game is ready to distribute, to make it very small (4K)
 * you should rip out this class methods and pack it to your game
 * as a single class file, that's it, your 4K Game has been completed! <p>
 *
 * Although this class is intended to be a small applet game,
 * the game still can be extended to be an application game. <br>
 * This is how to extend the game into a stand-alone application game :
 * <pre>
 *   public class AnyMiniApplet extends MiniApplet {
 *
 *       public static void main(String[] args) {
 *           Frame f = new Frame("Game Title");
 *           MiniApplet applet = new AnyMiniApplet();  // the game applet
 *           f.setResizable(false);         // make frame non-resizable
 *           f.add(applet);                 // add MiniApplet into the frame
 *           f.setSize(640, 480);           // set game size
 *           f.setLocationRelativeTo(null); // centering the game frame
 *           f.setVisible(true);            // show the game to screen
 *
 *           applet.start();                // start the game
 *      }
 *   }
 * </pre>
 */
public abstract class MiniApplet extends Applet implements Runnable,
														   KeyListener {

	/**
	 * The random object to generates random number.
	 */
	protected final Random rnd = new Random();

    /**
     * The canvas where the game played.
     */
	protected Canvas canvas = new Canvas();

	/**
	 * Keyboard button flags.
	 */
    protected boolean[] keyDown = new boolean[255];

	private boolean running;					// true, animation is running
	private boolean finish;						// game termination
    private BufferStrategy bufferStrategy;		// buffer strategy

	/**
	 * Game Frame-Per-Seconds (FPS).
	 */
	protected int currentFPS;
    private int	 frameCount, delay = 20;
    private long lastCount;


	/**
	 * Creates new instance of <code>MiniApplet</code>. <p>
	 *
	 * Note: Do not override or have any overloading constructor. All that
	 * belong to constructor should goes to {@link #initResources()}.
	 *
	 * @see #initResources()
	 */
	public MiniApplet() {
	}

	/**
	 * Starts the game, by default this method is called by the browser.
	 *
	 * @see #finish()
	 * @see #stop()
	 */
    public void start() {
		if (running || finish) return;
		running = true;

        if (bufferStrategy == null) {
			setIgnoreRepaint(true);

			canvas.setIgnoreRepaint(true);
            canvas.setSize(getSize().width, getSize().height);
			canvas.addKeyListener(this);

			setLayout(null);
			add(canvas);
			canvas.setLocation(0, 0);

            // using buffer strategy as backbuffer
            canvas.createBufferStrategy(2);
            bufferStrategy = canvas.getBufferStrategy();

			// initialization game resources
			initResources();
        }

		// start the game thread
		new Thread(this).start();

		canvas.requestFocus();
    }

	/**
	 * Stops the game from running, to resume the game,
	 * call {@link #start()} again, please <b>see note</b> below. <p>
	 *
	 * By default this method is called by browser.
	 *
	 * @see #finish()
	 */
	public void stop() {
		running = false;
	}


 /****************************************************************************/
 /************************** ABSTRACT MAIN-METHODS ***************************/
 /****************************************************************************/

	/**
	 * End the game, and the game can not replay again.
	 *
	 * @see #stop()
	 */
	public void finish() {
		finish = true;
		stop();
	}


    /**
     * Initialization of all game resources, everything that usually belong
     * to constructor should goes here.
     */
	public abstract void initResources();

	/**
	 * Updates game variables.
	 */
	public abstract void update();

    /**
     * Renders game to the screen.
     *
     * @param g	backbuffer graphics context
     */
	public abstract void render(Graphics2D g);


 /****************************************************************************/
 /******************************* MAIN-LOOP **********************************/
 /****************************************************************************/

    /**
     * The main loop thread.
     */
	public void run() {
        long t = System.currentTimeMillis();
        lastCount = t;

        // main loop
		while (running) {
	        t += delay;
            try { Thread.sleep(Math.max(0, t-System.currentTimeMillis()));
			} catch (InterruptedException e) { }

			if (System.currentTimeMillis()-lastCount > 1000) {
				// count FPS
				lastCount = System.currentTimeMillis();
				currentFPS = frameCount;
				frameCount = 0;
			}
		    frameCount++;

			update();		// game update

			Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
			render(g); 		// game render
			g.dispose();
			try { bufferStrategy.show();
			} catch (Exception e) { }
        }
    }


    /**
     * Returns a random number, range from lowerbound (inclusive) to
     * upperbound (inclusive).
     *
     * @param lowerbound the lowest random number
     * @param upperbound the highest random number
     * @return Random number.
     */
	public int getRandom(int lo, int hi) {
		return lo + rnd.nextInt(hi-lo+1);
	}

	/**
	 * <code>KeyListener</code> implementation to set keyboard button flags.
	 */
	public void keyPressed(KeyEvent e) {
		keyDown[e.getKeyCode() & 0xFF] = true;
	}

	/**
	 * <code>KeyListener</code> implementation to set keyboard button flags.
	 */
	public void keyReleased(KeyEvent e) {
		keyDown[e.getKeyCode() & 0xFF] = false;
	}

	/**
	 * <code>KeyListener</code> implementation, do nothing.
	 */
	public void keyTyped(KeyEvent e) { }

}