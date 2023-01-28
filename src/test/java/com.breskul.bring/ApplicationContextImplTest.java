package com.breskul.bring;

import com.breskul.bring.dump.service.DbService;
import com.breskul.bring.dump.service.PrinterService;
import com.breskul.bring.dump.service.impl.MySqlDbServiceImpl;
import com.breskul.bring.exceptions.NoSuchBeanException;
import com.breskul.bring.exceptions.NoUniqueBeanException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationContextImplTest {

	private ApplicationContext applicationContext;

	@BeforeEach
	void setUp() {
		applicationContext = new AnnotationConfigApplicationContext("com.breskul.bring");
	}

	@Test
	void testGetBeanShouldReturnBean() {
		var result = applicationContext.getBean(PrinterService.class);

		assertNotNull(result);
	}

	@Test
	void testGetBeanByNameShouldReturnBean() {
		DbService result = applicationContext.getBean("db", DbService.class);
		assertNotNull(result);
		assertEquals(result.getClass(), MySqlDbServiceImpl.class);
	}

	@Test
	void testGetAllBeansShouldReturnBean() {
		var result = applicationContext.getAllBeans(DbService.class);
		assertEquals(2, result.size());
	}

	@Test
	void testGetBeanShouldThrowNoSuchBeanException() {
		assertThrows(NoSuchBeanException.class, () -> applicationContext.getBean(String.class));
	}

	@Test
	void testGetBeanByNameShouldThrowNoSuchBeanException() {
		assertThrows(NoSuchBeanException.class, () -> applicationContext.getBean("str", String.class));
	}

	@Test
	void testGetBeanShouldThrowNoUniqueBeanException() {
		assertThrows(NoUniqueBeanException.class, () -> applicationContext.getBean(DbService.class));
	}
}
