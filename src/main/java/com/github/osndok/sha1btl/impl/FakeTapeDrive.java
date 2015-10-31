package com.github.osndok.sha1btl.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * I have no idea why someone would want to apply this to a tape drive, but it seems easy enough to simulate,
 * so it is included here for completeness sake. In reality, you would surly want to use LTFS instead. On the
 * other hand... apart from the magnitude time difference, tape drives behave quite like a hard drive, so maybe
 * it would be good to test against for comparing optimization algorithms?
 *
 * @url https://en.wikipedia.org/wiki/Linear_Tape_File_System
 *
 * Created by robert on 2015-10-31 15:39.
 */
public
class FakeTapeDrive extends FakeHardDisk
{
	private static final
	Logger log = LoggerFactory.getLogger(FakeTapeDrive.class);

	private final
	long seekTimePerBlockMillis;

	public
	FakeTapeDrive(int blockSize, int numBlocks, long endToEndSeekTimeMillis)
	{
		super(blockSize, numBlocks);

		this.seekTimePerBlockMillis = endToEndSeekTimeMillis / numBlocks;
	}

	int blockNumberUnderTapeHead;

	public
	int getBlockNumberUnderTapeHead()
	{
		return blockNumberUnderTapeHead;
	}

	public
	void setBlockNumberUnderTapeHead(int blockNumberUnderTapeHead)
	{
		this.blockNumberUnderTapeHead = blockNumberUnderTapeHead;
	}

	private
	void incurSeekTimeToMoveToBlock(int blockNumber)
	{
		final
		int numBlocks=Math.abs(blockNumberUnderTapeHead-blockNumber);

		final
		long millis=numBlocks*seekTimePerBlockMillis;

		if (millis>0)
		{
			log.info("seeking to block {} will take {} milliseconds", blockNumber, millis);

			try
			{
				Thread.sleep(millis);
			}
			catch (InterruptedException e)
			{
				log.error("interrupted", e);
			}
		}

		blockNumberUnderTapeHead=blockNumber;
	}

	@Override
	public
	void readBlock(int i, byte[] output) throws IOException
	{
		incurSeekTimeToMoveToBlock(i);
		super.readBlock(i, output);
		blockNumberUnderTapeHead=i+1;
	}

	@Override
	public
	void writeBlock(int i, byte[] input) throws IOException
	{
		incurSeekTimeToMoveToBlock(i);
		super.writeBlock(i, input);
		blockNumberUnderTapeHead=i+1;
	}
}
