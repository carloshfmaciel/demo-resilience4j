package com.example.demoresilience4j.request.utils;

import java.util.Date;

import com.example.demoresilience4j.Api;

public class SimulateExcessiveRequestsWaiting {

	public static void simulate(Api api) {

		while (true) {

			wait3Seconds();

			for (int i = 1; i <= 20; i++) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							System.out.println(api.getHello());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}

//			ExecutorService threads = Executors.newFixedThreadPool(20);
//			List<Callable<String>> torun = new ArrayList<>(20);
//			for (int i = 1; i <= 20; i++) {
//				torun.add(new Callable<String>() {
//					public String call() throws InterruptedException {
//						return api.getHello();
//					}
//				});
//			}
//			
//			// all tasks executed in different threads, at 'once'.
//			try {
//				List<Future<String>> futures = threads.invokeAll(torun);
//			    
//				// no more need for the threadpool
//			    threads.shutdown();
//			} catch (InterruptedException e) {
//				System.out.println("Thread interrupted!!");
//			}
			

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
