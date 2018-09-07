package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Implementation of an {@link IFractalProducer}. Provides multithreading
 * support for drawing fractals based on Newton-Raphson iteration.
 * @author 0036502252
 *
 */
public class MyProducer implements IFractalProducer {
	/**
	 * Thread pool used for multithreading.
	 */
	private ExecutorService pool;
	/**
	 * The complex polynomial used for generating fractals.
	 */
	private ComplexPolynomial polynom;
	/**
	 * The roots of the complex polynomial.
	 */
	private ComplexRootedPolynomial roots;

	/**
	 * Constructs a new {@link MyProducer}.
	 * @param roots the complex polynomial roots used for generating a 
	 * fractal
	 */
	public MyProducer(ComplexRootedPolynomial roots) {
		this.roots = roots;
		this.polynom = roots.toComplexPolynom();
		pool = Executors.newFixedThreadPool(
				Runtime.getRuntime().availableProcessors(),
				new MyThreadFactory());

	}

	/**
	 * A simple {@link ThreadFactory} implementation which generates a daemon
	 * thread.
	 * @author 0036502252
	 *
	 */
	static class MyThreadFactory implements ThreadFactory {
		@Override
		public Thread newThread(Runnable target) {
			Thread t = new Thread(target);
			t.setDaemon(true);
			return t;
		}

	}

	/**
	 * Produces a new fractal drawing which is sent to the GUI. Delegates a 
	 * portion of the image to each thread for faster computation.
	 */
	@Override
	public void produce(double reMin, double reMax, double imMin, double imMax,
			int width, int height, long requestNo,
			IFractalResultObserver observer) {

		short[] data = new short[width * height];

		final int numOfSections = 8 * Runtime.getRuntime().availableProcessors();
		int sectionWidth = height / numOfSections;

		List<Future<Void>> results = new ArrayList<>();

		for (int i = 0; i < numOfSections; i++) {
			int yMin = i * sectionWidth;

			int yMax = (i + 1) * sectionWidth - 1;

			if (i == numOfSections - 1) {
				yMax = height - 1;
			}
			Calculation posao = new Calculation(reMin, reMax, imMin, imMax,
					width, height, yMin, yMax, data, polynom, roots);
			results.add(pool.submit(posao));
		}

		for (Future<Void> job : results) {
			try {
				job.get();
			} catch (InterruptedException | ExecutionException ignorable) {
			}
		}

		System.out.println(
				"Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");

		observer.acceptResult(data, (short) (polynom.order() + 1), requestNo);
	}

}
