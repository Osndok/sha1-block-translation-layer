package com.github.osndok.sha1btl.impl;

import com.github.osndok.sha1btl.IntegerAddressedBlockDevice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
	void discardBlock(int i) throws IOException
	{
		storage.remove(i);
	}
}
