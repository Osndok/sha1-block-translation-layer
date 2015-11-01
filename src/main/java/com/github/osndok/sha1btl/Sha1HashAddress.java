package com.github.osndok.sha1btl;

/**
 * Used primarily for type-safety (e.g. to avoid passing two nameless byte-arrays to our store method),
 * but also lets us consolidate a bit of address-specific logic. In the abstract sense, a hash code is
 * simply a series of bytes. In our case, we are only concerned with SHA-1 hashes at the moment.
 *
 * Created by robert on 2015-10-30 14:08.
 */
public
class Sha1HashAddress
{
	private final
	byte[] bytes;

	public
	Sha1HashAddress(byte[] bytes)
	{
		MustLookLike.aSha1HashCode(bytes);
		this.bytes = bytes;
	}

	public
	byte[] getBytes()
	{
		return bytes;
	}

	/**
	 * Used for the BloomFilter. Using the least-significate-byte because we are sorting by the
	 * most-significant byte.
	 */
	public
	byte getLSB()
	{
		return bytes[19];
	}

	/**
	 * Used for the BloomFilter. Using the least-significate-byte because we are sorting by the
	 * most-significant byte.
	 */
	public
	byte getNearlyLSB()
	{
		return bytes[18];
	}

	private transient
	int hashCode;

	/**
	 * It might seem like an oxymoron to get the hashcode of a hash code. However, Java natively uses
	 * this function for hashmaps (and the like), and can be useful as an easy 32-bit compression of
	 * the larger 160-bit hash (by folding it into fives).
	 *
	 * NB: the case where the hash code is all zeros is intentionally not optimized in favor of focusing
	 * on and optimizing for the common case. If anyone where to ever find a byte-string that hashes to
	 * all zeros, then this implementation would not perform 'optimally' for that one key.
	 *
	 * @return a 32-bit representation of this 160-bit hash code
	 */
	@Override
	public
	int hashCode()
	{
		final
		int hashCode=this.hashCode;

		if (hashCode==0)
		{
			final
			byte[] b=this.bytes;

			return this.hashCode =
			  integerFrom(b[ 0],b[ 1],b[ 2],b[ 3])
			^ integerFrom(b[ 4],b[ 5],b[ 6],b[ 7])
			^ integerFrom(b[ 8],b[ 9],b[10],b[11])
			^ integerFrom(b[12],b[13],b[14],b[15])
			^ integerFrom(b[16],b[17],b[18],b[19])
			;
		}

		return hashCode;
	}

	static
	int integerFrom(byte a, byte b, byte c, byte d)
	{
		return ((int)a)<<24 | ((int)b)<<16 | ((int)c)<<8 | d;
	}
}
