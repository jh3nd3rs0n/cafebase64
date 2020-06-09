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

import argmatey.ArgMatey.ArgsParser;
import argmatey.ArgMatey.GnuLongOption;
import argmatey.ArgMatey.NonparsedArgSink;
import argmatey.ArgMatey.Option;
import argmatey.ArgMatey.OptionArgSpecBuilder;
import argmatey.ArgMatey.OptionBuilder;
import argmatey.ArgMatey.OptionSink;
import argmatey.ArgMatey.Options;
import argmatey.ArgMatey.PosixOption;
import argmatey.ArgMatey.StringConverter;

public enum Base64Transformer {
	
	INSTANCE;
	
	public static final class Cli {
		
		@OptionSink(
				optionBuilder = @OptionBuilder(
						doc = "decode data",
						name = "d",
						type = PosixOption.class 
				), 
				ordinal = 0,
				otherOptionBuilders = {
						@OptionBuilder(
								name = "decode",
								type = GnuLongOption.class
						)
				}
		)
		public boolean decode = false;
		
		@OptionSink(
				optionBuilder = @OptionBuilder(
						doc = "when decoding, ignore non-alphabet characters",
						name = "i",
						type = PosixOption.class 
				), 
				ordinal = 1,
				otherOptionBuilders = {
						@OptionBuilder(
								name = "ignore-garbage",
								type = GnuLongOption.class
						)
				}
		)
		public boolean ignoreGarbage = false;
		
		@OptionSink(
				optionBuilder = @OptionBuilder(
						doc = "wrap encoded lines after COLS character "
								+ "(default 76)." 
								+ "\r\n      Use 0 to disable line wrapping",
						optionArgSpecBuilder = @OptionArgSpecBuilder(
								name = "COLS",
								stringConverter = NonnegativeIntegerStringConverter.class
						),
						name = "w",
						type = PosixOption.class 
				), 
				ordinal = 2,
				otherOptionBuilders = {
						@OptionBuilder(
								name = "wrap",
								type = GnuLongOption.class
						)
				}
		)
		public int wrap = 76;
				
		private String file;
		private final Options options;
		private final String programName;
		private final String programVersion;
		
		public Cli(
				final String progName, 
				final String progVersion, 
				final Options opts) {
			this.file = null;
			this.options = opts;
			this.programName = progName;
			this.programVersion = progVersion;
		}
		
		@OptionSink(
				optionBuilder = @OptionBuilder(
						doc = "display this help and exit",
						name = "help",
						special = true,
						type = GnuLongOption.class 
				), 
				ordinal = 3
		)
		public void displayHelp() {
			System.out.printf("Usage: %s [OPTION]... [FILE]%n", 
					this.programName);
			System.out.printf("Base64 encode or decode FILE, or standard "
					+ "input, to standard output.%n%n");
			System.out.println("OPTIONS:");
			this.options.printHelpText();
			System.out.printf("%n%nWith no FILE, or when FILE is -, read "
					+ "standard input.%n");
			System.exit(0);
		}
		
		@OptionSink(
				optionBuilder = @OptionBuilder(
						doc = "display version information and exit",
						name = "version",
						special = true,
						type = GnuLongOption.class 
				), 
				ordinal = 4
		)
		public void displayVersion() {
			System.out.printf("%s %s%n", this.programName, this.programVersion);
			System.exit(0);
		}
		
		public String getFile() {
			return this.file;
		}
		
		@NonparsedArgSink
		public void setFile(final String f) {
			if (this.file != null) {
				throw new IllegalStateException(
						String.format("extra operand '%s'", f));
			}
			this.file = f;
		}
		
	}
	
