package com.mclarkdev.tools.libwebsvc;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

/**
 * LibWebSvc // LibWebSvcRequestInfo
 */
public class LibWebSvcRequestInfo {

	private String target;
	private String source;
	private String origin;

	/**
	 * Disallow public instantiation of the request info.
	 */
	private LibWebSvcRequestInfo() {
	}

	/**
	 * Returns the request target.
	 * 
	 * @return the request target
	 */
	public String getTarget() {

		return target;
	}

	/**
	 * Returns the source of the request.
	 * 
	 * @return source of request
	 */
	public String getSource() {

		return source;
	}

	/**
	 * Returns the origin of the request.
	 * 
	 * @return origin of the request
	 */
	public String getOrigin() {

		return origin;
	}

	/**
	 * Returns the request info as a JSON object.
	 * 
	 * @return request info
	 */
	public JSONObject toJSON() {
		return new JSONObject()//
				.put("target", getTarget())//
				.put("source", getSource())//
				.put("origin", getOrigin());
	}

	/**
	 * Returns the request info as a JSON string.
	 */
	public String toString() {
		return toJSON().toString();
	}

	/**
	 * Build an instance of the request info given the Servlet objects.
	 * 
	 * @param request the servlet request
	 * @return the session info
	 */
	public static LibWebSvcRequestInfo fromRequest(HttpServletRequest request) {

		LibWebSvcRequestInfo i = new LibWebSvcRequestInfo();

		i.target = request.getRequestURI();

		String ref = request.getHeader("Referer");
		i.source = (ref != null) ? ref : "API";

		String via = request.getRemoteAddr();
		String ip = request.getHeader("X-Forwarded-For");
		i.origin = (ip != null) ? (ip + " : " + via) : via;

		return i;
	}
}
