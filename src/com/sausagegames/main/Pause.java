package com.sausagegames.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Pause {

	public void tick() {
	}

	public void render(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 0, 100));
		g2.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.BOLD, 40));
		g.drawString("JOGO PAUSADO", (Game.WIDTH*Game.SCALE - 260) / 2, (Game.HEIGHT*Game.SCALE) / 2);
	}
}