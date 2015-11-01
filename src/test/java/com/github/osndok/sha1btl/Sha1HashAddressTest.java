package com.github.osndok.sha1btl;

import org.apache.commons.codec.binary.Hex;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by robert on 2015-11-01 16:14.
 */
public
class Sha1HashAddressTest
{
	private
	byte[] ZEROS=new byte[20];

	private
	byte[] ONES;

	@BeforeMethod
	public
	void setUp() throws Exception
	{
		final
		byte FULL_BYTE=(byte)0xFF;

		ONES=new byte[20];

		for (int i=0; i<20; i++)
		{
			ONES[i]=FULL_BYTE;
		}
	}

	@Test
	public
	void testGetBytes() throws Exception
	{
		assertEquals(new Sha1HashAddress(ZEROS).getBytes(), ZEROS);
	}

	@Test
	public
	void testHashCode() throws Exception
	{
		assertEquals(new Sha1HashAddress(ZEROS).hashCode(), 0);
		assertEquals(new Sha1HashAddress(ONES).hashCode(), 0xFFFFFFFF);

		byte[] bytes;
		Sha1HashAddress s;
		{
			bytes=new byte[]{
								1,0,0,16,
								2,0,0,8,
								4,0,0,4,
								8,0,0,2,
								16,0,0,1
			};

			assertEquals(new Sha1HashAddress(bytes).hashCode(), 0x1F00001F);
		}
	}

	@Test
	public
	void testIntegerFrom() throws Exception
	{
		assertEquals(i(0x00, 0x00, 0x00, 0x00), 0x00000000);
		assertEquals(i(0xFF, 0xFF, 0xFF, 0xFF), 0xFFFFFFFF);
		assertEquals(i(0x12, 0x34, 0x56, 0x78), 0x12345678);
	}

	private
	int i(int i, int i1, int i2, int i3)
	{
		return Sha1HashAddress.integerFrom((byte)i, (byte)i1, (byte)i2, (byte)i3);
	}
}