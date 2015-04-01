package com.wellpoint.mobility.aggregation.core.utilities;

/**
 * Stores a start and stop time and will display elasped time.
 */
public class StopWatch {
	long startTime;
	boolean isRunning;
	long stopTime;

	/**
	 * Creates a new stop watch object.
	 */
	public StopWatch() {
		// Throwable t = new Throwable();
		// System.out.println("StopWatch.ctor() - creating new StopWatch from: "
		// + Tools.getPrintableStackTrace(t));
	}

	/**
	 * Returns the elapsed time in milliseconds
	 * 
	 * @return long The elapsed time in milliseconds
	 */
	public long getElasped() {
		if (isRunning)
			return System.currentTimeMillis() - startTime;
		else
			return stopTime - startTime;
	}

	/**
	 * Restarts the stop watch. Creation date: (08/23/2000 12:47:42 PM)
	 */
	public void restart() {
		startTime = System.currentTimeMillis();
		isRunning = true;
	}

	/**
	 * If the stop watch is running, stops it. Creation date: (08/23/2000
	 * 12:44:20 PM)
	 * 
	 * @return java.lang.String The running time in milliseconds
	 */
	public String stop() {
		if (isRunning) {
			stopTime = System.currentTimeMillis();
			isRunning = false;
		}

		long runningMS = stopTime - startTime;

		return runningMS + "ms";
	}
}
