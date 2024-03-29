package com.mclarkdev.tools.libwebsvc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

/**
 * LibWebSvc // LibWebSvcAuthlist
 */
public class LibWebSvcAuthlist {

	private static final HashMap<String, List<String>> authMap = new HashMap<>();

	/**
	 * Returns the default AuthList.
	 * 
	 * @return
	 */
	public static List<String> getAuthlist() {
		return getAuthlist("_default");
	}

	/**
	 * Returns an AuthList with the given name.
	 * 
	 * @param listName name of the AuthList
	 * @return the AuthList
	 */
	public static List<String> getAuthlist(String listName) {

		List<String> authlist = authMap.get(listName);
		if (authlist == null) {
			try {
				String f = String.format("auth/%s.conf", listName);
				authlist = Files.readAllLines(Paths.get(f));
				authMap.put(listName, authlist);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return authlist;
	}
}
