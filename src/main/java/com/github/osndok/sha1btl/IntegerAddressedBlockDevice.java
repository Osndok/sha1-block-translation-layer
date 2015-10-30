package com.github.osndok.sha1btl;

import java.io.IOException;

/**
 * This is a low lever 'wrapper' around what *should* eventually be the actual block storage
 * device. As with real life, a conventional hard disk will behave differently than a solid-
 * state drive (SSD / flash-drive / mtd).
 *
 * Implicit in this interface is that the addresses fit within a 32-bit space, which (for a
 * now-standard 4k block size) means that it can only address up to 16 petabytes of data.
 *
 * Created by robert on 2015-10-30 13:29.
 */
public
interface IntegerAddressedBlockDevice extends AbstractBlockDevice
{
	/**
	 * Causes the contents of a particular block to be written into the given byte buffer. Returns only when
	 * the contents of the block have been fully stored into the buffer. For some implementations, attempting
	 * to read a block that has not been written to should throw an AssertionError.
	 *
	 * @param i - the block number to be fetched
	 * @param output - a byte buffer (of size blockSize) to be totally filled with the contents of the block
	 * @throws IOException - if the block could not be read, for some reason. In which case the output array might still have been effected.
	 */
	void readBlock(int i, byte[] output) throws IOException;

	/**
	 * Causes the given bytes to be written to the disk/medium, to later be returned by readBlock(). Returns
	 * only after the disk has actually written the bytes "to safety"... which in practice you can never really
	 * be sure (due to write-behind caches).
	 *
	 * @param i - the block number to be written
	 * @param input - the data to be written to the block (which must be blockSize in length)
	 * @throws IOException - if the block could not be written to, for some reason, which might indicate an unusable block number.
	 */
	void writeBlock(int i, byte[] input) throws IOException;

	/**
	 * For some block devices, like SSDs, we may want to issue a discard() command to indicate the block will not be read
	 * again. However, some sources indicate that overuse of discard() could be *VERY* damaging to SSDs, so... maybe not?
	 * If the underlying block device does not have (or need) this extra information, then it should amount to a no-op
	 * (rather than throwing an UnsupportedOperationException).
	 *
	 * @param i - the block number that shall never be read from again... until it is written.
	 * @throws IOException - the block could not be discarded.
	 */
	void discardBlock(int i) throws IOException;

	/**
	 * In case we venture into the MTD/FTL space, where we must do something akin to wear-leveling
	 * and erasing blocks at a time, this will return the size (in blocks) of the region that must be
	 * erased at the same time.
	 *
	 * @return null if this device does not support/require multi-block erasures, otherwise returns the number of blocks that must be zeroed at once
	 */
	Integer getErasureRegionSizeInBlocks();

	/**
	 * For the MTD/FTL experiments, this is the equivalent to issuing the erase-block/region command.
	 *
	 * @param j - the region number, starting from zero
	 * @param startingBlock - to be doubly sure, this is what the program 'thinks' is the first block being erased
	 * @param endingBlock - to be doubly sure, this is what the program 'thinks' is the last block being erased
	 * @throws IllegalArgumentException - if the given arguments do not 'jive' with the device's erasure block size.
	 */
	void eraseRegion(int j, int startingBlock, int endingBlock) throws IllegalArgumentException;
}
