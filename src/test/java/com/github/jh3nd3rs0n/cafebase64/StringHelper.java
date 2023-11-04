package com.github.jh3nd3rs0n.cafebase64;

public final class StringHelper {
	
	public static String wrap(final String string, final int columnLimit) {
		int column = 0;
		String lineSeparator = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		char[] chars = string.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			sb.append(c);
			if (columnLimit > 0) {
				if (i == chars.length - 1 && column + 1 < columnLimit) {
					sb.append(lineSeparator);
				}
				if (++column == columnLimit) {
					sb.append(lineSeparator);
					column = 0;
				}
			}
		}
		return sb.toString();
	}

	private StringHelper() { }
	
}
