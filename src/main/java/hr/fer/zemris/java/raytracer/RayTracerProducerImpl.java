package hr.fer.zemris.java.raytracer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * A simple ray casting algorithm implementation. Using a {@link Scene}, it
 * calculates ray intersections with it. Also calculates color intensity for
 * each pixel with the given light sources, using Phong's color model.
 * 
 * @author 0036502252
 * @see Ray
 * @see LightSource
 *
 */
public class RayTracerProducerImpl implements IRayTracerProducer {
	/**
	 * An error threshold used when comparing double values.
	 */
	private static double THRESHOLD = 10E-3;
	/**
	 * Passed to the constructor. If true, then the program runs using multiple
	 * threads.
	 */
	private boolean useMultithreading;
	/**
	 * The thread pool used for multithreading.
	 */
	private ForkJoinPool pool;
	/**
	 * The maximum number of lines that a single thread can calculate pixels
	 * for.
	 */
	private static final int LINES_PER_THREAD = 16;

	/**
	 * Constructs a new {@link RayTracerProducerImpl}.
	 * 
	 * @param useMultithreading
	 *            if true, the implementation will be run using multiple
	 *            threads.
	 */
	public RayTracerProducerImpl(boolean useMultithreading) {
		this.useMultithreading = useMultithreading;
		if (useMultithreading) {
			this.pool = new ForkJoinPool();
		}
	}

	/**
	 * The method used for calculation. Assigns a portion of the scene to be
	 * rendered to each separate thread.
	 * 
	 * @author 0036502252
	 *
	 */
	private static class Calculation extends RecursiveAction {

		/**
		 * Auto-generated serial version ID.
		 */
		private static final long serialVersionUID = -1249175631972502174L;

		/**
		 * The thread pool used for multithreading.
		 */
		private ForkJoinPool pool;

		/**
		 * The height of the picture to be rendered.
		 */
		private int height;
		/**
		 * The width of the picture to be rendered.
		 */
		private int width;
		/**
		 * The minimum height from which drawing starts.
		 */
		private int yMin;
		/**
		 * The minimum height where drawing ends.
		 */
		private int yMax;

		/**
		 * Utility parameter used when calculating x-axis vectors.
		 */
		private double horizontal;
		/**
		 * Utility parameter used when calculating y-axis vectors.
		 */
		private double vertical;

		/**
		 * Indicates the upper-left corner of the screen.
		 */
		private Point3D screenCorner;
		/**
		 * Indicates the observer's point of view.
		 */
		private Point3D eye;
		/**
		 * The normalized x-axis vector.
		 */
		private Point3D xAxisNormalized;
		/**
		 * The normalized y-axis vector.
		 */
		private Point3D yAxisNormalized;

		/**
		 * The scene to be rendered.
		 */
		private Scene scene;

		/**
		 * The intensity of red color for each pixel in the image.
		 */
		private short[] red;
		/**
		 * The intensity of green color for each pixel in the image.
		 */
		private short[] green;
		/**
		 * The intensity of blue color for each pixel in the image.
		 */
		private short[] blue;

		/**
		 * Constructs a new {@link Calculation} job.
		 * 
		 * @param pool
		 *            the thread pool
		 * @param height
		 *            image height
		 * @param width
		 *            image width
		 * @param yMin
		 *            vertical start of drawing
		 * @param yMax
		 *            vertical end of drawing
		 * @param horizontal
		 *            utility param
		 * @param vertical
		 *            utility param
		 * @param screenCorner
		 *            the corner of the render
		 * @param eye
		 *            the point of view
		 * @param xAxisNormalized
		 *            normalized x-axis vector
		 * @param yAxisNormalized
		 *            normalized y-axis vector
		 * @param scene
		 *            the scene to be rendered
		 * @param red
		 *            the intensity of red color for each pixel in the image
		 * @param green
		 *            the intensity of green color for each pixel in the image
		 * @param blue
		 *            the intensity of blue color for each pixel in the image
		 */
		public Calculation(ForkJoinPool pool, int height, int width, int yMin,
				int yMax, double horizontal, double vertical,
				Point3D screenCorner, Point3D eye, Point3D xAxisNormalized,
				Point3D yAxisNormalized, Scene scene, short[] red,
				short[] green, short[] blue) {
			this.pool = pool;
			this.height = height;
			this.width = width;
			this.yMin = yMin;
			this.yMax = yMax;
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.screenCorner = screenCorner;
			this.eye = eye;
			this.xAxisNormalized = xAxisNormalized;
			this.yAxisNormalized = yAxisNormalized;
			this.scene = scene;
			this.red = red;
			this.green = green;
			this.blue = blue;
		}

