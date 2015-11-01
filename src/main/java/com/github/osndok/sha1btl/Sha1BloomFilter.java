package com.github.osndok.sha1btl;

/**
 * This implements a not-quite-optimal bloom filter used to quickly determine (by comparing two longs)
 * if there is even a *chance* that the target we are looking for is present in the local dataset,
 * which would then require a binary or linear search.
 *
 * Created by robert on 2015-11-01 16:51.
 */
class Sha1BloomFilter
{
	/**
	 * This size was chosen because:
	 * (1) it is pretty small at the same size as two 64-bit longs,
	 * (2) it has an easy-to-address power-of-two space (2^7), and
	 * (3) it makes an "okay" bloom filter at 2 single-bit-hash-functions.
	 */
	private static final
	int NUM_BYTES = 16;

	/*
	NB: It is presumed that we will be *testing* more often than *accumulating* a bloom filter, so we
	keep it in the byte form... b/c the expected "common case" is:
	(1) load a byte array from a record,
	(2) test for the inclusion of one record,
	(3) move on [either to the next bloom filter, or to searching the list]
	 */
	final
	byte[] bytes;

	public
	Sha1BloomFilter(byte[] b)
	{
		assert (b.length == NUM_BYTES);
		this.bytes = b;
	}

	public
	Sha1BloomFilter()
	{
		bytes = new byte[NUM_BYTES];
	}

	private static final
	byte[] MASK = new byte[]{
								0x01,
								0x02,
								0x04,
								0x08,
								0x10,
								0x20,
								0x40,
								(byte) 0x80,
	};

	/**
	 * Returns true if and only if the specified bit position is true.
	 *
	 * @param i - the bit position [0-127], where 0 is the least-significant-bit (right-most)
	 * @return true, if that bit position has been flipped on, otherwise false.
	 */
	public
	boolean testBit(int i)
	{
		byte b=bytes[NUM_BYTES-1-i/8];
		return (b & MASK[i%8])!=0;
	}

	public
	void setBit(int i)
	{
		int byteNumber=NUM_BYTES-1-i/8;

		bytes[byteNumber]=(byte)(bytes[byteNumber] | MASK[i%8]);
	}

	public
	boolean indicatesTheProbablePresenceOf(Sha1HashAddress sha1HashAddress)
	{
		return testBit(bit1(sha1HashAddress))
			&& testBit(bit2(sha1HashAddress))
			;
	}

	public
	void accumulate(Sha1HashAddress sha1HashAddress)
	{
		setBit(bit1(sha1HashAddress));
		setBit(bit2(sha1HashAddress));
	}

	private
	int bit1(Sha1HashAddress sha1HashAddress)
	{
		return sha1HashAddress.getLSB() & 0x7F;
	}

	private
	int bit2(Sha1HashAddress sha1HashAddress)
	{
		return sha1HashAddress.getNearlyLSB() & 0x7F;
	}

	public
	byte[] getBytes()
	{
		return bytes;
	}
}
