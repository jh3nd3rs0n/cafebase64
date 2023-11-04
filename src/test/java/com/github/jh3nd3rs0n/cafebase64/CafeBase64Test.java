package com.github.jh3nd3rs0n.cafebase64;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class CafeBase64Test {

	@Test
	public void testDecode01() throws IOException {
		String expectedString = TestStringConstants.ORIGINAL_STRING_01;
		String base64String = TestStringConstants.ENCODED_STRING_01;
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.decode(
				new ByteArrayInputStream(base64String.getBytes()), 
				decodedOut, 
				false);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);
	}
	
	@Test
	public void testDecode02() throws IOException {
		String expectedString = TestStringConstants.ORIGINAL_STRING_02;
		String base64String = TestStringConstants.ENCODED_STRING_02;
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.decode(
				new ByteArrayInputStream(base64String.getBytes()), 
				decodedOut, 
				false);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);
	}
	
	@Test
	public void testDecode03() throws IOException {
		String expectedString = TestStringConstants.ORIGINAL_STRING_03;
		String base64String = TestStringConstants.ENCODED_STRING_03;
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.decode(
				new ByteArrayInputStream(base64String.getBytes()), 
				decodedOut, 
				false);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);
	}

	@Test
	public void testDecodeWithGarbage01() throws IOException {
		String expectedString = TestStringConstants.ORIGINAL_STRING_01;
		String base64String = TestStringConstants.ENCODED_STRING_01_WITH_GARBAGE;
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.decode(
				new ByteArrayInputStream(base64String.getBytes()), 
				decodedOut, 
				true);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);
	}
	
	@Test
	public void testDecodeWithGarbage02() throws IOException {
		String expectedString = TestStringConstants.ORIGINAL_STRING_02;
		String base64String = TestStringConstants.ENCODED_STRING_02_WITH_GARBAGE;
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.decode(
				new ByteArrayInputStream(base64String.getBytes()), 
				decodedOut, 
				true);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);
	}
	
	@Test
	public void testDecodeWithGarbage03() throws IOException {
		String expectedString = TestStringConstants.ORIGINAL_STRING_03;
		String base64String = TestStringConstants.ENCODED_STRING_03_WITH_GARBAGE;
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.decode(
				new ByteArrayInputStream(base64String.getBytes()), 
				decodedOut, 
				true);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);
	}
	
	@Test(expected = IOException.class)
	public void testDecodeWithGarbageThrowingIOException01() throws IOException {
		String base64String = TestStringConstants.ENCODED_STRING_01_WITH_GARBAGE;
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.decode(
				new ByteArrayInputStream(base64String.getBytes()), 
				decodedOut, 
				false);
	}
	
	@Test(expected = IOException.class)
	public void testDecodeWithGarbageThrowingIOException02() throws IOException {
		String base64String = TestStringConstants.ENCODED_STRING_02_WITH_GARBAGE;
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.decode(
				new ByteArrayInputStream(base64String.getBytes()), 
				decodedOut, 
				false);
	}
	
	@Test(expected = IOException.class)
	public void testDecodeWithGarbageThrowingIOException03() throws IOException {
		String base64String = TestStringConstants.ENCODED_STRING_03_WITH_GARBAGE;
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.decode(
				new ByteArrayInputStream(base64String.getBytes()), 
				decodedOut, 
				false);
	}
	
	@Test
	public void testEncode01() throws IOException {
		String expectedString = TestStringConstants.ENCODED_STRING_01;
		String originalString = TestStringConstants.ORIGINAL_STRING_01;
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.encode(new ByteArrayInputStream(
				originalString.getBytes()), encodedOut, 0);
		String encodedString = new String(encodedOut.toByteArray());
		assertEquals(expectedString, encodedString);
	}
	
	@Test
	public void testEncode02() throws IOException {
		String expectedString = TestStringConstants.ENCODED_STRING_02;
		String originalString = TestStringConstants.ORIGINAL_STRING_02;
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.encode(new ByteArrayInputStream(
				originalString.getBytes()), encodedOut, 0);
		String encodedString = new String(encodedOut.toByteArray());
		assertEquals(expectedString, encodedString);
	}
	
	@Test
	public void testEncode03() throws IOException {
		String expectedString = TestStringConstants.ENCODED_STRING_03;
		String originalString = TestStringConstants.ORIGINAL_STRING_03;
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.encode(new ByteArrayInputStream(
				originalString.getBytes()), encodedOut, 0);
		String encodedString = new String(encodedOut.toByteArray());
		assertEquals(expectedString, encodedString);
	}
	
	@Test
	public void testEncodeWithWrapping01() throws IOException {
		int columnLimit = 5;
		String expectedString = StringHelper.wrap(
				TestStringConstants.ENCODED_STRING_01, columnLimit);
		String originalString = TestStringConstants.ORIGINAL_STRING_01;
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.encode(new ByteArrayInputStream(
				originalString.getBytes()), encodedOut, columnLimit);
		String encodedString = new String(encodedOut.toByteArray());
		assertEquals(expectedString, encodedString);
	}
	
	@Test
	public void testEncodeWithWrapping02() throws IOException {
		int columnLimit = 10;
		String expectedString = StringHelper.wrap(
				TestStringConstants.ENCODED_STRING_02, columnLimit);
		String originalString = TestStringConstants.ORIGINAL_STRING_02;
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.encode(new ByteArrayInputStream(
				originalString.getBytes()), encodedOut, columnLimit);
		String encodedString = new String(encodedOut.toByteArray());
		assertEquals(expectedString, encodedString);
	}
	
	@Test
	public void testEncodeWithWrapping03() throws IOException {
		int columnLimit = 20;
		String expectedString = StringHelper.wrap(
				TestStringConstants.ENCODED_STRING_03, columnLimit);
		String originalString = TestStringConstants.ORIGINAL_STRING_03;
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.encode(new ByteArrayInputStream(
				originalString.getBytes()), encodedOut, columnLimit);
		String encodedString = new String(encodedOut.toByteArray());
		assertEquals(expectedString, encodedString);
	}
	
	@Test
	public void testWithRoundtripping01() throws IOException {
		String originalString = TestStringConstants.ORIGINAL_STRING_01;
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.encode(
				new ByteArrayInputStream(originalString.getBytes()), 
				encodedOut, 
				0);
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.decode(
				new ByteArrayInputStream(encodedOut.toByteArray()), 
				decodedOut, 
				false);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(originalString, decodedString);
	}
	
	@Test
	public void testWithRoundtripping02() throws IOException {
		String originalString = TestStringConstants.ORIGINAL_STRING_02;
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.encode(
				new ByteArrayInputStream(originalString.getBytes()), 
				encodedOut, 
				0);
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.decode(
				new ByteArrayInputStream(encodedOut.toByteArray()), 
				decodedOut, 
				false);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(originalString, decodedString);
	}
		
	@Test
	public void testWithRoundtripping03() throws IOException {
		String originalString = TestStringConstants.ORIGINAL_STRING_03;
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.encode(
				new ByteArrayInputStream(originalString.getBytes()), 
				encodedOut, 
				0);
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		CafeBase64.INSTANCE.decode(
				new ByteArrayInputStream(encodedOut.toByteArray()), 
				decodedOut, 
				false);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(originalString, decodedString);
	}
	
}
