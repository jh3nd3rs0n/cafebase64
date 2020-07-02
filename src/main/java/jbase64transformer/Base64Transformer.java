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

import argmatey.ArgMatey;
import argmatey.ArgMatey.Annotations.NonparsedArg;
import argmatey.ArgMatey.Annotations.Option;
import argmatey.ArgMatey.Annotations.OptionArgSpec;
import argmatey.ArgMatey.Annotations.OptionGroup;
import argmatey.ArgMatey.ArgsHandler;
import argmatey.ArgMatey.GnuLongOption;
import argmatey.ArgMatey.OptionGroups;
import argmatey.ArgMatey.PosixOption;
import argmatey.ArgMatey.StringConverter;

public enum Base64Transformer {
	
	INSTANCE;
	
	public static final class Cli {
		
		private static final int DECODE_OPTION_GROUP_ORDINAL = 0;
		private static final int IGNORE_GARBAGE_OPTION_GROUP_ORDINAL = 1;
		private static final int WRAP_OPTION_GROUP_ORDINAL = 2;
		private static final int HELP_OPTION_GROUP_ORDINAL = 3;
		private static final int VERSION_OPTION_GROUP_ORDINAL = 4;
		
		private ArgsHandler argsHandler;
		private int columnLimit;
		private boolean decodingMode;
		private String file;
		private boolean garbageIgnored;
		private OptionGroups optionGroups;
		private boolean programHelpDisplayed;		
		private final String programName;
		private final String programVersion;
		private boolean programVersionDisplayed;
		
		Cli() {
			this.argsHandler = null;
			this.columnLimit = 76;
			this.decodingMode = false;
			this.file = null;
			this.garbageIgnored = false;
			this.optionGroups = null;
			this.programHelpDisplayed = false;
			this.programName = Base64Transformer.class.getName();
			this.programVersion = "1.0";
			this.programVersionDisplayed = false;
		}
		
		 
		@OptionGroup(
				option = @Option(
						doc = "display this help and exit",
						name = "help", 
						special = true,
						type = GnuLongOption.class),
				ordinal = HELP_OPTION_GROUP_ORDINAL
		)
		public void displayHelp() {
			System.out.printf("Usage: %s [OPTION]... [FILE]%n", 
					this.programName);
			System.out.printf("Base64 encode or decode FILE, or standard "
					+ "input, to standard output.%n%n");
			System.out.println("OPTIONS:");
			this.optionGroups.printHelpText();
			System.out.printf("%n%nWith no FILE, or when FILE is -, read "
					+ "standard input.%n");
			this.programHelpDisplayed = true;
		}
				
		@OptionGroup(
				option = @Option(
						doc = "display version information and exit",
						name = "version",
						special = true,
						type = GnuLongOption.class 
				), 
				ordinal = VERSION_OPTION_GROUP_ORDINAL
		)
		public void displayVersion() {
			System.out.printf("%s %s%n", this.programName, this.programVersion);
			this.programVersionDisplayed = true;
		}
		
		public int process(final String[] args) {
			this.argsHandler = ArgsHandler.newInstance(args, this, false);
			this.optionGroups = this.argsHandler.getOptionGroups();
			ArgMatey.Option helpOption = this.optionGroups.toList().get(
					HELP_OPTION_GROUP_ORDINAL).toList().get(0);
			String suggestion = String.format(
					"Try '%s %s' for more information.", 
					this.programName, 
					helpOption.getUsage());
			while (this.argsHandler.hasNext()) {
				try {
					this.argsHandler.handleNext();
				} catch (Throwable t) {
					System.err.printf("%s: %s%n", this.programName, t);
					System.err.println(suggestion);
					t.printStackTrace(System.err);
					return -1;
				}
				if (this.programHelpDisplayed || this.programVersionDisplayed) {
					return 0;
				}
			}
			return this.transform();
		}
		
		@OptionGroup(
				option = @Option(
						doc = "wrap encoded lines after COLS character "
								+ "(default 76)." 
								+ "\r\n      Use 0 to disable line wrapping",
						name = "w",
						optionArgSpec = @OptionArgSpec(
								name = "COLS",
								stringConverter = NonnegativeIntegerStringConverter.class
						),
						type = PosixOption.class 
				), 
				ordinal = WRAP_OPTION_GROUP_ORDINAL,
				otherOptions = {
						@Option(
								name = "wrap",
								type = GnuLongOption.class
						)
				}
		)
		public void setColumnLimit(final int colLimit) {
			this.columnLimit = colLimit;
		}
		
