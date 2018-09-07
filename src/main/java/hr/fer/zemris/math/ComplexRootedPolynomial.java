package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author 0036502252
 *
 */
/**
 * Represents a complex polynomial, of the form (z-z1)*(z-z2)*...*(z-zn)
 * 
 * @author 0036502252
 *
 */
public class ComplexRootedPolynomial {
	/**
	 * Returned by the {@link #indexOfClosestRootFor(Complex, double)} method
	 * when no index is found.
	 */
	private static final int INVALID_INDEX = -1;
	/**
	 * The roots of this polynomial.
	 */
	private final Complex[] roots;

	/**
	 * Constructs a new {@link ComplexRootedPolynomial}.
	 * 
	 * @param roots
	 *            the roots to be assigned to the polynomial
	 */
	public ComplexRootedPolynomial(Complex... roots) {
		Objects.requireNonNull(roots);
		if (roots.length == 0) {
			throw new IllegalArgumentException(
					"Invalid polynomial! Must have at least 1 root!");
		}

		this.roots = Arrays.copyOf(roots, roots.length);
	}

	/**
	 * Computes polynomial value at given point z.
	 * 
	 * @param z
	 *            the point used for calculation
	 * @return the complex number resulting from applying <code>z</code>
	 */
	public Complex apply(Complex z) {
		Complex result = z.sub(roots[0]);

		for (int i = 1, n = roots.length; i < n; i++) {
			result = result.mul(z.sub(roots[i]));
		}

		return result;
	}

	/**
	 * Converts this representation to {@link ComplexPolynomial} type.
	 * 
	 * @return
	 */
	public ComplexPolynomial toComplexPolynom() {
		if (roots.length == 0)
			return new ComplexPolynomial();

		ComplexPolynomial result = new ComplexPolynomial(Complex.ONE,
				roots[0].negate());

		for (int i = 1, n = roots.length; i < n; i++) {
			ComplexPolynomial nextPolynomial = new ComplexPolynomial(
					Complex.ONE, roots[i].negate());
			result = result.multiply(nextPolynomial);
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("f(z) = ");
		for (Complex root : roots) {
			sb.append("(z - (" + root + "))*");
		}
		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * Finds index of closest root for given complex number z that is within
	 * threshold; if there is no such root, returns -1
	 * 
	 * @param z
	 *            the complex number for which the index is searched
	 * @param threshold
	 *            the margin for error
	 * @return index of closest root for given complex number z that is within
	 *         threshold, or -1 if none is found
	 */
	public int indexOfClosestRootFor(Complex z, double threshold) {
		int minIndex = INVALID_INDEX;

		for (int i = 0, n = roots.length; i < n; i++) {
			if (minIndex == INVALID_INDEX || (z.sub(roots[i])
					.module() < (z.sub(roots[minIndex]).module()))) {
				minIndex = i;
			}
		}

		return (z.sub(roots[minIndex]).module()) < threshold ? minIndex + 1
				: INVALID_INDEX;

	}

}
