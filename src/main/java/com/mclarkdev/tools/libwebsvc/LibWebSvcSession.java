package com.mclarkdev.tools.libwebsvc;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;

import org.json.JSONObject;

import com.mclarkdev.tools.libobjectcache.LibObjectCacheCachedObject;

/**
 * LibWebSvc // LibWebSvcSession
 */
public class LibWebSvcSession extends LibObjectCacheCachedObject {

	private final String key;
	private final ConcurrentHashMap<String, Integer> sessionRequests;

	/**
	 * Create a new session.
	 */
	public LibWebSvcSession() {
		this.setTimeout(_1D);

		this.key = UUID.randomUUID().toString();
		this.sessionRequests = new ConcurrentHashMap<String, Integer>();
	}

	/**
	 * Returns the session key.
	 * 
	 * @return session key
	 */
	public String getKey() {

		return key;
	}

	/**
	 * Hit the session request counter.
	 * 
	 * @param path the request path
	 */
	public void hit(String path) {

		this.touch();
		if (!sessionRequests.containsKey(path)) {

			sessionRequests.put(path, 1);
			return;
		}

		sessionRequests.put(path, sessionRequests.get(path) + 1);
	}

	/**
	 * Returns the session cookie.
	 * 
	 * @return session cookie
	 */
	public Cookie getCookie() {

		return new Cookie("session", getKey());
	}

	/**
	 * Returns the session info as a JSON object.
	 * 
	 * @return session info
	 */
	public JSONObject toJSON() {

		JSONObject requests = new JSONObject();
		for (Map.Entry<String, Integer> e : sessionRequests.entrySet()) {
			requests.put(getKey(), e.getValue());
		}

		return new JSONObject()//
				.put("key", getKey())//
				.put("create", getTimeCreated())//
				.put("last-seen", getTimeLastSeen())//
				.put("expiration", getTimeExpires())//
				.put("request-count", getTouchCount())//
				.put("requests", requests);
	}

	/**
	 * Returns the session info as a JSON string.
	 */
	public String toString() {
		return toJSON().toString();
	}
}
