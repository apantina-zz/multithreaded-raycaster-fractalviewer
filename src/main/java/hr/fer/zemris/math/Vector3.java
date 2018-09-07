package hr.fer.zemris.math;

import java.util.Locale;
import java.util.Objects;

/**
 * An implementation of a 3D vector. Supports various operations.
 * 
 * @author 0036502252
 *
 */
public class Vector3 {
	/**
	 * The x-coordinate of the vector.
	 */
	private final double x;
	/**
	 * The y-coordinate of the vector.
	 */
	private final double y;
	/**
	 * The z-coordinate of the vector.
	 */
	private final double z;

	/**
	 * Constructs a new {@link Vector3}.
	 * 
	 * @param x
	 *            x-coordinate of the vector
	 * @param y
	 *            y-coordinate of the vector
	 * @param z
	 *            z-coordinate of the vector
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * @return the x-coordinate of the vector
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y-coordinate of the vector
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return the z-coordinate of the vector
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @return the norm of this vector
	 */
	public double norm() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * @return the normalized version of this vector. Does not change this
	 *         vector.
	 */
	public Vector3 normalized() {
		double norm = norm();
		return new Vector3(x / norm, y / norm, z / norm);
	}

	/**
	 * Adds this vector with another.
	 * 
	 * @param other
	 *            the vector to be added with this vector
	 * @return a new vector, resulting from the addition operation
	 */
	public Vector3 add(Vector3 other) {
		Objects.requireNonNull(other);
		return new Vector3(x + other.x, y + other.y, z + other.z);
	}

	/**
	 * Subtracts this vector from another.
	 * 
	 * @param other
	 *            the vector to be subtracted from this vector
	 * @return a new vector, resulting from the subtraction operation
	 */
	public Vector3 sub(Vector3 other) {
		Objects.requireNonNull(other);
		return new Vector3(x - other.x, y - other.y, z - other.z);
	}

	/**
	 * Calculates the dot product of this and another vector.
	 * 
	 * @param other
	 *            the vector used for calculating the dot product
	 * @return the resulting scalar
	 */
	public double dot(Vector3 other) {
		Objects.requireNonNull(other);
		return x * other.x + y * other.y + z * other.x;
	}

	/**
	 * Calculates the cosine of the angle between this and another vector.
	 * 
	 * @param other
	 *            the other vector used for calculating the cosine
	 * @return the cosine of the angle between this and another vector
	 */
	public double cosAngle(Vector3 other) {
		Objects.requireNonNull(other);
		return this.dot(other) / (this.norm() * other.norm());
	}

	/**
	 * Calculates the cross product of this and another vector.
	 * 
	 * @param other
	 *            the vector used for calculating the cross product
	 * @return the vector resulting from the cross product
	 */
	public Vector3 cross(Vector3 other) {
		Objects.requireNonNull(other);

		double crossX = this.y * other.z - this.z * other.y;
		double crossY = this.x * other.z - this.z * other.x;
		double crossZ = this.x * other.y - this.y * other.x;

		return new Vector3(crossX, crossY, crossZ);
	}

	/**
	 * Scales this vector using a scalar value.
	 * 
	 * @param s
	 *            the value used for scaling
	 * @return a new scaled vector
	 */
	public Vector3 scale(double s) {
		return new Vector3(s * x, s * y, s * z);
	}

	/**
	 * @return the array representation of this vector
	 */
	public double[] toArray() {
		return new double[] { x, y, z };
	}

	@Override
	public String toString() {
		return String.format(Locale.ROOT, "(%.6f, %.6f, %.6f)", x, y, z);
	}

	@Override
	public boolean equals(Object obj) {
		final double DELTA = 1E-6;

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Vector3))
			return false;

		Vector3 other = (Vector3) obj;
		if ((Math.abs(x - other.x) > DELTA))
			return false;
		if (Math.abs(y - other.y) > DELTA)
			return false;
		if (Math.abs(z - other.z) > DELTA)
			return false;

		return true;
	}
}
