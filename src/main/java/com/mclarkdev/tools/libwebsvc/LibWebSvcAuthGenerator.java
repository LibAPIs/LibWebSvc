package com.mclarkdev.tools.libwebsvc;

import com.mclarkdev.tools.libextras.LibExtrasHashes;

/**
 * LibWebSvc // LibWebSvcAuthGenerator
 * 
 * Used to generate Username/Password strings for use in AuthLists.
 */
public class LibWebSvcAuthGenerator {

	/**
	 * Output String for use in AuthLists.
	 * 
	 * @param args [username] [password]
	 */
	public static void main(String[] args) {

		if (args.length < 1) {
			System.err.println("missing username");
			System.exit(1);
		}

		if (args.length < 2) {
			System.out.println("missing password");
			System.exit(1);
		}

		String passHash = LibExtrasHashes.sumSHA256(args[1].getBytes());
		String userAuth = String.format("%s:%s", args[0], passHash);

		System.out.println(userAuth);
	}
}
