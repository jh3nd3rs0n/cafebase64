package com.github.jh3nd3rs0n.cafebase64;

public final class TestStringConstants {

	public static final String ENCODED_STRING_01 = "SGVsbG8sIFdvcmxkCg==";
	public static final String ENCODED_STRING_01_WITH_GARBAGE = 
			"%SGVsbG8sIFdvcmxkCg==";
	public static final String ENCODED_STRING_02 = 
			"VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wZWQgb3ZlciB0aGUgbGF6eSBkb2cK";
	public static final String ENCODED_STRING_02_WITH_GARBAGE = 
			"VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wZWQg!b3ZlciB0aGUgbGF6eSBkb2cK";
	public static final String ENCODED_STRING_03 = "R29vZGJ5ZSwgV29ybGQK";
	public static final String ENCODED_STRING_03_WITH_GARBAGE = 
			"R29vZGJ5ZSwgV29ybGQK&";
	public static final String HELP_INFO_STRING;			
	public static final String ORIGINAL_STRING_01 = "Hello, World\n";
	public static final String ORIGINAL_STRING_02 = 
			"The quick brown fox jumped over the lazy dog\n";
	public static final String ORIGINAL_STRING_03 = "Goodbye, World\n";
	public static final String VERSION_INFO_STRING = String.format(
			"cafebase64 1.0%n");

	static {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Usage: cafebase64 [OPTION]... [FILE]%n"));
		sb.append(String.format("Base64 encode or decode FILE, or standard input, to standard output.%n"));
		sb.append(String.format("%n"));
		sb.append(String.format("With no FILE, or when FILE is -, read standard input.%n"));
		sb.append(String.format("%n"));
		sb.append(String.format("OPTIONS:%n"));
		sb.append(String.format("  -d, --decode%n"));
		sb.append(String.format("      decode data%n"));
		sb.append(String.format("  -i, --ignore-garbage%n"));
		sb.append(String.format("      when decoding, ignore non-alphabet characters%n"));
		sb.append(String.format("  -w COLS, --wrap=COLS%n"));
		sb.append(String.format("      wrap encoded lines after COLS character (default 76).%n"));
		sb.append(String.format("      Use 0 to disable line wrapping%n"));
		sb.append(String.format("  --help%n"));
		sb.append(String.format("      display this help and exit%n"));
		sb.append(String.format("  --version%n"));
		sb.append(String.format("      display version information and exit%n"));
		sb.append(String.format("%n"));
		HELP_INFO_STRING = sb.toString();
	}
	
	private TestStringConstants() { }
	
}
