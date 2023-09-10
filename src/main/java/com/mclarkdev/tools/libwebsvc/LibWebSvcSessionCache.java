package com.mclarkdev.tools.libwebsvc;

import javax.servlet.http.Cookie;

import com.mclarkdev.tools.libobjectcache.LibObjectCache;
import com.mclarkdev.tools.libobjectcache.LibObjectCacheCachedObject;

/**
 * LibWebSvc // LibWebSvcSessionCache
 */
public class LibWebSvcSessionCache {

	/**
	 * Returns a static instance of the web session cache.
	 * 
	 * @return web session cache
	 */
	public static LibObjectCache getCache() {
		return LibObjectCache.getCache("webSessions");
	}

	/**
	 * Lookup an existing session object in the cache with a given Session key.
	 * 
	 * @param key the session key
	 * @return the cached session object
	 */
	public static LibWebSvcSession getSession(String key) {

		LibObjectCacheCachedObject o = getCache().get(key);
		if (o == null) {
			return null;
		}

		return (LibWebSvcSession) o;
	}

	/**
	 * Lookup an existing session object in the cache.
	 * 
	 * @param cookies the request cookies
	 * @return the cached session object
	 */
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

	/**
	 * Create a new session object.
	 * 
	 * @return session object
	 */
	public static LibWebSvcSession createNewSession() {

		// create the new session object
		LibWebSvcSession session = new LibWebSvcSession();

		// add session to the cache
		getCache().put(session.getKey(), session);

		// return the session
		return session;
	}
}
