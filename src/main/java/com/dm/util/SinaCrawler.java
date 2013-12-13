package com.dm.util;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class SinaCrawler {

	public static void main(String[] args) throws Exception {
		char hexdigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };

		Cipher cipher = Cipher.getInstance("RSA");
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		KeySpec pubKeySpec = new RSAPublicKeySpec(
				new BigInteger(
						"EB2A38568661887FA180BDDB5CABD5F21C7BFD59C090CB2D245A87AC253062882729293E5506350508E7F9AA3BB77F4333231490F915F6D63C55FE2F08A49B353F444AD3993CACC02DB784ABBB8E42A9B1BBFFFB38BE18D78E87A0E41B9B8F73A928EE0CCEE1F6739884B9777E4FE9E88A1BBE495927AC4A799B3181D6442443",
						16), new BigInteger("10001", 16));
		Key key = keyFactory.generatePublic(pubKeySpec);
		cipher.init(Cipher.ENCRYPT_MODE, key);

		String servertime = "1386925363";
		String nonce = "6CM42Z";
		String passwd = "pwd";

		String message = servertime + '\t' + nonce + '\n' + passwd;

		byte[] cipherText = cipher.doFinal(message.getBytes());

		int j = cipherText.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = cipherText[i];
			String b = Integer.toBinaryString(byte0);
			System.out.println(b);
			// 01010000
			// 00000101
			// 11111111
			str[k++] = hexdigits[byte0 >>> 4 & 0xf];
			str[k++] = hexdigits[byte0 & 0xf];
		}
		String a = new String(str);
		System.out.println(a);
	}

}
