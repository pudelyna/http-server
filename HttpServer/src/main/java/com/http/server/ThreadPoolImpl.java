package com.http.server;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.http.server.constants.Constants;

/**
 * Thread pool implementation based on
 * @see java.util.concurrent.ThreadPoolExecutor
 * 
 * @author AndreeaSandru
 */
public class ThreadPoolImpl extends ThreadPoolExecutor implements ThreadPool {

	/** log4j logger for current class */
	private static final Logger log = Logger.getLogger(ThreadPoolImpl.class);

	/**
	 * Creates a new ThreadPool instance
	 */
	public ThreadPoolImpl() {
		super(Constants.POOL_SIZE, Constants.MAX_POOL_SIZE, Constants.KEEP_ALIVE_TIME, TimeUnit.MINUTES, workQueue);
	}

	/**
	 * Transfers the control over the request thread to the ThreadPoolExecutor
	 * 
	 * @see com.http.server.ThreadPool#dispatch(java.lang.Runnable)
	 */
	@Override
	public boolean dispatch(Runnable job) {
		try {
			super.execute(job);
			log.info("Request queue count.." + workQueue.size());
			return true;
		} catch (RejectedExecutionException e) {
			return false;
		}
	}

	/**
	 * Shuts down the thread pool and kills all of the request threads
	 */
	@Override
	public void shutDown() {
		super.shutdown();
	}

}
