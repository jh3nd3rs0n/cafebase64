package com.github.jh3nd3rs0n.cafebase64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.Test;

import com.github.jh3nd3rs0n.argmatey.ArgMatey.CLI;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.TerminationRequestedException;

public class CafeBase64CLITest {

	private static int handle(
			final String[] args, 
			final PrintStream err, 
			final InputStream in,
			final PrintStream out) throws IOException {
		PrintStream formerErr = System.err;
		InputStream formerIn = System.in;
		PrintStream formerOut = System.out;
		if (err != null) { System.setErr(err); }
		if (out != null) { System.setOut(out); }
		if (in != null) { System.setIn(in); }
		CLI cli = new CafeBase64CLI(args, false);
		int status = 0;
		try {
			cli.handleArgs();
		} catch (TerminationRequestedException e) {
			status = e.getExitStatusCode();
		} finally {
			if (err != null) { System.setErr(formerErr); }
			if (out != null) { System.setOut(formerOut); }
			if (in != null) { System.setIn(formerIn); }
		}
		return status;
	}
	
	@Test
	public void test01() throws IOException {
		String lineSeparator = System.getProperty("line.separator");
		String expectedString = TestStringConstants.ENCODED_STRING_01 + lineSeparator;
		String originalString = TestStringConstants.ORIGINAL_STRING_01;
		InputStream in = new ByteArrayInputStream(originalString.getBytes());
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(encodedOut);
		handle(new String[] { }, null, in, out);
		String encodedString = new String(encodedOut.toByteArray());
		assertEquals(expectedString, encodedString);
	}
	
	@Test
	public void test02() throws IOException {
		String lineSeparator = System.getProperty("line.separator");
		String expectedString = TestStringConstants.ENCODED_STRING_02 + lineSeparator;
		String originalString = TestStringConstants.ORIGINAL_STRING_02;
		InputStream in = new ByteArrayInputStream(originalString.getBytes());
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(encodedOut);
		handle(new String[] { }, null, in, out);
		String encodedString = new String(encodedOut.toByteArray());
		assertEquals(expectedString, encodedString);
	}
	
	@Test
	public void test03() throws IOException {
		String lineSeparator = System.getProperty("line.separator");
		String expectedString = TestStringConstants.ENCODED_STRING_03 + lineSeparator;
		String originalString = TestStringConstants.ORIGINAL_STRING_03;
		InputStream in = new ByteArrayInputStream(originalString.getBytes());
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(encodedOut);
		handle(new String[] { }, null, in, out);
		String encodedString = new String(encodedOut.toByteArray());
		assertEquals(expectedString, encodedString);
	}
	
	@Test
	public void testWithBogusOption() throws IOException {
		PrintStream err = new PrintStream(new ByteArrayOutputStream());
		InputStream in = new ByteArrayInputStream(new byte[] { });
		PrintStream out = new PrintStream(new ByteArrayOutputStream());
		int status = handle(new String[] { "--bogus" }, err, in, out);
		assertTrue(status != 0);
	}
	
