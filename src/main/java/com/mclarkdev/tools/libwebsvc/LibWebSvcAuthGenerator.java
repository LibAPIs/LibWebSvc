package com.mclarkdev.tools.libwebsvc;

public class LibWebSvcAuthGenerator {

	public static void main(String[] args) {

		String passHash = LibWebSvcUtils.sumSHA256(args[1].getBytes());
		String userAuth = String.format("%s:%s", args[0], passHash);

		System.out.println(userAuth);
	}
}
