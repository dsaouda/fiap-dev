package com.github.dsaouda.fiap.dev;

public class Soma {
	
	private float val1;
	private float val2;

	public Soma(float val1, float val2) {
		this.val1 = val1;
		this.val2 = val2;
	}
	
	public float getResultado() {
		return val1 + val2;
	}
	
	public float getVal1() {
		return val1;
	}
	
	public float getVal2() {
		return val2;
	}
}
