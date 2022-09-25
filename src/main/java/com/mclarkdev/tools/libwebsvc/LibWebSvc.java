package com.mclarkdev.tools.libwebsvc;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import com.mclarkdev.tools.libwebsvc.handler.LibWebSvcAPI;
import com.mclarkdev.tools.libwebsvc.handler.LibWebSvcWS;

public class LibWebSvc {

	private final HandlerCollection handlers;

	private final Server server;

	public LibWebSvc(int port) {

		this.handlers = new HandlerCollection();

		server = new Server(port);
		server.setHandler(handlers);
	}

	public void start() throws Exception {
		server.start();
	}

	public Server getServer() {
		return server;
	}

	public void addResources() throws Exception {

		// Instantiate the static resource handler and add it to the collection
		ContextHandler apiHandler = new ContextHandler();
		apiHandler.setHandler(new LibWebSvcResources(server));
		apiHandler.setContextPath("/");
		handlers.addHandler(apiHandler);
	}

	public void addHandler(String path, Class<? extends LibWebSvcAPI> clazz) throws Exception {

		LibWebSvcAPI handler = clazz.getConstructor().newInstance();

		ContextHandler context = new ContextHandler();
		context.setContextPath(path);
		context.setHandler(handler);

		handlers.addHandler(context);
	}

	public void addWSHandler(String path, Class<? extends LibWebSvcWS> clazz) throws Exception {

		ContextHandler handler = new ContextHandler();
		handler.setContextPath(path);

		handler.setHandler(new WebSocketHandler() {

			@Override
			public void configure(WebSocketServletFactory factory) {
				factory.register(clazz);
			}
		});

		handlers.addHandler(handler);
	}
}
