package com.golden.gamedev.engine.jogl;

// JFC
import java.awt.Graphics2D;

// JOGL
import net.java.games.jogl.GL;
import net.java.games.jogl.GLEventListener;
import net.java.games.jogl.GLDrawable;
import net.java.games.jogl.WGL;


/**
 * Listen to JOGL events and init the JOGL renderer.
 */
public class JOGLRenderer implements GLEventListener {


 /***************************** JOGL COMPONENT *******************************/

	private GL              gl;

	private boolean			vsync;


 /***************************** BACK BUFFER **********************************/

	private JOGLGraphics	currentGraphics;


 /****************************************************************************/
 /******************************* CONSTRUCTOR ********************************/
 /****************************************************************************/

	/**
	 * Constructs new <code>JOGLRenderer</code> to listen JOGL events and makes
	 * up JOGL renderer.
	 *
	 * @param vsync	whether the rendering should be sync to vertical refresh
	 * 				rate or not
	 */
	public JOGLRenderer(boolean vsync) {
		this.vsync = vsync;
	}


 /****************************************************************************/
 /***************************** JOGL PROPERTIES ******************************/
 /****************************************************************************/

	/**
	 * Returns OpenGL mapping to Graphics2D rendering.
	 */
	public Graphics2D getRenderer() {
	    return currentGraphics;
	}

	/**
	 * Returns whether this graphics engine is vsync to display mode refresh
	 * rate or not.
	 */
	public boolean isVSync() {
		return vsync;
	}

	/**
	 * Returns the basic JOGL OpenGL interface routines.
	 */
	public GL getGL() {
		return gl;
	}


 /****************************************************************************/
 /******************* GLEventListener Implementation *************************/
 /****************************************************************************/

	/**
	 * Called by the JOGL rendering process at initialisation. This method
	 * is responsible for setting up the GL context.
	 *
	 * @param drawable The GL context which is being initialised
	 */
    public void init(GLDrawable drawable) {
		// get hold of the GL content
		gl = drawable.getGL();

		// by default JOGL use vsync
		// change to requested vsync value
		if (!vsync) {
			if (gl.isExtensionAvailable("WGL_EXT_swap_control")) {
    			try {
					((WGL) gl).wglSwapIntervalEXT(0);
				} catch (Throwable e) {
					// something wrong here, just ignore it
					// it do no harm :-)
					vsync = true;
				}

			} else {
				// vsync control is not available
				vsync = true;
			}
		}


		// init GL rendering value

		// enable textures since we're going to use these for our sprites
		gl.glEnable(GL.GL_TEXTURE_2D);

		// set the background colour of the display to black
		gl.glClearColor(0, 0, 0, 0);

		// disable the OpenGL depth test since we're rendering 2D graphics
		gl.glDisable(GL.GL_DEPTH_TEST);

		// enable transparency
		gl.glEnable(gl.GL_BLEND);
		gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);


		// init fake Graphics2D (JOGL Graphics)
		currentGraphics = new JOGLGraphics(gl);
    }

    /**
     * Called by JOGL rendering process to initiate OpenGL rendering.
     */
	public void display(GLDrawable drawable) {
		// flush the graphics commands to the card
		gl.glFlush();
    }

	/**
	 * Called by the JOGL rendering process if and when the display is resized.
	 *
	 * @param drawable The GL content component being resized
	 * @param x The new x location of the component
	 * @param y The new y location of the component
	 * @param width The width of the component
	 * @param height The height of the component
	 */
	public void reshape(GLDrawable drawable, int x, int y, int width, int height) {
		gl = drawable.getGL();

		// at reshape we're going to tell OPENGL that we'd like to
		// treat the screen on a pixel by pixel basis by telling
		// it to use Orthographic projection.
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();

		gl.glOrtho(0, width, height, 0, -1, 1);

		// set the area being rendered
		gl.glViewport(0, 0, width, height);
	}

	/**
	 * Called by the JOGL rendering process if/when the display mode is changed.
	 *
	 * @param drawable 		the GL context which has changed
	 * @param modeChanged 	true if the display mode has changed
	 * @param deviceChanged	true if the device in use has changed
	 */
	public void displayChanged(GLDrawable drawable,
							   boolean modeChanged,
							   boolean deviceChanged) {
	}


}