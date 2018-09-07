package hr.fer.zemris.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Testing class for the {@link Complex} class.
 * 
 * @author 0036502252
 */
@SuppressWarnings("javadoc")
public class TestComplex {

	@Test
	public void getRealTest() {
		Complex c = new Complex(3, -4);
		double expected = 3.0;

		assertEquals(c.getReal(), expected, 0.00001);
	}

	@Test
	public void getImaginaryTest() {
		Complex c = new Complex(3, -4);
		double expected = -4.0;

		assertEquals(c.getImaginary(), expected, 0.00001);
	}

	@Test
	public void getMagnitudeTest() {
		Complex c = new Complex(6, -8);
		double expected = 10.0;
		double compare = expected - c.module();

		assertTrue(Math.abs(compare) <= 0.00001);
	}

	@Test
	public void getAngleTest() {
		Complex c = new Complex(1, 1);
		double expected = (double) Math.PI / 4;

		assertEquals(c.angle(), expected, 0.00001);
	}

	@Test
	public void addTest() {
		Complex c1 = new Complex(6, -8);
		Complex c2 = new Complex(-5, 4);
		Complex c3 = c1.add(c2);
		Complex c4 = new Complex(1, -4);

		assertEquals(c3, c4);
	}

	@Test
	public void subTest() {
		Complex c1 = new Complex(3, -5);
		Complex c2 = new Complex(-2, 1);
		Complex c3 = c1.sub(c2);
		Complex c4 = new Complex(5, -6);

		assertEquals(c3, c4);
	}

	@Test
	public void mulTest() {
		Complex c1 = new Complex(3, -5);
		Complex c2 = new Complex(-2, 1);
		Complex c3 = c1.mul(c2);
		Complex c4 = new Complex(-1, 13);

		assertTrue(c3.equals(c4));
	}

	@Test
	public void divTest() {
		Complex c1 = new Complex(3, -5);
		Complex c2 = new Complex(-2, 1);
		Complex c3 = c1.div(c2);
		Complex c4 = new Complex(-2.2, 1.4);

		assertTrue(c3.equals(c4));
	}

	@Test
	public void powerTest() {
		Complex c1 = new Complex(3, -5);
		Complex c2 = new Complex(-198, -10);
		Complex c3 = c1.power(3);

		assertEquals(c2, c3);
	}
	
	public void rootTest() {
		Complex div1 = new Complex(4, 4);

		Complex result = div1.root(2).get(0);
		assertEquals(2.197368227, result.getReal(), 1E-5);
		assertEquals(0.9101797211, result.getImaginary(), 1E-5);
	}

}