package com.sausagegames.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

public class Menu {
	
	public int currentOption = 0;

	public boolean up;
	public boolean down;
	
	private List<OptionsMenu> options = new LinkedList<OptionsMenu>();
	private int maxOption;
	
	public Menu() {
		ConfiguraOptionMenu();
	}
	
	public void tick() {
		if(up && Game.frameMenu > 15) {
			currentOption--;
			if(currentOption < 0) {
				currentOption = maxOption;
			}
			Game.frameMenu = 0;
			
		}
		if(down && Game.frameMenu > 15) {
			currentOption++;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
			Game.frameMenu = 0;
		}
	}

	public void render(Graphics g) {
		
		g.setColor(Color.black); 
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.RED);
		g.setFont(new Font("arial", Font.BOLD, 40));
		g.drawString("GUN", (Game.WIDTH*Game.SCALE - 60) / 2, (Game.HEIGHT*Game.SCALE - 300) / 2);
		
		g.setColor(Color.white );
		g.setFont(new Font("arial", Font.BOLD, 30));
		g.drawString("NOVO JOGO", (Game.WIDTH*Game.SCALE - 175) / 2, (Game.HEIGHT*Game.SCALE - 150) / 2);
		g.setFont(new Font("arial", Font.BOLD, 30));
		g.drawString("CARREGAR JOGO", (Game.WIDTH*Game.SCALE - 250) / 2, (Game.HEIGHT*Game.SCALE - 50) / 2);
		g.setFont(new Font("arial", Font.BOLD, 30));
		g.drawString("SAIR", (Game.WIDTH*Game.SCALE - 40) / 2, (Game.HEIGHT*Game.SCALE + 50) / 2);
		
		var current = options.get(currentOption);
		
		g.setColor(Color.yellow );
		g.setFont(new Font("arial", Font.BOLD, 30));
		g.drawString(current.getNome(), current.getX(), current.getY());
	}

	private void ConfiguraOptionMenu() {

		var optionNewGame = new OptionsMenu("NOVO JOGO", (Game.WIDTH*Game.SCALE - 175) / 2, (Game.HEIGHT*Game.SCALE - 150) / 2);
		var optionLoadGame = new OptionsMenu("CARREGAR JOGO", (Game.WIDTH*Game.SCALE - 250) / 2, (Game.HEIGHT*Game.SCALE - 50) / 2);
		var optionExit = new OptionsMenu("SAIR", (Game.WIDTH*Game.SCALE - 40) / 2, (Game.HEIGHT*Game.SCALE + 50) / 2);
		
		options.add(optionNewGame);
		options.add(optionLoadGame);
		options.add(optionExit);
		
		maxOption = options.size() - 1;
		
	}
}
