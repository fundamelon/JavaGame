package com.golden.gamedev.engine.audio;

// JFC
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

// JAVA LAYER
import javazoom.jl.player.*;
import javazoom.jl.decoder.*;

// GTGE
import com.golden.gamedev.engine.BaseAudioRenderer;


/**
 * Play MP3 sound (*.mp3) using JavaLayer library, <br>
 * JavaLayer library is available to download at
 * <a href="http://www.javazoom.net/javalayer/javalayer.html" target="_blank">
 * http://www.javazoom.net/javalayer/javalayer.html</a>. <p>
 *
 * Make sure the downloaded library is included into your game classpath
 * before using this audio renderer. <p>
 *
 * <b>Note: GTGE is not associated in any way with JavaLayer,
 * this class is only interfacing JavaLayer to be used in GTGE. <br>
 * This class is created and has been tested to be working properly
 * using <em>JavaLayer 1.0</em>.</b> <p>
 *
 * How-to-use <code>JavaLayerMp3Renderer</code> in GTGE Frame Work :
 * <pre>
 *    public class YourGame extends Game {
 *
 *       protected void initEngine() {
 *          super.initEngine();
 *
 *          // set sound effect to use mp3
 *          bsSound.setSampleRenderer(new JavaLayerMp3Renderer());
 *
 *          // set music to use mp3
 *          bsMusic.setSampleRenderer(new JavaLayerMp3Renderer());
 *       }
 *
 *    }
 * </pre>
 */
public class JavaLayerMp3Renderer extends BaseAudioRenderer {


 /****************************** MP3 PLAYER **********************************/

	private Player		player;


 /****************************************************************************/
 /******************************* CONSTRUCTOR ********************************/
 /****************************************************************************/

	/**
	 * Creates a new instance of <code>JavaLayerMp3Renderer</code>.
	 */
	public JavaLayerMp3Renderer() {
	}

    /**
     * <i>Please refer to super class method documentation.</i>
     */
	public boolean isAvailable() {
		return true;
	}

    /**
     * <i>Please refer to super class method documentation.</i>
     */
    protected void playSound(final URL audiofile) {
	    Thread thread = new Thread() {
		    public void run() {
				try {
					player = new Player(new BufferedInputStream(audiofile.openStream()),
										FactoryRegistry.systemRegistry().
										createAudioDevice());

					player.play();
				} catch (IOException e) {
					status = ERROR;
					System.err.println("Can not load audiofile ("+audiofile+": " + e);
				} catch (JavaLayerException e) {
					status = ERROR;
					System.err.println("Problem playing audio: " + e);
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
    }

    /**
     * <i>Please refer to super class method documentation.</i>
     */
    protected void replaySound(URL audiofile) {
	    playSound(audiofile);
    }

    /**
     * <i>Please refer to super class method documentation.</i>
     */
    protected void stopSound() {
		if (player != null) {
			player.close();
			player = null;
		}
    }

    /**
     * <i>Please refer to super class method documentation.</i>
     */
    public int getStatus() {
	    if (player != null) {
		    // return EOS if the sound has been completed played
			return (player.isComplete()) ? END_OF_SOUND : super.getStatus();
		}

		return super.getStatus();
	}

    /**
     * <i>Please refer to super class method documentation.</i>
     */
    public boolean isVolumeSupported() {
		return false;
	}

	/**
	 * Testing the MP3 Player.
	 */
	public static void main(String args[]) {
		BaseAudioRenderer mp3 = new JavaLayerMp3Renderer();

		String music1 = "file:///d:/golden t studios/trash/sound/MUSIC1.MP3";
		String music2 = "file:///d:/golden t studios/trash/sound/music2.mp3";

		if (args != null) {
			if (args.length >= 1) music1 = music2 = args[0];
			if (args.length >= 2) music1 = args[1];
		}

		// test first song
		try {
			mp3.play(new URL(music1));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// hear some music
		int update = 0;
		while (mp3.getStatus() == BaseAudioRenderer.PLAYING) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) { }

			System.out.println(update++);

			if (update > 4) {
				mp3.stop();
			}
		}

		// first song stopped
		System.out.println("end-song");

		// test second song
		try {
			mp3.play(new URL(music2));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// wait until complete
		while (mp3.getStatus() == BaseAudioRenderer.PLAYING) {
			try { Thread.sleep(1000);
			} catch (InterruptedException e) { }

			System.out.println(update++);
		}

		System.out.println("end-song 2");
	}


}