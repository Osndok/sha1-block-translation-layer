package com.github.osndok.sha1btl;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * Created by robert on 2015-11-01 18:01.
 */
public
class Example
{
	public static final byte[] SHA1_HASH_CODE;

	static
	{
		try
		{
			SHA1_HASH_CODE = Hex.decodeHex("aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d".toCharArray());
		}
		catch (DecoderException e)
		{
			throw new RuntimeException(e);
		}
	}
}
