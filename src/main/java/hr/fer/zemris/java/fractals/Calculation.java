package hr.fer.zemris.java.fractals;

import java.util.concurrent.Callable;

import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Each thread runs this method and calculates its portion of the fractal image.
 * 
 * @author 0036502252
 *
 */
public class Calculation implements Callable<Void> {
	/**
	 * The real part of the complex number when mapped to the screen as a pixel.
	 * Indicates the real part of the starting pixel if it were represented as a
	 * complex number on a 2D plane.
	 */
	private double reMin;
	/**
	 * The real part of the complex number representing the last pixel.
	 */
	private double reMax;
	/**
	 * The imaginary part of the complex number representing the starting pixel.
	 */
	private double imMin;
	/**
	 * The imaginary part of the complex number representing the last pixel.
	 */
	private double imMax;
	/**
	 * The total width of the image.
	 */
	private int width;
	/**
	 * The total height of the image.
	 */
	private int height;
	/**
	 * The starting height at which the thread commences calculation.
	 */
	private int yMin;
	/**
	 * The height at which the thread ends calculation.
	 */
	private int yMax;
	/**
	 * Stores coloring data.
	 */
	private short[] data;

	/**
	 * The polynom used for calculation.
	 */
	private ComplexPolynomial polynom;
	/**
	 * The roots of the polynom used for calculation.
	 */
	private ComplexRootedPolynomial roots;
	/**
	 * The derived polynom.
	 */
	private ComplexPolynomial derived;
	/**
	 * The threshold used for getting root indexes.
	 */
	private static final double ROOT_THRESHOLD = 1E-3;
	/**
	 * The threshold used for double comparison.
	 */
	private static final double CONVERGENCE_THRESHOLD = 1E-3;
	/**
	 * The maximum number of iterations of calculations after which the thread
	 * decides that the pixel does not converge.
	 */
	private static final int MAX_ITERATIONS = 16 * 16 * 16;

	/**
	 * Creates a new {@link Calculation} thread job.
	 * 
	 * @param reMin
	 *            real part of the complex number representing the starting
	 *            pixel
	 * @param reMax
	 *            real part of the complex number representing the last pixel
	 * @param imMin
	 *            imaginary part of the complex number representing the starting
	 *            pixel
	 * @param imMax
	 *            imaginary part of the complex number representing the last
	 *            pixel
	 * @param width
	 *            total width of the image
	 * @param height
	 *            total height of the image
	 * @param yMin
	 *            starting height at which the thread commences calculation
	 * @param yMax
	 *            height at which the thread ends calculation
	 * @param data
	 *            coloring data
	 * @param polynom
	 *            polynom used for calculation
	 * @param roots
	 *            roots of the polynom used for calculation
	 */
	public Calculation(double reMin, double reMax, double imMin, double imMax,
			int width, int height, int yMin, int yMax, short[] data,
			ComplexPolynomial polynom, ComplexRootedPolynomial roots) {
		this.reMin = reMin;
		this.reMax = reMax;
		this.imMin = imMin;
		this.imMax = imMax;
		this.width = width;
		this.height = height;
		this.yMin = yMin;
		this.yMax = yMax;
		this.data = data;
		this.polynom = polynom;
		this.roots = roots;
		this.derived = polynom.derive();
	}

	/**
	 * Starts the thread job.
	 */
	@Override
	public Void call() throws Exception {
		int offset = yMin * width;

		for (int y = yMin; y <= yMax; y++) {
			for (int x = 0; x < width; x++) {

				double cre = x * (reMax - reMin) / (width - 1) + reMin;

				double cim = (height - 1 - y) * (imMax - imMin) / (height - 1)
						+ imMin;

				Complex zn = new Complex(cre, cim);
				Complex zn1;

				int iter = 0;
				double module = 0;

				do {
					Complex numerator = polynom.apply(zn);
					Complex denominator = derived.apply(zn);
					Complex fraction = numerator.div(denominator);

					zn1 = zn.sub(fraction);
					module = zn1.sub(zn).module();
					iter++;
					zn = zn1;
				} while (module > CONVERGENCE_THRESHOLD
						&& iter < MAX_ITERATIONS);

				int index = roots.indexOfClosestRootFor(zn1, ROOT_THRESHOLD);

				data[offset++] = (short) (index == -1 ? 0 : index);

			}
		}

		return null;
	}

}
