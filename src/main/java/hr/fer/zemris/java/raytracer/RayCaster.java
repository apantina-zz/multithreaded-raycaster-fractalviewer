package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Starts the ray caster in single-threaded mode with a predefined scene and
 * point of view.
 * 
 * @author 0036502252
 *
 */
public class RayCaster {
	/**
	 * Main method.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(String[] args) {
		boolean useMultithreading = false;

		RayTracerViewer.show(new RayTracerProducerImpl(useMultithreading),
				new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}
}
