package com.breskul.bring.dump.service.impl;

import com.breskul.bring.annotations.Component;
import com.breskul.bring.dump.service.DbService;

@Component
public class OracleDbServiceImpl implements DbService {
	@Override
	public void connect() {
		System.out.println("connect to oracle");
	}
}
