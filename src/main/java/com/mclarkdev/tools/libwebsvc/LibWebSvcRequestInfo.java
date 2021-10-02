package com.mclarkdev.tools.libwebsvc;

import javax.servlet.http.HttpServletRequest;

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

	public String toString() {

		return String.format("%s:%s:%s", getTarget(), getSource(), getOrigin());
	}
}
