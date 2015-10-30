package com.github.osndok.sha1btl;

/**
 * Methods that are common to both low-level integer-addressed block devices (like a hard-disk) and our newfangled
 * hash-addressed block device.
 *
 * Note that there is intentionally no "blocks free" method, as 'allocation' is not something that most drives care
 * about.
 *
 * Created by robert on 2015-10-30 14:06.
 */
public
interface AbstractBlockDevice
{
	/**
	 * @return the size of each storable/retrievable block, in bytes
	 */
	int getBlockSizeInBytes();

	/**
	 * The number of addressable blocks. Note that there *are* times that this can change, such as if a power-
	 * user swaps out a small drive for a larger one, they might 'dd' the blocks to the new disk, which means
	 * that the filesystem must account for this changing between activations (at least... getting larger? as
	 * one should know that you cannot always fit a larger partition's data into a smaller one).
	 *
	 * @return one more than the maximum valid block address
	 */
	int getNumberOfAddressableBlocks();

}