		@OptionGroup(
				option = @Option(
						doc = "decode data",
						name = "d",
						type = PosixOption.class 
				), 
				ordinal = DECODE_OPTION_GROUP_ORDINAL,
				otherOptions = {
						@Option(
								name = "decode",
								type = GnuLongOption.class
						)
				}
		)
		public void setDecodingMode(final boolean b) {
			this.decodingMode = b;
		}
		
		@NonparsedArg
		public void setFile(final String f) {
			if (this.file != null) {
				throw new IllegalArgumentException(String.format(
						"extra operand '%s'", f));
			}
			this.file = f;
		}
		
		@OptionGroup(
				option = @Option(
						doc = "when decoding, ignore non-alphabet characters",
						name = "i",
						type = PosixOption.class 
				), 
				ordinal = IGNORE_GARBAGE_OPTION_GROUP_ORDINAL,
				otherOptions = {
						@Option(
								name = "ignore-garbage",
								type = GnuLongOption.class
						)
				}
		)
		public void setGarbageIgnored(final boolean b) {
			this.garbageIgnored = b;
		}
		
		private int transform() {
			InputStream in = null;
			if (this.file != null) {
				if (this.file.equals("-")) {
					in = System.in;
				} else {
					File f = new File(this.file);
					try {
						in = new FileInputStream(f);
					} catch (FileNotFoundException e) {
						System.err.printf("%s: %s%n", this.programName, e);
						e.printStackTrace(System.err);
						return -1;
					}
				}		
			}
			if (in == null) { in = System.in; } 
			Base64Transformer base64Transformer = Base64Transformer.INSTANCE;
			if (this.decodingMode) {
				try {
					base64Transformer.decode(
							in, System.out, this.garbageIgnored);
				} catch (IOException e) {
					System.err.printf("%n%s: %s%n", this.programName, e);
					e.printStackTrace(System.err);
					return -1;
				} finally {
					if (in instanceof FileInputStream) {
						try {
							in.close();
						} catch (IOException e) {
							System.err.printf("%s: %s%n", this.programName, e);
							e.printStackTrace(System.err);
						}
					}
				}
			} else {
				try {
					base64Transformer.encode(in, System.out, this.columnLimit);
				} catch (IOException e) {
					System.err.printf("%n%s: %s%n", this.programName, e);
					e.printStackTrace(System.err);
					return -1;
				} finally {
					if (in instanceof FileInputStream) {
						try {
							in.close();
						} catch (IOException e) {
							System.err.printf("%s: %s%n", this.programName, e);
							e.printStackTrace(System.err);
						}
					}
				}
			}
			return 0;
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
		Cli cli = new Cli();
		int status = cli.process(args);
		if (status != 0) { System.exit(status);	}
	}
	
	public void decode(
			final InputStream in, 
			final OutputStream out, 
			final boolean garbageIgnored) throws IOException {
		Reader reader = new InputStreamReader(in);
		StringBuilder sb = new StringBuilder();
		Base64.Decoder decoder = Base64.getDecoder();
		String base64AlphabetChars = 
				"ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "abcdefghijklmnopqrstuvwxyz"
				+ "0123456789"
				+ "+/=";
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
						&& !garbageIgnored) {
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
			final OutputStream out,
			final int columnLimit) throws IOException {
		if (columnLimit < 0) {
			throw new IllegalArgumentException(String.format(
					"integer must be between %s and %s (inclusive)", 
					0, Integer.MAX_VALUE));
		}
		Writer writer = new OutputStreamWriter(out);
		final int groupSize = 3;
		int column = 0;
		String lineSeparator = System.getProperty("line.separator");
		Base64.Encoder encoder = Base64.getEncoder();
		while (true) {
			byte[] b = new byte[groupSize];
			int newLength = in.read(b);
			if (newLength == -1) {
				if (columnLimit > 0 && column > 0 && column < columnLimit) {
					writer.write(lineSeparator);
				}
				break; 
			}
			b = Arrays.copyOf(b, newLength);
			String encoded = encoder.encodeToString(b);
			if (columnLimit > 0) {
				StringBuilder sb = new StringBuilder();
				for (char c : encoded.toCharArray()) {
					sb.append(c);
					if (++column == columnLimit) {
						sb.append(lineSeparator);
						column = 0;
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
