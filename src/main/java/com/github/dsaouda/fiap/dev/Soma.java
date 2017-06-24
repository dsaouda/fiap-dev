package com.github.dsaouda.fiap.dev;

public class Soma {
	
	private int val1;
	private int val2;

	public Soma(int val1, int val2) {
		this.val1 = val1;
		this.val2 = val2;
	}
	
	public int getResultado() {
		return val1 + val2;
	}
	
	public int getVal1() {
		return val1;
	}
	
	public int getVal2() {
		return val2;
	}
}
