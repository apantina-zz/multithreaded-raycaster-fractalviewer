package hr.fer.zemris.java.fractals;

import hr.fer.zemris.math.Complex;

/**
 * Parses a string into a complex number. String must be of form
 * <code>a + ib</code>, where <code>a</code> and <code>b</code> are real
 * numbers.
 * 
 * @author 0036502252
 *
 */
public class ComplexParser {
	/**
	 * The line to be parsed, deconstructed into an array of characters.
	 */
	private char[] data;
	/**
	 * The current index in the data array.
	 */
	private int currentIndex;
	/**
	 * The end of the line.
	 */
	private int end;
	/**
	 * The real part of the complex number to be parsed.
	 */
	private Double real;
	/**
	 * The imaginary part of the complex number to be parsed.
	 */
	private Double imaginary;
	/**
	 * Utility variable used for tracking operators.
	 */
	private char currentOperator;

	/**
	 * Constructs a new {@link ComplexParser} which will parse the given line.
	 * 
	 * @param line
	 *            the string to parse into a complex number
	 */
	public ComplexParser(String line) {
		if (line.isEmpty()) {
			throw new ParserException("Empty strings are illegal!");
		}

		this.data = line.toCharArray();
		this.currentIndex = 0;
		this.end = data.length;
		this.real = null;
		this.imaginary = null;
		this.currentOperator = '+';
	}

	/**
	 * Parses a string into a {@link Complex} instance.
	 * 
	 * @return the parsed {@link Complex}.
	 */
	public Complex parse() {
		parsingMethod();
		if (real == null && imaginary == null) {
			throw new ParserException("Invalid sequence of characters!"
					+ " Must be of type a + ib, where a, b are real "
					+ "numbers, and i is the imaginary unit.");
		}
		return new Complex(real == null ? 0 : real.doubleValue(),
				imaginary == null ? 0 : imaginary.doubleValue());
	}

	/**
	 * The parsing procedure.
	 */
	private void parsingMethod() {
		while (true) {

			if (currentIndex >= end) {
				return;
			}

			skipSpaces();

			if (currentIndex >= end) {
				return;
			}

			if (data[currentIndex] == 'i') {
				currentIndex++;
				// edge case: 'i' is the last character
				imaginary = currentIndex < end ? parseNumber()
						: (currentOperator == '+' ? 1 : -1);
			} else if (isPartOfDouble(data[currentIndex])) {
				real = parseNumber();
			} else if (data[currentIndex] == '+' || data[currentIndex] == '-') {
				currentOperator = data[currentIndex++];
			} else {
				throw new ParserException("Invalid sequence of characters!"
						+ " Must be of type a + ib, where a, b are real "
						+ "numbers, and i is the imaginary unit.");
			}
		}

	}

	/**
	 * Parses a real number.
	 * 
	 * @return the parsed number
	 * @throws NumberFormatException
	 */
	private double parseNumber() throws NumberFormatException {
		StringBuilder sb = new StringBuilder();
		while (currentIndex < end && isPartOfDouble(data[currentIndex])) {
			sb.append(data[currentIndex++]);
		}

		sb.insert(0, currentOperator);
		double number;

		try {
			number = Double.parseDouble(sb.toString());
		} catch (NumberFormatException ex) {
			throw new ParserException(
					"\"" + sb.toString() + "\" is not a valid number!");
		}
		return number;
	}

	/**
	 * Utility method. Skips all whitespaces.
	 */
	private void skipSpaces() {
		while (currentIndex < end
				&& Character.isWhitespace(data[currentIndex])) {
			currentIndex++;
		}
	}

	/**
	 * Checks if the given character can be a part of a double value.
	 * 
	 * @param c
	 *            the character to be checked
	 * @return true if the given character can be a part of a double value
	 */
	private boolean isPartOfDouble(char c) {
		return (c >= '0' && c <= '9') || c == '.';
	}
}
