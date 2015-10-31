package com.github.osndok.sha1btl.impl;

import com.github.osndok.sha1btl.FrequentAccessOptimizationMode;
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
	int getErasureRegionSizeInBlocks()
	{
		return 1;
	}

	public
	void eraseRegion(int j, int startingBlock, int endingBlock) throws IllegalArgumentException
	{
		if (j!=startingBlock || j!=endingBlock)
		{
			final
			String message=String.format("hard disk erasure size is 1 block, so eraseRegion(%d,%d,%d) does not make sense.", j, startingBlock, endingBlock);

			throw new IllegalArgumentException(message);
		}
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

	public
	FrequentAccessOptimizationMode getFrequentAccessOptimizationMode()
	{
		return FrequentAccessOptimizationMode.CENTRALIZED;
	}

	public
	int getEstimatedWriteFatigue()
	{
		return Anecdotal.MAGNETIC_WRITE_FATIGUE;
	}

	public
	long getBitRotRefreshPeriod()
	{
		return Anecdotal.HARD_DISK_LIFE_EXPECTANCY;
	}
}
