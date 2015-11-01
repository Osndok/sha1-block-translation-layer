package com.github.osndok.sha1btl;

import java.io.IOException;

/**
 * This is the interface that we are ultimately trying to implement. May still change, at this point.
 * Additionally, this *might* be the interface that we rely on for our lesser/subordinate storage ranks,
 * which would make the system... more... self-consistent?
 *
 * Created by robert on 2015-10-30 14:04.
 */
public
interface HashAddressedBlockDevice extends AbstractBlockDevice
{
	/**
	 * @return the number of bytes long that the returned hash-addresses will be
	 */
	int getHashSize();

	/**
	 * @return information regarding number of blocks stored locally (this device), not counting demotion ranks
	 */
	HashDeviceStats getLocalStatistics();

	/**
	 * @return information regarding number of blocks stored locally (this device), INCLUDING all lower ranks
	 */
	HashDeviceStats getRecursiveStatistics();

	/**
	 * @param sha1HashAddress - the previously-returned (or reconstructed) hash code returned by writeBlock()
	 * @param buffer - a place to store the data, must be blockSize long
	 * @throws IOException - if the data could not be read, in which case the output buffer might still contain useful data
	 * @sideeffect access times are updated, and the given block is less likely to be pushed into lower ranks.
	 */
	void readBlock(Sha1HashAddress sha1HashAddress, byte[] buffer) throws IOException;

	/**
	 * @param buffer - the incoming data you would like to store, must be blockSize long
	 * @return the 'place' that the data is stored, which is (by definition) a hash of it's contents, never null.
	 * @throws IOException if the data could not be written
	 * @sideeffect reference counter to the block is incremented, making the data 'less likely' to go away.
	 */
	Sha1HashAddress writeBlock(byte[] buffer) throws IOException, HashCollision;

	/**
	 * @param sha1HashAddress - the block on which we wish to get information
	 * @return meta information concerning the block (when inserted, accessed, etc), or null indicating the block could not be found
	 * @throws IOException - because i/o is never 100% reliable
	 */
	HashBlockStats statBlock(Sha1HashAddress sha1HashAddress) throws IOException;

	/**
	 * Indicates to the subsystem that the given reference is now substantially less likely to be referenced, and that
	 * it might be a good candidate for demotion into a slower (sub-layer) storage medium.
	 *
	 * @param sha1HashAddress the address of the block that is unlikely to be referenced again
	 */
	void demoteBlock(Sha1HashAddress sha1HashAddress) throws IOException;

	/**
	 * Indicates to the subsystem that the reference to the given block will be discarded immediately after the call.
	 *
	 * @param sha1HashAddress the address of the block that is going away
	 * @throws IOException could not correctly unlink the block
	 * @sideeffect the reference counter is decremented, if it reaches zero it becomes a candidate for garbage collection
	 * @throws AssertionError if reference counter tries to move below zero
	 */
	void unlinkBlock(Sha1HashAddress sha1HashAddress) throws IOException;

	/**
	 * Causes the current thread to stall until such a time that all I/O operations that pending as of the start
	 * of this function have been permanently stored (and are recoverable from) the underlying storage devices
	 * (e.g. in the event of a power failure).
	 *
	 * @throws IOException
	 */
	void sync() throws IOException;
}
