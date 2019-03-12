package com.example.demoresilience4j.fallback;

import org.springframework.stereotype.Component;

@Component
public class ApiFallback {

	public void fallbackDoSomeWithDelay(int requestNumber, long delayTime, Throwable exception) {
		System.out.println(String.format("Fallback being executed to request number %d . Exception: %s.", requestNumber,
				exception));
	}

	public String fallbackGetHello() {
		return "Calling Fallback! Requisição rejeitada pelo resilence4j!";
	}

	public void fallbackDoExceptionsForOddNumbers(int someValue, Throwable exception) {
		System.out.println(String.format("Fallback being executed to request number %d . Exception: %s.", someValue,
				exception.getMessage()));
	}

}
