package com.golden.gamedev;

// JFC
import java.awt.Dimension;
import javax.swing.JOptionPane;
import java.lang.reflect.Constructor;

// GTGE
import com.golden.gamedev.engine.jogl.JOGLWindowedMode;
import com.golden.gamedev.engine.jogl.JOGLFullScreenMode;
import com.golden.gamedev.engine.lwjgl.LWJGLMode;
import com.golden.gamedev.engine.lwjgl.LWJGLInput;
import com.golden.gamedev.engine.lwjgl.LWJGLTimer;
import com.golden.gamedev.engine.graphics.WindowExitListener;


/**
 * Extending <code>GameLoader</code> class for loading game with the support of
 * OpenGL renderer using LWJGL or JOGL. <p>
 *
 * You must download LWJGL and/or JOGL library from its official site to use
 * this class. <br>
 * Download LWJGL library if you want to use OpenGL via LWJGL. <br>
 * Download JOGL library if you want to use OpenGL via JOGL. <br>
 * Or download both libraries if you want your game can be running in both mode. <p>
 *
 * LWJGL is available to download at
 * <a href="http://lwjgl.org/" target="_blank">http://lwjgl.org/</a>. <br>
 * JOGL is available to download at
 * <a href="https://jogl.dev.java.net/" target="_blank">https://jogl.dev.java.net/</a>. <p>
 *
 * Make sure the downloaded library is included into your game classpath
 * before using this class. <p>
 *
 * <b>Note: GTGE is not associated in any way with LWJGL and JOGL,
 * this class is only interfacing LWJGL and JOGL to be used in GTGE. <br>
 * This class is created and has been tested to be working properly
 * using <em>LWJGL v0.95</em> and <em>JOGL v1.1b-08</em>.</b> <p>
 *
 * Example how-to-use <code>OpenGLGameLoader</code> :
 * <pre>
 *    public class YourGame extends Game {
 *
 *       public static void main(String[] args) {
 *          <b>OpenGLGameLoader</b> game = new OpenGLGameLoader();
 *
 *          // init game with OpenGL LWJGL fullscreen mode
 *          // 640x480 screen resolution
 *          game.setupLWJGL(<b>new YourGame()</b>, new Dimension(640,480), true);
 *
 *          // init game with OpenGL JOGL fullscreen mode
 *          // 640x480 screen resolution
 *          //game.setupJOGL(<b>new YourGame()</b>, new Dimension(640,480), true);
 *
 *          // init game with Java2D fullscreen mode
 *          // 640x480 screen resolution
 *          //game.setup(<b>new YourGame()</b>, new Dimension(640,480), true);
 *
 *          // pick the graphics engine you like
 *          // in this example we pick OpenGL via LWJGL
 *
 *          game.start();
 *       }
 *
 *    }
 * </pre>
 *
 * @see <a href="http://lwjgl.org/" target="_blank">LWJGL official site</a>
 * @see <a href="https://jogl.dev.java.net/" target="_blank">JOGL official site</a>
 */
public class OpenGLGameLoader extends GameLoader {


 /****************************************************************************/
 /******************************* CONSTRUCTOR ********************************/
 /****************************************************************************/

	/**
	 * Constructs new <code>OpenGLGameLoader</code>.
	 *
	 * @see #setupLWJGL(Game, Dimension, boolean, boolean)
	 * @see #setupJOGL(Game, Dimension, boolean, boolean)
	 */
	public OpenGLGameLoader() {
	}


 /****************************************************************************/
 /******************** SETUP GAME IN OPENGL VIA LWJGL ************************/
 /****************************************************************************/

	/**
	 * Initializes OpenGL LWJGL graphics engine with specified size, mode, and
	 * associates it with specified <code>Game</code> object.
	 */
	public void setupLWJGL(Game game, Dimension d,
						   boolean fullscreen, boolean vsync) {
	try {
		// validate java version first
		if (!validJavaVersion()) {
			// not valid java version!!
			JOptionPane.showMessageDialog(null,
				"Sorry, this game requires Java "+MINIMUM_VERSION+"++ installed\n" +
				"Your machine only has Java "+JAVA_VERSION+" installed\n\n"+
				"Please install the latest Java Runtime Edition (JRE)\n" +
				"from http://www.java.com",
				"Game Initialization", JOptionPane.ERROR_MESSAGE
			);

			// don't bother to continue
			System.exit(-1);
		}


		// time to create the OpenGL Graphics Engine
		LWJGLMode mode = new LWJGLMode(d, fullscreen, vsync);
		mode.setWindowListener(this);

		gfx = mode;

		this.game = game;
		this.game.bsGraphics = gfx;
		this.game.bsInput = new LWJGLInput();
		this.game.bsTimer = new LWJGLTimer();

	} catch (Throwable e) {
		e.printStackTrace();

		JOptionPane.showMessageDialog(null,
			"Your machine does not support OpenGL via LWJGL!\n" +
			"Caused by: " + e.toString() + "\n" +
			"Fail back to Java2D Graphics Engine.",
			"Game Initialization", JOptionPane.ERROR_MESSAGE
		);

		if (gfx != null) {
			gfx.cleanup();
		}

		setup(game, d, fullscreen, false);
	} }

