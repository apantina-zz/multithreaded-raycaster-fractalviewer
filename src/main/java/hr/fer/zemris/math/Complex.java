package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a complex number. Provides support for various operations.
 * 
 * @author 0036502252
 *
 */
public class Complex {
	/**
	 * Real part of the complex number.
	 */
	private final double real;
	/**
	 * Imaginary part of the complex number.
	 */
	private final double imaginary;

	/**
	 * A complex number with both the real and imaginary part set to zero.
	 */
	public static final Complex ZERO = new Complex(0, 0);
	/**
	 * A complex number a + ib, where a = 1 and b = 0.
	 */
	public static final Complex ONE = new Complex(1, 0);
	/**
	 * A complex number a + ib, where a = -1 and b = 0.
	 */
	public static final Complex ONE_NEG = new Complex(-1, 0);
	/**
	 * A complex number a + ib, where a = 0 and b = 1.
	 */
	public static final Complex IM = new Complex(0, 1);
	/**
	 * A complex number a + ib, where a = 0 and b = -1.
	 */
	public static final Complex IM_NEG = new Complex(0, -1);
	/**
	 * Threshold for comparing two complex numbers.
	 */
	private static final double DELTA = 1E-4;

	/**
	 * Constructs a new {@link Complex}.
	 * 
	 * @param real
	 *            the real part of the complex number
	 * @param imaginary
	 *            the imaginary part of the complex number
	 */
	public Complex(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	/**
	 * @return the imaginary part of the complex number
	 */
	public double getImaginary() {
		return imaginary;
	}

	/**
	 * @return the real part of the complex number
	 */
	public double getReal() {
		return real;
	}

	/**
	 * Performs the addition operation between this and another complex number.
	 * 
	 * @param c
	 *            the other complex number
	 * @return the new complex number as a result of the operation
	 */
	public Complex add(Complex c) {
		return new Complex(this.real + c.real, this.imaginary + c.imaginary);
	}

	/**
	 * Performs the subtraction operation between this and another complex
	 * number.
	 * 
	 * @param c
	 *            the other complex number
	 * @return the new complex number as a result of the operation
	 */
	public Complex sub(Complex c) {
		return new Complex(this.real - c.real, this.imaginary - c.imaginary);
	}

	/**
	 * @return this complex number, with both the real and imaginary part
	 *         negated
	 */
	public Complex negate() {
		return new Complex(-real, -imaginary);
	}

	/**
	 * @return the module of this complex number
	 */
	public double module() {
		return Math.sqrt(real * real + imaginary * imaginary);
	}

	/**
	 * Performs the division operation between this and another complex number.
	 * 
	 * @param c
	 *            the other complex number
	 * @return the new complex number as a result of the operation
	 */
	public Complex div(Complex c) {
		double a = this.real * c.real + this.imaginary * c.imaginary;
		double b = this.imaginary * c.real - this.real * c.imaginary;
		double denominator = c.real * c.real + c.imaginary * c.imaginary;

		return new Complex((double) a / denominator, (double) b / denominator);
	}

	/**
	 * Performs the power operation on this complex number.
	 * 
	 * @param n
	 *            the desired power
	 * @return the new complex number as a result of the operation
	 */
	public Complex power(int n) {
		if (n < 0)
			throw new IllegalArgumentException("Argument must be >=0!");
		double re = Math.pow(module(), n) * Math.cos(n * angle());
		double im = Math.pow(module(), n) * Math.sin(n * angle());
		return new Complex(re, im);
	}

	/**
	 * @return the angle of this complex number, in radians, between 0 and 2PI.
	 */
	public double angle() {
		double angle = Math.atan2(imaginary, real);
		return angle < 0 ? angle + Math.PI * 2 : angle;
	}

	/**
	 * Calculates the roots of the complex number.
	 * 
	 * @param n
	 *            the desired number of rootss
	 * @return an array of the roots calculated7
	 * @throws IllegalArgumentException
	 *             if the root is not > 0
	 */
	public List<Complex> root(int n) {
		if (n <= 0)
			throw new IllegalArgumentException("Argument must be >0!");

		double nthRoot = rootN(module(), n);
		double angle = angle();

		List<Complex> roots = new ArrayList<>();

		for (int i = 0; i < n; i++) {
			double re = nthRoot
					* Math.cos((double) (angle + 2 * i * Math.PI) / n);
			double im = nthRoot
					* Math.sin((double) (angle + 2 * i * Math.PI) / n);
			roots.add(new Complex(re, im));
		}
		return roots;
	}

	/**
	 * Utility method. Calculate the nth root of a real number.
	 * 
	 * @param num
	 *            the number upon which the operation is made
	 * @param root
	 *            the desired root
	 * @return the nth root
	 */
	private static double rootN(double num, double root) {
		return Math.pow(Math.E, Math.log(num) / root);
	}

	/**
	 * Multiplies this complex number with another.
	 * 
	 * @param c
	 *            the complex number with which <code>this</code> is multiplied
	 * @return the resulting complex number
	 */
	public Complex mul(Complex c) {
		double a = this.real * c.real - this.imaginary * c.imaginary;
		double b = this.real * c.imaginary + this.imaginary * c.real;
		return new Complex(a, b);
	}

	@Override
	public String toString() {
		if (real != 0 && imaginary != 0) {
			return String.format("%s %si", real,
					imaginary > 0 ? "+ " + imaginary
							: "- " + Math.abs(imaginary));

		} else if (real == 0 && imaginary != 0) {
			return String.format("%.4si", imaginary);
		} else if (real != 0 && imaginary == 0) {
			return String.format("%.4s", real);
		} else {
			return "0";
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Complex))
			return false;
		Complex other = (Complex) obj;

		double compareReal = this.getReal() - other.getReal();
		double compareImaginary = this.getImaginary() - other.getImaginary();

		if (Math.abs(compareReal) > DELTA)
			return false;
		if (Math.abs(compareImaginary) > DELTA)
			return false;

		return true;
	}

}
