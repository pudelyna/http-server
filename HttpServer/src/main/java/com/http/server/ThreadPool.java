package com.http.server;

import java.util.concurrent.ArrayBlockingQueue;

import com.http.server.constants.Constants;

/**
 * 
 * ThreadPool interface.
 * 
 * @author AndreeaSandru
 */
public interface ThreadPool {

	/** Queue to hold idle threads */
	public static final ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(Constants.POOL_SIZE);

	/**
	 * Dispatch job.
	 * @param job
	 * @return true if job executed successfully.
	 */
	public boolean dispatch(Runnable job);

	/**
	 * Shutdown the thread pool.
	 */
	public void shutDown();
}
