package com.mclarkdev.tools.libwebsvc;

import javax.servlet.http.Cookie;

import com.mclarkdev.tools.libobjectcache.LibObjectCache;
import com.mclarkdev.tools.libobjectcache.LibObjectCacheCachedObject;

public class LibWebSvcSessionCache {

	public static LibObjectCache getCache() {
		return LibObjectCache.getCache("sessions");
	}

	public static LibWebSvcSession getSession(String key) {

		LibObjectCacheCachedObject o = getCache().get(key);
		if (o == null) {
			return null;
		}

		return (LibWebSvcSession) o;
	}

	public static LibWebSvcSession lookupSession(Cookie[] cookies) {

		LibWebSvcSession session;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("session")) {
					session = LibWebSvcSessionCache.getSession(cookie.getValue());
					if (session != null) {
						return session;
					}
				}
			}
		}

		return createNewSession();
	}

	public static LibWebSvcSession createNewSession() {

		// create the new session object
		LibWebSvcSession session = new LibWebSvcSession();

		// add session to the cache
		getCache().put(session.getKey(), session);

		// return the session
		return session;
	}
}
