package com.example.demoresilience4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.demoresilience4j.request.utils.SimulateTimeout;

@SpringBootApplication
public class DemoResilience4jApplication {

	public static void main(String[] args) {
//		SpringApplication.run(DemoResilience4jApplication.class, args);

		ApplicationContext ctx = SpringApplication.run(DemoResilience4jApplication.class, args);
		Api api = ctx.getBean(Api.class);
		DemoResilience4jApplication demo = new DemoResilience4jApplication();

		SimulateTimeout.simulate(api);

	}

}
