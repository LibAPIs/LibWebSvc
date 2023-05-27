package com.mclarkdev.tools.libwebsvc;

import com.mclarkdev.tools.libextras.LibExtrasHashes;

public class LibWebSvcAuthGenerator {

	public static void main(String[] args) {

		String passHash = LibExtrasHashes.sumSHA256(args[1].getBytes());
		String userAuth = String.format("%s:%s", args[0], passHash);

		System.out.println(userAuth);
	}
}
