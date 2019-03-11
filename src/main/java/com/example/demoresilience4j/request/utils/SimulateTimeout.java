package com.example.demoresilience4j.request.utils;

import java.util.Random;

import com.example.demoresilience4j.Api;

public class SimulateTimeout implements Runnable {

	private int requestNumber;
	private Api api;

	public SimulateTimeout(int requestNumber, Api api) {
		super();
		this.requestNumber = requestNumber;
		this.api = api;
	}

	@Override
	public void run() {
		api.doSomeWithDelay(requestNumber, this.someNumberBetween1200And2500());
	}

	private long someNumberBetween1200And2500() {
		return new Random().nextInt(2500 - 1200) + 1200;
	}
	
	/**
	 * Simulates ten asynchronous requests to api
	 * @param api
	 */
	public static void simulate(Api api) {
		
		System.out.println("\n\n\n");

		for (int i = 1; i <= 10; i++) {
			new Thread(new SimulateTimeout(i, api)).start();
		}
	}
}