	/**
	 * Initializes OpenGL LWJGL graphics engine with specified size, mode, using
	 * vsync by default, and associates it with specified <code>Game</code> object.
	 */
	public void setupLWJGL(Game game, Dimension d, boolean fullscreen) {
		setupLWJGL(game, d, fullscreen, true);
	}


 /****************************************************************************/
 /********************* SETUP GAME IN OPENGL VIA JOGL ************************/
 /****************************************************************************/

	/**
	 * Initializes OpenGL JOGL graphics engine with specified size, mode, and
	 * associates it with specified <code>Game</code> object.
	 */
	public void setupJOGL(Game game, Dimension d,
						  boolean fullscreen, boolean vsync) {
	boolean orig = fullscreen;
	try {
		// validate java version first
		if (!validJavaVersion()) {
			// not valid java version!!
			JOptionPane.showMessageDialog(null,
				"Sorry, this game requires Java "+MINIMUM_VERSION+"++ installed\n" +
				"Your machine only has Java "+JAVA_VERSION+" installed\n\n"+
				"Please install the latest Java Runtime Edition (JRE)\n" +
				"from http://www.java.com",
				"Game Initialization", JOptionPane.ERROR_MESSAGE
			);

			// don't bother to continue
			System.exit(-1);
		}


		// time to create the OpenGL Graphics Engine

		if (fullscreen) {
			// fullscreen mode
			JOGLFullScreenMode mode = null;
			try {
				// using reflection to load the class, this is a work around to avoid
				// JOGL static initialization exception when JOGL library is not
				// included in the bundle, when the game is not using JOGL graphics engine
		        Class joglClass = Class.forName("com.golden.gamedev.engine.jogl.JOGLFullScreenMode");
		        Constructor joglConstructor = joglClass.getConstructor(new Class[] { Dimension.class, boolean.class });

		        mode = (JOGLFullScreenMode) joglConstructor.newInstance(new Object[] { d, new Boolean(vsync) });
				mode.getFrame().removeWindowListener(WindowExitListener.getInstance());
				mode.getFrame().addWindowListener(this);

				gfx = mode;
			} catch (Throwable e) {
				// the first exception is
				// the exception because of class creation via reflection
				// we need to know what is the actual exception!
				if (e.getCause() != null) {
					e = e.getCause();
				}

				e.printStackTrace();

				JOptionPane.showMessageDialog(null,
											  "ERROR: Entering JOGL FullScreen Mode\n" +
											  "Caused by: " + e.toString(),
											  "Graphics Engine Initialization",
											  JOptionPane.ERROR_MESSAGE);
				// fail-safe
				fullscreen = false;

				if (mode != null) {
					mode.cleanup();
				}
			}
		}

		if (!fullscreen) {
			// using reflection to load the class, this is a work around to avoid
			// JOGL static initialization exception when JOGL library is not
			// included in the bundle, when the game is not using JOGL graphics engine
	        Class joglClass = Class.forName("com.golden.gamedev.engine.jogl.JOGLWindowedMode");
	        Constructor joglConstructor = joglClass.getConstructor(new Class[] { Dimension.class, boolean.class });

	        JOGLWindowedMode mode = (JOGLWindowedMode) joglConstructor.newInstance(new Object[] { d, new Boolean(vsync) });
			mode.getFrame().removeWindowListener(WindowExitListener.getInstance());
			mode.getFrame().addWindowListener(this);

			gfx = mode;
		}

		this.game = game;
		this.game.bsGraphics = gfx;

	} catch (Throwable e) {
		// the first exception is
		// the exception because of class creation via reflection
		// we need to know what is the actual exception!
		if (e.getCause() != null) {
			e = e.getCause();
		}

		e.printStackTrace();

		JOptionPane.showMessageDialog(null,
			"Your machine does not support OpenGL via JOGL!\n" +
			"Caused by: " + e.toString() + "\n" +
			"Fail back to Java2D Graphics Engine.",
			"Game Initialization", JOptionPane.ERROR_MESSAGE
		);

		if (gfx != null) {
			gfx.cleanup();
		}

		setup(game, d, orig, false);
	} }

	/**
	 * Initializes OpenGL JOGL graphics engine with specified size, mode, using
	 * vsync by default, and associates it with specified <code>Game</code> object.
	 */
	public void setupJOGL(Game game, Dimension d, boolean fullscreen) {
		setupJOGL(game, d, fullscreen, true);
	}


}