package com.mclarkdev.tools.libwebsvc.handler;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

import com.mclarkdev.tools.libmetrics.LibMetrics;

/**
 * LibWebSvc // LibWebSvcWS
 */
public abstract class LibWebSvcWS {

	private final String _NAME;

	private final LibMetrics metrics;

	public LibWebSvcWS() {

		String handlerName = getClass().getName();
		this._NAME = handlerName.substring(handlerName.lastIndexOf('.') + 1);

		this.metrics = LibMetrics.instance();
	}

	/**
	 * Returns the name of the implemented handler.
	 * 
	 * @return name of the handler
	 */
	public String getName() {
		return _NAME;
	}

	/**
	 * Returns an instance of the metrics collector.
	 * 
	 * @return metrics collector
	 */
	public LibMetrics getMetrics() {
		return metrics;
	}

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {

		getMetrics().hitCounter("ws", getName(), "close");
		this.onWSClose(statusCode, reason);
	}

	@OnWebSocketError
	public void onError(Throwable t) {

		getMetrics().hitCounter("ws", getName(), "error");
		this.onWSError(t);
	}

	@OnWebSocketConnect
	public void onConnect(Session session) throws IOException {

		getMetrics().hitCounter("ws", getName(), "open");
		this.onWSConnect(session);
	}

	@OnWebSocketMessage
	public void onMessage(Session session, String text) throws IOException {

		getMetrics().hitCounter("ws", getName(), "messages");
		this.onWSMessage(session, text);
	}

	/**
	 * The method to be called in the implemented class.
	 * 
	 * Called when the Websocket is closed.
	 * 
	 * @param code   the exit code
	 * @param reason the exit reason
	 */
	protected abstract void onWSClose(int code, String reason);

	/**
	 * The method to be called in the implemented class.
	 * 
	 * Called when the Websocket produces and error.
	 * 
	 * @param t the throwable
	 */
	protected abstract void onWSError(Throwable t);

	/**
	 * The method to be called in the implemented class.
	 * 
	 * @param session the request session
	 * @throws IOException failure in socket communication
	 */
	protected abstract void onWSConnect(Session session) throws IOException;

	/**
	 * The method to be called in the implemented class.
	 * 
	 * @param session the request session
	 * @param text    the message sent by the client
	 * @throws IOException failure in socket communication
	 */
	protected abstract void onWSMessage(Session session, String text) throws IOException;
}
