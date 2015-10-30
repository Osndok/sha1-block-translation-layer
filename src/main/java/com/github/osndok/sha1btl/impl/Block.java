package com.github.osndok.sha1btl.impl;

/**
 * Created by robert on 2015-10-30 14:48.
 */
class Block
{
	private final
	byte[] bytes;

	public
	Block(int blockSize)
	{
		bytes = new byte[blockSize];
	}

	public
	void nowBecomes(byte[] buffer)
	{
		System.arraycopy(buffer, 0, bytes, 0, bytes.length);
	}

	public
	void readValueInto(byte[] buffer)
	{
		System.arraycopy(bytes, 0, buffer, 0, bytes.length);
	}
}