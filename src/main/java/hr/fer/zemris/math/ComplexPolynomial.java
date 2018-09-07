package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Represents a complex polynomial, of the form
 * zn*zn+z3^(n-1)*zn-1+...+z^2*z2+z*z1+z0
 * 
 * @author 0036502252
 *
 */
public class ComplexPolynomial {
	/**
	 * The factors of this polynomial.
	 */
	private final Complex[] factors;

	/**
	 * Constructs a new {@link ComplexPolynomial}.
	 * 
	 * @param factors
	 *            the factors of the polynomial
	 */
	public ComplexPolynomial(Complex... factors) {
		Objects.requireNonNull(factors);
		if (factors.length == 0) {
			throw new IllegalArgumentException(
					"Invalid polynomial! Must have at least 1 factor!");
		}
			
		this.factors = factors;
	}

	/**
	 * @return the order of this polynomial
	 */
	public short order() {
		return (short) (factors.length - 1);
	}

	/**
	 * Multiplies this polynomial with another.
	 * 
	 * @param p
	 *            the other polynomial
	 * @return the result of multiplication
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		int totalLength = this.factors.length + p.getFactors().length - 1;
		Complex[] resultingFactors = new Complex[totalLength];

		for (int i = 0; i < totalLength; i++) {
			resultingFactors[i] = Complex.ZERO;
		}

		for (int i = 0, n = this.factors.length; i < n; i++) {
			for (int j = 0, m = p.getFactors().length; j < m; j++) {
				resultingFactors[i + j] = resultingFactors[i + j]
						.add(this.factors[i].mul(p.getFactors()[j]));
			}
		}

		return new ComplexPolynomial(resultingFactors);

	}

	/**
	 * Computes the first derivative of this polynomial; for example, for
	 * (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
	 * 
	 * @return the derivated polynomial
	 */
	public ComplexPolynomial derive() {
		Complex[] derivatives = new Complex[factors.length - 1];

		for (int i = 0, n = derivatives.length; i < n; i++) {
			derivatives[i] = factors[i]
					.mul(new Complex(factors.length - 1 - i, 0));
		}

		return new ComplexPolynomial(derivatives);
	}

	/**
	 * Computes polynomial value at given point z.
	 * 
	 * @param z
	 *            the point used for calculation
	 * @return the complex number resulting from applying <code>z</code>
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ZERO;
		for (int i = 0, n = factors.length; i < n; i++) {
			result = result.add(z.power(n - i - 1).mul(factors[i]));
		}

		return result;
	}

	/**
	 * @return the factors of the polynomial
	 */
	public Complex[] getFactors() {
		return factors;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		int i = order();
		for (Complex factor : factors) {
			sb.append("(" + factor + ")" + (i == 0 ? "" : "z^" + i)
					+ (i == 0 ? "" : " + "));
			i--;
		}

		return sb.toString();
	}

}
