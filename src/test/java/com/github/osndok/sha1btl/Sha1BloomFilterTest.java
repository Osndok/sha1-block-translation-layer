package com.github.osndok.sha1btl;

import org.apache.commons.codec.binary.Hex;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by robert on 2015-11-01 17:26.
 */
public
class Sha1BloomFilterTest
{
	@Test
	public
	void testBitPatterns() throws Exception
	{
		final
		byte[] bytes=new byte[]{
								   0,0,0,0, 0,0,0,0,
								   0,0,0,0, 0,0,0,1,
		};

		final
		Sha1BloomFilter bloomFilter=new Sha1BloomFilter(bytes);

		assertTrue(bloomFilter.testBit(0));

		for (int i=1; i<128; i++)
		{
			assertFalse(bloomFilter.testBit(i));
		}

		bytes[0]=((byte)0x80); //One very-high bit... (highest *byte* and highest *bit-in-that-byte*).
		assertTrue(bloomFilter.testBit(127));

		assertTrue(bytes==bloomFilter.getBytes());
	}

	@Test
	public
	void testTwoUsefulBitsSet() throws Exception
	{
		final
		Sha1HashAddress key=new Sha1HashAddress(Example.SHA1_HASH_CODE);

		final
		Sha1BloomFilter bloomFilter=new Sha1BloomFilter();
		{
			assertFalse(bloomFilter.indicatesTheProbablePresenceOf(key));
			bloomFilter.accumulate(key);
			assertTrue(bloomFilter.indicatesTheProbablePresenceOf(key));
		}

		int numSet=0;

		for (int i=0; i<128; i++)
		{
			if (bloomFilter.testBit(i))
			{
				numSet++;
			}
		}

		//NB: for some hash codes (that have the same last-two hex-tuples) the count would correctly be 1.
		assertEquals(numSet, 2);
		assertEquals(bloomFilter.getBytes().length, 16);
	}
}