	public static final class NonnegativeIntegerStringConverter 
		extends StringConverter {

		public NonnegativeIntegerStringConverter() { }
		
		@Override
		public Object convert(final String string) {
			String message = String.format(
					"must be an integer between %s and %s (inclusive)", 
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
		
	}
	
	public static void main(final String[] args) {
		String programName = Base64Transformer.class.getName();
		String programVersion = "1.0";
		Options options = Options.newInstance(Cli.class);
		Cli cli = new Cli(programName, programVersion, options);
		ArgsParser argsParser = ArgsParser.newInstance(args, options, false);
		Option helpOption = options.toList().get(3);
		String suggestion = String.format("Try '%s %s' for more information.", 
				programName, helpOption.getUsage());
		try {
			argsParser.parseRemainingTo(cli);
		} catch (RuntimeException e) {
			System.err.printf("%s: %s%n%s%n", programName, e, suggestion);
			System.exit(-1);
		}
		InputStream in = null;
		String file = cli.getFile();
		if (file != null) {
			if (file.equals("-")) {
				in = System.in;
			} else {
				File f = new File(file);
				try {
					in = new FileInputStream(f);
				} catch (FileNotFoundException e) {
					System.err.printf("%s: %s%n", programName, e);
					System.exit(-1);
				}
			}		
		}
		if (in == null) { in = System.in; } 
		Base64Transformer base64Transformer = Base64Transformer.INSTANCE;
		if (cli.decode) {
			Reader reader = new InputStreamReader(in);
			try {
				base64Transformer.decode(reader, System.out, cli.ignoreGarbage);
			} catch (IOException e) {
				System.err.printf("%s: %s%n", programName, e);
				System.exit(-1);
			} finally {
				if (in instanceof FileInputStream) {
					try {
						in.close();
					} catch (IOException e) {
						System.err.printf("%s: %s%n", programName, e);
						System.exit(-1);
					}
				}
			}
		} else {
			Writer writer = new OutputStreamWriter(System.out);
			try {
				base64Transformer.encode(in, writer, cli.wrap);
			} catch (IOException e) {
				System.err.printf("%s: %s%n", programName, e);
				System.exit(-1);
			} finally {
				if (in instanceof FileInputStream) {
					try {
						in.close();
					} catch (IOException e) {
						System.err.printf("%s: %s%n", programName, e);
						System.exit(-1);
					}
				}
			}
		}
	}
	
	public void decode(
			final Reader reader, 
			final OutputStream out, 
			final boolean ignoreGarbage) throws IOException {
		StringBuilder sb = new StringBuilder();
		Base64.Decoder decoder = Base64.getDecoder();
		String base64AlphabetChars = 
				"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
		String acceptedWhitespaceChars = "\r\n";
		final int groupSize = 4;
		while (true) {
			int c = reader.read();
			if (c == -1) {
				if (sb.length() > 0) {
					out.write(decoder.decode(sb.toString()));
					sb.delete(0, sb.length());
				}
				break; 
			}
			if (base64AlphabetChars.indexOf(c) == -1) {
				if (acceptedWhitespaceChars.indexOf(c) == -1 
						&& !ignoreGarbage) {
					throw new IOException(String.format(
							"non-alphabet character found: '%s'", (char) c));
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
	
	public void encode(
			final InputStream in,
			final Writer writer,
			final int numOfColumnsLimit) throws IOException {
		if (numOfColumnsLimit < 0) {
			throw new IllegalArgumentException(String.format(
					"integer must be between %s and %s (inclusive)", 
					0, Integer.MAX_VALUE));
		}
		final int groupSize = 3;
		int numOfColumns = 0;
		String lineSeparator = System.getProperty("line.separator");
		Base64.Encoder encoder = Base64.getEncoder();
		while (true) {
			byte[] b = new byte[groupSize];
			int newLength = in.read(b);
			if (newLength == -1) {
				if (numOfColumnsLimit > 0 
						&& numOfColumns > 0 
						&& numOfColumns < numOfColumnsLimit) {
					writer.write(lineSeparator);
				}
				break; 
			}
			b = Arrays.copyOf(b, newLength);
			String encoded = encoder.encodeToString(b);
			if (numOfColumnsLimit > 0) {
				StringBuilder sb = new StringBuilder();
				for (char c : encoded.toCharArray()) {
					sb.append(c);
					if (++numOfColumns == numOfColumnsLimit) {
						sb.append(lineSeparator);
						numOfColumns = 0;
					}
				}
				encoded = sb.toString();
			}
			writer.write(encoded);
		}
		writer.flush();
	}
	
	@Override
	public String toString() {
		return Base64Transformer.class.getSimpleName();
	}
}
