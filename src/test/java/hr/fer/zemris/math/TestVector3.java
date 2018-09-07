package hr.fer.zemris.math;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Testing class for the {@link Vector3} class.
 * 
 * @author 0036502252
 * 
 */
@SuppressWarnings("javadoc")
public class TestVector3 {
	Vector3 i;
	Vector3 j;
	public static final double DELTA = 1E-6;

	@Before
	public void init() {
		i = new Vector3(1, 0, 0);
		j = new Vector3(0, 1, 0);
	}

	@Test
	public void crossTest() {
		assertEquals("(0.000000, 0.000000, 1.000000)", i.cross(j).toString());
	}

	@Test
	public void scaleTest() {
		Vector3 k = i.cross(j).add(j).scale(5);
		assertEquals("(0.000000, 5.000000, 5.000000)", k.toString());
	}

	@Test
	public void dotTest() {
		double l = i.cross(j).add(j).scale(5).dot(j);

		assertEquals(5, l, DELTA);
	}

	@Test
	public void cosAngleTest() {
		assertEquals("(0.000000, 0.000000, 1.000000)", i.cross(j).toString());
	}

}
