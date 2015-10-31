package com.github.osndok.sha1btl.impl;

import com.github.osndok.sha1btl.FrequentAccessOptimizationMode;

/**
 * Created by robert on 2015-10-30 14:52.
 */
public
class FakeSSD extends FakeHardDisk
{
	public
	FakeSSD(int blockSize, int numBlocks)
	{
		super(blockSize, numBlocks);
	}

	@Override
	public
	int getEstimatedWriteFatigue()
	{
		return Anecdotal.FLASH_WRITE_FATIGUE;
	}

	@Override
	public
	long getBitRotRefreshPeriod()
	{
		return Anecdotal.FLASH_CELL_POWERED_LIFE_EXPECTANCY;
	}

	@Override
	public
	FrequentAccessOptimizationMode getFrequentAccessOptimizationMode()
	{
		return FrequentAccessOptimizationMode.DIFFUSE;
	}
}
