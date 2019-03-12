package com.example.demoresilience4j;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demoresilience4j.fallback.ApiFallback;
import com.example.demoresilience4j.service.SomeService;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.vavr.control.Try;

@Service
public class Api {

	@Autowired
	private SomeService someService;

	@Autowired
	private ApiFallback fallback;

	public String getHello() throws InterruptedException {
		BulkheadConfig config = BulkheadConfig.custom().maxConcurrentCalls(10).maxWaitTime(0).build();

//		BulkheadRegistry registry = BulkheadRegistry.of(config);
//		Bulkhead bulkhead = registry.bulkhead("bhGetHelloEndpoint");

		Bulkhead bulkhead = Bulkhead.of("bulkhead", config);

//		Supplier<String> supplier = () -> {
//			try {
//				return someService.getHello();
//			} catch (InterruptedException e) {
//				System.out.println("Thread interrompida pelo resilience4j!");
//				return "Thread interrompida pelo resilience4j!";
//			}
//		};
//
//		return Try.ofSupplier(Bulkhead.decorateSupplier(bulkhead, supplier))
//				.onFailure(exception -> {
//					fallback.fallbackGetHello();
//				})
//				.get();

//		bulkhead.getEventPublisher()
//			.onCallPermitted(event -> System.out.println("Permitido"))
//		    .onCallRejected(event -> System.out.println("Rejeitado"))
//		    .onCallFinished(event -> System.out.println("Finished"));

		return Try.ofCallable(Bulkhead.decorateCallable(bulkhead, new Callable<String>() {
			@Override
			public String call() throws Exception {
				return someService.getHello();
			}
		})).onFailure(exception -> {
			fallback.fallbackGetHello();
		}).get();

	}

	public void doExceptionsForOddNumbers(int someValue) {
		CircuitBreakerConfig config = CircuitBreakerConfig.custom()
				.failureRateThreshold(30)
				.waitDurationInOpenState(Duration.ofMillis(10000))
				.ringBufferSizeInClosedState(1)
				.build();
		
		CircuitBreaker circuitBreaker = CircuitBreaker.of("circuitDoExceptionsForOddNumbers", config);

		Try.ofSupplier(CircuitBreaker.decorateSupplier(circuitBreaker, () -> someService.doExceptionsForOddNumbers(someValue)))
				.onFailure(exception -> {
					fallback.fallbackDoExceptionsForOddNumbers(someValue, exception);
				});

	}

	public void doSomeWithDelay(int requestNumber, long delayTime) {

		TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom().timeoutDuration(Duration.ofMillis(2000))
				.build();

		Supplier<Future> supplier = () -> {
			return Executors.newSingleThreadExecutor().submit(() -> {
				someService.doSomeWithDelay(requestNumber, delayTime);
			});
		};

//		try {
//			TimeLimiter.of(timeLimiterConfig).executeFutureSupplier(supplier);
//		} catch (Exception e) {
//			fallbackDoSomeWithDelay(requestNumber, delayTime, e);
//		}

		Try.ofCallable(TimeLimiter.decorateFutureSupplier(TimeLimiter.of(timeLimiterConfig), supplier))
				.onFailure(exception -> {
					fallback.fallbackDoSomeWithDelay(requestNumber, delayTime, exception);
				});

	}

}
