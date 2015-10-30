package com.github.osndok.sha1btl;

/**
 * Created by robert on 2015-10-30 14:25.
 */
public
interface HashBlockStats
{
	long getTimeReceived();
	long getAccessTime();
	int getNumReferences();
	int getBlockId();
}
