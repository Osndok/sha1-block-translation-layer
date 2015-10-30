package com.github.osndok.sha1btl.impl;

import com.github.osndok.sha1btl.IntegerAddressedBlockDevice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This simulates my understanding of how directly using flash storage should work.
 *
 * TODO: maybe we should track/report "wear", or simulate "wear-related failure"?
 *
 * Created by robert on 2015-10-30 14:55.
 */
public
class FakeFTL implements IntegerAddressedBlockDevice
{
	private final
	int blockSize;

	private final
	int numBlocks;

	private final
	int blocksPerErasureRegion;

	private final
	Map<Integer, Region> regionMap = new HashMap<Integer, Region>();

	public
	FakeFTL(int blockSize, int numBlocks, int blocksPerErasureRegion)
	{
		this.blockSize = blockSize;
		this.numBlocks = numBlocks;
		this.blocksPerErasureRegion = blocksPerErasureRegion;
	}

	private
	Region getRegionForBlock(int blockNumber)
	{
		final
		Integer regionNumber=blockNumber/blocksPerErasureRegion;

		Region region=regionMap.get(regionNumber);

		if (region==null)
		{
			region=new Region(regionNumber);
			regionMap.put(regionNumber, region);
		}

		return region;
	}

	public
	void readBlock(int i, byte[] output) throws IOException
	{
		getRegionForBlock(i).readBlock(i, output);
	}

	public
	void writeBlock(int i, byte[] input) throws IOException
	{
		getRegionForBlock(i).writeBlock(i, input);
	}

	public
	void discardBlock(int i) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	public
	Integer getErasureRegionSizeInBlocks()
	{
		return blocksPerErasureRegion;
	}

	public
	void eraseRegion(int j, int startingBlock, int endingBlock) throws IllegalArgumentException
	{
		final
		Integer regionNumber=j;

		Region region=regionMap.get(regionNumber);

		if (region==null)
		{
			region=new Region(j);
		}

		if (region.startingBlock!=startingBlock)
		{
			throw new IllegalArgumentException(String.format("region %d starts on block %d, not %d", regionNumber, region.startingBlock, startingBlock));
		}

		if (region.endingBlock != endingBlock)
		{
			throw new IllegalArgumentException(String.format("region %d ends on block %d, not %d", regionNumber, region.endingBlock, endingBlock));
		}

		regionMap.remove(regionNumber);
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

	private
	class Region
	{
		final
		int startingBlock;

		final
		int endingBlock;

		private
		Region(int i)
		{
			startingBlock = i*blocksPerErasureRegion;
			endingBlock = (i+1)*blocksPerErasureRegion-1;
		}

		final
		Map<Integer, Block> storage = new HashMap<Integer, Block>(blocksPerErasureRegion);

		public
		void readBlock(int i, byte[] output) throws IOException
		{
			checkRange(i);

			final
			Block block=storage.get(i);

			if (block==null)
			{
				throw new IOException("no value has been stored to block "+i);
			}

			block.readValueInto(output);
		}

		public
		void writeBlock(int i, byte[] input) throws IOException
		{
			checkRange(i);

			final
			Block block=new Block(blockSize);
			{
				block.nowBecomes(input);
			}

			if (storage.put(i, block)!=null)
			{
				throw new IOException("block "+i+" already has a value stored, and region was not erased");
			}
		}

		private
		void checkRange(int i)
		{
			if (i<startingBlock)
			{
				throw new AssertionError("block comes before region's startingBlock: "+i+" < "+startingBlock);
			}

			if (endingBlock < i)
			{
				throw new AssertionError("block comes after region's endingBlock: "+endingBlock+" < "+i);
			}
		}
	}
}
