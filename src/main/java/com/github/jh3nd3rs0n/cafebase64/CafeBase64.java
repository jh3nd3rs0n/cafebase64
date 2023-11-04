package com.github.jh3nd3rs0n.cafebase64;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Base64;

public enum CafeBase64 {
	
	INSTANCE;
	
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
		return CafeBase64.class.getSimpleName();
	}
	
}
