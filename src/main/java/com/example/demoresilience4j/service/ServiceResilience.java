package com.example.demoresilience4j.service;

import org.springframework.stereotype.Service;

@Service
public class ServiceResilience {
		
	public void doSomeWithDelay(int requestNumber, long delayTime) {
		System.out.println(String.format("Conectando no banco de dados for request number %d will take %d ms",
				requestNumber, delayTime));
		try {
			Thread.sleep(delayTime);
		} catch (InterruptedException e) {
			System.out.println(String.format("Request number %d interrupted by exceed timeout!", requestNumber));
		}
	}
	
}
