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
	 * For non-solid-state media (like hard disks, CDs, and Blu-ray disks... but probably even tape-drives,
	 * warehouse robots and other fun and cooky experiments), it is often the case that having frequently
	 * accessed blocks stored (or more literally read) closer towards the *center* of the available address
	 * space (as opposed to the near-zero and near-max edges). To my understanding, SSDs and MTDs do not
	 * exhibit this effect, and would actually have a slight benefit from the data being "spread out" or
	 * "clumped in the front" depending on what your concern is.
	 */
	FrequentAccessOptimizationMode getFrequentAccessOptimizationMode();

	/**
	 * @return the number of times that a block (or erasure region) can be written (or erased) before it is likely to result in an unusable block.
	 */
	int getEstimatedWriteFatigue();

	/**
	 * @url https://en.wikipedia.org/wiki/Data_degradation
	 * @return the estimated time (in milliseconds) that data stored on this device will last (e.g. "shelf life") before a refresh is required.
	 */
	long getBitRotRefreshPeriod();

	/**
	 * Since we have some interest in venturing into the crazy world of MTD devices, where the minimum erasable region
	 * is VERY BIG compared to the minimum writable block/page... we might as well impose the same logic onto all
	 * subordinate block devices. Then, conventional block devices become the "special case", where they can erase
	 * one block at a time. In a larger, more generalized sense, this same idea might be mappable onto "Storage Pods"
	 * if (after so many disk failures) one desires to migrate all the data off of a pod for servicing.
	 *
	 * @return the number of blocks that must be erased at the same time (1 for conventional block devices)
	 */
	int getErasureRegionSizeInBlocks();

	/**
	 * This is expressed in terms for the MTD experiments, but this method covers both he erase-block/region command
	 * AND the SSD discard() command. For SSDs (and single-block erasable partitions in general), all the arguments
	 * should mathematically come out to be the same integer values.
	 *
	 * @param j - the region number, starting from zero
	 * @param startingBlock - to be doubly sure, this is what the program 'thinks' is the first block being erased
	 * @param endingBlock - to be doubly sure, this is what the program 'thinks' is the last block being erased
	 * @throws IllegalArgumentException - if the given arguments do not 'jive' with the device's erasure block size.
	 */
	void eraseRegion(int j, int startingBlock, int endingBlock) throws IllegalArgumentException;
}
