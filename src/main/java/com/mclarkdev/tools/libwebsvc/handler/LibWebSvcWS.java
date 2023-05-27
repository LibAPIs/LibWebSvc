package com.mclarkdev.tools.libwebsvc.handler;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

import com.mclarkdev.tools.libmetrics.LibMetrics;

public abstract class LibWebSvcWS {

	private final String _NAME;

	private final LibMetrics metrics;

	public LibWebSvcWS() {

		String handlerName = getClass().getName();
		this._NAME = handlerName.substring(handlerName.lastIndexOf('.') + 1);

		this.metrics = LibMetrics.instance();
	}

	public String getName() {
		return _NAME;
	}

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

	protected abstract void onWSClose(int code, String reason);

	protected abstract void onWSError(Throwable t);

	protected abstract void onWSConnect(Session session) throws IOException;

	protected abstract void onWSMessage(Session session, String text) throws IOException;
}
