package com.github.osndok.sha1btl.impl;

import com.github.osndok.sha1btl.IntegerAddressedBlockDevice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by robert on 2015-10-30 14:39.
 */
public
class FakeHardDisk implements IntegerAddressedBlockDevice
{
	private final
	int blockSize;

	private final
	int numBlocks;

	protected final
	Map<Integer, Block> storage = new HashMap<Integer, Block>();

	public
	FakeHardDisk(int blockSize, int numBlocks)
	{
		this.blockSize = blockSize;
		this.numBlocks = numBlocks;
	}

	private
	Block getBlock(final Integer i)
	{
		Block retval = storage.get(i);

		if (retval == null)
		{
			retval = new Block(blockSize);
			storage.put(i, retval);
		}

		return retval;
	}

	public
	void readBlock(int i, byte[] output) throws IOException
	{
		getBlock(i).readValueInto(output);
	}

	public
	void writeBlock(int i, byte[] input) throws IOException
	{
		getBlock(i).nowBecomes(input);
	}

	public
	void discardBlock(int i) throws IOException
	{
		//Yea.. we could *actually* remove it, but that wouldn't be 'hard-drive like', and... we might catch
		//'block leaks' this way.
		//dont: storage.remove(i);
	}

	public
	Integer getErasureRegionSizeInBlocks()
	{
		return null;
	}

	public
	void eraseRegion(int j, int startingBlock, int endingBlock) throws IllegalArgumentException
	{
		throw new UnsupportedOperationException();
	}

	public
	int getBlockSizeInBytes()
	{
		return blockSize;
	}

	public
	int getNumberOfAddressableBlocks()
	{
		return numBlocks;
	}

}
