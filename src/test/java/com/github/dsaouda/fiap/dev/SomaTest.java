package com.github.dsaouda.fiap.dev;

import static org.junit.Assert.*;

import org.junit.Test;

public class SomaTest {

	@Test
	public void somaDeveSer10Test() {
		
		float resultado = 10.0f;
		Soma soma = new Soma(5, 5);
		
		assertTrue(soma.getClass() == Soma.class);
		assertEquals("5 + 5 = 10", resultado, soma.getResultado(), 0.0f);
	}
	
}
