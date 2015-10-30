package com.github.osndok.sha1btl;

/**
 * Used only for type-safety. A hash code is simply a series of bytes.
 *
 * Created by robert on 2015-10-30 14:08.
 */
public
class HashAddress
{
	public final
	byte[] bytes;

	public
	HashAddress(byte[] bytes)
	{
		this.bytes = bytes;
	}
}
