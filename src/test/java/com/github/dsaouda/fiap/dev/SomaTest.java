package com.github.dsaouda.fiap.dev;

import static org.junit.Assert.*;

import org.junit.Test;

public class SomaTest {

	@Test
	public void somaDeveSer10Test() {
		
		float resultado = 10.0f;
		Soma soma2 = new Soma(5, 5);
		
		assertEquals("5 + 5 = 10", resultado, soma2.getResultado(), 0.0f);
	}
	
}