		/**
		 * Each thread computes a portion of the image, depending on the set
		 * limit, determined by the <code>LINES_PER_THREAD</code> constant.
		 */
		@Override
		protected void compute() {
			if (yMax - yMin <= LINES_PER_THREAD) {
				calculate();
				return;
			}

			Calculation calc1 = new Calculation(pool, height, width, yMin,
					(yMin + yMax) / 2, horizontal, vertical, screenCorner, eye,
					xAxisNormalized, yAxisNormalized, scene, red, green, blue);
			Calculation calc2 = new Calculation(pool, height, width,
					(yMin + yMax) / 2, yMax, horizontal, vertical, screenCorner,
					eye, xAxisNormalized, yAxisNormalized, scene, red, green,
					blue);

			invokeAll(calc1, calc2);
		}

		/**
		 * Calculates the color intensity (Phong model) for each pixel of the
		 * image, depending on whether the ray finds an object in the scene.
		 */
		protected void calculate() {

			short[] rgb = new short[3];
			int offset = yMin * width;
			for (int y = yMin; y < yMax; y++) {
				for (int x = 0; x < width; x++) {

					Point3D screenPoint = screenCorner
							.add(xAxisNormalized.scalarMultiply(
									x * horizontal / (width - 1)))
							.sub(yAxisNormalized.scalarMultiply(
									y * vertical / (height - 1)));

					Ray ray = Ray.fromPoints(eye, screenPoint);

					RayIntersection closest = findClosestIntersection(scene,
							ray);

					if (closest == null) {
						rgb[0] = rgb[1] = rgb[2] = 0;
					} else {
						rgb = determineColor(scene, closest, eye);
					}

					red[offset] = rgb[0] > 255 ? 255 : rgb[0];
					green[offset] = rgb[1] > 255 ? 255 : rgb[1];
					blue[offset] = rgb[2] > 255 ? 255 : rgb[2];

					offset++;
				}
			}
		}

		/**
		 * When an intersection occurs, determine the color of the pixel
		 * depending on the intersected object and position relative to each of
		 * the light sources.
		 * 
		 * @param scene
		 *            the scene to be rendered
		 * @param intersection
		 *            the point at which the intersection occured
		 * @param eye
		 *            the point of view
		 * @return array where each value determines the intensity of the given
		 *         color, where arr[0] is red, arr[1] is green, and arr[2] is
		 *         blue.
		 */
		private short[] determineColor(Scene scene,
				RayIntersection intersection, Point3D eye) {
			short[] rgb = new short[3];
			rgb[0] = 15;
			rgb[1] = 15;
			rgb[2] = 15;

			for (LightSource source : scene.getLights()) {

				Ray position = Ray.fromPoints(source.getPoint(),
						intersection.getPoint());

				RayIntersection closestIntersection = findClosestIntersection(
						scene, position);

				// check if the light source is obscured at this meeting point
				if (closestIntersection == null
						|| intersection.getPoint().sub(source.getPoint())
								.norm() > THRESHOLD + closestIntersection
										.getPoint().sub(source.getPoint())
										.norm()) {

					continue;
				}

				// calculate the diffuse and reflective component for each color
				for (int i = 0; i < 3; i++) {
					rgb[i] += calculateDiffuseComponent(i, source, intersection)
							+ calculateReflectiveComponent(i, source,
									intersection, eye);
				}

			}

			return rgb;
		}

		/**
		 * Finds the closest intersection for the given ray in the scene.
		 * 
		 * @param scene
		 *            the scene to be rendered
		 * @param ray
		 *            the ray to be checked
		 * @return the closest possible intersection, or null if none are found
		 */
		private RayIntersection findClosestIntersection(Scene scene, Ray ray) {
			RayIntersection minIntersection = null;

			for (GraphicalObject object : scene.getObjects()) {

				RayIntersection i = object.findClosestRayIntersection(ray);
				if (i == null)
					continue;

				if (minIntersection == null
						|| i.getDistance() < minIntersection.getDistance()) {
					minIntersection = i;
				}
			}
			return minIntersection;
		}

