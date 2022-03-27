package com.mclarkdev.tools.libwebsvc;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;

import com.mclarkdev.tools.libwebsvc.handler.LibWebSvcAPI;

public class LibWebSvcResources extends LibWebSvcAPI {

	private ResourceHandler resources;

	public LibWebSvcResources(Server server) throws Exception {
		super();

		resources = new ResourceHandler();
		resources.setCacheControl("max-age=604800");
		resources.setServer(server);
		resources.setDirectoriesListed(false);
		resources.setResourceBase("resources");
		resources.setRedirectWelcome(true);
		resources.setWelcomeFiles(new String[] { "index.html" });
		resources.start();
	}

	@Override
	public void execute(LibWebSvcRequestContext request) throws Exception {

		request.getBaseRequest().setHandled(false);
		resources.handle(//
				request.getTarget(), request.getBaseRequest(), //
				request.getRequest(), request.getResponse());
	}
}
