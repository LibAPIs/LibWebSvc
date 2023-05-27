package com.mclarkdev.tools.libwebsvc;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

public class LibWebSvcRequestInfo {

	private final String target;
	private final String source;
	private final String origin;

	public LibWebSvcRequestInfo(HttpServletRequest request) {

		this.target = request.getRequestURI();

		String ref = request.getHeader("Referer");
		this.source = (ref != null) ? ref : "API";

		String via = request.getRemoteAddr();
		String ip = request.getHeader("X-Forwarded-For");
		this.origin = (ip != null) ? (ip + " : " + via) : via;
	}

	public String getTarget() {

		return target;
	}

	public String getSource() {

		return source;
	}

	public String getOrigin() {

		return origin;
	}

	public JSONObject toJSON() {
		return new JSONObject()//
				.put("target", getTarget())//
				.put("source", getSource())//
				.put("origin", getOrigin());
	}

	public String toString() {
		return toJSON().toString();
	}
}
