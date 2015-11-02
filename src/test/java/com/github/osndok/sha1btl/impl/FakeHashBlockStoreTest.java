package com.github.osndok.sha1btl.impl;

import com.github.osndok.sha1btl.Example;
import com.github.osndok.sha1btl.HashAddressedBlockDevice;
import com.github.osndok.sha1btl.Sha1HashAddress;
import org.apache.commons.codec.binary.Hex;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by robert on 2015-11-01 17:57.
 */
public
class FakeHashBlockStoreTest
{
	private final
	byte[] hello = "hello".getBytes();

	private
	byte[] hashed = Example.SHA1_HASH_CODE;

	@Test
	public
	void testBasicOperation() throws Exception
	{
		final
		Sha1HashAddress key;

		final
		HashAddressedBlockDevice storage=new FakeHashBlockStore(hello.length);
		{
			key=storage.writeBlock(hello);
		}

		final
		byte[] buffer=new byte[hello.length];
		{
			storage.readBlock(key, buffer);
		}

		assertEquals(buffer, hello);
	}
}