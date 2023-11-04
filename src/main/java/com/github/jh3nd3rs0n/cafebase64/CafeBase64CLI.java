package com.github.jh3nd3rs0n.cafebase64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.argmatey.ArgMatey;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.Annotations.Option;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.Annotations.OptionArgSpec;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.Annotations.OptionGroupHelpTextProvider;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.Annotations.Ordinal;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.CLI;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.OptionGroupHelpTextParams;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.OptionType;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.StringConverter;
import com.github.jh3nd3rs0n.argmatey.ArgMatey.TerminationRequestedException;

public final class CafeBase64CLI extends CLI {
	
	private static final class InterpolatedOptionGroupHelpTextProvider 
		extends ArgMatey.OptionGroupHelpTextProvider {

		@Override
		public String getOptionGroupHelpText(
				final OptionGroupHelpTextParams params) {
			ArgMatey.OptionGroupHelpTextProvider provider = 
					ArgMatey.OptionGroupHelpTextProvider.getDefault();
			String helpText = provider.getOptionGroupHelpText(params);
			return helpText.replace(
					"${line.separator}", System.getProperty("line.separator"));
		}
				
	}
	
	private static final class NonnegativeIntegerStringConverter 
		extends StringConverter {
		
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
	
	private static final int DECODE_OPTION_GROUP_ORDINAL = 0;
	private static final int IGNORE_GARBAGE_OPTION_GROUP_ORDINAL = 1;
	private static final int WRAP_OPTION_GROUP_ORDINAL = 2;
	private static final int HELP_OPTION_GROUP_ORDINAL = 3;
	private static final int VERSION_OPTION_GROUP_ORDINAL = 4;
	
	public static void main(final String[] args) {
		CLI cli = new CafeBase64CLI(args, false);
		try {
			cli.handleArgs();
		} catch (TerminationRequestedException e) {
			System.exit(e.getExitStatusCode());
		}
	}
	
	private int columnLimit;
	private boolean decodingMode;
	private String file;
	private boolean garbageIgnored;

	public CafeBase64CLI(final String[] args, final boolean posixlyCorrect) {
		super(args, posixlyCorrect);
		this.setProgramName("cafebase64");
		this.setProgramVersion("1.0");
	}
		
	@Override
	protected void afterHandleArgs() throws TerminationRequestedException {
		this.transform();
	}
	
	@Override
	protected void beforeHandleArgs() {
		this.columnLimit = 76;
		this.decodingMode = false;
		this.file = null;
		this.garbageIgnored = false;
	}
	
	@Option(
			doc = "display this help and exit",
			name = "help", 
			type = OptionType.GNU_LONG
	)
	@Ordinal(HELP_OPTION_GROUP_ORDINAL)
	@Override
	protected void displayProgramHelp()	throws TerminationRequestedException {
		System.out.printf("Usage: %s [OPTION]... [FILE]%n", 
				this.getProgramName());
		System.out.printf("Base64 encode or decode FILE, or standard "
				+ "input, to standard output.%n%n");
		System.out.printf("With no FILE, or when FILE is -, read standard "
				+ "input.%n%n");
		System.out.println("OPTIONS:");
		this.getOptionGroups().printHelpText();
		System.out.printf("%n");
		throw new TerminationRequestedException(0);
	}
	
	@Option(
			doc = "display version information and exit",
			name = "version",
			type = OptionType.GNU_LONG 
	)
	@Ordinal(VERSION_OPTION_GROUP_ORDINAL)
	@Override
	protected void displayProgramVersion()
			throws TerminationRequestedException {
		System.out.printf(
				"%s %s%n", this.getProgramName(), this.getProgramVersion());
		throw new TerminationRequestedException(0);
	}
	
	@Override
	protected void handleNonparsedArg(final String nonparsedArg) {
		if (this.file != null) {
			throw new IllegalArgumentException(String.format(
					"extra operand '%s'", nonparsedArg));
		}
		this.file = nonparsedArg;
	}
	
	@Override
	protected void handleThrowable(final Throwable t) 
			throws TerminationRequestedException {
		ArgMatey.Option helpOption = this.getOptionGroups().get(
				HELP_OPTION_GROUP_ORDINAL).get(0);
		String suggestion = String.format(
				"Try '%s %s' for more information.", 
				this.getProgramName(), 
				helpOption.getUsage());
		System.err.printf("%s: %s%n", this.getProgramName(), t);
		System.err.println(suggestion);
		t.printStackTrace(System.err);
		throw new TerminationRequestedException(-1);
	}
	
	@Option(
			doc = "wrap encoded lines after COLS character (default 76)."
					+ "${line.separator}      Use 0 to disable line wrapping",
			name = "w",
			optionArgSpec = @OptionArgSpec(
					name = "COLS",
					stringConverter = NonnegativeIntegerStringConverter.class
			),
			type = OptionType.POSIX 
	)
	@Option(
			name = "wrap",
			type = OptionType.GNU_LONG
	)
	@OptionGroupHelpTextProvider(InterpolatedOptionGroupHelpTextProvider.class)
	@Ordinal(WRAP_OPTION_GROUP_ORDINAL)
	private void setColumnLimit(final int colLimit) {
		this.columnLimit = colLimit;
	}
	
	@Option(
			doc = "decode data",
			name = "d",
			type = OptionType.POSIX 
	)
	@Option(
			name = "decode",
			type = OptionType.GNU_LONG
	)
	@Ordinal(DECODE_OPTION_GROUP_ORDINAL)
	private void setDecodingMode(final boolean b) {
		this.decodingMode = b;
	}
	
	@Option(
			doc = "when decoding, ignore non-alphabet characters",
			name = "i",
			type = OptionType.POSIX 
	)
	@Option(
			name = "ignore-garbage",
			type = OptionType.GNU_LONG
	)
	@Ordinal(IGNORE_GARBAGE_OPTION_GROUP_ORDINAL)
	private void setGarbageIgnored(final boolean b) {
		this.garbageIgnored = b;
	}
	
	private void transform() throws TerminationRequestedException {
		InputStream in = null;
		if (this.file != null) {
			if (this.file.equals("-")) {
				in = System.in;
			} else {
				File f = new File(this.file);
				try {
					in = new FileInputStream(f);
				} catch (FileNotFoundException e) {
					System.err.printf("%s: %s%n", this.getProgramName(), e);
					e.printStackTrace(System.err);
					throw new TerminationRequestedException(-1);
				}
			}		
		}
		if (in == null) { in = System.in; } 
		CafeBase64 cafeBase64 = CafeBase64.INSTANCE;
		if (this.decodingMode) {
			try {
				cafeBase64.decode(
						in, System.out, this.garbageIgnored);
			} catch (IOException e) {
				System.err.printf("%n%s: %s%n", this.getProgramName(), e);
				e.printStackTrace(System.err);
				throw new TerminationRequestedException(-1);
			} finally {
				if (in instanceof FileInputStream) {
					try {
						in.close();
					} catch (IOException e) {
						System.err.printf("%s: %s%n", this.getProgramName(), e);
						e.printStackTrace(System.err);
					}
				}
			}
		} else {
			try {
				cafeBase64.encode(in, System.out, this.columnLimit);
			} catch (IOException e) {
				System.err.printf("%n%s: %s%n", this.getProgramName(), e);
				e.printStackTrace(System.err);
				throw new TerminationRequestedException(-1);
			} finally {
				if (in instanceof FileInputStream) {
					try {
						in.close();
					} catch (IOException e) {
						System.err.printf("%s: %s%n", this.getProgramName(), e);
						e.printStackTrace(System.err);
					}
				}
			}
		}
	}
	
}