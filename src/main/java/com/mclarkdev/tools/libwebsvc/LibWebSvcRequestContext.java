package com.mclarkdev.tools.libwebsvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.eclipse.jetty.server.Request;

public class LibWebSvcRequestContext {

	private final HttpServletRequest request;
	private final HttpServletResponse response;

	private final String target;
	private final Request baseRequest;

	private final LibWebSvcRequestInfo requestInfo;
	private final LibWebSvcSession requestSession;

	public LibWebSvcRequestContext(//
			HttpServletRequest request, HttpServletResponse response, String target, Request baseRequest) {

		this.request = request;
		this.response = response;

		this.target = target;
		this.baseRequest = baseRequest;

		this.requestInfo = new LibWebSvcRequestInfo(request);
		this.requestSession = LibWebSvcSessionCache.lookupSession(request.getCookies());

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
	}

	public String getTarget() {
		return target;
	}

	public Request getBaseRequest() {
		return baseRequest;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public LibWebSvcRequestInfo getRequestInfo() {
		return requestInfo;
	}

	public LibWebSvcSession getSession() {
		return requestSession;
	}

	public String getParameter(String parameter) {
		return request.getParameter(parameter);
	}

	public String getMultipartParameter(String parameter) {
		try {
			Part part = request.getPart("customer");
			return new String(part.getInputStream().readAllBytes());
		} catch (IOException | ServletException e) {
			return null;
		}
	}
}
