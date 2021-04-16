package com.sausagegames.main;

public class OptionsMenu {

	private String Nome;
	
	private int X;
	
	private int Y;
	
	public OptionsMenu(String nome, int x, int y) {
		this.Nome = nome;
		this.X = x;
		this.Y = y;
	}

	public String getNome() {
		return Nome;
	}

	public void setNome(String nome) {
		Nome = nome;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}
}
