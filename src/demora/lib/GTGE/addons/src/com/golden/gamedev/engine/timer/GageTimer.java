package com.golden.gamedev.engine.timer;

// GAGE
import com.dnsalias.java.timer.AdvancedTimer;

// GTGE
import com.golden.gamedev.engine.BaseTimer;


/**
 * High-Resolution Timer builds based on Genuine Advantage Gaming Engine (GAGE)
 * Timer, <br>
 * GAGE Timer is available to download at
 * <a href="http://java.dnsalias.com/" target="_blank">http://java.dnsalias.com/</a>. <p>
 *
 * Make sure the downloaded library is included into your game classpath
 * before using this timer. <p>
 *
 * <b>Note: GTGE is not associated in any way with GAGE Timer,
 * this class is only interfacing GAGE Timer to be used in GTGE.</b> <p>
 *
 * How-to-use <code>GageTimer</code> in GTGE Frame Work :
 * <pre>
 *    public class YourGame extends Game {
 *
 *       protected void initEngine() {
 *          // initialize timer engine
 *          bsTimer = new GageTimer();
 *
 *          super.initEngine();
 *       }
 *
 *    }
 * </pre>
 *
 * @see <a href="http://java.dnsalias.com/" target="_blank">GAGE official site</a>
 */
public class GageTimer implements BaseTimer {

    private final AdvancedTimer timer;

    private boolean		running;

    private int	    	fps = 100;

    private long  		sleepTime;
	private long  		lastTicks;	// last sleep time
	private long		resolution;

	private long		startTime;

	private FPSCounter	fpsCounter;


 /****************************************************************************/
 /******************************* CONSTRUCTOR ********************************/
 /****************************************************************************/

    /**
     * Creates a new instance of <code>GageTimer</code>.
     *
     * @see #startTimer()
     */
    public GageTimer() {
        timer = new AdvancedTimer();
        fpsCounter = new FPSCounter();
    }


 /****************************************************************************/
 /**************************** START/STOP TIMER ******************************/
 /****************************************************************************/

    /**
     * <i>Please refer to super class method documentation.</i>
     */
	public void startTimer() {
	    if (running) stopTimer();
	    running = true;

        timer.start();

        resolution = timer.getTicksPerSecond() / 1000;
		sleepTime = timer.getTicksPerSecond() / fps;
        lastTicks = 0;
        startTime = getTime();

        fpsCounter.refresh();
    }

    /**
     * <i>Please refer to super class method documentation.</i>
     */
    public void stopTimer() {
		if (running) {
			timer.stop();
			running = false;
		}
	}


 /****************************************************************************/
 /************************** MAIN-METHOD: SLEEP() ****************************/
 /****************************************************************************/

    /**
     * <i>Please refer to super class method documentation.</i>
     */
    public long sleep() {
        timer.sleepUntil(lastTicks + sleepTime);
        lastTicks += sleepTime;

        fpsCounter.calculateFPS();

		long endTime = getTime();
		long elapsedTime = endTime - startTime;
		startTime = endTime;

        return elapsedTime;
    }


 /****************************************************************************/
 /***************************** OTHER FUNCTIONS ******************************/
 /****************************************************************************/

    /**
     * <i>Please refer to super class method documentation.</i>
     */
	public boolean isRunning() {
		return running;
	}

    /**
     * <i>Please refer to super class method documentation.</i>
     */
    public int getCurrentFPS() {
		return fpsCounter.getCurrentFPS();
	}

    /**
     * <i>Please refer to super class method documentation.</i>
     */
	public int getFPS() {
		return fps;
	}

    /**
     * <i>Please refer to super class method documentation.</i>
     */
	public void setFPS(int fps) {
		if (this.fps == fps) return;
		this.fps = fps;

		if (running) {
			startTimer();
		}
	}

    /**
     * <i>Please refer to super class method documentation.</i>
     */
    public long getTime() {
	    return timer.getClockTicks() / resolution;
	}

    /**
     * <i>Please refer to super class method documentation.</i>
     */
	public void refresh() {
		startTime = getTime();
	}

}