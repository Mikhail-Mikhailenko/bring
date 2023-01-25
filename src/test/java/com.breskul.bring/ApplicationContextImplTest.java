package com.breskul.bring;

import com.breskul.bring.dump.service.DbService;
import com.breskul.bring.dump.service.PrinterService;
import com.breskul.bring.exceptions.NoSuchBeanException;
import com.breskul.bring.exceptions.NoUniqueBeanException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationContextImplTest {

	private ApplicationContext applicationContextImplUnderTest;

	@BeforeEach
	void setUp() {
		applicationContextImplUnderTest = new AnnotationConfigApplicationContext("com.breskul.bring.dump");
	}

	@Test
	void testGetBeanShouldReturnBean() {
		var result = applicationContextImplUnderTest.getBean(PrinterService.class);

		assertNotNull(result);
	}

	@Test
	void testGetBeanByNameShouldReturnBean() {
		var result = applicationContextImplUnderTest.getBean("db", DbService.class);
		assertNotNull(result);
	}

	@Test
	void testGetAllBeansShouldReturnBean() {
		var result = applicationContextImplUnderTest.getAllBeans(DbService.class);
		assertEquals(2, result.size());
	}

	@Test
	void testGetBeanShouldThrowNoSuchBeanException() {
		assertThrows(NoSuchBeanException.class, () -> applicationContextImplUnderTest.getBean(String.class));
	}

	@Test
	void testGetBeanByNameShouldThrowNoSuchBeanException() {
		assertThrows(NoSuchBeanException.class, () -> applicationContextImplUnderTest.getBean("str", String.class));
	}

	@Test
	void testGetBeanShouldThrowNoUniqueBeanException() {
		assertThrows(NoUniqueBeanException.class, () -> applicationContextImplUnderTest.getBean(DbService.class));
	}
}
