package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.math.*;

/**
 * Takes roots of a polynom from the user, and generates a fractal using
 * Newton-Raphson iteration.
 * 
 * @author 0036502252
 *
 */
public class Newton {
	/**
	 * Roots start from this index.
	 */
	private static final int STARTING_INDEX = 1;

	/**
	 * Main method.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.println("Welcome to Newton-Raphson iteration-based fractal "
				+ "viewer.");
		System.out.println("Please enter at least two roots, one root per line."
				+ " Enter 'done' when done.");
		int i = STARTING_INDEX;
		List<Complex> factors = new ArrayList<>();

		while (true) {
			System.out.print("Root " + i + "> ");
			String line = sc.nextLine();
			if (line.equals("done"))
				break;

			Complex c = null;
			try {
				c = new ComplexParser(line).parse();
				factors.add(c);
			} catch (ParserException ex) {
				System.out.println(ex.getMessage());
				continue;
			} 
			System.out.println(c);
			i++;
		}
		sc.close();
		
		ComplexRootedPolynomial roots = new ComplexRootedPolynomial(
				(factors.toArray(new Complex[0])));

		System.out.println("Image of fractal will appear shortly. Thank you.");
		FractalViewer.show(new MyProducer(roots));
	}

}
