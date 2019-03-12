package com.example.demoresilience4j.request.utils;

import java.util.Date;
import com.example.demoresilience4j.Api;

public class SimulateManyErrors implements Runnable {

	private int someValue;
	private Api api;

	public SimulateManyErrors(int someValue, Api api) {
		super();
		this.someValue = someValue;
		this.api = api;
	}

	@Override
	public void run() {
		api.doExceptionsForOddNumbers(someValue);
	}

	public static void simulate(Api api) {

		while (true) {

			wait3Seconds();

			for (int i = 1; i <= 20; i++) {
				System.out.println("Doing request number : " + i);
				new Thread(new SimulateManyErrors(i, api)).start();
			}

		}

	}

	private static void wait3Seconds() {
		try {
			Thread.sleep(3000);
			System.out.println("\n\n\n\n\n\n\n\n");
			System.out.println("Hora da Request: " + new Date());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}