	@Test
	public void testWithDecodeOption01() throws IOException {
		String expectedString = TestStringConstants.ORIGINAL_STRING_01;
		String base64String = TestStringConstants.ENCODED_STRING_01;
		InputStream in = new ByteArrayInputStream(base64String.getBytes());
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(decodedOut);
		handle(new String[] { "-d" }, null, in, out);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);
	}
	
	@Test
	public void testWithDecodeOption02() throws IOException {
		String expectedString = TestStringConstants.ORIGINAL_STRING_02;
		String base64String = TestStringConstants.ENCODED_STRING_02;
		InputStream in = new ByteArrayInputStream(base64String.getBytes());
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(decodedOut);
		handle(new String[] { "-d" }, null, in, out);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);
	}
	
	@Test
	public void testWithDecodeOption03() throws IOException {
		String expectedString = TestStringConstants.ORIGINAL_STRING_03;
		String base64String = TestStringConstants.ENCODED_STRING_03;
		InputStream in = new ByteArrayInputStream(base64String.getBytes());
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(decodedOut);
		handle(new String[] { "--decode" }, null, in, out);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);
	}
	
	@Test()
	public void testWithDecodeOptionAndIgnoreGarbageOption01() throws IOException {
		String expectedString = TestStringConstants.ORIGINAL_STRING_01;
		String base64StringWithGarbage = 
				TestStringConstants.ENCODED_STRING_01_WITH_GARBAGE;
		InputStream in = new ByteArrayInputStream(
				base64StringWithGarbage.getBytes());
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(decodedOut);
		handle(new String[] { "-d", "-i" }, null, in, out);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);
	}
	
	@Test()
	public void testWithDecodeOptionAndIgnoreGarbageOption02() throws IOException {
		String expectedString = TestStringConstants.ORIGINAL_STRING_02;
		String base64StringWithGarbage = 
				TestStringConstants.ENCODED_STRING_02_WITH_GARBAGE;
		InputStream in = new ByteArrayInputStream(
				base64StringWithGarbage.getBytes());
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(decodedOut);
		handle(new String[] { "-di" }, null, in, out);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);
	}
	
	@Test()
	public void testWithDecodeOptionAndIgnoreGarbageOption03() throws IOException {
		String expectedString = TestStringConstants.ORIGINAL_STRING_03;
		String base64StringWithGarbage = 
				TestStringConstants.ENCODED_STRING_03_WITH_GARBAGE;
		InputStream in = new ByteArrayInputStream(
				base64StringWithGarbage.getBytes());
		ByteArrayOutputStream decodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(decodedOut);
		handle(new String[] { "--decode", "--ignore-garbage" }, null, in, out);
		String decodedString = new String(decodedOut.toByteArray());
		assertEquals(expectedString, decodedString);		
	}
	
	@Test
	public void testWithDecodeOptionWithGarbage01() throws IOException {
		String base64StringWithGarbage = 
				TestStringConstants.ENCODED_STRING_01_WITH_GARBAGE;
		PrintStream err = new PrintStream(new ByteArrayOutputStream());
		InputStream in = new ByteArrayInputStream(
				base64StringWithGarbage.getBytes());
		PrintStream out = new PrintStream(new ByteArrayOutputStream());
		int status = handle(new String[] { "-d" }, err, in, out);
		assertTrue(status != 0);
	}
	
	@Test
	public void testWithDecodeOptionWithGarbage02() throws IOException {
		String base64StringWithGarbage = 
				TestStringConstants.ENCODED_STRING_02_WITH_GARBAGE;
		PrintStream err = new PrintStream(new ByteArrayOutputStream());
		InputStream in = new ByteArrayInputStream(
				base64StringWithGarbage.getBytes());
		PrintStream out = new PrintStream(new ByteArrayOutputStream());
		int status = handle(new String[] { "-d" }, err, in, out);
		assertTrue(status != 0);
	}
	
	@Test
	public void testWithDecodeOptionWithGarbage03() throws IOException {
		String base64StringWithGarbage = 
				TestStringConstants.ENCODED_STRING_03_WITH_GARBAGE;
		PrintStream err = new PrintStream(new ByteArrayOutputStream());
		InputStream in = new ByteArrayInputStream(
				base64StringWithGarbage.getBytes());
		PrintStream out = new PrintStream(new ByteArrayOutputStream());
		int status = handle(new String[] { "--decode" }, err, in, out);
		assertTrue(status != 0);		
	}
	
	@Test
	public void testWithHelpOption() throws IOException {
		String expectedString = TestStringConstants.HELP_INFO_STRING;
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		handle(new String[] { "--help" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void testWithHelpOptionFirst01() throws IOException {
		String expectedString = TestStringConstants.HELP_INFO_STRING;
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		handle(new String[] { "--help", "--bogus" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void testWithHelpOptionFirst02() throws IOException {
		String expectedString = TestStringConstants.HELP_INFO_STRING;
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		handle(new String[] { "--help", "-diw50" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void testWithHelpOptionFirst03() throws IOException {
		String expectedString = TestStringConstants.HELP_INFO_STRING;
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		handle(new String[] { "--help", "--version" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void testWithVersionOption() throws IOException {
		String expectedString = TestStringConstants.VERSION_INFO_STRING;
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		handle(new String[] { "--version" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void testWithVersionOptionFirst01() throws IOException {
		String expectedString = TestStringConstants.VERSION_INFO_STRING;
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		handle(new String[] { "--version", "--bogus" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void testWithVersionOptionFirst02() throws IOException {
		String expectedString = TestStringConstants.VERSION_INFO_STRING;
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		handle(new String[] { "--version", "-diw50" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void testWithVersionOptionFirst03() throws IOException {
		String expectedString = TestStringConstants.VERSION_INFO_STRING;
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		handle(new String[] { "--version", "--help" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void testWithWrapOption01() throws IOException {
		int columnLimit = 5;
		String expectedString = StringHelper.wrap(
				TestStringConstants.ENCODED_STRING_01, columnLimit);
		String originalString = TestStringConstants.ORIGINAL_STRING_01;
		InputStream in = new ByteArrayInputStream(originalString.getBytes());
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(encodedOut);
		handle(new String[] { "-w", Integer.toString(columnLimit) }, null, in, out);
		String encodedString = new String(encodedOut.toByteArray());
		assertEquals(expectedString, encodedString);
	}
	
	@Test
	public void testWithWrapOption02() throws IOException {
		int columnLimit = 10;
		String expectedString = StringHelper.wrap(
				TestStringConstants.ENCODED_STRING_02, columnLimit);
		String originalString = TestStringConstants.ORIGINAL_STRING_02;
		InputStream in = new ByteArrayInputStream(originalString.getBytes());
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(encodedOut);
		handle(new String[] { String.format("-w%s", columnLimit) }, null, in, out);
		String encodedString = new String(encodedOut.toByteArray());
		assertEquals(expectedString, encodedString);
	}
	
	@Test
	public void testWithWrapOption03() throws IOException {
		int columnLimit = 20;
		String expectedString = StringHelper.wrap(
				TestStringConstants.ENCODED_STRING_03, columnLimit);
		String originalString = TestStringConstants.ORIGINAL_STRING_03;
		InputStream in = new ByteArrayInputStream(originalString.getBytes());
		ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(encodedOut);
		handle(new String[] { String.format("--wrap=%s", columnLimit) }, null, in, out);
		String encodedString = new String(encodedOut.toByteArray());
		assertEquals(expectedString, encodedString);
	}

}
