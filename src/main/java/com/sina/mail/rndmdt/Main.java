package com.sina.mail.rndmdt;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		if (args.length == 0)
			System.out.println("input some words ");
		 
		Detector dt=new Detector();
		dt.computeDefaultProbDist();
		for (String input : args)
			System.out.println(dt.segment(input));
		
		// use specified word probability distribution
		/*
		 * Detector dt=new Detector();
		 * dt.computeProbDist("/home/limeng/downloads/word_count.txt");
		 * System.out.println(dt.segment("dhelloworldd"));
		 */

		// use default word probability distribution
		/*
		 * Detector dt=new Detector();
		 * dt.computeDefaultProbDist();
		 * System.out.println(dt.segment("dhelloworldd"));
		 */


	}
}
