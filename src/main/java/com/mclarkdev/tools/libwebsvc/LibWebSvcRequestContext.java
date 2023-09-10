package com.mclarkdev.tools.libwebsvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.eclipse.jetty.server.Request;

/**
 * LibWebSvc // LibWebSvcRequestContext
 */
public class LibWebSvcRequestContext {

	private HttpServletRequest request;
	private HttpServletResponse response;

	private String target;
	private Request baseRequest;

	private LibWebSvcRequestInfo requestInfo;
	private LibWebSvcSession requestSession;

	/**
	 * Disallow public instantiation of the request context.
	 */
	private LibWebSvcRequestContext() {

	}

	/**
	 * Returns the requested target.
	 * 
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Returns the underlying request.
	 * 
	 * @return the request
	 */
	public Request getBaseRequest() {
		return baseRequest;
	}

	/**
	 * Returns the underlying servlet request.
	 * 
	 * @return the servlet request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * Returns the underlying servlet response.
	 * 
	 * @return the servlet response
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * Returns details about the original request.
	 * 
	 * @return request info
	 */
	public LibWebSvcRequestInfo getRequestInfo() {
		return requestInfo;
	}

	/**
	 * Returns the request session context.
	 * 
	 * @return session context
	 */
	public LibWebSvcSession getSession() {
		return requestSession;
	}

	/**
	 * Returns the value of a request parameter.
	 * 
	 * @param parameter the parameter name
	 * @return the value
	 */
	public String getParameter(String parameter) {
		return request.getParameter(parameter);
	}

	/**
	 * Returns the value of a MultiPart parameter.
	 * 
	 * @param parameter the parameter name
	 * @return the MultiPart value
	 */
	public String getMultipartParameter(String parameter) {
		try {
			Part part = request.getPart(parameter);
			return new String(part.getInputStream().readAllBytes());
		} catch (IOException | ServletException e) {
			return null;
		}
	}

	/**
	 * Build an instance of the request context given the Servlet objects.
	 * 
	 * @param request     the servlet request
	 * @param response    the servlet response
	 * @param target      the request target
	 * @param baseRequest the base request
	 * @return the request context
	 */
	public static LibWebSvcRequestContext fromRequest(//
			HttpServletRequest request, HttpServletResponse response, String target, Request baseRequest) {

		LibWebSvcRequestContext c = new LibWebSvcRequestContext();

		c.request = request;
		c.response = response;

		c.target = target;
		c.baseRequest = baseRequest;

		c.requestInfo = LibWebSvcRequestInfo.fromRequest(request);
		c.requestSession = LibWebSvcSessionCache.lookupSession(request.getCookies());

		// server details
		response.setHeader("Server", "");
		response.setHeader("Server-Node", "");
		response.setHeader("Accept-Charset", "utf-8");
		response.setCharacterEncoding("UTF-8");

		// add CORS headers
		String origin = request.getHeader("origin");
		if (origin != null) {

			response.setHeader("Access-Control-Max-Age", "86400");
			response.setHeader("Access-Control-Allow-Origin", origin);
			response.setHeader("Access-Control-Allow-Credentials", "true");
		}

		if (request.getMethod().equals("OPTIONS")) {
			response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		}

		return c;
	}
}
