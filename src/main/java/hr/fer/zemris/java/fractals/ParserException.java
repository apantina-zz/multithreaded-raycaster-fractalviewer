package hr.fer.zemris.java.fractals;

/**
 * A runtime exception used in the {@link ComplexParser} class.
 * 
 * @author 0036502252
 *
 */
public class ParserException extends RuntimeException {
	/**
	 * Auto-generated serial ID for this exception.
	 */
	static final long serialVersionUID = -7034897190745766939L;

	/**
	 * Constructs a new ParserException with {@code null} as its detail message.
	 * The cause is not initialized, and may subsequently be initialized by a
	 * call to {@link #initCause}.
	 */
	public ParserException() {
		super();
	}

	/**
	 * Constructs a new ParserException with the specified detail message.
	 * 
	 * @param message
	 *            the detail message.
	 */
	public ParserException(String message) {
		super(message);
	}

	/**
	 * Constructs a new ParserException with the specified detail message and
	 * cause.
	 * 
	 * @param message
	 *            the detail message
	 * @param cause
	 *            the cause
	 */
	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new ParserException with the specified cause.
	 * 
	 * @param cause
	 *            the cause
	 */
	public ParserException(Throwable cause) {
		super(cause);
	}

}
