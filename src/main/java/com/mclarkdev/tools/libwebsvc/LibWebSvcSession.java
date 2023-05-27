package com.mclarkdev.tools.libwebsvc;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;

import org.json.JSONObject;

import com.mclarkdev.tools.libobjectcache.LibObjectCacheCachedObject;

public class LibWebSvcSession extends LibObjectCacheCachedObject {

	private final String key;
	private final ConcurrentHashMap<String, Integer> sessionRequests;

	public LibWebSvcSession() {
		this.setTimeout(6, TimeUnit.HOURS);

		this.key = UUID.randomUUID().toString();
		this.sessionRequests = new ConcurrentHashMap<String, Integer>();
	}

	public String getKey() {

		return key;
	}

	public void hit(String data) {

		this.touch();
		if (!sessionRequests.containsKey(data)) {

			sessionRequests.put(data, 1);
			return;
		}

		sessionRequests.put(data, sessionRequests.get(data) + 1);
	}

	public Cookie getCookie() {

		return new Cookie("session", getKey());
	}

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

	@Override
	public String toString() {
		return toJSON().toString();
	}
}
