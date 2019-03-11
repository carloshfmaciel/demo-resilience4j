package com.example.demoresilience4j;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demoresilience4j.service.ServiceResilience;

import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.vavr.control.Try;

@Service
public class Api {

	@Autowired
	private ServiceResilience serviceResilience;

	public String getHello() throws InterruptedException {
		Thread.sleep(1000);
		return "Hello! Requisição processada!";
	}

	public String fallbackGetHello() {
		return "Calling Fallback! Requisição rejeitada pelo Hystrix!";
	}

	public void doExceptionsForOddNumbers(int someValue) {

		System.out.println("Conectando no banco de dados for request number " + someValue);

		if (someValue % 2 != 0) {
			throw new RuntimeException("Banco de dados indisponível!");
		}

	}

	public void fallbackDoExceptionsForOddNumbers(int someValue, Throwable exception) {
		System.out.println(String.format("Fallback being executed to request number %d . Exception: %s.", someValue,
				exception.getMessage()));
	}

	public void doSomeWithDelay(int requestNumber, long delayTime) {

		TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
				.timeoutDuration(Duration.ofMillis(2000))
				.build();

		Supplier<Future> supplier = () -> {
			return Executors.newSingleThreadExecutor().submit(() -> {
				serviceResilience.doSomeWithDelay(requestNumber, delayTime);
			});
		};

//		try {
//			TimeLimiter.of(timeLimiterConfig).executeFutureSupplier(supplier);
//		} catch (Exception e) {
//			fallbackDoSomeWithDelay(requestNumber, delayTime, e);
//		}

		Try.ofCallable(TimeLimiter.decorateFutureSupplier(TimeLimiter.of(timeLimiterConfig), supplier))
		   .onFailure(exception -> { 
			   fallbackDoSomeWithDelay(requestNumber, delayTime, exception);
		});
		
	}

	public void fallbackDoSomeWithDelay(int requestNumber, long delayTime, Throwable exception) {
		System.out.println(String.format("Fallback being executed to request number %d . Exception: %s.", requestNumber,
				exception));
	}

}
