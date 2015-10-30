package com.github.osndok.sha1btl;

/**
 * Created by robert on 2015-10-30 14:14.
 */
public
interface HashDeviceStats
{
	int getNumBlocksStored();
	int getNumBlocksFree();
	int getNumBlocksInLimbo();
}
