package com.golden.gamedev.engine.jogl;

// JFC
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.image.renderable.*;
import java.text.*;
import java.util.*;
import java.awt.RenderingHints.Key;
import java.util.WeakHashMap;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

// JOGL
import net.java.games.jogl.GL;
import net.java.games.jogl.util.BufferUtils;

// GTGE
import com.golden.gamedev.engine.graphics.NullGraphics;
import com.golden.gamedev.object.GameFont;
import com.golden.gamedev.object.font.AdvanceBitmapFont;
import com.golden.gamedev.util.FontUtil;
import com.golden.gamedev.util.Utility;
import com.golden.gamedev.util.ImageUtil;


/**
 * Fake Graphics2D for OpenGL JOGL.
 */
public class JOGLGraphics extends NullGraphics {


	// used to convert from a AffineTransform to a OpenGL matrix
	private static double affineMatrix[] = new double[3*3];

	// OpenGL matrix
	private static FloatBuffer glMatrix = BufferUtils.newFloatBuffer(16);

	// display dimension
	private static IntBuffer display = BufferUtils.newIntBuffer(16);


	private GL 	GL11;


 /****************************************************************************/
 /******************************* CONSTRUCTOR ********************************/
 /****************************************************************************/

	public JOGLGraphics(GL gl) {
		GL11 = gl;

		textureLoader = new TextureLoader(gl);

		fontMap = new WeakHashMap();
	}

	/**
	 * Returns OpenGL context.
	 */
	public GL getGL() {
		return GL11;
	}

	/**
	 * Returns texture loader used to load textures by this OpenGL renderer.
	 */
	public TextureLoader getTextureLoader() {
		return textureLoader;
	}


 /****************************************************************************/
 /*************************** CLASS VARIABLES ********************************/
 /****************************************************************************/

	private static final Rectangle NULL_RECTANGLE = new Rectangle();

	private TextureLoader 	textureLoader;

	private Color			color 		= Color.BLACK;
	private Color			background 	= Color.BLACK;

	private Composite		composite;

	private Font			font;
	private WeakHashMap		fontMap;
	private Graphics2D		g;
	private int				gap;

	private Rectangle		clipArea;


 /****************************************************************************/
 /**************************** OPENGL RENDERING ******************************/
 /****************************************************************************/

 /****************************************************************************/
 /*************************** IMAGE RENDERING ********************************/
 /****************************************************************************/

	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
	    startPainting();


		// store the current model matrix
		GL11.glPushMatrix();

		// bind to the appropriate texture for this sprite
		Texture texture = textureLoader.getTexture((BufferedImage) img);
		texture.bind(GL11);

		// translate to the right location and prepare to draw
		GL11.glTranslatef(x, y, 0);

		// draw a quad textured to match the sprite
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(0, 0);

			GL11.glTexCoord2f(0, texture.getHeight());
			GL11.glVertex2f(0, texture.getImageHeight());

			GL11.glTexCoord2f(texture.getWidth(), texture.getHeight());
			GL11.glVertex2f(texture.getImageWidth(), texture.getImageHeight());

			GL11.glTexCoord2f(texture.getWidth(), 0);
			GL11.glVertex2f(texture.getImageWidth(), 0);
		GL11.glEnd();

		// restore the model view matrix to prevent contamination
		GL11.glPopMatrix();


		endPainting();

