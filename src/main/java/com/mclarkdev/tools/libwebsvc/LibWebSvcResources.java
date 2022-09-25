package com.mclarkdev.tools.libwebsvc;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;

import com.mclarkdev.tools.libwebsvc.handler.LibWebSvcAPI;

public class LibWebSvcResources extends LibWebSvcAPI {

	private ResourceHandler resources;

	public LibWebSvcResources(Server server) throws Exception {
		super();

		resources = new ResourceHandler();
		resources.setServer(server);
		resources.setDirAllowed(false);
		resources.setDirectoriesListed(false);
		resources.setResourceBase("resources");
		resources.setRedirectWelcome(true);
		resources.setWelcomeFiles(new String[] { "index.html" });
		resources.start();

		setCacheTime(604800);
	}

	public void setCacheTime(long expires) {
		resources.setCacheControl("max-age=" + expires);
	}

	@Override
	public void execute(LibWebSvcRequestContext request) throws Exception {

		request.getBaseRequest().setHandled(false);
		resources.handle(//
				request.getTarget(), request.getBaseRequest(), //
				request.getRequest(), request.getResponse());
	}
}
