package com.mclarkdev.tools.libwebsvc.handler;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.JSONObject;

import com.mclarkdev.tools.libextras.LibExtrasHashes;
import com.mclarkdev.tools.liblog.LibLog;
import com.mclarkdev.tools.libmetrics.LibMetrics;
import com.mclarkdev.tools.libwebsvc.LibWebSvcAuthlist;
import com.mclarkdev.tools.libwebsvc.LibWebSvcRequestContext;

/**
 * LibWebSvc // LibWebSvcAPI
 */
public abstract class LibWebSvcAPI extends AbstractHandler {

	private static final MultipartConfigElement MULTI_PART_CONFIG = new MultipartConfigElement("./");

	public enum RequestMethod {
		ANY, GET, POST;
	}

	private final String _NAME;

	private final LibMetrics metrics = LibMetrics.instance();

	private RequestMethod method = RequestMethod.ANY;

	private String authlist = null;

	public LibWebSvcAPI() {

		final String handlerName = getClass().getName();
		this._NAME = handlerName.substring(handlerName.lastIndexOf('.') + 1);
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
	 * Sets the permitted request methods.
	 * 
	 * @param method permitted method
	 */
	public void setPermittedMethod(RequestMethod method) {
		this.method = method;
	}

	/**
	 * Returns the method permitted.
	 * 
	 * @return method permitted
	 */
	public RequestMethod getPermittedMethod() {
		return method;
	}

	/**
	 * Returns the AuthList being used.
	 * 
	 * @return name of auth list
	 */
	public String getAuthlist() {
		return authlist;
	}

	/**
	 * Sets the AuthList to be used.
	 * 
	 * @param authlist name of auth list
	 */
	public void setAuthlist(String authlist) {
		this.authlist = authlist;
	}

	/**
	 * Returns an instance of the metrics collector.
	 * 
	 * @return metrics collector
	 */
	protected LibMetrics getMetrics() {
		return metrics;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		long timeStart = System.currentTimeMillis();
		getMetrics().hitCounter("request", "count");
		getMetrics().hitCounter("request", "method", request.getMethod());
		getMetrics().hitCounter("request", "target", _NAME, "count");
		getMetrics().hitCounter("request", "target", _NAME, "method", request.getMethod());

		// skip if already handled
		if (!baseRequest.isHandled()) {
			baseRequest.setHandled(true);
		} else {
			return;
		}

		// create context and log
		LibWebSvcRequestContext webRequest = //
				LibWebSvcRequestContext.fromRequest(request, response, target, baseRequest);
		LibLog.log("web", webRequest.getRequestInfo().toString());

		// update scheme if requested via proxy
		String proto = request.getHeader("X-Forwarded-Proto");
		if (proto != null) {
			baseRequest.getMetaData().getURI().setScheme(proto);
		}

		// set allow multipart
		if (request.getContentType() != null && request.getContentType().startsWith("multipart/")) {
			request.setAttribute(Request.MULTIPART_CONFIG_ELEMENT, MULTI_PART_CONFIG);
		}

		// get user session info
		webRequest.getSession().hit(baseRequest.getOriginalURI().toString());
		response.addCookie(webRequest.getSession().getCookie());

		// validate correct method for endpoint
		if (this.method != RequestMethod.ANY && //
				!request.getMethod().equals(this.method.toString())) {

			getMetrics().hitCounter("request", "target", _NAME, "result", "_405");
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			response.addHeader("Content-Type", "application/json");
			response.getOutputStream().println((new JSONObject()//
					.put("error", "method not allowed")//
					.put("code", 405)//
			).toString(4));
			return;
		}

		// authenticate the user if required
		if (authlist != null && !validateAdmin(request)) {

			getMetrics().hitCounter("request", "authfail", request.getMethod());
			response.setHeader("WWW-Authenticate", "Basic realm=" + authlist);

			getMetrics().hitCounter("request", "target", _NAME, "result", "_401");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.addHeader("Content-Type", "application/json");
			response.getOutputStream().println((new JSONObject()//
					.put("error", "unauthorized")//
					.put("code", 401)//
			).toString(4));
			return;
		}

		try {

			// call the implemented method
			response.setStatus(HttpServletResponse.SC_OK);
			this.execute(webRequest);
			getMetrics().hitCounter("request", "target", _NAME, "result", "_" + response.getStatus());
		} catch (Exception e) {

			// catch errors / exceptions from implementation
			getMetrics().hitCounter("request", "target", _NAME, "result", "_500");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.addHeader("Content-Type", "application/json");
			response.getOutputStream().println((new JSONObject()//
					.put("error", "internal server error")//
					.put("type", e.getClass().toString())//
					.put("message", e.getMessage())//
					.put("code", 500)//
			).toString(4));
			return;
		}

		// hit the counters
		long targetTime = System.currentTimeMillis() - timeStart;
		getMetrics().hitCounter(targetTime, "request", "time");
		getMetrics().hitCounter(targetTime, "request", "target", _NAME, "time");
	}

	/**
	 * Called to validate if authorization required and check for user login.
	 * 
	 * @param request the servlet request
	 * @return if user is permitted
	 */
	protected boolean validateAdmin(HttpServletRequest request) {

		// false if no authentication
		String auth = request.getHeader("Authorization");
		if (auth == null || !auth.startsWith("Basic")) {
			return false;
		}

		String authString = auth.substring(6);
		String decode = new String(Base64.getDecoder().decode(authString));
		String[] unpw = decode.split(":");
		if (unpw.length != 2) {
			return false;
		}

		String passHash = LibExtrasHashes.sumSHA256(unpw[1].getBytes());
		String userAuth = String.format("%s:%s", unpw[0], passHash);

		return LibWebSvcAuthlist.getAuthlist(authlist).contains(userAuth);
	}

	/**
	 * The method to be called in the implemented class.
	 * 
	 * @param request the request context
	 * @throws Exception failure executing implemented method
	 */
	public abstract void execute(LibWebSvcRequestContext request) throws Exception;
}
