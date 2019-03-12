package com.example.demoresilience4j.service;

import org.springframework.stereotype.Service;

@Service
public class SomeService {
		
	public void doSomeWithDelay(int requestNumber, long delayTime) {
		System.out.println(String.format("Conectando no banco de dados for request number %d will take %d ms",
				requestNumber, delayTime));
		try {
			Thread.sleep(delayTime);
		} catch (InterruptedException e) {
			System.out.println(String.format("Request number %d interrupted by exceed timeout!", requestNumber));
		}
	}
	
	public String getHello() throws InterruptedException {	
		Thread.sleep(100);
		return "Hello! Requisição processada!";
	}
	
	public String doExceptionsForOddNumbers(int someValue) {

		System.out.println("Conectando no banco de dados for request number " + someValue);

		if (someValue % 2 != 0) {
			throw new RuntimeException("Banco de dados indisponível!");
		}
		
		return "success";

	}
	
}
