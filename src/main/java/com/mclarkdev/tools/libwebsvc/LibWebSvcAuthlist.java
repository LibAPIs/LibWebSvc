package com.mclarkdev.tools.libwebsvc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class LibWebSvcAuthlist {

	private static final HashMap<String, List<String>> authMap = new HashMap<>();

	public static List<String> getAuthlist() {
		return getAuthlist("_default");
	}

	public static List<String> getAuthlist(String listName) {

		List<String> authlist = authMap.get(listName);
		if (authlist == null) {
			try {
				String f = String.format("config/%s.conf", listName);
				authlist = Files.readAllLines(Paths.get(f));
				authMap.put(listName, authlist);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return authlist;
	}
}