		/**
		 * Caclulates the diffuse component for the given color intensity, light
		 * source and intersection.
		 * 
		 * @param rgb
		 *            the color intensity: 0 for red, 1 for green, 2 for blue.
		 * @param source
		 *            the light source
		 * @param intersection
		 *            the intersection of ray and object from the scene
		 * @return the diffuse component for the given color intensity
		 * 
		 */
		private short calculateDiffuseComponent(int rgb, LightSource source,
				RayIntersection intersection) {

			Point3D l = intersection.getPoint().sub(source.getPoint())
					.normalize();
			Point3D n = intersection.getNormal();

			double scalar = l.scalarProduct(n);

			int colorIntensity = rgb == 0 ? source.getR()
					: rgb == 1 ? source.getG() : source.getB();

			double coefficient = rgb == 0 ? intersection.getKdr()
					: rgb == 1 ? intersection.getKdg() : intersection.getKdb();

			return scalar > 0 ? (short) (scalar * colorIntensity * coefficient)
					: 0;
		}

		/**
		 * Caclulates the reflective component for the given color intensity,
		 * light source and intersection, depending on the point of view.
		 * 
		 * @param rgb
		 *            the color intensity: 0 for red, 1 for green, 2 for blue.
		 * @param source
		 *            the light source
		 * @param intersection
		 *            the intersection of ray and object from the scene
		 * @param eye
		 *            the point of view vector
		 * @return the diffuse component for the given color intensity
		 * 
		 */
		private short calculateReflectiveComponent(int rgb, LightSource source,
				RayIntersection intersection, Point3D eye) {

			Point3D l = intersection.getPoint().sub(source.getPoint())
					.normalize().negate();
			Point3D v = intersection.getPoint().sub(eye).normalize().negate();
			Point3D n = intersection.getNormal();

			Point3D r = n.scalarMultiply(2. * l.scalarProduct(n)).sub(l)
					.normalize();
			int colorIntensity = rgb == 0 ? source.getR()
					: rgb == 1 ? source.getG() : source.getB();
			double coefficient = rgb == 0 ? intersection.getKrr()
					: rgb == 1 ? intersection.getKrg() : intersection.getKrb();

			double cosine = Math.pow(r.scalarProduct(v), intersection.getKrn());

			return cosine > 0 ? (short) (colorIntensity * coefficient * cosine)
					: 0;
		}
	}

	/**
	 * Renders the scene, in single or multithreaded mode, depending on how 
	 * the constructor was called.
	 */
	@Override
	public void produce(Point3D eye, Point3D view, Point3D viewUp,
			double horizontal, double vertical, int width, int height,
			long requestNo, IRayTracerResultObserver observer) {
		System.out.println("Starting calculations.");

		long startTime = System.currentTimeMillis();

		short[] red = new short[width * height];
		short[] green = new short[width * height];
		short[] blue = new short[width * height];

		Point3D og = view.sub(eye).normalize();
		Point3D viewUpNormalized = viewUp.normalize();

		Point3D yAxisNormalized = viewUpNormalized
				.sub(og.scalarMultiply(og.scalarProduct(viewUpNormalized)));
		Point3D xAxisNormalized = og.vectorProduct(yAxisNormalized).normalize();

		Point3D screenCorner = view
				.sub(xAxisNormalized.scalarMultiply(horizontal / 2))
				.add(yAxisNormalized.scalarMultiply(horizontal / 2));

		Scene scene = RayTracerViewer.createPredefinedScene();

		if (useMultithreading) {
			pool.invoke(new Calculation(pool, height, width, 0, height - 1,
					horizontal, vertical, screenCorner, eye, xAxisNormalized,
					yAxisNormalized, scene, red, green, blue));
		} else {
			new Calculation(pool, height, width, 0, height - 1, horizontal,
					vertical, screenCorner, eye, xAxisNormalized,
					yAxisNormalized, scene, red, green, blue).calculate();
		}

		System.out.println("Calculations done.");
		observer.acceptResult(red, green, blue, requestNo);
		System.out.println("The observer has been notified.");

		long endTime = System.currentTimeMillis();

		String a = useMultithreading ? "multithreaded" : "singlethreaded";

		System.out.println("Ran as " + a + " program, and it took "
				+ (endTime - startTime) + " miliseconds.");
	}
}