package hr.fer.zemris.java.raytracer.model;

/**
 * A sphere graphical object implementation. Can be rendered in a {@link Scene}
 * when implementing a ray-caster.
 * 
 * @author 0036502252
 *
 */
public class Sphere extends GraphicalObject {
	/**
	 * The center of the sphere.
	 */
	Point3D center;
	/**
	 * The radius of the sphere.
	 */
	double radius;
	/**
	 * The diffuse coefficient of the sphere, for the color red.
	 */
	double kdr;
	/**
	 * The diffuse coefficient of the sphere, for the color green.
	 */
	double kdg;
	/**
	 * The diffuse coefficient of the sphere, for the color blue.
	 */
	double kdb;
	/**
	 * The reflective coefficient of the sphere, for the color red.
	 */
	double krr;
	/**
	 * The reflective coefficient of the sphere, for the color green.
	 */
	double krg;
	/**
	 * The reflective coefficient of the sphere, for the color blue.
	 */
	double krb;
	/**
	 * The roughness index of the sphere, used for calculating the reflective
	 * color component.
	 */
	double krn;

	/**
	 * Constructs a new {@link Sphere}.
	 * 
	 * @param center
	 *            center of the sphere
	 * @param radius
	 *            radius of the sphere
	 * @param kdr
	 *            diffuse coefficient of the sphere, for the color red
	 * @param kdg
	 *            diffuse coefficient of the sphere, for the color green
	 * @param kdb
	 *            diffuse coefficient of the sphere, for the color blue
	 * @param krr
	 *            reflective coefficient of the sphere, for the color red
	 * @param krg
	 *            reflective coefficient of the sphere, for the color green
	 * @param krb
	 *            reflective coefficient of the sphere, for the color blue
	 * @param krn
	 *            roughness index of the sphere, used for calculating the
	 *            reflective color component
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg,
			double kdb, double krr, double krg, double krb, double krn) {
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	/**
	 * Calculates the nearest intersection of the ray and this sphere. Returns
	 * null if no intersection is found.
	 */
	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		boolean outer = true;

		double b = 2 * (ray.direction.scalarProduct(ray.start.sub(center)));
		double c = ray.start.sub(center).scalarProduct(ray.start.sub(center))
				- radius * radius;
		double discriminant = b * b - 4 * c;

		if (discriminant < 0)
			return null; // no intersections found

		double d1 = (-b + Math.sqrt(discriminant)) / 2.0;
		double d2 = (-b - Math.sqrt(discriminant)) / 2.0;

		double nearestDistance;

		if (d1 < 0 && d2 > 0) {
			nearestDistance = d2;
		} else if (d1 > 0 && d2 < 0) {
			nearestDistance = d1;
		} else if (d1 < 0 && d2 < 0) {
			outer = false;
			nearestDistance = Math.max(d1, d2);
		} else {
			nearestDistance = Math.min(d1, d2);
		}

		Point3D pointOfIntersection = ray.start
				.add(ray.direction.scalarMultiply(nearestDistance));

		return new RayIntersection(pointOfIntersection, d2, outer) {

			@Override
			public Point3D getNormal() {
				return center.sub(getPoint()).normalize();
			}

			@Override
			public double getKrr() {
				return krr;
			}

			@Override
			public double getKrn() {
				return krn;
			}

			@Override
			public double getKrg() {
				return krg;
			}

			@Override
			public double getKrb() {
				return krb;
			}

			@Override
			public double getKdr() {
				return kdr;
			}

			@Override
			public double getKdg() {
				return kdg;
			}

			@Override
			public double getKdb() {
				return kdb;
			}
		};
	}
}
