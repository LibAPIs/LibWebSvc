package com.mclarkdev.tools.libwebsvc;

public class LibWebSvcOptions {

	private String addr = "0.0.0.0";
	private int port = 8080;

	private int poolMin = 10;
	private int poolMax = 50;

	private int poolTimeout = 60000;

	private int requestMaxSize = 8192;

	/**
	 * Returns the configured bind address
	 * 
	 * @return
	 */
	public String getBindAddr() {
		return this.addr;
	}

	/**
	 * Set the server bind address
	 * 
	 * @param addr
	 */
	public void setBindAddr(String addr) {
		this.addr = addr;
	}

	/**
	 * Returns the configured bind port
	 * 
	 * @return
	 */
	public int getBindPort() {
		return this.port;
	}

	/**
	 * Set the server bind port
	 * 
	 * @param port
	 */
	public void setBindPort(int port) {
		this.port = port;
	}

	/**
	 * Returns the configured minimum thread pool size
	 * 
	 * @return
	 */
	public int getPoolMin() {
		return this.poolMin;
	}

	/**
	 * Set the thread pool minimum size
	 * 
	 * @param poolMin
	 */
	public void setPoolMin(int poolMin) {
		this.poolMin = poolMin;
	}

	/**
	 * Returns the configured maximum thread pool size
	 * 
	 * @return
	 */
	public int getPoolMax() {
		return this.poolMax;
	}

	/**
	 * Set the thread pool maximum size
	 * 
	 * @param poolMax
	 */
	public void setPoolMax(int poolMax) {
		this.poolMax = poolMax;
	}

	/**
	 * Returns the thread pool timeout
	 * 
	 * @return
	 */
	public int getPoolTimeout() {
		return this.poolTimeout;
	}

	/**
	 * Set the thread pool timeout
	 * 
	 * @param timeout
	 */
	public void setPoolTimeout(int timeout) {
		this.poolTimeout = timeout;
	}

	/**
	 * Returns the maximum size of a client request
	 * 
	 * @return
	 */
	public int getRequestMaxSize() {
		return this.requestMaxSize;
	}

	/**
	 * Set the maximum size of a client request
	 * 
	 * @param maxSize
	 */
	public void setRequestMaxSize(int maxSize) {
		this.requestMaxSize = maxSize;
	}
}
