package com.mclarkdev.tools.libwebsvc;

import java.net.InetSocketAddress;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import com.mclarkdev.tools.libwebsvc.handler.LibWebSvcAPI;
import com.mclarkdev.tools.libwebsvc.handler.LibWebSvcWS;

/**
 * LibWebSvc // LibWebSvc
 * 
 * A collection of tools for easily starting a Jetty server embedded within your
 * application.
 */
public class LibWebSvc {

	private final HandlerCollection handlers;

	private final Server server;

	/**
	 * Creates an embedded instance of Jetty webserver.
	 * 
	 * @param options server configuration options
	 */
	public LibWebSvc(LibWebSvcOptions options) {

		options = ((options != null) ? options : new LibWebSvcOptions());

		InetSocketAddress bindAddr = InetSocketAddress//
				.createUnresolved(options.getBindAddr(), options.getBindPort());

		ThreadPool pool = new QueuedThreadPool(//
				options.getPoolMax(), options.getPoolMin(), options.getPoolTimeout());

		this.handlers = new HandlerCollection();

		this.server = new Server(pool);
		this.server.setHandler(handlers);

		HttpConfiguration httpConfig = new HttpConfiguration();
		httpConfig.setRequestHeaderSize(options.getRequestMaxSize());

		ServerConnector connector = new ServerConnector(//
				server, new HttpConnectionFactory(httpConfig));

		connector.setHost(bindAddr.getHostName());
		connector.setPort(bindAddr.getPort());

		this.server.setConnectors(new Connector[] { connector });
	}

	/**
	 * Start the Jetty embedded web server.
	 * 
	 * @throws Exception Failed to start Jetty embedded
	 */
	public void start() throws Exception {
		server.start();
	}

	/**
	 * Returns the running Jetty server instance.
	 * 
	 * @return The underlying Jetty server
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * Register static resources handler to serve HTML files from packages files.
	 * 
	 * @param cacheAge Time before resources should expire
	 * @throws Exception Error creating handler
	 */
	public void addResources(long cacheAge) throws Exception {

		// Instantiate new resource handler
		LibWebSvcResources resourceHandler = //
				new LibWebSvcResources(server);
		resourceHandler.setCacheTime(cacheAge);

		// Instantiate context handler and add it to the collection
		ContextHandler apiHandler = new ContextHandler();
		apiHandler.setHandler(resourceHandler);
		apiHandler.setContextPath("/");
		handlers.addHandler(apiHandler);
	}

	/**
	 * Register a new handler by path and class.
	 * 
	 * @param path  The target path
	 * @param clazz The target handler class
	 * @throws Exception Error creating handler
	 */
	public void addHandler(String path, Class<? extends LibWebSvcAPI> clazz) throws Exception {

		LibWebSvcAPI handler = clazz.getConstructor().newInstance();

		ContextHandler context = new ContextHandler();
		context.setContextPath(path);
		context.setHandler(handler);

		handlers.addHandler(context);
	}

	/**
	 * Register a new WebSocket handler by path and class.
	 * 
	 * @param path  The target path
	 * @param clazz The target handler class
	 * @throws Exception Error creating handler
	 */
	public void addWSHandler(String path, Class<? extends LibWebSvcWS> clazz) throws Exception {

		ContextHandler handler = new ContextHandler();
		handler.setContextPath(path);

		handler.setHandler(new WebSocketHandler() {

			@Override
			public void configure(WebSocketServletFactory factory) {
				factory.getPolicy().setMaxTextMessageSize(16 * 1024 * 1024);
				factory.getPolicy().setMaxBinaryMessageSize(0);
				factory.register(clazz);
			}
		});

		handlers.addHandler(handler);
	}
}
