package com.breskul.bring.dump.service.impl;

import com.breskul.bring.annotations.Component;
import com.breskul.bring.dump.service.PrinterService;

@Component
public class PrinterServiceImpl implements PrinterService {
	@Override
	public void printHello() {
		System.out.println("Hello!");
	}
}
