package jbase64transformer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Base64;

import argmatey.ArgsParser;
import argmatey.GnuLongOption;
import argmatey.Option;
import argmatey.OptionArgSpec;
import argmatey.Options;
import argmatey.ParseResult;
import argmatey.PosixOption;
import argmatey.StringConverter;

public final class Base64Transformer {
	
	private static void decode(
			final Reader reader, 
			final OutputStream out, 
			final boolean ignoreGarbage) throws IOException {
		String base64Alphabet = 
				"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		String whitespaceNewlines = "\r\n";
		final int groupSize = 4;
		StringBuilder sb = new StringBuilder();
		Base64.Decoder decoder = Base64.getDecoder();
		while (true) {
			int c = reader.read();
			if (c == -1) { break; }
			if (base64Alphabet.indexOf(c) == -1) {
				if (whitespaceNewlines.indexOf(c) == -1 && !ignoreGarbage) {
					throw new IOException("non-alphabet character found");
				}
				continue;
			}
			sb.append((char) c);
			if (sb.length() < groupSize) {
				continue;
			}
			out.write(decoder.decode(sb.toString()));
			sb.delete(0, groupSize);
		}
		out.flush();
	}
	
	private static void encode(
			final InputStream in,
			final Writer writer,
			final int numOfColumnsLimit) throws IOException {
		final int groupSize = 3;
		Base64.Encoder encoder = Base64.getEncoder();
		int numOfColumns = 0;
		String lineSeparator = System.getProperty("line.separator");
		while (true) {
			byte[] b = new byte[groupSize];
			int newLength = in.read(b);
			if (newLength == -1) {
				if (numOfColumns < numOfColumnsLimit) {
					writer.write(lineSeparator);
				}
				break; 
			}
			b = Arrays.copyOf(b, newLength);
			String encoded = encoder.encodeToString(b);
			if (numOfColumnsLimit > 0) {
				int encodedLength = encoded.length();
				numOfColumns += encodedLength;
				if (numOfColumns >= numOfColumnsLimit) {
					int diff = numOfColumns - numOfColumnsLimit;
					StringBuilder sb = new StringBuilder(encoded);
					sb.insert(encodedLength - diff, lineSeparator);
					encoded = sb.toString();
					numOfColumns = diff;
				}
			}
			writer.write(encoded);
		}
		writer.flush();
	}
	
	public static void main(final String[] args) {
		String lineSeparator = System.getProperty("line.separator");
		Option decodeOption = new PosixOption.Builder('d')
				.doc("decode data")
				.otherBuilders(new GnuLongOption.Builder("decode"))
				.build();
		Option ignoreGarbageOption = new PosixOption.Builder('i')
				.doc("when decoding, ignore non-alphabet characters")
				.otherBuilders(new GnuLongOption.Builder("ignore-garbage"))
				.build();
		Option wrapOption = new PosixOption.Builder('w')
				.doc("wrap encoded lines after COLS character (default 76)."
						+ lineSeparator 
						+ "      Use 0 to disable line wrapping")
				.optionArgSpec(new OptionArgSpec.Builder()
						.name("COLS")
						.stringConverter(new StringConverter() {

							@Override
							public Object convert(String string) {
								String message = String.format(
										"must be an integer between "
										+ "%s (inclusive) and %s (inclusive)", 
										0,
										Integer.MAX_VALUE);
								int intValue;
								try {
									intValue = Integer.parseInt(string);
								} catch (NumberFormatException e) {
									throw new IllegalArgumentException(
											message, e);
								}
								if (intValue < 0) {
									throw new IllegalArgumentException(message);
								}
								return Integer.valueOf(intValue);
							}
							
						})
						.type(Integer.class)
						.build())
				.otherBuilders(new GnuLongOption.Builder("wrap"))
				.build();
		Option helpOption = new GnuLongOption.Builder("help")
				.doc("display this help and exit")
				.special(true)
				.build();
		Option versionOption = new GnuLongOption.Builder("version")
				.doc("display version information and exit")
				.special(true)
				.build();
		Options options = new Options(
				decodeOption, 
				ignoreGarbageOption, 
				wrapOption, 
				helpOption, 
				versionOption);
		ArgsParser argsParser = ArgsParser.newInstance(args, options, false);
		String programName = Base64Transformer.class.getName();
		String programVersion = "1.0";
		String suggestion = String.format("Try '%s %s' for more information", 
				programName, helpOption);
		boolean decode = false;
		boolean ignoreGarbage = false;
		final int defaultNumOfColumnsLimit = 76;
		int numOfColumnsLimit = defaultNumOfColumnsLimit;
		InputStream in = null;
		while (argsParser.hasNext()) {
			ParseResult parseResult = null;
			try { 
				parseResult = argsParser.parseNext(); 
			} catch (RuntimeException e) {
				System.err.printf("%s: %s%n%s%n", 
						programName, e.toString(), suggestion);
				System.exit(-1);
			}
			if (parseResult.hasOptionFrom(helpOption)) {
				System.out.printf("Usage: %s [OPTION]... [FILE]%n", 
						programName);
				System.out.printf("Base64 encode or decode FILE, or standard "
						+ "input, to standard output.%n%n");
				System.out.println("OPTIONS:");
				options.printHelpText();
				System.out.printf("%n%nWith no FILE, or when FILE is -, read "
						+ "standard input.%n");
				return;
			}
			if (parseResult.hasOptionFrom(versionOption)) {
				System.out.printf("%s %s%n", programName, programVersion);
				return;
			}
			if (parseResult.hasOptionFrom(decodeOption)) {
				decode = true;
			}
			if (parseResult.hasOptionFrom(ignoreGarbageOption)) {
				ignoreGarbage = true;
			}
			if (parseResult.hasOptionFrom(wrapOption)) {
				numOfColumnsLimit = parseResult.getOptionArg().getTypeValue(
						Integer.class).intValue();
			}
			if (parseResult.hasNonparsedArg()) {
				String arg = parseResult.getNonparsedArg();
				if (in != null) {
					System.err.printf("%s: extra operand '%s'%n%s%n", 
							programName, arg, suggestion);
					System.exit(-1);
				}
				if (arg.equals("-")) {
					in = System.in;
				} else {
					File file = new File(arg);
					try {
						in = new FileInputStream(file);
					} catch (FileNotFoundException e) {
						System.err.printf("%s: %s%n", 
								programName, e.toString());
						System.exit(-1);
					}
				}
			}
		}
		if (in == null) { in = System.in; }
		if (decode) {
			Reader reader = new InputStreamReader(in);
			try {
				decode(reader, System.out, ignoreGarbage);
			} catch (IOException e) {
				System.err.printf("%s: %s%n", programName, e.toString());
				System.exit(-1);
			}
		} else {
			Writer writer = new OutputStreamWriter(System.out);
			try {
				encode(in, writer, numOfColumnsLimit);
			} catch (IOException e) {
				System.err.printf("%s: %s%n", programName, e.toString());
				System.exit(-1);
			}
		}
	}
}
