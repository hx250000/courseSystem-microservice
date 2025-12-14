package com.zjgsu.hx.enrollment_service;

import com.zjgsu.hx.enrollment_service.client.UserClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootTest
class enrollmentserviceTests {

	@Autowired
	private ApplicationContext applicationContext;
	@Test
	void contextLoads() {
		String[] beanNames = applicationContext.getBeanNamesForType(UserClient.class);
		System.out.println("===== UserClient Beans =====");
		for (String name : beanNames) {
			System.out.println(name);
		}
	}

}
