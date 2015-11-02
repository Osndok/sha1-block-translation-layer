package com.github.osndok.sha1btl.impl;

import com.github.osndok.sha1btl.HashAddressedBlockDevice;
import com.github.osndok.sha1btl.HashBlockStats;
import com.github.osndok.sha1btl.HashCollision;
import com.github.osndok.sha1btl.HashDeviceStats;
import com.github.osndok.sha1btl.Sha1HashAddress;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by robert on 2015-11-01 17:50.
 */
public
class FakeHashBlockStore implements HashAddressedBlockDevice
{
	private final
	int blockSize;

	private final
	Map<Sha1HashAddress, Block> storage = new ConcurrentHashMap<Sha1HashAddress, Block>();

	public
	FakeHashBlockStore(int blockSize)
	{
		this.blockSize = blockSize;
	}

	public
	int getHashSize()
	{
		return 20;
	}

	public
	HashDeviceStats getLocalStatistics()
	{
		return null;
	}

	public
	HashDeviceStats getRecursiveStatistics()
	{
		return null;
	}

	public
	void readBlock(Sha1HashAddress sha1HashAddress, byte[] buffer) throws IOException
	{
		try
		{
			storage.get(sha1HashAddress).readValueInto(buffer);
		}
		catch (NullPointerException e)
		{
			throw new IOException("does not appear to be stored: "+sha1HashAddress, e);
		}
	}

	public
	Sha1HashAddress writeBlock(byte[] buffer) throws IOException, HashCollision
	{
		//BUG: does not do reference counting.
		final
		Sha1HashAddress sha1=computeSha1(buffer);

		final
		Block block=new Block(blockSize);
		{
			block.nowBecomes(buffer);
		}

		storage.put(sha1, block);

		return sha1;
	}

	private
	Sha1HashAddress computeSha1(byte[] buffer)
	{
		try
		{
			final
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			{
				messageDigest.reset();
			}

			return new Sha1HashAddress(messageDigest.digest(buffer));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public
	HashBlockStats statBlock(Sha1HashAddress sha1HashAddress) throws IOException
	{
		//TODO
		return null;
	}

	public
	void demoteBlock(Sha1HashAddress sha1HashAddress) throws IOException
	{

	}

	public
	void unlinkBlock(Sha1HashAddress sha1HashAddress) throws IOException
	{
		storage.remove(sha1HashAddress);
	}

	public
	void sync() throws IOException
	{
		//???
	}

	public
	int getBlockSizeInBytes()
	{
		return blockSize;
	}

	public
	int getNumberOfAddressableBlocks()
	{
		return Integer.MAX_VALUE;
	}
}