		return true;
    }


    public boolean drawImage(Image img,
							 int x, int y, int width, int height,
							 ImageObserver observer) {
		startPainting();


		GL11.glPushMatrix();

	    Texture texture = textureLoader.getTexture((BufferedImage) img);
		texture.bind(GL11);

		GL11.glTranslatef(x, y, 0);

		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(0, 0);

			GL11.glTexCoord2f(0, texture.getHeight());
			GL11.glVertex2f(0, height);

			GL11.glTexCoord2f(texture.getWidth(), texture.getHeight());
			GL11.glVertex2f(width, height);

			GL11.glTexCoord2f(texture.getWidth(), 0);
			GL11.glVertex2f(width, 0);
		GL11.glEnd();

		GL11.glPopMatrix();


		endPainting();

	    return true;
    }


    public boolean drawImage(Image img,
							 int dx1, int dy1, int dx2, int dy2,
							 int sx1, int sy1, int sx2, int sy2,
							 ImageObserver observer) {
		startPainting();


	    Texture texture = textureLoader.getTexture((BufferedImage) img);
		texture.bind(GL11);

		float tx0 = ((float) sx1 / texture.getTextureWidth());
		float tx1 = ((float) sx2 / texture.getTextureWidth());
		float ty0 = ((float) sy1 / texture.getTextureHeight());
		float ty1 = ((float) sy2 / texture.getTextureHeight());

		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tx0, ty0);
			GL11.glVertex2f(dx1, dy1);

			GL11.glTexCoord2f(tx1, ty0);
			GL11.glVertex2f(dx2, dy1);

			GL11.glTexCoord2f(tx1, ty1);
			GL11.glVertex2f(dx2, dy2);

			GL11.glTexCoord2f(tx0, ty1);
			GL11.glVertex2f(dx1, dy2);
		GL11.glEnd();


		endPainting();

	    return true;
    }

    public boolean drawImage(Image img,
							 AffineTransform transform,
							 ImageObserver obs) {
		transform.getMatrix(affineMatrix);

		glMatrix.rewind();
		glMatrix.put((float) affineMatrix[0]).put((float) affineMatrix[1]).put(0).put(0);
		glMatrix.put((float) affineMatrix[2]).put((float) affineMatrix[3]).put(0).put(0);
		glMatrix.put(0).put(0).put(1).put(0);
		glMatrix.put((float) affineMatrix[4]).put((float) affineMatrix[5]).put(0).put(1);
		glMatrix.rewind();

		GL11.glPushMatrix();
		GL11.glMultMatrixf(glMatrix);

		drawImage(img, 0, 0, null);

		GL11.glPopMatrix();

		return true;
	}


 /****************************************************************************/
 /**************************** TEXT RENDERING ********************************/
 /****************************************************************************/

    public void drawString(String str, int x, int y) {
	    GameFont font = getGameFont();

		font.drawString(this, str, x, y + gap);
   }


 /****************************************************************************/
 /************************** PRIMITIVE RENDERING *****************************/
 /****************************************************************************/

	public void fillRect(int x, int y, int width, int height) {
		drawRect(x,y,width,height,GL11.GL_QUADS,color);
	}

    public void drawRect(int x, int y, int width, int height) {
		drawRect(x,y,width,height,GL11.GL_LINE_LOOP,color);
  	}

    public void clearRect(int x, int y, int width, int height) {
		drawRect(x,y,width,height,GL11.GL_QUADS,background);
    }

	public void drawLine(int x1, int y1, int x2, int y2) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);


		GL11.glColor4f((float) color.getRed() / 255f,
					   (float) color.getGreen() / 255f,
					   (float) color.getBlue() / 255f,
					   (float) color.getAlpha() / 255f);
		GL11.glLineWidth(1.0f);

		GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2f(x1, y1);
			GL11.glVertex2f(x2, y2);
		GL11.glEnd();

		GL11.glColor3f(1.0f,1.0f,1.0f);


		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}


 /****************************************************************************/
 /************************** GET / SET OPERATION *****************************/
 /****************************************************************************/

	public void setColor(Color c) {
		color = c;
	}

	public Color getColor() {
		return color;
	}

    public void setBackground(Color color) {
		background = color;
    }

    public Color getBackground() {
	    return background;
    }

    public void setComposite(Composite comp) {
	    composite = comp;
    }

    public Composite getComposite() {
	    return composite;
    }

    public Font getFont() {
		if (font == null) {
			setFont(new Font("Dialog", Font.PLAIN, 12));
		}

		return font;
    }

    public void setFont(Font font) {
	    if (font != null) {
			this.font = font;

	   		FontMetrics fm = getFontMetrics(font);
	   		gap = fm.getDescent() - fm.getMaxAscent() - fm.getMaxDescent() - fm.getLeading();
		}
    }

    public FontMetrics getFontMetrics() {
		return getFontMetrics(getFont());
    }

    public FontMetrics getFontMetrics(Font f) {
		if (g == null) {
			// dummy graphics only to get system font metrics
			g = ImageUtil.createImage(1,1).createGraphics();
		}

		return g.getFontMetrics(f);
    }

    public void dispose() {
	}


 /****************************************************************************/
 /************************* GRAPHICS OPERATION *******************************/
 /****************************************************************************/

    public void clipRect(int x, int y, int width, int height) {
		setClip(x,y,width,height);
    }

    public void setClip(int x, int y, int width, int height) {
		GL11.glGetIntegerv(GL11.GL_VIEWPORT, display);

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(x,display.get(3)-y-height,width,height);

		if (clipArea == null) {
			clipArea = NULL_RECTANGLE;
		}

		clipArea.setBounds(x, y, width, height);
    }

	public Shape getClip() {
	    return clipArea;
	}

    public void setClip(Shape clip) {
	    clipArea = (Rectangle) clip;

	    if (clipArea == null) {
		    GL11.glDisable(GL11.GL_SCISSOR_TEST);

		} else {
		    setClip(clipArea.x, clipArea.y, clipArea.width, clipArea.height);
		}
	}


 /****************************************************************************/
 /**************************** PRIVATE METHODS *******************************/
 /****************************************************************************/

    private void startPainting() {
	    if (composite != null) {
		    try {
				GL11.glColor4f(1.0f, 1.0f, 1.0f,
							   ((AlphaComposite) composite).getAlpha());
			} catch (ClassCastException e) { }
		}
	}

	private void endPainting() {
	    if (composite != null) {
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
		}
	}

  	private void drawRect(int x, int y, int width, int height,
	  					  int type, Color col) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);


		GL11.glColor4f((float) col.getRed() / 255f,
					   (float) col.getGreen() / 255f,
					   (float) col.getBlue() / 255f,
					   (float) col.getAlpha() / 255f);
		GL11.glLineWidth(1.0f);

		GL11.glBegin(type);
			GL11.glVertex2f(x, 		 y);
			GL11.glVertex2f(x+width, y);
			GL11.glVertex2f(x+width, y+height);
			GL11.glVertex2f(x, 		 y+height);
		GL11.glEnd();

		GL11.glColor3f(1.0f,1.0f,1.0f);


		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	private GameFont getGameFont() {
		Font f = getFont();
		GameFont gameFont = (GameFont) fontMap.get(f);

		if (gameFont == null) {
			BufferedImage bitmap = FontUtil.createBitmapFont(f, Color.BLACK);

			int delimiter = bitmap.getRGB(0,0); // pixel <0,0> : delimiter
			int[] width = new int[100]; 		// assumption : 100 letter
			int ctr = 0;
			int last = 0;  // last width point

			for (int i=1;i < bitmap.getWidth();i++) {
				if (bitmap.getRGB(i, 0) == delimiter) {
					// found delimiter
					width[ctr++] = i - last;
					last = i;

				    if (ctr >= width.length) {
						width = (int[]) Utility.expand(width, 50);
					}
				}
			}

			// create bitmap font
			BufferedImage[] imagefont = new BufferedImage[ctr];
			int height = bitmap.getHeight() - 1;
			int w = 0;
			for (int i=0;i < imagefont.length;i++) {
				imagefont[i] = bitmap.getSubimage(w, 1, width[i], height);

				w += width[i];
			}

			gameFont = new AdvanceBitmapFont(imagefont);

		    fontMap.put(f, gameFont);
		}

		return gameFont;
	}


}