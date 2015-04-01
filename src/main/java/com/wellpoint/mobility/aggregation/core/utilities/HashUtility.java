package com.wellpoint.mobility.aggregation.core.utilities;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class HashUtility
{
	public static String hashValue(String text)
	{
		byte[] theDigest = null;
		String hashString = null;
		try
		{
			byte[] bytesOfMessage = text.getBytes("UTF-8");

			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			theDigest = messageDigest.digest(bytesOfMessage);
			System.out.println(messageDigest.getDigestLength());
			hashString = Base64.encodeBase64String(theDigest);
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}

		return hashString;
	}
}
