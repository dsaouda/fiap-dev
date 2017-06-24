package com.github.dsaouda.fiap.dev;

import static org.junit.Assert.*;

import org.junit.Test;

public class SomaTest {

	@Test
	public void somaDeveSer10Test() {
		
		int resultado = 10;
		Soma soma = new Soma(5, 5);
		
		assertTrue(soma instanceof Soma);
		assertEquals(5, soma.getVal1());
		assertEquals(5, soma.getVal2());
		assertEquals(resultado, new Soma(5,5).getResultado());
	}
	
}
